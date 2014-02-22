package com.hackrice.gamecalc.util;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

public class Notifications {

	public static void push(String message, Activity c) {
		notify(message, c);
	}

	private static void notify(final String unlocked, final Activity c) {
		Runnable run = new Runnable() {
			public void run() {
				Toast msg = Toast.makeText(c, unlocked
						+ " unlocked!", Toast.LENGTH_LONG);
				msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
						msg.getYOffset() / 2);
				msg.show();
			}
		};
		c.runOnUiThread(run);
	}

}
