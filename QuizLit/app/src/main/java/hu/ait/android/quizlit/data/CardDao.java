package hu.ait.android.quizlit.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CardDao {

    @Query("SELECT * FROM card")
    List<Card> getAll();

    @Query("SELECT * FROM card WHERE deckId=:deckId")
    List<Card> getCardsFromDeck(final long deckId);

    @Insert
    long insert(Card card);

    @Delete
    void delete(Card card);

    @Update
    void update(Card card);

}
