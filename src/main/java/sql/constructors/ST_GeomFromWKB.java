package sql.constructors;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;

@SuppressWarnings("checkstyle:TypeName")
public class ST_GeomFromWKB extends ScalarFunction {
    public static GeometryFactory geometryFactory = new GeometryFactory();
    public static WKBReader wkbReader = new WKBReader(geometryFactory);
    public static @DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry eval(byte[] wkb)
            throws ParseException {
        return wkbReader.read(wkb);
    }
}
