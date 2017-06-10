package appfactory.edu.uwp.franklloydwrighttrail.Apis;

/**
 * Created by zstue_000 on 10/31/2016.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeocodedWaypoint {

    private String geocoderStatus;
    private String placeId;
    private List<String> types = new ArrayList<String>();


    /**
     *
     * @return
     * The geocoderStatus
     */
    public String getGeocoderStatus() {
        return geocoderStatus;
    }

    /**
     *
     * @param geocoderStatus
     * The geocoder_status
     */
    public void setGeocoderStatus(String geocoderStatus) {
        this.geocoderStatus = geocoderStatus;
    }

    /**
     *
     * @return
     * The placeId
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     *
     * @param placeId
     * The place_id
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     *
     * @return
     * The types
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     *
     * @param types
     * The types
     */
    public void setTypes(List<String> types) {
        this.types = types;
    }


    @Override
    public String toString() {
        return "GeocodedWaypoint{" +
                "geocoderStatus='" + geocoderStatus + '\'' +
                ", placeId='" + placeId + '\'' +
                ", types=" + types +
                '}';
    }
}
