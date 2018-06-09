package hu.ait.android.weatherforecast.data;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import hu.ait.android.weatherforecast.R;

import static android.app.Activity.RESULT_OK;

public class NewCityDialog extends DialogFragment {

    public interface CityHandler {
        public void onNewCityCreated(City city);
    }

    private CityHandler cityHandler;
    private EditText etCityName;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CityHandler) {
            cityHandler = (CityHandler) context;
        } else {
            throw new RuntimeException(getString(R.string.error_wrong_interface));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add city");

        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_city, null);
        etCityName = view.findViewById(R.id.etCityName);


        builder.setView(view);
        builder.setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button saveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            Button cancelButton = (Button) d.getButton(Dialog.BUTTON_NEGATIVE);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(etCityName.getText())) {
                        City newCity = new City(
                                etCityName.getText().toString()
                        );
                        cityHandler.onNewCityCreated(newCity);
                        d.dismiss();
                    } else {
                        etCityName.setError(getString(R.string.error_field_empty));
                    }
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    d.dismiss();
                }
            });
        }
    }
}
