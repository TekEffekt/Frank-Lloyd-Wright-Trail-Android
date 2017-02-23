package appfactory.edu.uwp.franklloydwrighttrail;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import io.realm.RealmObject;

public class FLWLocation extends RealmObject {
    @StringRes private int name;
    @DrawableRes private int image;
    @DrawableRes private int markerColor;
    private String latlong;
    private double latitude;
    private double longitude;
    private String address;
    private long startTourTime;
    private long endTourTime;

    public FLWLocation(@StringRes int name, @DrawableRes int image, @DrawableRes int markerColor, String latlong, double latitude,double longitude,String address, long startTourTime, long endTourTime) {
        this.name = name;
        this.image = image;
        this.markerColor = markerColor;
        this.latlong = latlong;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.startTourTime = startTourTime;
        this.endTourTime = endTourTime;
    }

    public FLWLocation(){
        this.name = -1;
        this.image = -1;
        this.markerColor = -1;
        this.latlong = null;
        this.latitude = -1;
        this.longitude = -1;
        this.address = null;
    }

    public int getImage() { return image; }

    public void setImage(int image) { this.image = image; }

    public int getName() { return name; }

    public void setName(int name) { this.name = name; }

    public int getMarkerColor() { return markerColor; }

    public void setMarkerColor(int markerColor) { this.markerColor = markerColor; }

    public String getLatlong() { return latlong; }

    public void setLatlong(String latlong) { this.latlong = latlong; }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {return address;}

    public void setAddress(String address) { this.address = address;}

    public long getStartTourTime() {
        return startTourTime;
    }

    public void setStartTourTime(long startTourTime) {
        this.startTourTime = startTourTime;
    }

    public long getEndTourTime() {
        return endTourTime;
    }

    public void setEndTourTime(long endTourTime) {
        this.endTourTime = endTourTime;
    }
}
