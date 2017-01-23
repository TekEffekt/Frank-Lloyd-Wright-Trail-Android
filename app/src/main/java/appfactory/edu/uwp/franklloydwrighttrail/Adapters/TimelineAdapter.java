package appfactory.edu.uwp.franklloydwrighttrail.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipul.hp_hp.timelineview.TimelineView;

import java.util.ArrayList;

import appfactory.edu.uwp.franklloydwrighttrail.Activities.DescriptonActivity;
import appfactory.edu.uwp.franklloydwrighttrail.Activities.LocationSelectionActivity;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import appfactory.edu.uwp.franklloydwrighttrail.TripOrder;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sterl on 11/3/2016.
 */

public class TimelineAdapter extends TimelineRealmAdapter<TripObject> {
    private ArrayList<TimelineAdapter.TimelineViewHolder> views;
    private Context context;
    private TripObject trips;
    int tLine = 0;
    int temp =0;
    int counter =0;

    public TimelineAdapter (Context context){
        this.context = context;
        this.views = new ArrayList<>();
        trips = RealmController.getInstance().getTrip();
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
        //hides if last
        if (trips.getTrips().size() - 1 == position){
            holder.tripLengthContainer.setVisibility(View.GONE);
        }

        final TripOrder trip = trips.getTrips().get(position);
        holder.picture.setBackground(ContextCompat.getDrawable(context, trip.getLocation().getImage()));
        //Debug Code

        if (position != 0) {
            holder.infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sName = context.getString(trip.getLocation().getName());
                    Intent intent = new Intent(context, DescriptonActivity.class);
                    intent.putExtra("Title", sName);
                    context.startActivity(intent);
                }
            });

            holder.locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Grab Location
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + trip.getLocation().getLatlong());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
                }
            });


        } else {
            holder.infoButton.setVisibility(View.GONE);
            holder.locationButton.setVisibility(View.GONE);
            holder.picture.setVisibility(View.GONE);
        }

        if (trip.getTimeText() == null){
            holder.name.setText(trip.getLocation().getName());
            holder.time.setText(trip.getTimeText());
            holder.tripLength.setText(trip.getTimeValue());

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
                tLine = trips.getStartTime()+temp+tLine;
                hour = tLine/60;
                min = tLine%60;
                temp = trip.getTimeValue();

            }
            else if(position == 2)
            {
                tLine = 60+temp+tLine;
                hour = tLine/60;
                min = tLine%60;
                temp = trip.getTimeValue();

            }
            else if(position == 3)
            {
                tLine = 60+temp+tLine;
                hour = tLine/60;
                min = tLine%60;
                temp = trip.getTimeValue();

            }
            else if(position == 4)
            {
                tLine = 60+temp+tLine;
                hour = tLine/60;
                min = tLine%60;
                temp = trip.getTimeValue();

            }
            else if(position == 5)
            {
                tLine = 60+temp+tLine;
                hour = tLine/60;
                min = tLine%60;
                temp = trip.getTimeValue();

            }
            else if(position == 6)
            {
                tLine = 60+temp+tLine;
                hour = tLine/60;
                min = tLine%60;
                temp = trip.getTimeValue();

            }
            else if(position == 7)
            {
                tLine = 60+temp+tLine;
                hour = tLine/60;
                min = tLine%60;
                temp = trip.getTimeValue();

            }
            else if(position == 8)
            {
                tLine = 60+temp+tLine;
                hour = tLine/60;
                min = tLine%60;
                temp = trip.getTimeValue();

            }
            else if(position == 9)
            {
                tLine = 60+temp+tLine;
                hour = tLine/60;
                min = tLine%60;
                temp = trip.getTimeValue();

            }
            if(hour > 23)
            {
                hour = hour - 23;
            }
            holder.name.setText(trip.getLocation().getName());
            if(hour < 12 && min < 10)
            {
                holder.time.setText(hour + ":" + "0" + min + " AM");
            }
            else if (hour == 12 && min < 10) {
                holder.time.setText(hour + ":" + "0" +min + " PM");
            }
            else if (hour == 12) {
                int tempHour = hour - 12;
                holder.time.setText(hour + ":" + min + " PM");
            }
            else if (hour > 12 && min < 10) {
                int tempHour = hour - 12;
                holder.time.setText(tempHour + ":" + "0" +min + " PM");
            }
            else if (hour > 12) {
                int tempHour = hour - 12;
                holder.time.setText(tempHour + ":" + min + " PM");
            }
            else
            {
                holder.time.setText(hour + ":" + min + " AM");
            }

            holder.tripLength.setText(trip.getTimeText());
        }

    }


    @Override
    public int getItemCount() {
        return trips.getTrips().size();
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

        public TimelineViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            timelineView = (TimelineView) itemView.findViewById(R.id.timeline_marker);
            timelineView.initLine(viewType);
        }
    }
}
