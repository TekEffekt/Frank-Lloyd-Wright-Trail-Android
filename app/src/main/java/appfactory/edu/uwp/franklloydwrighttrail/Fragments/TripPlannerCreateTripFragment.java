package appfactory.edu.uwp.franklloydwrighttrail.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import appfactory.edu.uwp.franklloydwrighttrail.Adapters.CurrentStopAdapter;
import appfactory.edu.uwp.franklloydwrighttrail.Adapters.TourTimesAdapter;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import appfactory.edu.uwp.franklloydwrighttrail.TripOrder;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by sterl on 3/25/2017.
 */

public class TripPlannerCreateTripFragment extends Fragment {

    private RelativeLayout startTimeLayout;
    private RelativeLayout endTimeLayout;
    private RelativeLayout startDateLayout;
    private RelativeLayout endDateLayout;

    private TextView startTimeLabel;
    private TextView endTimeLabel;
    private TextView startDateLabel;
    private TextView endDateLabel;

    private EditText tripNameEdit;

    private TimePickerDialog timePicker;
    private Calendar currentTime;
    private int hour;
    private int minute;

    private DatePickerDialog datePicker;
    private int year;
    private int month;
    private int day;

    private TripObject trip;
    private Realm realm;
    private static String tripPosition;

    private RecyclerView editRecyclerView;
    private TourTimesAdapter editAdapter;
    private RecyclerView stopRecyclerView;
    private CurrentStopAdapter stopAdapter;
    private LinearLayoutManager stopLayoutManager;
    private LinearLayoutManager editLayoutManager;

    private boolean startTimeChosen = false;
    private boolean endTimeChosen = false;
    private boolean timeValid = false;

    public static TripPlannerCreateTripFragment newInstance(String position){
        TripPlannerCreateTripFragment fragment = new TripPlannerCreateTripFragment();
        tripPosition = position;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_trip_planner_menu, container, false);

        //Grab Trip Object
        realm = RealmController.getInstance().getRealm();
        trip = new TripObject(tripPosition);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new TripObject(tripPosition));
        realm.commitTransaction();

        tripNameEdit = (EditText) view.findViewById(R.id.trip_name);
        startTimeLayout = (RelativeLayout) view.findViewById(R.id.start_time_container);
        endTimeLayout = (RelativeLayout) view.findViewById(R.id.end_time_container);
        startDateLayout = (RelativeLayout) view.findViewById(R.id.start_date_container);
        endDateLayout = (RelativeLayout) view.findViewById(R.id.end_date_container);
        startTimeLabel = (TextView) view.findViewById(R.id.start_time);
        endTimeLabel = (TextView) view.findViewById(R.id.end_time);
        startDateLabel = (TextView) view.findViewById(R.id.start_date);
        endDateLabel = (TextView) view.findViewById(R.id.end_date);


        setupRecyclerViews(view);
        setupTourTimeInput();

        return view;
    }

    private void setupRecyclerViews(View view){
        editRecyclerView = (RecyclerView) view.findViewById(R.id.tour_edit_recycler);
        editAdapter = new TourTimesAdapter(RealmController.getInstance().getTripResults(tripPosition).get(0), tripPosition);
        editRecyclerView.setAdapter(editAdapter);
        stopRecyclerView = (RecyclerView) view.findViewById(R.id.add_stop_recycler);
        stopAdapter = new CurrentStopAdapter(RealmController.getInstance().getTripResults(tripPosition).get(0), tripPosition);
        stopRecyclerView.setAdapter(stopAdapter);

        stopLayoutManager = new LinearLayoutManager(getContext());
        stopLayoutManager.generateDefaultLayoutParams();
        stopLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stopLayoutManager.scrollToPosition(0);

        editLayoutManager = new LinearLayoutManager(getContext());
        editLayoutManager.generateDefaultLayoutParams();
        editLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        editLayoutManager.scrollToPosition(0);

        stopRecyclerView.setLayoutManager(stopLayoutManager);
        editRecyclerView.setLayoutManager(editLayoutManager);
    }

    private void setupTourTimeInput(){
        currentTime = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);
        year = currentTime.get(Calendar.YEAR);
        month = currentTime.get(Calendar.MONTH);
        day = currentTime.get(Calendar.DAY_OF_MONTH);

        // Name
        tripNameEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    realm.beginTransaction();
                    RealmController.getInstance().getTripResults(tripPosition).get(0).setName(tripNameEdit.getText().toString());
                    realm.commitTransaction();
                    return true;
                }
                return false;
            }
        });


        startTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(getContext(),TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time textTime = new Time(hourOfDay,minute,0);
                        int time = hourOfDay*60+minute;
                        String hourDay = "";
                        String minuteDay = "";
                        realm.beginTransaction();
                        RealmController.getInstance().getTripResults(tripPosition).get(0).setStartTime(time);
                        startTimeChosen = true;
                        realm.commitTransaction();


                        if(minute < 10) {
                            minuteDay = "0" + minute;
                        } else {
                            minuteDay = minute + "";
                        }
                        if(hourOfDay > 12) {
                            hourOfDay -= 12;
                            hourDay = hourOfDay +"";
                            minuteDay = minuteDay + " PM";
                        } else {
                            if (hourOfDay == 0){
                                hourOfDay = 12;
                            }
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
                timePicker = new TimePickerDialog(getContext(), TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time textTime = new Time(hourOfDay,minute,0);
                        int time = hourOfDay*60+minute;
                        String hourDay = "";
                        String minuteDay = "";
                        realm.beginTransaction();
                        RealmController.getInstance().getTripResults(tripPosition).get(0).setEndTime(time);
                        endTimeChosen = true;
                        realm.commitTransaction();

                        if(minute < 10) {
                            minuteDay = "0" + minute;
                        } else {
                            minuteDay = minute + "";
                        }

                        if(hourOfDay > 12) {
                            hourOfDay -= 12;
                            hourDay = hourOfDay +"";
                            minuteDay = minuteDay + " PM";
                        } else {
                            if (hourOfDay == 0){
                                hourOfDay = 12;
                            }
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

        startDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker = new DatePickerDialog(getContext(), DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date tourDate = new Date(year,month,dayOfMonth);

                        //setTourDate(tourDate);

                        String dateString = (getMonth(month) + " " + dayOfMonth + ", " + year);
                        startDateLabel.setText(dateString);
                    }
                }, year, month, day);
                datePicker.setTitle("Trip Tour Date");
                datePicker.show();
            }
        });

        endDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker = new DatePickerDialog(getContext(), DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date tourDate = new Date(year,month,dayOfMonth);

                        //setTourDate(tourDate);

                        String dateString = (getMonth(month) + " " + dayOfMonth + ", " + year);
                        endDateLabel.setText(dateString);
                    }
                }, year, month, day);
                datePicker.setTitle("Trip Tour Date");
                datePicker.show();
            }
        });
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

    private String getMonth(int month){
        switch (month){
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "Month";
        }
    }
}
