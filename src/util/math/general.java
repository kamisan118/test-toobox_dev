package util.math;

import java.util.ArrayList;

public class general {
	
	public static double avgValue(ArrayList input1DAL){
		
		double avgValue = 0;
		
		for(int i = 0; i < input1DAL.size(); i++){
			avgValue = avgValue + Double.parseDouble(((String)input1DAL.get(i)));
		}
		avgValue = avgValue / input1DAL.size();
		
		return avgValue;		
	}
	
	public static double avgValue(ArrayList input2DAL, int tarCol){
		double avgValue = 0;
		
		for(int i = 0; i < input2DAL.size(); i++){
			avgValue = avgValue + Double.parseDouble(((String)((ArrayList)input2DAL.get(i)).get(tarCol)));
		}
		avgValue = avgValue / input2DAL.size();
		
		return avgValue;
	}
	
	
	public static double stdValue(ArrayList input1DAL){
		double avgValue = avgValue(input1DAL);
		double stdValue = 0;
		
		for(int i = 0; i < input1DAL.size(); i++){
			stdValue = stdValue + Math.pow((Double.parseDouble(((String)input1DAL.get(i))) - avgValue), 2);
		}
		stdValue = stdValue / (input1DAL.size() - 1);
		stdValue = Math.sqrt(stdValue);
		
		return stdValue;
	}
	
	public static double stdValue(ArrayList input2DAL, int tarCol){
		double avgValue = avgValue(input2DAL, tarCol);
		double stdValue = 0;
		
		for(int i = 0; i < input2DAL.size(); i++){
			stdValue = stdValue + Math.pow((Double.parseDouble(((String)((ArrayList)input2DAL.get(i)).get(tarCol))) - avgValue), 2);
		}
		stdValue = stdValue / (input2DAL.size() - 1);
		stdValue = Math.sqrt(stdValue);
		
		return stdValue;
	}
	
	
	public static double CV_Value(ArrayList input1DAL){
		double retValue = (stdValue(input1DAL) / avgValue(input1DAL));		
		return retValue;	
	}
	
	public static double CV_Value(ArrayList input2DAL, int tarCol){
		double retValue = (stdValue(input2DAL, tarCol) / avgValue(input2DAL, tarCol));		
		return retValue;
	}
	
}
