package appfactory.edu.uwp.franklloydwrighttrail.Apis;

/**
 * Created by zstue_000 on 10/31/2016.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import appfactory.edu.uwp.franklloydwrighttrail.Apis.Bounds;
import appfactory.edu.uwp.franklloydwrighttrail.Apis.Leg;
import appfactory.edu.uwp.franklloydwrighttrail.Apis.OverviewPolyline;

public class Route {

    private Bounds bounds;
    private String copyrights;
    private List<Leg> legs = new ArrayList<Leg>();
    private OverviewPolyline overviewPolyline;
    private String summary;
    private List<Object> warnings = new ArrayList<Object>();
    @SerializedName("waypoint_order")
    private List<Integer> waypointOrder = new ArrayList<Integer>();


    /**
     *
     * @return
     * The bounds
     */
    public Bounds getBounds() {
        return bounds;
    }

    /**
     *
     * @param bounds
     * The bounds
     */
    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    /**
     *
     * @return
     * The copyrights
     */
    public String getCopyrights() {
        return copyrights;
    }

    /**
     *
     * @param copyrights
     * The copyrights
     */
    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    /**
     *
     * @return
     * The legs
     */
    public List<Leg> getLegs() {
        return legs;
    }

    /**
     *
     * @param legs
     * The legs
     */
    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    /**
     *
     * @return
     * The overviewPolyline
     */
    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    /**
     *
     * @param overviewPolyline
     * The overview_polyline
     */
    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    /**
     *
     * @return
     * The summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     *
     * @param summary
     * The summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     *
     * @return
     * The warnings
     */
    public List<Object> getWarnings() {
        return warnings;
    }

    /**
     *
     * @param warnings
     * The warnings
     */
    public void setWarnings(List<Object> warnings) {
        this.warnings = warnings;
    }

    /**
     *
     * @return
     * The waypointOrder
     */
    public List<Integer> getWaypointOrder() {
        return waypointOrder;
    }

    /**
     *
     * @param waypointOrder
     * The waypoint_order
     */
    public void setWaypointOrder(List<Integer> waypointOrder) {
        this.waypointOrder = waypointOrder;
    }



}
