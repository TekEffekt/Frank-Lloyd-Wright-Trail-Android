package appfactory.edu.uwp.franklloydwrighttrail;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.Date;

import io.realm.RealmObject;

public class FLWLocation extends RealmObject{
    @StringRes private int name;
    private String genericName;

    // These are exclusive to FLW Stops
    @DrawableRes private int image;
    @DrawableRes private int markerColor;
    private String latlong;
    private double latitude;
    private double longitude;
    private String address;
    @StringRes private int website;

    private String day;



    public FLWLocation(@StringRes int name, @DrawableRes int image, @DrawableRes int markerColor, String latlong, double latitude,double longitude,String address, String day, @StringRes int website) {
        this.name = name;
        this.genericName = "";
        this.image = image;
        this.markerColor = markerColor;
        this.latlong = latlong;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.website = website;

        this.day = day;
    }

    public FLWLocation(){
        this.name = -1;
        this.genericName = null;
        this.image = -1;
        this.markerColor = -1;
        this.latlong = null;
        this.latitude = -1;
        this.longitude = -1;
        this.address = null;
        this.day = null;
        this.website = -1;
    }

    public FLWLocation(String genericName){
        this.name = -1;
        this.genericName = genericName;
        this.image = -1;
        this.markerColor = -1;
        this.latlong = null;
        this.latitude = -1;
        this.longitude = -1;
        this.address = null;
        this.day = null;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    @Override
    public boolean equals(Object location)
    {
        if(location instanceof FLWLocation)
        {
            if(this.getName() == ((FLWLocation) location).getName() && this.getGenericName() == ((FLWLocation)location).getGenericName())
                return true;
            return false;
        }
        return false;

    }


    public int getWebsite() {
        return website;
    }

    public void setWebsite(int website) {
        this.website = website;
    }
}
