package appfactory.edu.uwp.franklloydwrighttrail;

import io.realm.RealmObject;

/**
 * Created by zstue_000 on 10/31/2016.
 */

public class TripOrder extends RealmObject{

    private String timeText;
    private int timeValue;
    private FLWLocation location;

    public TripOrder(FLWLocation location, String timeText, int timeValue){
        this.timeText = timeText;
        this.timeValue = timeValue;
        this.location = location;
    }

    public TripOrder(FLWLocation location) {
        this.location = location;
        this.timeValue = -1;
        this.timeText = null;
    }

    public TripOrder(){
        this.location = null;
        this.timeValue = -1;
        this.timeText = null;
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
}
