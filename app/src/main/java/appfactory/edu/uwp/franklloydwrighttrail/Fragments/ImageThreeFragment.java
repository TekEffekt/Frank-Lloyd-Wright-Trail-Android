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


public class ImageThreeFragment extends Fragment {
    public static ImageView imageThree;
    public static TextView credits;

    public ImageThreeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_three, container, false);
        imageThree = (ImageView) view.findViewById(R.id.imageThree);
        credits = (TextView) view.findViewById(R.id.credits);

        switch(DescriptonActivity.value)
        {
            case "SC Johnson Administration Building and Research Tower":
                imageThree.setImageResource(R.drawable.scj3);

                break;
            case "Wingspread":
                imageThree.setImageResource(R.drawable.wingspread3);

                break;
            case "Monona Terrace":
                imageThree.setImageResource(R.drawable.mt3);
                credits.setText("Photo: Sarah Wykhuis");

                break;
            case "Meeting House":
                imageThree.setImageResource(R.drawable.meetinghouse3);

                break;
            case "FLW Visitor Center":
                imageThree.setImageResource(R.drawable.vc3);

                break;
            case "German Warehouse":
                imageThree.setImageResource(R.drawable.gw3);

                break;
            case "American System-Built Homes":

                imageThree.setImageResource(R.drawable.bh3);
                break;
            case "Wyoming Valley School":

                imageThree.setImageResource(R.drawable.wvs3);
                break;



        }
        return view;
    }


}
