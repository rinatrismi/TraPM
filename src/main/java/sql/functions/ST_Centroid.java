package sql.functions;


import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;

@SuppressWarnings("checkstyle:TypeName")
//example: ST_Centroid(ST_GeomFromText('polygon ((0 0, 3 6, 6 0, 0 0))'))
public class ST_Centroid extends ScalarFunction {
    @DataTypeHint(value = "RAW", bridgedTo = Geometry.class)
    public static Geometry eval(@DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Object o) {
        Geometry geom = (Geometry) o;
        return geom.getCentroid();
    }
}
