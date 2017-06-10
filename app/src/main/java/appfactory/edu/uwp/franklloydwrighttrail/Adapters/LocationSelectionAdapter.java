package appfactory.edu.uwp.franklloydwrighttrail.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import appfactory.edu.uwp.franklloydwrighttrail.Activities.LocationSelectionActivity;
import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmList;

/**
 * Work in progress for the Recycler View
 */

public class LocationSelectionAdapter extends RecyclerView.Adapter<LocationSelectionAdapter.ViewHolder> {
    private RealmList<FLWLocation> locations;
    private ArrayList<ViewHolder> views;
    private Context context;

    public LocationSelectionAdapter (RealmList<FLWLocation> locations) {
        this.locations = locations;
        this.views = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        views.add(new ViewHolder(inflater.inflate(R.layout.location_item, parent, false)));
        return views.get(views.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FLWLocation location = locations.get(position);
        holder.picture.setBackground(ContextCompat.getDrawable(context, location.getImage()));
        holder.textView.setText(location.getName());
        if (location.getName() == R.string.scjohnson){
            holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        }
        holder.marker.setImageResource(location.getMarkerColor());
        if (LocationSelectionActivity.isReceivingLocation()) {
            float miles = LocationSelectionActivity.updateDistance(position)/(float) 1609.344;
            holder.distance.setText(String.format("%.1f", miles) + " mi");
            holder.distance.setVisibility(View.VISIBLE);
        } else {
            holder.distance.setVisibility(View.INVISIBLE);
        }
    }

    public void updateDistance() {
        for (ViewHolder holder: views) {
            float miles = LocationSelectionActivity.updateDistance(holder.getAdapterPosition())/(float) 1609.344;
            holder.distance.setText(String.format("%.1f", miles) + " mi");
            holder.distance.setVisibility(View.VISIBLE);
        }
    }

    public void disableDistance() {
        for (int i = 0; i < views.size(); i++) {
            ViewHolder holder = views.get(i);
            holder.distance.setVisibility(View.INVISIBLE);
        }
    }

    public FLWLocation getItem(int position) {
        return locations.get(position);
    }

    @Override
    public int getItemCount() { return locations.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable @Bind(R.id.name) TextView textView;
        @Nullable @Bind(R.id.picture) RelativeLayout picture;
        @Nullable @Bind(R.id.marker) ImageView marker;
        @Nullable @Bind(R.id.distance) TextView distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
