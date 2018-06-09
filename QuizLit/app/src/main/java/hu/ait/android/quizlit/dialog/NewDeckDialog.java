package hu.ait.android.quizlit.dialog;

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
import android.widget.Button;
import android.widget.EditText;

import hu.ait.android.quizlit.R;
import hu.ait.android.quizlit.activity.MainActivity;
import hu.ait.android.quizlit.data.Deck;

public class NewDeckDialog extends DialogFragment {

    public interface DeckHandler {
        public void onNewDeckCreated(String title);
        public void onDeckUpdated(Deck deck);
    }

    private DeckHandler deckHandler;
    private EditText etTitle;
    private Deck deckToEdit = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DeckHandler) {
            deckHandler = (DeckHandler) context;
        } else {
            throw new RuntimeException(getString(R.string.error_wrong_interface));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_create_deck_title);

        // Set up the input
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_deck, null);
        etTitle = view.findViewById(R.id.etTitle);

        // if in Edit mode
        if (getArguments() != null &&
                getArguments().containsKey(MainActivity.KEY_EDIT)) {
            builder.setTitle(R.string.dialog_edit_deck_title);
            deckToEdit = (Deck) getArguments().getSerializable(MainActivity.KEY_EDIT);
            etTitle.setText(deckToEdit.getTitle());
        }

        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
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

        final AlertDialog d = (AlertDialog) getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(etTitle.getText())) {
                        // Edit mode
                        if (getArguments() != null &&
                                getArguments().containsKey(MainActivity.KEY_EDIT)) {
                            deckToEdit = (Deck) getArguments().getSerializable(MainActivity.KEY_EDIT);
                            deckToEdit.setTitle(etTitle.getText().toString());
                            deckHandler.onDeckUpdated(deckToEdit);
                        } else {
                            // Create mode
                           deckHandler.onNewDeckCreated(etTitle.getText().toString());
                        }
                        d.dismiss();

                    } else {
                        etTitle.setError(getString(R.string.error_field_empty));
                    }
                }
            });
        }
    }
}
