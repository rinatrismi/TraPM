package sql.functions;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

@SuppressWarnings("checkstyle:TypeName")
public class ST_Intersection extends ScalarFunction {
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();
    private static final Geometry EMPTY_POLYGON = GEOMETRY_FACTORY.createPolygon(null, null);

    @DataTypeHint(value = "RAW", bridgedTo = Geometry.class)
    public static Geometry eval(@DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry leftGeom,
                                @DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry rightGeom){
        boolean isIntersects = leftGeom.intersects(rightGeom);
        if (!isIntersects) {
            return EMPTY_POLYGON;
        }
        if (leftGeom.contains(rightGeom)) {
            return rightGeom;
        }
        if (rightGeom.contains(leftGeom)) {
            return leftGeom;
        }
        return leftGeom.intersection(rightGeom);
    }
}
