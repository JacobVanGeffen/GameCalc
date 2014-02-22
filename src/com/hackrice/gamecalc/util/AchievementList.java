package com.hackrice.gamecalc.util;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hackrice.gamecalc.R;

public class AchievementList {

	private static ArrayList<Achievement> list;
	
	private static String[] 
			names = {"smiley"}, 
			reg = {".*"}, 
			des = {"smile :)"};
	private static int[] 
			draws = {R.drawable.smiley};
	
	static {
		list = new ArrayList<Achievement>();
		for(int a=0; a<names.length; a++)
			list.add(new Achievement(names[a], des[a], reg[a], draws[a]));
	}
	
	public static ArrayList<Achievement> get(){
		return list;
	}
	
	public static void push(Activity c, ViewGroup root, String eq){
		Log.wtf("List", list+"");
		for(Achievement a : list)
			if(a.check(eq)){
				Log.wtf("A works", "ASFDASDF");
				a.push(c, root);
			}
	}
	
	public static View getListDisplay(Context c){
		LinearLayout ret = new LinearLayout(c);
		for(Achievement a : list)
			ret.addView(a.getDisplay(c));
		return ret;
	}
	
}
