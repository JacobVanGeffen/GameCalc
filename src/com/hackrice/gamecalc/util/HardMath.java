package com.hackrice.gamecalc.util;

import java.util.HashMap;

import com.hackrice.gamecalc.parse.Evaluator;

public class HardMath {

	//precision: (0, 1], lower = more precise
	public static double integral(String eq, String dVar, double lower, double upper, double precision){
		double dx = (upper-lower)*(precision), 
				at = lower, total = 0;
		HashMap<String, Double> vars = new HashMap<String, Double>();
		while (lower<upper ? at<upper : at>upper) {
			vars.put(dVar, at);
			total += Double.parseDouble(Evaluator.mathEval(eq, vars));
			at += dx;
		}
		return total;
	}
	
	//precision: (0, oo), lower = more precise
	public static double derivative(String eq, String dVar, double dVal, double precision){
		HashMap<String, Double> vars = new HashMap<String, Double>();
		double left, right;
		vars.put(dVar, dVal-precision);
		left = Double.parseDouble(Evaluator.mathEval(eq, vars));
		vars.put(dVar, dVal+precision);
		right = Double.parseDouble(Evaluator.mathEval(eq, vars));
		return (right - left)/(2*precision);
	}
	
	/*
	 * from: {-1, 0, 1}
	 * -1 -> left
	 * 0 -> both
	 * 1 -> right 
	 */
	//precision: (0, oo), lower = more precise
	//returns NaN if non-existent
	public static double limit(String eq, String dVar, double approach, int from, double precision){
		if(from==0){
			double left = limit(eq, dVar, approach, -1, precision),
					right = limit(eq, dVar, approach, 1, precision);
			return Math.abs(right-left)<=precision?left:Double.NaN;
		}
		HashMap<String, Double> vars = new HashMap<String, Double>();
		try{
			vars.put(dVar, approach);
			return Double.parseDouble(Evaluator.mathEval(eq, vars));
		}catch(NumberFormatException e){
			vars.put(dVar, approach + from*precision);
			return Double.parseDouble(Evaluator.mathEval(eq, vars));
		}
	}
	
	public static double summation(String eq, String dVar, int start, int end){
		double total = 0;
		HashMap<String, Double> vars = new HashMap<String, Double>();
		for(; start<=end; start++){
			vars.put(dVar, (double) start);
			total += Double.parseDouble(Evaluator.mathEval(eq, vars));
		}
		return total;
	}
	
	public static double geomation(String eq, String dVar, int start, int end){
		double total = 0;
		HashMap<String, Double> vars = new HashMap<String, Double>();
		for(; start<=end; start++){
			vars.put(dVar, (double) start);
			total *= Double.parseDouble(Evaluator.mathEval(eq, vars));
		}
		return total;
	}
	
}
