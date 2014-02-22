package com.hackrice.gamecalc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.hackrice.gamecalc.parse.UnlockParser;
import com.hackrice.gamecalc.util.AchievementList;
import com.hackrice.gamecalc.util.CalcButton;
import com.hackrice.gamecalc.util.Notifications;

@SuppressLint("UseSparseArrays")
public class MainActivity extends Activity {

	public static MainActivity current;
	
	public static int BASE_WIDTH = 0;
	//private static boolean hasInit = false;
	public static HashMap<Integer, CalcButton> actMap;
	public static int OPS_DONE, BUTTONS_UNLOCKED;
	
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		current = this;
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		actMap = new HashMap<Integer, CalcButton>();
		prefs = getSharedPreferences("com.hackrice.gamecalc", MODE_PRIVATE);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		BASE_WIDTH = displaymetrics.widthPixels/4;
	}

	public void onResume() {
		super.onResume();
		/*if(hasInit)
			return;
		hasInit = true;*/
		setupGUI();
	}
	
	public void onPause(){
		super.onPause();
		Set<String> puts = new HashSet<String>();
		for(CalcButton b : actMap.values())
			if(b.isActivated()){
				puts.add(b.getText());
				prefs.edit().putBoolean(b.getText(), true).commit();
			}
	}
	
	public void offerActivate(String eq, String res){
		Set<String> adds = UnlockParser.checkAns(res);
		adds.addAll(UnlockParser.parseExpr(eq));
		for(final CalcButton b : actMap.values())
			if(adds.contains(b.getText())){
				if(!b.isActivated())
					Notifications.push(b.getText(), this);
				b.setActivated(true);
				prefs.edit().putBoolean(b.getText(), true).commit();
			}
	}
	
	public void pushAchievements(String eq){
		Log.wtf("Main", "PUSH");
		AchievementList.push(this, (ViewGroup)findViewById(R.id.rlOuter), eq);
	}
	
	
	
	
	
	//Messy as hell, STAY AWAY
	private void setupGUI() {
		runOnUiThread(new Runnable() {

			private String[] act = {"C", "del", "=", "1", "+", "-"};
			
			@Override
			public void run() {	
				//settings button
				ImageButton ibSettings = (ImageButton) findViewById(R.id.ibSettings);
				ibSettings.getLayoutParams().width = BASE_WIDTH/2;
				ibSettings.getLayoutParams().height = BASE_WIDTH/2;
				ibSettings.setPadding(20, 20, 20, 20);
				ibSettings.setOnClickListener(new Handler(MainActivity.this));
				
				//ken's email
				//ken.forslund@gmail.com
				
				//setup general
				CalcButton clear = addButton("C", R.id.clear, 
						(RelativeLayout) MainActivity.this.findViewById(R.id.rlInner));
				clear.params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

				int[] svIds = { R.id.add, R.id.sub, R.id.mult, R.id.div,
						R.id.exp, R.id.fact, R.id.sqrt, R.id.abs, R.id.sin,
						R.id.cos, R.id.tan, R.id.cot, R.id.sec, R.id.csc,
						R.id.ln, R.id.log, R.id.nCr, R.id.nPr, R.id.integral,
						R.id.pi, R.id.tau, R.id.e };
				String[] svStrs = { "+", "-", "*", "/", "^", "!", "√", "abs",
						"sin", "cos", "tan", "cot", "sec", "csc", "ln", "log",
						"nCr", "nPr", "∫", "π", "τ", "e" };
				ScrollView sv = new ScrollView(MainActivity.this);
				RelativeLayout rlScroll = new RelativeLayout(MainActivity.this);
				sv.addView(rlScroll);
				sv.setId(R.id.funct_bar);
				rlScroll.setOnTouchListener(new Handler(MainActivity.this));
				findViewById(R.id.ivScroll).getLayoutParams().width = BASE_WIDTH;
				findViewById(R.id.ivScroll).getLayoutParams().height = BASE_WIDTH;
				addButton(svStrs[0], svIds[0], rlScroll);
				for(int a=1; a<svIds.length; a++){
					CalcButton cb = addButton(svStrs[a], svIds[a], rlScroll);
					cb.params.addRule(RelativeLayout.BELOW, svIds[a-1]);
				}
				RelativeLayout.LayoutParams svpar = new RelativeLayout.LayoutParams(BASE_WIDTH, 4*BASE_WIDTH);
				svpar.addRule(RelativeLayout.BELOW, R.id.equals);
				svpar.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
				((ViewGroup)MainActivity.this.findViewById(R.id.rlInner)).addView(sv, svpar);

				sv.scrollTo(0, BASE_WIDTH*4);
				int[][] ids = {
						{ R.id.clear, R.id.parl, R.id.parr, R.id.equals },
						{ R.id.b7, R.id.b8, R.id.b9 },
						{ R.id.b4, R.id.b5, R.id.b6 },
						{ R.id.b1, R.id.b2, R.id.b3 },
						{ R.id.decimal, R.id.b0, R.id.del } };
				String[][] strs = { 
						{ "C", "(", ")", "=" }, 
						{ "7", "8", "9" },
						{ "4", "5", "6" }, 
						{ "1", "2", "3" },
						{ ".", "0", "del" } };
				
				for(int s=0; s<ids.length; s++)
					for (int a = 0; a < ids[s].length; a++){
						if(a==0 && s==0) continue;
						CalcButton cb = addButton(strs[s][a], 
								ids[s][a], (RelativeLayout) MainActivity.this.findViewById(R.id.rlInner));
						if(s!=0)
							cb.params.addRule(RelativeLayout.BELOW, ids[s-1][a]);
						if(a==0)
							cb.params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
						else
							cb.params.addRule(RelativeLayout.RIGHT_OF, ids[s][a-1]);
					}
				
			}

			private CalcButton addButton(String s, int id, ViewGroup parent) {
				CalcButton button = new CalcButton(MainActivity.this, s);
				button.params = new RelativeLayout.LayoutParams(BASE_WIDTH,
						BASE_WIDTH);
				button.setId(id);
				button.setOnClickListener(new Handler(MainActivity.this));
				parent.addView(button, button.params);
				
				for(int a=0; a<act.length; a++)
					if(act[a].equals(s))
						button.setActivated(true);
				
				Set<String> unlocks = prefs.getAll().keySet();
				if(unlocks.contains(button.getText()))
					button.setActivated(true);
				
				actMap.put(id, button);
				return button;
			}
			
		});
	}

}
