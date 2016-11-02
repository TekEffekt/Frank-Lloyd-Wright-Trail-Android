package appfactory.edu.uwp.franklloydwrighttrail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by sterl on 10/28/2016.
 */

public class TripPlannerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerView.OnItemTouchListener{
    private DrawerLayout drawer;
    private TripObject trip;
    private ArrayList<FLWLocation> locations;
    private Button cont;

    private RecyclerView recyclerView;
    private TripSelectionAdapter adapter;
    private GridLayoutManager layoutManager;
    private GestureDetectorCompat gestureDetector;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TripPlannerActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new TripSelectionAdapter((LocationModel.getLocations()));
        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(this, 2);
        layoutManager.generateDefaultLayoutParams();
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(this, new TripPlannerActivity.RecyclerViewGestureListener());

        cont = (Button) findViewById(R.id.cont);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripPlannerActivity.this, TripPlannerTimes.class);
                intent.putExtra("Trip", trip.getTrips());
                TripPlannerActivity.this.startActivity(intent);
            }
        });

        trip = new TripObject();
        locations = new LocationModel().getLocations();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_locations:
                break;
            case R.id.nav_trip_planner:
                break;
            case R.id.nav_scrapbook:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_about:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            onClick(recyclerView.getChildAdapterPosition(view));
            return super.onSingleTapConfirmed(e);
        }
    }

    private void onClick(int position) {
        boolean exists = false;
        switch (position) {
            case 0:
                //SC Johnson
                if (trip.getTrips().size() == 0) {
                    for (int i = 0; i < trip.getTrips().size(); i++) {
                        if (trip.getTrips().get(i) == locations.get(0)) {
                            trip.getTrips().remove(i);
                            exists = true;
                        }
                    }
                    if (!exists) {
                        trip.getTrips().add(locations.get(0));
                    }
                } else {
                    trip.getTrips().add(locations.get(0));
                }
                break;
            case 1:
                //Wingspread
                if (trip.getTrips().size() == 0) {
                    for (int i = 0; i < trip.getTrips().size(); i++) {
                        if (trip.getTrips().get(i) == locations.get(1)) {
                            trip.getTrips().remove(i);
                            exists = true;
                        }
                    }
                    if (!exists) {
                        trip.getTrips().add(locations.get(1));
                    }
                } else {
                    trip.getTrips().add(locations.get(1));
                }
                break;
            case 2:
                //Monona Terrace
                if (trip.getTrips().size() == 0) {
                    for (int i = 0; i < trip.getTrips().size(); i++) {
                        if (trip.getTrips().get(i) == locations.get(2)) {
                            trip.getTrips().remove(i);
                            exists = true;
                        }
                    }
                    if (!exists) {
                        trip.getTrips().add(locations.get(2));
                    }
                } else {
                    trip.getTrips().add(locations.get(2));
                }
                break;
            case 3:
                //Meeting House
                if (trip.getTrips().size() == 0) {
                    for (int i = 0; i < trip.getTrips().size(); i++) {
                        if (trip.getTrips().get(i) == locations.get(3)) {
                            trip.getTrips().remove(i);
                            exists = true;
                        }
                    }
                    if (!exists) {
                        trip.getTrips().add(locations.get(3));
                    }
                } else {
                    trip.getTrips().add(locations.get(3));
                }
                break;
            case 4:
                //FLW Visitor Center
                if (trip.getTrips().size() == 0) {
                    for (int i = 0; i < trip.getTrips().size(); i++) {
                        if (trip.getTrips().get(i) == locations.get(4)) {
                            trip.getTrips().remove(i);
                            exists = true;
                        }
                    }
                    if (!exists) {
                        trip.getTrips().add(locations.get(4));
                    }
                } else {
                    trip.getTrips().add(locations.get(4));
                }
                break;
            case 5:
                //German Warehouse
                if (trip.getTrips().size() == 0) {
                    for (int i = 0; i < trip.getTrips().size(); i++) {
                        if (trip.getTrips().get(i) == locations.get(5)) {
                            trip.getTrips().remove(i);
                            exists = true;
                        }
                    }
                    if (!exists) {
                        trip.getTrips().add(locations.get(5));
                    }
                } else {
                    trip.getTrips().add(locations.get(5));
                }
                break;
        }
    }
}
