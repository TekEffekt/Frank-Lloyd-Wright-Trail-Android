package appfactory.edu.uwp.franklloydwrighttrail;


import java.sql.Time;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sterl on 10/31/2016.
 */

public class TripObject extends RealmObject{
    private RealmList<TripOrder> trips;
    @PrimaryKey
    private int key = 0;
    private int startTime;
    private int endTime;

    private int dinnerStartTime;
    private int lunchStartTime;
    private int breakfastStartTime;

    private int dinnerEndTime;
    private int lunchEndTime;
    private int breakfastEndTime;

    public TripObject (){
        trips = new RealmList<TripOrder>();
        startTime = 0;
        endTime = 0;

        dinnerStartTime = 0;
        lunchStartTime = 0;
        breakfastStartTime = 0;

        dinnerEndTime = 0;
        lunchEndTime = 0;
        breakfastEndTime = 0;
    };
    public RealmList<TripOrder> getTrips() {
        return trips;
    }

    public void setTrips(RealmList<TripOrder> trips) {
        this.trips = trips;
    }
    public int getKey() {return key;}
    public void setKey(int key){ this.key= key; }
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

    public int getDinnerStartTime() {
        return dinnerStartTime;
    }

    public void setDinnerStartTime(int dinnerStartTime) {
        this.dinnerStartTime = dinnerStartTime;
    }

    public int getLunchStartTime() {
        return lunchStartTime;
    }

    public void setLunchStartTime(int lunchStartTime) {
        this.lunchStartTime = lunchStartTime;
    }

    public int getBreakfastStartTime() {
        return breakfastStartTime;
    }

    public void setBreakfastStartTime(int breakfastStartTime) {
        this.breakfastStartTime = breakfastStartTime;
    }

    public int getDinnerEndTime() {
        return dinnerEndTime;
    }

    public void setDinnerEndTime(int dinnerEndTime) {
        this.dinnerEndTime = dinnerEndTime;
    }

    public int getLunchEndTime() {
        return lunchEndTime;
    }

    public void setLunchEndTime(int lunchEndTime) {
        this.lunchEndTime = lunchEndTime;
    }

    public int getBreakfastEndTime() {
        return breakfastEndTime;
    }

    public void setBreakfastEndTime(int breakfastEndTime) {
        this.breakfastEndTime = breakfastEndTime;
    }
}
