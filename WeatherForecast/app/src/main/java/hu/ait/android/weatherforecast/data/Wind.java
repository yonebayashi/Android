package hu.ait.android.weatherforecast.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    @Expose
    public Double speed;
    @SerializedName("deg")
    @Expose
    public Double deg;
}
