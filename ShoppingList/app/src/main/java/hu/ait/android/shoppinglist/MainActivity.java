package hu.ait.android.shoppinglist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import hu.ait.android.shoppinglist.adapter.ShoppingListAdapter;
import hu.ait.android.shoppinglist.data.AppDatabase;
import hu.ait.android.shoppinglist.data.Item;
import hu.ait.android.shoppinglist.data.NewItemDialog;
import hu.ait.android.shoppinglist.touch.ShoppingItemTouchHelperCallback;


public class MainActivity extends AppCompatActivity implements NewItemDialog.ItemHandler {

    public static final String KEY_EDIT = "KEY_EDIT";
    public static final String CREATE_ITEM_DIALOG = "CreateItemDialog";
    public static final String EDIT_DIALOG = "EditDialog";
    private ShoppingListAdapter shoppingListAdapter;
    private CoordinatorLayout layoutContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutContent = findViewById(R.id.layoutContent);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showCreateItemDialog();
            }
        });

        Button btnDeleteAll = findViewById(R.id.btnDeleteAll);
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoppingListAdapter.removeAll();
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recyclerList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initItems(recyclerView);
    }

    private void initItems(final RecyclerView recyclerView) {
        // get all items
        new Thread() {
            @Override
            public void run() {
                final List<Item> items = AppDatabase.getAppDatabase(MainActivity.this).itemDao().getAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shoppingListAdapter = new ShoppingListAdapter(items, MainActivity.this);
                        recyclerView.setAdapter(shoppingListAdapter);

                        ItemTouchHelper.Callback callback =
                                new ShoppingItemTouchHelperCallback(shoppingListAdapter);
                        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                        touchHelper.attachToRecyclerView(recyclerView);
                    }
                });
            }
        }.start();
    }


    private void showCreateItemDialog() {
        new NewItemDialog().show(getSupportFragmentManager(), CREATE_ITEM_DIALOG);
    }


    public void showEditItemDialog(Item item) {
        NewItemDialog editDialog = new NewItemDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_EDIT, item);
        editDialog.setArguments(bundle);

        editDialog.show(getSupportFragmentManager(), EDIT_DIALOG);
    }

    public void showSnackBarMessage(String message) {
        Snackbar.make(
                layoutContent,
                message,
                Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.action_hide), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        }).show();
    }

    @Override
    public void onNewItemCreated(final Item item) {
        new Thread() {
            @Override
            public void run() {
                long id = AppDatabase.getAppDatabase(MainActivity.this).
                        itemDao().insertItem(item);
                item.setItemId(id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shoppingListAdapter.addItem(item);
                        showSnackBarMessage(getString(R.string.msg_item_added));
                    }
                });
            }
        }.start();
    }

    @Override
    public void onItemUpdated(final Item item) {
        new Thread() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(MainActivity.this).itemDao().update(item);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shoppingListAdapter.updateItem(item);
                    }
                });
            }
        }.start();
    }

}
