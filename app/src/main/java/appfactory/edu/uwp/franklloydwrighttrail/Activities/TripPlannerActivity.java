package appfactory.edu.uwp.franklloydwrighttrail.Activities;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
import appfactory.edu.uwp.franklloydwrighttrail.TripOrder;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
/**
 * Created by sterl on 3/1/2017.
 */
public class TripPlannerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    private RelativeLayout create;
    private MenuItem addTrip;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private FrameLayout fragmentView;
    private RecyclerView recycler;
    private TourMenuAdapter adapter;
    private LinearLayoutManager layoutManager;
    public static HashMap<String, RealmList<TripOrder>> hm = new HashMap<>();
    public static LinkedHashSet<String> dates = new LinkedHashSet<>();
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
            goBackToMenu();
        } else {
            super.onBackPressed();
        }
    }
    public void goBackToMenu(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();
        //FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.remove(fragmentManager.getFragments().get(0)).commit();
        setupRecycler();
        newTripPosition = UUID.randomUUID().toString();
        addTrip.setVisible(true);
        recycler.setVisibility(View.VISIBLE);
        fragmentView.setVisibility(View.GONE);
        viewingFragment = false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_menu);
        this.realm = Realm.getDefaultInstance();
        newTripPosition = UUID.randomUUID().toString(); // ensures it's random
        create = (RelativeLayout) findViewById(R.id.create);
        fragmentView = (FrameLayout) findViewById(R.id.content_frame);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Trip Planner");
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_trip_planner).setChecked(true);
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
        addTrip = menu.findItem(R.id.menu_item_add_trip);
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
        fragmentView.setVisibility(View.VISIBLE);
        create.setVisibility(View.GONE);
        recycler.setVisibility(View.GONE);
        toolbar.setTitle("Trip Creation");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            // Actions
            case R.id.nav_locations:
                finish();
                break;
            case R.id.nav_trip_planner:
                break;
            // FLW Locations
            case R.id.nav_scjohnson:
                intent = new Intent(TripPlannerActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "SC Johnson Administration Building and Research Tower");
                TripPlannerActivity.this.startActivity(intent);
                break;
            case R.id.nav_wingspread:
                intent = new Intent(TripPlannerActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "Wingspread");
                TripPlannerActivity.this.startActivity(intent);
                break;
            case R.id.nav_monona_terrace:
                intent = new Intent(TripPlannerActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "Monona Terrace");
                TripPlannerActivity.this.startActivity(intent);
                break;
            case R.id.nav_meeting_house:
                intent = new Intent(TripPlannerActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "First Unitarian Society Meeting House");
                TripPlannerActivity.this.startActivity(intent);
                break;
            case R.id.nav_visitor_center:
                intent = new Intent(TripPlannerActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "Taliesin and FLW Visitor Center");
                TripPlannerActivity.this.startActivity(intent);
                break;
            case R.id.nav_german_warehouse:
                intent = new Intent(TripPlannerActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "A.D. German Warehouse");
                TripPlannerActivity.this.startActivity(intent);
                break;
            case R.id.nav_valley_school:
                intent = new Intent(TripPlannerActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "Wyoming Valley School");
                TripPlannerActivity.this.startActivity(intent);
                break;
            case R.id.nav_built_homes:
                intent = new Intent(TripPlannerActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "American System-Built Homes");
                TripPlannerActivity.this.startActivity(intent);
                break;
            /*case R.id.nav_settings:
                break;
            case R.id.nav_about:
                break; */
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        toggle.onConfigurationChanged(newConfig);
    }
    public void showTrip(String position){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, TripPlannerCreateTripFragment.newInstance(position)).commit();
        addTrip.setVisible(false);
        fragmentView.setVisibility(View.VISIBLE);
        create.setVisibility(View.GONE);
        recycler.setVisibility(View.GONE);
        toolbar.setTitle("Trip Creation");
        viewingFragment = true;
    }
    public void showTimeline(boolean isFinal, String position){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, TripPlannerTimelineFragment.newInstance(isFinal, position)).commit();
        addTrip.setVisible(false);
        fragmentView.setVisibility(View.VISIBLE);
        create.setVisibility(View.GONE);
        recycler.setVisibility(View.GONE);
        toolbar.setTitle("Trip Creation");
        viewingFragment = true;
    }
    public void setToolbarTitle(String title){
        toolbar.setTitle(title);
    }

    @Override
    protected void onDestroy() {
        RealmController.getInstance().close();
        super.onDestroy();
    }
}