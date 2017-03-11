package appfactory.edu.uwp.franklloydwrighttrail.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import appfactory.edu.uwp.franklloydwrighttrail.Adapters.TourMenuAdapter;
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

    private FragmentPositionAdapter fragmentPositionAdapter;
    private ViewPager viewPager;
    private ImageView rightFragment;
    private ImageView leftFragment;
    private RelativeLayout fragmentNav;
    private DrawerLayout drawer;
    private RelativeLayout create;
    private NavigationView navigationView;

    private RecyclerView recycler;
    private TourMenuAdapter adapter;
    private LinearLayoutManager layoutManager;

    private Realm realm;

    private static int fragment;
    private static final int TOTAL_FRAGMENTS = 4;
    private static int newTripPosition;


    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TripPlannerActivity.class);
        return intent;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_menu);

        this.realm = RealmController.with(this).getRealm();
        newTripPosition = RealmController.getInstance().getTripResults().size(); // ensures it's always one more than normal
        fragment = 0;

        setupNavMenu();
        setupViewPager();
        setupRecycler();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, TripPlannerSelectionFragment.newInstance(newTripPosition)).commit();

                fragmentNav.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                create.setVisibility(View.GONE);
                recycler.setVisibility(View.GONE);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setTitle("Trip Creation");
                setSupportActionBar(toolbar);
            }
        });

        fragmentPositionAdapter = new FragmentPositionAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPositionAdapter);

        leftFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment--;
                viewPager.setCurrentItem(previousFragmentIndex());
            }
        });

        rightFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment < TOTAL_FRAGMENTS){
                    fragment++;
                    viewPager.setCurrentItem(nextFragmentIndex());
                } else {
                    Intent intent = new TripPlannerActivity().newIntent(getBaseContext());
                    startActivity(intent);
                    fragment = 0;
                    finish();
                }

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

    private void setupViewPager(){
        create = (RelativeLayout) findViewById(R.id.create);
        fragmentNav = (RelativeLayout) findViewById(R.id.fragment_position);
        leftFragment = (ImageView) findViewById(R.id.left_fragment);
        rightFragment = (ImageView) findViewById(R.id.right_fragment);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);

        if (RealmController.getInstance().hasTrip()){
            if (RealmController.getInstance().getTrip().getStartTime() != RealmController.getInstance().getTrip().getEndTime()){
                create.setVisibility(View.GONE);
            } else {
                create.setVisibility(View.VISIBLE);
            }
        } else {
            create.setVisibility(View.VISIBLE);
        }
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

        adapter = new TourMenuAdapter(trips);
        recycler.setAdapter(adapter);

        if (trips.length > 0){
            recycler.setVisibility(View.VISIBLE);
        } else {
            recycler.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trip_planner_timeline, menu);

        final MenuItem addTrip = menu.findItem(R.id.menu_item_add_trip);
        final MenuItem removeTrip = menu.findItem(R.id.menu_item_remove_trip);
        addTrip.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, TripPlannerSelectionFragment.newInstance(newTripPosition)).commit();

                fragmentNav.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                create.setVisibility(View.GONE);
                recycler.setVisibility(View.GONE);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setTitle("Trip Creation");
                setSupportActionBar(toolbar);
                return true;
            }
        });
        removeTrip.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Program to delete item
                return false;
            }
        });

        // Needs to be tested
        // Sets New Trip option to disappear after fragment stuff happens
        if (fragment != 0){
            addTrip.setVisible(false);
            removeTrip.setVisible(false);
        } else {
            addTrip.setVisible(true);
            removeTrip.setVisible(true);
        }

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

    public void setFragmentIndex(int fragmentIndex){
        fragment = fragmentIndex;
    }

    public int getFragmentIndex(){
        return fragment;
    }

    public int nextFragmentIndex(){
        if (fragment >= TOTAL_FRAGMENTS) {
            fragment = TOTAL_FRAGMENTS;
        }
        return fragment;
    }

    public int previousFragmentIndex(){
        if (fragment <= 0) {
            fragment = 0;
        }

        return fragment;
    }

    public static class FragmentPositionAdapter extends FragmentStatePagerAdapter {

        public FragmentPositionAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return TripPlannerSelectionFragment.newInstance(newTripPosition);
                case 1:
                    return TripPlannerTimesFragment.newInstance(newTripPosition);
                case 2:
                    return TripPlannerOptionsFragment.newInstance(newTripPosition);
                case 3:
                    return TripPlannerTimelineFragment.newInstance(false, newTripPosition);
                case 4:
                    return TripPlannerTourTimesFragment.newInstance(newTripPosition);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TOTAL_FRAGMENTS + 1;
        }
    }
}
