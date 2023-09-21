package sql.constructors;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

@SuppressWarnings("checkstyle:TypeName")
public class ST_Polygon extends ScalarFunction {
    public static GeometryFactory factory = new GeometryFactory();
    public static WKTReader wktReader = new WKTReader(factory);

    public @DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry eval(@DataTypeHint(value = "RAW", bridgedTo = LineString.class) LineString shell) {
        return factory.createPolygon(shell.getCoordinates());
    }
    public @DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry eval(String wkt)
            throws ParseException {
        return wktReader.read(wkt);
    }

    public @DataTypeHint(value = "RAW", bridgedTo = Polygon.class) Polygon eval(@DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry g)
            throws ParseException {
        return (Polygon) g;
    }
    /*public @DataTypeHint(value = "RAW", bridgedTo = Polygon.class) Polygon eval(@DataTypeHint(value = "RAW", bridgedTo = LineString.class) LineString shell) {
        return factory.createPolygon(shell.getCoordinates());
    }
    public @DataTypeHint(value = "RAW", bridgedTo = Polygon.class) Polygon eval(String wkt)
            throws ParseException {
        return (Polygon) wktReader.read(wkt);
    }*/

}
