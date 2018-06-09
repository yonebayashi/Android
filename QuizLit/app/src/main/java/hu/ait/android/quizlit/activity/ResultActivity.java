package hu.ait.android.quizlit.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import hu.ait.android.quizlit.R;

public class ResultActivity extends AppCompatActivity {

    public static final String KEY_SCORE = "KEY_SCORE";
    private int score;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent resultIntent = getIntent();
        score = resultIntent.getIntExtra(QuizActivity.KEY_SCORE, 0);

        tvResult = findViewById(R.id.tvResult);
        tvResult.setText("Your score: " + score + "%");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.putExtra(KEY_SCORE, score);
        startActivity(intent);
        finish();
    }
}
