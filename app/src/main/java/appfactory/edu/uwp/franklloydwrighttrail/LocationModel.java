package appfactory.edu.uwp.franklloydwrighttrail;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class LocationModel {
    @NonNull
    public static ArrayList<FLWLocation> getLocations() {
        ArrayList<FLWLocation> locations = new ArrayList<>();
        locations.add(0, new FLWLocation(R.string.scjohnson, R.mipmap.scjohnson, R.drawable.marker_red));
        locations.add(1, new FLWLocation(R.string.monona_terrace, R.mipmap.monona_terrace, R.drawable.marker_orange));
        locations.add(2, new FLWLocation(R.string.wingspread, R.mipmap.wingspread, R.drawable.marker_yellow));
        locations.add(3, new FLWLocation(R.string.meeting_house, R.mipmap.meeting_house, R.drawable.marker_green));
        locations.add(4, new FLWLocation(R.string.visitor_center, R.mipmap.visitor_center, R.drawable.marker_blue));
        locations.add(5, new FLWLocation(R.string.german_warehouse, R.mipmap.german_warehouse, R.drawable.marker_violet));

        return locations;
    }
}
