package appfactory.edu.uwp.franklloydwrighttrail.Adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import appfactory.edu.uwp.franklloydwrighttrail.Activities.TripPlannerActivity;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.TripPlannerCreateTripFragment;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by sterl on 3/8/2017.
 */


// This adapter is used for the Tour Menu Activity to manage all trips
public class TourMenuAdapter extends RecyclerView.Adapter<TourMenuAdapter.ViewHolder> {
    private TripObject[] trips;
    private ArrayList<TourMenuAdapter.ViewHolder> views;
    private Context context;
    private Realm realm;

    public TourMenuAdapter () {
        realm = RealmController.getInstance().getRealm();
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
    public void onBindViewHolder(@NonNull TourMenuAdapter.ViewHolder holder, final int position) {
        final TripObject trip = RealmController.getInstance().getTripResults().get(position);
        //holder.name.setText(trip.getName());
        holder.name.setText(trip.getName());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TripPlannerActivity) context).showTrip(trip.getKey());
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                // use realm.delete
                RealmResults<TripObject> results = RealmController.getInstance().getTripResults(trip.getKey());
                results.deleteAllFromRealm();
                notifyDataSetChanged();
                realm.commitTransaction();
            }
        });
        holder.viewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trip.isFinal()) {
                    ((TripPlannerActivity) context).showTimeline(true, trip.getKey());
                } else {
                    ((TripPlannerActivity) context).showTrip(trip.getKey());
                }
            }
        });
    }

    public TripObject getItem(int position) {
        return trips[position];
    }

    @Override
    public int getItemCount() { return RealmController.getInstance().getTripResults().size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @Bind(R.id.trip_name)
        TextView name;

        @Nullable
        @Bind(R.id.remove_trip)
        ImageView remove;

        @Nullable
        @Bind(R.id.edit_trip)
        ImageView edit;

        @Nullable
        @Bind(R.id.right_arrow_view)
        ImageView viewTrip;

        @Nullable
        @Bind(R.id.trip_menu_item_container)
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
