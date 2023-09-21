package spatialObjects;

import spatialIndices.UniformGrid;

public class POIWithIndex {
    PointOfInterest poi;
    String gridId;

    public POIWithIndex(PointOfInterest poi) {
        this.poi = poi;
        this.gridId = UniformGrid.getGridIndex(poi);
    }

    public PointOfInterest getPoi() {
        return poi;
    }

    public void setPoi(PointOfInterest poi) {
        this.poi = poi;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    @Override
    public String toString() {
        return "POIWithIndex{" +
                "poi=" + poi +
                ", gridId='" + gridId + '\'' +
                '}';
    }
}
