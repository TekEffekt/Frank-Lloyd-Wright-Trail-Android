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
import java.util.Comparator;
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
    private Realm realm;
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
        spinner = (ProgressBar) view.findViewById(R.id.spinner);

        setupButtons(view);

        if (isFinal) {
            createFinalTripPlan();
            contTimes.setVisibility(View.GONE);
        } else {
            initiateDataCalculation();
        }

        timelineView = (RecyclerView) view.findViewById(R.id.trip_timeline);
        setupTimeline();

        //Grab Trip Object
        trip = RealmController.getInstance().getTripResults(tripPosition).get(0);
        return view;
    }

    //Sets up buttons
    private void setupButtons(View view){
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
    }

    // Connects timeline to adapter
    private void setupTimeline(){
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.generateDefaultLayoutParams();
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        timelineView.setLayoutManager(layoutManager);

        adapter = new TimelineAdapter(getActivity(), tripPosition,isFinal);
        timelineView.setAdapter(adapter);
    }

    // Given a location name, using the list of locations, find and return it's position
    public int findLocation(int location, RealmList<FLWLocation> locations) {
        for(int i=0; i < locations.size(); i++){
            if(locations.get(i).getName() == location) {
                return i;
            }
        }
        return -1; // Out of index return
    }

    // Creates the final trip plan
    public void createFinalTripPlan() {
        trip = RealmController.getInstance().getTripResults(tripPosition).get(0);

        ArrayList<TripOrder> aTemp = new ArrayList<>();

        // Contains the TripOrder and a Position #Note to self, What is the point of this
        positionLookup = new HashMap<>();
        TripPlannerActivity.dates = new LinkedHashSet<>(); // Contains the dates of each location
        TripPlannerActivity.hm = new HashMap<>(); // Contains days with sorted trips

        // Scan through each trip
        for(int j = 0; j < trip.getTrips().size(); j++) {
            // Place the date into it's linked hash set
            TripPlannerActivity.dates.add(trip.getTrips().get(j).getLocation().getDay());
            // Put the trip and it's position into a HashMap
            positionLookup.put(trip.getTrips().get(j), j);
        }

        Iterator<String> it = TripPlannerActivity.dates.iterator();
        if(it.hasNext()) {
            it.next(); // Skips the first item if there are any dates
        }

        while(it.hasNext()) { // Scans through each day
            date = it.next(); // Grabs current day

            // Starts a new list of locations
            RealmList<TripOrder> flwLocations = new RealmList<>();

            // Scan through each trip
            for(int j = 0; j < trip.getTrips().size(); j++) {
                // As long as it's not "Home"
                if(trip.getTrips().get(j).getLocation().getName() != R.string.user) {
                    // If the day is equal to the current date
                    if(trip.getTrips().get(j).getLocation().getDay().equals(date)) {
                        flwLocations.add(trip.getTrips().get(j)); // Add location to current day trip
                        aTemp.add(trip.getTrips().get(j)); // Add location to overall trip
                    }
                } else { // If it is home #Note to self, not sure what the point of this is
                    // as long as the overall trip doesn't contain the current location
                    if(!aTemp.contains(trip.getTrips().get(j))) {
                        flwLocations.add(trip.getTrips().get(j)); // Add location to current day
                        aTemp.add(trip.getTrips().get(j)); // Add location to overall trip
                    }
                }
            }
            // Sort the locations in current day based on starting time
            Collections.sort(flwLocations, new Comparator<TripOrder>() {
                @Override
                public int compare(TripOrder o1, TripOrder o2) {
                    return Long.valueOf(o1.getStartTourTime()).compareTo(o2.getStartTourTime());
                }
            });
            // Put the date in the HashMap with the day's trip as the value
            TripPlannerActivity.hm.put(date, flwLocations);
        }

        realm.beginTransaction();
        for(int i=0; i < aTemp.size(); i++) {
            // Set current Position in Current Trip Plan to contain
            // the location of the overall day's trip in order
            RealmController.getInstance().getTripResults(tripPosition).get(0).getTrips().set(i, aTemp.get(i));
        }
        realm.commitTransaction();

        // Set current viewed trip to now have our updated trip list
        trip = RealmController.getInstance().getTripResults(tripPosition).get(0);
        for(int i = 0; i < trip.getTrips().size(); i++) {
            positionLookup.put(trip.getTrips().get(i), i); // Reset each trip to now have the current position
        }
        it = TripPlannerActivity.dates.iterator(); // Reset iterator
        if(it.hasNext()) { // Skip first date
            it.next();
        }
        while(it.hasNext()) { // Run through each date
            date = it.next(); // Set current date

            flwLocations = TripPlannerActivity.hm.get(date); // Get ordered list for current day
            if(flwLocations.size() >= 2) { // As long as the trip list contains 2 or more locations
                // Create an array to contain the middle locations
                String[] middleLatLong = new String[flwLocations.size() - 2];
                String midLatLong = ""; // instantiate variable
                int arrayPos = 0; // Current position of middleLatLong array
                for (int i = 1; i < flwLocations.size(); i++) {
                    // as long as the location is not the last one and
                    // the location has a latlong
                    if (i != flwLocations.size() - 1 && flwLocations.get(i).getLocation().getLatlong() != null) {
                        // Add the latlong to the array
                        middleLatLong[arrayPos] = flwLocations.get(i).getLocation().getLatlong();
                        arrayPos++;
                    }
                }
                // Create the middle locations string for the api
                for (int i = 0; i < middleLatLong.length; i++) {
                    // This is for formatting, it's basic concatenation
                    if (i != middleLatLong.length - 1) {
                        midLatLong += middleLatLong[i] + "|";
                    } else {
                        midLatLong += middleLatLong[i];
                    }
                }
                // Call the Directions api to get the order and travel times for each site
                DirectionsApi directionsApi = DirectionsApi.retrofit.create(DirectionsApi.class);
                Call<DirectionsModel> call2 = directionsApi.directions(flwLocations.get(0).getLocation().getLatlong(),
                        flwLocations.get(flwLocations.size()-1).getLocation().getLatlong(), midLatLong, key);
                // place date into a final string variable for inner method use
                final String finalDate = date;
                call2.enqueue(new Callback<DirectionsModel>() {
                    @Override
                    public void onResponse(Call<DirectionsModel> call, Response<DirectionsModel> response) {
                        // Grab list of trips based on date
                        RealmList<TripOrder> flwLocations = TripPlannerActivity.hm.get(finalDate);
                        if (response.isSuccessful()) {
                            realm.beginTransaction();
                            // Grab the order of the middle sites
                            if (response.body().getRoutes().size() != 0) {
                                for (int i = 0; i < response.body().getRoutes().get(0).getLegs().size(); i++) {
                                    // Get travel time for the locations
                                    if (i < flwLocations.size() - 1) {
                                        flwLocations.get(i).setTimeText(response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText());
                                        flwLocations.get(i).setTimeValue(response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue() / 60);

                                        if(flwLocations.size() > 2 && flwLocations.get(i+1).getStartTourTime() <
                                                (flwLocations.get(i).getTimeValue() + flwLocations.get(i).getEndTourTime()) &&
                                                flwLocations.get(i+1).getLocation().getDay().equals(flwLocations.get(i).getLocation().getDay())) {
                                            flwLocations.get(i+1).getLocation().setIsNoTime(true);
                                        } else {
                                            flwLocations.get(i+1).getLocation().setIsNoTime(false);
                                        }
                                    } else {
                                        Log.e("Timeline", "direction request and number of locations for day do not match");
                                    }
                                }
                            }
                            /*for (TripOrder location: flwLocations) {
                                Log.d("location day", location.getLocation().getDay() == null ? "no date" : location.getLocation().getDay());
                            }*/
                            realm.copyToRealmOrUpdate(trip);
                            realm.commitTransaction();

                            TripPlannerActivity.hm.put(finalDate, flwLocations);
                            adapter.setTrip(flwLocations);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(Call<DirectionsModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Please connect to the internet for most recent trip times",Toast.LENGTH_LONG).show();
                    }
                });
            } else { // Whenever trips are one location
                realm.beginTransaction();
                for (TripOrder tripOrder: flwLocations) {
                    Log.d("location day", tripOrder.getLocation().getDay() == null ? "no date" : tripOrder.getLocation().getDay());
                    tripOrder.getLocation().setIsNoTime(false);
                }
                TripPlannerActivity.hm.put(date,flwLocations);

                realm.copyToRealmOrUpdate(trip);
                realm.commitTransaction();
            }
        }
        spinner.setVisibility(View.GONE);
    }

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
                if (!locations.contains(tp.getLocation())) {
                    locations.add(tp.getLocation());
                }
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
                                    if(index == -1) {
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
            if(locations.size() != 2) {
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
            } else {
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
                            } // Get travel time for the end location
                            else if (i == response.body().getRoutes().get(0).getLegs().size()) {
                                TripOrder trip = new TripOrder(endLocation, "", 0);
                                trip.setTimeValue(trip.getTimeValue() / 60);
                                if (!mTripOrder.contains(trip)) {
                                    mTripOrder.add(trip);
                                }
                            } else { // Get the travel time for the middle locations
                                TripOrder trip = new TripOrder(locations.get(waypointOrder.get(j)), response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText(), response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue());
                                trip.setTimeValue(trip.getTimeValue() / 60);
                                if (!mTripOrder.contains(trip)) {
                                    mTripOrder.add(trip);
                                }
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
                    Toast.makeText(getContext(), "Please connect to the internet for most recent times",Toast.LENGTH_LONG).show();
                }
            });
            spinner.setVisibility(View.GONE);
        }
    }
}