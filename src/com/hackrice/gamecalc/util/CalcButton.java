package com.hackrice.gamecalc.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hackrice.gamecalc.R;

public class CalcButton extends RelativeLayout{

	private Context context;
	private TextView tv;
	private ImageView outline, shade;
	public LayoutParams params;
	private boolean activated;
	
	public CalcButton(Context c) {
		super(c);
		init(c);
	}
	
	public CalcButton(Context c, AttributeSet attrs) {
		super(c, attrs);
		init(c);
	}
	
	public CalcButton(Context c, String text) {
		this(c);
		setText(text);
	}
	
	private void init(Context c){
		context = c;
		setupGUI();
		setActivated(false);
	}
	
	public boolean isActivated(){
		return activated;
	}
	
	public void setActivated(boolean act){
		activated = act;
		if(activated)
			shade.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		else 
			shade.setBackground(context.getResources().getDrawable(R.drawable.outline_disactive));
		tv.setTextColor(getResources().getColor(
				activated ? android.R.color.background_dark
						: android.R.color.darker_gray));
	}

	private void setupGUI(){
		outline = new ImageView(context);
		outline.setId(R.id.outline);
		outline.setBackground(context.getResources().getDrawable(R.drawable.outline));
		addView(outline, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setBackgroundColor(context.getResources().getColor(android.R.color.background_light));
		
		tv = new TextView(context);
		LayoutParams ptv = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		ptv.addRule(CENTER_IN_PARENT, TRUE);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(40);
		tv.setTextColor(getResources().getColor(android.R.color.background_dark));
		addView(tv, ptv);
		
		shade = new ImageView(context);
		addView(shade, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	public void setText(String s){
		tv.setText(s);
	}
	
	public String getText(){
		return tv.getText().toString();
	}
	
}
