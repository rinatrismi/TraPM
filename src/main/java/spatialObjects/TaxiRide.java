package spatialObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxiRide implements Serializable {
    Long trjId;
    String drivingMode;
    String osName;
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    Long eventTime;
    Double lon;
    Double lat;
    Double speed;
    Double bearing;
    Double accuracy;

    public TaxiRide() {}

    public TaxiRide(Long trjId, String drivingMode, String osName, Long eventTime, Double lat, Double lon, Double speed, Double bearing, Double accuracy) {
        this.trjId = trjId;
        this.drivingMode = drivingMode;
        this.osName = osName;
        this.eventTime = eventTime;
        //Instant.now().toEpochMilli()
        this.lat = lat;
        this.lon = lon;
        this.speed = speed;
        this.bearing = bearing;
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return "TaxiRide{" +
                " trjId=" + trjId +
                ", drivingMode='" + drivingMode + '\'' +
                ", osName='" + osName + '\'' +
                ", eventTime=" + eventTime +
                ", lon=" + lon +
                ", lat=" + lat +
                ", speed=" + speed +
                ", bearing=" + bearing +
                ", accuracy=" + accuracy +
                '}';
    }

    public Long getTrjId() {
        return trjId;
    }

    public void setTrjId(Long trjId) {
        this.trjId = trjId;
    }

    public String getDrivingMode() {
        return drivingMode;
    }

    public void setDrivingMode(String drivingMode) {
        this.drivingMode = drivingMode;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getBearing() {
        return bearing;
    }

    public void setBearing(Double bearing) {
        this.bearing = bearing;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxiRide taxiRide = (TaxiRide) o;
        return Objects.equals(trjId, taxiRide.trjId) && Double.compare(taxiRide.lat, lat) == 0 && Double.compare(taxiRide.lon, lon) == 0 && Double.compare(taxiRide.speed, speed) == 0 && Double.compare(taxiRide.bearing, bearing) == 0 && Double.compare(taxiRide.accuracy, accuracy) == 0 && Objects.equals(drivingMode, taxiRide.drivingMode) && Objects.equals(osName, taxiRide.osName) && Objects.equals(eventTime, taxiRide.eventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trjId, drivingMode, osName, eventTime, lat, lon, speed, bearing, accuracy);
    }


}
