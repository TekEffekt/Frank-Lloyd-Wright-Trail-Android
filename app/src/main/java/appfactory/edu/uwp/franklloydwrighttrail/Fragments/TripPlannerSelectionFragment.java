package appfactory.edu.uwp.franklloydwrighttrail.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import appfactory.edu.uwp.franklloydwrighttrail.Adapters.TourTimesAdapter;
import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.Models.LocationModel;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import appfactory.edu.uwp.franklloydwrighttrail.TripOrder;
import appfactory.edu.uwp.franklloydwrighttrail.Adapters.TripSelectionAdapter;
import io.realm.Realm;
import io.realm.RealmList;
/**
 * Created by sterl on 10/28/2016.
 */

public class TripPlannerSelectionFragment extends Fragment implements RecyclerView.OnItemTouchListener{
    private TripObject trip;
    private RealmList<TripOrder> trips;
    private RealmList<FLWLocation> locations;

    private RecyclerView recyclerView;
    private TripSelectionAdapter adapter;
    private GridLayoutManager layoutManager;
    private GestureDetectorCompat gestureDetector;
    private LayoutInflater inflater;

    private Realm realm;
    private static String tripPosition;

    private CardView destinationCard;
    private CardView genericStop;

    private Calendar currentTime;

    private TimePickerDialog timePicker;
    private int hour;
    private int minute;

    private DatePickerDialog datePicker;
    private int year;
    private int month;
    private int day;

    private String genericName;
    private Date genericDate;
    private int genericStart;
    private int genericEnd;

    public static TripPlannerSelectionFragment newInstance(String position){
        TripPlannerSelectionFragment selection = new TripPlannerSelectionFragment();
        tripPosition = position;
        return selection;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.content_trip_planner, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        adapter = new TripSelectionAdapter((LocationModel.getLocations()));
        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.generateDefaultLayoutParams();
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(getActivity(), new TripPlannerSelectionFragment.RecyclerViewGestureListener());

        realm = RealmController.getInstance().getRealm();
        trip = new TripObject(tripPosition);
        trips = new RealmList<TripOrder>();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new TripObject(tripPosition));
        realm.commitTransaction();
        genericStopSetup(view);

        // Initialize Times
        currentTime = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);
        year = currentTime.get(Calendar.YEAR);
        month = currentTime.get(Calendar.MONTH);
        day = currentTime.get(Calendar.DAY_OF_MONTH);

        locations = new LocationModel().getLocations();

        return view;
    }

    private void genericStopSetup(View view){
        genericStop = (CardView) view.findViewById(R.id.other_stop);
        genericStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View content = inflater.inflate(R.layout.generic_stop_item, null);
                final EditText editName = (EditText) content.findViewById(R.id.stop_name);

                final RelativeLayout editDate = (RelativeLayout) content.findViewById(R.id.date_container);
                final RelativeLayout editStartTime = (RelativeLayout) content.findViewById(R.id.start_time_container);
                final RelativeLayout editEndTime = (RelativeLayout) content.findViewById(R.id.end_time_container);

                final TextView dateText = (TextView) content.findViewById(R.id.tour_date);
                final TextView startTimeText = (TextView) content.findViewById(R.id.tour_start_time);
                final TextView endTimeText = (TextView) content.findViewById(R.id.tour_end_time);

                editName.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER) {
                            if (v != null) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            }
                            genericName = editName.getText().toString();

                            return true;
                        }
                        return false;
                    }
                });

                editDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePicker = new DatePickerDialog(getContext(), DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Date tourDate = new Date(year,month,dayOfMonth);
                                genericDate = tourDate;
                                String dateString = (getMonth(month) + " " + dayOfMonth + ", " + year);
                                dateText.setText(dateString);
                            }
                        }, year, month, day);
                        datePicker.setTitle("Trip Tour Date");
                        datePicker.show();
                    }
                });

                editStartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePicker = new TimePickerDialog(getContext(), TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Time textTime = new Time(hourOfDay,minute,0);
                                int time = hourOfDay*60+minute;

                                genericStart = time;

                                String hourDay = "";
                                String minuteDay = "";

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
                                startTimeText.setText(hourDay + ":" + minuteDay);
                            }
                        }, hour, minute, false);
                        timePicker.setTitle("Trip Tour Time");
                        timePicker.show();
                    }
                });

                editEndTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePicker = new TimePickerDialog(getContext(), TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Time textTime = new Time(hourOfDay,minute,0);
                                int time = hourOfDay*60+minute;

                                genericEnd = time;

                                String hourDay = "";
                                String minuteDay = "";

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
                                endTimeText.setText(hourDay + ":" + minuteDay);
                            }
                        }, hour, minute, false);
                        timePicker.setTitle("Trip Tour Time");
                        timePicker.show();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(content)
                        .setTitle("Other Stop")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                realm.beginTransaction();
                                RealmController.getInstance().getTripResults(tripPosition).get(0).getTrips().add(new TripOrder(new FLWLocation(genericName, genericStart, genericEnd, genericDate)));
                                realm.commitTransaction();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Undo
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     * A gesture listener for on top of the recycler view.
     */
    private class RecyclerViewGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                destinationCard = (CardView) view.findViewById(R.id.destination_card);
                onClick(recyclerView.getChildAdapterPosition(view));
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    private void onClick(int position) {
        checkSelection(position);
        realm.beginTransaction();
        //RealmController.getInstance().getTripResults(tripPosition).get(0).setTrips(trips);
        trip.setTrips(trips);
        realm.copyToRealmOrUpdate(trip);
        realm.commitTransaction();
    }

    private void checkSelection(int selection) {
        boolean existed = false;
        System.out.println(trips.toString());
        if (trips.size() != 0) {
            for (int i = 0; i < trips.size(); i++) {
                if (trips.get(i).getLocation() == locations.get(selection)) {
                    trips.remove(i);
                    existed = true;
                    showSelection(selection,existed);
                }
            }
            if (!existed) {
                trips.add(new TripOrder(locations.get(selection)));
                showSelection(selection,existed);
            }
        } else {
            trips.add(new TripOrder(locations.get(selection)));
            showSelection(selection,existed);
        }
    }

    private void showSelection(int selection, boolean isSelected) {
        if (isSelected) {
            destinationCard.setCardBackgroundColor(Color.WHITE);
        } else {
            destinationCard.setCardBackgroundColor(Color.LTGRAY);
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
