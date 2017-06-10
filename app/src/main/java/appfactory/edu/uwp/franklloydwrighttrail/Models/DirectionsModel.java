package appfactory.edu.uwp.franklloydwrighttrail.Models;

/**
 * Created by zstue_000 on 10/31/2016.
 */

import java.util.ArrayList;
import java.util.List;

import appfactory.edu.uwp.franklloydwrighttrail.Apis.GeocodedWaypoint;
import appfactory.edu.uwp.franklloydwrighttrail.Apis.Route;

public class DirectionsModel {

    private List<GeocodedWaypoint> geocodedWaypoints = new ArrayList<GeocodedWaypoint>();
    private List<Route> routes = new ArrayList<Route>();
    private String status;
    /**
     *
     * @return
     * The geocodedWaypoints
     */
    public List<GeocodedWaypoint> getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    /**
     *
     * @param geocodedWaypoints
     * The geocoded_waypoints
     */
    public void setGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
    }

    /**
     *
     * @return
     * The routes
     */
    public List<Route> getRoutes() {
        return routes;
    }

    /**
     *
     * @param routes
     * The routes
     */
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DirectionsModel{" +
                "geocodedWaypoints=" + geocodedWaypoints +
                ", routes=" + routes +
                ", status='" + status + '\'' +
                '}';
    }
}
