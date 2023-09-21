package spatialIndices;

import spatialObjects.PointOfInterest;

import java.util.ArrayList;
import java.util.List;

public class ListPOI {
    static List<PointOfInterest> listPOI = new ArrayList<>();

    public ListPOI() {
    }

    public static List<PointOfInterest> getPoi() {
        return listPOI;
    }

    public static void setListPoi(List<PointOfInterest> poi) {
        ListPOI.listPOI = poi;
    }

    public static void addPOI(PointOfInterest poi) {
        ListPOI.listPOI.add(poi);
    }


    @Override
    public String toString() {
        return "ListPOI{}";
    }
}
