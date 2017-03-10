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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerOptionsFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerSelectionFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerTimelineFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerTimesFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerTourTimesFragment;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import io.realm.Realm;

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

    private Realm realm;

    private static int fragment;
    private static final int TOTAL_FRAGMENTS = 4;


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
        fragment = 0;

        setContentView(R.layout.activity_trip_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Trip Planner");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.realm = RealmController.with(this).getRealm();

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

        //This is probably where the adapter goes

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, TripPlannerSelectionFragment.newInstance()).commit();

                fragmentNav.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                create.setVisibility(View.GONE);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trip_planner_timeline, menu);

        final MenuItem newTrip = menu.findItem(R.id.menu_item_new_trip);
        newTrip.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, TripPlannerSelectionFragment.newInstance()).commit();

                fragmentNav.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                create.setVisibility(View.GONE);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setTitle("Trip Creation");
                setSupportActionBar(toolbar);
                return true;
            }
        });

        // Needs to be tested
        // Sets New Trip option to disappear after fragment stuff happens
        if (fragment != 0){
             newTrip.setVisible(false);
        } else {
            newTrip.setVisible(true);
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
                    return TripPlannerSelectionFragment.newInstance();
                case 1:
                    return TripPlannerTimesFragment.newInstance();
                case 2:
                    return TripPlannerOptionsFragment.newInstance();
                case 3:
                    return TripPlannerTimelineFragment.newInstance();
                case 4:
                    return TripPlannerTourTimesFragment.newInstance();
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
