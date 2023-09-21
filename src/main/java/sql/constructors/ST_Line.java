package sql.constructors;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.List;

@SuppressWarnings("checkstyle:TypeName")
public class ST_Line extends ScalarFunction {
    public static GeometryFactory factory = new GeometryFactory();

    public static @DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry eval(List<Point> points) {
        return factory
                .createLineString(points.stream()
                        .map(Point::getCoordinate)
                        .toArray(Coordinate[]::new));
    }
    /*public static @DataTypeHint(value = "RAW", bridgedTo = LineString.class) LineString eval(List<Point> points) {
        return factory
                .createLineString(points.stream()
                        .map(Point::getCoordinate)
                        .toArray(Coordinate[]::new));
    }*/
}
