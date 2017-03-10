package appfactory.edu.uwp.franklloydwrighttrail.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;

import appfactory.edu.uwp.franklloydwrighttrail.R;
import appfactory.edu.uwp.franklloydwrighttrail.RealmController;
import io.realm.Realm;

/**
 * Created by sterl on 10/28/2016.
 */

public class TripPlannerOptionsFragment extends Fragment {
    private Button cont;

    private Switch breakfastSwitch;
    private Switch lunchSwitch;
    private Switch dinnerSwitch;

    private RelativeLayout breakfastContainer;
    private RelativeLayout lunchContainer;
    private RelativeLayout dinnerContainer;

    private boolean yesBreakfast = false;
    private boolean yesLunch = false;
    private boolean yesDinner = false;

    private boolean complete = true;

    private EditText timeBreakfast;
    private EditText timeLunch;
    private EditText timeDinner;

    private Realm realm;

    public static TripPlannerOptionsFragment newInstance(){
        TripPlannerOptionsFragment tripPlannerOptionsFragment = new TripPlannerOptionsFragment();
        return tripPlannerOptionsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_trip_options, container, false);
        initializeViews(view);
        realm = RealmController.getInstance().getRealm();
        initializeSwitches();
        return view;
    }

    private void mealCheck(){
        if (yesBreakfast) {
            if (timeBreakfast.getText().toString().equals("") || Integer.parseInt(timeBreakfast.getText().toString()) > 121) {
                complete = false;
            } else {
                realm.beginTransaction();
                RealmController.getInstance().getTrip().setBreakfastTime(Integer.parseInt(timeBreakfast.getText().toString()));
                realm.commitTransaction();
                complete = true;
            }
        } else {
            complete = true;
        }
        if (yesLunch) {
            if (timeLunch.getText().toString().equals("") || Integer.parseInt(timeLunch.getText().toString()) > 121) {
                complete = false;
            } else {
                realm.beginTransaction();
                RealmController.getInstance().getTrip().setLunchTime(Integer.parseInt(timeLunch.getText().toString()));
                realm.commitTransaction();
                complete = true;
            }
        } else {
            complete = true;
        }
        if (yesDinner) {
            if (timeDinner.getText().toString().equals("") || Integer.parseInt(timeDinner.getText().toString()) > 121) {
                complete = false;
            } else {
                realm.beginTransaction();
                RealmController.getInstance().getTrip().setDinnerTime(Integer.parseInt(timeDinner.getText().toString()));
                realm.commitTransaction();
                complete = true;
            }
        } else {
            complete = true;
        }
    }

    /*
        Switches the appr
     */
    private boolean mealToggle(RelativeLayout mealContainer, Boolean yesMeal) {
        if (yesMeal) { //turn off
            mealContainer.setVisibility(View.GONE);
            return false;
        } else { //turn on
            mealContainer.setVisibility(View.VISIBLE);
            return true;
        }
    }

    // Easy Clump of all the used views
    private void initializeViews(View view){
        breakfastSwitch = (Switch) view.findViewById(R.id.breakfast_switch);
        lunchSwitch = (Switch) view.findViewById(R.id.lunch_switch);
        dinnerSwitch = (Switch) view.findViewById(R.id.dinner_switch);

        breakfastContainer = (RelativeLayout) view.findViewById(R.id.length_breakfast_container);
        lunchContainer = (RelativeLayout) view.findViewById(R.id.length_lunch_container);
        dinnerContainer = (RelativeLayout) view.findViewById(R.id.length_dinner_container);

        timeBreakfast = (EditText) view.findViewById(R.id.length_time_breakfast);
        timeLunch = (EditText) view.findViewById(R.id.length_time_lunch);
        timeDinner = (EditText) view.findViewById(R.id.length_time_dinner);
    }

    private void initializeSwitches(){
        breakfastSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                yesBreakfast = mealToggle(breakfastContainer,yesBreakfast);
                realm.beginTransaction();
                RealmController.getInstance().getTrip().setBreakfastTime(0);
                realm.commitTransaction();
            }
        });

        lunchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                yesLunch = mealToggle(lunchContainer,yesLunch);
                realm.beginTransaction();
                RealmController.getInstance().getTrip().setLunchTime(0);
                realm.commitTransaction();
            }
        });

        dinnerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                yesDinner = mealToggle(dinnerContainer,yesDinner);
                realm.beginTransaction();
                RealmController.getInstance().getTrip().setDinnerTime(0);
                realm.commitTransaction();
            }
        });
    }

/*
    private void initializeContainers(){
        breakfastContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        realm.beginTransaction();
                        RealmController.getInstance().getTrip().setBreakfastTime(2);
                        realm.commitTransaction();
                        complete = true;
            }
        });

        lunchContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        realm.beginTransaction();
                        RealmController.getInstance().getTrip().setLunchTime(2);
                        realm.commitTransaction();
                        complete = true;
            }
        });

        dinnerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        //Set time here
                        realm.beginTransaction();
                        RealmController.getInstance().getTrip().setDinnerTime(2);
                        realm.commitTransaction();
                        complete = true;
            }
        });

    }
    */
}
