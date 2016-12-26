package appfactory.edu.uwp.franklloydwrighttrail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by sterl on 11/3/2016.
 */

public class TimelineModel<T extends RealmObject> extends RealmBaseAdapter<T> {
    public TimelineModel(Context context, RealmResults<T> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        return null;
    }
}
