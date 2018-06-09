package hu.ait.android.quizlit.activity;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hu.ait.android.quizlit.R;
import hu.ait.android.quizlit.adapter.DecksAdapter;
import hu.ait.android.quizlit.data.AppDatabase;
import hu.ait.android.quizlit.data.Card;


public class CardFlipActivity extends AppCompatActivity {

    public static final String KEY_FRONT = "KEY_FRONT";
    public static final String KEY_BACK = "KEY_BACK";
    private boolean showBack=false;
    private long deckId;
    private List<Card> cards;
    private Button btnNext;
    private Button btnPrev;
    private CardFrontFragment cardFrontFragment;
    private CardBackFragment cardBackFragment;

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_flip);

        Intent resultIntent = getIntent();
        deckId = resultIntent.getLongExtra(DecksAdapter.KEY_CONTENT, 1);

        initCards();

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % cards.size();
                updateQuestion();
                updateAnswer();
                showBack=false;
            }
        });

        btnPrev = findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex <= 0) {
                    currentIndex = cards.size() - 1;
                } else {
                    currentIndex = (currentIndex - 1) % cards.size();
                }
                updateQuestion();
                updateAnswer();
                showBack=false;
            }
        });

    }

    private void initCards() {
        new Thread() {
            @Override
            public void run() {
                cards = AppDatabase.getAppDatabase(CardFlipActivity.this).cardDao().getCardsFromDeck(deckId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateQuestion();
                        updateAnswer();
                    }
                });
            }
        }.start();
    }


    private void updateQuestion() {
        String question = cards.get(currentIndex).getQuestion();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_FRONT, question);

        cardFrontFragment = new CardFrontFragment();
        cardFrontFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, cardFrontFragment)
                .commit();
    }

    private void updateAnswer() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_BACK, cards.get(currentIndex).getAnswer());

        cardBackFragment = new CardBackFragment();
        cardBackFragment.setArguments(bundle);
    }

    public void flipCard(View view) {
        if(showBack){
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.card_flip_right_in,R.animator.card_flip_right_out)
                    .replace(R.id.container, cardFrontFragment)
                    .commit();
            showBack=false;
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.card_flip_right_in,R.animator.card_flip_right_out)
                    .replace(R.id.container, cardBackFragment)
                    .commit();
            showBack=true;
        }
    }

    public static class CardFrontFragment extends Fragment {
        String question;

        public CardFrontFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_card_front, container, false);

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                question = bundle.getString(KEY_FRONT, "");
            }

            TextView tvCardFront = (TextView) rootView.findViewById(R.id.tvCardFront);
            tvCardFront.setText(question);
            return rootView;
        }
    }

    public static class CardBackFragment extends Fragment {
        boolean answer;

        public CardBackFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView =  inflater.inflate(R.layout.fragment_card_back, container, false);

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                answer = bundle.getBoolean(KEY_BACK, true);
            }

            TextView tvCardBack = (TextView) rootView.findViewById(R.id.tvCardBack);
            tvCardBack.setText(""+answer);
            return rootView;
        }
    }
}
