package com.hackrice.gamecalc.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.util.Log;

//NOT CURRENTLY BEING USED

@SuppressLint("UseSparseArrays")
public class UnlockCode {
	
	private static int HASHCODE = Integer.MIN_VALUE;

	public ArrayList<String> matches, matchesResult;
	private int hash;
	
	public UnlockCode(ArrayList<String> regexes){
		this();
		for(String s : regexes)
			matches.add(s);
	}
	
	public UnlockCode(){
		matchesResult = new ArrayList<String>();
		matches = new ArrayList<String>();
		hash = HASHCODE++;
	}
	
	public UnlockCode add(String regex){
		matches.add(regex);
		return this;
	}
	
	public UnlockCode addResult(String regex){
		matchesResult.add(regex);
		return this;
	}
	
	//attempt to match w/ each combination of removed parenthesis
	public boolean match(String eq){
		if(matchIter(eq, null, -1))
			return true;
		
		TreeMap<Integer, Integer> pars = new TreeMap<Integer, Integer>();
		Stack<Integer> left = new Stack<Integer>();
		for(int a=0; a<eq.length(); a++){
			if(eq.charAt(a)=='(')
				left.push(a);
			else if (eq.charAt(a)==')')
				pars.put(left.pop(), a);
		}
		
		for(Map.Entry<Integer, Integer> e : pars.entrySet())
			if (matchIter(eq.substring(0, e.getKey())
					+ eq.substring(e.getKey() + 1, e.getValue())
					+ eq.substring(e.getValue() + 1), 
					pars, -1))
				return true;
		return false;
	}
	
	private boolean matchIter(String eq, TreeMap<Integer, Integer> pars, int pastKey){
		Log.wtf("MatchIter", eq);
		Integer next = null;
		if(pars!=null && pastKey==-1)
			next = pars.firstKey();
		else if(pars!=null)
			next = pars.ceilingKey(pastKey+1);
		if(next!=null)
			return matchIter(eq, pars, next)
					|| matchIter(removePair(eq, next, pars.get(next)), pars, next);
		for(String s : matches)
			if(eq.matches(s))
				return true;
		return false;
	}
	
	private String removePair(String s, int left, int right){
		return s.substring(0, left)
		+ s.substring(left + 1, right)
		+ s.substring(right + 1);
	}
	
	public boolean matchResult(String res){
		for(String s : matchesResult)
			if(res.matches(s))
				return true;
		return false;
	}

	@Override
	public int hashCode() {
		return hash;
	}
	
}
