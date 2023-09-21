package spatialIndices;

public class ListRTreeSubzoneIndex {
    static RTreeSubzone subzoneIndex;

    public ListRTreeSubzoneIndex(){}

    public static RTreeSubzone getIndexList() {
        return subzoneIndex;
    }

    public static void setIndexList(RTreeSubzone subzoneIndex) {
        ListRTreeSubzoneIndex.subzoneIndex = subzoneIndex;
    }

    @Override
    public String toString() {
        return "ListRTreeSubzoneIndex{\n" + subzoneIndex + "\n}";
    }
}
