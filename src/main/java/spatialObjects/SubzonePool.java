package spatialObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubzonePool {
    static List<SubZoneWithIndex> subZoneWithIndexList;

    public SubzonePool(){
        SubzonePool.subZoneWithIndexList = new ArrayList<>();
    };

    public static List<SubZoneWithIndex> getIndexList() {
        return subZoneWithIndexList;
    }

    public static List<Subzone> getSubzoneList() {
        return subZoneWithIndexList.stream().map(x -> x.getSubzone()).collect(Collectors.toList());
    }


    public static void setListSubzone(List<SubZoneWithIndex> listSubzone) {
        SubzonePool.subZoneWithIndexList = listSubzone;
    }

    public static void addSubzone(SubZoneWithIndex subZoneWithIndex){
        SubzonePool.subZoneWithIndexList.add(subZoneWithIndex);
    }

    @Override
    public String toString() {
        return "SubzonePool{"+subZoneWithIndexList+"}";
    }
}
