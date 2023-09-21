package sql.functions;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;

@SuppressWarnings("checkstyle:TypeName")
public class ST_Union extends ScalarFunction {
    @DataTypeHint(value = "RAW", bridgedTo = Geometry.class)
    public static Geometry eval(@DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry geom1,
                                @DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry geom2) {
        return geom1.union(geom2);
    }
}
