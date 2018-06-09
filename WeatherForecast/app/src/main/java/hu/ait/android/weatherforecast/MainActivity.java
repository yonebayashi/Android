package hu.ait.android.weatherforecast;

import android.arch.persistence.room.Database;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hu.ait.android.weatherforecast.adapter.CitiesAdapter;
import hu.ait.android.weatherforecast.data.AppDatabase;
import hu.ait.android.weatherforecast.data.City;
import hu.ait.android.weatherforecast.data.NewCityDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewCityDialog.CityHandler {

    public static final String ADD_CITY_DIALOG = "addCityDialog";
    private CitiesAdapter citiesAdapter;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCityDialog();
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final RecyclerView recyclerView = findViewById(R.id.recyclerList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initCities(recyclerView);
    }

    private void initCities(final RecyclerView recyclerView) {
        new Thread() {
            @Override
            public void run() {
                // get all cities
                final List<City> cities = AppDatabase.getAppDatabase(MainActivity.this).cityDao().getAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        citiesAdapter = new CitiesAdapter(cities, MainActivity.this);
                        recyclerView.setAdapter(citiesAdapter);
                    }
                });
            }
        }.start();
    }

    private void showAddCityDialog() {
        new NewCityDialog().show(getSupportFragmentManager(), ADD_CITY_DIALOG);
    }

    public void showSnackBarMessage(String message) {
        Snackbar.make(
                drawerLayout,
                message,
                Snackbar.LENGTH_LONG
        ).setAction("action hide", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        }).show();
    }

    @Override
    public void onNewCityCreated(final City city) {
        new Thread() {
            @Override
            public void run() {
                long id = AppDatabase.getAppDatabase(MainActivity.this)
                        .cityDao().insert(city);
                city.setCityId(id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        citiesAdapter.addCity(city);
                    }
                });
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            showAddCityDialog();
        } else if (id == R.id.nav_about) {
            showSnackBarMessage(getString(R.string.msg_author));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;    }
}
