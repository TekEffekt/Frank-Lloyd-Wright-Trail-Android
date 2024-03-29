package appfactory.edu.uwp.franklloydwrighttrail.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vipul.hp_hp.timelineview.TimelineView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import appfactory.edu.uwp.franklloydwrighttrail.Activities.DescriptonActivity;
import appfactory.edu.uwp.franklloydwrighttrail.Activities.LocationSelectionActivity;
import appfactory.edu.uwp.franklloydwrighttrail.Activities.TripPlannerActivity;
import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import appfactory.edu.uwp.franklloydwrighttrail.TripOrder;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;

import static appfactory.edu.uwp.franklloydwrighttrail.R.color.gray;
import static appfactory.edu.uwp.franklloydwrighttrail.R.color.light_gray;

/**
 * Created by sterl on 11/3/2016.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {
    private ArrayList<TimelineAdapter.TimelineViewHolder> views;
    private Context context;
    private TripObject trips;
    private ArrayList<FLWLocation> sorted;
    int tLine = 0;
    int temp =0;
    int counter =0;
    RealmList<TripOrder> aTrip = new RealmList<>();
    private TripOrder trip;
    private boolean isFinal;

    public void setTrip(RealmList<TripOrder> trip) {
        this.aTrip = trip;
    }

    public TimelineAdapter (Context context, String tripPosition, boolean isFinal){
        this.context = context;
        this.views = new ArrayList<>();
        trips = RealmController.getInstance().getTripResults(tripPosition).get(0);
        this.isFinal = isFinal;
        if(isFinal) {
            Iterator<String> it = TripPlannerActivity.dates.iterator();

            RealmList<TripOrder> temp;
            if(it.hasNext()) {
                it.next();
            }
            while(it.hasNext()) {
                temp = TripPlannerActivity.hm.get(it.next());
                for(int i = 0;i<temp.size();i++)
                {
                    aTrip.add(temp.get(i));

                }
            }
        }
    }

    @NonNull
    @Override
    public TimelineAdapter.TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        views.add(new TimelineAdapter.TimelineViewHolder(inflater.inflate(R.layout.timeline_item, parent,false), 0));
        return views.get(views.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        // Exclusively for Final Timeline
        if(isFinal) {
            ArrayList<String> dates = new ArrayList<>(TripPlannerActivity.dates);

            Collections.sort(dates, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
                    if( o1 == o2 ) {
                        return 0;
                    }
                    if( o1 == null ) {
                        return -1;
                    }
                    if( o2 == null ) {
                        return 1;
                    }
                    try {
                        Date date1 = format.parse(o1);
                        Date date2 = format.parse(o2);
                        return date1.compareTo(date2);
                    } catch (ParseException e) {
                        Log.e("Parse Error", e.getMessage());
                        return o1.compareTo( o2 );
                    }
                }
            });

            Iterator<String> it = dates.iterator();

            RealmList<TripOrder> temp;

            aTrip = new RealmList<>();
            while(it.hasNext()) {
                String key = it.next();
                temp = TripPlannerActivity.hm.get(key);
                for(int i = 0;i<temp.size();i++) {
                    Log.d(key, temp.get(i).toString());
                    aTrip.add(temp.get(i));
                }
            }
        } else {
            holder.time.setVisibility(View.GONE);
        }

        //hides trip length if last
        if (getItemCount() - 1 == position){
            holder.tripLengthContainer.setVisibility(View.GONE);
        } else {
            holder.tripLengthContainer.setVisibility(View.VISIBLE);
        }

        if(!isFinal) {

            // Set focused trip to trips
            trip = trips.getTrips().get(position);

        } else {

            // Set focused trip to current position
            trip = aTrip.get(position);
        }

        // Deal with images for a location or the lack of
        if (trip.getLocation().getImage() != -1) {
            holder.picture.setBackground(ContextCompat.getDrawable(context, trip.getLocation().getImage()));
        } else {
            holder.picture.setVisibility(View.GONE);
        }

        // As long as it's not home
        if (trip.getLocation().getName() != R.string.user) {
            final String STRING_NAME = context.getString(trip.getLocation().getName());
            final String LOCATION_URI = "google.navigation:q=" + trip.getLocation().getLatlong();
            holder.infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DescriptonActivity.class);
                    intent.putExtra("Title", STRING_NAME);
                    context.startActivity(intent);
                }
            });

            holder.locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Grab Location
                    Uri gmmIntentUri = Uri.parse(LOCATION_URI);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
                }
            });
        } else {
            holder.name.setText(R.string.user);
            holder.infoButton.setVisibility(View.GONE);
            holder.locationButton.setVisibility(View.GONE);
            holder.picture.setVisibility(View.GONE);
            holder.homeIcon.setVisibility(View.VISIBLE);
        }

        if (trip.getTimeText() == null){
            if(trip.getLocation().getGenericName() != null) {
                holder.name.setText(trip.getLocation().getGenericName());
            } else if (trip.getLocation().getName() == R.string.user) {
                //ignore for now
                //this shouldn't happen, these need times attached to them
            } else {
                holder.name.setText(trip.getLocation().getName());
                holder.time.setText(trip.getTimeText());
                holder.tripLength.setText(trip.getTimeValue());
            }
        } else {
            int hour =0;
            int min =0;

            if (isFinal && trip.getLocation().getName() == R.string.user) {
                int total =(int) aTrip.get(position + 1).getStartTourTime()-trip.getTimeValue();
                hour = total /60;
                min = total %60;
            } else {
                hour = (int) trip.getStartTourTime() / 60;
                min = (int) trip.getStartTourTime() % 60;
            }

            if(position == 0) {
                if(counter == 1) {
                    counter = 0;
                    tLine = 0;
                }

                temp = trip.getTimeValue();
                counter++;
            } else if(position == 1) {
                if(!isFinal) {
                    tLine = trips.getStartTime() + temp + tLine;
                    hour = tLine/60;
                    min = tLine%60;
                    temp = trip.getTimeValue();
                }
            } else if(position > 1) {
                if(!isFinal) {
                    tLine = 60 + temp + tLine;
                    hour = tLine/60;
                    min = tLine%60;
                    temp = trip.getTimeValue();
                }
            }

            if (trip.getLocation().getImage() != -1){
                holder.name.setText(trip.getLocation().getName());
                // Because SCJ's title is way too long
                if (trip.getLocation().getName() == R.string.scjohnson){
                    holder.name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                }
            } else {
                holder.name.setText(trip.getLocation().getGenericName());
            }

            if (trip.getLocation().getName() == R.string.user) {
                holder.picture.setVisibility(View.GONE);
                if (isFinal) {
                    holder.time.setText(aTrip.get(position + 1).getLocation().getDay() + "  " + timeToString(hour, min));
                }
            } else {
                holder.picture.setVisibility(View.VISIBLE);
                holder.time.setText(timeToString(hour, min));
            }

            if(trip.getLocation().getIsNoTime()) {
                Toast.makeText(context, "There is not enough time to get to "+context.getString(trip.getLocation().getName()),
                        Toast.LENGTH_SHORT).show();
                holder.tripLocationContainer.setBackgroundColor(Color.GRAY);
            } else {
                holder.tripLocationContainer.setBackgroundColor(Color.WHITE);
            }

            if(isFinal && position + 1 < getItemCount() && aTrip.get(position +1).getLocation().getName() == R.string.user) {
                holder.tripLengthContainer.setVisibility(View.GONE);
//            } else if (trip.getLocation().getName() == R.string.user) {
                //ignore for now
            } else {
                Log.d(trip.getLocation().getGenericName(), "time text length: " + trip.getTimeText().length());
                holder.carIcon.setVisibility(View.VISIBLE);
                holder.tripLength.setText(trip.getTimeText());
            }
        }
    }


    @Override
    public int getItemCount() {
        if(!isFinal)
            return trips.getTrips().size();
        else {
            Iterator<String> it = TripPlannerActivity.dates.iterator();
            int count = 0;
            while (it.hasNext())
            {
                count += TripPlannerActivity.hm.get(it.next()).size();

            }
            return count;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    public class TimelineViewHolder extends RecyclerView.ViewHolder {
        public TimelineView timelineView;
        @Nullable @Bind(R.id.start_time_timeline)
        TextView time;
        @Nullable @Bind(R.id.location_name_timeline)
        TextView name;
        @Nullable @Bind(R.id.picture_timeline)
        RelativeLayout picture;
        @Nullable @Bind(R.id.trip_length_container)
        LinearLayout tripLengthContainer;
        @Nullable @Bind(R.id.trip_length_time)
        TextView tripLength;
        @Nullable @Bind(R.id.location_container)
        RelativeLayout tripLocationContainer;
        @Nullable @Bind(R.id.info_button)
        ImageButton infoButton;
        @Nullable @Bind(R.id.location_button)
        ImageButton locationButton;
        @Nullable @Bind(R.id.home_icon)
        ImageView homeIcon;
        @Nullable @Bind(R.id.trip_length_car_image)
        ImageView carIcon;

        public TimelineViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            timelineView = (TimelineView) itemView.findViewById(R.id.timeline_marker);
            timelineView.initLine(viewType);
        }
    }

    private String timeToString(int hourOfDay, int minute){
        String hourDay = "";
        String minuteDay = "";

        if(minute < 10) {
            minuteDay = "0" + minute;
        } else {
            minuteDay = minute + "";
        }
        if(hourOfDay >= 12) {
            if (hourOfDay > 12) {
                hourOfDay -= 12;
            }
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

}
