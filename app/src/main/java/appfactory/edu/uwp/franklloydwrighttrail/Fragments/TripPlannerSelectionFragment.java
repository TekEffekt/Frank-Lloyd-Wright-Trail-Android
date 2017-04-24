package appfactory.edu.uwp.franklloydwrighttrail.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;

import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.Models.LocationModel;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import appfactory.edu.uwp.franklloydwrighttrail.TripOrder;
import appfactory.edu.uwp.franklloydwrighttrail.Adapters.TripSelectionAdapter;
import io.realm.Realm;
import io.realm.RealmList;
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

    private Button cont;

    private Realm realm;
    private static String tripPosition;

    private CardView destinationCard;

    public static TripPlannerSelectionFragment newInstance(String position){
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

        cont = (Button) view.findViewById(R.id.cont);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, TripPlannerCreateTripFragment.newInstance(tripPosition)).commit();
            }
        });

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
    }

    private void checkSelection(int selection) {
        boolean existed = false;
        if (RealmController.getInstance().getTripResults(tripPosition).get(0).getTrips().size() != 0) {
            for (int i = 0; i < RealmController.getInstance().getTripResults(tripPosition).get(0).getTrips().size(); i++) {
                if (RealmController.getInstance().getTripResults(tripPosition).get(0).getTrips().get(i).getLocation() == locations.get(selection)) {
                    realm.beginTransaction();
                    RealmController.getInstance().getTripResults(tripPosition).get(0).getTrips().remove(i);
                    realm.commitTransaction();
                    existed = true;
                    showSelection(selection,existed);
                }
            }
            if (!existed) {
                realm.beginTransaction();
                RealmController.getInstance().getTripResults(tripPosition).get(0).getTrips().add(new TripOrder(locations.get(selection)));
                realm.commitTransaction();
                showSelection(selection,existed);
            }
        } else {
            realm.beginTransaction();
            RealmController.getInstance().getTripResults(tripPosition).get(0).getTrips().add(new TripOrder(locations.get(selection)));
            realm.commitTransaction();
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
