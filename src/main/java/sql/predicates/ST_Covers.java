package sql.predicates;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;

@SuppressWarnings("checkstyle:TypeName")
public class ST_Covers extends ScalarFunction {
    public static boolean eval(@DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry geom1,
                        @DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry geom2) {
        return geom1.covers(geom2);

    }
}
