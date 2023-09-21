package spatialIndices;

import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Rectangle;
import geoUtils.GeometricDistanceCalculator;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;

public class RTreePOI <PointOfInterest extends Geometry> {
    private static final int DEFAULT_NODE_CAPACITY = 3;

    public RTree<PointOfInterest, com.github.davidmoten.rtree.geometry.Geometry> rTree;

    public RTreePOI() {
        this(DEFAULT_NODE_CAPACITY);
    }

    public RTreePOI(int nodeCapacity) {
        if (nodeCapacity < 3)
            throw new IllegalArgumentException("Node capacity of R-Tree must greater than 2");
        rTree = RTree.maxChildren(nodeCapacity).create();
    }

    public RTreePOI(int minNode, int maxNode) {
        if (minNode < 3)
            throw new IllegalArgumentException("Node capacity of R-Tree must greater than 2");
        rTree = RTree.minChildren(minNode).maxChildren(maxNode).create();
    }

    public void insert(List<PointOfInterest> geometries) {
        geometries.forEach(this::insertGeom);
    }

    public void insert(PointOfInterest poi) {
        insertGeom(poi);
    }

    public int size() {
        return rTree.size();
    }

    private void insertGeom(PointOfInterest poi) {
        Envelope box = poi.getEnvelopeInternal();
        // Node<T> node = new Node<>(geom);
        rTree = rTree.add(poi, envelopeToRect(box));
    }

    public List<PointOfInterest> query(Envelope envelope) {
        List<PointOfInterest> result = new ArrayList<>();
        rTree.search(envelopeToRect(envelope))
                .toBlocking()
                .toIterable()
                .forEach(item -> result.add(item.value()));
        return result;
    }

    public List<PointOfInterest> query(Geometry geometry) {
        return query(geometry.getEnvelopeInternal());
    }

    public List<PointOfInterest> query(Geometry geom, double distance) {
        //  Point point = geom instanceof Point ? (Point) geom : geom.getCentroid();
        Envelope box = GeometricDistanceCalculator.calcBoxByDist(geom, distance);
        List<PointOfInterest> result = new ArrayList<>();
        rTree.search(envelopeToRect(box))
                .toBlocking()
                .toIterable()
                .forEach(item -> {
                    if (GeometricDistanceCalculator.calcDistance(geom, item.value()) <= distance)
                        result.add(item.value());
                });
        return result;
    }

    private static Rectangle envelopeToRect(Envelope box) {
        return Geometries.rectangle(box.getMinX(), box.getMinY(), box.getMaxX(), box.getMaxY());
    }

    public String toString() {
        return rTree.asString();
    }
}
