package appfactory.edu.uwp.franklloydwrighttrail.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

import appfactory.edu.uwp.franklloydwrighttrail.Adapters.TourTimesAdapter;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import io.realm.Realm;

/**
 * Created by sterl on 2/19/2017.
 */

public class TripPlannerTourTimesFragment extends Fragment {

    private RecyclerView recyclerView;
    private TourTimesAdapter adapter;
    private LinearLayoutManager layoutManager;

    private Realm realm;
    private static String tripPosition;

    public static TripPlannerTourTimesFragment newInstance(String position){
        TripPlannerTourTimesFragment tripPlannerTourTimesFragment = new TripPlannerTourTimesFragment();
        tripPosition = position;
        return tripPlannerTourTimesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_trip_tour_times, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.tour_edit_recycler);
        adapter = new TourTimesAdapter(RealmController.getInstance().getTripResults(tripPosition).get(0), tripPosition);
        recyclerView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.generateDefaultLayoutParams();
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        realm = RealmController.getInstance().getRealm();

        return view;
    }
}
