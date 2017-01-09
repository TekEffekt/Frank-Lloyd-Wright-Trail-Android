package appfactory.edu.uwp.franklloydwrighttrail.Models;

import android.content.Context;

import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import io.realm.RealmResults;

/**
 * Created by sterl on 12/25/2016.
 */

public class TimelineRealmModelAdapter extends TimelineModel<TripObject> {
    public TimelineRealmModelAdapter(Context context, RealmResults<TripObject> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}

