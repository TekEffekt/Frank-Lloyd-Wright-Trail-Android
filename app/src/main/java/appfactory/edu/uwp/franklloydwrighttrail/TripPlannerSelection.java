package appfactory.edu.uwp.franklloydwrighttrail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by sterl on 10/28/2016.
 */

public class TripPlannerSelection extends AppCompatActivity implements RecyclerView.OnItemTouchListener{
    private DrawerLayout drawer;
    public TripObject trip;
    private ArrayList<FLWLocation> locations;
    private Button cont;

    private RecyclerView recyclerView;
    private TripSelectionAdapter adapter;
    private GridLayoutManager layoutManager;
    private GestureDetectorCompat gestureDetector;

    private static final int SELECTION_ACTIVITY = 1;

    private Realm realm;

    private CardView destinationCard;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TripPlannerSelection.class);
        intent.putExtra("SELECTION", SELECTION_ACTIVITY);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planner);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new TripSelectionAdapter((LocationModel.getLocations()));
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.choose_destinations);
        setSupportActionBar(toolbar);

        layoutManager = new GridLayoutManager(this, 2);
        layoutManager.generateDefaultLayoutParams();
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(this, new TripPlannerSelection.RecyclerViewGestureListener());

        realm = RealmController.getInstance().getRealm();
        //resetTrip();
        trip = new TripObject();
        locations = new LocationModel().getLocations();

        cont = (Button) findViewById(R.id.cont);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trip.getTrips().size() != 0) {
                    realm.beginTransaction();
                    RealmResults<TripObject> results = realm.where(TripObject.class).findAll();
                    results.clear();
                    realm.copyToRealm(trip);
                    realm.commitTransaction();
                    Intent intent = new Intent(TripPlannerSelection.this, TripPlannerTimes.class);
                    TripPlannerSelection.this.startActivity(intent);
                    finish();
                } else {
                    //make toast yelling at user
                }
            }
        });
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
