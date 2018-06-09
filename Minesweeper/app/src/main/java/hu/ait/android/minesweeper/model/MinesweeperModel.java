package hu.ait.android.minesweeper.model;

import java.util.Random;

public class MinesweeperModel {
    private static MinesweeperModel instance = null;

    // creates an instance of the game one at a time
    public static MinesweeperModel getInstance() {
        if (instance == null) {
            instance = new MinesweeperModel();
        }
        return instance;
    }

    private MinesweeperModel() {
    }

    // declares model variables
    public static final short EMPTY = 10;
    public static final short MINE = 11;
    public static final short FLAG = 12;
    public static short flag = 0;

    private short[][] model = {
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY}
    };

    // declares model methods
    public void setFieldContent(short x, short y, short content) {
        model[x][y] = content;
    }

    public short getFieldContent(short x, short y) { return model[x][y]; }

    public void insertMines() {
        // insert 3 mines at random
        Random rand = new Random();
        for (int i = 0; i < 3; i ++) {
            int x = rand.nextInt(5);
            int y = rand.nextInt(5);

            if (model[x][y] != MINE) {
                model[x][y] = MINE;
            }
        }
    }

    public short checkNeighborMines(int x, int y) {
        short count = 0;

        if ((x-1) >= 0) {
            if (model[x-1][y] == MINE || model[x-1][y] == FLAG) {
                count++;
            }
        }
        if ((x+1) < 5) {
            if (model[x+1][y] == MINE || model[x+1][y] == FLAG) {
                count++;
            }
        }
        if ((y-1) >= 0) {
            if (model[x][y-1] == MINE || model[x][y-1] == FLAG) {
                count++;
            }
        }
        if ((y+1) < 5) {
            if (model[x][y+1] == MINE || model[x][y+1] == FLAG) {
                count++;
            }
        }
        if ((x-1) >= 0 && (y-1) >= 0) {
            if (model[x-1][y-1] == MINE || model[x-1][y-1] == FLAG) {
                count++;
            }
        }
        if ((x+1) <5 && (y+1) < 5) {
            if (model[x+1][y+1] == MINE || model[x+1][y+1] == FLAG) {
                count++;
            }
        }
        if ((x-1) >= 0 && (y+1) <5) {
            if (model[x-1][y+1] == MINE || model[x-1][y+1] == FLAG) {
                count++;
            }
        }
        if ((x+1) <5 && (y-1) >= 0) {
            if (model[x+1][y-1] == MINE || model[x+1][y-1] == FLAG) {
                count++;
            }
        }
        return count;
    }

    public void resetGame() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                model[i][j] = EMPTY;
            }
        }
        insertMines();
    }
}
