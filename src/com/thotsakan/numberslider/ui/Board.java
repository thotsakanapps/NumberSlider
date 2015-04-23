package com.thotsakan.numberslider.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

final class Board {

	public final int BLANK_TILE;

	private int boardSize;

	public int[][] tiles;

	public Board(int boardSize) {
		this.boardSize = boardSize;
		BLANK_TILE = boardSize * boardSize;
		tiles = new int[boardSize][boardSize];
	}

	public void initBoard() {
		List<Integer> tileTypes = new ArrayList<Integer>(boardSize * boardSize);
		for (int i = 1; i <= boardSize * boardSize; i++) {
			tileTypes.add(i);
		}
		Collections.shuffle(tileTypes, new Random(System.currentTimeMillis()));
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				tiles[i][j] = tileTypes.get(i * boardSize + j);
			}
		}
	}

	public boolean hasWon() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (tiles[i][j] != i * boardSize + j + 1) {
					return false;
				}
			}
		}
		return true;
	}

	private int[] getBlankTileLoc() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (tiles[i][j] == BLANK_TILE) {
					return new int[] { i, j };
				}
			}
		}
		return null;
	}

	public enum Direction {
		DOWN, LEFT, RIGHT, UP;

		private static Direction get(double angle) {
			if (inRange(angle, 45, 135)) {
				return UP;
			} else if (inRange(angle, 225, 315)) {
				return DOWN;
			} else if (inRange(angle, 135, 225)) {
				return LEFT;
			} else {
				return RIGHT;
			}
		}

		private static double getAngle(float x1, float y1, float x2, float y2) {
			double rad = Math.atan2(y1 - y2, x2 - x1);
			return (rad * 180 / Math.PI + 360) % 360;
		}

		public static Direction getDirection(float x1, float y1, float x2, float y2) {
			double angle = getAngle(x1, y1, x2, y2);
			return Direction.get(angle);
		}

		private static boolean inRange(double angle, float init, float end) {
			return (angle >= init) && (angle < end);
		}
	}

	public boolean handleFling(Direction direction) {
		int[] blankTileLoc = getBlankTileLoc();

		switch (direction) {
		case UP:
			return moveUp(blankTileLoc[0], blankTileLoc[1]);
		case DOWN:
			return moveDown(blankTileLoc[0], blankTileLoc[1]);
		case LEFT:
			return moveLeft(blankTileLoc[0], blankTileLoc[1]);
		case RIGHT:
			return moveRight(blankTileLoc[0], blankTileLoc[1]);
		default:
			return false;
		}
	}

	private boolean moveDown(int row, int col) {
		if (row == 0) {
			return false;
		}

		tiles[row][col] = tiles[row - 1][col];
		tiles[row - 1][col] = BLANK_TILE;
		return true;
	}

	private boolean moveLeft(int row, int col) {
		if (col == boardSize - 1) {
			return false;
		}

		tiles[row][col] = tiles[row][col + 1];
		tiles[row][col + 1] = BLANK_TILE;
		return true;
	}

	private boolean moveRight(int row, int col) {
		if (col == 0) {
			return false;
		}

		tiles[row][col] = tiles[row][col - 1];
		tiles[row][col - 1] = BLANK_TILE;
		return true;
	}

	private boolean moveUp(int row, int col) {
		if (row == boardSize - 1) {
			return false;
		}

		tiles[row][col] = tiles[row + 1][col];
		tiles[row + 1][col] = BLANK_TILE;
		return true;
	}
}
