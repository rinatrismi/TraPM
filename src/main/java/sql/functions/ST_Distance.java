package sql.functions;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;

@SuppressWarnings("checkstyle:TypeName")
public class ST_Distance extends ScalarFunction {
    public static double eval (@DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry geom1,
                        @DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry geom2) {
       /* Point p1 = geom1.getCentroid();
        Point p2 = geom2.getCentroid();
        return getEuclideanDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY());*/
        return geom1.distance(geom2);
    }
}
