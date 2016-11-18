package appfactory.edu.uwp.franklloydwrighttrail;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by sterl on 11/3/2016.
 */

public class TimelineModel {
    //Where we want to parce the trip model into things for the recyclerview.
    //For now just grabs w/e locations we have in the trip period
    @NonNull
    public static ArrayList<FLWLocation> getTrip() {
        return LocationModel.getLocations();
    }

    public static void setTrip(){};
}
