package appfactory.edu.uwp.franklloydwrighttrail.Adapters;

import android.content.Context;
import android.content.Intent;
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

import com.vipul.hp_hp.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.Iterator;

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

    public TimelineAdapter (Context context, String tripPosition, boolean isFinal){
        this.context = context;
        this.views = new ArrayList<>();
        trips = RealmController.getInstance().getTripResults(tripPosition).get(0);
        this.isFinal = isFinal;
        if(isFinal)
        {
            Iterator<String> it = TripPlannerActivity.dates.iterator();

            RealmList<TripOrder> temp;
            if(it.hasNext())
            it.next();

            while(it.hasNext())
            {
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
        if(isFinal)
        {
            Iterator<String> it = TripPlannerActivity.dates.iterator();

            RealmList<TripOrder> temp;
            if(it.hasNext())
            it.next();
            aTrip = new RealmList<>();
            while(it.hasNext())
            {
                temp = TripPlannerActivity.hm.get(it.next());
                for(int i = 0;i<temp.size();i++)
                {
                    aTrip.add(temp.get(i));

                }
            }

        }
        //hides if last
        if (trips.getTrips().size() - 1 == position){
            holder.tripLengthContainer.setVisibility(View.GONE);
        }
        if(!isFinal)
        {
            trip = trips.getTrips().get(position);
        }
        else
        {
            trip = aTrip.get(position);
        }


        if (trip.getLocation().getImage() != -1) {
            holder.picture.setBackground(ContextCompat.getDrawable(context, trip.getLocation().getImage()));
        }

        if (position != 0) {
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
            holder.infoButton.setVisibility(View.GONE);
            holder.locationButton.setVisibility(View.GONE);
            holder.picture.setVisibility(View.GONE);
            holder.homeIcon.setVisibility(View.VISIBLE);
        }
        Log.d("debug", "Time text: " + trip.getTimeText());
            if (trip.getTimeText() == null){
                if(trip.getLocation().getGenericName() != null)
                {
                    holder.name.setText(trip.getLocation().getGenericName());
                }
                else
                {
                    holder.name.setText(trip.getLocation().getName());
                    holder.time.setText(trip.getTimeText());
                    holder.tripLength.setText(trip.getTimeValue());
                }

            } else {
                int hour =0;
                int min =0;

                if(position == 0)
                {
                    if(counter == 1)
                    {
                        counter = 0;
                        tLine = 0;
                    }
                    hour = trips.getStartTime()/60;
                    min = trips.getStartTime()%60;
                    temp = trip.getTimeValue();
                    counter++;
                }
                else if(position == 1)
                {
                    if(isFinal) {

                        hour = (int) trip.getStartTourTime() / 60;
                        min = (int) trip.getStartTourTime() % 60;
                    }
                    else
                    {
                        tLine = trips.getStartTime() + temp + tLine;
                        hour = tLine/60;
                        min = tLine%60;
                        temp = trip.getTimeValue();
                    }
                }
                else if(position > 1)
                {
                    if(isFinal) {
                        hour = (int) trip.getStartTourTime() / 60;
                        min = (int) trip.getStartTourTime() % 60;
                    } else {
                        tLine = 60 + temp + tLine;
                        hour = tLine/60;
                        min = tLine%60;
                        temp = trip.getTimeValue();
                    }
                }
                if (trip.getLocation().getImage() != -1){
                    holder.name.setText(trip.getLocation().getName());
                    if (trip.getLocation().getName() == R.string.scjohnson){
                        holder.name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    }
                } else {
                    holder.name.setText(trip.getLocation().getGenericName());
                }

                holder.time.setText(timeToString(hour,min));

                if(isFinal && position !=0 && position < getItemCount()-2 && !trip.getLocation().getDay().equals(aTrip.get(position+1).getLocation().getDay()))
                {
                    holder.tripLength.setText(aTrip.get(position+1).getLocation().getDay());
                    holder.carIcon.setVisibility(View.GONE);
                }
                else
                {
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
            it.next();
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

}
