package appfactory.edu.uwp.franklloydwrighttrail.Fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;

import appfactory.edu.uwp.franklloydwrighttrail.Activities.TripPlannerTimeline;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import io.realm.Realm;

/**
 * Created by sterl on 10/28/2016.
 */

public class TripPlannerTimesFragment extends Fragment {
    private Button cont;

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
    private boolean timeValid = false;

    public static TripPlannerTimesFragment newInstance(){
        TripPlannerTimesFragment tripPlannerTimesFragment = new TripPlannerTimesFragment();
        return tripPlannerTimesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_trip_times, container, false);

        /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.choose_times);
        setSupportActionBar(toolbar); */

        /*
        cont = (Button) view.findViewById(R.id.cont);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeValid = checkTimeValid();
                if (startTimeChosen && endTimeChosen && timeValid){
                    Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
                    toolbar.setTitle(R.string.trip_options);
                    ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

                    FragmentManager fragmentManager = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(TripPlannerOptionsFragment.newInstance(), null).commit();
                } else {
                    //Make toast yelling at user
                }
            }
        }); */

        //Grab Trip Object
        realm = RealmController.getInstance().getRealm();
        trip = RealmController.getInstance().getTrip();

        startTimeLayout = (RelativeLayout) view.findViewById(R.id.start_time_container);
        endTimeLayout = (RelativeLayout) view.findViewById(R.id.end_time_container);
        startTimeLabel = (TextView) view.findViewById(R.id.start_time);
        endTimeLabel = (TextView) view.findViewById(R.id.end_time);

        currentTime = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);

        startTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time textTime = new Time(hourOfDay,minute,0);
                        int time = hourOfDay*60+minute;
                        String hourDay = "";
                        String minuteDay = "";
                        realm.beginTransaction();
                        RealmController.getInstance().getTripResults().get(0).setStartTime(time);
                        startTimeChosen = true;
                        //trip.setStartTime(time.getTime());
                        realm.commitTransaction();


                        if(minute < 10)
                            minuteDay = "0"+minute;
                        else
                            minuteDay = minute+"";

                        if(hourOfDay > 12)
                        {
                            hourOfDay -= 12;
                            hourDay = hourOfDay +"";
                            minuteDay = minuteDay + " PM";
                        }
                        else
                        {
                            hourDay = hourOfDay +"";
                            minuteDay = minuteDay + " AM";
                        }
                        startTimeLabel.setText(hourDay + ":" + minuteDay);
                    }
                }, hour, minute, false);
                timePicker.setTitle("Choose Start Time");
                timePicker.show();
            }
        });

        endTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time textTime = new Time(hourOfDay,minute,0);
                        int time = hourOfDay*60+minute;
                        String hourDay = "";
                        String minuteDay = "";
                        realm.beginTransaction();
                        RealmController.getInstance().getTripResults().get(0).setEndTime(time);
                        endTimeChosen = true;
                        //trip.setEndTime(time.getTime());
                        realm.commitTransaction();
                        if(minute < 10)
                            minuteDay = "0"+minute;
                        else
                            minuteDay = minute+"";

                        if(hourOfDay > 12)
                        {
                            hourOfDay -= 12;
                            hourDay = hourOfDay +"";
                            minuteDay = minuteDay + " PM";
                        }
                        else
                        {
                            hourDay = hourOfDay +"";
                            minuteDay = minuteDay + " AM";
                        }
                            endTimeLabel.setText(hourDay + ":" + minuteDay);
                    }
                }, hour, minute, false);
                timePicker.setTitle("Choose Start Time");
                timePicker.show();
            }
        });

        return view;
    }

    private boolean checkTimeValid(){
        if (trip.getStartTime() != 0 && trip.getEndTime() != 0) {
            if (trip.getStartTime() > trip.getEndTime()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
