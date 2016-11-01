package appfactory.edu.uwp.franklloydwrighttrail;

/**
 * Created by zstue_000 on 10/31/2016.
 */

public class TripOrder {

    private String startAddress;
    private String endAddress;
    private String timeText;
    private int timeValue;

    public TripOrder(String startAddress,String endAddress,String timeText,int timeValue)
    {
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.timeText = timeText;
        this.timeValue = timeValue;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
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
}
