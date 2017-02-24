package appfactory.edu.uwp.franklloydwrighttrail.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public static TripPlannerTourTimesFragment newInstance(){
        TripPlannerTourTimesFragment tripPlannerTourTimesFragment = new TripPlannerTourTimesFragment();
        return tripPlannerTourTimesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_trip_tour_times, container, false);

        realm = RealmController.getInstance().getRealm();

        recyclerView = (RecyclerView) view.findViewById(R.id.tour_edit_recycler);
        adapter = new TourTimesAdapter(RealmController.getInstance().getTrip());
        recyclerView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.generateDefaultLayoutParams();
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
}
