package hu.ait.android.quizlit.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import hu.ait.android.quizlit.activity.CardsActivity;
import hu.ait.android.quizlit.R;
import hu.ait.android.quizlit.activity.MainActivity;
import hu.ait.android.quizlit.data.Card;
import hu.ait.android.quizlit.data.Deck;

public class NewCardDialog extends DialogFragment {

    public interface CardHandler {
        public void onNewCardCreated(String question, boolean answer, long deckId);
        public void onCardUpdated(Card card);
    }

    private CardHandler cardHandler;
    private EditText etQuestion;
    private CheckBox cbTrue;
    private long deckId;
    private Card cardToEdit = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CardHandler) {
            cardHandler = (CardHandler) context;
        } else {
            throw new RuntimeException(getString(R.string.error_wrong_interface));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_create_card_title);

        deckId = getArguments().getLong(CardsActivity.KEY_DECK);

        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_card, null);

        // Set up the view
        etQuestion = view.findViewById(R.id.etQuestion);
        cbTrue = view.findViewById(R.id.cbTrue);

        if (getArguments() != null &&
                getArguments().containsKey(CardsActivity.KEY_EDIT)) {
            builder.setTitle(R.string.dialog_edit_card_title);
            cardToEdit = (Card) getArguments().getSerializable(CardsActivity.KEY_EDIT);
            etQuestion.setText(cardToEdit.getQuestion());
            if (cardToEdit.getAnswer()) {
                cbTrue.setChecked(true);
            } else {
                cbTrue.setChecked(false);
            }
        }

        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
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
                    if (!TextUtils.isEmpty(etQuestion.getText())) {
                        // Edit mode
                        if (getArguments() != null &&
                                getArguments().containsKey(CardsActivity.KEY_EDIT)) {
                            cardToEdit = (Card) getArguments().getSerializable(CardsActivity.KEY_EDIT);
                            cardToEdit.setQuestion(etQuestion.getText().toString());
                            cardToEdit.setAnswer(cbTrue.isChecked());
                            cardHandler.onCardUpdated(cardToEdit);
                        } else {
                            // Create mode
                            cardHandler.onNewCardCreated(
                                    etQuestion.getText().toString(),
                                    cbTrue.isChecked(),
                                    deckId);
                        }
                        d.dismiss();

                    } else {
                        etQuestion.setError(getString(R.string.error_field_empty));
                    }
                }
            });
        }
    }
}
