package appfactory.edu.uwp.franklloydwrighttrail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    private ArrayList<TimelineAdapter.TimelineViewHolder> views;

    @Override
    public TimelineAdapter.TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.timeline_item, null);
        return new TimelineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimelineViewHolder holder, int position) {
        //where we wanna put whatever
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    public class TimelineViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @Bind(R.id.name)
        TextView textView;
        @Nullable @Bind(R.id.picture)
        RelativeLayout picture;
        public TimelineView timelineView;

        public TimelineViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            timelineView = (TimelineView) itemView.findViewById(R.id.trip_timeline);
            timelineView.initLine(viewType);
        }
    }
}
