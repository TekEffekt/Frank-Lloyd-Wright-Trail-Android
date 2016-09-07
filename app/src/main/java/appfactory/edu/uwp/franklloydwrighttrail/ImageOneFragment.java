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
                imageOne.setImageResource(R.mipmap.scjohnson);

                break;
            case "Wingspread":
                imageOne.setImageResource(R.mipmap.wingspread);

                break;
            case "MononaTerrace":
                imageOne.setImageResource(R.mipmap.monona_terrace);

                break;
            case "Meeting House":
                imageOne.setImageResource(R.mipmap.meeting_house);

                break;
            case "FLW Visitor Center":
                imageOne.setImageResource(R.mipmap.visitor_center);

                break;
            case "German Warehouse":
                imageOne.setImageResource(R.mipmap.german_warehouse);

                break;



        }
        return view;
    }








}
