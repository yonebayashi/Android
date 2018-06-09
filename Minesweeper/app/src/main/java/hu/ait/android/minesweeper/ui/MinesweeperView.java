package hu.ait.android.minesweeper.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import hu.ait.android.minesweeper.MainActivity;
import hu.ait.android.minesweeper.R;
import hu.ait.android.minesweeper.model.MinesweeperModel;

public class MinesweeperView extends View {
    private Paint paintBackground;
    private Paint paintLine;
    private Paint paintText;
    private Bitmap bitmapFlag;
    private Bitmap bitmapMine;
    private Bitmap bitmapBg;

    public static int status = 2;
    public static int minesFound = 0;

    public MinesweeperView(Context context,
                           @Nullable AttributeSet attrs) {
        super(context, attrs);
        paintBackground = new Paint();
        paintBackground.setColor(Color.BLACK);
        paintBackground.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.GRAY);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        paintText= new Paint();
        paintText.setColor(Color.WHITE);

        bitmapMine = BitmapFactory.decodeResource(getResources(),R.drawable.mine);
        bitmapFlag = BitmapFactory.decodeResource(getResources(),R.drawable.flag);
        bitmapBg = BitmapFactory.decodeResource(getResources(), R.drawable.background);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0,0, getWidth(), getHeight(), paintBackground);
        canvas.drawBitmap(bitmapBg, 0, 0, null);

        drawGameArea(canvas);
        drawPlayer(canvas);
    }

    private void drawPlayer(Canvas canvas) {
        if (status == 0) {
            onGameLost(canvas);

        } else {
            onGameContinued(canvas);

        }
    }

    private void onGameContinued(Canvas canvas) {
        // game is still going
        for (short i=0; i<5; i++) {
            for (short j=0; j<5; j++) {
                if (MinesweeperModel.getInstance().getFieldContent(i,j) == MinesweeperModel.FLAG) {
                    canvas.drawBitmap(bitmapFlag, i * getWidth()/5, j * getHeight()/5, null);
                }
                else if (MinesweeperModel.getInstance().getFieldContent(i,j) == MinesweeperModel.EMPTY ||
                        MinesweeperModel.getInstance().getFieldContent(i,j) == MinesweeperModel.MINE) {
                }
                else {
                    canvas.drawText(
                            String.valueOf(MinesweeperModel.getInstance().getFieldContent(i, j)),
                            i * getWidth() / 5,
                            j * getWidth() / 5 + getHeight() / 10, paintText);
                }
            }
        }
        if (status == 1) {
            // player has won
            ((MainActivity)getContext()).showMessage("You won!");
        }
    }

    private void onGameLost(Canvas canvas) {
        // player has lost
        for (short i=0; i<5; i++) {
            for (short j=0; j<5; j++) {
                if (MinesweeperModel.getInstance().getFieldContent(i,j) == MinesweeperModel.MINE ||
                        MinesweeperModel.getInstance().getFieldContent(i,j) == MinesweeperModel.FLAG) {
                    canvas.drawBitmap(bitmapMine, i * getWidth()/5, j * getHeight()/5, null);
                }
                else if (MinesweeperModel.getInstance().getFieldContent(i,j) == MinesweeperModel.EMPTY) {
                }
                else {
                    canvas.drawText(
                            String.valueOf(MinesweeperModel.getInstance().getFieldContent(i, j)),
                            i * getWidth() / 5,
                            j * getWidth() / 5 + getHeight()/10, paintText);
                }
            }
        }
        ((MainActivity)getContext()).showMessage("Wrong guess! Game over.");
    }

    private void drawGameArea(Canvas canvas) {
        // border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);

        // four horizontal lines
        canvas.drawLine(0, getHeight() / 5, getWidth(), getHeight() / 5, paintLine);
        canvas.drawLine(0, 2 * getHeight() / 5, getWidth(), 2 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 3 * getHeight() / 5, getWidth(), 3 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 4 * getHeight() / 5, getWidth(), 4 * getHeight() / 5, paintLine);

        // four vertical lines
        canvas.drawLine(getWidth() / 5, 0, getWidth() / 5, getHeight(), paintLine);
        canvas.drawLine(2 * getWidth() / 5, 0, 2 * getWidth() / 5, getHeight(), paintLine);
        canvas.drawLine(3 * getWidth() / 5, 0, 3 * getWidth() / 5, getHeight(), paintLine);
        canvas.drawLine(4 * getWidth() / 5, 0, 4 * getWidth() / 5, getHeight(), paintLine);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (status == 2) {
                int tX = ((int) event.getX() / (getWidth() / 5));
                int tY = ((int) event.getY() / (getHeight() / 5));

                if (MinesweeperModel.flag == 1) {
                    flagField((short) tX, (short) tY);

                } else {
                    tryField(tX, tY);

                }
            } else {
                // disable touch event if game is over
            }
        }
        return true;

    }

    private void tryField(int tX, int tY) {
        // Player is trying a field
        if (MinesweeperModel.getInstance().getFieldContent((short) tX, (short) tY) == MinesweeperModel.MINE) {
            // Mine found. Game over
            status = 0;
            invalidate();
        } else if (MinesweeperModel.getInstance().getFieldContent((short) tX, (short) tY) == MinesweeperModel.EMPTY) {
            MinesweeperModel.getInstance().setFieldContent((short) tX, (short) tY, MinesweeperModel.getInstance().checkNeighborMines(tX, tY));
            status = 2;
            invalidate();
        } else {
            status = 2;
        }
    }

    private void flagField(short tX, short tY) {
        // Player is flagging a field
        if (MinesweeperModel.getInstance().getFieldContent(tX, tY) == MinesweeperModel.MINE) {
            // Mine found.
            minesFound ++;
            MinesweeperModel.getInstance().setFieldContent(tX, tY, MinesweeperModel.FLAG);
            // If there's no more mine left, the player wins the game
            if (checkIfWon()) {
                status = 1;
                invalidate();
            } else {
                // else, just draw a flag
                status = 2;
                invalidate();
            }
        } else if (MinesweeperModel.getInstance().getFieldContent(tX, tY) == MinesweeperModel.EMPTY) {
            // No mine found. Game over
            status = 0;
            invalidate();
        } else {
            status = 2;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag, getWidth()/5, getHeight()/5, false);
        bitmapMine = Bitmap.createScaledBitmap(bitmapMine, getWidth()/5, getHeight()/5, false);

        paintText.setTextSize(getHeight()/10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // creates a square (even if we change the dimensions in the activity layout)
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    public void clearBoard() {
        status = 2;
        MinesweeperModel.getInstance().resetGame();
        invalidate();
    }

    public boolean checkIfWon() {
        return minesFound == 3;
    }
}
