package appfactory.edu.uwp.franklloydwrighttrail;

/**
 * Created by zstue_000 on 10/31/2016.
 */

import java.util.HashMap;
import java.util.Map;

public class Step {

    private Distance distance;
    private Duration duration;
    private EndLocation endLocation;
    private String htmlInstructions;
    private Polyline polyline;
    private StartLocation startLocation;
    private String travelMode;
    private String maneuver;


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
     * The htmlInstructions
     */
    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    /**
     *
     * @param htmlInstructions
     * The html_instructions
     */
    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    /**
     *
     * @return
     * The polyline
     */
    public Polyline getPolyline() {
        return polyline;
    }

    /**
     *
     * @param polyline
     * The polyline
     */
    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
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
     * The travelMode
     */
    public String getTravelMode() {
        return travelMode;
    }

    /**
     *
     * @param travelMode
     * The travel_mode
     */
    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    /**
     *
     * @return
     * The maneuver
     */
    public String getManeuver() {
        return maneuver;
    }

    /**
     *
     * @param maneuver
     * The maneuver
     */
    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }



}
