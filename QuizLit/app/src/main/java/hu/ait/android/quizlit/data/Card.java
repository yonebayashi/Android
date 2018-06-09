package hu.ait.android.quizlit.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import static android.arch.persistence.room.ForeignKey.CASCADE;

import java.io.Serializable;

@Entity(foreignKeys = @ForeignKey(
        entity = Deck.class,
        parentColumns = "id",
        childColumns = "deckId",
        onDelete = CASCADE))

public class Card implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long cardId;

    @ColumnInfo(name = "question")
    private String question;

    @ColumnInfo(name = "answer")
    private boolean answer;

    @ColumnInfo(name = "deckId")
    private long deckId;

    public Card(String question, boolean answer, final long deckId) {
        this.question = question;
        this.answer = answer;
        this.deckId = deckId;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public long getDeckId() {
        return deckId;
    }

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean getAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
