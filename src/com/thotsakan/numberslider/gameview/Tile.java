package com.thotsakan.numberslider.gameview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

final class Tile extends Point {

	private static final Paint paintTile = new Paint();

	public static void swapTiles(Tile tile1, Tile tile2) {
		TileType temp = tile1.tileType;
		tile1.tileType = tile2.tileType;
		tile2.tileType = temp;
	}

	private TileType tileType;

	public Tile(int row, int column) {
		super(row, column);
		tileType = TileType.BLANK;
	}

	public void draw(Canvas canvas, Resources resources, int width, int height) {
		Bitmap bitmap = BitmapFactory.decodeResource(resources, tileType.getResourceId());
		canvas.drawBitmap(bitmap, null, new Rect(y * height, x * width, (y * height) + height, (x * width) + width), paintTile);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (this == object) {
			return true;
		}
		if (!(object instanceof Tile)) {
			return false;
		}
		Tile other = (Tile) object;
		return tileType == other.tileType;
	}

	public TileType getTileType() {
		return tileType;
	}

	@Override
	public int hashCode() {
		return tileType.hashCode();
	}

	public boolean isBlank() {
		return tileType == TileType.BLANK;
	}

	public void setBlank() {
		tileType = TileType.BLANK;
	}

	public void setTileType(TileType tileType) {
		this.tileType = tileType;
	}
}
