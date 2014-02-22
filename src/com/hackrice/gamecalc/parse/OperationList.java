package com.hackrice.gamecalc.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hackrice.gamecalc.util.HardMath;

import android.annotation.SuppressLint;
import android.util.Log;
import de.congrace.exp4j.CustomFunction;
import de.congrace.exp4j.CustomOperator;
import de.congrace.exp4j.InvalidCustomFunctionException;

public class OperationList {
	
	@SuppressLint("UseSparseArrays")
	public static Map<Integer, String> innerEqs = new HashMap<Integer, String>();
	
	public static final String[] FUNCTS_WITH_EQS = {"int", "der", "lim", "sum", "geo"};
	
	private static CustomOperator[] ops = {

		new CustomOperator("!", true, 6, 1) {
			@Override
			protected double applyOperation(double[] values) {
				return fact(values[0]);
			}
		},

		//nCr
		new CustomOperator(">", false, 4, 2) {
			@Override
			protected double applyOperation(double[] values) {
				return fact(values[0])/(fact(values[1])*fact(values[0]-values[1]));
			}
		},

		//nPr
		new CustomOperator("<", false, 4, 2) {
			@Override
			protected double applyOperation(double[] values) {
				return fact(values[0])/fact(values[0]-values[1]);
			}
		},

		//bitshifts
		new CustomOperator("<<", false, 0, 2) {
			@Override
			protected double applyOperation(double[] values) {
				return (int)values[0] << (int)values[1];
			}
		},
		new CustomOperator(">>", false, 0, 2) {
			@Override
			protected double applyOperation(double[] values) {
				return (int)values[0] >> (int)values[1];
			}
		},
		new CustomOperator(">>>", false, 0, 2) {
			@Override
			protected double applyOperation(double[] values) {
				return (int)values[0] >>> (int)values[1];
			}
		},

		//bitwise
		new CustomOperator("&", false, -1, 2) {
			@Override
			protected double applyOperation(double[] values) {
				return (int)values[0] & (int)values[1];
			}
		},
		new CustomOperator("#", false, -2, 2) {
			@Override
			protected double applyOperation(double[] values) {
				return (int)values[0] ^ (int)values[1];
			}
		},
		new CustomOperator("|", false, -3, 2) {
			@Override
			protected double applyOperation(double[] values) {
				return (int)values[0] | (int)values[1];
			}
		},

		//store, TODO: actually store variable
		new CustomOperator("~>", false, -4, 2) {
			@Override
			protected double applyOperation(double[] values) {
				return values[0];
			}
		},
		
		//add more operations
		
	};
	
	private static double fact(double d){
		double tmp = 1d;
		int steps = 1;
		while (steps < d) {
			tmp = tmp * (++steps);
		}
		return tmp;
	}
	
	private static CustomFunction[] functs;
	
	static { 
		try {

			functs = new CustomFunction[]{
					
				new CustomFunction("sqrt", 1) {
					@Override
					public double applyFunction(double... arg) {
						return Math.sqrt(arg[0]);
					}
				},		
				
				new CustomFunction("sin", 1) {
					@Override
					public double applyFunction(double... arg) {
						return Math.sin(arg[0]);
					}
				},	
				
				new CustomFunction("cos", 1) {
					@Override
					public double applyFunction(double... arg) {
						return Math.cos(arg[0]);
					}
				},	
				
				new CustomFunction("tan", 1) {
					@Override
					public double applyFunction(double... arg) {
						return Math.tan(arg[0]);
					}
				},	
				
				new CustomFunction("cot", 1) {
					@Override
					public double applyFunction(double... arg) {
						return 1/Math.tan(arg[0]);
					}
				},	
				
				new CustomFunction("sec", 1) {
					@Override
					public double applyFunction(double... arg) {
						return 1/Math.cos(arg[0]);
					}
				},	
				
				new CustomFunction("csc", 1) {
					@Override
					public double applyFunction(double... arg) {
						return 1/Math.sin(arg[0]);
					}
				},
				
				new CustomFunction("abs", 1) {
					@Override
					public double applyFunction(double... arg) {
						return Math.abs(arg[0]);
					}
				},	
				
				new CustomFunction("ln", 1) {
					@Override
					public double applyFunction(double... arg) {
						return Math.log(arg[0]);
					}
				},	
				
				new CustomFunction("log", 1) {
					@Override
					public double applyFunction(double... arg) {
						return Math.log10(arg[0]);
					}
				},
				
				new CustomFunction("int", 3) {
					@Override
					public double applyFunction(double... arg) {
						return HardMath.integral(innerEqs.get((int)arg[0]), "x", arg[1], arg[2], 0.000000001);
					}
				},

				new CustomFunction("der", 2) {
					@Override
					public double applyFunction(double... arg) {
						return HardMath.derivative(innerEqs.get((int)arg[0]), "x", arg[1], 0.000000001);
					}
				},
				
				new CustomFunction("lim", 2) {
					@Override
					public double applyFunction(double... arg) {
						return HardMath.limit(innerEqs.get((int)arg[0]), "x", arg[1], 0, 0.000000001);
					}
				},
				
				new CustomFunction("sum", 3) {
					@Override
					public double applyFunction(double... arg) {
						return HardMath.summation(innerEqs.get((int)(arg[0])), "x", (int)arg[1], (int)arg[2]);
					}
				},
				
				new CustomFunction("geo", 3) {
					@Override
					public double applyFunction(double... arg) {
						return HardMath.geomation(innerEqs.get((int)(arg[0])), "x", (int)arg[1], (int)arg[2]);
					}
				},
				
				//add more functions
				
			};
			
		} catch (InvalidCustomFunctionException e) {
			Log.wtf("Creating Functions Error", e+"");
		}
		
	}
	
	public static ArrayList<CustomOperator> getOps(){
		ArrayList<CustomOperator> ret = new ArrayList<CustomOperator>();
		for(CustomOperator op : ops)
			ret.add(op);
		return ret;
	}
	
	public static ArrayList<CustomFunction> getFuncts(){
		ArrayList<CustomFunction> ret = new ArrayList<CustomFunction>();
		for(CustomFunction funct : functs)
			ret.add(funct);
		return ret;
	}
	
}
