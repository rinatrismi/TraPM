package spatialObjects;

import org.locationtech.jts.geom.*;

import java.io.Serializable;
import java.util.Objects;

public class PointOfInterest extends Geometry implements Serializable {
    Geometry geom;
    Envelope env;
    AttributePOI attr;

    public PointOfInterest(GeometryFactory factory) {
        super(factory);
    }

    public PointOfInterest(Geometry geom){
        super(geom.getFactory());
        this.geom = geom;
        this.attr = (AttributePOI) geom.getUserData();
        super.setUserData(geom.getUserData());
    }



    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    public void setAttr(Geometry geom) {
        this.attr = (AttributePOI) geom.getUserData();
    }

    public Geometry getGeom() {
        return geom;
    }

    public AttributePOI getAttr() {
        return (AttributePOI) geom.getUserData();
    }

    public Object getUserData() {
        return geom.getUserData();
    }

    @Override
    public String getGeometryType() {
        return geom.getGeometryType();
    }

    @Override
    public Coordinate getCoordinate() {
        return geom.getCoordinate();
    }

    @Override
    public Coordinate[] getCoordinates() {
        return new Coordinate[0];
    }

    @Override
    public int getNumPoints() {
        return geom.getNumPoints();
    }

    @Override
    public boolean isEmpty() {
        return geom.isEmpty();
    }

    @Override
    public int getDimension() {
        return geom.getDimension();
    }

    @Override
    public Geometry getBoundary() {
        return geom.getBoundary();
    }

    @Override
    public int getBoundaryDimension() {
        return geom.getBoundaryDimension();
    }

    @Override
    protected Geometry reverseInternal() {
        return null;
    }

    @Override
    public boolean equalsExact(Geometry geometry, double v) {
        return false;
    }

    @Override
    public void apply(CoordinateFilter coordinateFilter) {

    }

    @Override
    public void apply(CoordinateSequenceFilter coordinateSequenceFilter) {

    }

    @Override
    public void apply(GeometryFilter geometryFilter) {

    }

    @Override
    public void apply(GeometryComponentFilter geometryComponentFilter) {

    }

    @Override
    protected Geometry copyInternal() {
        return null;
    }

    @Override
    public void normalize() {

    }

    @Override
    protected Envelope computeEnvelopeInternal() {
        if (env == null) {
            env = geom.getEnvelopeInternal();
        }
        return env;
    }

    @Override
    protected int compareToSameClass(Object o) {
        return 0;
    }

    @Override
    protected int compareToSameClass(Object o, CoordinateSequenceComparator coordinateSequenceComparator) {
        return 0;
    }

    @Override
    public Point getCentroid() {
        return geom.getCentroid();
    }

    @Override
    protected int getTypeCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "POI{" +
                "geom=" + geom +
                ", attr=" + attr +
                '}';
        // + "\n"
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PointOfInterest poi = (PointOfInterest) o;
        return Objects.equals(geom, poi.geom) && Objects.equals(env, poi.env) && Objects.equals(attr, poi.attr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), geom, env, attr);
    }

    public static class AttributePOI {
        String id;
        String name;
        String category;

        public AttributePOI(String id, String name, String category) {
            this.id = id;
            this.name = name;
            this.category = category;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AttributePOI that = (AttributePOI) o;
            return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(category, that.category);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, category);
        }

        @Override
        public String toString() {
            return "AttributePOI{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", category='" + category + '\'' +
                    '}';
        }
    }
}
