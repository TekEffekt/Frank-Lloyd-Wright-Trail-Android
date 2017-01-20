package appfactory.edu.uwp.franklloydwrighttrail.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
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
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import appfactory.edu.uwp.franklloydwrighttrail.DirectionsApi;
import appfactory.edu.uwp.franklloydwrighttrail.Models.DirectionsModel;
import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.Models.LocationModel;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.Adapters.TimelineAdapter;
import appfactory.edu.uwp.franklloydwrighttrail.Models.TimelineRealmModelAdapter;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import appfactory.edu.uwp.franklloydwrighttrail.TripOrder;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static appfactory.edu.uwp.franklloydwrighttrail.Activities.LocationSelectionActivity.myLocation;

/**
 * Created by sterl on 11/3/2016.
 */

public class TripPlannerTimeline extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    private DrawerLayout drawer;
    private Button create;
    private TripObject trip;
    private RecyclerView timelineView;
    private TimelineAdapter adapter;
    private LinearLayoutManager layoutManager;
    private final static boolean forceNetwork = false;
    public Location location;
    private LocationManager locationManager;
    public RealmList<FLWLocation> locations = LocationModel.getLocations();
    RealmList<TripOrder> mTripOrder = new RealmList<>();
    private Realm realm;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TripPlannerTimeline.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trip_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Trip Planner");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.realm = RealmController.with(this).getRealm();

        // Creates Timeline if there is a trip
        timelineView = (RecyclerView) findViewById(R.id.trip_timeline);
        setupTimeline();
        create = (Button) findViewById(R.id.create);
        if (RealmController.getInstance().hasTrip()){
            if (RealmController.getInstance().getTrip().getStartTime() != RealmController.getInstance().getTrip().getEndTime()){
                create.setVisibility(View.GONE);
                timelineView.setVisibility(View.VISIBLE);
                setRealmAdapter(RealmController.with(this).getTripResults());
                initLocationService(this);
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
            trip = RealmController.getInstance().getTrip();
        } else {
            realm.beginTransaction();
            trip = new TripObject();
            realm.copyToRealm(trip);
            realm.commitTransaction();
        }

        create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripPlannerTimeline.this, TripPlannerSelection.class);
                TripPlannerTimeline.this.startActivity(intent);
                finish();
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
                Intent intent = new Intent(TripPlannerTimeline.this, TripPlannerSelection.class);
                TripPlannerTimeline.this.startActivity(intent);
                finish();
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
            //case R.id.nav_scrapbook:
            //    break;
            case R.id.nav_settings:
                break;
            case R.id.nav_about:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public void createTripPlan(RealmList<TripOrder> tripOrder)
    {
        int startTime = trip.getStartTime();
        int endTime = trip.getEndTime();
        int totalTime = endTime - startTime;
        Log.d("debug", "TotalTimeRealm: "+totalTime);

        int breakfast = trip.getBreakfastTime();

        Log.d("debug", "breakfast: "+breakfast);
        int lunch = trip.getLunchTime();

        Log.d("debug", "lunch: "+lunch);
        int dinner = trip.getDinnerTime();

        Log.d("debug", "dinner: "+dinner);
        int time = 0;
        Toast toast;
        int mealtime = breakfast+lunch+dinner;
        Log.d("debug", "mealtime: "+ mealtime);

        for(int i=0;i<tripOrder.size();i++)
        {
            if(i==0)
                time += tripOrder.get(i).getTimeValue();
            else
                time += tripOrder.get(i).getTimeValue()+60;
        }
        Log.d("debug", "time: "+time);
        if(mealtime > totalTime)
        {
            Log.d("debug", "meals take too much time.");
            toast = Toast.makeText(getApplicationContext(),"Meals take too much time.",Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(mealtime+time > totalTime)
        {
            Log.d("debug", "total trip too long ");

            time = time - tripOrder.get(tripOrder.size()-1).getTimeValue()-60;
            tripOrder.remove(tripOrder.size()-1);
            if(mealtime+time> totalTime)
            {
                Log.d("debug", "total trip still too long ");
                time = time - tripOrder.get(tripOrder.size()-1).getTimeValue()-60;
                tripOrder.remove(tripOrder.size()-1);
                if(mealtime+time>totalTime){
                    Log.d("debug", "total trip is still too long ");
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
                    Log.d("debug", "enough time with 2 sites taken off ");
                    toast = Toast.makeText(getApplicationContext(),"Total trip too long 2 sites taken off.",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            else
            {
                Log.d("debug", "enough time with 1 site taken off ");
                toast = Toast.makeText(getApplicationContext(),"Total trip too long 1 site taken off.",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else
        {
            Log.d("debug", "enough time ");
        }
        TripObject tObject = new TripObject();
        tObject.setTrips(tripOrder);
        tObject.setBreakfastTime(trip.getBreakfastTime());

        tObject.setLunchTime(trip.getLunchTime());

        tObject.setDinnerTime(trip.getDinnerTime());
        
        tObject.setStartTime(trip.getStartTime());
        tObject.setEndTime(trip.getEndTime());
            realm.beginTransaction();

            realm.copyToRealmOrUpdate(tObject);
            realm.commitTransaction();
        RealmController.getInstance().refresh();
        trip = RealmController.getInstance().getTrip();
        Log.d("debug", "realmTrip: " + trip.getTrips());


    }
    @Override
    public void onProviderEnabled(String provider){

    }
    @Override
    public void onProviderDisabled(String provider){

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){

    }
    @Override
    public void onLocationChanged(Location location){

    }
    @TargetApi(23)
    private void initLocationService(Context context)
    {
        boolean isGPSEnabled = false;

        if(Build.VERSION.SDK_INT>=23 && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        try{
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(forceNetwork) isGPSEnabled = false;



                if(isGPSEnabled){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,10,this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }

        }catch (Exception ex)
        {
            Log.d("debug", "Error creating service: " + ex.getMessage());
        }
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
        trip = RealmController.getInstance().getTrip();
        locations = new RealmList<>();
        for ( TripOrder tp: trip.getTrips())
              {
                  locations.add(tp.getLocation());


        }

        if(findLocation(R.string.user,locations) == -1)
        {
            Location myLocation = location;
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

        Log.d("debug", "location: "+ locations);



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

        if(myLocation.distanceTo(locationA) < myLocation.distanceTo(locationB)) {

            endLocation = bLatLong;
            endLatLong = bLatLong.getLatlong();
            endLoc = bLoc;
        } else {

            endLocation = aLatLong;
            endLatLong = aLatLong.getLatlong();
            endLoc = aLoc;
        }
        Log.d("debug", "aloc: "+aLoc);
        Log.d("debug", "bloc: "+bLoc);
        locations.remove(endLoc);
        locations.add(endLocation);
        endLoc = findLocation(endLocation.getName(),locations);
        String [] middleLatLong = new String[locations.size()-2];
        int j=0;
        for(int i=0;i<locations.size();i++) {
            if(startLoc != i && endLoc != i) {
                middleLatLong[j] = locations.get(i).getLatlong();
                j++;
            }
        }
        for(int i=0;i<middleLatLong.length;i++) {
            if(i!= middleLatLong.length-1) {
                midLatLong += middleLatLong[i] + "|";
            } else {
                midLatLong += middleLatLong[i];
            }
        }
        Log.d("debug", "midLatLong: "+ midLatLong);
        DirectionsApi directionsApi = DirectionsApi.retrofit.create(DirectionsApi.class);
        Call<DirectionsModel> call2 = directionsApi.directions(startLatLong,endLatLong,midLatLong);
        Log.d("debug", "onCreate: "+startLatLong+"  "+endLatLong+"  "+midLatLong);
        call2.enqueue(new Callback<DirectionsModel>() {
            @Override
            public void onResponse(Call<DirectionsModel> call, Response<DirectionsModel> response) {
                if(response.isSuccessful()) {
                    int j=0;
                    ArrayList<Integer> waypointOrder = new ArrayList<>();
                    Log.d("debug", "waypointOrder: "+ response.body().getRoutes().get(0).getWaypointOrder().toString());
                    for(int k=0;k<response.body().getRoutes().get(0).getWaypointOrder().size();k++) {
                        waypointOrder.add(response.body().getRoutes().get(0).getWaypointOrder().get(k)+1);

                    }
                    Log.d("debug", "actualwaypointOrder: "+waypointOrder.toString());
                    for(int l = 0;l<locations.size();l++)
                    {
                        Log.d("debug", "locations: "+locations.get(l).getLatlong());

                    }
                    for(int i=0;i<=response.body().getRoutes().get(0).getLegs().size();i++) {
                        if(i==0) {
                            TripOrder trip = new TripOrder(startLocation,response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText(),response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue());
                            trip.setTimeValue(trip.getTimeValue()/60);
                            Log.d("debug", "startLocation: "+startLocation.getLatlong());
                            mTripOrder.add(trip);
                        }
                        else if(i==response.body().getRoutes().get(0).getLegs().size()) {
                            TripOrder trip = new TripOrder(endLocation,"",0);
                            trip.setTimeValue(trip.getTimeValue()/60);
                            Log.d("debug", "endLocation: "+endLocation.getLatlong());
                            mTripOrder.add(trip);
                        } else {
                            TripOrder trip = new TripOrder(locations.get(waypointOrder.get(j)),response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText(),response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue());
                            trip.setTimeValue(trip.getTimeValue()/60);
                            Log.d("debug", "middleLocations: "+locations.get(waypointOrder.get(j)).getLatlong());
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
}