package hu.ait.android.weatherforecast.network;

import hu.ait.android.weatherforecast.data.Coord;
import hu.ait.android.weatherforecast.data.Weather;
import hu.ait.android.weatherforecast.data.WeatherResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("data/2.5/weather")
    Call<WeatherResult> getBase(@Query("q") String name,
                                @Query("units") String units,
                                @Query("appid") String appid
    );
}
