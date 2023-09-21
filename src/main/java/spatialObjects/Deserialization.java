package spatialObjects;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Deserialization implements Serializable {

    public static ArrayList<PointOfInterest> jsonToPOI(String path) throws IOException {
        ArrayList<PointOfInterest> poi = new ArrayList<>();
        GeometryFactory geofact = new GeometryFactory();
        String content = new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
        JSONObject geojsonObj = new JSONObject(content);
        JSONArray features = geojsonObj.getJSONArray("features");
        for (int i = 0; i < features.length(); i++) {
            JSONObject feature = features.getJSONObject(i);
            // System.out.println(feature);
            JSONObject geometry = feature.getJSONObject("geometry");
            JSONObject properties = feature.getJSONObject("properties");
            double longitude = geometry.getJSONArray("coordinates").getDouble(0);
            double latitude = geometry.getJSONArray("coordinates").getDouble(1);
            Point p = geofact.createPoint(new Coordinate(longitude, latitude));
            String id = String.valueOf(properties.getInt("osm_id"));
            String name = properties.getString("name");
            String category = null;
            if (!properties.getString("amenity").isEmpty()) {
                category = properties.getString("amenity");
            } else if (!properties.getString("shop").isEmpty()) {
                category = properties.getString("shop");
            } else if (!properties.getString("tourism").isEmpty()) {
                category = properties.getString("tourism");
            } else {
                category = properties.getString("man_made");
            }
            p.setUserData(new PointOfInterest.AttributePOI(id, name, category));

            PointOfInterest pointOfInterest = new PointOfInterest(p);
            poi.add(pointOfInterest);
        }
        return poi;
    }

    public static ArrayList<Subzone> geoJsonToSubzone(String path) throws IOException {
        ArrayList<Subzone> s = new ArrayList<>();
        GeometryFactory geofact = new GeometryFactory();
        String content = new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
        JSONObject geojsonObj = new JSONObject(content);
        JSONArray features = geojsonObj.getJSONArray("features");
        for (int i = 0; i < features.length(); i++) {
            JSONObject feature = features.getJSONObject(i);
            String id = feature.getJSONObject("properties").getString("SUBZONE_C");
            String name = feature.getJSONObject("properties").getString("SUBZONE_N");
            JSONObject geometry = feature.getJSONObject("geometry");
            JSONObject properties = feature.getJSONObject("properties");
            JSONArray coordinates;
            String postgisCoordinates = "";
            String wktTemplate = "POLYGON((%s))";
            ArrayList<Coordinate> points = new ArrayList<Coordinate>();
            if (geometry.getString("type").equals("Polygon")) {
                coordinates = geometry.getJSONArray("coordinates").getJSONArray(0);
                for (int j = 0; j < coordinates.length(); j++) {
                    double longitude = coordinates.getJSONArray(j).getDouble(0);
                    double latitude = coordinates.getJSONArray(j).getDouble(1);

                    points.add(new Coordinate(longitude, latitude));

                }
                Polygon poly = geofact.createPolygon((Coordinate[]) points.toArray(new Coordinate[]{}));
                poly.setUserData(new Subzone.AttributeSubZone(id, name));
                Subzone subzone = new Subzone(poly);

                s.add(subzone);

            } else if (geometry.getString("type").equals("MultiPolygon")) {
                coordinates = geometry.getJSONArray("coordinates").getJSONArray(0).getJSONArray(0);
                for (int j = 0; j < coordinates.length(); j++) {
                    double longitude = coordinates.getJSONArray(j).getDouble(0);
                    double latitude = coordinates.getJSONArray(j).getDouble(1);

                    points.add(new Coordinate(longitude, latitude));

                }
                Polygon poly = geofact.createPolygon((Coordinate[]) points.toArray(new Coordinate[]{}));

                poly.setUserData(new Subzone.AttributeSubZone(id, name));

                Subzone subzone = new Subzone(poly);

                s.add(subzone);
            }
        }
        return s;
    }

    public static DataStream<TaxiRide> TrajectoryStream(DataStream inputStream, String inputType, String delimiter) {

        DataStream<TaxiRide> trajectoryStream = null;


        if(inputType.equals("GeoJSON")) {
            trajectoryStream = inputStream.map(new GeoJSONToTaxiRide());
        }
        else if(inputType.equals("CSV")) {
            trajectoryStream = inputStream.map(new CSVToTaxiRide(delimiter));
                    //.returns(PointOfInterest.class);
        } else {
            System.out.println("input type is not supported");
        }
        return trajectoryStream;
    }

    private static class GeoJSONToTaxiRide extends RichMapFunction<String, TaxiRide> {
        @Override
        public TaxiRide map(String s) throws Exception {
            return null;
        }
    }

    private static class CSVToTaxiRide extends RichMapFunction<String, TaxiRide> {

        String delimiter;

        //public CSVToTaxiRide(){}
        public CSVToTaxiRide(String delimiter) {
            this.delimiter = delimiter;
        }
        @Override
        public TaxiRide map(String s) throws Exception {
            String[] tokens = s.split(delimiter);
            TaxiRide ride = new TaxiRide();

            if(tokens.length >=10) {
                try {
                    ride.setTrjId(Long.parseLong(tokens[1]));
                    ride.setDrivingMode(tokens[2]);
                    ride.setOsName(tokens[3]);
                    ride.setEventTime(Long.parseLong(tokens[4])); //*1000
                    ride.setLon(Double.parseDouble(tokens[6]));
                    ride.setLat(Double.parseDouble(tokens[5]));
                    ride.setSpeed(Double.parseDouble(tokens[7]));
                    ride.setBearing(Double.parseDouble(tokens[8]));
                    ride.setAccuracy(Double.parseDouble(tokens[9]));
                } catch (NumberFormatException nfe) {
                    throw new RuntimeException("Invalid record: " + s, nfe);
                }
            }
            return ride;
        }
    }
}
