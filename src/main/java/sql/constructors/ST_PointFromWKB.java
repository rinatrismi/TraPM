package sql.constructors;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;

@SuppressWarnings("checkstyle:TypeName")
public class ST_PointFromWKB extends ScalarFunction {
    private transient WKBReader wkbReader;

    @Override
    public void open(FunctionContext context) {
        wkbReader = new WKBReader();
    }

    @DataTypeHint(value = "RAW", bridgedTo = Geometry.class)
    public Geometry eval(byte[] wkb) throws ParseException {
        return wkbReader.read(wkb);
    }

   /* @DataTypeHint(value = "RAW", bridgedTo = Point.class)
    public Point eval(byte[] wkb) throws ParseException {
        return (Point) wkbReader.read(wkb);
    }*/
}
