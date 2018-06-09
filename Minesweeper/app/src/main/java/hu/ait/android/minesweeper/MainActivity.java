package hu.ait.android.minesweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import hu.ait.android.minesweeper.model.MinesweeperModel;
import hu.ait.android.minesweeper.ui.MinesweeperView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MinesweeperView minesweeperView = findViewById(R.id.minesweeper);
        Button btnClear = findViewById(R.id.btnClear);
        ToggleButton btnToggle = findViewById(R.id.btnToggle);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minesweeperView.clearBoard();
            }
        });
        btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MinesweeperModel.flag = 1;
                } else {
                    MinesweeperModel.flag = 0;
                }
            }
        });
        minesweeperView.clearBoard();
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
