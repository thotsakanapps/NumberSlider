package com.thotsakan.numberslider.gameview;

import java.util.Arrays;
import java.util.Comparator;

import com.thotsakan.numberslider.R;

public enum TileType {

	BLANK(0, R.drawable.ic_0),

	EIGHT(8, R.drawable.ic_8),

	ELEVEN(11, R.drawable.ic_11),

	FIFTEEN(15, R.drawable.ic_15),

	FIVE(5, R.drawable.ic_5),

	FOUR(4, R.drawable.ic_4),

	FOURTEEN(14, R.drawable.ic_14),

	NINE(9, R.drawable.ic_9),

	ONE(1, R.drawable.ic_1),

	SEVEN(7, R.drawable.ic_7),

	SIX(6, R.drawable.ic_6),

	TEN(10, R.drawable.ic_10),

	THIRTEEN(13, R.drawable.ic_13),

	THREE(3, R.drawable.ic_3),

	TWELVE(12, R.drawable.ic_12),

	TWO(2, R.drawable.ic_2);

	public static TileType[] getSortedValues() {
		TileType[] values = values();
		Arrays.sort(values, new Comparator<TileType>() {
			@Override
			public int compare(TileType tile1, TileType tile2) {
				if (tile1.order < tile2.order) {
					return -1;
				} else if (tile1.order == tile2.order) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		return values;
	}

	public static TileType getTileType(int order) {
		for (TileType tileType : values()) {
			if (tileType.order == order) {
				return tileType;
			}
		}
		return BLANK;
	}

	private int order;

	private int resourceId;

	private TileType(int order, int resourceId) {
		this.order = order;
		this.resourceId = resourceId;
	}

	public int getResourceId() {
		return resourceId;
	}
}
