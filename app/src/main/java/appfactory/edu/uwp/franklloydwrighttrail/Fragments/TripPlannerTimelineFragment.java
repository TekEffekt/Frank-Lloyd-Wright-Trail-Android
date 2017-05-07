package appfactory.edu.uwp.franklloydwrighttrail.Fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

import appfactory.edu.uwp.franklloydwrighttrail.Activities.TripPlannerActivity;
import appfactory.edu.uwp.franklloydwrighttrail.Apis.DirectionsApi;
import appfactory.edu.uwp.franklloydwrighttrail.Apis.DistanceMatrixApi;
import appfactory.edu.uwp.franklloydwrighttrail.Models.DirectionsModel;
import appfactory.edu.uwp.franklloydwrighttrail.FLWLocation;
import appfactory.edu.uwp.franklloydwrighttrail.Models.DistanceModel;
import appfactory.edu.uwp.franklloydwrighttrail.Models.LocationModel;
import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import appfactory.edu.uwp.franklloydwrighttrail.Adapters.TimelineAdapter;
import appfactory.edu.uwp.franklloydwrighttrail.TripObject;
import appfactory.edu.uwp.franklloydwrighttrail.TripOrder;
import appfactory.edu.uwp.franklloydwrighttrail.UserLocation;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sterl on 11/3/2016.
 */

public class TripPlannerTimelineFragment extends Fragment {
    private TripObject trip;
    private TimelineAdapter adapter;
    private LinearLayoutManager layoutManager;
    public Location location;
    public RealmList<FLWLocation> locations = LocationModel.getLocations();
    RealmList<TripOrder> mTripOrder = new RealmList<>();
    RealmList<TripOrder> newTripOrder = new RealmList<>();
    private Realm realm;
    public int locationIndex;
    private ProgressBar spinner;
    private RecyclerView timelineView;
    private static boolean isFinal;
    private static String tripPosition;
    private RealmList<TripOrder> flwLocations;
    private Button contTimes;
    private Button previous;
    public HashMap<TripOrder, Integer> positionLookup;
    private String date;
    private String key;

    public static TripPlannerTimelineFragment newInstance(boolean finalTimeline, String position){
        TripPlannerTimelineFragment tripPlannerTimelineFragment = new TripPlannerTimelineFragment();
        isFinal = finalTimeline;
        tripPosition = position;
        return tripPlannerTimelineFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_trip_timeline, container, false);
        realm = RealmController.with(this).getRealm();
        trip = RealmController.getInstance().getTripResults(tripPosition).get(0);
        key = getString(R.string.google_api_key);


        //if (RealmController.getInstance().hasTrip()){

        spinner = (ProgressBar) view.findViewById(R.id.spinner);
        if (RealmController.getInstance().getTripResults(tripPosition).get(0).getStartTime() != RealmController.getInstance().getTripResults(tripPosition).get(0).getEndTime()) {
            if(isFinal) {
                createFinalTripPlan();
            } else {
                boolean init = false;
                int i = 0;
                if(trip.getTrips().size() == 1)
                {
                    init = true;
                }
                else
                {
                    for (TripOrder tripOrder: trip.getTrips()) {
                        if (tripOrder.getStartTourTime() == -1&& i !=0) {
                            init = true;
                        }
                        i++;
                    }
                }
                if (init) {
                    initiateDataCalculation();
                }
                else
                {
                    createFinalTripPlan();
                }
            }
            // Creates Timeline if there is a trip
            timelineView = (RecyclerView) view.findViewById(R.id.trip_timeline);

            setupTimeline();
        } else {

        }

        contTimes = (Button) view.findViewById(R.id.to_times_cont);
        contTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((TripPlannerActivity)getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, TripPlannerTourTimesFragment.newInstance(tripPosition)).commit();
                ((TripPlannerActivity)getContext()).setToolbarTitle("Edit Tour Times");
            }
        });

        previous = (Button) view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFinal) {
                    realm.beginTransaction();
                    RealmController.getInstance().getTripResults(tripPosition).get(0).setFinal(false);
                    realm.commitTransaction();
                    FragmentManager fragmentManager = ((TripPlannerActivity) getContext()).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.content_frame, TripPlannerTourTimesFragment.newInstance(tripPosition)).commit();
                    ((TripPlannerActivity)getContext()).setToolbarTitle("Edit Tour Times");
                } else {
                    FragmentManager fragmentManager = ((TripPlannerActivity)getContext()).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.content_frame, TripPlannerCreateTripFragment.newInstance(tripPosition)).commit();
                    ((TripPlannerActivity)getContext()).setToolbarTitle("Create Trip");
                }
            }
        });

        if (isFinal){

            contTimes.setVisibility(View.GONE);
        }

        //Grab Trip Object
        trip = RealmController.getInstance().getTripResults(tripPosition).get(0);
        return view;
    }

    private void setupTimeline(){
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.generateDefaultLayoutParams();
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        timelineView.setLayoutManager(layoutManager);

        adapter = new TimelineAdapter(getActivity(), tripPosition,isFinal);
        timelineView.setAdapter(adapter);
    }

    public int findLocation(int location,RealmList<FLWLocation> locations)
    {
        for(int i=0;i<locations.size();i++){
            if(locations.get(i).getName() == location) {
                return i;
            }
        }
        return -1;
    }
    public void createFinalTripPlan()
    {
        trip = RealmController.getInstance().getTripResults(tripPosition).get(0);

        ArrayList<TripOrder> aTemp = new ArrayList<>();

         positionLookup = new HashMap<>();
        TripPlannerActivity.dates = new LinkedHashSet<>();
        TripPlannerActivity.hm = new HashMap<>();
        for(int j = 0;j<trip.getTrips().size();j++)
        {
            TripPlannerActivity.dates.add(trip.getTrips().get(j).getLocation().getDay());
            positionLookup.put(trip.getTrips().get(j), j);
        }
        Iterator<String> it = TripPlannerActivity.dates.iterator();
        if(it.hasNext())
            it.next();
        while(it.hasNext())
        {

            date = it.next();

            RealmList<TripOrder> flwLocations = new RealmList<>();

            for(int j = 0;j<trip.getTrips().size();j++)
            {
                if(trip.getTrips().get(j).getLocation().getName() != R.string.user)
                {

                    if(trip.getTrips().get(j).getLocation().getDay().equals(date))
                    {
                        flwLocations.add(trip.getTrips().get(j));
                        aTemp.add(trip.getTrips().get(j));
                    }
                }
                else
                {
                    if(!aTemp.contains(trip.getTrips().get(j)))
                    {
                        flwLocations.add(trip.getTrips().get(j));
                        aTemp.add(trip.getTrips().get(j));
                    }

                }
            }
            TripOrder temp;
            for(int i=0;i<flwLocations.size()-1;i++)
            {
                    if(flwLocations.get(i+1).getStartTourTime() < flwLocations.get(i).getStartTourTime())
                    {
                        temp = flwLocations.get(i);
                        flwLocations.set(i,flwLocations.get(i+1));
                        flwLocations.set(i+1,temp);
                    }
            }
                TripPlannerActivity.hm.put(date, flwLocations );
        }

        realm.beginTransaction();
        for(int i=0;i<aTemp.size();i++)
        {
            RealmController.getInstance().getTripResults(tripPosition).get(0).getTrips().set(i,aTemp.get(i));
        }
        realm.commitTransaction();



        trip = RealmController.getInstance().getTripResults(tripPosition).get(0);
        for(int i = 0;i<trip.getTrips().size();i++)
        {
            positionLookup.put(trip.getTrips().get(i), i);
        }
        it = TripPlannerActivity.dates.iterator();
        if(it.hasNext())
            it.next();
        while(it.hasNext()) {
            date = it.next();

            flwLocations = TripPlannerActivity.hm.get(date);
                if(flwLocations.size() > 2)
                {
                    String[] middleLatLong = new String[flwLocations.size() - 2];
                    String midLatLong = "";
                    int j = 0;
                    // Put the middle locations in an array
                    for (int i = 0; i < flwLocations.size(); i++) {
                        if ( i != 0 && i != flwLocations.size()-1 && flwLocations.get(i).getLocation().getLatlong() != null) {
                            middleLatLong[j] = flwLocations.get(i).getLocation().getLatlong();
                            j++;
                        }
                    }
                    // Create the middle locations string for the api
                    for (int i = 0; i < middleLatLong.length; i++) {
                        if (i != middleLatLong.length - 1) {
                            midLatLong += middleLatLong[i] + "|";
                        } else {
                            midLatLong += middleLatLong[i];
                        }
                    }
                    // Call the Directions api to get the order and travel times for each site
                    DirectionsApi directionsApi = DirectionsApi.retrofit.create(DirectionsApi.class);

                    Call<DirectionsModel> call2 = directionsApi.directions(flwLocations.get(0).getLocation().getLatlong(), flwLocations.get(flwLocations.size()-1).getLocation().getLatlong(), midLatLong,key);

                    call2.enqueue(new Callback<DirectionsModel>() {
                        @Override
                        public void onResponse(Call<DirectionsModel> call, Response<DirectionsModel> response) {
                            Toast toast;
                            if (response.isSuccessful()) {
                                int j = 0;
                                realm.beginTransaction();
                                // Grab the order of the middle sites
                                if (response.body().getRoutes().size() != 0) {
                                    for (int i = 0; i < response.body().getRoutes().get(0).getLegs().size(); i++) {
                                        // Get travel time for the start location
                                        if (i == 0) {
                                            flwLocations.get(0).setTimeText(response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText());
                                            flwLocations.get(0).setTimeValue(response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue() / 60);
                                        }

                                        // Get the travel time for the middle locations
                                        else {
                                            flwLocations.get(i).setTimeText(response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText());
                                            flwLocations.get(i).setTimeValue(response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue() / 60);
                                            j++;
                                        }
                                    }
                                }
                                for(int i = 0;i<flwLocations.size()-1;i++)
                                {
                                    flwLocations.get(i+1).getLocation().setIsNoTime(false);

                                    if(flwLocations.get(i+1).getStartTourTime() < (flwLocations.get(i).getTimeValue()+flwLocations.get(i).getEndTourTime())&& !flwLocations.get(i+1).getLocation().getIsNoTime())
                                    {
                                        flwLocations.get(i+1).getLocation().setIsNoTime(true);
                                        TripPlannerActivity.hm.put(date, flwLocations);
                                        realm.copyToRealmOrUpdate(trip);

                                    }
                                }

                                realm.commitTransaction();

                                TripPlannerActivity.hm.put(date,flwLocations);
                                adapter.setTrip(flwLocations);
                                adapter.notifyDataSetChanged();


                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsModel> call, Throwable t) {

                        }
                    });
                }
        }
        spinner.setVisibility(View.GONE);

    }


    //Z's Code
    private void initiateDataCalculation(){
        if(!isFinal) {
            FLWLocation aLatLong;
            FLWLocation bLatLong;
            final FLWLocation startLocation = new FLWLocation();
            final FLWLocation endLocation;
            String startLatLong;
            String endLatLong;
            int startLoc;
            int endLoc;
            int index;
            int aLoc;
            int bLoc;
            String midLatLong = "optimize:true|";
            trip = RealmController.getInstance().getTripResults(tripPosition).get(0);
            locations = new RealmList<>();

            for (TripOrder tp : trip.getTrips()) {
                if (!locations.contains(tp.getLocation()))
                    locations.add(tp.getLocation());


            }
            // Get the users location from realm
            UserLocation ul = RealmController.getInstance().getUserLocation();
            Location myLocation = new Location("user");
            myLocation.setLatitude(ul.getLatitude());
            myLocation.setLongitude(ul.getLongitude());
            startLocation.setLatitude(ul.getLatitude());
            startLocation.setLongitude(ul.getLongitude());
            startLocation.setLatlong(ul.getLatitude() + "," + ul.getLongitude());
            startLocation.setName(R.string.user);
            startLatLong = startLocation.getLatlong();
            startLocation.setImage(android.R.color.transparent);
            if (findLocation(R.string.user, locations) != -1) {
                locations.remove(findLocation(R.string.user, locations));
                locations.add(0, startLocation);
            } else {
                locations.add(0, startLocation);
            }

            if (findLocation(R.string.user, locations) == -1) {
                if (location == null) {
                    location = new Location("default");
                    location.setLatitude(42.7152375);
                    location.setLongitude(-87.7906969);
                }
                myLocation = location;
                // Sets start location to the users location
                startLocation.setLatitude(myLocation.getLatitude());
                startLocation.setLongitude(myLocation.getLongitude());
                startLocation.setLatlong(myLocation.getLatitude() + "," + myLocation.getLongitude());
                startLocation.setName(R.string.user);
                startLatLong = startLocation.getLatlong();
                startLocation.setImage(android.R.color.transparent);
                locations.add(0, startLocation);


            }

            startLoc = findLocation(R.string.user, locations);
            startLocation.setLatitude(locations.get(startLoc).getLatitude());
            startLocation.setLongitude(locations.get(startLoc).getLongitude());
            startLocation.setLatlong(startLocation.getLatitude() + "," + startLocation.getLongitude());
            startLocation.setName(locations.get(startLoc).getName());
            startLocation.setImage(locations.get(startLoc).getImage());
            startLatLong = startLocation.getLatlong();


            // Find one end point
            index = findLocation(R.string.scjohnson, locations);

            if (index == -1) {
                index = findLocation(R.string.wingspread, locations);
                if (index == -1) {
                    index = findLocation(R.string.built_homes, locations);
                    if (index == -1) {
                        index = findLocation(R.string.meeting_house, locations);
                        if (index == -1) {
                            index = findLocation(R.string.monona_terrace, locations);
                            if (index == -1) {
                                index = findLocation(R.string.visitor_center, locations);
                                if (index == -1){
                                    index = findLocation(R.string.valley_school, locations);
                                    if(index == -1)
                                    {
                                        index = findLocation(R.string.german_warehouse,locations);
                                    }
                                }


                            }
                        }
                    }
                }
            }

            aLatLong = locations.get(index);
            aLoc = index;
            index = findLocation(R.string.german_warehouse, locations);
            // Find the second end point
            if (index == -1) {
                index = findLocation(R.string.valley_school, locations);
                if (index == -1) {
                    index = findLocation(R.string.visitor_center, locations);
                    if (index == -1) {
                        index = findLocation(R.string.meeting_house, locations);
                        if (index == -1) {
                            index = findLocation(R.string.monona_terrace, locations);
                            if (index == -1) {
                                index = findLocation(R.string.built_homes, locations);
                                if (index == -1) {
                                    index = findLocation(R.string.wingspread, locations);
                                }
                            }
                        }
                    }
                }
            }
            if(locations.size() != 2)
            {
                bLatLong = locations.get(index);
                bLoc = index;

                Location locationA = new Location("point A");
                locationA.setLatitude(aLatLong.getLatitude());
                locationA.setLongitude(aLatLong.getLongitude());
                Location locationB = new Location("point B");
                locationB.setLatitude(bLatLong.getLatitude());
                locationB.setLongitude(bLatLong.getLongitude());
                // Check which end point is closer to the user location
                if (myLocation.distanceTo(locationA) < myLocation.distanceTo(locationB)) {

                    endLocation = bLatLong;
                    endLatLong = bLatLong.getLatlong();
                    endLoc = bLoc;
                } else {

                    endLocation = aLatLong;
                    endLatLong = aLatLong.getLatlong();
                    endLoc = aLoc;
                }
                locations.remove(endLoc);
                locations.add(endLocation);

                endLoc = findLocation(endLocation.getName(), locations);
            }
            else
            {
                endLocation = aLatLong;
                endLatLong = aLatLong.getLatlong();
                endLoc = aLoc;
                locations.remove(endLoc);
                locations.add(endLocation);

                endLoc = findLocation(endLocation.getName(), locations);
            }


            String[] middleLatLong = new String[locations.size() - 2];
            int j = 0;
            // Put the middle locations in an array
            for (int i = 0; i < locations.size(); i++) {
                if (startLoc != i && endLoc != i && locations.get(i).getLatlong() != null) {
                    middleLatLong[j] = locations.get(i).getLatlong();
                    j++;
                }
            }
            // Create the middle locations string for the api
            for (int i = 0; i < middleLatLong.length; i++) {
                if (i != middleLatLong.length - 1) {
                    midLatLong += middleLatLong[i] + "|";
                } else {
                    midLatLong += middleLatLong[i];
                }
            }
            mTripOrder = new RealmList<>();
            // Call the Directions api to get the order and travel times for each site
            DirectionsApi directionsApi = DirectionsApi.retrofit.create(DirectionsApi.class);
            Call<DirectionsModel> call2 = directionsApi.directions(startLatLong, endLatLong, midLatLong.length() == 0? "":midLatLong,key);

            call2.enqueue(new Callback<DirectionsModel>() {
                @Override
                public void onResponse(Call<DirectionsModel> call, Response<DirectionsModel> response) {
                    if (response.isSuccessful()) {
                        int j = 0;
                        ArrayList<Integer> waypointOrder = new ArrayList<>();
                        // Grab the order of the middle sites
                        for (int k = 0; k < response.body().getRoutes().get(0).getWaypointOrder().size(); k++) {
                            waypointOrder.add(response.body().getRoutes().get(0).getWaypointOrder().get(k) + 1);
                        }
                        for (int i = 0; i <= response.body().getRoutes().get(0).getLegs().size(); i++) {
                            // Get travel time for the start location
                            if (i == 0) {
                                TripOrder trip = new TripOrder(startLocation, response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText(), response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue());
                                trip.setTimeValue(trip.getTimeValue() / 60);
                                if (!mTripOrder.contains(trip))
                                    mTripOrder.add(trip);
                            }
                            // Get travel time for the end location
                            else if (i == response.body().getRoutes().get(0).getLegs().size()) {
                                TripOrder trip = new TripOrder(endLocation, "", 0);
                                trip.setTimeValue(trip.getTimeValue() / 60);
                                if (!mTripOrder.contains(trip))
                                    mTripOrder.add(trip);
                            }
                            // Get the travel time for the middle locations
                            else {
                                TripOrder trip = new TripOrder(locations.get(waypointOrder.get(j)), response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText(), response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue());
                                trip.setTimeValue(trip.getTimeValue() / 60);
                                if (!mTripOrder.contains(trip))
                                    mTripOrder.add(trip);
                                j++;
                            }
                        }
                        realm.beginTransaction();
                        trip.setTrips(mTripOrder);
                        realm.copyToRealmOrUpdate(trip);
                        realm.commitTransaction();
                        trip = RealmController.getInstance().getTripResults(tripPosition).get(0);


                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<DirectionsModel> call, Throwable t) {

                }
            });
            spinner.setVisibility(View.GONE);
        }
    }
    public void createTripPlan(RealmList<TripOrder> tripOrder)
    {
        trip = RealmController.getInstance().getTripResults(tripPosition).get(0);
        int startTime = trip.getStartTime();
        int endTime = trip.getEndTime();
        int totalTime = endTime - startTime;
        int time = 0;
        Toast toast;
        /*
        if(mealtime > totalTime)
        {
            toast = Toast.makeText(getContext(),"Meals take too much time.",Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(mealtime+time > totalTime)
        {

            time = time - tripOrder.get(tripOrder.size()-1).getTimeValue()-60;
            tripOrder.remove(tripOrder.size()-1);

            if(mealtime+time> totalTime)
            {
                time = time - tripOrder.get(tripOrder.size()-1).getTimeValue()-60;
                tripOrder.remove(tripOrder.size()-1);
                if(mealtime+time>totalTime){
                    time = time - tripOrder.get(tripOrder.size()-1).getTimeValue()-60;
                    tripOrder.remove(tripOrder.size()-1);
                    if(mealtime+time>totalTime){
                        tripOrder.remove(tripOrder.size()-1);
                    }
                    else
                    {
                        toast = Toast.makeText(getContext(),"Total trip too long 3 sites taken off.",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else
                {
                    toast = Toast.makeText(getContext(),"Total trip too long 2 sites taken off.",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            else
            {
                toast = Toast.makeText(getContext(),"Total trip too long 1 site taken off.",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else
        {
        }


        // Fill the trip object with the new times
        TripObject tObject = new TripObject(tripPosition);
        tObject.setTrips(tripOrder);
        tObject.setStartTime(trip.getStartTime());
        tObject.setEndTime(trip.getEndTime());
        for(int i = 0; i< trip.getTrips().size();i++)
        {
            if(trip.getTrips().get(i).getLocation().getGenericName() != null)
            {
                tObject.getTrips().add(trip.getTrips().get(i));
            }
        }
        */
        // Update realm with the new times




    }
}