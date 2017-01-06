package appfactory.edu.uwp.franklloydwrighttrail;


import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by sterl on 10/31/2016.
 */

public class TripObject extends RealmObject{
    private RealmList<TripOrder> trips;

    private long startTime;
    private long endTime;

    private int dinnerTime;
    private int lunchTime;
    private int breakfastTime;

    public TripObject (){
        trips = new RealmList<TripOrder>();
        startTime = 0;
        endTime = 0;

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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
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
}
