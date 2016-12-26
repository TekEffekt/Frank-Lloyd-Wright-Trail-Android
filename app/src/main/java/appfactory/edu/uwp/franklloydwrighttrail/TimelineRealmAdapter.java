package appfactory.edu.uwp.franklloydwrighttrail;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;

/**
 * Created by sterl on 12/25/2016.
 */

public class TimelineRealmAdapter <T extends RealmObject> extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {

    private RealmBaseAdapter<T> realmBaseAdapter;

    public T getItem(int position) {
        return realmBaseAdapter.getItem(position);
    }

    public RealmBaseAdapter<T> getRealmAdapter() {
        return realmBaseAdapter;
    }

    public void setRealmAdapter(RealmBaseAdapter<T> realmAdapter) {
        realmBaseAdapter = realmAdapter;
    }

    @Override
    public TimelineAdapter.TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TimelineAdapter.TimelineViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}