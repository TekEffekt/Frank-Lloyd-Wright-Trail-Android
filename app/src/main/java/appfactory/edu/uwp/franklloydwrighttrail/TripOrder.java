package appfactory.edu.uwp.franklloydwrighttrail;

import android.support.annotation.NonNull;

import java.util.Comparator;

import io.realm.RealmObject;

/**
 * Created by zstue_000 on 10/31/2016.
 */

public class TripOrder extends RealmObject implements Comparable<TripOrder>{

    private String timeText;
    private int timeValue;
    private FLWLocation location;
    // These also double as Generic Stop End and Start times
    private long startTourTime;
    private long endTourTime;

    public TripOrder(FLWLocation location, String timeText, int timeValue){
        this.timeText = timeText;
        this.timeValue = timeValue;
        this.location = location;
        this.startTourTime = -1;
        this.endTourTime = -1;
    }

    public TripOrder(FLWLocation location) {
        this.location = location;
        this.timeValue = -1;
        this.timeText = null;
        this.startTourTime = -1;
        this.endTourTime = -1;
    }

    public TripOrder(){
        this.location = null;
        this.timeValue = -1;
        this.timeText = null;
        this.startTourTime = -1;
        this.endTourTime = -1;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String time) {
        this.timeText = time;
    }

    public int getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(int timeValue) {
        this.timeValue = timeValue;
    }

    public FLWLocation getLocation(){return location;}

    public void setLocation(FLWLocation location) {this.location = location;}

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

    @Override
    public int compareTo(@NonNull TripOrder tripOrder) {
        if(this.getStartTourTime() < tripOrder.getStartTourTime())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
    public static Comparator<TripOrder> TripOrderComparator = new Comparator<TripOrder>() {
        @Override
        public int compare(TripOrder tripOrder, TripOrder t1) {
            return tripOrder.compareTo(t1);
        }
    };
}
