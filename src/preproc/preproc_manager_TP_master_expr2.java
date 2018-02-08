package preproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import StringOp.myLCS;

import util.LabVIEWtick2CSharp;
import fsOp.FileTraversal_manager;
import fsOp.fileLineCount;
import fsOp.fileRead;
import fsOp.fileWrite;
import fsOp.killFilesAtRoot;


class MyIntComparable_TP_master_expr2 implements Comparator<ArrayList>{
	int tarCol;
	
	public MyIntComparable_TP_master_expr2(int tarCol){
		
		this.tarCol = tarCol;
		
	}
	
    @Override
    public int compare(ArrayList o1, ArrayList o2) {
    	long t1;
    	long t2;
    	
    	//因為不會有"N/A"所以不再需要這個檢查
    	t1 = Long.parseLong(((String)o1.get(tarCol)));
    	t2 = Long.parseLong(((String)o2.get(tarCol)));

    	return (t1>t2 ? 1 : (t1<t2 ? -1 : 0));
    	
    }
}

public class preproc_manager_TP_master_expr2 {

	/**
	 * @param args
	 */
		
	public static void combi_results_gen(){
		
		//--------參數設定s-----------
		Hashtable mapTable_order = new Hashtable();
		mapTable_order.put("F_+A-A,+A", "1");
		mapTable_order.put("F_+A-A,-A", "2");
		mapTable_order.put("F_-A+A,+A", "2");
		mapTable_order.put("F_-A+A,-A", "1");		
		mapTable_order.put("R_+A-A,+A", "1");
		mapTable_order.put("R_+A-A,-A", "2");
		mapTable_order.put("R_-A+A,+A", "2");
		mapTable_order.put("R_-A+A,-A", "1");
		
		String predef_terminator = "NumPad1"; //注意這個element只能出現一次.
		
		final String[] inputDir = 
			{"C:/DBoxs/prim/Dropbox/特設-校務大小事/VBM Lab/2013Q1 master thesis/TP_master_finale/data/F_+A-A", 
				"C:/DBoxs/prim/Dropbox/特設-校務大小事/VBM Lab/2013Q1 master thesis/TP_master_finale/data/F_-A+A", 
				"C:/DBoxs/prim/Dropbox/特設-校務大小事/VBM Lab/2013Q1 master thesis/TP_master_finale/data/R_+A-A", 
				"C:/DBoxs/prim/Dropbox/特設-校務大小事/VBM Lab/2013Q1 master thesis/TP_master_finale/data/R_-A+A"};				
		
		
		//-------第一步, 讀出檔案s
		ArrayList fileList = new ArrayList();
		for(int i = 0; i < inputDir.length; i++){

			try {
				FileTraversal_manager fm = new FileTraversal_manager();
				Hashtable dir_tab = fm.traverse_and_build_two_level_dir(new File(inputDir[i]));				
				Enumeration e = dir_tab.keys();			
				
				while( e.hasMoreElements() )
				{
					String subjectName = e.nextElement().toString();
					if ((subjectName.equals("+A")) || (subjectName.equals("-A"))){
						fileList.addAll((ArrayList)dir_tab.get(subjectName));
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		//製作general_results
		general_results_gen(fileList, mapTable_order);
		
		//將所有檔案tag上id, order, emotion, subject; 並且合併為一個檔案
		//欄位: id, subject, Fix/Beep, order, emotion, C#tick, key, UD
		ArrayList output_content = new ArrayList();
		
		//欄位: id, beep_length, Response Time (RT)
		ArrayList RTs_content = new ArrayList();
		
		int id = 1;
		int subjectNum = 0;
		Hashtable subject_done = new Hashtable();
		for(int i = 0; i < fileList.size(); i++){
			String filepath = (String)fileList.get(i);			
			
			String[] filepathString = filepath.split("\\\\");
			if (filepathString[filepathString.length - 1].equals("Correct.txt")){
				ArrayList file_content = fileRead.get2D_arraylist_string_from_file(filepath, ",");
				
				for(int j = 0; j < file_content.size(); j++){										
					ArrayList output_content_row = new ArrayList();
					//確認是否該換subject id
					String subjectName = filepathString[filepathString.length - 3];

					if (!subject_done.containsKey(subjectName)){						
						subjectNum++;
						subject_done.put(subjectName, String.valueOf(subjectNum));						
					}
					
					
					
					//RTs of Correct Sequences
					if (((String)((ArrayList)file_content.get(j)).get(2)).equals("Beep")){					
						ArrayList RTs_content_row = new ArrayList();
						
						RTs_content_row.add(String.valueOf(id)); // id
						RTs_content_row.add(((String)((ArrayList)file_content.get(j)).get(4))); // Beep Length
						RTs_content_row.add(String.valueOf(Long.parseLong(((String)((ArrayList)file_content.get(j+1)).get(3))) 
								- Long.parseLong(((String)((ArrayList)file_content.get(j)).get(3))))); // Response Time (RT)
						
						RTs_content.add(RTs_content_row);
					}else{
						output_content_row.add(String.valueOf(id)); //id
						output_content_row.add(String.valueOf(subject_done.get(subjectName))); // subject
						output_content_row.add(filepathString[filepathString.length - 4].substring(0, 1)); //Fix_or_Rand
						output_content_row.add(mapTable_order.get(filepathString[filepathString.length - 4] + "," + filepathString[filepathString.length - 2])); // order
						output_content_row.add(filepathString[filepathString.length - 2].substring(0,1)); // emotion							
						output_content_row.add(((ArrayList)file_content.get(j)).get(3)); // CSharp Tick
						output_content_row.add(((ArrayList)file_content.get(j)).get(2)); // key
						output_content_row.add(((ArrayList)file_content.get(j)).get(4)); // UD
						output_content.add(output_content_row);
						
						output_content_row = new ArrayList();
						output_content_row.add(String.valueOf(id)); //id
						output_content_row.add(String.valueOf(subject_done.get(subjectName))); // subject
						output_content_row.add(filepathString[filepathString.length - 4].substring(0, 1)); // Fix_or_Rand
						output_content_row.add(mapTable_order.get(filepathString[filepathString.length - 4] + "," + filepathString[filepathString.length - 2])); // order
						output_content_row.add(filepathString[filepathString.length - 2].substring(0,1)); // emotion
						output_content_row.add(((ArrayList)file_content.get(j)).get(5)); // CSharp Tick
						output_content_row.add(((ArrayList)file_content.get(j)).get(2)); // key
						output_content_row.add(((ArrayList)file_content.get(j)).get(6)); // UD
						output_content.add(output_content_row);
						
						//確認是否該換id
						if (((String)((ArrayList)file_content.get(j)).get(2)).equals(predef_terminator))
							id++;
					}
					
					
				}
			}				
		}
		
			
		//RTs of Error Sequences
		for(int i = 0; i < fileList.size(); i++){
			String filepath = (String)fileList.get(i);						
			String[] filepathString = filepath.split("\\\\");		
			if (filepathString[filepathString.length - 1].equals("Error.txt")){
				ArrayList file_content = fileRead.get2D_arraylist_string_from_file(filepath, ",");				
				for(int j = 0; j < file_content.size(); j++){										
					
					if (((String)((ArrayList)file_content.get(j)).get(2)).equals("Beep")){					
						ArrayList RTs_content_row = new ArrayList();
						
						RTs_content_row.add(String.valueOf(id)); // id
						RTs_content_row.add(((String)((ArrayList)file_content.get(j)).get(4))); // Beep Length
						RTs_content_row.add(String.valueOf(Long.parseLong(((String)((ArrayList)file_content.get(j+1)).get(3))) 
								- Long.parseLong(((String)((ArrayList)file_content.get(j)).get(3))))); // Response Time (RT)
												
						RTs_content.add(RTs_content_row);
						id++;						
					}					
				}
			}
		}
		
		ordered_all_12_table(fileList, mapTable_order, subject_done);
		
		
		fileWrite.write_2d_arraylist_string2file("keyUDcorrect.csv", output_content, true); // no append
		fileWrite.write_2d_arraylist_string2file("RT_info_all.csv", RTs_content, true); // no append
		
	}
	
	public static ArrayList remove_beepEvents(ArrayList inputAL){
		int i = 0;
		while(i != inputAL.size()){
			if(((String)((ArrayList)inputAL.get(i)).get(2)).equals("Beep"))
				inputAL.remove(i);
			else
				i++;
		}
		
		
		return inputAL;
		
		
	}
	
	
	public static void ordered_all_12_table(ArrayList fileList, Hashtable mapTable_order, Hashtable subject_done){
		//0: no data, 1: correct, 2: incorrect
		//subject, order, condition, [sequence]..
		ArrayList matrix_view_al = new ArrayList();
		
		Hashtable numbersTab = new Hashtable();
		String path_correct = null, path_error = null;
		for (int i = 0; i < fileList.size(); i++){
			ArrayList output_content_row = new ArrayList();
			String filepath = (String)fileList.get(i);
			String[] filepathString = filepath.split("\\\\");			
			
			if (filepathString[filepathString.length - 1].equals("Correct.txt")){				
				path_correct = filepath;
				numbersTab.put("CorrectNum", "");	
			}
			else if (filepathString[filepathString.length - 1].equals("Error.txt")){
				path_error = filepath;
				numbersTab.put("ErrorNum", "");	
			}
			
			//假設讀檔時fileList有依照路徑order過~
			if(numbersTab.containsKey("CorrectNum") && numbersTab.containsKey("ErrorNum")){
				String subjectName = filepathString[filepathString.length - 3];
				
				output_content_row.add(String.valueOf(subject_done.get(subjectName))); // subject
				output_content_row.add(filepathString[filepathString.length - 4].substring(0, 1)); //Fix_or_Rand
				output_content_row.add(mapTable_order.get(filepathString[filepathString.length - 4] + "," + filepathString[filepathString.length - 2])); // order
				output_content_row.add(filepathString[filepathString.length - 2].substring(0,1)); // condition
				
				
				//這邊來合併Correct.txt與Error.txt
				ArrayList correctrows = fileRead.get2D_arraylist_string_from_file(path_correct);
				ArrayList errorrows = fileRead.get2D_arraylist_string_from_file(path_error);
				
				correctrows = remove_beepEvents(correctrows);
				errorrows = remove_beepEvents(errorrows);
				
				ArrayList totalrows = new ArrayList();
				//先全部tag成1 or 2
				for (int j = 0 ; j < correctrows.size(); j++){
					ArrayList totalrows_row = new ArrayList();
					totalrows_row.add(((ArrayList)correctrows.get(j)).get(3)); // tick
					totalrows_row.add("1"); // tag
					totalrows.add(totalrows_row);
				}
				for (int j = 0 ; j < errorrows.size(); j++){
					ArrayList totalrows_row = new ArrayList();
					totalrows_row.add(((ArrayList)errorrows.get(j)).get(3)); // tick
					totalrows_row.add("2"); // tag
					totalrows.add(totalrows_row);
				}
								
				Collections.sort(totalrows, 
						new MyIntComparable_TP_master_expr2(0));
				
				//加進去output_content_row
				for (int j = 0 ; j < totalrows.size(); j++){
					output_content_row.add(((ArrayList)totalrows.get(j)).get(1));
				}

				
				matrix_view_al.add(output_content_row);
				
				output_content_row = new ArrayList();
				numbersTab = new Hashtable();
			}
				
		}
		
		
		fileWrite.write_2d_arraylist_string2file("matrix_view.csv", matrix_view_al, true); // no append
		
	}
	
	
	
	public static void general_results_gen(ArrayList fileList, Hashtable mapTable_order){
		ArrayList general_results_al = new ArrayList();		
		Hashtable subject_done = new Hashtable();
		
		int subjectNum = 0;
		
		
		Hashtable numbersTab = new Hashtable();
		for (int i = 0; i < fileList.size(); i++){
			ArrayList output_content_row = new ArrayList();
			String filepath = (String)fileList.get(i);
			String[] filepathString = filepath.split("\\\\");
			
			
			
			//確認是否該換subject id
			String subjectName = filepathString[filepathString.length - 3];
			if (!subject_done.containsKey(subjectName)){						
				subjectNum++;
				subject_done.put(subjectName, String.valueOf(subjectNum));						
			}			
			
			if (filepathString[filepathString.length - 1].equals("Correct.txt")){				
				try {
					numbersTab.put("CorrectNum", String.valueOf(fileLineCount.count(filepath)));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			else if (filepathString[filepathString.length - 1].equals("Error.txt")){
				try {
					numbersTab.put("ErrorNum", String.valueOf(fileLineCount.count(filepath)));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			
			//假設讀檔時fileList有依照路徑order過~
			if(numbersTab.containsKey("CorrectNum") && numbersTab.containsKey("ErrorNum")){
				output_content_row.add(String.valueOf(subject_done.get(subjectName))); // subject				
				output_content_row.add(filepathString[filepathString.length - 4].substring(0, 1)); // Fix_or_Rand
				output_content_row.add(mapTable_order.get(filepathString[filepathString.length - 4] + "," + filepathString[filepathString.length - 2])); // order
				output_content_row.add(filepathString[filepathString.length - 2].substring(0, 1)); // condition	
				output_content_row.add((String)numbersTab.get("CorrectNum"));
				output_content_row.add((String)numbersTab.get("ErrorNum"));
				general_results_al.add(output_content_row);
				
				output_content_row = new ArrayList();
				numbersTab = new Hashtable();
			}
				
		}
		
		fileWrite.write_2d_arraylist_string2file("general_results.csv", general_results_al, true); // no append
	}
		
	public static void KB_features_DL_brute(String keyAndUD2_Path){
		ArrayList al_extract_KeyUD, al_extract_KeyUD_features = new ArrayList();
		al_extract_KeyUD = fileRead.get2D_arraylist_string_from_file(keyAndUD2_Path, ",");
		
		// 1~8 = 6, 7, 8, 2, 9, 7s, 5, 1
		//Duration
		KB_features_DL_brute_SelKeyUD("NumPad6", "0", "NumPad6", "1", al_extract_KeyUD, al_extract_KeyUD_features, true);		
		KB_features_DL_brute_SelKeyUD("NumPad6", "0", "NumPad7", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad6", "0", "NumPad8", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad6", "0", "NumPad2", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad6", "0", "NumPad9", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad6", "0", "NumPad7s", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad6", "0", "NumPad5", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad6", "0", "NumPad1", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);

		KB_features_DL_brute_SelKeyUD("NumPad7", "0", "NumPad7", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "0", "NumPad8", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "0", "NumPad2", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "0", "NumPad9", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "0", "NumPad7s", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "0", "NumPad5", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "0", "NumPad1", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad8", "0", "NumPad8", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad8", "0", "NumPad2", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad8", "0", "NumPad9", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad8", "0", "NumPad7s", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad8", "0", "NumPad5", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad8", "0", "NumPad1", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad2", "0", "NumPad2", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "0", "NumPad9", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "0", "NumPad7s", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "0", "NumPad5", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "0", "NumPad1", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		
		KB_features_DL_brute_SelKeyUD("NumPad9", "0", "NumPad9", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad9", "0", "NumPad7s", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad9", "0", "NumPad5", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad9", "0", "NumPad1", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad7s", "0", "NumPad7s", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7s", "0", "NumPad5", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7s", "0", "NumPad1", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
				
		KB_features_DL_brute_SelKeyUD("NumPad5", "0", "NumPad5", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad5", "0", "NumPad1", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);

		KB_features_DL_brute_SelKeyUD("NumPad1", "0", "NumPad1", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		//Lactency			
		KB_features_DL_brute_SelKeyUD("NumPad6", "1", "NumPad7", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad6", "1", "NumPad8", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad6", "1", "NumPad2", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad6", "1", "NumPad9", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad6", "1", "NumPad7s", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad6", "1", "NumPad5", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad6", "1", "NumPad1", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad7", "1", "NumPad8", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "1", "NumPad2", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "1", "NumPad9", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "1", "NumPad7s", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "1", "NumPad5", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "1", "NumPad1", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		
		KB_features_DL_brute_SelKeyUD("NumPad8", "1", "NumPad2", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad8", "1", "NumPad9", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad8", "1", "NumPad7s", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad8", "1", "NumPad5", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad8", "1", "NumPad1", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad2", "1", "NumPad9", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "1", "NumPad7s", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "1", "NumPad5", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "1", "NumPad1", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
				
		KB_features_DL_brute_SelKeyUD("NumPad9", "1", "NumPad7s", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad9", "1", "NumPad5", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad9", "1", "NumPad1", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
						
		KB_features_DL_brute_SelKeyUD("NumPad7s", "1", "NumPad5", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7s", "1", "NumPad1", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad5", "1", "NumPad1", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		/*  -- 注意 --
		 * (1) latency features有可能出現負的, 因為..還沒release這個鍵, 就把下個鍵給按下去了.
		 * 
		 */
		
		fileWrite.write_2d_arraylist_string2file("keystrokeUD_extrac_features1.csv", al_extract_KeyUD_features, true); // new file
		
	}
	
	
	public static void KB_features_DL_brute_SelKeyUD(String st_Key, String st_UD, 
			String ed_Key, String ed_UD, ArrayList al_extract_KeyUD, ArrayList outputAL, boolean new_featureTab){
		
		String st_KeyUD = st_Key + st_UD;
		String ed_KeyUD = ed_Key + ed_UD;
		
		ArrayList tmpAL = null;
		Hashtable calTab = null;
		
		int LastID = -1, CurrentID;
		
		boolean isLastSeqSuccess = false;
		
		int ite_outputAL = 0;
		
		//過程中會把non-success的seq.都去除不計~
		for(int i = 0; i < al_extract_KeyUD.size(); i++){
			CurrentID = Integer.parseInt(((String)((ArrayList)al_extract_KeyUD.get(i)).get(0)));			
			if (CurrentID != LastID){
				
				//因為是從CurrentID去extract LastID的資料, 所以第一次都不用做~ CurrentID = 2時, 會立馬去處理LastID = 1的序列
				if (isLastSeqSuccess){
					if (new_featureTab){	//如果是第一次開feature table -- 就跑這串; 之後就只是append!
						tmpAL = new ArrayList();
						tmpAL.add(String.valueOf(LastID));
						tmpAL.add(String.valueOf(((String)((ArrayList)al_extract_KeyUD.get(i)).get(1)))); // subjectID
						tmpAL.add(String.valueOf(((String)((ArrayList)al_extract_KeyUD.get(i)).get(2)))); // Fix_or_Rand
						tmpAL.add(String.valueOf(((String)((ArrayList)al_extract_KeyUD.get(i)).get(3)))); // order
						tmpAL.add(String.valueOf(((String)((ArrayList)al_extract_KeyUD.get(i)).get(4)) ));// emotion
						
						tmpAL.add(String.valueOf(Long.parseLong((String)calTab.get(ed_KeyUD)) - Long.parseLong((String)calTab.get(st_KeyUD))));
						outputAL.add(tmpAL);						
					}
					else{
						tmpAL = (ArrayList)outputAL.get(ite_outputAL);
						tmpAL.add(String.valueOf(Long.parseLong((String)calTab.get(ed_KeyUD)) - Long.parseLong((String)calTab.get(st_KeyUD))));
						outputAL.set(ite_outputAL, tmpAL);
						ite_outputAL++;
					}					
				}				
				
				calTab = new Hashtable();				
				LastID = CurrentID;				
			}
			

			isLastSeqSuccess = true;
			
			
			//放入keyUD跟CSharp Tick Time pairs
			calTab.put(((String)((ArrayList)al_extract_KeyUD.get(i)).get(6))
					+((String)((ArrayList)al_extract_KeyUD.get(i)).get(7)), ((ArrayList)al_extract_KeyUD.get(i)).get(5));
			
		}
		//別忘了最後一個event也要補處理 (因為是使用if (CurrentID != LastID))
		if (isLastSeqSuccess){
			if (new_featureTab){
				tmpAL = new ArrayList();
				tmpAL.add(String.valueOf(LastID));
				tmpAL.add(String.valueOf(((String)((ArrayList)al_extract_KeyUD.get(al_extract_KeyUD.size()-1)).get(1)))); // order
				tmpAL.add(String.valueOf(((String)((ArrayList)al_extract_KeyUD.get(al_extract_KeyUD.size()-1)).get(2)))); // Fix_or_Rand
				tmpAL.add(String.valueOf(((String)((ArrayList)al_extract_KeyUD.get(al_extract_KeyUD.size()-1)).get(3)))); // subjectID
				tmpAL.add(String.valueOf(((String)((ArrayList)al_extract_KeyUD.get(al_extract_KeyUD.size()-1)).get(4)) ));// emotion
				tmpAL.add(String.valueOf(Long.parseLong((String)calTab.get(ed_KeyUD)) - Long.parseLong((String)calTab.get(st_KeyUD))));
				outputAL.add(tmpAL);
			}
			else{
				tmpAL = (ArrayList)outputAL.get(ite_outputAL);
				tmpAL.add(String.valueOf(Long.parseLong((String)calTab.get(ed_KeyUD)) - Long.parseLong((String)calTab.get(st_KeyUD))));
				outputAL.set(ite_outputAL, tmpAL);
				ite_outputAL++;
			}					
		}	
		
	}
	
	
	
	public static void main(String[] args) {
			
		//killFilesAtRoot.kill(); //只有要做pic_results_gen()時才有需要打開.
		
		
		
		
		/* "KB_results_gen"會包在"pic_results_gen"中一起執行, generate 1個table
		 * 
		 * input: N個directory, 裡面都是directories(以subject name命名)
		 * procedure: 
		 * 1. 讀入所有"correct sequences"的檔案s，並貼上獨立唯一的ids後合併成一個檔案
		 * 作法--讀入一個directory, 將裡面所有的資料都tag上id, order, emotion, subject
		 * --欄位: id, subject, order, emotion, C#tick, key, UD
		 * 
		 * 2.負責generate general results, 包含錯誤element數的tracking (general_results.csv)
		 * 
		 * 3. 把大家order, Correct = 1, Error = 2, 表現在一個矩陣上頭 (matrix_view.csv)
		 * 
		 * output: RT_info_all.csv, keyUDcorrect.csv, general_results.csv, matrix_view.csv 
		 * 	
		 */
		combi_results_gen();
		
		
		///------------前置作業完畢，開始輸出feautures-----------
		///------------前置作業完畢，開始輸出feautures-----------
		///------------前置作業完畢，開始輸出feautures-----------
		
		/* input: keyUDcorrect.csv
		 * procedure: 針對keyUDcorrect.csv的sequences把所有組合的latency與duration算出features;
		 * output: keystrokeUD_extrac_features.csv
		 */				
		KB_features_DL_brute("keyUDcorrect.csv");

		
		/* --- 還沒做的功能s --- 
		 * 目前都手動XD 並且combine 所有基礎資訊 (pic_results.csv); 是最終結果
		 */
		
	}

}
