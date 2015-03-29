package com.thotsakan.numberslider.gameview;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.thotsakan.numberslider.R;
import com.thotsakan.numberslider.gameview.Board.Direction;

public class GameView extends View {

	private Board board;

	private int boardSize;

	private boolean gameOver;

	GestureDetector gestureDetector;

	private Paint paint;

	private Board winningBoard;

	public GameView(Context context, int boardSize) {
		super(context);
		this.boardSize = boardSize;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5);
		gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				board.handleFling(Direction.getDirection(e1.getX(), e1.getY(), e2.getX(), e2.getY()));
				invalidate();
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
		winningBoard = new Board(boardSize);
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (i == boardSize - 1 && i == j) {
					winningBoard.setTileType(i, j, TileType.BLANK);
				} else {
					winningBoard.setTileType(i, j, TileType.getTileType(i * boardSize + j + 1));
				}
			}
		}
		board = new Board(boardSize);
		newGame();
	}

	private void displayResult() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
		dialogBuilder.setTitle(R.string.results_dialog_title);
		dialogBuilder.setMessage(R.string.results_dialog_message);
		dialogBuilder.setPositiveButton(R.string.results_dialog_newGame, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				newGame();
				dialog.dismiss();
			}
		});
		dialogBuilder.show();
	}

	public void newGame() {
		gameOver = false;
		board.initBoard();
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int boardLen = Math.min(getWidth(), getHeight());
		int tileLen = boardLen / boardSize;
		board.draw(canvas, getResources(), tileLen, tileLen); // board
		for (int i = 0; i <= boardSize; i++) {
			int tileEnd = i * tileLen;
			canvas.drawLine(tileEnd, 0, tileEnd, boardLen, paint); // vertical
			canvas.drawLine(0, tileEnd, boardLen, tileEnd, paint); // horizontal
		}
		super.onDraw(canvas);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gameOver) {
			return super.onTouchEvent(event);
		} else {
			boolean onTouchResult = gestureDetector.onTouchEvent(event);
			gameOver = winningBoard.isSameAs(board);
			if (gameOver) {
				displayResult();
			}
			return onTouchResult;
		}
	}
}
