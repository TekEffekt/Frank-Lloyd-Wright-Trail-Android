package appfactory.edu.uwp.franklloydwrighttrail.Apis;

import appfactory.edu.uwp.franklloydwrighttrail.Models.DistanceModel;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zstue_000 on 10/25/2016.
 */

public interface DistanceMatrixApi {
    public final String BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/";
//json?units=imperial&origins={latlong}&destinations={latlong2}
    @GET("https://maps.googleapis.com/maps/api/distancematrix/json")
    Call<DistanceModel> timeDuration(
            @Query("units") String unit,
            @Query("origins") String latlong,
            @Query("destinations") String latlong2
            );

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    //.client(httpClient.addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY)).build())
    public static final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

}
