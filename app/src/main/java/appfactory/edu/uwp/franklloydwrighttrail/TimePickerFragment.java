package appfactory.edu.uwp.franklloydwrighttrail;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.sql.Time;

/**
 * Created by sterl on 10/13/2016.
 */

public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME =  "appfactory.edu.uwp.franklloydwrighttrail.time";
    private static final String ARG_TIME = "time";
    private TimePicker timePicker;

    public static TimePickerFragment newInstance(Time time) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Time time = (Time) getArguments().getSerializable(ARG_TIME);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);

        timePicker = (TimePicker) view.findViewById(R.id.dialog_time_time_picker);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int hour = timePicker.getCurrentHour();
                                int minute = timePicker.getCurrentMinute();
                                Time time = new Time(hour, minute, 0);
                                sendResult(Activity.RESULT_OK, time);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, Time time) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
