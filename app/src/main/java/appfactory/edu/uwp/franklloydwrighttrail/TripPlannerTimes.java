package appfactory.edu.uwp.franklloydwrighttrail;

import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Calendar;

import io.realm.Realm;

/**
 * Created by sterl on 10/28/2016.
 */

public class TripPlannerTimes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Button cont;

    private static final String DIALOG_START = "DialogStart";
    private static final String DIALOG_END = "DialogEnd";

    private static final int REQUEST_START = 0;
    private static final int REQUEST_END = 1;

    private RelativeLayout startTimeLayout;
    private RelativeLayout endTimeLayout;

    private TextView startTimeLabel;
    private TextView endTimeLabel;

    private TimePickerDialog timePicker;
    private Calendar currentTime;
    private int hour;
    private int minute;

    private TripObject trip;
    private Realm realm;

    private boolean startTimeChosen = false;
    private boolean endTimeChosen = false;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TripPlannerOptions.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_times);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.choose_times);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cont = (Button) findViewById(R.id.cont);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startTimeChosen && endTimeChosen){
                    Intent intent = new Intent(TripPlannerTimes.this, TripPlannerOptions.class);
                    TripPlannerTimes.this.startActivity(intent);
                    finish();
                } else {
                    //Make toast yelling at user
                }
            }
        });

        //Grab Trip Object
        realm = RealmController.getInstance().getRealm();
        trip = RealmController.getInstance().getTrip();

        startTimeLayout = (RelativeLayout) findViewById(R.id.start_time_container);
        endTimeLayout = (RelativeLayout) findViewById(R.id.end_time_container);
        startTimeLabel = (TextView) findViewById(R.id.start_time);
        endTimeLabel = (TextView) findViewById(R.id.end_time);

        currentTime = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);

        startTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(TripPlannerTimes.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time time = new Time(hourOfDay,minute,0);
                        realm.beginTransaction();
                        RealmController.getInstance().getTripResults().get(0).setStartTime(time.getTime());
                        startTimeChosen = true;
                        //trip.setStartTime(time.getTime());
                        realm.commitTransaction();
                        startTimeLabel.setText(time.toString());
                    }
                }, hour, minute, false);
                timePicker.setTitle("Choose Start Time");
                timePicker.show();
            }
        });

        endTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(TripPlannerTimes.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time time = new Time(hourOfDay,minute,0);
                        realm.beginTransaction();
                        RealmController.getInstance().getTripResults().get(0).setEndTime(time.getTime());
                        endTimeChosen = true;
                        //trip.setEndTime(time.getTime());
                        realm.commitTransaction();
                        endTimeLabel.setText(time.toString());
                    }
                }, hour, minute, false);
                timePicker.setTitle("Choose Start Time");
                timePicker.show();
            }
        });

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
}
