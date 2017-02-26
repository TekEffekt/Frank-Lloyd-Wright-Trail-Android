package appfactory.edu.uwp.franklloydwrighttrail.Adapters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by sterl on 2/19/2017.
 */

public class TourTimesAdapter extends RecyclerView.Adapter<TourTimesAdapter.ViewHolder> {
    private TripObject locations;
    private ArrayList<TourTimesAdapter.ViewHolder> views;
    private Context context;
    private Realm realm;

    private Calendar calendar;

    private TimePickerDialog timePicker;
    private int hour;
    private int minute;

    private DatePickerDialog datePicker;
    private int year;
    private int month;
    private int day;

    public TourTimesAdapter (TripObject locations) {
        this.locations = locations;
        this.views = new ArrayList<>();

        // Initialize Times
        calendar = Calendar.getInstance();
        year = calendar.getTime().getYear();
        month = calendar.getTime().getMonth();
        day = calendar.getTime().getDay();
        hour = calendar.getTime().getHours();
        minute = calendar.getTime().getMinutes();
    }

    @NonNull
    @Override
    public TourTimesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        views.add(new TourTimesAdapter.ViewHolder(inflater.inflate(R.layout.tour_times_item, parent, false)));
        return views.get(views.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull final TourTimesAdapter.ViewHolder holder, final int position) {
        FLWLocation location = locations.getTrips().get(position).getLocation();
        holder.name.setText(location.getName());

        //holder.website.setText(location.getWebsite());
        holder.website.setText(Html.fromHtml(context.getResources().getString(R.string.scj_website)));
        holder.website.setMovementMethod(LinkMovementMethod.getInstance());

        //Hide the tour when dropdown is pressed
        holder.dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.container.getVisibility() != View.GONE) {
                    holder.container.setVisibility(View.GONE);
                    holder.dropdown.setImageResource(R.drawable.ic_dropup);
                } else {
                    holder.container.setVisibility(View.VISIBLE);
                    holder.dropdown.setImageResource(R.drawable.ic_dropdown);
                }
            }
        });

        // Gather tour date
        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTourDate(holder, position);
            }
        });

        holder.dateArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTourDate(holder, position);
            }
        });

        // Gather tour time
        holder.startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTourStartTime(holder, position);
            }
        });

        holder.startTimeArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTourStartTime(holder, position);
            }
        });

        // Gather tour time
        holder.endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTourEndTime(holder, position);
            }
        });

        holder.endTimeArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTourEndTime(holder, position);
            }
        });
    }

    // This method enables the user to input a new tour date
    private void getTourDate(@NonNull final TourTimesAdapter.ViewHolder holder, final int position){
        datePicker = new DatePickerDialog(context, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date tourDate = new Date(year,month,dayOfMonth);

                //setTourDate(tourDate);

                String dateString = (getMonth(month) + " " + dayOfMonth + ", " + year);
                holder.date.setText(dateString);
            }
        }, year, month, day);
        datePicker.setTitle("Trip Tour Date");
        datePicker.show();
    }

    // This method enables the user to input a new start tour time
    private void getTourStartTime(@NonNull final TourTimesAdapter.ViewHolder holder, final int position){
        timePicker = new TimePickerDialog(context, TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Time textTime = new Time(hourOfDay,minute,0);
                int time = hourOfDay*60+minute;

                realm.beginTransaction();
                RealmController.getInstance().getTrip().getTrips().get(position).getLocation().setStartTourTime(time);
                realm.commitTransaction();

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
                holder.startTime.setText(hourDay + ":" + minuteDay);
            }
        }, hour, minute, false);
        timePicker.setTitle("Trip Tour Time");
        timePicker.show();
    }

    // This method enables the user to input a new start tour time
    private void getTourEndTime(@NonNull final TourTimesAdapter.ViewHolder holder, final int position){
        timePicker = new TimePickerDialog(context, TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Time textTime = new Time(hourOfDay,minute,0);
                int time = hourOfDay*60+minute;

                realm.beginTransaction();
                RealmController.getInstance().getTrip().getTrips().get(position).getLocation().setEndTourTime(time);
                realm.commitTransaction();

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
                holder.endTime.setText(hourDay + ":" + minuteDay);
            }
        }, hour, minute, false);
        timePicker.setTitle("Trip Tour Time");
        timePicker.show();
    }

    public FLWLocation getItem(int position) {
        return locations.getTrips().get(position).getLocation();
    }

    @Override
    public int getItemCount() { return locations.getTrips().size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @Bind(R.id.tour_time_name)
        TextView name;
        @Nullable
        @Bind(R.id.tour_website)
        TextView website;

        @Nullable
        @Bind(R.id.dropdown_trip_item)
        ImageView dropdown;
        @Nullable
        @Bind(R.id.tour_time_container)
        RelativeLayout container;

        @Nullable
        @Bind(R.id.signup_container)
        RelativeLayout signupContainer;

        @Nullable
        @Bind(R.id.tour_date)
        TextView date;
        @Nullable
        @Bind(R.id.right_arrow_date)
        ImageView dateArrow;

        @Nullable
        @Bind(R.id.tour_start_time)
        TextView startTime;
        @Nullable
        @Bind(R.id.right_arrow_start_time)
        ImageView startTimeArrow;

        @Nullable
        @Bind(R.id.tour_start_time)
        TextView endTime;
        @Nullable
        @Bind(R.id.right_arrow_start_time)
        ImageView endTimeArrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
