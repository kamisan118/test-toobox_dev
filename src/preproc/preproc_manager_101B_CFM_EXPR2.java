package preproc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import util.LabVIEWtick2CSharp;

import fsOp.FileTraversal_manager;
import fsOp.fileRead;
import fsOp.fileWrite;

public class preproc_manager_101B_CFM_EXPR2 {
	
	final static String matchingTargetNumberSequence = "748596132";
	
	
	public static ArrayList getSAMfromEvent(ArrayList filepaths){
		ArrayList outputAL = new ArrayList();
		ArrayList file_content;		
		
		int primary_key = 0;
		for(int i = 0; i < filepaths.size(); i++){
			file_content = fileRead.get2D_arraylist_string_from_file(((String)filepaths.get(i)), ",");
			String[] splited_filepath = ((String)filepaths.get(i)).split("\\\\");
			String subject = splited_filepath[splited_filepath.length - 3].split("_")[1];
			boolean expr_start = false;
			String Order = null;
			for(int j = 0; j < file_content.size(); j++){
				if(((String)((ArrayList)file_content.get(j)).get(3)).contains("Order"))Order = ((String)((ArrayList)file_content.get(j)).get(3)).substring(((String)((ArrayList)file_content.get(j)).get(3)).length()-1); 
				if(((String)((ArrayList)file_content.get(j)).get(3)).equals("Experiment trial start"))expr_start = true;
				if(expr_start && (((String)((ArrayList)file_content.get(j)).get(3)).equals("Ready"))){
					ArrayList outputAL_row = new ArrayList();
					
					
					outputAL_row.add(String.valueOf(primary_key+1)); // primary_key
					outputAL_row.add(String.valueOf(i+1)); // subject_id
					outputAL_row.add(subject); // subject
					outputAL_row.add(Order); // Order: 1 or 2
														
					outputAL_row.add(((String)((ArrayList)file_content.get(j)).get(4))); // trial number
					
					if(!((String)((ArrayList)file_content.get(j+1)).get(3)).equals("Sound play"))continue;
					outputAL_row.add(((String)((ArrayList)file_content.get(j+1)).get(4))); // played sound
					
					if(!((String)((ArrayList)file_content.get(j+2)).get(3)).equals("Number show"))continue;
					outputAL_row.add(((String)((ArrayList)file_content.get(j+2)).get(2))); // sTyping_tick(Number show Tick)
					
					if(!((String)((ArrayList)file_content.get(j+5)).get(3)).equals("Arousal"))continue;
					outputAL_row.add(((String)((ArrayList)file_content.get(j+5)).get(2))); // eTyping_tick(Arousal Tick)
					
					if(!((String)((ArrayList)file_content.get(j+4)).get(3)).equals("Valance"))continue;
					outputAL_row.add(((String)((ArrayList)file_content.get(j+4)).get(4))); // valence
					
					if(!((String)((ArrayList)file_content.get(j+5)).get(3)).equals("Arousal"))continue;
					outputAL_row.add(((String)((ArrayList)file_content.get(j+5)).get(4))); // arousal
					
					primary_key++;
					outputAL.add(outputAL_row);
					//System.out.println(outputAL_row);
					
				}				
				if(((String)((ArrayList)file_content.get(j)).get(3)).equals("Experiment over"))break;
			}
			
		}
		
		System.out.println("---getSAMfromEvent Done---");
		return outputAL; //return 2D al.
	}
	
	public static ArrayList getKeyStrokeFeatures(ArrayList filepaths, ArrayList output_getSAMfromEvent){
		/*Keystroke data analysis -- 作法:
		0. 從"Events.csv"抓subject-trialnumber的開始與結束tick # def.: Number show tcik ~ Arousal tick的區間
		1. 先用subject-trialnumber的開始與結束tick去解出"KeyStroke_Mix.txt"(2D al.s), 用ArrayList裝起來
		2. (進一步分析...)想用BruteForce解ALL, 然後error的特別挑出來---How?
			---> 1) 先mark是否success, 2) 針對success者做bruteforce (類似TP-thesis expr2 preproc.的作法)*/			
		
		ArrayList outputAL = new ArrayList();
		ArrayList file_content;	
		
		ArrayList KeyStroke_Mix_ALL = new ArrayList();
		ArrayList KeyStroke_Mix_subject_trial;
		 
		
		long startTick, endTick;
		for(int i = 0; i < output_getSAMfromEvent.size(); i++){
			startTick = Long.parseLong(((String)((ArrayList)output_getSAMfromEvent.get(i)).get(6)));
			endTick = Long.parseLong(((String)((ArrayList)output_getSAMfromEvent.get(i)).get(7)));
			KeyStroke_Mix_subject_trial = new ArrayList();
			
			//open right keystroke_mix file, filt out the keystroke data.
			String file_processing = null;
			for(int j = 0; j < filepaths.size(); j++){
				String[] splited_filepath = ((String)filepaths.get(j)).split("\\\\");
				String subject = splited_filepath[splited_filepath.length - 3].split("_")[1];
				if ((subject.equals(((String)((ArrayList)output_getSAMfromEvent.get(i)).get(2)))))
						file_processing =(String)filepaths.get(j);				
			}
			file_content = fileRead.get2D_arraylist_string_from_file(file_processing, ",");
			
			for(int j = 0; j < file_content.size(); j++){
				long timeOfKeyStroke = Long.parseLong(((String)((ArrayList)file_content.get(j)).get(2))); 
				if((timeOfKeyStroke >= startTick) && (timeOfKeyStroke <= endTick)){
					
					//add in the primary key at the front
					((ArrayList)file_content.get(j)).add(0, ((String)((ArrayList)output_getSAMfromEvent.get(i)).get(0)));					
					KeyStroke_Mix_subject_trial.add(file_content.get(j)); 
				}
			}
			
			KeyStroke_Mix_ALL.add(KeyStroke_Mix_subject_trial);		
			//fileWrite.write_2d_arraylist_string2file(String.valueOf(startTick) + "_" + String.valueOf(endTick) + ".csv", KeyStroke_Mix_subject_trial, true); // no append
		}
		
		
		
		//KeyStroke_Mix_ALL obtained, now proc. the features!
		KB_features_checkSuccess(KeyStroke_Mix_ALL);
		
		//Brute Force extract all features from successful typed sequences
		outputAL = KB_features_DL_brute(KeyStroke_Mix_ALL);
			
		
		System.out.println("---getKeyStrokeFeatures Done---");		
		return outputAL; //return 2D al.
	}
	
	public static void KB_features_checkSuccess(ArrayList KeyStroke_Mix_ALL){
	
		/*mark是否success: 概念 -- 整個順序都要對，不可多不可少挖; 
		做法: 先取all 0(press)出來, 然後concate string of keys -- 然後直接比較string對不對~*/
		for(int i = 0; i < KeyStroke_Mix_ALL.size(); i++){
			
			ArrayList tmpAL = new ArrayList();
			//[check for press event only]
			for(int j = 0; j < ((ArrayList)KeyStroke_Mix_ALL.get(i)).size(); j++){
				if(((String)((ArrayList)((ArrayList)KeyStroke_Mix_ALL.get(i)).get(j)).get(5)).equals("0")) // get(5) not (4) 因為前面加了primary key在col1
					tmpAL.add(((ArrayList)((ArrayList)KeyStroke_Mix_ALL.get(i)).get(j)));				
			}
			
			String typedSequence = "";
			for(int j = 0; j < tmpAL.size(); j++){
				typedSequence += ((String)((ArrayList)tmpAL.get(j)).get(4)).substring(((String)((ArrayList)tmpAL.get(j)).get(4)).length()-1); // get(4) not (3) 因為前面加了primary key在col1
			}
			
			for(int j = 0; j < ((ArrayList)KeyStroke_Mix_ALL.get(i)).size(); j++){
				if (matchingTargetNumberSequence.equals(typedSequence))
					((ArrayList)((ArrayList)KeyStroke_Mix_ALL.get(i)).get(j)).add(1, String.valueOf(1)); //isSuccess
				else
					((ArrayList)((ArrayList)KeyStroke_Mix_ALL.get(i)).get(j)).add(1, String.valueOf(0)); //!isSuccess
			}
					
			
		}
	}
	
	public static String KB_features_DL_brute_SelKeyUD(ArrayList KeyStroke_Mix_ALL_element, String st_Key, String st_UD, 
			String ed_Key, String ed_UD){
		
		long startTick = 0;
		long endTick = -99;
		
		
		if(((String)((ArrayList)KeyStroke_Mix_ALL_element.get(0)).get(1)).equals("0")) //it's an !isSuccess typed sequence. 
			return "N/A";
		
		for(int i = 0; i < KeyStroke_Mix_ALL_element.size(); i++){
			if(((String)((ArrayList)KeyStroke_Mix_ALL_element.get(i)).get(5)).equals(st_Key) && ((String)((ArrayList)KeyStroke_Mix_ALL_element.get(i)).get(6)).equals(st_UD)) //(5)是key, 因為col1跟col2分別是primary key跟isSuccess
				startTick = Long.parseLong(((String)((ArrayList)KeyStroke_Mix_ALL_element.get(i)).get(4)));
			if(((String)((ArrayList)KeyStroke_Mix_ALL_element.get(i)).get(5)).equals(ed_Key) && ((String)((ArrayList)KeyStroke_Mix_ALL_element.get(i)).get(6)).equals(ed_UD)) //(5)是key, 因為col1跟col2分別是primary key跟isSuccess
				endTick = Long.parseLong(((String)((ArrayList)KeyStroke_Mix_ALL_element.get(i)).get(4)));
		}
		
		
		return String.valueOf(endTick - startTick);
	}
	
	public static ArrayList KB_features_DL_brute(ArrayList KeyStroke_Mix_ALL){
		
		
		ArrayList keystroke_features_brute = new ArrayList();
		for(int i = 0; i < KeyStroke_Mix_ALL.size(); i++){
			
			ArrayList KeyStroke_Mix_ALL_element = ((ArrayList)KeyStroke_Mix_ALL.get(i));
			ArrayList keystroke_features_brute_row = new ArrayList();	
			
			keystroke_features_brute_row.add((String)((ArrayList)KeyStroke_Mix_ALL_element.get(0)).get(0)); //primary key
			keystroke_features_brute_row.add((String)((ArrayList)KeyStroke_Mix_ALL_element.get(0)).get(1)); //isSuccess
			
			//final static String matchingTargetNumberSequence = "748596132";
			//81個features對嗎? => yes
			//Duration
			for(int j = 0; j < matchingTargetNumberSequence.length(); j++){
				for(int k = j; k < matchingTargetNumberSequence.length(); k++){
					
					keystroke_features_brute_row.add(KB_features_DL_brute_SelKeyUD(KeyStroke_Mix_ALL_element, 
							"NumPad"+matchingTargetNumberSequence.charAt(j), "0", 
							"NumPad"+matchingTargetNumberSequence.charAt(k), "1"));
				}
			}
				
			//Lactency	
			for(int j = 0; j < matchingTargetNumberSequence.length(); j++){
				for(int k = (j+1); k < matchingTargetNumberSequence.length(); k++){
					keystroke_features_brute_row.add(KB_features_DL_brute_SelKeyUD(KeyStroke_Mix_ALL_element, 
							"NumPad"+matchingTargetNumberSequence.charAt(j), "1", 
							"NumPad"+matchingTargetNumberSequence.charAt(k), "0"));
				}
			}
			
			keystroke_features_brute.add(keystroke_features_brute_row);
			
//			// 1~8 = 6, 7, 8, 2, 9, 7s, 5, 1
//			//Duration
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "0", "NumPad6", "1");		
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "0", "NumPad7", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "0", "NumPad8", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "0", "NumPad2", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "0", "NumPad9", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "0", "NumPad7s", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "0", "NumPad5", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "0", "NumPad1", "1");
//
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7", "0", "NumPad7", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7", "0", "NumPad8", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7", "0", "NumPad2", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7", "0", "NumPad9", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7", "0", "NumPad7s", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7", "0", "NumPad5", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7", "0", "NumPad1", "1");
//			
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad8", "0", "NumPad8", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad8", "0", "NumPad2", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad8", "0", "NumPad9", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad8", "0", "NumPad7s", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad8", "0", "NumPad5", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad8", "0", "NumPad1", "1");
//			
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad2", "0", "NumPad2", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad2", "0", "NumPad9", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad2", "0", "NumPad7s", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad2", "0", "NumPad5", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad2", "0", "NumPad1", "1");
//			
//			
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad9", "0", "NumPad9", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad9", "0", "NumPad7s", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad9", "0", "NumPad5", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad9", "0", "NumPad1", "1");
//			
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7s", "0", "NumPad7s", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7s", "0", "NumPad5", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7s", "0", "NumPad1", "1");
//					
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad5", "0", "NumPad5", "1");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad5", "0", "NumPad1", "1");
//
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad1", "0", "NumPad1", "1");
//			
//			//Lactency			
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "1", "NumPad7", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "1", "NumPad8", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "1", "NumPad2", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "1", "NumPad9", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "1", "NumPad7s", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "1", "NumPad5", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad6", "1", "NumPad1", "0");
//			
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7", "1", "NumPad8", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7", "1", "NumPad2", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7", "1", "NumPad9", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7", "1", "NumPad7s", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7", "1", "NumPad5", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7", "1", "NumPad1", "0");
//			
//			
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad8", "1", "NumPad2", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad8", "1", "NumPad9", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad8", "1", "NumPad7s", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad8", "1", "NumPad5", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad8", "1", "NumPad1", "0");
//			
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad2", "1", "NumPad9", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad2", "1", "NumPad7s", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad2", "1", "NumPad5", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad2", "1", "NumPad1", "0");
//					
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad9", "1", "NumPad7s", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad9", "1", "NumPad5", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad9", "1", "NumPad1", "0");
//							
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7s", "1", "NumPad5", "0");
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad7s", "1", "NumPad1", "0");
//			
//			KB_features_DL_brute_SelKeyUD(((ArrayList)KeyStroke_Mix_ALL.get(i)), "NumPad5", "1", "NumPad1", "0");
			
			/*  -- 注意 --
			 * (1) latency features有可能出現負的, 因為..還沒release這個鍵, 就把下個鍵給按下去了.
			 * 
			 */
			
		}
		
		
		System.out.println("---KB_features_DL_brute Done---");
		return keystroke_features_brute;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//[preproces the main data; whole package]
		mainAndkeystroke();
		
		//[preproc for training data only, probably should be run only after executing mainAndkeystroke()--後來做成不用~]
		preproc_trainingData();
		
		
		//2013/10/01--->this has not been done because pmli abort this task, decided not to include physiological signals in J17 study for PLOS ONE submission
		//batchLabVIEWtick2CSharp_main();
		
	}
	
	public static void preproc_trainingData() {
		/*
		 目標--分析training的資料：
		*開始到完成字串的速度
		*duration的變化--第一個到最後一個
		*latency的變化--第一個到最後一個
		
		演算法：針對每個人
		1. 取出找Event.txt中的"Trainning strat"跟"Trainning over,Well training"
		2. 把其中的所有Keystroke Data從KeyStroke_Mix.txt取出
		3. 拿2當作終止, 切割所有的748596132組
		4. 取features, 做成一個output file		
		-subject_id
		-subject_學號
		-id: 第幾組
		-「開始到完成字串的速度」 => 把個別組--第一個跟最後一個的時間相減, 做出一個col
		-「duration的變化」 => 把"所有"duration取出, 做出一個col
		-「latency的變化」 => 把"所有"latency取出, 做出一個col
		 */
		
		
		final String inputDir = "E:/_DataBase/201305 101B_CFM_EXPR2 (52)/raw_data";
		
		
		FileTraversal_manager fm = new FileTraversal_manager();						
		Hashtable dir_tab;
		ArrayList fileList_tmp = new ArrayList();
		ArrayList fileList_Event = new ArrayList();
		ArrayList fileList_KeyStroke_Mix = new ArrayList();
		try {
			fm = new FileTraversal_manager();
			dir_tab = fm.traverse_and_build_two_level_dir(new File(inputDir));				
			Enumeration e = dir_tab.keys();			
			
			while( e.hasMoreElements() )
			{
				String DirName = e.nextElement().toString();
				if (DirName.length() > 9){
					fileList_tmp.addAll((ArrayList)dir_tab.get(DirName)); // get all files in the directories
				}
			}
						
			for(int i = 0; i < fileList_tmp.size(); i++){
				if (((String)fileList_tmp.get(i)).endsWith("Event.txt"))fileList_Event.add(fileList_tmp.get(i));
				if (((String)fileList_tmp.get(i)).endsWith("KeyStroke_Mix.txt"))fileList_KeyStroke_Mix.add(fileList_tmp.get(i));
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		//ArrayList output_getSAMfromEvent = fileRead.get2D_arraylist_string_from_file("preproc_Lv1_getSAMfromEvent.csv", ",");
		//ArrayList output_getKeyStrokeFeatures = fileRead.get2D_arraylist_string_from_file("preproc_Lv2_getKeyStrokeFeatures.csv", ",");
		
		ArrayList output_trainDataPreProc = new ArrayList();
		
				
		
		int primary_key = 0;
		for(int i = 0; i < fileList_Event.size(); i++){
			ArrayList file_content_Event = fileRead.get2D_arraylist_string_from_file(((String)fileList_Event.get(i)), ",");
			String[] splited_filepath = ((String)fileList_Event.get(i)).split("\\\\");
			String subject = splited_filepath[splited_filepath.length - 3].split("_")[1];
			long train_start_tick = 99999999999999L;
			long train_over_tick = 999999999999999L;
			String Order = null;
			for(int j = 0; j < file_content_Event.size(); j++){
				 
				if(((String)((ArrayList)file_content_Event.get(j)).get(3)).equals("Trainning strat"))train_start_tick = Long.parseLong(((String)((ArrayList)file_content_Event.get(j)).get(2)));
				if(((String)((ArrayList)file_content_Event.get(j)).get(3)).equals("Trainning over")){
					train_over_tick = Long.parseLong(((String)((ArrayList)file_content_Event.get(j)).get(2)));
					break;
				}
			}
			
			ArrayList output_trainDataPreProc_row_header = new ArrayList();			
			output_trainDataPreProc_row_header.add(String.valueOf(i+1)); // subject_id
			output_trainDataPreProc_row_header.add(subject); // subject
			output_trainDataPreProc_row_header.add(String.valueOf(train_start_tick)); // train_start_tick
			output_trainDataPreProc_row_header.add(String.valueOf(train_over_tick)); // train_over_tick
			
			
			
			ArrayList file_content_KeyStrokeMix = fileRead.get2D_arraylist_string_from_file(((String)fileList_KeyStroke_Mix.get(i)), ",");
			boolean startTraining = false;
			boolean endTraining = false;
			ArrayList KeyStrokeMix_trainingOnly = new ArrayList();
			for(int j = 0; j < file_content_KeyStrokeMix.size(); j++){
				if (Long.parseLong(((String)((ArrayList)file_content_KeyStrokeMix.get(j)).get(2))) > train_start_tick)startTraining = true;
				if (Long.parseLong(((String)((ArrayList)file_content_KeyStrokeMix.get(j)).get(2))) > train_over_tick)endTraining = true;
				
				if(!startTraining)continue;				
				if(endTraining)break;

				KeyStrokeMix_trainingOnly.add(file_content_KeyStrokeMix.get(j));			
			}
			
			//mark for "組次"
			int iteCount = 1;
			ArrayList KeyStrokeMix_trainingOnly_level2 = new ArrayList();
			ArrayList KeyStrokeMix_trainingOnly_level2_row = new ArrayList();
			for(int j = 0; j < KeyStrokeMix_trainingOnly.size(); j++){
				
				((ArrayList)KeyStrokeMix_trainingOnly.get(j)).add(0,String.valueOf(iteCount));
				
				KeyStrokeMix_trainingOnly_level2_row.add(((ArrayList)KeyStrokeMix_trainingOnly.get(j)));
				
				if(((String)((ArrayList)KeyStrokeMix_trainingOnly.get(j)).get(4)).equals("NumPad2") && ((String)((ArrayList)KeyStrokeMix_trainingOnly.get(j)).get(5)).equals("1")){
					KeyStrokeMix_trainingOnly_level2.add(KeyStrokeMix_trainingOnly_level2_row);
					KeyStrokeMix_trainingOnly_level2_row = new ArrayList();
					iteCount++;
				}
			}
			
			
			//KeyStroke_Mix_ALL obtained, now proc. the features!			
			KB_features_checkSuccess(KeyStrokeMix_trainingOnly_level2);
			
			//Brute Force extract all features from successful typed sequences
			ArrayList KB_features_result = (ArrayList)KB_features_DL_brute(KeyStrokeMix_trainingOnly_level2);
			
			for(int j = 0; j < KB_features_result.size(); j++){
				
				ArrayList output_trainDataPreProc_row = new ArrayList();
				
				output_trainDataPreProc_row.addAll(output_trainDataPreProc_row_header);
				output_trainDataPreProc_row.addAll((ArrayList)KB_features_result.get(j));
				
				output_trainDataPreProc.add(output_trainDataPreProc_row);
				
				/*for(int k = 0; k < KeyStrokeMix_trainingOnly_level2_row.size(); k++){
					ArrayList output_trainDataPreProc_row = new ArrayList();
					
					output_trainDataPreProc_row.addAll(output_trainDataPreProc_row_header);
					output_trainDataPreProc_row.addAll(KeyStrokeMix_trainingOnly_level2_row);
					
					output_trainDataPreProc.add(output_trainDataPreProc_row);
				}*/
				
			}
			
			
			
			
			
		}		
		System.out.println("---preproc_trainingData() Done---");
		System.out.println("---ALL Done---");
		
		
		fileWrite.write_2d_arraylist_string2file("preproc_output_trainDataPreProc.csv", output_trainDataPreProc, true); // no append
				
	}
	
	public static void batchLabVIEWtick2CSharp_main() {
		
		final String inputDir = "E:/_DataBase/201305 101B_CFM_EXPR2 (52)/raw_data";
				
		FileTraversal_manager fm = new FileTraversal_manager();						
		Hashtable dir_tab;
		ArrayList fileList_tmp = new ArrayList();
		ArrayList fileList_physi = new ArrayList();
		
		try {
			fm = new FileTraversal_manager();
			dir_tab = fm.traverse_and_build_two_level_dir(new File(inputDir));				
			Enumeration e = dir_tab.keys();			
			
			while( e.hasMoreElements() )
			{
				String DirName = e.nextElement().toString();
				if (DirName.length() > 9){
					fileList_tmp.addAll((ArrayList)dir_tab.get(DirName)); // get all files in the directories
				}
			}
						
			for(int i = 0; i < fileList_tmp.size(); i++){
				if (((String)fileList_tmp.get(i)).endsWith("physi.csv"))fileList_physi.add(fileList_tmp.get(i));
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		batchLabVIEWtick2CSharp(fileList_physi, "physi.csv", "physi_CSharpTick");
				
	}
	
	public static void batchLabVIEWtick2CSharp(ArrayList filepaths, String oldfileName, String newfileName){
	
		
		for(int i = 0; i < filepaths.size(); i++){
			ArrayList file_content = fileRead.get2D_arraylist_string_from_file(((String)filepaths.get(i)), ",");
			
			LabVIEWtick2CSharp t1 = new LabVIEWtick2CSharp();
			t1.SetLabVIEWTimeBasesWithCSharp(((String)((ArrayList)file_content.get(0)).get(1)),Long.parseLong(((String)((ArrayList)file_content.get(0)).get(2))));
					
			for(int j = 1; j < file_content.size(); j++){
				((ArrayList)file_content.get(j)).set(4, String.valueOf(t1.LabVIEWtick2CSharp_exec(((String)((ArrayList)file_content.get(j)).get(4)))));
				((ArrayList)file_content.get(j)).set(5, String.valueOf(t1.LabVIEWtick2CSharp_exec(((String)((ArrayList)file_content.get(j)).get(5)))));
				((ArrayList)file_content.get(j)).set(6, String.valueOf(t1.LabVIEWtick2CSharp_exec(((String)((ArrayList)file_content.get(j)).get(6)))));
				((ArrayList)file_content.get(j)).set(7, String.valueOf(t1.LabVIEWtick2CSharp_exec(((String)((ArrayList)file_content.get(j)).get(7)))));
			}
			
			fileWrite.write_2d_arraylist_string2file(((String)filepaths.get(i)).substring(0,((String)filepaths.get(i)).length()-1-oldfileName.length())+newfileName, file_content, true); // no append
		}
		
		
	}
	
	
	

	public static void mainAndkeystroke() {

		final String inputDir = "E:/_DataBase/201305 101B_CFM_EXPR2 (52)/raw_data";
		
		
		FileTraversal_manager fm = new FileTraversal_manager();						
		Hashtable dir_tab;
		ArrayList fileList_tmp = new ArrayList();
		ArrayList fileList_Event = new ArrayList();
		ArrayList fileList_KeyStroke_Mix = new ArrayList();
		try {
			fm = new FileTraversal_manager();
			dir_tab = fm.traverse_and_build_two_level_dir(new File(inputDir));				
			Enumeration e = dir_tab.keys();			
			
			while( e.hasMoreElements() )
			{
				String DirName = e.nextElement().toString();
				if (DirName.length() > 9){
					fileList_tmp.addAll((ArrayList)dir_tab.get(DirName)); // get all files in the directories
				}
			}
						
			for(int i = 0; i < fileList_tmp.size(); i++){
				if (((String)fileList_tmp.get(i)).endsWith("Event.txt"))fileList_Event.add(fileList_tmp.get(i));
				if (((String)fileList_tmp.get(i)).endsWith("KeyStroke_Mix.txt"))fileList_KeyStroke_Mix.add(fileList_tmp.get(i));
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		ArrayList output_getSAMfromEvent = getSAMfromEvent(fileList_Event);
		ArrayList output_getKeyStrokeFeatures = getKeyStrokeFeatures(fileList_KeyStroke_Mix, output_getSAMfromEvent);
		
		fileWrite.write_2d_arraylist_string2file("preproc_Lv1_getSAMfromEvent.csv", output_getSAMfromEvent, true); // no append
		fileWrite.write_2d_arraylist_string2file("preproc_Lv2_getKeyStrokeFeatures.csv", output_getKeyStrokeFeatures, true); // no append
		
		

		
		System.out.println("---ALL Done---");
		
	}

}
