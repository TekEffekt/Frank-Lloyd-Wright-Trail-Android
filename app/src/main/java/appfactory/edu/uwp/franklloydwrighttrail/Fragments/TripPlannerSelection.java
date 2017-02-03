package appfactory.edu.uwp.franklloydwrighttrail.Fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class TripPlannerSelection extends Fragment implements RecyclerView.OnItemTouchListener{
    public TripObject trip;
    private RealmList<FLWLocation> locations;
    private Button cont;

    private RecyclerView recyclerView;
    private TripSelectionAdapter adapter;
    private GridLayoutManager layoutManager;
    private GestureDetectorCompat gestureDetector;

    private Realm realm;

    private CardView destinationCard;

    public static TripPlannerSelection newInstance(){
        TripPlannerSelection selection = new TripPlannerSelection();
        return selection;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_trip_planner, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        adapter = new TripSelectionAdapter((LocationModel.getLocations()));
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.choose_destinations);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.generateDefaultLayoutParams();
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(getActivity(), new TripPlannerSelection.RecyclerViewGestureListener());

        realm = RealmController.getInstance().getRealm();
        //resetTrip();
        trip = new TripObject();
        locations = new LocationModel().getLocations();

        cont = (Button) view.findViewById(R.id.cont);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trip.getTrips().size() != 0) {
                    realm.beginTransaction();
                    RealmResults<TripObject> results = realm.where(TripObject.class).findAll();
                    results.clear();
                    realm.copyToRealm(trip);
                    realm.commitTransaction();
                    //Transition to next page
                } else {
                    //make toast yelling at user
                }
            }
        });

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
    }

    private void checkSelection(int selection) {
        boolean existed = false;
        if (trip.getTrips().size() != 0) {
            for (int i = 0; i < trip.getTrips().size(); i++) {
                if (trip.getTrips().get(i).getLocation() == locations.get(selection)) {
                    trip.getTrips().remove(i);
                    existed = true;
                    showSelection(selection,existed);
                }
            }
            if (!existed) {
                trip.getTrips().add(new TripOrder(locations.get(selection)));
                showSelection(selection,existed);
            }
        } else {
            trip.getTrips().add(new TripOrder(locations.get(selection)));
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

    private void resetTrip(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TripObject> results = realm.where(TripObject.class).findAll();
                results.clear();
            }
        });
    }

}
