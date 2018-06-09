package hu.ait.android.quizlit.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hu.ait.android.quizlit.R;
import hu.ait.android.quizlit.adapter.DecksAdapter;
import hu.ait.android.quizlit.data.AppDatabase;
import hu.ait.android.quizlit.data.Card;


public class QuizActivity extends AppCompatActivity {

    public static final String KEY_SCORE = "KEY_SCORE";
    private static final String KEY_INDEX = "index";
    private CoordinatorLayout quizLayout;
    private long deckId;

    private TextView tvQuestion;
    private Button btnTrue;
    private Button btnFalse;
    private List<Card> cards;

    private int currentIndex = 0;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        setContentView(R.layout.activity_quiz);
        quizLayout = findViewById(R.id.quizLayout);

        Intent resultIntent = getIntent();
        deckId = resultIntent.getLongExtra(DecksAdapter.KEY_CONTENT, 1);

        tvQuestion = findViewById(R.id.tvQuestion);

        initCards();

        btnTrue = findViewById(R.id.btnTrue);
        btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                if (currentIndex < cards.size() - 1) {
                    currentIndex += 1;
                    updateQuestion();
                } else {
                    showQuizResult();
                }
            }
        });

        btnFalse = findViewById(R.id.btnFalse);
        btnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                if (currentIndex < cards.size() - 1) {
                    currentIndex += 1;
                    updateQuestion();
                } else {
                    showQuizResult();
                }
            }
        });
    }

    private void initCards() {
        new Thread() {
            @Override
            public void run() {
                cards = AppDatabase.getAppDatabase(QuizActivity.this).cardDao().getCardsFromDeck(deckId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateQuestion();
                    }
                });
            }
        }.start();
    }

    private void showQuizResult() {
        // delay 1 second before starting the new activity
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra(KEY_SCORE, (score * 100) / cards.size());
                finish();
                startActivity(intent);
            }
        }, 1000);
    }

    private void updateQuestion() {
        String question = cards.get(currentIndex).getQuestion();
        tvQuestion.setText(question);
    }


    private void checkAnswer(boolean userInput) {
        boolean answer = cards.get(currentIndex).getAnswer();
        if (userInput == answer) {
            showSnackBarMessage(getString(R.string.msg_correct));
            score += 1;
        } else {
            showSnackBarMessage(getString(R.string.msg_incorrect));
        }
    }

    public void showSnackBarMessage(String message) {
        Snackbar.make(
                quizLayout,
                message,
                Snackbar.LENGTH_SHORT
        ).setAction("action hide", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        }).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, currentIndex);
    }

}
