package hu.ait.android.shoppinglist.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

import hu.ait.android.shoppinglist.R;

@Entity
public class Item implements Serializable {

    public enum ItemCategory {
        FOOD(0, R.drawable.food),
        CLOTHES(1, R.drawable.clothes),
        ELECTRONIC(2, R.drawable.electronic),
        BOOK(3, R.drawable.book);

        private int value;
        private int iconId;

        private ItemCategory(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public int getValue() {
            return value;
        }

        public int getIconId() {
            return iconId;
        }

        public static ItemCategory fromInt(int value) {
            for (ItemCategory i : ItemCategory.values()) {
                if (i.value == value) {
                    return i;
                }
            }
            return FOOD;
        }
    }

    @PrimaryKey(autoGenerate = true)
    private long itemId;

    @ColumnInfo(name = "item-category")
    private int itemCategory;

    @ColumnInfo(name = "item-name")
    private String itemName;

    @ColumnInfo(name = "item-price")
    private int itemPrice;

    @ColumnInfo(name = "purchased")
    private boolean purchased;

    @ColumnInfo(name = "item-desc")
    private String itemDesc;

    public Item(int itemCategory, String itemName, int itemPrice, boolean purchased, String itemDesc) {
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.purchased = purchased;
        this.itemDesc = itemDesc;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(int itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public ItemCategory getItemCategoryAsEnum() {
        return ItemCategory.fromInt(itemCategory);
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

}
