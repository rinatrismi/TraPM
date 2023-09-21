package sql;

import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import sql.constructors.*;
//import sql.custom.*;
import sql.functions.*;
import sql.outputs.*;
import sql.predicates.*;


public class SQLRegister {
    public static void registerUDF(StreamTableEnvironment tableEnv) {
        tableEnv.createTemporaryFunction("ST_Point", ST_Point.class);
        tableEnv.createTemporaryFunction("ST_PointFromText", ST_PointFromText.class);
        tableEnv.createTemporaryFunction("ST_PointFromWKB", ST_PointFromWKB.class);
        tableEnv.createTemporaryFunction("ST_GeomFromText", ST_GeomFromText.class);
        tableEnv.createTemporaryFunction("ST_GeomFromWKT", ST_GeomFromWKT.class);
        tableEnv.createTemporaryFunction("ST_GeomFromWKB", ST_GeomFromWKB.class);
        tableEnv.createTemporaryFunction("ST_LineFromText", ST_LineFromText.class);
        tableEnv.createTemporaryFunction("ST_PolygonFromText", ST_PolygonFromText.class);
        tableEnv.createTemporaryFunction("ST_AsText", ST_AsText.class);
        tableEnv.createTemporaryFunction("ST_AsBinary", ST_AsBinary.class);
        tableEnv.createTemporaryFunction("ST_Area", ST_Area.class);
        tableEnv.createTemporaryFunction("ST_Boundary", ST_Boundary.class);
        tableEnv.createTemporaryFunction("ST_Buffer", ST_Buffer.class);
        tableEnv.createTemporaryFunction("ST_Centroid", ST_Centroid.class);
        tableEnv.createTemporaryFunction("ST_ConvexHull", ST_ConvexHull.class);
        tableEnv.createTemporaryFunction("ST_Difference", ST_Difference.class);
        tableEnv.createTemporaryFunction("ST_Distance", ST_Distance.class);
        tableEnv.createTemporaryFunction("ST_Envelope", ST_Envelope.class);
        tableEnv.createTemporaryFunction("ST_Intersection", ST_Intersection.class);
        tableEnv.createTemporaryFunction("ST_Length", ST_Length.class);
        tableEnv.createTemporaryFunction("ST_Union", ST_Union.class);
        tableEnv.createTemporaryFunction("IsEmpty", IsEmpty.class);
        tableEnv.createTemporaryFunction("ST_Contains", ST_Contains.class);
        tableEnv.createTemporaryFunction("ST_CoveredBy", ST_CoveredBy.class);
        tableEnv.createTemporaryFunction("ST_Covers", ST_Covers.class);
        tableEnv.createTemporaryFunction("ST_Crosses", ST_Crosses.class);
        tableEnv.createTemporaryFunction("ST_Disjoint", ST_Disjoint.class);
        tableEnv.createTemporaryFunction("ST_Equals", ST_Equals.class);
        tableEnv.createTemporaryFunction("ST_Intersects", ST_Intersects.class);
        tableEnv.createTemporaryFunction("ST_Overlaps", ST_Overlaps.class);
        tableEnv.createTemporaryFunction("ST_Touches", ST_Touches.class);
        tableEnv.createTemporaryFunction("ST_Within", ST_Within.class);
        //tableEnv.createTemporaryFunction("IsNear", IsNear.class);
        //tableEnv.createTemporaryFunction("IsNearPOI", IsNearPOI.class);
        //tableEnv.createTemporaryFunction("ST_Within_Subzone", ST_Within_Subzone.class);
        //tableEnv.createTemporaryFunction("IsNearPOIGridIndex", IsNearPOIGridIndex.class);
        //tableEnv.createTemporaryFunction("IsNearPOIWithoutIndex", IsNearPOIWithoutIndex.class);
        //tableEnv.createTemporaryFunction("ST_Within_GridIndex", ST_Within_GridIndex.class);
        //tableEnv.createTemporaryFunction("ST_Within_WithoutIndex", ST_Within_WithoutIndex.class);
    }
}
