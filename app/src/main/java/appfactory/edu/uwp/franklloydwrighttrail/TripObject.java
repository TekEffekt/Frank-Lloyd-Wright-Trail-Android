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

    private long dinnerStartTime;
    private long lunchStartTime;
    private long breakfastStartTime;

    private long dinnerEndTime;
    private long lunchEndTime;
    private long breakfastEndTime;

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

    public long getDinnerStartTime() {
        return dinnerStartTime;
    }

    public void setDinnerStartTime(long dinnerStartTime) {
        this.dinnerStartTime = dinnerStartTime;
    }

    public long getLunchStartTime() {
        return lunchStartTime;
    }

    public void setLunchStartTime(long lunchStartTime) {
        this.lunchStartTime = lunchStartTime;
    }

    public long getBreakfastStartTime() {
        return breakfastStartTime;
    }

    public void setBreakfastStartTime(long breakfastStartTime) {
        this.breakfastStartTime = breakfastStartTime;
    }

    public long getDinnerEndTime() {
        return dinnerEndTime;
    }

    public void setDinnerEndTime(long dinnerEndTime) {
        this.dinnerEndTime = dinnerEndTime;
    }

    public long getLunchEndTime() {
        return lunchEndTime;
    }

    public void setLunchEndTime(long lunchEndTime) {
        this.lunchEndTime = lunchEndTime;
    }

    public long getBreakfastEndTime() {
        return breakfastEndTime;
    }

    public void setBreakfastEndTime(long breakfastEndTime) {
        this.breakfastEndTime = breakfastEndTime;
    }
}
