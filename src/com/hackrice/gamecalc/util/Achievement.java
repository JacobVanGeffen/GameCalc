package com.hackrice.gamecalc.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.hackrice.gamecalc.MainActivity;
import com.hackrice.gamecalc.R;

public class Achievement {
	
	private static SharedPreferences prefs;
	
	private String name, des, regex;
	private int drawableId;
	private static int colors[], cAt = 0;
	
	public Achievement(String name, String description){
		this(name, description, "", -1);
	}
	
	public Achievement(String name, String description, String regex){
		this(name, description, regex, -1);
	}
	
	public Achievement(String name, String description, String regex, int drawable){
		this.name = name;
		this.des = description;
		this.regex = regex;
		drawableId = drawable;
		
	}
	
	public boolean check(String eq){
		return eq.matches(regex);
	}
	
	public void setDrawable(int id){
		drawableId = id;
	}
	
	private static void initColors(Context c) {
		colors = new int[] {
			c.getResources().getColor(android.R.color.holo_blue_light), 
			c.getResources().getColor(android.R.color.holo_green_light), 
			c.getResources().getColor(android.R.color.holo_red_light), 
		};
	}
	
	public void push(final Activity c, final ViewGroup root){
		prefs = c.getSharedPreferences("com.example.gamecaclulator.ach", Context.MODE_PRIVATE);
		Log.wtf("prefs", prefs.getAll().toString());
		if(true){//!prefs.contains(name)){
			prefs.edit().putBoolean(name, true).commit();
			final View push = getGraphicalView(c);
			root.addView(push, push.getLayoutParams());
			push.startAnimation(AnimationUtils.loadAnimation(c, R.anim.up_in));
			c.runOnUiThread(new Runnable(){
				public void run(){
					try{
						Thread.sleep(1000);
					}catch(InterruptedException e){}
					root.removeView(push);
				}
			});
		}
	}
	
	public RelativeLayout getDisplay(Context c){
		prefs = c.getSharedPreferences("com.example.gamecaclulator.ach", Context.MODE_PRIVATE);
		prefs.edit().clear().commit();
		RelativeLayout ret = getGraphicalView(c);
		if(!prefs.contains(name)){ // add blur & take out description text
			ret.findViewById(R.id.tvAchDesc).setVisibility(View.INVISIBLE);
			ImageView blur = new ImageView(c);
			blur.setBackground(c.getResources().getDrawable(R.drawable.outline_disactive));
			RelativeLayout.LayoutParams params = 
					new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			ret.addView(blur, params);
		}
		return ret;
	}
	
	private RelativeLayout getGraphicalView(Context c){
		if(colors==null)
			initColors(c);
		LayoutParams params[] = {
				new LayoutParams(LayoutParams.MATCH_PARENT, MainActivity.BASE_WIDTH),
				new LayoutParams(MainActivity.BASE_WIDTH, MainActivity.BASE_WIDTH),
				new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT),
				new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT),
		};
		RelativeLayout ret = new RelativeLayout(c);
		ImageView iv = new ImageView(c);
		TextView tv[] = {
				new TextView(c), new TextView(c)
		};
		iv.setId(R.id.ivAch);
		iv.setBackground(c.getResources().getDrawable(drawableId));
		
		tv[0].setId(R.id.tvAchName);
		tv[1].setId(R.id.tvAchDesc);
		tv[0].setText(name);
		tv[1].setText(des);
		tv[0].setTextSize(30);
		tv[1].setTextSize(15);
		tv[0].setTextColor(c.getResources().getColor(android.R.color.background_dark));
		tv[1].setTextColor(c.getResources().getColor(android.R.color.background_dark));
		
		params[1].addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		params[1].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params[2].addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		params[3].addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		params[2].addRule(RelativeLayout.RIGHT_OF, R.id.ivAch);
		params[3].addRule(RelativeLayout.RIGHT_OF, R.id.ivAch);
		params[3].addRule(RelativeLayout.BELOW, R.id.tvAchName);
		
		ret.addView(iv, params[1]);
		ret.addView(tv[0], params[2]);
		ret.addView(tv[1], params[3]);
		
		ret.setLayoutParams(params[0]);
		ret.setBackgroundColor(colors[cAt]);
		cAt++;
		cAt %= colors.length;
		return ret;
	}
	
}
