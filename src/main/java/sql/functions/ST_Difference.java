package sql.functions;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

@SuppressWarnings("checkstyle:TypeName")
public class ST_Difference extends ScalarFunction {
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();
    private static final Geometry EMPTY_POLYGON = GEOMETRY_FACTORY.createPolygon(null, null);
    @DataTypeHint(value = "RAW", bridgedTo = Geometry.class)
    public static Geometry eval(@DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry geom1,
                        @DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry geom2) {
        boolean isIntersects = geom1.intersects(geom2);
        if (!isIntersects) {
            return geom1;
        } else if (geom2.contains(geom1)) {
            return EMPTY_POLYGON;
        } else {
            return geom1.difference(geom2);
        }
    }
}
