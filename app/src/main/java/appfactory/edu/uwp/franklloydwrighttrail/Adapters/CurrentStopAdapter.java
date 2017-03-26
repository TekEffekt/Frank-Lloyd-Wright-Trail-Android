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
import android.widget.TextView;

import java.util.ArrayList;

import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by sterl on 3/25/2017.
 */

public class CurrentStopAdapter extends RecyclerView.Adapter<CurrentStopAdapter.ViewHolder> {
    private TripObject locations;
    private ArrayList<CurrentStopAdapter.ViewHolder> views;
    private Context context;
    private Realm realm;
    private String tripPosition;

    public CurrentStopAdapter (TripObject locations, String tripPosition) {
        this.tripPosition = tripPosition;
        this.locations = locations;
        this.views = new ArrayList<>();
    }

    @NonNull
    @Override
    public CurrentStopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        views.add(new CurrentStopAdapter.ViewHolder(inflater.inflate(R.layout.tour_stop_item, parent, false)));
        return views.get(views.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentStopAdapter.ViewHolder holder, final int position) {
        FLWLocation location = locations.getTrips().get(position).getLocation();

        if (location.getName()!= -1) {
            holder.name.setText(location.getName());
        } else {
            holder.name.setText(location.getGenericName());
        }

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                RealmController.getInstance().getTripResults(tripPosition).get(0).getTrips().remove(position);
                notifyDataSetChanged();
                realm.commitTransaction();
            }
        });
    }

    public FLWLocation getItem(int position) {
        return locations.getTrips().get(position).getLocation();
    }

    @Override
    public int getItemCount() { return locations.getTrips().size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @Bind(R.id.recycler_stop_name)
        TextView name;

        @Nullable
        @Bind(R.id.remove_stop_button)
        ImageView remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
