package appfactory.edu.uwp.franklloydwrighttrail.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmList;

/**
 * Created by sterl on 3/8/2017.
 */


// This adapter is used for the Tour Menu Activity to manage all trips
public class TourMenuAdapter extends RecyclerView.Adapter<TourMenuAdapter.ViewHolder> {
    private List<TripObject> trips;
    private ArrayList<TourMenuAdapter.ViewHolder> views;
    private Context context;

    public TourMenuAdapter (List<TripObject> trips) {
        this.trips = trips;
        this.views = new ArrayList<>();
    }

    @NonNull
    @Override
    public TourMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        views.add(new TourMenuAdapter.ViewHolder(inflater.inflate(R.layout.trip_menu_item, parent, false)));
        return views.get(views.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull TourMenuAdapter.ViewHolder holder, int position) {
        TripObject trip = trips.get(position);
        holder.name.setText(trip.getName());
        holder.viewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set this up so it brings you to the new/finished timeline fragment
            }
        });
    }

    public TripObject getItem(int position) {
        return trips.get(position);
    }

    @Override
    public int getItemCount() { return trips.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @Bind(R.id.trip_name)
        TextView name;

        @Nullable
        @Bind(R.id.right_arrow_view)
        ImageView viewTrip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
