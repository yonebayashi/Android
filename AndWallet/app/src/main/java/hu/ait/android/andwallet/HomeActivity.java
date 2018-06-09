package hu.ait.android.andwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    public static final String UPDATE_INCOME_AMOUNT = "UPDATE_INCOME_AMOUNT";
    public static final String UPDATE_EXPENSE_AMOUNT = "UPDATE_EXPENSE_AMOUNT";

    private int totalIncomeCounter = 0;
    private int totalExpenseCounter = 0;

    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tbIncome)
    ToggleButton tbIncome;
    @BindView(R.id.layoutContent)
    LinearLayout layoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSave)
    public void saveButtonClicked() {
        // inflate one item row
        final View itemRow = getLayoutInflater().inflate(R.layout.item_row, null, false);

        // set the content of the item row
        TextView tvItemTitle = itemRow.findViewById(R.id.tvItemTitle);
        String strItemTitle = etTitle.getText().toString();
        if (strItemTitle.isEmpty()) {
           etTitle.setError(getString(R.string.ERROR_MESSAGE));
           return;
        }
        tvItemTitle.setText(strItemTitle);

        TextView tvItemAmount = itemRow.findViewById(R.id.tvItemAmount);
        String strItemAmount = etAmount.getText().toString();
        if (strItemAmount.isEmpty()) {
            etAmount.setError(getString(R.string.ERROR_MESSAGE));
            return;
        }
        tvItemAmount.setText(strItemAmount);

        ImageView ivIcon = itemRow.findViewById(R.id.ivIcon);

        if (tbIncome.isChecked()) {
            ivIcon.setImageResource(R.drawable.income);
            totalIncomeCounter += Integer.parseInt(strItemAmount);
        } else {
            ivIcon.setImageResource(R.drawable.expense);
            totalExpenseCounter += Integer.parseInt(strItemAmount);
        }

        // add the item row to the main layout
        layoutContent.addView(itemRow);

        // update balance
        TextView tvShowBalance = findViewById(R.id.tvShowBalance);
        tvShowBalance.setText(String.valueOf(totalIncomeCounter - totalExpenseCounter));

        // reset input fields after the item is added
        etTitle.setText("");
        etAmount.setText("");
    }

    @OnClick(R.id.btnClearAll)
    public void clearAllButtonClicked() {
        layoutContent.removeAllViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_home:
                break;
            case R.id.action_summary:
                Intent intent = new Intent(HomeActivity.this, SummaryActivity.class);
                intent.putExtra(UPDATE_INCOME_AMOUNT, String.valueOf(totalIncomeCounter));
                intent.putExtra(UPDATE_EXPENSE_AMOUNT, String.valueOf(totalExpenseCounter));

                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
