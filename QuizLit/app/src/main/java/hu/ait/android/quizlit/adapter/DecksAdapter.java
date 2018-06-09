package hu.ait.android.quizlit.adapter;

import hu.ait.android.quizlit.activity.CardFlipActivity;
import hu.ait.android.quizlit.activity.CardsActivity;
import hu.ait.android.quizlit.activity.MainActivity;
import hu.ait.android.quizlit.activity.QuizActivity;
import  hu.ait.android.quizlit.R;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import hu.ait.android.quizlit.data.AppDatabase;
import hu.ait.android.quizlit.data.Card;
import hu.ait.android.quizlit.data.Deck;

public class DecksAdapter
        extends RecyclerView.Adapter<DecksAdapter.ViewHolder> {

    public static final String KEY_CONTENT = "KEY_CONTENT";
    private List<Deck> deckList;
    private Context context;

    // constructor
    public DecksAdapter(List<Deck> decks, Context context) {
        deckList = decks;
        this.context = context;
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvSize;
        private Button btnView;
        private Button btnStart;
        private Button btnStudy;
        private Button btnEdit;

        public ViewHolder (View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSize = itemView.findViewById(R.id.tvSize);
            btnStudy = itemView.findViewById(R.id.btnStudy);
            btnStart = itemView.findViewById(R.id.btnStart);
            btnView = itemView.findViewById(R.id.btnView);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewRow = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.deck_row, parent, false);
        return new ViewHolder(viewRow);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, final int position) {
        vh.tvTitle.setText(deckList.get(position).getTitle());

        setDeckSize(vh, position);

        vh.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long deckId = deckList.get(position).getId();

                Intent intent = new Intent(context, CardsActivity.class);
                intent.putExtra(KEY_CONTENT, deckId);
                context.startActivity(intent);
            }
        });

        vh.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Deck deck = deckList.get(position);

                if (deck.getSize() > 0) {
                    Intent intent = new Intent(context, QuizActivity.class);
                    intent.putExtra(KEY_CONTENT, deck.getId());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Add some cards to start quiz!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        vh.btnStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Deck deck = deckList.get(position);

                if (deck.getSize() > 0) {
                    Intent intent = new Intent(context, CardFlipActivity.class);
                    intent.putExtra(KEY_CONTENT, deck.getId());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Add some cards to start study!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        vh.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).showEditDeckDialog(
                      deckList.get(vh.getAdapterPosition())
                );
            }
        });
    }

    private void setDeckSize(final ViewHolder vh, final int position) {
        new Thread(){
            @Override
            public void run() {
                final Deck deck = deckList.get(position);
                final List<Card> cards = AppDatabase.getAppDatabase(context).cardDao().getCardsFromDeck(deck.getId());
                deck.setSize(cards.size());

                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vh.tvSize.setText(cards.size() + " cards");
                    }
                });
            }
        }.start();
    }

    @Override
    public int getItemCount() {
        return deckList.size();
    }

    public void addDeck(Deck deck) {
        deckList.add(deck);
        notifyDataSetChanged();
    }

    public void updateDeck(Deck deck) {
        int index = findIndexOf(deck.getId());
        deckList.set(index, deck);
        notifyItemChanged(index);
    }

    private int findIndexOf(long id) {
        for (int i = 0; i < deckList.size(); i++) {
            if (deckList.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

}

