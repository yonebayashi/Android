package hu.ait.android.shoppinglist.data;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import hu.ait.android.shoppinglist.MainActivity;
import hu.ait.android.shoppinglist.R;


public class NewItemDialog extends DialogFragment {

    public interface ItemHandler {
        public void onNewItemCreated(Item item);
        public void onItemUpdated(Item item);
    }

    private ItemHandler itemHandler;
    private Spinner spinnerItemCategory;
    private EditText etItemName;
    private EditText etItemPrice;
    private EditText etItemDesc;
    private CheckBox cbPurchased;
    private Item itemToEdit = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Check if the Activity has implemented the interface, else throw an exception (immediately when app crashes)
        if (context instanceof ItemHandler) {
            itemHandler = (ItemHandler) context;
        } else {
            throw new RuntimeException(
                    getString(R.string.error_wrong_interface)
            );
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.create_dialog_title);

        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_item, null);

        spinnerItemCategory = view.findViewById(R.id.spinnerItemCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.item_categories_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItemCategory.setAdapter(adapter);

        etItemName = view.findViewById(R.id.etItemName);
        etItemPrice = view.findViewById(R.id.etItemPrice);
        etItemDesc = view.findViewById(R.id.etItemDesc);
        cbPurchased = view.findViewById(R.id.cbDialogPurchased);

        // Display item data in Edit mode
        if (getArguments() != null &&
                getArguments().containsKey(MainActivity.KEY_EDIT)) {
            builder.setTitle(R.string.edit_dialog_title);
            itemToEdit = (Item) getArguments().getSerializable(MainActivity.KEY_EDIT);

            etItemName.setText(itemToEdit.getItemName());
            etItemPrice.setText("" + itemToEdit.getItemPrice());
            etItemDesc.setText(itemToEdit.getItemDesc());
            cbPurchased.setChecked(itemToEdit.isPurchased());
            spinnerItemCategory.setSelection(itemToEdit.getItemCategoryAsEnum().getValue());
        }

        builder.setView(view);
        builder.setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ...
            }
        });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
                {
                    Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(new View.OnClickListener()
                    {
                @Override
                public void onClick(View v)
                {
                    if (!TextUtils.isEmpty(etItemName.getText()) && !TextUtils.isEmpty(etItemPrice.getText())) {
                        // Edit item mode
                        if (getArguments() != null &&
                                getArguments().containsKey(MainActivity.KEY_EDIT)) {
                            itemToEdit = (Item) getArguments().getSerializable(MainActivity.KEY_EDIT);
                            itemToEdit.setItemName(etItemName.getText().toString());
                            itemToEdit.setItemPrice(Integer.parseInt(etItemPrice.getText().toString()));
                            itemToEdit.setPurchased(cbPurchased.isChecked());
                            itemToEdit.setItemDesc(etItemDesc.getText().toString());
                            itemToEdit.setItemCategory(spinnerItemCategory.getSelectedItemPosition());

                            itemHandler.onItemUpdated(itemToEdit);
                        } else {
                            // Create item mode
                            Item item = new Item(
                                    spinnerItemCategory.getSelectedItemPosition(),
                                    etItemName.getText().toString(),
                                    Integer.parseInt(etItemPrice.getText().toString()),
                                    cbPurchased.isChecked(),
                                    etItemDesc.getText().toString()
                            );

                            itemHandler.onNewItemCreated(item);
                        }
                        d.dismiss();

                    } else {
                        etItemName.setError(getString(R.string.error_field_empty));
                        etItemPrice.setError(getString(R.string.error_field_empty));
                    }
                }
            });
        }
    }
}
