package hu.ait.android.quizlit.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Deck.class, Card.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract DeckDao deckDao();
    public abstract CardDao cardDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            // access the db
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "quizlit-db").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
