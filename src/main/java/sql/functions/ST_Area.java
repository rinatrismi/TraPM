package sql.functions;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;

@SuppressWarnings("checkstyle:TypeName")
public class ST_Area extends ScalarFunction {
    public static double eval(@DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry g) {
        return g.getArea();
    }
}
