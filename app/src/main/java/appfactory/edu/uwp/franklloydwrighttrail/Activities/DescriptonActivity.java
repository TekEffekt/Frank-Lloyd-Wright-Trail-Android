package appfactory.edu.uwp.franklloydwrighttrail.Activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import appfactory.edu.uwp.franklloydwrighttrail.Fragments.ImageOneFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.ImageThreeFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Fragments.ImageTwoFragment;
import appfactory.edu.uwp.franklloydwrighttrail.Adapters.ImageViewPagerAdapter;
import appfactory.edu.uwp.franklloydwrighttrail.R;


public class DescriptonActivity extends AppCompatActivity {
protected View view;
    private ViewPager _mViewPager;
    private ImageViewPagerAdapter _adapter;
    private ImageView _btn1, _btn2, _btn3;
    private ImageView fullScreen;
    private View textView;
    private RelativeLayout selection;
    public static String value;

    private Button locationButton;
    private Button scheduleButton;
    private TextView phone;
    private TextView website;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        value = intent.getStringExtra("Title");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_descripton);
        textView = findViewById(R.id.bottom_sheet);
        selection = (RelativeLayout) findViewById(R.id.selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fullScreen = (ImageView) findViewById(R.id.fullscreen);
        setUpView();
        setTab();
        onCircleButtonClick();

        TextView name = (TextView) findViewById(R.id.name);
        TextView built = (TextView) findViewById(R.id.built);
        TextView description = (TextView) findViewById(R.id.description);
        description.setMovementMethod(new ScrollingMovementMethod());
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locationButton = (Button) findViewById(R.id.location_button);
        scheduleButton = (Button) findViewById(R.id.schedule_button);
        phone = (TextView) findViewById(R.id.phone);
        website = (TextView) findViewById(R.id.website);

        switch(value)
        {
            case "SC Johnson Administration Building and Research Tower":
                name.setText(R.string.scjohnson);
                built.setText(R.string.scj_built);
                description.setText(R.string.scj_desc);
                locationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Grab Location
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=42.7152375,-87.7906969");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });
                setupScheduleButton(0);
                website.setMovementMethod(LinkMovementMethod.getInstance());
                website.setText(Html.fromHtml(this.getResources().getString(R.string.scj_website)));
                phone.setMovementMethod(LinkMovementMethod.getInstance());
                phone.setText(Html.fromHtml(this.getResources().getString(R.string.scj_phone)));

                break;
            case "Wingspread":

                name.setText(R.string.wingspread);
                built.setText(R.string.wingspread_built);
                description.setText(R.string.wingspread_desc);
                locationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Grab Location
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=42.784562,-87.771588");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });
                setupScheduleButton(1);
                website.setMovementMethod(LinkMovementMethod.getInstance());
                website.setText(Html.fromHtml(this.getResources().getString(R.string.wingspread_website)));
                phone.setMovementMethod(LinkMovementMethod.getInstance());
                phone.setText(Html.fromHtml(this.getResources().getString(R.string.wingspread_phone)));

                break;
            case "Monona Terrace":

                name.setText(R.string.monona_terrace);
                built.setText(R.string.monona_terrace_built);
                description.setText(R.string.monona_terrace_desc);
                locationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Grab Location
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=43.0717445,-89.3804018");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });
                setupScheduleButton(2);
                website.setMovementMethod(LinkMovementMethod.getInstance());
                website.setText(Html.fromHtml(this.getResources().getString(R.string.monona_website)));
                phone.setMovementMethod(LinkMovementMethod.getInstance());
                phone.setText(Html.fromHtml(this.getResources().getString(R.string.monona_phone)));
                break;
            case "First Unitarian Society Meeting House":

                name.setText(R.string.meeting_house);
                built.setText(R.string.meeting_house_built);
                description.setText(R.string.meeting_house_desc);
                locationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Grab Location
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=43.0757361,-89.4353368");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });
                setupScheduleButton(3);
                website.setMovementMethod(LinkMovementMethod.getInstance());
                website.setText(Html.fromHtml(this.getResources().getString(R.string.meeting_house_website)));
                phone.setMovementMethod(LinkMovementMethod.getInstance());
                phone.setText(Html.fromHtml(this.getResources().getString(R.string.meeting_house_phone)));

                break;
            case "Taliesin and FLW Visitor Center":

                name.setText(R.string.visitor_center);
                built.setText(R.string.visitor_center_sub);
                description.setText(R.string.visitor_center_desc);
                locationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Grab Location
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=43.144128,-90.059512");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });
                setupScheduleButton(4);
                website.setMovementMethod(LinkMovementMethod.getInstance());
                website.setText(Html.fromHtml(this.getResources().getString(R.string.visitor_center_website)));
                phone.setMovementMethod(LinkMovementMethod.getInstance());
                phone.setText(Html.fromHtml(this.getResources().getString(R.string.visitor_center_phone)));

                break;
            case "A.D. German Warehouse":

                name.setText(R.string.german_warehouse);
                built.setText(R.string.german_warehouse_built);
                description.setText(R.string.german_warehouse_desc);
                locationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Grab Location
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=43.3334718,-90.38436739999997");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });
                setupScheduleButton(5);
                website.setMovementMethod(LinkMovementMethod.getInstance());
                website.setText(Html.fromHtml(this.getResources().getString(R.string.german_warehouse_website)));
                phone.setMovementMethod(LinkMovementMethod.getInstance());
                phone.setText(Html.fromHtml(this.getResources().getString(R.string.german_warehouse_phone)));

                break;
            case "Wyoming Valley School":
                name.setText(R.string.valley_school);
                built.setText(R.string.valley_school_built);
                description.setText(R.string.valley_school_desc);
                locationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Grab Location
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=43.119255,-90.114908");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });
                setupScheduleButton(6);
                website.setMovementMethod(LinkMovementMethod.getInstance());
                website.setText(Html.fromHtml(this.getResources().getString(R.string.valley_school_website)));
                phone.setMovementMethod(LinkMovementMethod.getInstance());
                phone.setText(Html.fromHtml(this.getResources().getString(R.string.valley_school_phone)));

                break;
            case "American System-Built Homes":
                name.setText(R.string.built_homes);
                built.setText(R.string.built_homes_built);
                description.setText(R.string.built_homes_desc);
                locationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Grab Location
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=43.010584,-87.948539");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });
                setupScheduleButton(7);
                website.setMovementMethod(LinkMovementMethod.getInstance());
                website.setText(Html.fromHtml(this.getResources().getString(R.string.built_homes_website)));
                phone.setMovementMethod(LinkMovementMethod.getInstance());
                phone.setText(Html.fromHtml(this.getResources().getString(R.string.built_homes_phone)));

                break;
            default:
                description.setText("Other Place");
                getSupportActionBar().setTitle("Other");
                setupScheduleButton(0);
                break;

        }



        _mViewPager.setOnTouchListener(new View.OnTouchListener() {
            private float pointX;
            private float pointY;
            private int tolerance = 50;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:return false;
                    case MotionEvent.ACTION_DOWN:
                        pointX = event.getX();
                        pointY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        boolean sameX = pointX + tolerance > event.getX() && pointX - tolerance < event.getX();
                        boolean sameY = pointY + tolerance > event.getY() && pointY - tolerance < event.getY();
                        if(sameX && sameY)
                        {
                            int pos = _mViewPager.getCurrentItem();
                            switch (pos)
                            {
                                case 0: fullScreen.setImageDrawable(ImageOneFragment.imageOne.getDrawable());
                                    break;
                                case 1: fullScreen.setImageDrawable(ImageTwoFragment.imageTwo.getDrawable());
                                    break;
                                case 2: fullScreen.setImageDrawable(ImageThreeFragment.imageThree.getDrawable());
                            }


                            fullScreen.setVisibility(View.VISIBLE);
                            selection.setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                            _mViewPager.setVisibility(View.GONE);
                        }
                }
                return false;
            }
        });



        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener(){
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout1, int verticalOffset){
                if(scrollRange == -1){
                    scrollRange = appBarLayout1.getTotalScrollRange();
                }
                if(scrollRange + verticalOffset ==0){
                    collapsingToolbarLayout.setTitle("Frank Lloyd Wright Trail");
                    isShow = true;
                }
                else if(isShow)
                {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setUpView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        _mViewPager = null;
        _adapter = null;
    }

    @Override
    public void onBackPressed(){
        if(fullScreen.getVisibility()== View.VISIBLE){
            fullScreen.setImageDrawable(null);
            fullScreen.setVisibility(View.GONE);
            selection.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            _mViewPager.setVisibility(View.VISIBLE);
        }else{
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(fullScreen.getVisibility()== View.VISIBLE){
            fullScreen.setVisibility(View.GONE);
            fullScreen.setImageDrawable(null);
            selection.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            _mViewPager.setVisibility(View.VISIBLE);
            return true;
        }else{
            super.onBackPressed();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            this.finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void setupScheduleButton(final int position){
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";
                switch (position){
                    case 0:
                        url = getResources().getString(R.string.scj_tourD_website);
                        break;
                    case 1:
                        url = getResources().getString(R.string.wingspread_tourD_website);
                        break;
                    case 2:
                        url = getResources().getString(R.string.monona_tourD_website);
                        break;
                    case 3:
                        url = getResources().getString(R.string.meeting_house_tourD_website);
                        break;
                    case 4:
                        url = getResources().getString(R.string.visitor_center_tourD_website);
                        break;
                    case 5:
                        url = getResources().getString(R.string.german_warehouse_tourD_website);
                        break;
                    case 6:
                        url = getResources().getString(R.string.valley_school_tourD_website);
                        break;
                    case 7:
                        url = getResources().getString(R.string.built_homes_tourD_website);
                        break;
                    default:
                        break;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private void onCircleButtonClick()
    {
        _btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                _btn1.setImageResource(R.drawable.selecteditem_dot);
                _mViewPager.setCurrentItem(0);
            }
        });
        _btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                _btn2.setImageResource(R.drawable.selecteditem_dot);
                _mViewPager.setCurrentItem(1);
            }
        });
        _btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                _btn3.setImageResource(R.drawable.selecteditem_dot);
                _mViewPager.setCurrentItem(2);
            }
        });

    }

    private void setUpView()
    {
        _mViewPager = (ViewPager) findViewById(R.id.imageviewPager);
        _adapter = new ImageViewPagerAdapter(getSupportFragmentManager());
        _mViewPager.setAdapter(_adapter);
        _mViewPager.setCurrentItem(0);

        initButton();


    }
    private void setTab()
    {
        _mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrollStateChanged(int position){}
            @Override
            public void onPageScrolled(int arg0,float arg1, int arg2){}
            @Override
            public void onPageSelected(int position)
            {
                _btn1.setImageResource(R.drawable.nonselecteditem_dot);
                _btn2.setImageResource(R.drawable.nonselecteditem_dot);
                _btn3.setImageResource(R.drawable.nonselecteditem_dot);

                btnAction(position);
            }
        });
    }

    private void btnAction(int action)
    {
        switch (action)
        {
            case 0: _btn1.setImageResource(R.drawable.selecteditem_dot);
                break;
            case 1: _btn2.setImageResource(R.drawable.selecteditem_dot);
                break;
            case 2: _btn3.setImageResource(R.drawable.selecteditem_dot);
                break;
        }
    }

    private void initButton()
    {
        _btn1 = (ImageView) findViewById(R.id.btn1);
        _btn1.setImageResource(R.drawable.selecteditem_dot);
        _btn2 = (ImageView) findViewById(R.id.btn2);
        _btn3 = (ImageView) findViewById(R.id.btn3);
    }


}
