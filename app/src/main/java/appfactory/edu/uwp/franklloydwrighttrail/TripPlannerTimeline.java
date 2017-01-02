package appfactory.edu.uwp.franklloydwrighttrail;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vipul.hp_hp.timelineview.TimelineView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sterl on 11/3/2016.
 */

public class TripPlannerTimeline extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Button create;
    private TripObject trip;
    private RecyclerView timelineView;
    private TimelineAdapter adapter;
    private LinearLayoutManager layoutManager;
    public ArrayList<FLWLocation> locations = LocationModel.getLocations();
    ArrayList<TripOrder> mTripOrder = new ArrayList<>();
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
    public int findLocation(int location,ArrayList<FLWLocation> locations)
    {
        for(int i=0;i<locations.size();i++){
            if(locations.get(i).getName() == location) {
                return i;
            }

        }
        return -1;
    }

    public void createTripPlan(ArrayList<TripOrder> tripOrder)
    {
        long breakfast = 3600;
        breakfast = breakfast/60;
        Log.d("debug", "breakfast: "+breakfast);
        long lunch = 3600;
        lunch = lunch/60;
        Log.d("debug", "lunch: "+lunch);
        long dinner = 3600;
        dinner = dinner/60;
        Log.d("debug", "dinner: "+dinner);
        int startHour = 9;
        int startMin = 30;
        int endHour = 7;
        int endMin= 30;
        long totalTime = 36000;
        totalTime = totalTime/60;
        int time = 0;
        Toast toast;
        long mealtime = breakfast+lunch+dinner;
        Log.d("debug", "mealtime: "+ mealtime);
        for(int i=0;i<tripOrder.size();i++)
        {
            time += tripOrder.get(i).getTimeValue()/60+60;
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

    }

    //Z's Code
    private void initiateDataCalculation(){

        FLWLocation aLatLong;
        FLWLocation bLatLong;
        final FLWLocation startLocation;
        final FLWLocation endLocation;
        String startLatLong;
        String endLatLong;
        int startLoc;
        int endLoc;
        int index;
        int aLoc;
        int bLoc;
        String midLatLong = "optimize:true|";
        String [] middleLatLong = new String[locations.size()-2];
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
        Location myLocation = new Location ("my location");
        myLocation.setLatitude(LocationSelectionActivity.myLocation.getLatitude());
        myLocation.setLongitude(LocationSelectionActivity.myLocation.getLongitude());
        Location locationA = new Location("point A");
        locationA.setLatitude(aLatLong.getLatitude());
        locationA.setLongitude(aLatLong.getLongitude());
        Location locationB = new Location("point B");
        locationB.setLatitude(bLatLong.getLatitude());
        locationB.setLongitude(bLatLong.getLongitude());

        if(myLocation.distanceTo(locationA) < myLocation.distanceTo(locationB)) {
            startLocation = aLatLong;
            startLatLong = aLatLong.getLatlong();
            startLoc = aLoc;
            endLocation = bLatLong;
            endLatLong = bLatLong.getLatlong();
            endLoc = bLoc;
        } else {
            startLocation = bLatLong;
            startLatLong = bLatLong.getLatlong();
            startLoc = bLoc;
            endLocation = aLatLong;
            endLatLong = aLatLong.getLatlong();
            endLoc = aLoc;
        }
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
                    for(int i=0;i<=response.body().getRoutes().get(0).getLegs().size();i++) {
                        if(i==0) {
                            TripOrder trip = new TripOrder(startLocation,response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText(),response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue());
                            mTripOrder.add(trip);
                        }
                        else if(i==response.body().getRoutes().get(0).getLegs().size()) {
                            TripOrder trip = new TripOrder(endLocation,"",0);
                            mTripOrder.add(trip);
                        } else {
                            TripOrder trip = new TripOrder(locations.get(waypointOrder.get(j)),response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText(),response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue());
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