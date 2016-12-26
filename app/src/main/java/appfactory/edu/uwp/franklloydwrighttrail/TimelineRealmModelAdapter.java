package appfactory.edu.uwp.franklloydwrighttrail;

import android.content.Context;

import io.realm.RealmResults;

/**
 * Created by sterl on 12/25/2016.
 */

public class TimelineRealmModelAdapter extends TimelineModel<TripObject> {
    public TimelineRealmModelAdapter(Context context, RealmResults<TripObject> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}

