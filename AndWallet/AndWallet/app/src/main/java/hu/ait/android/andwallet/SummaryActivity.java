package hu.ait.android.andwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;


public class SummaryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        Integer newIncomeAmount = Integer.parseInt(intent.getStringExtra(HomeActivity.UPDATE_INCOME_AMOUNT));
        Integer newExpenseAmount = Integer.parseInt(intent.getStringExtra(HomeActivity.UPDATE_EXPENSE_AMOUNT));

        handleUpdateIncome(newIncomeAmount);
        handleUpdateExpense(newExpenseAmount);
        handleUpdateBalance(newIncomeAmount - newExpenseAmount);
    }

    public void handleUpdateIncome(int amount) {
        TextView tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvTotalIncome.setText(String.valueOf(amount));
    }

    public void handleUpdateExpense(int amount) {
        TextView tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvTotalExpense.setText(String.valueOf(amount));
    }

    public void handleUpdateBalance(int amount) {
        TextView tvBalance = findViewById(R.id.tvBalance);
        tvBalance.setText(String.valueOf(amount));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_home:
                finish();
                break;
            case R.id.action_summary:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}