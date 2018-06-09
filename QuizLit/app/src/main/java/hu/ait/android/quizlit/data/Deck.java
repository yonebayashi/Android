package hu.ait.android.quizlit.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Deck implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "title")
    private String title;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @ColumnInfo(name = "size")
    private int size;

    public Deck(String title) {
        this.title = title;
        this.size = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
