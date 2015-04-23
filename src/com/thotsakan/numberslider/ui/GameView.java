package com.thotsakan.numberslider.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.thotsakan.numberslider.MainActivity;
import com.thotsakan.numberslider.R;
import com.thotsakan.numberslider.ui.Board.Direction;

public class GameView extends View {

	private Resources resources;

	private final Paint paintTileBorder;

	private final Paint paintTileBackground;

	private final Paint paintNumber;

	private Board board;

	private int boardSize;

	private boolean gameOver;

	private int currentMoves;

	private final GestureDetector gestureDetector;

	private final RectF rect;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		resources = getResources();

		paintTileBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintTileBorder.setColor(Color.BLACK);
		paintTileBorder.setStyle(Style.STROKE);
		paintTileBorder.setStrokeWidth(5);

		paintTileBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintTileBackground.setColor(Color.WHITE);
		paintTileBackground.setAlpha(128);
		paintTileBackground.setStyle(Style.FILL);

		paintNumber = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintNumber.setColor(Color.BLACK);
		paintNumber.setStyle(Style.FILL);
		paintNumber.setTypeface(Typeface.DEFAULT_BOLD);

		gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				++currentMoves;
				board.handleFling(Direction.getDirection(e1.getX(), e1.getY(), e2.getX(), e2.getY()));
				invalidate();
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});

		rect = new RectF();
	}

	public void newGame(int boardSize) {
		this.boardSize = boardSize;
		this.board = new Board(boardSize);
		this.gameOver = false;
		this.currentMoves = 0;
		this.board.initBoard();
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int boardLen = Math.min(getWidth(), getHeight());
		int tileLen = boardLen / boardSize;

		paintNumber.setTextSize(tileLen / 2);

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (board.tiles[i][j] != board.BLANK_TILE) {
					float topX = j * tileLen;
					float topY = i * tileLen;

					rect.set(topX, topY, topX + tileLen, topY + tileLen);
					rect.inset(5, 5);

					canvas.drawRoundRect(rect, 10, 10, paintTileBackground);
					canvas.drawRoundRect(rect, 10, 10, paintTileBorder);

					String value = Integer.toString(board.tiles[i][j]);
					float offsetX = (tileLen - paintNumber.measureText(value)) / 2;
					float offsetY = (tileLen - paintNumber.getTextSize()) / 2 - paintNumber.ascent();
					canvas.drawText(value, topX + offsetX, topY + offsetY, paintNumber);
				}
			}
		}

		showMoves();

		super.onDraw(canvas);
	}

	private void showMoves() {
		NumberFormat scoreFormat = new DecimalFormat("0000");

		// show best moves
		String key = resources.getString(R.string.best_moves_key) + "_" + boardSize;
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		final int bestMoves = preferences.getInt(key, 0);
		TextView bestMovesView = (TextView) ((MainActivity) getContext()).findViewById(R.id.best_moves);
		bestMovesView.setText("BEST: " + scoreFormat.format(bestMoves));

		// show current moves
		TextView currentMovesView = (TextView) ((MainActivity) getContext()).findViewById(R.id.current_moves);
		currentMovesView.setText("MOVES: " + scoreFormat.format(currentMoves));
	}

	private void updateBestMoves() {
		String key = resources.getString(R.string.best_moves_key) + "_" + boardSize;
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		int currentBest = preferences.getInt(key, 0);
		if (currentBest == 0 || currentBest > currentMoves) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt(key, currentMoves);
			editor.apply();
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gameOver) {
			return super.onTouchEvent(event);
		} else {
			boolean onTouchResult = gestureDetector.onTouchEvent(event);
			gameOver = board.hasWon();
			if (gameOver) {
				updateBestMoves();
				displayResult();
			}
			return onTouchResult;
		}
	}

	private void displayResult() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
		dialogBuilder.setTitle(R.string.results_dialog_title);
		dialogBuilder.setMessage(R.string.results_dialog_message);
		dialogBuilder.setPositiveButton(R.string.results_dialog_newGame, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				newGame(boardSize);
				dialog.dismiss();
			}
		});
		dialogBuilder.show();
	}
}
