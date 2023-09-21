package spatialIndices;

import geoUtils.GeometricDistanceCalculator;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.index.strtree.STRtree;

import java.util.List;

public class RTreeJTSPOI <PointOfInterest extends Geometry>{
    private static final int DEFAULT_NODE_CAPACITY = 3;
    private final STRtree stRtree;

    public RTreeJTSPOI() {
        stRtree = new STRtree(DEFAULT_NODE_CAPACITY);
    }

    public RTreeJTSPOI(int nodeCapacity) {
        stRtree = new STRtree(nodeCapacity);
    }

    public void insert(List<PointOfInterest> geometries) {
        geometries.forEach(this::insert);
    }


    public void insert(PointOfInterest geom) {
        stRtree.insert(geom.getEnvelopeInternal(), geom);
    }


    public List query(Envelope envelope) {
        return stRtree.query(envelope);
    }


    public List query(Geometry geometry) {
        return query(geometry.getEnvelopeInternal());
    }

    public List<PointOfInterest> query(Geometry geometry, double distance) {
        Point point = geometry instanceof Point ? (Point) geometry : geometry.getCentroid();
        Envelope envelope = GeometricDistanceCalculator.calcBoxByDist(point, distance);
        List<PointOfInterest> result = stRtree.query(envelope);
        result.removeIf(geom -> GeometricDistanceCalculator.calcDistance(point, geom) > distance);
        return result;
    }

    /**
     * STRTree use `==` to just the equality of objects int the tree,
     * so only support for removing object with the same address.
     * */

    public void remove(PointOfInterest geom) {
        stRtree.remove(geom.getEnvelopeInternal(), geom);
    }


    public int size() {
        return stRtree.size();
    }


    public String toString() {
        return stRtree.toString();
    }

}
