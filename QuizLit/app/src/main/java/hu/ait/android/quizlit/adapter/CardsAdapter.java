package hu.ait.android.quizlit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

import hu.ait.android.quizlit.R;
import hu.ait.android.quizlit.activity.CardsActivity;
import hu.ait.android.quizlit.activity.MainActivity;
import hu.ait.android.quizlit.data.Card;
import hu.ait.android.quizlit.data.Deck;

public class CardsAdapter
        extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    private List<Card> cardList;
    private Context context;

    public CardsAdapter(List<Card> cards, Context context) {
        cardList = cards;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFrontText;
        private TextView tvBackText;
        private Button btnEditCard;

        public ViewHolder (View itemView) {
            super(itemView);
            tvFrontText = itemView.findViewById(R.id.tvFrontText);
            tvBackText = itemView.findViewById(R.id.tvBackText);
            btnEditCard = itemView.findViewById(R.id.btnEditCard);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewRow = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_row, parent, false);
        return new ViewHolder(viewRow);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {
        vh.tvFrontText.setText(cardList.get(position).getQuestion());
        vh.tvBackText.setText(""+cardList.get(position).getAnswer());
        vh.btnEditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CardsActivity) context).showEditCardDialog(
                        cardList.get(vh.getAdapterPosition())
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public void addCard(Card card) {
        cardList.add(card);
        notifyDataSetChanged();
    }

    public void updateCard(Card card) {
        int index = findIndexOf(card.getCardId());
        cardList.set(index, card);
        notifyItemChanged(index);
    }

    private int findIndexOf(long id) {
        for (int i = 0; i < cardList.size(); i++) {
            if (cardList.get(i).getCardId() == id) {
                return i;
            }
        }
        return -1;
    }

}
