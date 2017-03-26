package appfactory.edu.uwp.franklloydwrighttrail.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import appfactory.edu.uwp.franklloydwrighttrail.Adapters.TourMenuAdapter;
import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerCreateTripFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerOptionsFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerSelectionFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerTimelineFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerTimesFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerTourTimesFragment;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by sterl on 3/1/2017.
 */

public class TripPlannerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    private RelativeLayout create;
    private NavigationView navigationView;

    private RecyclerView recycler;
    private TourMenuAdapter adapter;
    private LinearLayoutManager layoutManager;
    public static HashMap<Date, ArrayList<FLWLocation>> hm = new HashMap<>();
    private Realm realm;
    private static String newTripPosition;
    private boolean viewingFragment = false;


    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TripPlannerActivity.class);
        return intent;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (viewingFragment) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragmentManager.getFragments().get(0)).commit();

            recycler.setVisibility(View.VISIBLE);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Trip Planner");
            setSupportActionBar(toolbar);
            viewingFragment = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_menu);

        this.realm = RealmController.with(this).getRealm();
        newTripPosition = UUID.randomUUID().toString(); // ensures it's random
        create = (RelativeLayout) findViewById(R.id.create);

        setupNavMenu();
        setupRecycler();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTrip();
            }
        });
    }

    private void setupNavMenu(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Trip Planner");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupRecycler(){
        recycler =(RecyclerView) findViewById(R.id.recycler);

        RealmResults realmResults = RealmController.getInstance().getTripResults();
        TripObject[] trips = Arrays.copyOf(realmResults.toArray(), realmResults.toArray().length, TripObject[].class);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.generateDefaultLayoutParams();
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recycler.setLayoutManager(layoutManager);

        adapter = new TourMenuAdapter();
        recycler.setAdapter(adapter);

        if (trips.length > 0){
            recycler.setVisibility(View.VISIBLE);
            create.setVisibility(View.GONE);
        } else {
            recycler.setVisibility(View.GONE);
            create.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trip_planner_timeline, menu);
        final MenuItem addTrip = menu.findItem(R.id.menu_item_add_trip);
        addTrip.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                addTrip();
                item.setVisible(false);
                return true;
            }
        });
        return true;
    }

    private void addTrip(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, TripPlannerCreateTripFragment.newInstance(newTripPosition)).commit();

        create.setVisibility(View.GONE);
        recycler.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Trip Creation");
        setSupportActionBar(toolbar);
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
                finish();
                break;
            case R.id.nav_trip_planner:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_about:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void showTrip(String position){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, TripPlannerCreateTripFragment.newInstance(position)).commit();

        create.setVisibility(View.GONE);
        recycler.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Trip Creation");
        setSupportActionBar(toolbar);
        viewingFragment = true;
    }

    public void showTimeline(boolean isFinal, String position){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, TripPlannerTimelineFragment.newInstance(isFinal, position)).commit();

        create.setVisibility(View.GONE);
        recycler.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Trip Creation");
        setSupportActionBar(toolbar);
        viewingFragment = true;
    }
}
