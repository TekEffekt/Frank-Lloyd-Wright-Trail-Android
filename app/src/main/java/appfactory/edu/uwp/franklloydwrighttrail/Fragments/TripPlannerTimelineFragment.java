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
    public RealmList<FLWLocation> locations = LocationModel.getLocations();
    RealmList<TripOrder> tripsOrder = new RealmList<>();
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
    private int[] sortedTrips;
    private Location myLocation;

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
        trip = RealmController.getInstance().getTripResults(tripPosition).get(0);
        FLWLocation startLocation; // Contains first non Home Location
        FLWLocation endLocation; // Contains last location (if there's more than 2)
        String homeLatLong; // Contains LatLong of user location
        String endLatLong; // Contains LatLong of last location
        int endLoc; // Position of final location

        // These are relative and can swap positions, they are basically endpoints
        int startIndex; // Position of first trip location
        int endIndex; // Position of last trip location
        String midLatLong = "optimize:true|";

        locations = new RealmList<>();
        locations = createHome(locations);
        for (TripOrder tp : trip.getTrips()) {
            // Adds locations to location list while checking for duplicates
            if (!locations.contains(tp.getLocation())) {
                locations.add(tp.getLocation());
            }
        }
        
        // Create Home Point
        final FLWLocation homeLocation = locations.get(0);
        homeLatLong = homeLocation.getLatlong();

        // Find start point
        sortedTrips = sortLocations();
        int index = -1;
        for(int i = 0; i < sortedTrips.length; i++){
            if (sortedTrips[i] != 0){
                index = i;
            }
        }
        startLocation = locations.get(index);
        startIndex = index;

        final FLWLocation finalLocation; // Declared final to work in inner classes
        if(locations.size() > 2) { // as long as there's more than 2 locations
            for(int i = 7; i != 0; i--){
                if (sortedTrips[i] != 0){
                    index = i;
                }
            }
            endLocation = locations.get(index);
            endIndex = index;

            Location locationA = toLocation(startLocation);
            Location locationB = toLocation(endLocation);

            // Check which end point is closer to the user location
            if (myLocation.distanceTo(locationA) < myLocation.distanceTo(locationB)) {
                finalLocation = endLocation; // The "farthest" location is the closest
                endLatLong = endLocation.getLatlong();
                endLoc = endIndex; // Set index to be deleted later
            } else {
                finalLocation = startLocation; // The "closest" location is the closest
                endLatLong = startLocation.getLatlong();
                endLoc = startIndex; // Set index to be deleted later
            }
        } else {
            finalLocation = startLocation; // The "closest" location is the closest
            endLatLong = startLocation.getLatlong();
            endLoc = startIndex; // Set index to be deleted later
        }

        locations.remove(endLoc); // Delete old location.
        locations.add(finalLocation); // Add the same location as last stop

        // Concatenate our middle locations into an api formatted string
        for (int i = 1; i < locations.size() - 1; i++) {
            if (i != locations.size() - 2) {
                midLatLong += locations.get(i).getLatlong() + "|";
            } else {
                midLatLong += locations.get(i).getLatlong();
            }
        }

        tripsOrder = new RealmList<>();
        // Call the Directions api to get the order and travel times for each site
        DirectionsApi directionsApi = DirectionsApi.retrofit.create(DirectionsApi.class);
        Call<DirectionsModel> call2 = directionsApi.directions(homeLatLong, endLatLong, midLatLong.length() == 0? "":midLatLong,key);

        call2.enqueue(new Callback<DirectionsModel>() {
            @Override
            public void onResponse(Call<DirectionsModel> call, Response<DirectionsModel> response) {
                if (response.isSuccessful()) {
                    ArrayList<Integer> wayPointOrder = new ArrayList<>();
                    // Grab the order of the middle sites
                    for (int i = 0; i < response.body().getRoutes().get(0).getWaypointOrder().size(); i++) {
                        wayPointOrder.add(response.body().getRoutes().get(0).getWaypointOrder().get(i) + 1);
                    }
                    for (int i = 0; i <= response.body().getRoutes().get(0).getLegs().size(); i++) {
                        // Get travel time for the start location
                        if (i == 0) {
                            TripOrder location = new TripOrder(homeLocation,
                                    response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText(),
                                    response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue());
                            location.setTimeValue(location.getTimeValue() / 60);
                            tripsOrder.add(location);
                        } // Get travel time for the end location
                        else if (i == response.body().getRoutes().get(0).getLegs().size()) {
                            TripOrder location = new TripOrder(finalLocation, "", 0);
                            location.setTimeValue(location.getTimeValue() / 60);
                            tripsOrder.add(location);
                        } else { // Get the travel time for the middle locations
                            TripOrder location = new TripOrder(locations.get(wayPointOrder.get(i - 1)),
                                    response.body().getRoutes().get(0).getLegs().get(i).getDuration().getText(),
                                    response.body().getRoutes().get(0).getLegs().get(i).getDuration().getValue());
                            location.setTimeValue(location.getTimeValue() / 60);
                            tripsOrder.add(location);
                        }
                    }
                    realm.beginTransaction();
                    trip.setTrips(tripsOrder);
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

    // Creates a FLWLocation object based on user's current position
    // Returns list of locations including "Home" as first object
    private RealmList<FLWLocation> createHome(RealmList<FLWLocation> locations){
        FLWLocation homeLocation = new FLWLocation();

        UserLocation ul = RealmController.getInstance().getUserLocation();
        myLocation = new Location("user");
        myLocation.setLatitude(ul.getLatitude());
        myLocation.setLongitude(ul.getLongitude());
        homeLocation.setLatitude(ul.getLatitude());
        homeLocation.setLongitude(ul.getLongitude());
        homeLocation.setLatlong(ul.getLatitude() + "," + ul.getLongitude());
        homeLocation.setName(R.string.user);
        homeLocation.setImage(android.R.color.transparent);
        if (findLocation(R.string.user, locations) != -1) {
            locations.remove(findLocation(R.string.user, locations));
        }
        locations.add(0, homeLocation);
        return locations;
    }

    // Translates FLWLocation to Location
    private Location toLocation (FLWLocation flwLocation){
        Location location = new Location("From FLWLocation");
        location.setLatitude(flwLocation.getLatitude());
        location.setLongitude(flwLocation.getLongitude());
        return location;
    }

    private int[] sortLocations(){
        int[] location = new int[8];
        for (int i = 0; i < locations.size(); i++){
            switch (locations.get(i).getName()){
                case (R.string.scjohnson):
                    location[0] = i;
                    break;
                case (R.string.wingspread):
                    location[1] = i;
                    break;
                case (R.string.built_homes):
                    location[2] = i;
                    break;
                case (R.string.meeting_house):
                    location[3] = i;
                    break;
                case (R.string.monona_terrace):
                    location[4] = i;
                    break;
                case (R.string.visitor_center):
                    location[5] = i;
                    break;
                case (R.string.valley_school):
                    location[6] = i;
                    break;
                case (R.string.german_warehouse):
                    location[7] = i;
                    break;
            }
        }
        return location;
    }
}