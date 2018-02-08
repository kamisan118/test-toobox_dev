package preproc.J16;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import fsOp.FileTraversal_manager;
import fsOp.fileLineCount;
import fsOp.fileRead;
import fsOp.fileWrite;

public class TP_expr1_expr2 {

	/**
	 * @param args
	 */
	
	public static void TP_expr1_preproc(String[] inputDir) {

		//--------參數設定s-----------
		Hashtable mapTable_order = new Hashtable();
		mapTable_order.put("+-,+", "1");
		mapTable_order.put("+-,-", "2");
		mapTable_order.put("-+,+", "2");
		mapTable_order.put("-+,-", "1");
		
		
		ArrayList fileList = new ArrayList();
		for(int i = 0; i < inputDir.length; i++){

			try {
				FileTraversal_manager fm = new FileTraversal_manager();
				Hashtable dir_tab = fm.traverse_and_build_two_level_dir(new File(inputDir[i]));				
				Enumeration e = dir_tab.keys();			
				
				while( e.hasMoreElements() )
				{
					String dirName = e.nextElement().toString();
					if ((dirName.equals("+")) || (dirName.equals("-"))){
						fileList.addAll((ArrayList)dir_tab.get(dirName));
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		ArrayList fileList_Correct = new ArrayList();
		ArrayList fileList_Error = new ArrayList();
		for(int i = 0; i < fileList.size(); i++){
			if (((String)fileList.get(i)).endsWith("Correct.txt"))fileList_Correct.add(fileList.get(i));
			if (((String)fileList.get(i)).endsWith("Error.txt"))fileList_Error.add(fileList.get(i));
		}
		
		
		
		ArrayList outputAL = new ArrayList();
		ArrayList outputAL_row_head;
		ArrayList outputAL_row;
		
		Hashtable subjectIDs = new Hashtable();
		
		for (int i = 0; i < fileList_Correct.size(); i++){
			outputAL_row_head = new ArrayList();
						
			String filepath = (String)fileList_Correct.get(i);
			String[] filepathString = filepath.split("\\\\");
									
			//[subject(不要了),order,emotion,correct(key-level),feature,value]
			String subjectName = filepathString[filepathString.length - 3];			
			if (!subjectIDs.containsKey(subjectName))subjectIDs.put(subjectName, String.valueOf(i+1));
			outputAL_row_head.add(subjectIDs.get(subjectName)); // subjectName會有問題...ASCII
			
			outputAL_row_head.add(mapTable_order.get(filepathString[filepathString.length - 4] + "," + filepathString[filepathString.length - 2])); // order
			outputAL_row_head.add(filepathString[filepathString.length - 2]); // condition
			
			ArrayList ErrorAL = fileRead.get2D_arraylist_string_from_file(filepath.replace("Correct", "Error"), ",");
			for (int j = 0; j < ErrorAL.size(); j++){
				outputAL_row = (ArrayList) outputAL_row_head.clone();
				outputAL_row.add("0"); // 0: inCorrectly Typed Key
				outputAL_row.add(""); // blank
				outputAL_row.add(""); // blank
				outputAL.add(outputAL_row);
			}
			
			ArrayList CorrectAL = fileRead.get2D_arraylist_string_from_file(filepath, ",");			
			for (int j = 0; j < CorrectAL.size(); j++){
				
				//"678297s51";
				
				//[Single Keystroke Duration]
				outputAL_row = (ArrayList) outputAL_row_head.clone();
				outputAL_row.add("1"); // 0: inCorrectly Typed Key
				String key1 = ((String)((ArrayList)CorrectAL.get(j)).get(2)).substring(6);				
				outputAL_row.add(key1 + "D"); // feature
				outputAL_row.add(String.valueOf(Long.parseLong(((String)((ArrayList)CorrectAL.get(j)).get(5))) - Long.parseLong(((String)((ArrayList)CorrectAL.get(j)).get(3))))); // value
				outputAL.add(outputAL_row);
				
				//[Single Keystroke Latency]				
				if (!key1.equals("1")){
					outputAL_row = (ArrayList) outputAL_row_head.clone();
					outputAL_row.add("1"); // 0: inCorrectly Typed Key
					String key2 = ((String)((ArrayList)CorrectAL.get(j+1)).get(2)).substring(6);
					outputAL_row.add(key1 + "_"+ key2 + "L"); // feature
					outputAL_row.add(String.valueOf(Long.parseLong(((String)((ArrayList)CorrectAL.get(j+1)).get(3))) - Long.parseLong(((String)((ArrayList)CorrectAL.get(j)).get(5))))); // value
					outputAL.add(outputAL_row);
				}
				
			}			
		}		
		fileWrite.write_2d_arraylist_string2file("TP_expr1_preproc.csv", outputAL, true); // no append		
	}
	
	
	public static void TP_expr2_preproc(String[] inputDir) {

		//--------參數設定s-----------
		Hashtable mapTable_order = new Hashtable();
		mapTable_order.put("F_+A-A,+A", "1");
		mapTable_order.put("F_+A-A,-A", "2");
		mapTable_order.put("F_-A+A,-A", "1");
		mapTable_order.put("F_-A+A,+A", "2");
		mapTable_order.put("R_+A-A,+A", "1");
		mapTable_order.put("R_+A-A,-A", "2");
		mapTable_order.put("R_-A+A,-A", "1");
		mapTable_order.put("R_-A+A,+A", "2");
			
		
		ArrayList fileList = new ArrayList();
		for(int i = 0; i < inputDir.length; i++){

			try {
				FileTraversal_manager fm = new FileTraversal_manager();
				Hashtable dir_tab = fm.traverse_and_build_two_level_dir(new File(inputDir[i]));				
				Enumeration e = dir_tab.keys();			
				
				while( e.hasMoreElements() )
				{
					String dirName = e.nextElement().toString();
					if ((dirName.equals("+A")) || (dirName.equals("-A"))){
						fileList.addAll((ArrayList)dir_tab.get(dirName));
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		
		
		ArrayList fileList_Correct = new ArrayList();
		ArrayList fileList_Error = new ArrayList();
		for(int i = 0; i < fileList.size(); i++){
			if (((String)fileList.get(i)).endsWith("Correct.txt"))fileList_Correct.add(fileList.get(i));
			if (((String)fileList.get(i)).endsWith("Error.txt"))fileList_Error.add(fileList.get(i));
		}
		
		
		
		ArrayList outputAL = new ArrayList();
		ArrayList outputAL_row_head;
		ArrayList outputAL_row;
		
		Hashtable subjectIDs = new Hashtable();
		
		ArrayList myStack;
		
		for (int i = 0; i < fileList_Correct.size(); i++){
			outputAL_row_head = new ArrayList();
						
			String filepath = (String)fileList_Correct.get(i);
			String[] filepathString = filepath.split("\\\\");
									
			//[subject(不要了),order,emotion,correct(key-level),feature,value,BeepType,BeepInval,RT]
			
			String subjectName = filepathString[filepathString.length - 3];			
			if (!subjectIDs.containsKey(subjectName))subjectIDs.put(subjectName, String.valueOf(i+1));
			outputAL_row_head.add(subjectIDs.get(subjectName)); // subjectName會有問題...ASCII
			
			outputAL_row_head.add(mapTable_order.get(filepathString[filepathString.length - 4] + "," + filepathString[filepathString.length - 2])); // order
			outputAL_row_head.add(filepathString[filepathString.length - 2]); // condition
			
			String BeepType = filepathString[filepathString.length - 4].substring(0,1); // BeepType
			
			ArrayList ErrorAL = fileRead.get2D_arraylist_string_from_file(filepath.replace("Correct", "Error"), ",");
			
			myStack = new ArrayList();
			for (int j = 0; j < ErrorAL.size(); j++){
				if(((String)((ArrayList)ErrorAL.get(j)).get(2)).equals("Beep")){
					if(!myStack.isEmpty())myStack = new ArrayList();
					
					myStack.add(((String)((ArrayList)ErrorAL.get(j)).get(4)));	//BeepIntval
					
					myStack.add(String.valueOf(Long.parseLong(((String)((ArrayList)ErrorAL.get(j+1)).get(3))) 
							- Long.parseLong(((String)((ArrayList)ErrorAL.get(j)).get(3))))); // RT
					
					continue;
				}
				
				outputAL_row = (ArrayList) outputAL_row_head.clone();
				outputAL_row.add("0"); // 0: inCorrectly Typed Key
				outputAL_row.add(""); // blank
				outputAL_row.add(""); // blank
				outputAL_row.add(BeepType); // BeepType
				
				if(myStack.isEmpty()){
					outputAL_row.add("N/A"); // BeepInval
					outputAL_row.add("N/A"); // RT
				}
					
				else{
					outputAL_row.add((String)myStack.get(0)); // BeepInval
					outputAL_row.add((String)myStack.get(1)); // RT
				}
					
				
				
				outputAL.add(outputAL_row);
			}
			
			ArrayList CorrectAL = fileRead.get2D_arraylist_string_from_file(filepath, ",");			
			for (int j = 0; j < CorrectAL.size(); j++){
				
				//"678297s51";
				if(((String)((ArrayList)CorrectAL.get(j)).get(2)).equals("Beep")){
					if(!myStack.isEmpty())myStack = new ArrayList();
					
					myStack.add(((String)((ArrayList)CorrectAL.get(j)).get(4)));	//BeepIntval
					
					myStack.add(String.valueOf(Long.parseLong(((String)((ArrayList)CorrectAL.get(j+1)).get(3))) 
							- Long.parseLong(((String)((ArrayList)CorrectAL.get(j)).get(3))))); // RT
					continue;
				}
				
				//[Single Keystroke Duration]
				outputAL_row = (ArrayList) outputAL_row_head.clone();
				outputAL_row.add("1"); // 0: inCorrectly Typed Key
				String key1 = ((String)((ArrayList)CorrectAL.get(j)).get(2)).substring(6);				
				outputAL_row.add(key1 + "D"); // feature
				outputAL_row.add(String.valueOf(Long.parseLong(((String)((ArrayList)CorrectAL.get(j)).get(5))) - Long.parseLong(((String)((ArrayList)CorrectAL.get(j)).get(3))))); // value
				outputAL_row.add(BeepType); // BeepType
				outputAL_row.add((String)myStack.get(0)); // BeepInval
				outputAL_row.add((String)myStack.get(1)); // RT
				outputAL.add(outputAL_row);
				
				//[Single Keystroke Latency]				
				if (!key1.equals("1")){
					outputAL_row = (ArrayList) outputAL_row_head.clone();
					outputAL_row.add("1"); // 0: inCorrectly Typed Key
					String key2 = ((String)((ArrayList)CorrectAL.get(j+1)).get(2)).substring(6);
					outputAL_row.add(key1 + "_"+ key2 + "L"); // feature
					outputAL_row.add(String.valueOf(Long.parseLong(((String)((ArrayList)CorrectAL.get(j+1)).get(3))) - Long.parseLong(((String)((ArrayList)CorrectAL.get(j)).get(5))))); // value
					outputAL_row.add(BeepType); // BeepType
					outputAL_row.add((String)myStack.get(0)); // BeepInval
					outputAL_row.add((String)myStack.get(1)); // RT
					outputAL.add(outputAL_row);
				}
				
			}			
		}	
		
		fileWrite.write_2d_arraylist_string2file("TP_expr2_preproc.csv", outputAL, true); // no append		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		final String[] expr1_inputDir = 
			{"E:/_DataBase/2013Q1 情緒對鍵盤影響驗證實驗-基於面部情緒刺激(30x2)/_rawData/+-", 
			"E:/_DataBase/2013Q1 情緒對鍵盤影響驗證實驗-基於面部情緒刺激(30x2)/_rawData/-+"};
				
		TP_expr1_preproc(expr1_inputDir);
		
		final String[] expr2_inputDir = 
			{"E:/_DataBase/2013Q1 情緒對鍵盤影響驗證實驗-基於面部情緒刺激(30x2)/_rawData/F_+A-A", 
			"E:/_DataBase/2013Q1 情緒對鍵盤影響驗證實驗-基於面部情緒刺激(30x2)/_rawData/F_+A-A", 
			"E:/_DataBase/2013Q1 情緒對鍵盤影響驗證實驗-基於面部情緒刺激(30x2)/_rawData/R_+A-A", 
			"E:/_DataBase/2013Q1 情緒對鍵盤影響驗證實驗-基於面部情緒刺激(30x2)/_rawData/R_+A-A"};
		
		TP_expr2_preproc(expr2_inputDir);

	}

}
