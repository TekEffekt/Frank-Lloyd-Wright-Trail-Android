package appfactory.edu.uwp.franklloydwrighttrail;


import java.sql.Time;
import java.util.ArrayList;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sterl on 10/31/2016.
 */

public class TripObject extends RealmObject{
    private RealmList<TripOrder> trips;
    @PrimaryKey
    private String key;
    private int startTime;
    private int endTime;

    private String name;

    private int dinnerTime;
    private int lunchTime;
    private int breakfastTime;

    public TripObject(){
        trips = new RealmList<TripOrder>();
        startTime = 0;
        endTime = 0;

        name = "Default Trip";

        dinnerTime = 0;
        lunchTime = 0;
        breakfastTime = 0;
    }
    public TripObject (String key){
        trips = new RealmList<TripOrder>();
        startTime = 0;
        endTime = 0;
        this.key = key;

        name = "Trip " + key.toString();

        dinnerTime = 0;
        lunchTime = 0;
        breakfastTime = 0;
    };

    public RealmList<TripOrder> getTrips() {
        return trips;
    }

    public void setTrips(RealmList<TripOrder> trips) {
        this.trips = trips;
    }

    public String getKey() {return key;}

    public void setKey(String key){ this.key= key; }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getDinnerTime() {
        return dinnerTime;
    }

    public void setDinnerTime(int dinnerTime) {
        this.dinnerTime = dinnerTime;
    }

    public int getLunchTime() {
        return lunchTime;
    }

    public void setLunchTime(int lunchTime) {
        this.lunchTime = lunchTime;
    }

    public int getBreakfastTime() {
        return breakfastTime;
    }

    public void setBreakfastTime(int breakfastTime) {
        this.breakfastTime = breakfastTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
