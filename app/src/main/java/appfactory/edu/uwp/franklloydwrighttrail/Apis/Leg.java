package appfactory.edu.uwp.franklloydwrighttrail.Apis;

/**
 * Created by zstue_000 on 10/31/2016.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Leg {

    private Distance distance;
    private Duration duration;
    @SerializedName("end_address")
    private String endAddress;
    private EndLocation endLocation;
    @SerializedName("start_address")
    private String startAddress;
    private StartLocation startLocation;
    private List<Step> steps = new ArrayList<Step>();
    private List<Object> trafficSpeedEntry = new ArrayList<Object>();
    private List<Object> viaWaypoint = new ArrayList<Object>();


    /**
     *
     * @return
     * The distance
     */
    public Distance getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     * The distance
     */
    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    /**
     *
     * @return
     * The duration
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     *
     * @param duration
     * The duration
     */
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    /**
     *
     * @return
     * The endAddress
     */
    public String getEndAddress() {
        return endAddress;
    }

    /**
     *
     * @param endAddress
     * The end_address
     */
    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    /**
     *
     * @return
     * The endLocation
     */
    public EndLocation getEndLocation() {
        return endLocation;
    }

    /**
     *
     * @param endLocation
     * The end_location
     */
    public void setEndLocation(EndLocation endLocation) {
        this.endLocation = endLocation;
    }

    /**
     *
     * @return
     * The startAddress
     */
    public String getStartAddress() {
        return startAddress;
    }

    /**
     *
     * @param startAddress
     * The start_address
     */
    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    /**
     *
     * @return
     * The startLocation
     */
    public StartLocation getStartLocation() {
        return startLocation;
    }

    /**
     *
     * @param startLocation
     * The start_location
     */
    public void setStartLocation(StartLocation startLocation) {
        this.startLocation = startLocation;
    }

    /**
     *
     * @return
     * The steps
     */
    public List<Step> getSteps() {
        return steps;
    }

    /**
     *
     * @param steps
     * The steps
     */
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    /**
     *
     * @return
     * The trafficSpeedEntry
     */
    public List<Object> getTrafficSpeedEntry() {
        return trafficSpeedEntry;
    }

    /**
     *
     * @param trafficSpeedEntry
     * The traffic_speed_entry
     */
    public void setTrafficSpeedEntry(List<Object> trafficSpeedEntry) {
        this.trafficSpeedEntry = trafficSpeedEntry;
    }

    /**
     *
     * @return
     * The viaWaypoint
     */
    public List<Object> getViaWaypoint() {
        return viaWaypoint;
    }

    /**
     *
     * @param viaWaypoint
     * The via_waypoint
     */
    public void setViaWaypoint(List<Object> viaWaypoint) {
        this.viaWaypoint = viaWaypoint;
    }

    @Override
    public String toString() {
        return "Leg{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", endAddress='" + endAddress + '\'' +
                ", endLocation=" + endLocation +
                ", startAddress='" + startAddress + '\'' +
                ", startLocation=" + startLocation +
                ", steps=" + steps +
                ", trafficSpeedEntry=" + trafficSpeedEntry +
                ", viaWaypoint=" + viaWaypoint +
                '}';
    }
}
