package preproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import StringOp.myLCS;

import util.LabVIEWtick2CSharp;
import fsOp.FileTraversal_manager;
import fsOp.fileRead;
import fsOp.fileWrite;

public class preproc_manager_IGT {

	
	
	public static String QChoiceTransABCD2DIG(String in){
		String out;
		
		if (in.equals("A"))
			out = "1";
		else if (in.equals("B"))
			out = "2";	
		else if (in.equals("C"))
			out = "3";
		else if (in.equals("D"))
			out = "4";
		else
			out = "N/A";
		
		return out; 
	}
	
	public static void comb_outputGen(){
		
		final String inputDir = "D:/ALL_Users/pmli/analysis WK/_raw data (new)";
		final String schedulePath = "D:/ALL_Users/pmli/analysis WK/_raw data (new)/Schedule.csv"; 
		
		int Subject;
		ArrayList filelist;
		ArrayList output = new ArrayList();
		ArrayList schedule = fileRead.get2D_arraylist_string_from_file(schedulePath, ",");
		ArrayList result;
		
		FileTraversal_manager fm = new FileTraversal_manager();						
		
		try {
			fm.traverse(new File(inputDir));

			Subject = 0;
			for (int i = 0; i < fm.traverse_log_table_file.size(); i++){
				
				if (((File)fm.traverse_log_table_file.get(i)).getName().equals("results.csv")){
					Subject++;					
					result = fileRead.get2D_arraylist_string_from_file(((File)fm.traverse_log_table_file.get(i)).getCanonicalPath(), ",");
					single_outputGen(result, schedule, Subject, output);					
				}
				
			}
			
		

			
			fileWrite.write_2d_arraylist_string2file("comb_Result.csv", output, true); // new file
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
			
	
	public static void single_outputGen(ArrayList result, ArrayList schedule, int Subject, ArrayList allCombResult){
		
		float beta = (float) 0.2;
		
		//outputPath = "output.csv";
		//ArrayList schedule = fileRead.get2D_arraylist_string_from_file("Schedule.csv", ",");
		//ArrayList result = fileRead.get2D_arraylist_string_from_file("results.csv", ",");
		//ArrayList output = new ArrayList();
		ArrayList output = allCombResult;
		
		ArrayList outputElement;
		/*
		 0: Subject
		 1: Trial# 
		 2. act of MaxQ 
		 3: Choice 
		 4: R
		 5. Total 
		 6: Q1 
		 7: Q2 
		 8: Q3 
		 9: Q4
		 */
		
		float defaultValue = -99999;
		
		int trial = 0;
		int actMaxQ = (int) defaultValue; float MaxQ = defaultValue;
		String choice = "";
		int R = (int) defaultValue;
		int total = 2000;
		float[] Q = {defaultValue, 0, 0, 0, 0};
		int[] scheduleCounter = {(int) defaultValue, 0, 0, 0, 0};
		
				
		//Trial0;
		outputElement = new ArrayList();
		outputElement.add(String.valueOf(Subject));
		outputElement.add(String.valueOf(trial));
		outputElement.add("1");
		outputElement.add("N/A");
		outputElement.add(String.valueOf("0"));
		outputElement.add(String.valueOf(total));
		outputElement.add(String.valueOf(Q[1]));
		outputElement.add(String.valueOf(Q[2]));
		outputElement.add(String.valueOf(Q[3]));
		outputElement.add(String.valueOf(Q[4]));
		output.add(outputElement);
		
		for(int i = 0; i < result.size(); i++){
			trial++;			
			actMaxQ = (int) defaultValue; MaxQ = defaultValue;
			for(int j = 0; j < Q.length; j++){
				if (Q[j] > MaxQ){		//假設Q-value相同情況下, 會優先選擇左邊的choice
					MaxQ = Q[j];
					actMaxQ = j;
				}
			}
			
			choice = QChoiceTransABCD2DIG(((ArrayList)result.get(i)).get(1).toString());
			
			R = (-1) * Integer.parseInt(((ArrayList)schedule.get(scheduleCounter[Integer.parseInt(choice)])).get(Integer.parseInt(choice) - 1).toString());			
			if (choice.equals("1") || choice.equals("2"))
				R = R + 100;
			else if (choice.equals("3") || choice.equals("4"))
				R = R + 50;
			else
				R = (int) defaultValue;
			
			total = total + R;
			
			Q[Integer.parseInt(choice)] = Q[Integer.parseInt(choice)] + beta * (R - Q[Integer.parseInt(choice)]); 
			
			scheduleCounter[Integer.parseInt(choice)]++;
			
			outputElement = new ArrayList();
			outputElement.add(String.valueOf(Subject));
			outputElement.add(String.valueOf(trial));
			outputElement.add(String.valueOf(actMaxQ));
			outputElement.add(choice);
			outputElement.add(String.valueOf(R));
			outputElement.add(String.valueOf(total));
			outputElement.add(String.valueOf(Q[1]));
			outputElement.add(String.valueOf(Q[2]));
			outputElement.add(String.valueOf(Q[3]));
			outputElement.add(String.valueOf(Q[4]));
			output.add(outputElement);
		}
		

		//fileWrite.write_2d_arraylist_string2file("output.csv", output, true); // new file
	}

	public static void main(String[] args) {
		comb_outputGen();
	}
	
	

}
