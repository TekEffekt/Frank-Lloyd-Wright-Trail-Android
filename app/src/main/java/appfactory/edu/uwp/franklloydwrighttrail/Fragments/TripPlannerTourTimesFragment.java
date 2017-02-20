package appfactory.edu.uwp.franklloydwrighttrail.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import appfactory.edu.uwp.franklloydwrighttrail.R;

/**
 * Created by sterl on 2/19/2017.
 */

public class TripPlannerTourTimesFragment extends Fragment {

    public static TripPlannerTourTimesFragment newInstance(){
        TripPlannerTourTimesFragment tripPlannerTourTimesFragment = new TripPlannerTourTimesFragment();
        return tripPlannerTourTimesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_trip_tour_times, container, false);

        return view;
    }
}
