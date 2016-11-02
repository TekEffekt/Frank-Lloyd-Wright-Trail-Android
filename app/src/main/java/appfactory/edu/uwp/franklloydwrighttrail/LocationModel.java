package appfactory.edu.uwp.franklloydwrighttrail;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class LocationModel {
    @NonNull
    public static ArrayList<FLWLocation> getLocations() {
        ArrayList<FLWLocation> locations = new ArrayList<>();
        locations.add(0, new FLWLocation(R.string.scjohnson, R.drawable.scjohnson, R.drawable.marker_red,"42.715237,-87.790697"));
        locations.add(1, new FLWLocation(R.string.monona_terrace, R.drawable.monona_terrace, R.drawable.marker_orange,"43.071744,-89.380402"));
        locations.add(2, new FLWLocation(R.string.wingspread, R.drawable.wingspread1, R.drawable.marker_yellow,"42.784472,-87.771599"));
        locations.add(3, new FLWLocation(R.string.meeting_house, R.drawable.meeting_house, R.drawable.marker_green,"43.075736,-89.435337"));
        locations.add(4, new FLWLocation(R.string.visitor_center, R.drawable.visitor_center, R.drawable.marker_blue,"43.143901,-90.059523"));
        locations.add(5, new FLWLocation(R.string.german_warehouse, R.drawable.german_warehouse, R.drawable.marker_violet,"43.333472,-90.384367"));

        return locations;
    }
}
