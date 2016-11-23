package appfactory.edu.uwp.franklloydwrighttrail;

/**
 * Created by zstue_000 on 10/31/2016.
 */

public class TripOrder {


    private String timeText;
    private int timeValue;
    private FLWLocation location;

    public TripOrder(FLWLocation location ,String timeText,int timeValue)
    {

        this.timeText = timeText;
        this.timeValue = timeValue;
        this.location = location;
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
