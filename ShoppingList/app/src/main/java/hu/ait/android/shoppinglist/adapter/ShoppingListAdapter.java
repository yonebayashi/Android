package hu.ait.android.shoppinglist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import hu.ait.android.shoppinglist.MainActivity;
import hu.ait.android.shoppinglist.R;
import hu.ait.android.shoppinglist.data.AppDatabase;
import hu.ait.android.shoppinglist.data.Item;

public class ShoppingListAdapter
        extends RecyclerView.Adapter <ShoppingListAdapter.ViewHolder> {

    private List<Item> itemList;
    private Context context;

    public ShoppingListAdapter(List<Item> items, Context context) {
        itemList = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivIcon;
        public TextView tvName;
        public TextView tvPrice;
        public CheckBox cbPurchased;
        public Button btnViewItemDetails;
        public Button btnDelete;
        public Button btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);

            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            cbPurchased = itemView.findViewById(R.id.cbPurchased);
            btnViewItemDetails = itemView.findViewById(R.id.btnViewItemDetails);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRow = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_row, parent, false);
        return new ViewHolder(viewRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tvName.setText(
                itemList.get(position).getItemName());
        holder.tvPrice.setText("$ " + itemList.get(position).getItemPrice());
        holder.ivIcon.setImageResource(itemList.get(position).getItemCategoryAsEnum().getIconId());
        holder.cbPurchased.setChecked(itemList.get(position).isPurchased());

        holder.cbPurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemList.get(position).setPurchased(holder.cbPurchased.isChecked());
            }
        });

        holder.btnViewItemDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).showSnackBarMessage(itemList.get(position).getItemDesc());
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(holder.getAdapterPosition());
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showEditItemDialog(itemList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem(Item item) {
        itemList.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        final Item itemToDelete = itemList.get(position);
        itemList.remove(itemToDelete);
        notifyItemRemoved(position);

        new Thread() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(context).itemDao().delete(itemToDelete);
            }
        }.start();
    }

    public void updateItem(Item item) {
        int index = findItemIndex(item.getItemId());
        itemList.set(index, item);
        notifyItemChanged(index);
    }

    public void removeAll() {
        while (!itemList.isEmpty()){
            removeItem(0);
        }
    }

    private int findItemIndex(long itemId) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getItemId() == itemId) {
                return i;
            }
        }
        return -1;
    }

    public void swapItems(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }
}
