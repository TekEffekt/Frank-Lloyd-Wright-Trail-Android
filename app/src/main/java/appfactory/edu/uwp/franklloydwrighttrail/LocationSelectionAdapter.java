package appfactory.edu.uwp.franklloydwrighttrail;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Work in progress for the Recycler View
 */

public class LocationSelectionAdapter extends RecyclerView.Adapter<LocationSelectionAdapter.ViewHolder> {
    private ArrayList<FLWLocation> locations;
    private Context context;

    public LocationSelectionAdapter (ArrayList<FLWLocation> locations) {this.locations = locations; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        return new ViewHolder(inflater.inflate(R.layout.location_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FLWLocation location = locations.get(position);
        holder.picture.setBackground(ContextCompat.getDrawable(context, location.getImage()));
        holder.textView.setText(location.getName());
        holder.marker.setImageResource(location.getMarkerColor());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
