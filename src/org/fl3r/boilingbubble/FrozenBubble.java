/* Boiling Bubble
 * Copyright (c) 2012-2013 Armando "FL3R" Fiore
 * This code is distributed under the GNU General Public License
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.fl3r.boilingbubble;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.app.AlertDialog;

import org.fl3r.boilingbubble.GameView;
import org.fl3r.boilingbubble.R;
import org.fl3r.boilingbubble.GameView.GameThread;

public class FrozenBubble extends Activity {
	public final static int SOUND_WON = 0;
	public final static int SOUND_LOST = 1;
	public final static int SOUND_LAUNCH = 2;
	public final static int SOUND_DESTROY = 3;
	public final static int SOUND_REBOUND = 4;
	public final static int SOUND_STICK = 5;
	public final static int SOUND_HURRY = 6;
	public final static int SOUND_NEWROOT = 7;
	public final static int SOUND_NOH = 8;
	public final static int NUM_SOUNDS = 9;

	public final static int GAME_NORMAL = 0;
	public final static int GAME_COLORBLIND = 1;

	// public final static int MENU_COLORBLIND_MODE_ON = 1;
	// public final static int MENU_COLORBLIND_MODE_OFF = 2;
	public final static int MENU_FULLSCREEN_ON = 3;
	public final static int MENU_FULLSCREEN_OFF = 4;
	public final static int MENU_SOUND_ON = 5;
	public final static int MENU_SOUND_OFF = 6;
	public final static int MENU_DONT_RUSH_ME = 7;
	public final static int MENU_RUSH_ME = 8;
	public final static int MENU_NEW_GAME = 9;
	public final static int MENU_ABOUT = 10;
	public final static int MENU_EDITOR = 11;
	public final static int MENU_TOUCHSCREEN_AIM_THEN_SHOOT = 12;
	public final static int MENU_TOUCHSCREEN_POINT_TO_SHOOT = 13;

	public final static String PREFS_NAME = "frozenbubble";

	private static int gameMode = GAME_NORMAL;
	private static boolean soundOn = true;
	private static boolean dontRushMe = false;
	private static boolean aimThenShoot = false;

	private boolean fullscreen = true;

	private GameThread mGameThread;
	private GameView mGameView;

	private static final String EDITORACTION = "org.fl3r.boilingbubble.GAME";
	private static final int MENU_CONDIVIDI = 0;
	private boolean activityCustomStarted = false;

	// fl3r: menu

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// menu.add(0, MENU_COLORBLIND_MODE_ON, 0,
		// R.string.menu_colorblind_mode_on);

		// menu.add(0, MENU_COLORBLIND_MODE_OFF, 0,
		// R.string.menu_colorblind_mode_off);

		menu.add(0, MENU_FULLSCREEN_ON, 0, R.string.menu_fullscreen_on)
				.setIcon(R.drawable.menu_fullscreen_off);
		menu.add(0, MENU_FULLSCREEN_OFF, 0, R.string.menu_fullscreen_off)
				.setIcon(R.drawable.menu_fullscreen_on);

		menu.add(0, MENU_SOUND_ON, 0, R.string.menu_sound_on).setIcon(
				R.drawable.menu_sound_off);
		menu.add(0, MENU_SOUND_OFF, 0, R.string.menu_sound_off).setIcon(
				R.drawable.menu_sound_on);

		menu.add(0, MENU_TOUCHSCREEN_AIM_THEN_SHOOT, 0,
				R.string.menu_touchscreen_aim_then_shoot).setIcon(
				R.drawable.menu_sparo_al_tocco);
		menu.add(0, MENU_TOUCHSCREEN_POINT_TO_SHOOT, 0,
				R.string.menu_touchscreen_point_to_shoot).setIcon(
				R.drawable.menu_mira_e_poi_spara);

		menu.add(0, MENU_CONDIVIDI, 0, R.string.menu_condividi).setIcon(
				R.drawable.menu_condividi);

		/*
		 * menu.add(0, MENU_DONT_RUSH_ME, 0,
		 * R.string.menu_dont_rush_me).setIcon( R.drawable.menu_vibrazione_on);
		 * menu.add(0, MENU_RUSH_ME, 0, R.string.menu_rush_me).setIcon(
		 * R.drawable.menu_vibrazione_off);
		 */menu.add(0, MENU_ABOUT, 0, R.string.menu_about).setIcon(
				R.drawable.menu_informazioni);

		menu.add(0, MENU_NEW_GAME, 0, R.string.menu_new_game).setIcon(
				R.drawable.menu_nuova_partita);

		// menu.add(0, MENU_EDITOR, 0, R.string.menu_editor);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(MENU_SOUND_ON).setVisible(!getSoundOn());
		menu.findItem(MENU_SOUND_OFF).setVisible(getSoundOn());
		// menu.findItem(MENU_COLORBLIND_MODE_ON).setVisible(
		// getMode() == GAME_NORMAL);
		// menu.findItem(MENU_COLORBLIND_MODE_OFF).setVisible(
		// getMode() != GAME_NORMAL);
		menu.findItem(MENU_FULLSCREEN_ON).setVisible(!fullscreen);
		menu.findItem(MENU_FULLSCREEN_OFF).setVisible(fullscreen);
		menu.findItem(MENU_TOUCHSCREEN_AIM_THEN_SHOOT).setVisible(
				!getAimThenShoot());
		menu.findItem(MENU_TOUCHSCREEN_POINT_TO_SHOOT).setVisible(
				getAimThenShoot());
		// menu.findItem(MENU_DONT_RUSH_ME).setVisible(!getDontRushMe());
		// menu.findItem(MENU_RUSH_ME).setVisible(getDontRushMe());
		return true;
	}

	// fl3r: ripristino del salvataggio con popup di conferma.

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_NEW_GAME:

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle(R.string.ripristino);
			alertDialog.setMessage(R.string.eliminare_i_salvataggi);
			alertDialog.setIcon(R.drawable.riciclo);

			alertDialog.setCancelable(false);
			alertDialog.setPositiveButton(R.string.si,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {

							mGameThread.newGame();

							Context context = getApplicationContext();
							int text = R.string.eliminare_i_salvataggi_si;
							int duration = Toast.LENGTH_LONG;

							Toast toast = Toast.makeText(context, text,
									duration);
							toast.show();

						}
					});

			alertDialog.setNegativeButton(R.string.no,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {

							Context context = getApplicationContext();
							int text = R.string.eliminare_i_salvataggi_no;
							int duration = Toast.LENGTH_LONG;

							Toast toast = Toast.makeText(context, text,
									duration);
							toast.show();
						}
					});
			AlertDialog alert = alertDialog.create();
			alert.show();

			return true;

			// fl3r.

			// fl3r: vecchio pulsante per daltonici.

			// case MENU_COLORBLIND_MODE_ON:
			// setMode(GAME_COLORBLIND);
			// return true;
			// case MENU_COLORBLIND_MODE_OFF:
			// setMode(GAME_NORMAL);
			// return true;

			// fl3r.

			// fl3r: aggiunti i vari toast

		case MENU_FULLSCREEN_ON:
			fullscreen = true;
			setFullscreen();

			{
				Context context = getApplicationContext();
				int text = R.string.menu_fullscreen_on2;
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}

			return true;
		case MENU_FULLSCREEN_OFF:
			fullscreen = false;
			setFullscreen();

			{
				Context context = getApplicationContext();
				int text = R.string.menu_fullscreen_off2;
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}

			return true;
		case MENU_SOUND_ON:
			setSoundOn(true);
			{
				Context context = getApplicationContext();
				int text = R.string.menu_sound_on2;
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			return true;
		case MENU_SOUND_OFF:
			setSoundOn(false);
			{
				Context context = getApplicationContext();
				int text = R.string.menu_sound_off2;
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			return true;

			// fl3r: menu info

		case MENU_ABOUT:

			AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
			alertDialog2.setTitle(R.string.info_titolo);
			alertDialog2.setMessage(R.string.info_info);
			alertDialog2.setIcon(R.drawable.info_love);

			alertDialog2.setCancelable(true);
			alertDialog2.setPositiveButton(R.string.info_ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
						}
					});

			AlertDialog alert2 = alertDialog2.create();
			alert2.show();

			return true;

		case MENU_TOUCHSCREEN_AIM_THEN_SHOOT:
			setAimThenShoot(true);
			{
				Context context = getApplicationContext();
				int text = R.string.menu_touchscreen_aim_then_shoot2;
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}

			return true;
		case MENU_TOUCHSCREEN_POINT_TO_SHOOT:
			setAimThenShoot(false);
			{
				Context context = getApplicationContext();
				int text = R.string.menu_touchscreen_point_to_shoot2;
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			return true;

			/*
			 * case MENU_DONT_RUSH_ME: setDontRushMe(true); { Context context =
			 * getApplicationContext(); int text = R.string.menu_dont_rush_me2;
			 * int duration = Toast.LENGTH_SHORT;
			 * 
			 * Toast toast = Toast.makeText(context, text, duration);
			 * toast.show(); } return true; case MENU_RUSH_ME:
			 * setDontRushMe(false); { Context context =
			 * getApplicationContext(); int text = R.string.menu_rush_me2; int
			 * duration = Toast.LENGTH_SHORT;
			 * 
			 * Toast toast = Toast.makeText(context, text, duration);
			 * toast.show(); } return true;
			 */

		case MENU_CONDIVIDI:
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("text/plain");
			// emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
			// getResources().getString(R.string.recommendation_subject));
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					getResources().getString(R.string.condividi_testo));
			startActivity(emailIntent);
			return true;

		case MENU_EDITOR:
			startEditor();
			return true;
		}
		return false;
	}

	private void setFullscreen() {
		if (fullscreen) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		mGameView.requestLayout();
	}

	// fl3r: informazioni

	public void informazioni() {

	}

	public synchronized static void setMode(int newMode) {
		gameMode = newMode;
	}

	public synchronized static int getMode() {
		return gameMode;
	}

	public synchronized static boolean getSoundOn() {
		return soundOn;
	}

	public synchronized static void setSoundOn(boolean so) {
		soundOn = so;
	}

	public synchronized static boolean getAimThenShoot() {
		return aimThenShoot;
	}

	public synchronized static void setAimThenShoot(boolean ats) {
		aimThenShoot = ats;
	}

	public synchronized static boolean getDontRushMe() {
		return dontRushMe;
	}

	public synchronized static void setDontRushMe(boolean dont) {
		dontRushMe = dont;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			// Log.i("frozen-bubble", "FrozenBubble.onCreate(...)");
		} else {
			// Log.i("frozen-bubble", "FrozenBubble.onCreate(null)");
		}
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Allow editor functionalities.
		Intent i = getIntent();
		if (null == i || null == i.getExtras()
				|| !i.getExtras().containsKey("levels")) {
			// Default intent.
			activityCustomStarted = false;
			setContentView(R.layout.main);
			mGameView = (GameView) findViewById(R.id.game);
		} else {
			// Get custom level last played.
			SharedPreferences sp = getSharedPreferences(
					FrozenBubble.PREFS_NAME, Context.MODE_PRIVATE);
			int startingLevel = sp.getInt("levelCustom", 0);
			int startingLevelIntent = i.getIntExtra("startingLevel", -2);
			startingLevel = (startingLevelIntent == -2) ? startingLevel
					: startingLevelIntent;
			activityCustomStarted = true;
			mGameView = new GameView(this,
					i.getExtras().getByteArray("levels"), startingLevel);
			setContentView(mGameView);
		}

		mGameThread = mGameView.getThread();

		if (savedInstanceState != null) {
			mGameThread.restoreState(savedInstanceState);
		}
		mGameView.requestFocus();
		setFullscreen();
	}

	/**
	 * Invoked when the Activity loses user focus.
	 */
	@Override
	protected void onPause() {
		// Log.i("frozen-bubble", "FrozenBubble.onPause()");
		super.onPause();
		mGameView.getThread().pause();
		// Allow editor functionalities.
		Intent i = getIntent();
		// If I didn't run game from editor, save last played level.
		if (null == i || !activityCustomStarted) {
			SharedPreferences sp = getSharedPreferences(PREFS_NAME,
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putInt("level", mGameThread.getCurrentLevelIndex());
			editor.commit();
		} else {
			// Editor's intent is running.
			SharedPreferences sp = getSharedPreferences(PREFS_NAME,
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putInt("levelCustom", mGameThread.getCurrentLevelIndex());
			editor.commit();
		}
	}

	@Override
	protected void onStop() {
		// Log.i("frozen-bubble", "FrozenBubble.onStop()");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// Log.i("frozen-bubble", "FrozenBubble.onDestroy()");
		super.onDestroy();
		if (mGameView != null) {
			mGameView.cleanUp();
		}
		mGameView = null;
		mGameThread = null;
	}

	/**
	 * Notification that something is about to happen, to give the Activity a
	 * chance to save state.
	 * 
	 * @param outState
	 *            a Bundle into which this Activity should save its state
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Log.i("frozen-bubble", "FrozenBubble.onSaveInstanceState()");
		// Just have the View's thread save its state into our Bundle.
		super.onSaveInstanceState(outState);
		mGameThread.saveState(outState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		if (null != intent && EDITORACTION.equals(intent.getAction())) {
			if (!activityCustomStarted) {
				activityCustomStarted = true;

				// Get custom level last played.
				SharedPreferences sp = getSharedPreferences(
						FrozenBubble.PREFS_NAME, Context.MODE_PRIVATE);
				int startingLevel = sp.getInt("levelCustom", 0);
				int startingLevelIntent = intent.getIntExtra("startingLevel",
						-2);
				startingLevel = (startingLevelIntent == -2) ? startingLevel
						: startingLevelIntent;

				mGameView = null;
				mGameView = new GameView(this, intent.getExtras().getByteArray(
						"levels"), startingLevel);
				setContentView(mGameView);
				mGameThread = mGameView.getThread();
				mGameThread.newGame();
				mGameView.requestFocus();
				setFullscreen();
			}
		}
	}

	// Starts editor / market with editor's download.
	private void startEditor() {
		Intent i = new Intent();
		// First try to run the plus version of Editor.
		i.setClassName("sk.halmi.fbeditplus",
				"sk.halmi.fbeditplus.EditorActivity");
		try {
			startActivity(i);
			finish();
		} catch (ActivityNotFoundException e) {
			// If not found, try to run the normal version.
			i.setClassName("sk.halmi.fbedit", "sk.halmi.fbedit.EditorActivity");
			try {
				startActivity(i);
				finish();
			} catch (ActivityNotFoundException ex) {
				// If user doesnt have Frozen Bubble Editor take him to market.
				try {
					Toast.makeText(getApplicationContext(),
							R.string.install_editor, 1000).show();
					i = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("market://search?q=frozen bubble level editor"));
					startActivity(i);
				} catch (Exception exc) {
					// Damn you don't have market?
					Toast.makeText(getApplicationContext(),
							R.string.market_missing, 1000).show();
				}
			}
		}
	}
}
