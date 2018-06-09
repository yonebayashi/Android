package hu.ait.android.weatherforecast.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hu.ait.android.weatherforecast.R;
import hu.ait.android.weatherforecast.WeatherDetailsActivity;
import hu.ait.android.weatherforecast.data.AppDatabase;
import hu.ait.android.weatherforecast.data.City;

public class CitiesAdapter
        extends RecyclerView.Adapter <CitiesAdapter.ViewHolder> {

    public static final String KEY_CONTENT = "KEY_CONTENT";
    private List<City> cityList;
    private Context context;

    public CitiesAdapter(List<City> cities, Context context) {
        cityList = cities;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCityName;
        public Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRow = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.city_row, parent, false);
        return new ViewHolder(viewRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder vh, int position) {
        vh.tvCityName.setText(cityList.get(position).getCityName());
        vh.tvCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = vh.tvCityName.getText().toString();

                Intent intent = new Intent(context, WeatherDetailsActivity.class);
                intent.putExtra(KEY_CONTENT, content);
                context.startActivity(intent);
            }
        });

        vh.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCity(vh.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public void addCity(City city) {
        cityList.add(city);
        notifyDataSetChanged();
    }

    public void removeCity(int position) {
       final City cityToDelete = cityList.get(position);
       cityList.remove(cityToDelete);
       notifyItemRemoved(position);

        new Thread() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(context).cityDao().delete(cityToDelete);
            }
        }.start();
    }

    public void updateCity(City city) {
        int index = findIndexOf(city.getCityId());
        cityList.set(index, city);
        notifyItemChanged(index);
    }

    private int findIndexOf(long cityId) {
        for (int i = 0; i < cityList.size(); i++) {
            if (cityList.get(i).getCityId() == cityId) {
                return i;
            }
        }
        return -1;
    }
}
