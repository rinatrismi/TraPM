package sql.outputs;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;

@SuppressWarnings("checkstyle:TypeName")
//example: ST_AsText(ST_Point(1,2))
public class ST_AsText extends ScalarFunction {
    public static WKTWriter wktWriter = new WKTWriter();

    public String eval(@DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry geom) {
        return wktWriter.write(geom);
    }
}
