package appfactory.edu.uwp.franklloydwrighttrail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipul.hp_hp.timelineview.TimelineView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sterl on 11/3/2016.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {
    private ArrayList<FLWLocation> trips;
    private ArrayList<TimelineAdapter.TimelineViewHolder> views;
    private Context context;

    public TimelineAdapter (ArrayList<FLWLocation> trips) {
        this.trips = trips;
        this.views = new ArrayList<>();
    }

    @NonNull
    @Override
    public TimelineAdapter.TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.timeline_item, null);
        return new TimelineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {
        FLWLocation trip = trips.get(position);
        holder.picture.setBackground(ContextCompat.getDrawable(context, trip.getImage()));
        holder.name.setText(trip.getName());
        //TEMP
        holder.time.setText((position + 1) + ":00PM");
    }


    @Override
    public int getItemCount() {
        return trips.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    public class TimelineViewHolder extends RecyclerView.ViewHolder {
        @Nullable @Bind(R.id.start_time_timeline)
        TextView time;
        @Nullable @Bind(R.id.location_name_timeline)
        TextView name;
        @Nullable @Bind(R.id.picture_timeline)
        RelativeLayout picture;
        public TimelineView timelineView;

        public TimelineViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            timelineView = (TimelineView) itemView.findViewById(R.id.timeline_marker);
            timelineView.initLine(viewType);
        }
    }
}
