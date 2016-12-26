package appfactory.edu.uwp.franklloydwrighttrail;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by sterl on 12/24/2016.
 */

public class RealmController {
    private  static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment){
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    public void refresh() {
        realm.refresh();
    }

    public void clearAll() {
        realm.beginTransaction();;
        realm.clear(TripObject.class);
        realm.commitTransaction();
    }

    public TripObject getTrip(){
        if (hasTrip()) {
            RealmResults<TripObject> results = realm.where(TripObject.class).findAll();
            return results.get(0);
        } else {
            return null;
        }
    }

    public RealmResults<TripObject> getTripResults(){
        return realm.where(TripObject.class).findAll();
    }

    public boolean hasTrip() {
        return !realm.allObjects(TripObject.class).isEmpty();
    }

}

