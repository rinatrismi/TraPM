package spatialIndices;

public class ListRTreePOIIndex {
    static RTreePOI poiIndex;

    public ListRTreePOIIndex(){}

    public static RTreePOI getIndexList() {
        return poiIndex;
    }

    public static void setIndexList(RTreePOI poiIndex) {
        ListRTreePOIIndex.poiIndex = poiIndex;
    }

    @Override
    public String toString() {
        return "ListRTreePOIIndex{\n" + poiIndex + "\n}";
    }
}
