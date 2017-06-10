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


public class ImageOneFragment extends Fragment {
    public static ImageView imageOne;
    public static TextView credits;

    public ImageOneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image_one, container, false);
        imageOne = (ImageView) view.findViewById(R.id.imageOne);
        credits = (TextView) view.findViewById(R.id.credits);

        switch(DescriptonActivity.value)
        {
            case "SC Johnson Administration Building and Research Tower":
                imageOne.setImageResource(R.drawable.scj1);
                credits.setText("Photo: SC Johnson");

                break;
            case "Wingspread":
                imageOne.setImageResource(R.drawable.wingspread1);

                break;
            case "Monona Terrace":
                imageOne.setImageResource(R.drawable.mt1);

                break;
            case "First Unitarian Society Meeting House":
                imageOne.setImageResource(R.mipmap.meeting_house);

                break;
            case "Taliesin and FLW Visitor Center":
                imageOne.setImageResource(R.drawable.vc1);

                break;
            case "A.D. German Warehouse":
                imageOne.setImageResource(R.drawable.gw1);

                break;

            case "American System-Built Homes":
                imageOne.setImageResource(R.drawable.bh1);
                break;
            case "Wyoming Valley School":

                imageOne.setImageResource(R.drawable.wvs1);
                break;


        }
        return view;
    }








}
