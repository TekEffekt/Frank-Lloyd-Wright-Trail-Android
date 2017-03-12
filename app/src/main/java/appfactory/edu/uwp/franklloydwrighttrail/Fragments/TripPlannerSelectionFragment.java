package appfactory.edu.uwp.franklloydwrighttrail.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.Models.LocationModel;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import appfactory.edu.uwp.franklloydwrighttrail.TripOrder;
import appfactory.edu.uwp.franklloydwrighttrail.Adapters.TripSelectionAdapter;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by sterl on 10/28/2016.
 */

public class TripPlannerSelectionFragment extends Fragment implements RecyclerView.OnItemTouchListener{
    private TripObject trip;
    private RealmList<TripOrder> trips;
    private RealmList<FLWLocation> locations;

    private RecyclerView recyclerView;
    private TripSelectionAdapter adapter;
    private GridLayoutManager layoutManager;
    private GestureDetectorCompat gestureDetector;

    private Realm realm;
    private static int tripPosition;

    private CardView destinationCard;

    public static TripPlannerSelectionFragment newInstance(int position){
        TripPlannerSelectionFragment selection = new TripPlannerSelectionFragment();
        tripPosition = position;
        return selection;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.content_trip_planner, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        adapter = new TripSelectionAdapter((LocationModel.getLocations()));
        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.generateDefaultLayoutParams();
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(getActivity(), new TripPlannerSelectionFragment.RecyclerViewGestureListener());

        realm = RealmController.getInstance().getRealm();
        trip = new TripObject();
        trips = new RealmList<TripOrder>();
        realm.beginTransaction();
        realm.copyToRealm(new TripObject());
        realm.commitTransaction();

        locations = new LocationModel().getLocations();

        return view;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     * A gesture listener for on top of the recycler view.
     */
    private class RecyclerViewGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                destinationCard = (CardView) view.findViewById(R.id.destination_card);
                onClick(recyclerView.getChildAdapterPosition(view));
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    private void onClick(int position) {
        checkSelection(position);
        realm.beginTransaction();
        //RealmController.getInstance().getTripResults().get(tripPosition).setTrips(trips);
        trip.setTrips(trips);
        realm.copyToRealmOrUpdate(trip);
        realm.commitTransaction();
    }

    private void checkSelection(int selection) {
        boolean existed = false;
        System.out.println(trips.toString());
        if (trips.size() != 0) {
            for (int i = 0; i < trips.size(); i++) {
                if (trips.get(i).getLocation() == locations.get(selection)) {
                    trips.remove(i);
                    existed = true;
                    showSelection(selection,existed);
                }
            }
            if (!existed) {
                trips.add(new TripOrder(locations.get(selection)));
                showSelection(selection,existed);
            }
        } else {
            trips.add(new TripOrder(locations.get(selection)));
            showSelection(selection,existed);
        }
    }

    private void showSelection(int selection, boolean isSelected) {
        if (isSelected) {
            destinationCard.setCardBackgroundColor(Color.WHITE);
        } else {
            destinationCard.setCardBackgroundColor(Color.LTGRAY);
        }
    }

}
