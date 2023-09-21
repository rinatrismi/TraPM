package spatialObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class POIPool {
    static List<POIWithIndex> listPOIWithIndex;

    public POIPool(){
        POIPool.listPOIWithIndex = new ArrayList<>();
    };

    public static List<POIWithIndex> getIndexList() {
        return listPOIWithIndex;
    }
    public static List<PointOfInterest> getPOIList() {
        return listPOIWithIndex.stream().map(x -> x.getPoi()).collect(Collectors.toList());
    }

    public static void setListPOI(List<POIWithIndex> listPOI) {
        POIPool.listPOIWithIndex = listPOI;
    }

    public static void addPOI(POIWithIndex poiWithIndex){
        POIPool.listPOIWithIndex.add(poiWithIndex);
    }

    @Override
    public String toString() {
        return "POIPool{"+listPOIWithIndex+"}";
    }


}
