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

    private long dinnerTime;
    private long lunchTime;
    private long breakfastTime;

    public TripObject (){
        trips = new RealmList<TripOrder>();
        startTime = 0;
        endTime = 0;
        dinnerTime = 0;
        lunchTime = 0;
        breakfastTime = 0;
    };
    public long getDinnerTime() {
        return dinnerTime;
    }

    public void setDinnerTime(long dinnerTime) {
        this.dinnerTime = dinnerTime;
    }

    public long getLunchTime() {
        return lunchTime;
    }

    public void setLunchTime(long lunchTime) {
        this.lunchTime = lunchTime;
    }

    public long getBreakfastTime() {
        return breakfastTime;
    }

    public void setBreakfastTime(long breakfastTime) {
        this.breakfastTime = breakfastTime;
    }

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
}
