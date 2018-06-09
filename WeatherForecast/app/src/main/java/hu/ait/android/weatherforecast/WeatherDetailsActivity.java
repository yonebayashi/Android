package hu.ait.android.weatherforecast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import hu.ait.android.weatherforecast.adapter.CitiesAdapter;
import hu.ait.android.weatherforecast.data.WeatherResult;
import hu.ait.android.weatherforecast.network.WeatherAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherDetailsActivity extends AppCompatActivity {

    private final String URL_BASE = "http://api.openweathermap.org/";
    private WeatherAPI weatherAPI;

    private ImageView ivIcon;
    private TextView tvTemp;
    private TextView tvHumidity;
    private TextView tvPressure;
    private TextView tvTempMax;
    private TextView tvTempMin;
    private TextView tvDesc;
    private TextView tvSunrise;
    private TextView tvSunset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherAPI = retrofit.create(WeatherAPI.class);

        ivIcon = findViewById(R.id.ivIcon);
        tvTemp = findViewById(R.id.tvTemp);
        tvPressure = findViewById(R.id.tvPressure);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvTempMax = findViewById(R.id.tvTempMax);
        tvTempMin = findViewById(R.id.tvTempMin);
        tvDesc = findViewById(R.id.tvDesc);
        tvSunrise = findViewById(R.id.tvSunrise);
        tvSunset = findViewById(R.id.tvSunset);


        Intent resultIntent = getIntent();
        String cityName = resultIntent.getStringExtra(CitiesAdapter.KEY_CONTENT);

        Call<WeatherResult> callBase = weatherAPI.getBase(
                cityName,
                getString(R.string.UNIT_METRIC),
                getString(R.string.API_KEY));

        callBase.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                String icon = response.body().getWeather().get(0).getIcon();
                String iconURL = "http://openweathermap.org/img/w/" + icon + ".png";

                String temp = "Temperature: " + response.body().getMain().getTemp();
                String pressure = "Pressure: " + response.body().getMain().getPressure();
                String humidity = "Humidity: " + response.body().getMain().getHumidity();
                String tempMax = "Max temperature: " + response.body().getMain().getTempMax();
                String tempMin = "Min temperature: " + response.body().getMain().getTempMin();
                String desc = "Description: " + response.body().getWeather().get(0).getDescription();
                String sunrise = "Sunrise: " + response.body().getSys().getSunrise();
                String sunset = "Sunset: " + response.body().getSys().getSunset();

                Glide.with(WeatherDetailsActivity.this).load(iconURL).into(ivIcon);
                tvTemp.setText(temp);
                tvPressure.setText(pressure);
                tvHumidity.setText(humidity);
                tvTempMax.setText(tempMax);
                tvTempMin.setText(tempMin);
                tvDesc.setText(desc);
                tvSunrise.setText(sunrise);
                tvSunset.setText(sunset);
            }

            @Override
            public void onFailure(Call<WeatherResult> call, Throwable t) {
                Toast.makeText(WeatherDetailsActivity.this,
                        "Error " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
