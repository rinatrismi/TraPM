package sql.constructors;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

@SuppressWarnings("checkstyle:TypeName")
public class ST_LineFromText extends ScalarFunction {
    public static GeometryFactory geometryFactory = new GeometryFactory();
    public static WKTReader wktReader = new WKTReader(geometryFactory);


    @DataTypeHint(value = "RAW", bridgedTo = Geometry.class)
    public static Geometry eval(String wkt) throws ParseException {
        return wktReader.read(wkt);
    }

   /* @DataTypeHint(value = "RAW", bridgedTo = LineString.class)
    public static LineString eval(String wkt) throws ParseException {
        return (LineString) wktReader.read(wkt);
    }*/
}
