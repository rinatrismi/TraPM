package sql.functions;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;

@SuppressWarnings("checkstyle:TypeName")
public class ST_Buffer extends ScalarFunction {
    @DataTypeHint(value = "RAW", bridgedTo = Geometry.class)
    public static Geometry eval(@DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Object o, double radius) {
        Geometry geom = (Geometry) o;
        return geom.buffer(radius);
    }
}
