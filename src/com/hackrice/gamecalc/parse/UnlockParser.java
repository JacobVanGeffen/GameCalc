package com.hackrice.gamecalc.parse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;

public class UnlockParser {

	//TODO: have another map for series regexes (use for series and factorial)
	//TODO: have another map for achievements
	private static HashMap<String,String> regexes = new HashMap<String,String>();
	private static HashMap<String,String> anses = new HashMap<String,String>();
		
	//** negative = -, subtract = \\-
	//if one regex unlocks multiple buttons, seperate button titles w/ a comma (,)
	static{
		regexes.put("\\d+\\D+\\d+\\D+\\d+", "(,)");
		regexes.put("((\\d+)\\+(\\2))(\\+\\2)*", "*"); // mult
		regexes.put("((\\d+)\\*(\\2))(\\*\\2)*", "^"); // exp
		regexes.put("(\\d+)\\^-\\d+", "/"); // div
		regexes.put("1/cos", "sec");
		regexes.put("1/tan", "cot");
		regexes.put("1/sin", "csc");
		regexes.put("10\\^", "log");
		regexes.put("e\\^", "ln");
		regexes.put("sin\\((.+)\\)/cos\\(\\1\\)", "tan");
		regexes.put("cos\\((.+)\\)/sin\\(\\1\\)", "cot");
		regexes.put("(((((((9\\*)?8\\*)?7\\*)?6\\*)?5\\*)?4\\*)?)?3\\*2\\*1", "!"); // fact
		regexes.put("1\\*2\\*3((((((\\*4)?\\*5)?\\*6)?\\*7)?\\*8)?\\*9)?", "!"); // fact
		regexes.put("\\d+\\^0?\\.5", "√"); // sqrt
		regexes.put("\\d+\\^\\(1/2\\)", "√"); // sqrt
		regexes.put("(\\d+)\\/((√\\(\\1\\^2\\+\\d+\\^2\\))|(√\\(\\d+\\^2\\+\\1\\^2\\)))", "sin,cos");
		regexes.put("(\\d+)!\\/(\\(\\1\\-\\d+\\)!)", "nPr");
		regexes.put("√\\(\\-?\\d+\\^2\\)", "abs");
		regexes.put("(\\d+)!\\/(\\(\\(\\1\\-(\\d+)\\)!\\2\\))", "nCr");
		regexes.put("(\\d+)!\\/(\\((\\d+)!\\*\\(\\1\\-\\3\\)!\\))", "nCr");
		anses.put("\\d+\\.\\d+", ".,/"); // decimal
		anses.put("3\\.1415\\d*", "π"); // pi
		anses.put("2\\.7182\\d*", "e"); // e
		anses.put("6\\.2831\\d*", "τ"); // tau
		anses.put("0", "0"); // digits
		anses.put("1", "1"); // 
		anses.put("2", "2"); // 
		anses.put("3", "3"); // 
		anses.put("4", "4"); // 
		anses.put("5", "5"); // 
		anses.put("6", "6"); // 
		anses.put("7", "7"); // 
		anses.put("8", "8"); // 
		anses.put("9", "9"); // 
	}
	
	//returns newly unlocked symbols based on answer
	public static Set<String> checkAns(String ans){
		Set<String> set = new HashSet<String>();

		for(String key : anses.keySet()){
			if(ans.matches(key))
				for(String put : anses.get(key).split("\\,"))
					set.add(put);
		}
		return set;
	}
	
	// returns newly unlocked symbols based on equation
	public static Set<String> parseExpr(String eq){
		Set<String> set = new HashSet<String>();

		for(String key : regexes.keySet()){
			if(eq.matches(addAmbPar(key)))
				for(String put : regexes.get(key).split(","))
					set.add(put);
		}
		return set;
	}
	
	// Adds possibility of Ambiguous Parenthesis
	// e.g. allows √((-5)^2) to unlock absolute value
	private static String addAmbPar(String regex){
		String ret = regex.replaceAll(
				"((?<!\\\\)(-\\??))?(\\\\)?[0-9a-zπτ.]+(\\\\\\.[0-9]+)?[+*?]?",
				"\\\\\\(*$0\\\\\\)*");
		Log.wtf("SAFDASF", ret);
		return ret;
	}
	
}
