package appfactory.edu.uwp.franklloydwrighttrail;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zstue_000 on 10/31/2016.
 */

public interface DirectionsApi {
    public final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    //json?origin=42.715237,-87.790697&destination=43.33472,-90.384367&waypoints=optimize:true|43.143901,-90.059523|42.784472,-87.771599
    @GET("https://maps.googleapis.com/maps/api/directions/json")
    Call<DirectionsModel> directions(
            @Query("origin") String startLatLong,
            @Query("destination") String endLatLong,
            @Query("waypoints") String middleLatLong
    );

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY)).build()).build();

}

