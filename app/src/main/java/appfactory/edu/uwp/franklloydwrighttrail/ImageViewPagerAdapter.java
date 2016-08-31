package appfactory.edu.uwp.franklloydwrighttrail;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by zstue_000 on 8/16/2016.
 */
public class ImageViewPagerAdapter extends FragmentPagerAdapter{

    public static int totalPage = 3;
    ImageOneFragment iof = new ImageOneFragment();
    ImageTwoFragment itf = new ImageTwoFragment();
    ImageThreeFragment ithf = new ImageThreeFragment();

    public ImageViewPagerAdapter( FragmentManager fm)
    {
        super(fm);


    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment f = new Fragment();
        switch (position)
        {
            case 0: f= iof;
                break;
            case 1: f= itf;
                break;
            case 2: f= ithf;
                break;
        }
        return f;
    }
@Override
    public int getCount()
{
    return totalPage;
}

    @Override
    public int getItemPosition(Object item)
    {
        return POSITION_NONE;
    }
}
