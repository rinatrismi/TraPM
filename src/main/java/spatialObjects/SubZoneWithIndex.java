package spatialObjects;

import spatialIndices.UniformGrid;

import java.util.HashSet;

public class SubZoneWithIndex {
    public Subzone subzone;
    public HashSet<String> gridId;

    public SubZoneWithIndex(Subzone subzone) {
        this.subzone = subzone;
        this.gridId = UniformGrid.getGridIndex(subzone);
    }

    public HashSet<String> getGridId() {
        return gridId;
    }

    public Subzone getSubzone() {
        return subzone;
    }

    public void setSubzone(Subzone subzone) {
        this.subzone = subzone;
    }

    public void setGridId(HashSet<String> gridId) {
        this.gridId = gridId;
    }

    @Override
    public String toString() {
        return "SubZoneWithIndex{"+
                "subzone=" + subzone +
                ", gridId='" + gridId + '\'' +
                '}';
    }
}
