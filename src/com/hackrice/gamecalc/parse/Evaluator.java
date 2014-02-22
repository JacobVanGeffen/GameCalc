package com.hackrice.gamecalc.parse;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.util.Log;

import com.hackrice.gamecalc.Handler;
import com.hackrice.gamecalc.MainActivity;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

@SuppressLint("DefaultLocale")
public class Evaluator {	
	//actual
    private static String pival = "("+Math.PI+")";
    private static String tauval = "("+2*Math.PI+")"; 
    private static String eulerval = "("+Math.E+")";
    
    public static String mathEval(String expr){
    	return mathEval(expr, new HashMap<String, Double>());
    }
    
	public static String mathEval(String expr, Map<String, Double> vars) {
		
		try{
			if(expr.length()==0)
				return "";
		}catch(Exception e){
			return "";
		}
		
		expr = expr.replaceAll("π", pival)
				.replaceAll("(?<!s)e", eulerval)
				.replaceAll("τ", tauval)
				.replaceAll("√", "sqrt")
				.replaceAll("(\\d+)\\!", "($1!)")
                .replaceAll("(?<!\\d)\\.", "0.")
                .replaceAll("nCr", ">")
                .replaceAll("nPr", "<")
                .replaceAll("∫", "int")
                .replaceAll("((?<=\\))\\s*(?=[0-9\\(]))|((?<=[0-9\\)])\\s*(?=\\())", "*");
		
		expr = setInnerEqs(expr);

		Log.wtf("Expression", expr);
		
		Calculable calc = null;
		try {
			calc = new ExpressionBuilder(expr).withOperations(OperationList.getOps()).withCustomFunctions(OperationList.getFuncts())
					.withVariables(vars).build();
		} catch (UnknownFunctionException e) {
			Log.wtf("Calc Error", e+"");
		} catch (UnparsableExpressionException e) {
			Log.wtf("Calc Error", e+"");
		}
		String ret = (calc.calculate()+"").replaceAll("\\.0(?!\\d)", "");
		return ret;
	}
	
	private static String setInnerEqs(String eq){
		OperationList.innerEqs.clear();
		int key = 0;
		for(String funct : OperationList.FUNCTS_WITH_EQS){
			int prev = -1;
			while((prev=eq.indexOf(funct, prev+1))>-1)
				OperationList.innerEqs.put(key++, getInnerEq(eq, prev+funct.length()));
		}
		for(Integer k : OperationList.innerEqs.keySet())
			eq = eq.replace(OperationList.innerEqs.get(k), k+"");
		return eq;
	}
	
	private static String getInnerEq(String eq, int i){
		int start = ++i;
		int pars = 0;
		while(pars!=0 || eq.charAt(i)!=','){
			if(eq.charAt(i)=='(')
				pars++;
			else if(eq.charAt(i)==')')
				pars--;
			i++;
		}
		return eq.substring(start, i);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//old parser
	public static String eval(String eq){
		boolean error = false;
		String orig = eq;
		String res = eq;
		try{
			res = replace(res);
			res = "("+res+")";
			res = evalGen(res, 0, res.length()-1);
			res = res.replaceAll("\\.0(?!\\d+)", "").replaceAll("(?<!(\\d|\\.))(0+)(?=\\d+)", "");
		}catch(Exception e){
			Log.wtf("Error", "Eval "+e);
			error = true;
		}finally{
			if(error)
				return "Error";
			MainActivity.current.offerActivate(orig, res);
		}
		return res;
	}
	
	private static String replace(String eq){
		return eq.replaceAll("\\)\\(", ")*(")
				.replaceAll("(?<!s)e", Math.E+"")
				.replaceAll("π", Math.PI+"")
				.replaceAll("τ", 2*Math.PI+"")
				.replaceAll("\\/√(\\(.+\\))", "*($1)^-.5")
				.replaceAll("√(\\(.+\\))", "($1)^.5")
				//.replaceAll("Infinity", Double.MAX_VALUE+"")
				.replaceAll("(\\d+(\\.\\d+)?)\\-", "$1+-")
				.replaceAll("/(\\d+(\\.\\d+)?\\!?)", "*($1)^-1")
				.replaceAll("/(\\(\\d+.*\\)\\!?)", "*($1)^-1")
				.replaceAll("\\/([a-z]+\\(.+\\))", "*($1)^-1");
				//.replaceAll("\\d+(\\.\\d+)?\\!", "($0)")
				//.replaceAll("\\(.+\\)\\!", "($0)");
	}
	
	private static String evalGen(String eq, int start, int end) throws Exception{
		for(int a=0; a<Handler.functStrs.length; a++){
			String s = Handler.functStrs[a];
			while(eq.substring(start, end+1).indexOf(s)!=-1){
				int diff = eq.length();
				eq = evalFunct(eq, a, eq.substring(start, end+1).indexOf(s)+start);
				diff -= eq.length();
				end -= diff;
			}
		}
		//while(!eq.substring(start, end+1).matches("\\(?\\d+(\\.\\d+(E\\d+)?)?\\)?")){
			//int diff = eq.length();
			eq = evalNoFunct(eq, start, end);
			//diff -= eq.length();
			//end -= diff;
		//}
		return eq;
	}
	
	private static String evalFunct(String eq, int fi, int loc) throws Exception{
		Log.wtf("Loc", loc+"");
		int left = -1, right = -1;
		left = loc+Handler.functStrs[fi].length();
		Stack<Integer> pars = new Stack<Integer>();
		pars.push(left);
		right = left+1;
		while(pars.size()>0){
			if(eq.charAt(right)=='(')
				pars.push(right);
			else if(eq.charAt(right)==')')
				pars.pop();
			right++;
		}
		right--;
		int diff = eq.length();
		Log.wtf("Gen "+left+" "+right, eq);
		eq = evalGen(eq, left, right);
		diff -= eq.length();
		right -= diff;
		String ans = eq.substring(left, right+1);
		double ansRes = Double.parseDouble(ans);
		switch(fi){
		case 0: ans = Math.pow(ansRes, 0.5)+""; break;
		case 1: ans = Math.sin(ansRes)+""; break;
		case 2: ans = Math.cos(ansRes)+""; break;
		case 3: ans = Math.tan(ansRes)+""; break;
		case 4: ans = 1/Math.tan(ansRes)+""; break;
		case 5: ans = 1/Math.cos(ansRes)+""; break;
		case 6: ans = 1/Math.sin(ansRes)+""; break;
		case 7: /* do integral */ break;
		case 8: ans = Math.log(ansRes)+""; break;
		case 9: ans = Math.log10(ansRes)+""; break;
		case 10: ans = Math.abs(ansRes)+""; break;
		}
		Log.wtf("Ans",eq.substring(0, left-Handler.functStrs[fi].length())+ans+eq.substring(right+1));
		return eq.substring(0, left-Handler.functStrs[fi].length())+ans+eq.substring(right+1);
	}
	
	private static String evalNoFunct(String eq, int start, int end) throws Exception{
		Stack<Integer> left = new Stack<Integer>();
		while(eq.substring(start, end+1).contains("(")){
			for(int a=start; a<=end; a++){
				if(eq.charAt(a)=='(')
					left.push(a);
				else if(eq.charAt(a)==')'){
					int li = left.pop();
					Log.wtf("points", li+" "+eq+" "+a);
					int diff = eq.length();
					Log.wtf("Bounds", start+" "+end);
					eq = evalNoPar(eq, li, a);
					diff -= eq.length();
					end-=diff;
					left.clear();
					break;
				}
			}
		}
		Log.wtf("No Funct", "Out here too");
		return eq;
	}
	
	private static String evalNoPar(String eq, int start, int end) throws Exception {
		String[] ops = {"!", "nCr", "nPr", "^", "*", "+"};
		for(String s : ops)
			//for(int a=start; a<=end && a<eq.length(); a++)
				while(eq.substring(start, end+1).indexOf(s)!=-1){
					String basic = evalBasicFunct(eq, eq.substring(start, end+1).indexOf(s)+start);
					int diff = eq.length();
					Log.wtf("Basic", basic);
					eq = basic;
					diff -= eq.length();
					end -= diff;
				}
		Log.wtf("Basic", "Made it out");
		/*end = start+1;
		while(eq.charAt(end++)!=')');
		end--;*/
		return eq.substring(0, start)+eq.substring(start+1, end)+eq.substring(end+1);
	}
	
	private static String evalBasicFunct(String eq, int index) throws Exception {
		int lbar = index, rbar = index;
		if(eq.charAt(index)=='n')
			rbar+=2;
		try{
			while(Character.isDigit(eq.charAt(--lbar)) || (eq.charAt(lbar)=='.') 
					|| (eq.charAt(lbar)=='-') || (eq.charAt(rbar)=='E'));
		}catch(Exception e){lbar = -1;}
		try{
			while(Character.isDigit(eq.charAt(++rbar)) || (eq.charAt(rbar)=='.') 
					|| (eq.charAt(rbar)=='-') || (eq.charAt(rbar)=='E'));
		}catch(Exception e){rbar = eq.length();}
		lbar++; 
		Log.wtf("l "+lbar, "r "+rbar);
		double a = 0, b=0;
		a = Double.parseDouble(eq.substring(lbar, index));
		try{
			b = Double.parseDouble(eq.substring(
					eq.charAt(index) == 'n' ? index + 3 : index + 1, rbar));
		}catch(Exception e){
			if(eq.charAt(index)!='!')
				throw e;
		}
		Log.wtf(a+"", b+" "+eq.charAt(index));
		switch(eq.charAt(index)){
		case '!': return eq.substring(0, lbar)+fact((int)a)+eq.substring(rbar);
		case '/': return eq.substring(0, lbar)+a/b+eq.substring(rbar);
		case '^': return eq.substring(0, lbar)+Math.pow(a, b)+eq.substring(rbar);
		case '*': return eq.substring(0, lbar)+a*b+eq.substring(rbar);
		case '+': return eq.substring(0, lbar)+(a+b)+eq.substring(rbar);
		case '-': return eq.substring(0, lbar)+(a-b)+eq.substring(rbar);
		case 'n':
			double answer = fact((int)a)/fact((int)(a-b));
			if(eq.charAt(index+1)=='C')
				answer /= fact((int)b);
			return eq.substring(0, lbar)+answer+eq.substring(rbar);
		}
		return eq;
	}
	
	private static double fact(int n){
		if(n<2)
			return 1;
		return n*fact(n-1);
	}
	
}
