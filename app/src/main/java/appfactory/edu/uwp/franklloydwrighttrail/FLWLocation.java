package appfactory.edu.uwp.franklloydwrighttrail;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class FLWLocation {
    @StringRes private int name;
    @DrawableRes private int image;
    @DrawableRes private int markerColor;

    public FLWLocation(@StringRes int name, @DrawableRes int image, @DrawableRes int markerColor) {
        this.name = name;
        this.image = image;
        this.markerColor = markerColor;
    }

    public int getImage() { return image; }

    public void setImage(int image) { this.image = image; }

    public int getName() { return name; }

    public void setName(int name) { this.name = name; }

    public int getMarkerColor() { return markerColor; }

    public void setMarkerColor(int markerColor) { this.markerColor = markerColor; }
}
