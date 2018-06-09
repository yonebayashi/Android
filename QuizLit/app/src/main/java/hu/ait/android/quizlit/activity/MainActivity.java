package hu.ait.android.quizlit.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import hu.ait.android.quizlit.R;
import hu.ait.android.quizlit.adapter.DecksAdapter;
import hu.ait.android.quizlit.data.AppDatabase;
import hu.ait.android.quizlit.data.Deck;
import hu.ait.android.quizlit.dialog.NewDeckDialog;
import hu.ait.android.quizlit.receiver.AlarmReceiver;

public class MainActivity extends AppCompatActivity implements NewDeckDialog.DeckHandler {

    public static final String NEW_DECK_DIALOG = "NewDeckDialog";
    public static final String KEY_EDIT = "KEY_EDIT";
    public static final String EDIT_DECK_DIALOG = "EDIT_DECK_DIALOG";
    private DecksAdapter decksAdapter;
    private int score;
    private TextView tvScore;

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
               showNewDeckDialog();
            }
        });

        Intent scoreIntent = getIntent();
        score = scoreIntent.getIntExtra(ResultActivity.KEY_SCORE, 0);
        tvScore = findViewById(R.id.tvScore);
        tvScore.setText("Latest score: " + score + "%");

        final RecyclerView recyclerView = findViewById(R.id.recyclerDeck);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

       initDecks(recyclerView);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.SECOND, 5);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }

    private void initDecks(final RecyclerView recyclerView) {
        new Thread() {
            @Override
            public void run() {
            final List<Deck> decks = AppDatabase.getAppDatabase(MainActivity.this).deckDao().getAll();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    decksAdapter = new DecksAdapter(decks, MainActivity.this);
                    recyclerView.setAdapter(decksAdapter);
                }
            });
            }
        }.start();
    }

    private void showNewDeckDialog() {
        new NewDeckDialog().show(getSupportFragmentManager(), NEW_DECK_DIALOG);
    }

    public void showEditDeckDialog(Deck deck) {
        NewDeckDialog editDialog = new NewDeckDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_EDIT, deck);
        editDialog.setArguments(bundle);

        editDialog.show(getSupportFragmentManager(), EDIT_DECK_DIALOG);
    }

    @Override
    public void onNewDeckCreated(final String title) {
        new Thread() {
            @Override
            public void run() {
            final Deck newDeck = new Deck(title);
            long id = AppDatabase.getAppDatabase(MainActivity.this).deckDao().insert(newDeck);
            newDeck.setId(id);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    decksAdapter.addDeck(newDeck);
                }
            });
            }
        }.start();
    }

    @Override
    public void onDeckUpdated(final Deck deck) {
        new Thread() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(MainActivity.this).deckDao().update(deck);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        decksAdapter.updateDeck(deck);
                    }
                });
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (decksAdapter != null){
            decksAdapter.notifyDataSetChanged();
        }
    }
}
