package appfactory.edu.uwp.franklloydwrighttrail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DescriptonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripton);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String value = intent.getStringExtra("Title");
        ImageView picture = (ImageView) findViewById(R.id.picture);
        TextView description = (TextView) findViewById(R.id.description);
        switch(value)
        {
            case "SC Johnson Administration Building and Research Tower":
                picture.setImageResource(R.mipmap.scjohnson);
                getSupportActionBar().setTitle("SC Johnson Administration Building and Research Tower");
                break;
            case "Wingspread":
                picture.setImageResource(R.mipmap.wingspread);
                getSupportActionBar().setTitle("Wingspread");
                break;
            case "MononaTerrace":
                picture.setImageResource(R.mipmap.monona_terrace);
                getSupportActionBar().setTitle("Monona Terrace");
                break;
            case "Meeting House":
                picture.setImageResource(R.mipmap.meeting_house);
                getSupportActionBar().setTitle("Meeting House");
                break;
            case "FLW Visitor Center":
                picture.setImageResource(R.mipmap.visitor_center);
                getSupportActionBar().setTitle("FLW Visitor Center");
                break;
            case "German Warehouse":
                picture.setImageResource(R.mipmap.german_warehouse);
                getSupportActionBar().setTitle("A.D. German Warehouse");
                break;
            default:
                description.setText("Other Place");
                getSupportActionBar().setTitle("Other");

        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
