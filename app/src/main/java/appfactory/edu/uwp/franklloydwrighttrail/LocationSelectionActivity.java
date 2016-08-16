package appfactory.edu.uwp.franklloydwrighttrail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationSelectionActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng cameraPlace = new LatLng(43.0717445, -89.38040180000002);
    private Marker SCJohnson;
    private Marker Wingspread;
    private Marker MononaTerrace;
    private Marker MeetingHouse;
    private Marker FLWVisitorCenter;
    private Marker GermanWarehouse;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;
        SCJohnson = mMap.addMarker(new MarkerOptions().position(new LatLng(42.7152375, -87.7906969)).title("SC Johnson Administration Building and Research Tower"));
        Wingspread = mMap.addMarker(new MarkerOptions().position(new LatLng(42.784562, -87.771588)).title("Wingspread"));
        MononaTerrace = mMap.addMarker(new MarkerOptions().position(cameraPlace).title("MononaTerrace"));
        MeetingHouse = mMap.addMarker(new MarkerOptions().position(new LatLng(43.0757361, -89.43533680000002)).title("Meeting House"));
        FLWVisitorCenter = mMap.addMarker(new MarkerOptions().position(new LatLng(43.14390059999999, -90.05952260000004)).title("FLW Visitor Center"));
        GermanWarehouse = mMap.addMarker(new MarkerOptions().position(new LatLng(43.3334718, -90.38436739999997)).title("German Warehouse"));

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
}
