package com.thotsakan.numberslider.gameview;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Canvas;

final class Board {

	public enum Direction {
		DOWN, LEFT, RIGHT, UP;

		public static Direction get(double angle) {
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
			double rad = Math.atan2(y1 - y2, x2 - x1) + Math.PI;
			return (rad * 180 / Math.PI + 180) % 360;
		}

		public static Direction getDirection(float x1, float y1, float x2, float y2) {
			double angle = getAngle(x1, y1, x2, y2);
			return Direction.get(angle);
		}

		private static boolean inRange(double angle, float init, float end) {
			return (angle >= init) && (angle < end);
		}
	}

	private int boardSize;

	private Tile[][] tiles;

	public Board(int boardSize) {
		this.boardSize = boardSize;
		tiles = new Tile[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				tiles[i][j] = new Tile(i, j);
			}
		}
	}

	public void draw(Canvas canvas, Resources resources, int width, int height) {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				tiles[i][j].draw(canvas, resources, width, height);
			}
		}
	}

	private int[] getBlankTileLoc() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (tiles[i][j].isBlank()) {
					return new int[] { i, j };
				}
			}
		}
		return null;
	}

	public boolean handleFling(Direction direction) {
		int[] blankTileLoc = getBlankTileLoc();

		switch (direction) {
		case UP:
			return moveUp(blankTileLoc);
		case DOWN:
			return moveDown(blankTileLoc);
		case LEFT:
			return moveLeft(blankTileLoc);
		case RIGHT:
			return moveRight(blankTileLoc);
		default:
			return false;
		}
	}

	public void initBoard() {
		List<TileType> tileTypes = Arrays.asList(Arrays.copyOfRange(TileType.getSortedValues(), 0, boardSize * boardSize));
		Collections.shuffle(tileTypes, new Random(System.currentTimeMillis()));
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				tiles[i][j].setTileType(tileTypes.get(i * boardSize + j));
			}
		}
	}

	public boolean isSameAs(Board other) {
		if (boardSize != other.boardSize) {
			return false;
		}
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (!tiles[i][j].equals(other.tiles[i][j])) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean moveDown(int[] blankTileLoc) {
		if (blankTileLoc[0] == 0) {
			return false;
		}

		Tile blankTile = tiles[blankTileLoc[0]][blankTileLoc[1]];
		Tile topTile = tiles[blankTileLoc[0] - 1][blankTileLoc[1]];
		Tile.swapTiles(blankTile, topTile);
		return true;
	}

	private boolean moveLeft(int[] blankTileLoc) {
		if (blankTileLoc[1] == boardSize - 1) {
			return false;
		}

		Tile blankTile = tiles[blankTileLoc[0]][blankTileLoc[1]];
		Tile rightTile = tiles[blankTileLoc[0]][blankTileLoc[1] + 1];
		Tile.swapTiles(blankTile, rightTile);
		return true;
	}

	private boolean moveRight(int[] blankTileLoc) {
		if (blankTileLoc[1] == 0) {
			return false;
		}

		Tile blankTile = tiles[blankTileLoc[0]][blankTileLoc[1]];
		Tile leftTile = tiles[blankTileLoc[0]][blankTileLoc[1] - 1];
		Tile.swapTiles(blankTile, leftTile);
		return true;
	}

	private boolean moveUp(int[] blankTileLoc) {
		if (blankTileLoc[0] == boardSize - 1) {
			return false;
		}

		Tile blankTile = tiles[blankTileLoc[0]][blankTileLoc[1]];
		Tile bottomTile = tiles[blankTileLoc[0] + 1][blankTileLoc[1]];
		Tile.swapTiles(blankTile, bottomTile);
		return true;
	}

	public void setTileType(int row, int col, TileType tileType) {
		tiles[row][col].setTileType(tileType);
	}
}
