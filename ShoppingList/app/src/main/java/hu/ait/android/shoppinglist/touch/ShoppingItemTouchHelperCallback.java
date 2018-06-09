package hu.ait.android.shoppinglist.touch;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import hu.ait.android.shoppinglist.adapter.ShoppingListAdapter;

public class ShoppingItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ShoppingListAdapter adapter;

    public ShoppingItemTouchHelperCallback(ShoppingListAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    // enable swipes
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        adapter.swapItems(viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.removeItem(viewHolder.getAdapterPosition());
    }
}
