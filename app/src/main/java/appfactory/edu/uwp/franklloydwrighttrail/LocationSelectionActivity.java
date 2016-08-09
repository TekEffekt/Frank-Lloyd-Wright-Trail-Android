package appfactory.edu.uwp.franklloydwrighttrail;

import android.content.Intent;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationSelectionActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng cameraPlace = new LatLng(43.0717445,-89.38040180000002);
    private Marker SCJohnson;
    private Marker Wingspread;
    private Marker MononaTerrace;
    private Marker MeetingHouse;
    private Marker FLWVisitorCenter;
    private Marker GermanWarehouse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
    public void onMapReady(GoogleMap map)
    {

        mMap = map;
        SCJohnson = mMap.addMarker(new MarkerOptions().position(new LatLng(42.7152375,-87.7906969)).title("SC Johnson Administration Building and Research Tower"));
        Wingspread = mMap.addMarker(new MarkerOptions().position(new LatLng(42.784562,-87.771588)).title("Wingspread"));
        MononaTerrace = mMap.addMarker(new MarkerOptions().position(cameraPlace).title("MononaTerrace"));
        MeetingHouse = mMap.addMarker(new MarkerOptions().position(new LatLng(43.0757361,-89.43533680000002)).title("Meeting House"));
        FLWVisitorCenter = mMap.addMarker(new MarkerOptions().position(new LatLng(43.14390059999999,-90.05952260000004)).title("FLW Visitor Center"));
        GermanWarehouse = mMap.addMarker(new MarkerOptions().position(new LatLng(43.3334718,-90.38436739999997)).title("German Warehouse"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPlace,7));
        mMap.setOnMarkerClickListener(this);
    }
    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        return false;
    }
}
