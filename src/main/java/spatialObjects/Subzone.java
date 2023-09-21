package spatialObjects;

import org.locationtech.jts.geom.*;

import java.io.Serializable;
import java.util.Objects;

public class Subzone extends Geometry implements Serializable {
    Geometry geom;
    Envelope env;
    AttributeSubZone attr;
    public Subzone(GeometryFactory factory) {
        super(factory);
    }

    public Subzone(Geometry geom){
        super(geom.getFactory());
        this.geom = geom;
        this.attr = (AttributeSubZone) geom.getUserData();
        super.setUserData(geom.getUserData());
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    public void setAttr(Geometry geom) {
        this.attr = (AttributeSubZone) geom.getUserData();
    }

    public Geometry getGeom() {
        return geom;
    }

    public AttributeSubZone getAttr() {
        return (AttributeSubZone) geom.getUserData();
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
        return "SubZone{" +
                "geom=" + geom +
                //", env=" + env +
                ", attr=" + attr +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subzone s = (Subzone) o;
        return Objects.equals(geom, s.geom) && Objects.equals(env, s.env) && Objects.equals(attr, s.attr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), geom, env, attr);
    }

    public static class AttributeSubZone {
        String id;
        String name;

        public AttributeSubZone(String id, String name) {
            this.id = id;
            this.name = name;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AttributeSubZone that = (AttributeSubZone) o;
            return Objects.equals(id, that.id) && Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }

        @Override
        public String toString() {
            return "AttributeSubZone{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
