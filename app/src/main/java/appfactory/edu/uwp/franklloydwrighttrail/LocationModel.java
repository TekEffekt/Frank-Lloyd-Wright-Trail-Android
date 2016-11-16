package appfactory.edu.uwp.franklloydwrighttrail;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class LocationModel {
    @NonNull
    public static ArrayList<FLWLocation> getLocations() {
        ArrayList<FLWLocation> locations = new ArrayList<>();
        locations.add(0, new FLWLocation(R.string.scjohnson, R.drawable.scjohnson, R.drawable.marker_red,"42.7152375,-87.7906969",42.7152375,-87.7906969));
        locations.add(1, new FLWLocation(R.string.monona_terrace, R.drawable.monona_terrace, R.drawable.marker_orange,"43.0717445,-89.3804018",43.0717445,-89.3804018));
        locations.add(2, new FLWLocation(R.string.wingspread, R.drawable.wingspread1, R.drawable.marker_yellow,"42.784562,-87.771588",42.784562,-87.771588));
        locations.add(3, new FLWLocation(R.string.meeting_house, R.drawable.meeting_house, R.drawable.marker_green,"43.0757361,-89.4353368",43.0757361,-89.4353368));
        locations.add(4, new FLWLocation(R.string.visitor_center, R.drawable.visitor_center, R.drawable.marker_blue,"43.14390059999999,-90.05952260000004",43.14390059999999,-90.05952260000004));
        locations.add(5, new FLWLocation(R.string.german_warehouse, R.drawable.german_warehouse, R.drawable.marker_violet,"43.3334718,-90.38436739999997",43.3334718,-90.38436739999997));
        locations.add(6, new FLWLocation(R.string.valley_school, R.drawable.valley_school, R.drawable.marker_magenta,"43.119255,-90.114908",43.119255,-90.114908));
        locations.add(7, new FLWLocation(R.string.built_homes, R.drawable.built_homes, R.drawable.marker_cyan,"43.010584,-87.948539",43.010584,-87.948539));

        return locations;
    }
}
