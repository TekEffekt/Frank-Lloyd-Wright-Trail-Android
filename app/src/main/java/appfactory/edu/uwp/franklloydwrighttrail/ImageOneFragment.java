package appfactory.edu.uwp.franklloydwrighttrail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;


public class ImageOneFragment extends Fragment {
    public static ImageView imageOne;
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
        switch(DescriptonActivity.value)
        {
            case "SC Johnson Administration Building and Research Tower":
                imageOne.setImageResource(R.drawable.scj1);

                break;
            case "Wingspread":
                imageOne.setImageResource(R.drawable.wingspread1);

                break;
            case "Monona Terrace":
                imageOne.setImageResource(R.drawable.mononaterrace);

                break;
            case "Meeting House":
                imageOne.setImageResource(R.mipmap.meeting_house);

                break;
            case "FLW Visitor Center":
                imageOne.setImageResource(R.drawable.visitorcenter1);

                break;
            case "German Warehouse":
                imageOne.setImageResource(R.drawable.adgermanwarehouse1);

                break;

            case "Taliesin":

                imageOne.setImageResource(R.drawable.taliesin);
                break;
            case "Wyoming Valley School":

                imageOne.setImageResource(R.drawable.valley_school);
                break;


        }
        return view;
    }








}
