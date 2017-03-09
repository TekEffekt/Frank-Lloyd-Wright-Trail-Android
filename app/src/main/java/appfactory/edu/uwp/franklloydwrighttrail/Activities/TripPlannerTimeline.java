package appfactory.edu.uwp.franklloydwrighttrail.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import appfactory.edu.uwp.franklloydwrighttrail.Apis.DirectionsApi;
import appfactory.edu.uwp.franklloydwrighttrail.Apis.DistanceMatrixApi;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerOptionsFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerSelectionFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerTimesFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerTourTimesFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Models.DirectionsModel;
import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.Models.DistanceModel;
import appfactory.edu.uwp.franklloydwrighttrail.Models.LocationModel;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.Adapters.TimelineAdapter;
import appfactory.edu.uwp.franklloydwrighttrail.Models.TimelineRealmModelAdapter;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import appfactory.edu.uwp.franklloydwrighttrail.TripOrder;
import appfactory.edu.uwp.franklloydwrighttrail.UserLocation;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * Created by sterl on 11/3/2016.
 */

public class TripPlannerTimeline extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TripObject trip;
    private TimelineAdapter adapter;
    private LinearLayoutManager layoutManager;
    public Location location;
    public RealmList<FLWLocation> locations = LocationModel.getLocations();
    RealmList<TripOrder> mTripOrder = new RealmList<>();
    private Realm realm;
    private FragmentPositionAdapter fragmentPositionAdapter;
    public int locationIndex;
    private ViewPager viewPager;
    private ImageView rightFragment;
    private ImageView leftFragment;
    private RelativeLayout fragmentNav;
    private DrawerLayout drawer;
    private RelativeLayout create;
    private RecyclerView timelineView;
    private NavigationView navigationView;

    private static int fragment;
    private final int TOTAL_FRAGMENTS = 2;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TripPlannerTimeline.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = 0;

        setContentView(R.layout.activity_trip_timeline);

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

        // Creates Timeline if there is a trip
        timelineView = (RecyclerView) findViewById(R.id.trip_timeline);
        setupTimeline();

        create = (RelativeLayout) findViewById(R.id.create);
        fragmentNav = (RelativeLayout) findViewById(R.id.fragment_position);
        leftFragment = (ImageView) findViewById(R.id.left_fragment);
        rightFragment = (ImageView) findViewById(R.id.right_fragment);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        if (RealmController.getInstance().hasTrip()){
            if (RealmController.getInstance().getTrip().getStartTime() != RealmController.getInstance().getTrip().getEndTime()){
                create.setVisibility(View.GONE);
                timelineView.setVisibility(View.VISIBLE);
                setRealmAdapter(RealmController.with(this).getTripResults());
                createFinalTripPlan();
                initiateDataCalculation();


            } else {
                timelineView.setVisibility(View.GONE);
                create.setVisibility(View.VISIBLE);
            }
        } else {
            timelineView.setVisibility(View.GONE);
            create.setVisibility(View.VISIBLE);
        }

        //Grab Trip Object
        if (RealmController.getInstance().hasTrip()) {
            trip = RealmController.getInstance().getTripResults().get(0);
        } else {
            realm.beginTransaction();
            trip = new TripObject();
            realm.copyToRealm(trip);
            realm.commitTransaction();
        }

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, TripPlannerSelectionFragment.newInstance()).commit();

                fragmentNav.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                create.setVisibility(View.GONE);
                timelineView.setVisibility(View.GONE);

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
                    Intent intent = new TripPlannerTimeline().newIntent(getBaseContext());
                    startActivity(intent);
                    fragment = 0;
                    finish();
                }

            }
        });

        /*
        if (trip === null) {
            create.setVisibility(View.VISIBLE);
            timelineView.setVisibility(View.GONE);
        } */

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setRealmAdapter(RealmResults<TripObject> trip){
        TimelineRealmModelAdapter realmAdapter = new TimelineRealmModelAdapter(this.getApplicationContext(), trip, true);
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    private void setupTimeline(){
        layoutManager = new LinearLayoutManager(this);
        layoutManager.generateDefaultLayoutParams();
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        timelineView.setLayoutManager(layoutManager);

        adapter = new TimelineAdapter(this);
        timelineView.setAdapter(adapter);
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
                timelineView.setVisibility(View.GONE);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setTitle("Trip Creation");
                setSupportActionBar(toolbar);
                return true;
            }
        });
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
    public int findLocation(int location,RealmList<FLWLocation> locations)
    {
        for(int i=0;i<locations.size();i++){
            if(locations.get(i).getName() == location) {
                return i;
            }

        }
        return -1;
    }
    public void createFinalTripPlan()
    {
        trip = RealmController.getInstance().getTripResults().get(0);
        int i = 0;
        long startTourTime = 0;
        long endTourTime = 0;
        Toast toast;

        while(i< trip.getTrips().size()-1)
        {

            startTourTime = trip.getTrips().get(i+1).getLocation().getStartTourTime();
            endTourTime = trip.getTrips().get(i).getLocation().getEndTourTime();
            if(trip.getTrips().get(i).getLocation().getName() == R.string.user)
            {
                i++;
            }
            else if(startTourTime != 0 && endTourTime != 0)
            {
                if(startTourTime >= (endTourTime + trip.getTrips().get(i).getTimeValue()))
                {

                    i++;
                }
                else
                {
                    toast = Toast.makeText(getApplicationContext(), "Unable to make it to the next location in time. Removed a location from list", Toast.LENGTH_LONG);
                    toast.show();

                    realm.beginTransaction();
                    RealmController.getInstance().getTripResults().get(0).getTrips().remove(i+1);
                    realm.commitTransaction();

                    trip = RealmController.getInstance().getTripResults().get(0);
                    adapter.notifyDataSetChanged();
                    Log.d("debug", "Size: "+trip.getTrips().size());
                    locationIndex = i;
                    if(i < trip.getTrips().size()-1 && trip.getTrips().get(i+1).getLocation().getStartTourTime() != 0){
                        DistanceMatrixApi distanceMatrixApi = DistanceMatrixApi.retrofit.create(DistanceMatrixApi.class);
                        Call<DistanceModel> call2 = distanceMatrixApi.timeDuration("imperial", trip.getTrips().get(i).getLocation().getLatlong(), trip.getTrips().get(i+1).getLocation().getLatlong());

                        call2.enqueue(new Callback<DistanceModel>() {
                            @Override
                            public void onResponse(Call<DistanceModel> call, Response<DistanceModel> response) {
                                if(response.isSuccessful()) {
                                    realm.beginTransaction();
                                    RealmController.getInstance().getTripResults().get(0).getTrips().get(locationIndex).setTimeValue(response.body().getRows().get(0).getElements().get(0).getDuration().getValue()/60 +1);
                                    realm.commitTransaction();
                                    trip = RealmController.getInstance().getTripResults().get(0);
                                    adapter.notifyDataSetChanged();
                                } else {

                                }
                            }
                            @Override
                            public void onFailure(Call<DistanceModel> call, Throwable t) {

                            }
                        });
                    }

                }

            }

            Log.d("debug", "Adapter Size: " + adapter.getItemCount());
        }

    }
    public void createTripPlan(RealmList<TripOrder> tripOrder)
    {
        trip = RealmController.getInstance().getTripResults().get(0);
        int startTime = trip.getStartTime();
        int endTime = trip.getEndTime();
        int totalTime = endTime - startTime;


        int breakfast = trip.getBreakfastTime();


        int lunch = trip.getLunchTime();


        int dinner = trip.getDinnerTime();


        int time = 0;
        Toast toast;
        int mealtime = breakfast+lunch+dinner;

        for(int i=0;i<tripOrder.size();i++)
        {
            if(i==0)
                time += tripOrder.get(i).getTimeValue();
            else
                time += tripOrder.get(i).getTimeValue()+60;
        }
        if(mealtime > totalTime)
        {
            toast = Toast.makeText(getApplicationContext(),"Meals take too much time.",Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(mealtime+time > totalTime)
        {

            time = time - tripOrder.get(tripOrder.size()-1).getTimeValue()-60;
            tripOrder.remove(tripOrder.size()-1);
            if(mealtime+time> totalTime)
            {
                time = time - tripOrder.get(tripOrder.size()-1).getTimeValue()-60;
                tripOrder.remove(tripOrder.size()-1);
                if(mealtime+time>totalTime){
                    time = time - tripOrder.get(tripOrder.size()-1).getTimeValue()-60;
                    tripOrder.remove(tripOrder.size()-1);
                    if(mealtime+time>totalTime){
                        tripOrder.remove(tripOrder.size()-1);
                    }
                    else
                    {
                        toast = Toast.makeText(getApplicationContext(),"Total trip too long 3 sites taken off.",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else
                {
                    toast = Toast.makeText(getApplicationContext(),"Total trip too long 2 sites taken off.",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            else
            {
                toast = Toast.makeText(getApplicationContext(),"Total trip too long 1 site taken off.",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else
        {
        }
        // Fill the trip object with the new times
        TripObject tObject = new TripObject();
        tObject.setTrips(tripOrder);
        tObject.setBreakfastTime(trip.getBreakfastTime());

        tObject.setLunchTime(trip.getLunchTime());

        tObject.setDinnerTime(trip.getDinnerTime());

        tObject.setStartTime(trip.getStartTime());
        tObject.setEndTime(trip.getEndTime());
        // Update realm with the new times
            realm.beginTransaction();

            realm.copyToRealmOrUpdate(tObject);
            realm.commitTransaction();
        RealmController.getInstance().refresh();
        trip = RealmController.getInstance().getTrip();


    }

    //Z's Code
    private void initiateDataCalculation(){

        FLWLocation aLatLong;
        FLWLocation bLatLong;
        final FLWLocation startLocation = new FLWLocation();
        final FLWLocation endLocation;
        String startLatLong;
        String endLatLong;
        int startLoc;
        int endLoc;
        int index;
        int aLoc;
        int bLoc;
        String midLatLong = "optimize:true|";
        trip = RealmController.getInstance().getTripResults().get(0);
        locations = new RealmList<>();

        for ( TripOrder tp: trip.getTrips())
              {
                  locations.add(tp.getLocation());


        }
        // Get the users location from realm
        UserLocation ul = RealmController.getInstance().getUserLocation();
        Location myLocation = new Location("user");
        myLocation.setLatitude(ul.getLatitude());
        myLocation.setLongitude(ul.getLongitude());
        startLocation.setLatitude(ul.getLatitude());
        startLocation.setLongitude(ul.getLongitude());
        startLocation.setLatlong(ul.getLatitude()+","+ul.getLongitude());
        startLocation.setName(R.string.user);
        startLatLong = startLocation.getLatlong();
        startLocation.setImage(android.R.color.transparent);
        if(findLocation(R.string.user,locations) != -1)
        {
            locations.remove(0);
            locations.add(0,startLocation);
        }
        else
        {
            locations.add(0,startLocation);
        }

        if(findLocation(R.string.user,locations) == -1)
        {
            if (location == null) {
                location = new Location("default");
                location.setLatitude(42.7152375);
                location.setLongitude(-87.7906969);
            }
             myLocation = location;
            // Sets start location to the users location
            startLocation.setLatitude(myLocation.getLatitude());
            startLocation.setLongitude(myLocation.getLongitude());
            startLocation.setLatlong(myLocation.getLatitude()+","+myLocation.getLongitude());
            startLocation.setName(R.string.user);
            startLatLong = startLocation.getLatlong();
            startLocation.setImage(android.R.color.transparent);
            locations.add(0,startLocation);


        }

        startLoc = findLocation(R.string.user,locations);
        startLocation.setLatitude(locations.get(startLoc).getLatitude());
        startLocation.setLongitude(locations.get(startLoc).getLongitude());
        startLocation.setLatlong(startLocation.getLatitude() + ","+ startLocation.getLongitude());
        startLocation.setName(locations.get(startLoc).getName());
        startLocation.setImage(locations.get(startLoc).getImage());
        startLatLong = startLocation.getLatlong();




        // Find one end point
        index = findLocation(R.string.scjohnson,locations);

        if(index == -1)
        {
            index = findLocation(R.string.wingspread,locations);
            if(index == -1)
            {
                index = findLocation(R.string.built_homes,locations);
                if(index == -1)
                {
                    index = findLocation(R.string.meeting_house, locations);
                    if (index == -1)
                    {
                        index = findLocation(R.string.monona_terrace, locations);
                        if (index == -1)
                        {
                            index = findLocation(R.string.visitor_center, locations);
                            if(index == -1)
                                index = findLocation(R.string.valley_school,locations);

                        }
                    }
                }
            }
        }

        aLatLong = locations.get(index);
        aLoc = index;
        index = findLocation(R.string.german_warehouse, locations);
        // Find the second end point
        if(index == -1) {
            index = findLocation(R.string.valley_school,locations);
            if(index == -1) {
                index = findLocation(R.string.visitor_center, locations);
                if (index == -1) {
                    index = findLocation(R.string.meeting_house, locations);
                    if (index == -1) {
                        index = findLocation(R.string.monona_terrace, locations);
                        if (index == -1) {
                            index = findLocation(R.string.built_homes,locations);
                            if(index == -1) {
                                index = findLocation(R.string.wingspread, locations);
                            }
                        }
                    }
                }
            }
        }

        bLatLong = locations.get(index);
        bLoc = index;

        Location locationA = new Location("point A");
        locationA.setLatitude(aLatLong.getLatitude());
        locationA.setLongitude(aLatLong.getLongitude());
        Location locationB = new Location("point B");
        locationB.setLatitude(bLatLong.getLatitude());
        locationB.setLongitude(bLatLong.getLongitude());
        // Check which end point is closer to the user location
        if(myLocation.distanceTo(locationA) < myLocation.distanceTo(locationB)) {

            endLocation = bLatLong;
            endLatLong = bLatLong.getLatlong();
            endLoc = bLoc;
        } else {

            endLocation = aLatLong;
            endLatLong = aLatLong.getLatlong();
            endLoc = aLoc;
        }
        locations.remove(endLoc);
        locations.add(endLocation);
        endLoc = findLocation(endLocation.getName(),locations);
        String [] middleLatLong = new String[locations.size()-2];
        int j=0;
        // Put the middle locations in an array
        for(int i=0;i<locations.size();i++) {
            if(startLoc != i && endLoc != i) {
                middleLatLong[j] = locations.get(i).getLatlong();
                j++;
            }
        }
        // Create the middle locations string for the api
        for(int i=0;i<middleLatLong.length;i++) {
            if(i!= middleLatLong.length-1) {
                midLatLong += middleLatLong[i] + "|";
            } else {
                midLatLong += middleLatLong[i];
            }
        }
        // Call the Directions api to get the order and travel times for each site
        DirectionsApi directionsApi = DirectionsApi.retrofit.create(DirectionsApi.class);
        Call<DirectionsModel> call2 = directionsApi.directions(startLatLong,endLatLong,midLatLong);

        call2.enqueue(new Callback<DirectionsModel>() {
            @Override
            public void onResponse(Call<DirectionsModel> call, Response<DirectionsModel> response) {
                if(response.isSuccessful()) {
                    int j=0;
                    ArrayList<Integer> waypointOrder = new ArrayList<>();
                    // Grab the order of the middle sites
                    for(int k=0;k<response.body().getRoutes().get(0).getWaypointOrder().size();k++) {
                        waypointOrder.add(response.body().getRoutes().get(0).getWaypointOrder().get(k)+1);
                    }
                    for(int i=0;i<=response.body().getRoutes().get(0).getLegs().size();i++) {
                        // Get travel time for the start location
                        if(i==0) {
                            TripOrder trip = new TripOrder(startLocation,response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText(),response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue());
                            trip.setTimeValue(trip.getTimeValue()/60);
                            mTripOrder.add(trip);
                        }
                        // Get travel time for the end location
                        else if(i==response.body().getRoutes().get(0).getLegs().size()) {
                            TripOrder trip = new TripOrder(endLocation,"",0);
                            trip.setTimeValue(trip.getTimeValue()/60);
                            mTripOrder.add(trip);
                        }
                        // Get the travel time for the middle locations
                        else {
                            TripOrder trip = new TripOrder(locations.get(waypointOrder.get(j)),response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText(),response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue());
                            trip.setTimeValue(trip.getTimeValue()/60);
                            mTripOrder.add(trip);
                            j++;
                        }
                    }
                    createTripPlan(mTripOrder);
                    adapter.notifyDataSetChanged();
                } else {

                }
            }
            @Override
            public void onFailure(Call<DirectionsModel> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();


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
                    return TripPlannerTourTimesFragment.newInstance();
                case 3:
                    return TripPlannerOptionsFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}