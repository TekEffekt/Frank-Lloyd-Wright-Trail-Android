package appfactory.edu.uwp.franklloydwrighttrail.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import appfactory.edu.uwp.franklloydwrighttrail.Activities.DescriptonActivity;
import appfactory.edu.uwp.franklloydwrighttrail.R;


public class ImageTwoFragment extends Fragment {
    public static ImageView imageTwo;
    public static TextView credits;

    public ImageTwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_two, container, false);
        imageTwo = (ImageView) view.findViewById(R.id.imageTwo);
        credits = (TextView) view.findViewById(R.id.credits);

        switch(DescriptonActivity.value)
        {
            case "SC Johnson Administration Building and Research Tower":
                imageTwo.setImageResource(R.drawable.scj2);
                credits.setText("Photo: SC Johnson");

                break;
            case "Wingspread":
                imageTwo.setImageResource(R.drawable.wingspread2);

                break;
            case "Monona Terrace":
                imageTwo.setImageResource(R.drawable.mt2);

                break;
            case "First Unitarian Society Meeting House":
                imageTwo.setImageResource(R.drawable.meetinghouse2);

                break;
            case "Taliesin and FLW Visitor Center":
                imageTwo.setImageResource(R.drawable.vc2);

                break;
            case "A.D. German Warehouse":
                imageTwo.setImageResource(R.drawable.gw2);

                break;
            case "American System-Built Homes":

                imageTwo.setImageResource(R.drawable.bh2);
                break;
            case "Wyoming Valley School":

                imageTwo.setImageResource(R.drawable.wvs2);
                break;



        }
        return view;
    }


}
