package sql.constructors;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Table API / SQL Scalar UDF to convert from lon lat to point
 */

@SuppressWarnings("checkstyle:TypeName")
public class ST_Point extends ScalarFunction {
    public static GeometryFactory factory = new GeometryFactory();

    public static @DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry eval(Double x, Double y) {
        try {
            return factory.createPoint(new Coordinate(x, y));
        } catch (Exception e) {
            return null;
        }
    }

/*    public static @DataTypeHint(value = "RAW", bridgedTo = Point.class) Point eval(Double x, Double y) {
            try {
                return factory.createPoint(new Coordinate(x, y));
            } catch (Exception e) {
                return null;
            }
    }*/
   }
