package com.thotsakan.numberslider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.thotsakan.numberslider.gameview.GameView;

public class MainActivity extends Activity {

	private GameView gameView;

	private int getBoardSize() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String boardSize = preferences.getString(getString(R.string.board_size_key), getString(R.string.board_size_default));
		return Integer.valueOf(boardSize);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		LinearLayout gameBoard = (LinearLayout) findViewById(R.id.game_view);
		gameView = new GameView(this, getBoardSize());
		gameBoard.addView(gameView);

		// AdMob
		AdView mAdView = (AdView) findViewById(R.id.ad_view);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new_game:
			gameView.newGame();
			break;
		case R.id.action_info:
			showInfo();
			break;
		case R.id.action_settings:
			Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(intent);
			break;
		default:
		}
		return super.onOptionsItemSelected(item);
	}

	private void showInfo() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(R.string.info_dialog_title);
		dialogBuilder.setMessage(R.string.info_dialog_message);

		ImageView imageView = new ImageView(this);
		if (getBoardSize() == Integer.parseInt(getString(R.string.board_size_default))) {
			imageView.setImageResource(R.drawable.ic_3by3);
		} else {
			imageView.setImageResource(R.drawable.ic_4by4);
		}
		imageView.setPadding(0, 0, 0, 30);
		dialogBuilder.setView(imageView);

		dialogBuilder.setNeutralButton(R.string.info_dialog_neutral_button_text, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		dialogBuilder.show();
	}
}
