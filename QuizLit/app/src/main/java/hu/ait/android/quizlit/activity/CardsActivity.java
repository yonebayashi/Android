package hu.ait.android.quizlit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import hu.ait.android.quizlit.R;
import hu.ait.android.quizlit.adapter.CardsAdapter;
import hu.ait.android.quizlit.adapter.DecksAdapter;
import hu.ait.android.quizlit.data.AppDatabase;
import hu.ait.android.quizlit.data.Card;
import hu.ait.android.quizlit.data.Deck;
import hu.ait.android.quizlit.dialog.NewCardDialog;
import hu.ait.android.quizlit.dialog.NewDeckDialog;

public class CardsActivity extends AppCompatActivity implements NewCardDialog.CardHandler {

    public static final String NEW_CARD_DIALOG = "NEW_CARD_DIALOG";
    public static final String KEY_DECK = "KEY_DECK";
    public static final String KEY_EDIT = "KEY_EDIT";
    public static final String EDIT_CARD_DIALOG = "EDIT_CARD_DIALOG";
    private CoordinatorLayout cardsLayout;
    private long deckId;
    private CardsAdapter cardsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showNewCardDialog();
            }
        });

        cardsLayout = findViewById(R.id.cardsLayout);

        Intent resultIntent = getIntent();
        deckId = resultIntent.getLongExtra(DecksAdapter.KEY_CONTENT, 1);

        final RecyclerView recyclerView = findViewById(R.id.recyclerCards);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initCards(recyclerView);
    }

    private void showNewCardDialog() {
        NewCardDialog newCardDialog = new NewCardDialog();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_DECK, deckId);
        newCardDialog.setArguments(bundle);
        newCardDialog.show(getSupportFragmentManager(), NEW_CARD_DIALOG);
    }

    public void showEditCardDialog(Card card) {
        NewCardDialog editDialog = new NewCardDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_EDIT, card);
        editDialog.setArguments(bundle);

        editDialog.show(getSupportFragmentManager(), EDIT_CARD_DIALOG);
    }

    private void initCards(final RecyclerView recyclerView) {
        new Thread() {
            @Override
            public void run() {
                final List<Card> cards = AppDatabase.getAppDatabase(CardsActivity.this).cardDao().getCardsFromDeck(deckId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cardsAdapter = new CardsAdapter(cards, CardsActivity.this);
                        recyclerView.setAdapter(cardsAdapter);
                    }
                });
            }
        }.start();
    }

    @Override
    public void onNewCardCreated(final String question, final boolean answer, final long deckId) {
        new Thread() {
            @Override
            public void run() {
                final Card newCard = new Card(question, answer, deckId);
                long id = AppDatabase.getAppDatabase(CardsActivity.this).cardDao().insert(newCard);
                newCard.setCardId(id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cardsAdapter.addCard(newCard);
                    }
                });
            }
        }.start();
    }

    @Override
    public void onCardUpdated(final Card card) {
        new Thread() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(CardsActivity.this).cardDao().update(card);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cardsAdapter.updateCard(card);
                    }
                });
            }
        }.start();
    }

}
