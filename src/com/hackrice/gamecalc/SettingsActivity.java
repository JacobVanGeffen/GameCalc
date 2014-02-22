package com.hackrice.gamecalc;

import com.hackrice.gamecalc.util.AchievementList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ScrollView;

public class SettingsActivity extends Activity{

	public void onCreate(Bundle b){
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);
		setupGUI();
	}
	
	private void setupGUI(){
		findViewById(R.id.ibClearProgress).setOnClickListener(new Handler(this));
		((ScrollView)findViewById(R.id.svAch)).addView(AchievementList.getListDisplay(this));
	}
	
}
