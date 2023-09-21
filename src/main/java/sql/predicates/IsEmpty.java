package sql.predicates;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;

@SuppressWarnings("checkstyle:TypeName")
public class IsEmpty extends ScalarFunction {
    public static boolean eval(@DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Object o) {
        Geometry geom = (Geometry) o;
        return geom.isEmpty();
    }
}
