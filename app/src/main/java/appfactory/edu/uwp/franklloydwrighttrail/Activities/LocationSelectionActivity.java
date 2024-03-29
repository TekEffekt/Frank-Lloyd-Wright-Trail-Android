package appfactory.edu.uwp.franklloydwrighttrail.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import appfactory.edu.uwp.franklloydwrighttrail.Models.LocationModel;
import appfactory.edu.uwp.franklloydwrighttrail.Adapters.LocationSelectionAdapter;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.UserLocation;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LocationSelectionActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback, RecyclerView.OnItemTouchListener, NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    // This is the Main Activity

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private LatLng cameraPlace = new LatLng(43.0717445, -89.38040180000002);
    private Marker SCJohnson;
    private Marker Wingspread;
    private Marker MononaTerrace;
    private Marker MeetingHouse;
    private Marker FLWVisitorCenter;
    private Marker GermanWarehouse;
    private Marker ValleySchool;
    private Marker BuiltHomes;

    private Realm realm;

    private RecyclerView recyclerView;
    private LocationSelectionAdapter adapter;
    private GestureDetectorCompat gestureDetector;

    private DrawerLayout drawer;
    private int currentLocation = -1;
    private GridLayoutManager layoutManager;
    private NavigationView navigationView;

    private RealmConfiguration realmConfiguration;

    private static final int PLAY_SERVICES_REQUEST_CODE = 1978;
    private LocationRequest mLocationRequest;
    public static Location myLocation;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLocation = new Location("gps");
        initializeRealm();
        initializeGoogleApiClient();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_locations).setChecked(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new LocationSelectionAdapter(LocationModel.getLocations());
        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(this, 2);
        layoutManager.generateDefaultLayoutParams();
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(this, new RecyclerViewGestureListener());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void initializeGoogleApiClient() {
        if (checkPlayServices()) {
            buildGoogleApiClient();
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        } else {

            // prepare connection request
            createLocationRequest();
        }
    }

    private void initializeRealm() {
        this.realm = Realm.getDefaultInstance();
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
                realm.beginTransaction();
                UserLocation ul = null;
                if (myLocation != null) {
                    ul = new UserLocation(myLocation.getLatitude(), myLocation.getLongitude());
                } else {
                    ul = new UserLocation(43.0717445, -89.38040180000002);
                    Toast.makeText(this, "Please enable GPS for accurate location information", Toast.LENGTH_LONG).show();
                }
                realm.copyToRealmOrUpdate(ul);
                realm.commitTransaction();
                Intent intent = TripPlannerActivity.newIntent(this);
                startActivity(intent);
                break;
            // FLW Locations
            case R.id.nav_scjohnson:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "SC Johnson Administration Building and Research Tower");
                LocationSelectionActivity.this.startActivity(intent);
                break;
            case R.id.nav_wingspread:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "Wingspread");
                LocationSelectionActivity.this.startActivity(intent);
                break;
            case R.id.nav_monona_terrace:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "Monona Terrace");
                LocationSelectionActivity.this.startActivity(intent);
                break;
            case R.id.nav_meeting_house:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "First Unitarian Society Meeting House");
                LocationSelectionActivity.this.startActivity(intent);
                break;
            case R.id.nav_visitor_center:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "Taliesin and FLW Visitor Center");
                LocationSelectionActivity.this.startActivity(intent);
                break;
            case R.id.nav_german_warehouse:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "A.D. German Warehouse");
                LocationSelectionActivity.this.startActivity(intent);
                break;
            case R.id.nav_valley_school:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "Wyoming Valley School");
                LocationSelectionActivity.this.startActivity(intent);
                break;
            case R.id.nav_built_homes:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "American System-Built Homes");
                LocationSelectionActivity.this.startActivity(intent);
                break;
            //case R.id.nav_scrapbook:
            //    break;
            //case R.id.nav_settings:
            //    break;
            //case R.id.nav_about:
            //    break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        SCJohnson = mMap.addMarker(new MarkerOptions().position(new LatLng(42.7152375, -87.7906969))
                .title("SC Johnson Administration Building and Research Tower")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        Wingspread = mMap.addMarker(new MarkerOptions().position(new LatLng(42.784562, -87.771588))
                .title("Wingspread")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        MononaTerrace = mMap.addMarker(new MarkerOptions().position(cameraPlace)
                .title("Monona Terrace")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        MeetingHouse = mMap.addMarker(new MarkerOptions().position(new LatLng(43.0757361, -89.43533680000002))
                .title("First Unitarian Society Meeting House")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        FLWVisitorCenter = mMap.addMarker(new MarkerOptions().position(new LatLng(43.14390059999999, -90.05952260000004))
                .title("Taliesin and FLW Visitor Center")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        GermanWarehouse = mMap.addMarker(new MarkerOptions().position(new LatLng(43.3334718, -90.38436739999997))
                .title("A.D. German Warehouse")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        ValleySchool = mMap.addMarker(new MarkerOptions().position(new LatLng(43.119255, -90.114908))
                .title("Wyoming Valley School")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        BuiltHomes = mMap.addMarker(new MarkerOptions().position(new LatLng(43.010584, -87.948539))
                .title("American System-Built Homes")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        List<LatLng> polyLineFromJson = getFromFile(this, "flw-polyline.json", new TypeToken<LatLng>(){});
        mMap.addPolyline(new PolylineOptions().addAll(polyLineFromJson).color(Color.parseColor("#a6192e")).width(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPlace, 7));
        mMap.setOnMarkerClickListener(this);
    }

    private <T> ArrayList<T> getFromFile(Context context, String file, TypeToken<T> token) {
        Gson gson = new GsonBuilder().create();
        ArrayList<T> returnList = new ArrayList<>();
        try {
            JsonReader reader = new JsonReader(
                    new InputStreamReader(context.getAssets().open(file), "UTF-8"));
            reader.beginArray();
            while (reader.hasNext()) {
                T object = gson.fromJson(reader, token.getType());
                returnList.add(object);
            }
            reader.endArray();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Map", "JSON read error: " + e.getMessage());
        }
        Log.d("Map Coordinates", returnList.toString());
        return returnList;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Intent intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
        intent.putExtra("Title", marker.getTitle());
        LocationSelectionActivity.this.startActivity(intent);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        mMap.setMyLocationEnabled(true);

                        if (mClient.isConnected())
                            mClient.disconnect();

                        // Initialize mGoogleApiClient
                        if (checkPlayServices()) {
                            buildGoogleApiClient();
                        }

                        if (mClient != null) {
                            mClient.connect();
                        }

                        // prepare connection request
                        createLocationRequest();

                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                } else {
                    adapter.disableDistance();
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        if (mClient != null) {
            mClient.connect();
        }
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LocationSelection Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://appfactory.edu.uwp.franklloydwrighttrail/http/host/path")
        );
        if (mClient != null) {
            AppIndex.AppIndexApi.start(mClient, viewAction);
        }
    }

    private void onClick(int position) {
        CameraUpdate center;
        CameraUpdate zoom;
        switch (position) {
            case 0:

                    center = CameraUpdateFactory.newLatLng(new LatLng(42.7152375, -87.7906969));
                    zoom = CameraUpdateFactory.zoomTo(8);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                    currentLocation = position;

                break;
            case 1:

                    center = CameraUpdateFactory.newLatLng(new LatLng(42.784562, -87.771588));
                    zoom = CameraUpdateFactory.zoomTo(8);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                    currentLocation = position;

                break;
            case 2:

                    center = CameraUpdateFactory.newLatLng(new LatLng(43.010584, -87.948539));
                    zoom = CameraUpdateFactory.zoomTo(8);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                    currentLocation = position;

                break;
            case 3:

                    center = CameraUpdateFactory.newLatLng(new LatLng(43.0757361, -89.43533680000002));
                    zoom = CameraUpdateFactory.zoomTo(8);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                    currentLocation = position;

                break;
            case 4:

                    center = CameraUpdateFactory.newLatLng(new LatLng(43.0757361, -89.43533680000002));
                    zoom = CameraUpdateFactory.zoomTo(8);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                    currentLocation = position;

                break;
            case 5:

                    center = CameraUpdateFactory.newLatLng(new LatLng(43.14390059999999, -90.05952260000004));
                    zoom = CameraUpdateFactory.zoomTo(8);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                    currentLocation = position;

                break;
            case 6:

                    center = CameraUpdateFactory.newLatLng(new LatLng(43.119255, -90.114908));
                    zoom = CameraUpdateFactory.zoomTo(8);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                    currentLocation = position;

                break;
            case 7:

                    center = CameraUpdateFactory.newLatLng(new LatLng(43.3334718, -90.38436739999997));
                    zoom = CameraUpdateFactory.zoomTo(8);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                    currentLocation = position;

                break;
            default:
                break;
        }
    }

    private void doubleClick(int position) {
        Intent intent;
        switch (position) {
            case 0:
                    intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                    intent.putExtra("Title", "SC Johnson Administration Building and Research Tower");
                    LocationSelectionActivity.this.startActivity(intent);
                break;
            case 1:

                    intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                    intent.putExtra("Title", "Wingspread");
                    LocationSelectionActivity.this.startActivity(intent);

                break;
            case 2:

                    intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                    intent.putExtra("Title", "American System-Built Homes");
                    LocationSelectionActivity.this.startActivity(intent);

                break;
            case 3:

                    intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                    intent.putExtra("Title", "Monona Terrace");
                    LocationSelectionActivity.this.startActivity(intent);

                break;
            case 4:

                    intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                    intent.putExtra("Title", "First Unitarian Society Meeting House");
                    LocationSelectionActivity.this.startActivity(intent);

                break;
            case 5:
                    intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                    intent.putExtra("Title", "Taliesin and FLW Visitor Center");
                    LocationSelectionActivity.this.startActivity(intent);

                break;
            case 6:
                    intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                    intent.putExtra("Title", "Wyoming Valley School");
                    LocationSelectionActivity.this.startActivity(intent);

                break;
            case 7:
                    intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                    intent.putExtra("Title", "A.D. German Warehouse");
                    LocationSelectionActivity.this.startActivity(intent);

                break;
            default:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LocationSelection Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://appfactory.edu.uwp.franklloydwrighttrail/http/host/path")
        );
        if (mClient != null) {
            AppIndex.AppIndexApi.end(mClient, viewAction);
        }

        // disconnect from google api
        if (mClient != null) {
            mClient.disconnect();
        }
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
    public boolean onTouchEvent(MotionEvent e) {
        this.gestureDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
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
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d("GestureListener", "SingleTapUp event");
                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                onClick(recyclerView.getChildAdapterPosition(view));
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(@NonNull MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_UP) {
                Log.d("GestureListener", "DoubleTapEvent event");
                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                doubleClick(recyclerView.getChildAdapterPosition(view));
            }
            return true;
        }
    }

    public static float updateDistance(int position) {
        Location place = new Location("temp");
        switch (position) {
            case 0:
                //sc-johnson
                place.setLatitude(42.7152375);
                place.setLongitude(-87.7906969);
                return myLocation.distanceTo(place);
            case 1:
                //Wingspread
                place.setLatitude(42.784562);
                place.setLongitude(-87.771588);
                return myLocation.distanceTo(place);
            case 2:
                //Built Homes
                place.setLatitude(43.010584);
                place.setLongitude(-87.948539);
                return myLocation.distanceTo(place);
            case 3:
                //Monona Terrace
                place.setLatitude(43.0757361);
                place.setLongitude(-89.43533680000002);
                return myLocation.distanceTo(place);
            case 4:
                //Meeting House
                place.setLatitude(43.0757361);
                place.setLongitude(-89.43533680000002);
                return myLocation.distanceTo(place);
            case 5:
                //FLW Visitor Center
                place.setLatitude(43.14390059999999);
                place.setLongitude(-90.05952260000004);
                return myLocation.distanceTo(place);
            case 6:
                //Wyoming Valley School
                place.setLatitude(43.119255);
                place.setLongitude(-90.114908);
                return myLocation.distanceTo(place);
            case 7:
                //German Warehouse
                place.setLatitude(43.3334718);
                place.setLongitude(-90.38436739999997);
                return myLocation.distanceTo(place);
            default:
                return 0;
        }
    }

    // location methods

    //checks google play service on device and returns boolean
    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_REQUEST_CODE).show();
            }

            return false;
        }

        return true;
    }

    // handles error code from checkGooglePlayServices method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLAY_SERVICES_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mClient.isConnecting() && !mClient.isConnected()) {
                    mClient.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Google Play Services must be installed.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // get location from google api
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // no location permission stop trying to connect
            adapter.disableDistance();
            return;
        }
        myLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);

        // has location services
        if (myLocation != null) {
            adapter.updateDistance();
        }

        // start location updates
        startLocationUpdates();
    }

    // sets location update intervals and accuracy
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    // start location updates
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            adapter.disableDistance();
            return;
        }
        try {
            if (mClient.isConnected()) {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mClient, mLocationRequest, this);
            }
        } catch (NullPointerException e) {
            Log.e("Google API", e.getMessage());
        }
    }

    // stop location updates
    protected void stopLocationUpdates() {
        if (mClient != null && mClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mClient, this);

        }
    }

    // handles location changes
    @Override
    public void onLocationChanged(Location location) {

        // get new location
        myLocation = location;

        // location services is on display new location
        if (myLocation != null) {
            adapter.updateDistance();
        }

        // location services is off prompt user to enable
        else {
            adapter.disableDistance();
        }
    }

    // activity paused
    @Override
    protected void onPause() {
        super.onPause();

        // stop location updates
        stopLocationUpdates();

    }

    @Override
    protected void onDestroy() {
        RealmController.getInstance().close();
        super.onDestroy();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public static boolean isReceivingLocation() {
        return myLocation != null;
    }
}

