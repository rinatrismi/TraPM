package spatialIndices;

import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import geoUtils.GeometricDistanceCalculator;
import org.locationtech.jts.geom.Envelope;

import java.util.ArrayList;
import java.util.List;

public class RTreeSubzone<Subzone extends org.locationtech.jts.geom.Geometry>  {
    private static final int DEFAULT_NODE_CAPACITY = 3;

    public RTree<Subzone, Geometry> rTree;

    public RTreeSubzone() {
        this(DEFAULT_NODE_CAPACITY);
    }

    public RTreeSubzone(int nodeCapacity) {
        if (nodeCapacity < 3)
            throw new IllegalArgumentException("Node capacity of R-Tree must greater than 2");
        rTree = RTree.maxChildren(nodeCapacity).create();
    }

    public RTreeSubzone(int minNode, int maxNode) {
        if (minNode < 3)
            throw new IllegalArgumentException("Node capacity of R-Tree must greater than 2");
        rTree = RTree.minChildren(minNode).maxChildren(maxNode).create();
    }

    public void insert(List<Subzone> geometries) {
        geometries.forEach(this::insertGeom);
    }

    public void insert(Subzone subzone) {
        insertGeom(subzone);
    }

    public int size() {
        return rTree.size();
    }

    private void insertGeom(Subzone subzone) {
        Envelope box = subzone.getEnvelopeInternal();
        // Node<T> node = new Node<>(geom);
        rTree = rTree.add(subzone, envelopeToRect(box));
    }

    public List<Subzone> query(Envelope envelope) {
        List<Subzone> result = new ArrayList<>();
        rTree.search(envelopeToRect(envelope))
                .toBlocking()
                .toIterable()
                .forEach(item -> result.add(item.value()));
        return result;
    }

    public List<Subzone> query(org.locationtech.jts.geom.Geometry geometry) {
        return query(geometry.getEnvelopeInternal());
    }

    public List<Subzone> query(org.locationtech.jts.geom.Geometry geom, double distance) {
        //  Point point = geom instanceof Point ? (Point) geom : geom.getCentroid();
        Envelope box = GeometricDistanceCalculator.calcBoxByDist(geom, distance);
        List<Subzone> result = new ArrayList<>();
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
