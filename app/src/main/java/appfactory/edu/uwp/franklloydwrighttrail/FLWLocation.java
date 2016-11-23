package appfactory.edu.uwp.franklloydwrighttrail;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class FLWLocation {
    @StringRes private int name;
    @DrawableRes private int image;
    @DrawableRes private int markerColor;
    private String latlong;
    private double latitude;
    private double longitude;
    private String address;

    public FLWLocation(@StringRes int name, @DrawableRes int image, @DrawableRes int markerColor, String latlong, double latitude,double longitude,String address) {
        this.name = name;
        this.image = image;
        this.markerColor = markerColor;
        this.latlong = latlong;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
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
}
