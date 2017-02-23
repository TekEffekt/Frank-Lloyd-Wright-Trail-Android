package appfactory.edu.uwp.franklloydwrighttrail.Apis;

/**
 * Created by zstue_000 on 10/25/2016.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import appfactory.edu.uwp.franklloydwrighttrail.Apis.Element;

public class Row {
    @SerializedName("elements")
    private List<Element> elements = new ArrayList<Element>();


    /**
     *
     * @return
     * The elements
     */
    public List<Element> getElements() {
        return elements;
    }

    /**
     *
     * @param elements
     * The elements
     */
    public void setElements(List<Element> elements) {
        this.elements = elements;
    }



}
