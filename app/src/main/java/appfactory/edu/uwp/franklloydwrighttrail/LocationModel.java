package appfactory.edu.uwp.franklloydwrighttrail;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import io.realm.RealmList;

public class LocationModel {
    @NonNull
    public static RealmList<FLWLocation> getLocations() {
        RealmList<FLWLocation> locations = new RealmList<>();

        /*
        locations.add(0, new FLWLocation(R.string.scjohnson, R.drawable.scjohnson, R.drawable.marker_red,"42.7152375,-87.7906969",42.7152375,-87.7906969, "1525 Howe St, Racine, WI 53403, USA"));
        locations.add(1, new FLWLocation(R.string.wingspread, R.drawable.wingspread1, R.drawable.marker_yellow,"42.784562,-87.771588",42.784562,-87.771588,"36 E 4 Mile Rd, Wind Point, WI 53402, USA"));
        locations.add(2, new FLWLocation(R.string.built_homes, R.drawable.built_homes, R.drawable.marker_cyan,"43.010584,-87.948539",43.010584,-87.948539,"2714 W Burnham St, Milwaukee, WI 53215, USA"));
        locations.add(3, new FLWLocation(R.string.monona_terrace, R.drawable.monona_terrace, R.drawable.marker_orange,"43.0717445,-89.3804018",43.0717445,-89.3804018,"1 John Nolen Dr, Madison, WI 53703, USA"));
        locations.add(4, new FLWLocation(R.string.meeting_house, R.drawable.meeting_house, R.drawable.marker_green,"43.0757361,-89.4353368",43.0757361,-89.4353368,"900 University Bay Dr, Madison, WI 53705, USA"));
        locations.add(5, new FLWLocation(R.string.visitor_center, R.drawable.visitor_center, R.drawable.marker_blue,"43.144128,-90.059512",43.144128,-90.059512,"5607 County Rd C, Spring Green, WI 53588, USA"));
        locations.add(6, new FLWLocation(R.string.valley_school, R.drawable.valley_school, R.drawable.marker_magenta,"43.119255,-90.114908",43.119255,-90.114908,"6306 WI-23 Trunk, Spring Green, WI 53588, USA"));
        locations.add(7, new FLWLocation(R.string.german_warehouse, R.drawable.german_warehouse, R.drawable.marker_violet,"43.3334718,-90.38436739999997",43.3334718,-90.38436739999997,"300 S Church St, Richland Center, WI 53581, USA"));
        */


        locations.add(0, new FLWLocation(R.string.scjohnson, R.mipmap.scjohnson, R.drawable.marker_red,"42.7152375,-87.7906969",42.7152375,-87.7906969, "1525 Howe St, Racine, WI 53403, USA"));
        locations.add(1, new FLWLocation(R.string.wingspread, R.mipmap.wingspread, R.drawable.marker_yellow,"42.784562,-87.771588",42.784562,-87.771588,"36 E 4 Mile Rd, Wind Point, WI 53402, USA"));
        locations.add(2, new FLWLocation(R.string.built_homes, R.mipmap.built_homes, R.drawable.marker_cyan,"43.010584,-87.948539",43.010584,-87.948539,"2714 W Burnham St, Milwaukee, WI 53215, USA"));
        locations.add(3, new FLWLocation(R.string.monona_terrace, R.mipmap.monona_terrace, R.drawable.marker_orange,"43.0717445,-89.3804018",43.0717445,-89.3804018,"1 John Nolen Dr, Madison, WI 53703, USA"));
        locations.add(4, new FLWLocation(R.string.meeting_house, R.mipmap.meeting_house, R.drawable.marker_green,"43.0757361,-89.4353368",43.0757361,-89.4353368,"900 University Bay Dr, Madison, WI 53705, USA"));
        locations.add(5, new FLWLocation(R.string.visitor_center, R.mipmap.visitor_center, R.drawable.marker_blue,"43.144128,-90.059512",43.144128,-90.059512,"5607 County Rd C, Spring Green, WI 53588, USA"));
        locations.add(6, new FLWLocation(R.string.valley_school, R.mipmap.valley_school, R.drawable.marker_magenta,"43.119255,-90.114908",43.119255,-90.114908,"6306 WI-23 Trunk, Spring Green, WI 53588, USA"));
        locations.add(7, new FLWLocation(R.string.german_warehouse, R.mipmap.german_warehouse, R.drawable.marker_violet,"43.3334718,-90.38436739999997",43.3334718,-90.38436739999997,"300 S Church St, Richland Center, WI 53581, USA"));



        return locations;
    }
}
