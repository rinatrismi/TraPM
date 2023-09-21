import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import spatialIndices.*;
import spatialObjects.*;
import sql.SQLRegister;

import java.util.List;
import java.util.stream.Collectors;

public class TraPMJob {
    public static void main(String[] args) throws Exception {
        String subzonePath = "/Users/rinatrisminingsih/Intellij-workspace/spatial-rpr/src/main/resources/subzone.geojson";
        String POIPath = "/Users/rinatrisminingsih/Intellij-workspace/spatial-rpr/src/main/resources/points_of_interest.json";
        Configuration config = new Configuration();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config);
        //final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ExecutionConfig executionConfig = env.getConfig();
        executionConfig.setAutoWatermarkInterval(1000L);
        env.setParallelism(1);
        env.enableCheckpointing(100000);

        //Create environment of Stream Table API
        final StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        /* =========================== Grid Index Definition ======================================== */
        // Boundaries of Singapore
        double minX = 103.6;     //X - East-West longitude
        double maxX = 104.1;
        double minY = 1.21;     //Y - North-South latitude
        double maxY = 1.47;

        //Size of Uniform Grid 100 x 100
        int uniformGridSize = 100;

        // Defining Grid
        UniformGrid uGrid = new UniformGrid(minX, maxX, minY, maxY, uniformGridSize);

        //System.out.println(uGrid.getCellLength());

        /* =========================== Subzone ======================================== */

        /* ===============================Taxi Ride Stream Definition==================================== */

        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("GrabTaxi1")
                .setGroupId("test-consumer-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        DataStream<String> RawStream  = env.fromSource(source, WatermarkStrategy.forMonotonousTimestamps(), "Kafka Source");

        // Deserialize String stream into TaxiRide stream
        DataStream<TaxiRide> RideStream = Deserialization.TrajectoryStream(RawStream, "CSV", ",").rebalance();

        //Keyed stream based on trjId using keyby

        KeyedStream<TaxiRide, Tuple> KeyRideStream = RideStream.keyBy("trjId");

        // interpret the insert-only DataStream as a Table
        Table rideTable = tableEnv.fromDataStream(KeyRideStream,
                Schema.newBuilder()
                        .column("trjId", DataTypes.BIGINT())
                        .column("drivingMode", DataTypes.STRING())
                        .column("osName", DataTypes.STRING())
                        //.columnByExpression("time","TO_TIMESTAMP(FROM_UNIX(eventTime))")
                        .column("eventTime", DataTypes.BIGINT())
                        .column("lon", DataTypes.DOUBLE())
                        .column("lat", DataTypes.DOUBLE())
                        .column("speed", DataTypes.DOUBLE())
                        .column("bearing", DataTypes.DOUBLE())
                        .column("accuracy", DataTypes.DOUBLE())
                        .columnByExpression("rowtime","TO_TIMESTAMP_LTZ(eventTime, 0)")
                        .columnByExpression("proc_time", "PROCTIME()")
                        .watermark("rowtime", "rowtime - INTERVAL '5' SECONDS")
                        .build());

        // register the Table object as a view and query it
        tableEnv.createTemporaryView("TaxiRides", rideTable);

        /* =========================== Point of Interest ======================================== */
        List<PointOfInterest> poix = Deserialization.jsonToPOI(POIPath);
        List<PointOfInterest> poi = poix.subList(0, 100);

        //POI: school
        List<PointOfInterest> school = poi.stream().filter(p->p.getAttr().getCategory().equalsIgnoreCase("school")).collect(Collectors.toList());

        //Assign RTree Index
        RTreePOI<PointOfInterest> poiRTreeIndex = new RTreePOI<>();
        poiRTreeIndex.insert(poi);

        //System.out.println(poiRTreeIndex);

        RTreePOI<PointOfInterest> schoolRTreeIndex = new RTreePOI<>();
        schoolRTreeIndex.insert(school);

        //List of RTree Index of POI
        ListRTreePOIIndex ListPOIRTree = new ListRTreePOIIndex();
        ListPOIRTree.setIndexList(poiRTreeIndex);

       // System.out.println(ListPOIRTree);

        //Assign Grid Index
        POIPool poiPool = new POIPool();
        for(PointOfInterest p : poi){
            poiPool.addPOI(new POIWithIndex(p));
        }

        /* =========================== Subzone ======================================== */
        List<Subzone> subzonex = Deserialization.geoJsonToSubzone(subzonePath);
        List<Subzone> subzone = subzonex.subList(0, 1);

        //Assign RTree Index
        RTreeSubzone<Subzone> subzoneRTreeIndex = new RTreeSubzone<>();
        subzoneRTreeIndex.insert(subzone);

        //List of RTree Index of Subzone
        ListRTreeSubzoneIndex ListSubzoneRTree = new ListRTreeSubzoneIndex();
        ListRTreeSubzoneIndex.setIndexList(subzoneRTreeIndex);

        //Assign Grid Index
        SubzonePool subzones = new SubzonePool();
        for(Subzone s : subzone){
            subzones.addSubzone(new SubZoneWithIndex(s));
        }
        /* ============================== TABLE SQL ===================================== */
        SQLRegister.registerUDF(tableEnv);

        //Table result = tableEnv.sqlQuery(("SELECT trjId, CAST(rowtime AS TIMESTAMP) AS t1, window_start, window_end, window_time from TABLE(TUMBLE(TABLE TaxiRides, DESCRIPTOR(rowtime), INTERVAL '15' MINUTES)) where trjId = 75567 order by rowtime"));

        Table result = tableEnv.sqlQuery(("SELECT * from TaxiRides where trjId = 45277 order by rowtime"));

        Table duration = tableEnv.sqlQuery(("SELECT trjId, max(eventTime), min (eventTime), (max(eventTime) - min (eventTime))/60 as duration from TaxiRides group by trjId"));

        Table rpr = tableEnv.sqlQuery("SELECT * \n" +
                "FROM TaxiRides \n" +
                "MATCH_RECOGNIZE (\n" +
                "  PARTITION BY trjId\n" +
                "  ORDER BY proc_time\n" +
                "  MEASURES \n" +
                "    A.`speed` AS speed, \n" +
                "    B.`bearing` AS bearing\n" +
                "  AFTER MATCH SKIP PAST LAST ROW\n" +
                "  PATTERN (A B C) WITHIN INTERVAL '10' MINUTES" +
                "  DEFINE\n" +
                "   A AS A.`speed` < 5,\n" +
                // "   B AS ST_Within_WithoutIndex(ST_Point(B.`lon`, B.`lat`)),\n" +
                 "   B AS B.`bearing` > 200,\n" +
                "   C AS true\n" +
                ")");

        Table rprRTree = tableEnv.sqlQuery("SELECT count(*) \n" +
                "FROM TaxiRides \n" +
                "MATCH_RECOGNIZE (\n" +
                "  PARTITION BY trjId\n" +
                "  ORDER BY rowtime\n" +
                "  MEASURES \n" +
                "    A.`eventTime` AS startT, \n" +
                "    D.`eventTime` AS endT\n" +
                "  AFTER MATCH SKIP PAST LAST ROW\n" +
                "  PATTERN (A B C D) WITHIN INTERVAL '30' MINUTES\n" +
                "  DEFINE\n" +
               "   A AS IsNearPOI(ST_Point(A.`lon`, A.`lat`), 0.01),\n" +
                "   B AS ST_Within_S ubzone(ST_Point(B.`lon`, B.`lat`)),\n" +
                "   C AS C.`bearing` > 100,\n" +
                "   D AS true\n" +
                ")");

        Table rprGridIndex = tableEnv.sqlQuery("SELECT count(*) \n" +
                "FROM TaxiRides \n" +
                "MATCH_RECOGNIZE (\n" +
                "  PARTITION BY trjId\n" +
                "  ORDER BY rowtime\n" +
                "  MEASURES \n" +
                "    A.`eventTime` AS startT, \n" +
                "    D.`eventTime` AS endT\n" +
                "  AFTER MATCH SKIP PAST LAST ROW\n" +
                "  PATTERN (A B C D) WITHIN INTERVAL '30' MINUTES\n" +
                "  DEFINE\n" +
                "   A AS IsNearPOIGridIndex(ST_Point(A.`lon`, A.`lat`), 0.01),\n" +
                "   B AS ST_Within_GridIndex(ST_Point(B.`lon`, B.`lat`)),\n" +
                "   C AS C.`bearing` > 100,\n" +
                "   D AS true\n" +
                ")");

        Table rprWithoutIndex = tableEnv.sqlQuery("SELECT count(*) \n" +
                "FROM TaxiRides \n" +
                "MATCH_RECOGNIZE (\n" +
                "  PARTITION BY trjId\n" +
                "  ORDER BY rowtime\n" +
                "  MEASURES \n" +
                "    A.`eventTime` AS startT, \n" +
                "    D.`eventTime` AS endT\n" +
                "  AFTER MATCH SKIP PAST LAST ROW\n" +
                "  PATTERN (A B C D) WITHIN INTERVAL '30' MINUTES\n" +
                "  DEFINE\n" +
                "   A AS IsNearPOIWithoutIndex(ST_Point(A.`lon`, A.`lat`), 0.01),\n" +
                "   B AS ST_Within_WithoutIndex(ST_Point(B.`lon`, B.`lat`)),\n" +
                "   C AS C.`bearing` > 100,\n" +
                "   D AS true\n" +
                ")");

        // interpret the insert-only Table as a DataStream again
      // DataStream<Row> resultStream = tableEnv.toDataStream(rprRTree);
        	tableEnv.toRetractStream(duration, Row.class).print();
        //resultStream.print();

        env.execute("Spatial RPR Job");

    }
}
