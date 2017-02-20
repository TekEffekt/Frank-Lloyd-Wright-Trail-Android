package appfactory.edu.uwp.franklloydwrighttrail.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sterl on 2/19/2017.
 */

public class TourTimesAdapter extends RecyclerView.Adapter<TourTimesAdapter.ViewHolder> {
    private TripObject locations;
    private ArrayList<TourTimesAdapter.ViewHolder> views;
    private Context context;

    public TourTimesAdapter (TripObject locations) {
        this.locations = locations;
        this.views = new ArrayList<>();
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
    public void onBindViewHolder(@NonNull final TourTimesAdapter.ViewHolder holder, int position) {
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
                getTourDate();
            }
        });

        holder.dateArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTourDate();
            }
        });

        // Gather tour time
        holder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTourTime();
            }
        });

        holder.timeArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTourTime();
            }
        });



    }

    // This method enables the user to input a new tour date
    private void getTourDate(){

    }

    // This method enables the user to input a new tour time
    private void getTourTime(){

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
        @Bind(R.id.date)
        TextView date;
        @Nullable
        @Bind(R.id.right_arrow_date)
        ImageView dateArrow;

        @Nullable
        @Bind(R.id.tour_time)
        TextView time;
        @Nullable
        @Bind(R.id.right_arrow_time)
        ImageView timeArrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
