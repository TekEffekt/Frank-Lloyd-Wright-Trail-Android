package appfactory.edu.uwp.franklloydwrighttrail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipul.hp_hp.timelineview.TimelineView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by sterl on 11/3/2016.
 */

public class TimelineAdapter extends TimelineRealmAdapter<TripObject> {
    private ArrayList<TimelineAdapter.TimelineViewHolder> views;
    private Context context;
    private TripObject trips;

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

        TripOrder trip = trips.getTrips().get(position);
        holder.picture.setBackground(ContextCompat.getDrawable(context, trip.getLocation().getImage()));
        //Debug Code
        if (trip.getTimeText() == null){
            holder.name.setText(trip.getLocation().getName());
            holder.time.setText(trip.getTimeText());
            holder.tripLength.setText(trip.getTimeValue());
        } else {
            holder.name.setText(trip.getLocation().getName());
            holder.time.setText("ERROR");
            holder.tripLength.setText("ERROR");
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

        public TimelineViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            timelineView = (TimelineView) itemView.findViewById(R.id.timeline_marker);
            timelineView.initLine(viewType);
        }
    }
}
