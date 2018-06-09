package hu.ait.android.quizlit.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DeckDao {

    @Query("SELECT * FROM deck")
    List<Deck> getAll();

    @Insert
    long insert(Deck deck);

    @Delete
    void delete(Deck deck);

    @Update
    void update(Deck deck);
}
