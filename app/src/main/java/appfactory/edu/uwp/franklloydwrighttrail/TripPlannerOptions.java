package appfactory.edu.uwp.franklloydwrighttrail;

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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by sterl on 10/28/2016.
 */

public class TripPlannerOptions extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Button cont;

    private Switch breakfastSwitch;
    private Switch lunchSwitch;
    private Switch dinnerSwitch;

    private LinearLayout breakfastContainer;
    private LinearLayout lunchContainer;
    private LinearLayout dinnerContainer;

    private boolean yesBreakfast = false;
    private boolean yesLunch = false;
    private boolean yesDinner = false;

    private RelativeLayout breakfastStartTimeContainer;
    private RelativeLayout breakfastEndTimeContainer;
    private RelativeLayout lunchStartTimeContainer;
    private RelativeLayout lunchEndTimeContainer;
    private RelativeLayout dinnerStartTimeContainer;
    private RelativeLayout dinnerEndTimeContainer;

    private TextView startTimeBreakfast;
    private TextView endTimeBreakfast;
    private TextView startTimeLunch;
    private TextView endTimeLunch;
    private TextView startTimeDinner;
    private TextView endTimeDinner;

    private TimePickerDialog timePicker;
    private Calendar currentTime;
    private int hour;
    private int minute;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TripPlannerOptions.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_options);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.trip_options);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cont = (Button) findViewById(R.id.cont);
        initializeViews();

        currentTime = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);

        initializeSwitches();
        initializeContainers();

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    /*
        Switches the appr
     */
    private boolean mealToggle(LinearLayout mealContainer, Boolean yesMeal) {
        if (yesMeal) { //turn off
            mealContainer.setVisibility(View.GONE);
            return false;
        } else { //turn on
            mealContainer.setVisibility(View.VISIBLE);
            return true;
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

    // Easy Clump of all the used views
    private void initializeViews(){
        breakfastSwitch = (Switch) findViewById(R.id.breakfast_switch);
        lunchSwitch = (Switch) findViewById(R.id.lunch_switch);
        dinnerSwitch = (Switch) findViewById(R.id.dinner_switch);

        breakfastContainer = (LinearLayout) findViewById(R.id.breakfast_time_container);
        lunchContainer = (LinearLayout) findViewById(R.id.lunch_time_container);
        dinnerContainer = (LinearLayout) findViewById(R.id.dinner_time_container);

        breakfastStartTimeContainer = (RelativeLayout) findViewById(R.id.start_breakfast);
        breakfastEndTimeContainer = (RelativeLayout) findViewById(R.id.end_breakfast);
        lunchStartTimeContainer = (RelativeLayout) findViewById(R.id.start_lunch);
        lunchEndTimeContainer = (RelativeLayout) findViewById(R.id.end_lunch);
        dinnerStartTimeContainer = (RelativeLayout) findViewById(R.id.start_dinner);
        dinnerEndTimeContainer = (RelativeLayout) findViewById(R.id.end_dinner);

        startTimeBreakfast = (TextView) findViewById(R.id.start_time_breakfast);
        endTimeBreakfast = (TextView) findViewById(R.id.end_time_breakfast);
        startTimeLunch = (TextView) findViewById(R.id.start_time_lunch);
        endTimeLunch = (TextView) findViewById(R.id.end_time_lunch);
        startTimeDinner = (TextView) findViewById(R.id.start_time_dinner);
        endTimeDinner = (TextView) findViewById(R.id.end_time_dinner);
    }

    private void initializeSwitches(){
        breakfastSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                yesBreakfast = mealToggle(breakfastContainer,yesBreakfast);
            }
        });

        lunchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                yesLunch = mealToggle(lunchContainer,yesLunch);
            }
        });

        dinnerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                yesDinner = mealToggle(dinnerContainer,yesDinner);
            }
        });
    }

    private void initializeContainers(){
        breakfastStartTimeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(TripPlannerOptions.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time time = new Time(hourOfDay,minute,0);
                        //Set time here
                        startTimeBreakfast.setText(time.toString());
                    }
                }, hour, minute, false);
                timePicker.setTitle("Choose Start Time");
                timePicker.show();
            }
        });

        breakfastEndTimeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(TripPlannerOptions.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time time = new Time(hourOfDay,minute,0);
                        //Set time here
                        endTimeBreakfast.setText(time.toString());
                    }
                }, hour, minute, false);
                timePicker.setTitle("Choose Start Time");
                timePicker.show();
            }
        });

        lunchStartTimeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(TripPlannerOptions.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time time = new Time(hourOfDay,minute,0);
                        //Set time here
                        startTimeLunch.setText(time.toString());
                    }
                }, hour, minute, false);
                timePicker.setTitle("Choose Start Time");
                timePicker.show();
            }
        });

        lunchEndTimeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(TripPlannerOptions.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time time = new Time(hourOfDay,minute,0);
                        //Set time here
                        endTimeLunch.setText(time.toString());
                    }
                }, hour, minute, false);
                timePicker.setTitle("Choose Start Time");
                timePicker.show();
            }
        });

        dinnerStartTimeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(TripPlannerOptions.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time time = new Time(hourOfDay,minute,0);
                        //Set time here
                        startTimeDinner.setText(time.toString());
                    }
                }, hour, minute, false);
                timePicker.setTitle("Choose Start Time");
                timePicker.show();
            }
        });

        dinnerEndTimeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(TripPlannerOptions.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time time = new Time(hourOfDay,minute,0);
                        //Set time here
                        endTimeDinner.setText(time.toString());
                    }
                }, hour, minute, false);
                timePicker.setTitle("Choose Start Time");
                timePicker.show();
            }
        });
    }
}
