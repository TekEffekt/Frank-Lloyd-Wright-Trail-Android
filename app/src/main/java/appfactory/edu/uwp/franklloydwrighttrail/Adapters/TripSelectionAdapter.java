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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmList;

/**
 * Created by sterl on 10/29/2016.
 */

public class TripSelectionAdapter extends RecyclerView.Adapter<TripSelectionAdapter.ViewHolder> {
    private RealmList<FLWLocation> locations;
    private ArrayList<TripSelectionAdapter.ViewHolder> views;
    private Context context;

    public TripSelectionAdapter (RealmList<FLWLocation> locations) {
        this.locations = locations;
        this.views = new ArrayList<>();
    }

    @NonNull
    @Override
    public TripSelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        views.add(new TripSelectionAdapter.ViewHolder(inflater.inflate(R.layout.trip_location_item, parent, false)));
        return views.get(views.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull TripSelectionAdapter.ViewHolder holder, int position) {
        FLWLocation location = locations.get(position);
        holder.picture.setBackground(ContextCompat.getDrawable(context, location.getImage()));
        holder.name.setText(location.getName());
        if (location.getName() == R.string.scjohnson){
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        }
    }

    public FLWLocation getItem(int position) {
        return locations.get(position);
    }

    @Override
    public int getItemCount() { return locations.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @Bind(R.id.name)
        TextView name;
        @Nullable @Bind(R.id.picture)
        RelativeLayout picture;
        @Nullable
        @Bind(R.id.trip_length_time)
        TextView tripLengthTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
