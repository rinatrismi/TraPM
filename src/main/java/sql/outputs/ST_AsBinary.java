package sql.outputs;


import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKBWriter;

@SuppressWarnings("checkstyle:TypeName")
public class ST_AsBinary extends ScalarFunction {

    public static GeometryFactory geometryFactory = new GeometryFactory();
    public static WKBWriter wkbWriter = new WKBWriter();

    public static  byte[] eval(@DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry geom) {
        return wkbWriter.write(geom);
    }

}
