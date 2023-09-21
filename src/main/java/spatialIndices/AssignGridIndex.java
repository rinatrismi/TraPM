package spatialIndices;

import org.apache.flink.api.java.tuple.Tuple2;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import spatialObjects.PointOfInterest;
import spatialObjects.Subzone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AssignGridIndex {

    public static List<Tuple2<HashSet<String>, Subzone>> subzoneGridIndex(List<Subzone> subzone) {
        List<Tuple2<HashSet<String>, Subzone>> subzoneWithGridIndex = new ArrayList<>();
        for(int i = 0; i<subzone.size(); i++){
            HashSet<String> index = UniformGrid.getGridIndex((Polygon) (subzone.get(i).getGeom()));
            subzoneWithGridIndex.add(new Tuple2<>(index, subzone.get(i)));
        }
        return subzoneWithGridIndex;

    }

    public static HashSet<String> subzoneGridIndex(Subzone subzone) {
        return UniformGrid.getGridIndex((Polygon) (subzone.getGeom()));
    }

    public static List<Tuple2<String, PointOfInterest>> POIGridIndex(List<PointOfInterest> poi) {
        List<Tuple2<String, PointOfInterest>> poiWithIndex = new ArrayList<>();

        for(int i = 0; i<poi.size(); i++){
            String index = UniformGrid.getGridIndex((Point)poi.get(i).getGeom());
            poiWithIndex.add(new Tuple2<>(index, poi.get(i)));
        }
        return poiWithIndex;
    }
}
