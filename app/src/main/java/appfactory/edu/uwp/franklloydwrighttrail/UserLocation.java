package appfactory.edu.uwp.franklloydwrighttrail;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by Work PC on 2/21/2017.
 */

public class UserLocation extends RealmObject {


    @PrimaryKey
    private int key = 1;



    private String name;
    private double latitude;
    private double longitude;
    public UserLocation()
    {
        this.name = "home";
        this.latitude = 0.0;
        this.longitude = 0.0;
    }
    public UserLocation(double latitude, double longitude)
    {
        this.name = "home";
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
