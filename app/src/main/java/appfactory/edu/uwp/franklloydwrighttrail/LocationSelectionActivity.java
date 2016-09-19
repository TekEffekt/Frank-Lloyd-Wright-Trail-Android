package appfactory.edu.uwp.franklloydwrighttrail;

import android.content.Intent;
import android.net.Uri;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationSelectionActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, RecyclerView.OnItemTouchListener, NavigationView.OnNavigationItemSelectedListener {
    private GoogleMap mMap;
    private LatLng cameraPlace = new LatLng(43.0717445, -89.38040180000002);
    private Marker SCJohnson;
    private Marker Wingspread;
    private Marker MononaTerrace;
    private Marker MeetingHouse;
    private Marker FLWVisitorCenter;
    private Marker GermanWarehouse;

    private RecyclerView recyclerView;
    private LocationSelectionAdapter adapter;
    private GestureDetectorCompat gestureDetector;

    private DrawerLayout drawer;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new LocationSelectionAdapter((LocationModel.getLocations()));
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.generateDefaultLayoutParams();
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(this, new RecyclerViewGestureListener());

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                .title("Meeting House")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        FLWVisitorCenter = mMap.addMarker(new MarkerOptions().position(new LatLng(43.14390059999999, -90.05952260000004))
                .title("FLW Visitor Center")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        GermanWarehouse = mMap.addMarker(new MarkerOptions().position(new LatLng(43.3334718, -90.38436739999997))
                .title("German Warehouse")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        SCJohnson.showInfoWindow();
        Wingspread.showInfoWindow();
        MononaTerrace.showInfoWindow();
        MeetingHouse.showInfoWindow();
        FLWVisitorCenter.showInfoWindow();
        GermanWarehouse.showInfoWindow();


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPlace, 7));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Intent intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
        intent.putExtra("Title", marker.getTitle());
        LocationSelectionActivity.this.startActivity(intent);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
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
        AppIndex.AppIndexApi.start(mClient, viewAction);
    }

    private void onClick(int position) {
        Intent intent;
        switch (position){
            case 0:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "SC Johnson Administration Building and Research Tower");
                LocationSelectionActivity.this.startActivity(intent);
                break;
            case 1:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "Monona Terrace");
                LocationSelectionActivity.this.startActivity(intent);
                break;
            case 2:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "Wingspread");
                LocationSelectionActivity.this.startActivity(intent);
                break;
            case 3:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "Meeting House");
                LocationSelectionActivity.this.startActivity(intent);
                break;
            case 4:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "FLW Visitor Center");
                LocationSelectionActivity.this.startActivity(intent);
                break;
            case 5:
                intent = new Intent(LocationSelectionActivity.this, DescriptonActivity.class);
                intent.putExtra("Title", "German Warehouse");
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
        AppIndex.AppIndexApi.end(mClient, viewAction);
        mClient.disconnect();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

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
}
