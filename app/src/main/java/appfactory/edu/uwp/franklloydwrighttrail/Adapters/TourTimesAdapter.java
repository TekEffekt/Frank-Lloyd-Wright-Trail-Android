package appfactory.edu.uwp.franklloydwrighttrail.Adapters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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
    private String tripPosition;

    private Calendar currentTime;

    private TimePickerDialog timePicker;
    private int hour;
    private int minute;

    private DatePickerDialog datePicker;
    private int year;
    private int month;
    private int day;

    public TourTimesAdapter (TripObject locations, String tripPosition) {
        this.tripPosition = tripPosition;
        this.locations = locations;
        this.views = new ArrayList<>();

        // Initialize Times
        currentTime = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);
        year = currentTime.get(Calendar.YEAR);
        month = currentTime.get(Calendar.MONTH);
        day = currentTime.get(Calendar.DAY_OF_MONTH);
    }

    @NonNull
    @Override
    public TourTimesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        realm = RealmController.getInstance().getRealm();
        views.add(new TourTimesAdapter.ViewHolder(inflater.inflate(R.layout.tour_times_item, parent, false)));
        return views.get(views.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull final TourTimesAdapter.ViewHolder holder, final int position) {
        FLWLocation location = locations.getTrips().get(position).getLocation();

        if (location.getName() != -1 && location.getName() != R.string.user) {
            holder.name.setText(location.getName());
            //holder.website.setText(location.getWebsite());
            holder.website.setText(Html.fromHtml(context.getResources().getString(location.getWebsite())));
            holder.website.setMovementMethod(LinkMovementMethod.getInstance());
        } else if (location.getName() != R.string.user) {
            holder.name.setText(location.getGenericName());
            holder.signupContainer.setVisibility(View.GONE);
        } else {
            holder.container.setVisibility(View.GONE);
        }

        //Hide the tour when dropdown is pressed
        holder.dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.timeContainer.getVisibility() != View.GONE) {
                    holder.timeContainer.setVisibility(View.GONE);
                    holder.dropdown.setImageResource(R.drawable.ic_dropup);
                } else {
                    holder.timeContainer.setVisibility(View.VISIBLE);
                    holder.dropdown.setImageResource(R.drawable.ic_dropdown);
                }
            }
        });

        if (location.getDay() != null){
            holder.date.setText(location.getDay());
        }
        if (locations.getTrips().get(position).getStartTourTime() != -1){
            long time = locations.getTrips().get(position).getStartTourTime();
            int minute = (int) time % 60;
            int hour = ((int) time - minute) / 60;
            holder.startTime.setText(timeToString(hour, minute));
        }
        if (locations.getTrips().get(position).getEndTourTime() != -1){
            long time = locations.getTrips().get(position).getEndTourTime();
            holder.startTime.setText(timeToString(hour, minute));
        }

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
        datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date tourDate = new Date(year,month,dayOfMonth);
                String dateString = (getMonth(month) + " " + dayOfMonth + ", " + year);
                holder.date.setText(dateString);
                realm.beginTransaction();
                RealmController.getInstance().getTripResults(tripPosition).get(0).getTrips().get(position).getLocation().setDay(dateString);
                realm.commitTransaction();
            }
        }, year, month, day);
        datePicker.setTitle("Trip Tour Date");
        datePicker.show();
    }

    // This method enables the user to input a new start tour time
    private void getTourStartTime(@NonNull final TourTimesAdapter.ViewHolder holder, final int position){
        timePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Time textTime = new Time(hourOfDay,minute,0);
                int time = hourOfDay*60+minute;
                Log.d("debug", "Time: "+ time);
                realm.beginTransaction();
                RealmController.getInstance().getTripResults(tripPosition).get(0).getTrips().get(position).setStartTourTime(time);
                realm.commitTransaction();

                holder.startTime.setText(timeToString(hourOfDay, minute));
            }
        }, hour, minute, false);
        timePicker.setTitle("Trip Tour Time");
        timePicker.show();
    }

    private String timeToString(int hourOfDay, int minute){
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
        return hourDay + ":" + minuteDay;
    }

    // This method enables the user to input a new end tour time
    private void getTourEndTime(@NonNull final TourTimesAdapter.ViewHolder holder, final int position){
        timePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Time textTime = new Time(hourOfDay,minute,0);
                int time = hourOfDay*60+minute;

                realm.beginTransaction();
                RealmController.getInstance().getTripResults(tripPosition).get(0).getTrips().get(position).setEndTourTime(time);
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
        @Bind(R.id.container)
        LinearLayout container;

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
        RelativeLayout timeContainer;

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
        @Bind(R.id.tour_end_time)
        TextView endTime;
        @Nullable
        @Bind(R.id.right_arrow_end_time)
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
