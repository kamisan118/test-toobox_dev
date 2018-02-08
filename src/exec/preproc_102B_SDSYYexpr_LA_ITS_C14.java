package exec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import preproc.proc_manager_KM_C04data_wo_Mouse;
import arrayOp.Array2DAppend;

import fsOp.FileTraversal_manager;
import fsOp.fileRead;
import fsOp.fileWrite;

public class preproc_102B_SDSYYexpr_LA_ITS_C14 {

	/*Debug:
		若出現
		Exception in thread "main" java.lang.NullPointerException
		at fsOp.FileTraversal_manager.traverse_and_build_two_level_dir_hashTable_in2ndDirListing(FileTraversal_manager.java:59)
		at exec.preproc_102B_SDSYYexpr_LA_ITS_C14.main(preproc_102B_SDSYYexpr_LA_ITS_C14.java:403)
		就是代表不是兩層Dir結構, 很可能第一層Dir有file!*/	
	
	final static double errorConst = -9999.0;
	final static long missingDataFlag = -99L;
	
	final static long episode_threshold_max_keystroke = 3000L; //second
	final static long episode_threshold_max_mouse = 1000L; //second
	
	
	static String inputDir = "D:/ALL_Users/pmli/analysis WK/sd-syy_expr/20140115_1612/data";
	static String inputConfDir = "D:/ALL_Users/pmli/analysis WK/sd-syy_expr/20140115_1612/conf";	
	
	/**
	 * @param args
	 */
	
	public static void Mouse_Move_CSV_FormatAdj(String path){
		
		
		
		try
	    {	    
          //create BufferedReader to read csv file
          BufferedReader br = new BufferedReader( new FileReader(path));
          String fileContent = br.readLine();
          String[] fileContent_StrArr = fileContent.split(",,");
          
          ArrayList al = new ArrayList();	      		
          for (int i = 0; i < fileContent_StrArr.length; i++){
        	  ArrayList al_row = new ArrayList();
        	  al_row.add(fileContent_StrArr[i]);	            	  
        	  al.add(al_row);
          }
	    
          fileWrite.write_2d_arraylist_string2file(path.replace(".csv", "")+"(Fixed).csv", al, true); // no append
	                        
	    }
	    catch(Exception e)
	    {
	              System.out.println("Exception while reading csv file: " + e);                  
	    }
		
	}
	
	
	public static int get_TurnCSV_row_by_turnNum(String turnNumber, ArrayList TurnStartCSV){
		return get_TurnCSV_row_by_turnNum(turnNumber, 1, TurnStartCSV); // only used for TurnStart and TurnEnd
	}
	
	public static int get_TurnCSV_row_by_turnNum(String turnNumber, int tartgetCol, ArrayList TurnStartCSV){
		
		for(int i=0; i<TurnStartCSV.size();i++){
			if (((String)((ArrayList)TurnStartCSV.get(i)).get(1)).equals(turnNumber))
				return i;
		}
	
		return (int) errorConst;
	}
	
	
	
	

	public static ArrayList getRowInWindow(ArrayList fileContent, long st_time, long ed_time){
		return getRowInWindow(fileContent, st_time, ed_time, 0);
	}
	
	public static ArrayList getRowInWindow(ArrayList fileContent, long st_time, long ed_time, int tickCol){
		
		ArrayList rowInWindow = new ArrayList();
		
		for(int i=0; i <fileContent.size();i++){
			if(((String)((ArrayList)fileContent.get(i)).get(tickCol)).matches("^\\d{1,}$")){ //because some data rows are broke...orz
				long tick_of_row = Long.parseLong((String)((ArrayList)fileContent.get(i)).get(tickCol));
				
				if((tick_of_row >= st_time) && (tick_of_row <= ed_time))
					rowInWindow.add(fileContent.get(i));	
			}				
		}
		
		return rowInWindow;
		
	}
	
	
	
	
	
	public static ArrayList get_keyStrokeDurations(ArrayList InWin_KeyStroke_Mix_CSV){
				
		ArrayList keyStrokeDurations = new ArrayList();
		
		for(int i=0; i<InWin_KeyStroke_Mix_CSV.size();i++){
			if (((ArrayList)InWin_KeyStroke_Mix_CSV.get(i)).size() >= 3){
				keyStrokeDurations.add(String.valueOf( 
					(Long.parseLong(((String)((ArrayList)InWin_KeyStroke_Mix_CSV.get(i)).get(2))) 
						- Long.parseLong(((String)((ArrayList)InWin_KeyStroke_Mix_CSV.get(i)).get(1))))
						));				
			}
		}
		
		return keyStrokeDurations;
		
	}
	
	public static ArrayList get_keyStrokeLatencies(ArrayList InWin_KeyStroke_Mix_CSV){
		ArrayList keyStrokeLatencies = new ArrayList();
		
		for(int i=1; i<InWin_KeyStroke_Mix_CSV.size();i++){
			if (((ArrayList)InWin_KeyStroke_Mix_CSV.get(i)).size() >= 3)
				if(((Long.parseLong(((String)((ArrayList)InWin_KeyStroke_Mix_CSV.get(i)).get(1))) 
						- Long.parseLong(((String)((ArrayList)InWin_KeyStroke_Mix_CSV.get(i-1)).get(1))))) <= episode_threshold_max_keystroke){
					
					long latency = ((Long.parseLong(((String)((ArrayList)InWin_KeyStroke_Mix_CSV.get(i)).get(1))) 
							- Long.parseLong(((String)((ArrayList)InWin_KeyStroke_Mix_CSV.get(i-1)).get(1)))));
					
					keyStrokeLatencies.add(String.valueOf(latency)); 
							
				}				
		}
		
		
		return keyStrokeLatencies;		
	}
	
	
	public static ArrayList get_MouseClickDurations(ArrayList InWin_Mouse_Mix_CSV){
		ArrayList MouseClickDurations = new ArrayList();
		
		for(int i=0; i<InWin_Mouse_Mix_CSV.size();i++){
			if (((ArrayList)InWin_Mouse_Mix_CSV.get(i)).size() >= 2){
				MouseClickDurations.add(String.valueOf( 
					(Long.parseLong(((String)((ArrayList)InWin_Mouse_Mix_CSV.get(i)).get(1))) 
						- Long.parseLong(((String)((ArrayList)InWin_Mouse_Mix_CSV.get(i)).get(0))))
						));				
			}
		}
		
		return MouseClickDurations;
		
	}
	
	public static ArrayList get_MouseClickLatencies(ArrayList InWin_Mouse_Mix_CSV){
		ArrayList MouseClickLatencies = new ArrayList();
		
		for(int i=1; i<InWin_Mouse_Mix_CSV.size();i++){
			if (((ArrayList)InWin_Mouse_Mix_CSV.get(i)).size() >= 2)
				if(((Long.parseLong(((String)((ArrayList)InWin_Mouse_Mix_CSV.get(i)).get(0))) 
						- Long.parseLong(((String)((ArrayList)InWin_Mouse_Mix_CSV.get(i-1)).get(0))))) <= episode_threshold_max_mouse){
					
					long latency = ((Long.parseLong(((String)((ArrayList)InWin_Mouse_Mix_CSV.get(i)).get(0))) 
							- Long.parseLong(((String)((ArrayList)InWin_Mouse_Mix_CSV.get(i-1)).get(0)))));
					
					MouseClickLatencies.add(String.valueOf(latency)); 
				}				
		}
		
		
		return MouseClickLatencies;
	}
	
	public static ArrayList get_MouseMove_goalReachSpeeds(ArrayList InWin_Mouse_Mix_CSV, ArrayList InWin_Mouse_Move_CSV){
		
		int ite_InWin_Mouse_Move_CSV = 0;
		
		ArrayList goalReachSpeeds = new ArrayList();
		
		for(int i = 0; i < InWin_Mouse_Mix_CSV.size(); i++){
			long goalReachTick = Long.parseLong(((String)((ArrayList)InWin_Mouse_Mix_CSV.get(i)).get(0)));
			long goalReachingTick = (long) errorConst; 
			long goalReachedTick = Long.parseLong(((String)((ArrayList)InWin_Mouse_Move_CSV.get(ite_InWin_Mouse_Move_CSV)).get(0)));
			while(goalReachedTick < goalReachTick){
				ite_InWin_Mouse_Move_CSV++;
				goalReachingTick = goalReachedTick;
				
				if(ite_InWin_Mouse_Move_CSV >= InWin_Mouse_Move_CSV.size()){					
					break;
				}
				else
					goalReachedTick = Long.parseLong(((String)((ArrayList)InWin_Mouse_Move_CSV.get(ite_InWin_Mouse_Move_CSV)).get(0)));
			}			
			ite_InWin_Mouse_Move_CSV--;			
			
			int goal_x = Integer.parseInt((((String)((ArrayList)InWin_Mouse_Mix_CSV.get(i)).get(2))));
			int goal_y = Integer.parseInt((((String)((ArrayList)InWin_Mouse_Mix_CSV.get(i)).get(3))));
			int last_x = Integer.parseInt((((String)((ArrayList)InWin_Mouse_Move_CSV.get(ite_InWin_Mouse_Move_CSV)).get(1))));
			int last_y = Integer.parseInt((((String)((ArrayList)InWin_Mouse_Move_CSV.get(ite_InWin_Mouse_Move_CSV)).get(2))));
			
			goalReachSpeeds.add(String.valueOf((Math.sqrt(Math.pow((goal_x - last_x), 2) + Math.pow((goal_y - last_y), 2)))
			/(((double)(goalReachTick - goalReachingTick))/1000)));
			
			 
		}
		
		return goalReachSpeeds;
	}
	
	
	public static ArrayList preprocessor2(ArrayList extData2, int subjectNum, String subjectName, Hashtable subjectFiles) {
		
		ArrayList Pretest_StartCSV = fileRead.get2D_arraylist_string_from_file((String)subjectFiles.get("Pretest_Start.csv"), ",");
		ArrayList Pretest_EndCSV = fileRead.get2D_arraylist_string_from_file((String)subjectFiles.get("Pretest_End.csv"), ",");
		ArrayList Pretest_summary_EndCSV = fileRead.get2D_arraylist_string_from_file((String)subjectFiles.get("Pretest_summary.csv"), ",");
		ArrayList Posttest_StartCSV = fileRead.get2D_arraylist_string_from_file((String)subjectFiles.get("Posttest_Start.csv"), ",");
		ArrayList Postest_EndCSV = fileRead.get2D_arraylist_string_from_file((String)subjectFiles.get("Posttest_End.csv"), ",");
		
		ArrayList mainCSV = fileRead.get2D_arraylist_string_from_file(inputConfDir+"/main.csv", ",");
		
		int ite = 0;
		String group = null;
		while(true){
			if(((String)((ArrayList)mainCSV.get(ite)).get(0)).equals(subjectName))
			{
				group = ((String)((ArrayList)mainCSV.get(ite)).get(3));
				break;
			}			
			ite++;
		}
		
		
		
//寫法1		
		/*String pretest_score = null;
		if(Pretest_summary_EndCSV.size() >=1)
			pretest_score = ((String)((ArrayList)Pretest_summary_EndCSV.get(0)).get(1));
		else
			return extData2; // give away this data
*/	
		
		
//寫法2
		/*int pretest_score = 0;		
		for(int i = 0; i < Pretest_EndCSV.size(); i++){
			if(Boolean.parseBoolean(((String)((ArrayList)Pretest_EndCSV.get(i)).get(4)))){
				pretest_score++;
			}
		}
		
		int posttest_score = 0;
		for(int i = 0; i < Postest_EndCSV.size(); i++){
			if(Boolean.parseBoolean(((String)((ArrayList)Postest_EndCSV.get(i)).get(4)))){
				posttest_score++;
			}
		}*/

//寫法3
		
		int posttest_score = 0;
		for(int i = 0; i < Postest_EndCSV.size(); i++){
			if(Boolean.parseBoolean(((String)((ArrayList)Postest_EndCSV.get(i)).get(4)))){
				posttest_score++;
			}
		}
		
		ite = 0;
		int pretest_score = 0;
		int adj_posttest_score = 0;
		for(int i = 0; i < Pretest_EndCSV.size(); i++){
			if(Boolean.parseBoolean(((String)((ArrayList)Pretest_EndCSV.get(i)).get(4)))){
				pretest_score++;
			}
			
			if ((i % 2) == 0)
				ite = i+1;
			else
				ite = i-1;

			if(Postest_EndCSV.size() >= (ite+1)){
				if(Boolean.parseBoolean(((String)((ArrayList)Postest_EndCSV.get(ite)).get(4)))){
					adj_posttest_score++;
				}
			}
			else{
				if(Boolean.parseBoolean(((String)((ArrayList)Pretest_EndCSV.get(i)).get(4)))){
					adj_posttest_score++;
				}
			}
					
		}
		
		
		ArrayList extData2_row = new ArrayList();
		
		extData2_row.add(subjectName);								//subjectName
		extData2_row.add(group);									//group
		extData2_row.add(String.valueOf(pretest_score));			//pretest_score
		extData2_row.add(String.valueOf(posttest_score));			//posttest_score
		extData2_row.add(String.valueOf(adj_posttest_score));		//adj_posttest_score
		
		
		
		extData2.add(extData2_row);	
		return extData2;
		
	}
	
	
	public static ArrayList preprocessor(ArrayList extData, int subjectNum, String subjectName, Hashtable subjectFiles) {
		
		ArrayList SurveyCSV = fileRead.get2D_arraylist_string_from_file((String)subjectFiles.get("Survey.csv"), ",");
		ArrayList TurnStartCSV = fileRead.get2D_arraylist_string_from_file((String)subjectFiles.get("TurnStart.csv"));
		ArrayList TurnEndCSV = fileRead.get2D_arraylist_string_from_file((String)subjectFiles.get("TurnEnd.csv"));
		ArrayList TurnAnsweringCSV = fileRead.get2D_arraylist_string_from_file((String)subjectFiles.get("TurnAnswering.csv"));
		
		ArrayList KeyStroke_MixCSV = fileRead.get2D_arraylist_string_from_file((String)subjectFiles.get("KeyStroke_Mix.csv"));
		ArrayList Mouse_MixCSV = fileRead.get2D_arraylist_string_from_file((String)subjectFiles.get("Mouse_Mix.csv"));
		
		
		int max_pri_key;
		
		for(int i = 0; i < SurveyCSV.size(); i++){
			max_pri_key = extData.size();			
			ArrayList extData_row = new ArrayList();
						
			String turnNumber = (String)((ArrayList)SurveyCSV.get(i)).get(1);
			
			//TurnStart
			int turnNumberTOite = get_TurnCSV_row_by_turnNum(turnNumber, TurnStartCSV);
			long TurnStartTickTime = Long.parseLong(((String)((ArrayList)TurnStartCSV.get(turnNumberTOite)).get(0)));
			String KC = ((String)((ArrayList)TurnStartCSV.get(turnNumberTOite)).get(2));
			String turnNumberKC = ((String)((ArrayList)TurnStartCSV.get(turnNumberTOite)).get(3));
			String turnType = ((String)((ArrayList)TurnStartCSV.get(turnNumberTOite)).get(5)); // turnType: 定義0/範例1/測驗2
			
			//TurnEnd
			turnNumberTOite = get_TurnCSV_row_by_turnNum(turnNumber, TurnEndCSV);
			long TurnEndTickTime = Long.parseLong(((String)((ArrayList)TurnEndCSV.get(turnNumberTOite)).get(0)));
			
			//TurnAnswer
			turnNumberTOite = get_TurnCSV_row_by_turnNum(turnNumber, TurnAnsweringCSV);
			String turnResult = null; // turnResult: 該turn答對1答錯2無需答題0
			if(((String)((ArrayList)TurnAnsweringCSV.get(turnNumberTOite)).get(5)).equalsIgnoreCase("D"))
				turnResult = "0";
			else{
				if (Boolean.parseBoolean(((String)((ArrayList)TurnAnsweringCSV.get(turnNumberTOite)).get(4))))
					turnResult = "1";
				else
					turnResult = "2";
			}
				
			
			
			long surveyEndTime = Long.parseLong((String)((ArrayList)SurveyCSV.get(i)).get(0));
			
			ArrayList keyStrokeDurations = get_keyStrokeDurations(
					getRowInWindow(KeyStroke_MixCSV, TurnStartTickTime, surveyEndTime, 1));
			
			ArrayList keyStrokeLatencies = get_keyStrokeLatencies(
					getRowInWindow(KeyStroke_MixCSV,TurnStartTickTime, surveyEndTime, 1));
			
			ArrayList MouseClickDurations = get_MouseClickDurations(
					getRowInWindow(Mouse_MixCSV, TurnStartTickTime, surveyEndTime, 0));
			
			ArrayList MouseClickLatencies = get_MouseClickLatencies(
					getRowInWindow(Mouse_MixCSV, TurnStartTickTime, surveyEndTime, 0));
			
		/*	ArrayList MouseMove_goalReachSpeeds = get_MouseMove_goalReachSpeeds(
					getRowInWindow(
							fileRead.get2D_arraylist_string_from_file((String)subjectFiles.get("Mouse_Mix.csv")), 
							TurnStartTickTime, surveyTime, 0), 
					getRowInWindow(
							fileRead.get2D_arraylist_string_from_file((String)subjectFiles.get("Mouse_Move(Fixed).csv")), 
							TurnStartTickTime, surveyTime, 0));*/
			
			//extData_row.add(String.valueOf(max_pri_key+1));		//pri_key
			//extData_row.add(String.valueOf(subjectNum));		//subjectNum -- 目標要跟main.csv結合, 但目前還沒有做
			extData_row.add(subjectName);						//subjectName

			extData_row.add(String.valueOf(util.math.general.avgValue(keyStrokeDurations))); // AVG keyStrokeDuration
			//extData_row.add(String.valueOf(util.math.general.stdValue(keyStrokeDurations))); // sd keyStrokeDuration
			//extData_row.add(String.valueOf(util.math.general.CV_Value(keyStrokeDurations))); // cv keyStrokeDuration
			
			//extData_row.add(String.valueOf(util.math.general.avgValue(keyStrokeLatencies))); // AVG keyStrokeLatency
			//extData_row.add(String.valueOf(util.math.general.stdValue(keyStrokeLatencies))); // sd keyStrokeLatency
			//extData_row.add(String.valueOf(util.math.general.CV_Value(keyStrokeLatencies))); // cv keyStrokeLatency
			
			extData_row.add(String.valueOf(util.math.general.avgValue(MouseClickDurations))); // AVG MouseClickDuration
			//extData_row.add(String.valueOf(util.math.general.stdValue(MouseClickDurations))); // sd MouseClickDuration
			//extData_row.add(String.valueOf(util.math.general.CV_Value(MouseClickDurations))); // cv MouseClickDuration			
			
			//extData_row.add(String.valueOf(util.math.general.avgValue(MouseClickLatencies))); // AVG MouseClickLatency
			//extData_row.add(String.valueOf(util.math.general.stdValue(MouseClickLatencies))); // sd MouseClickLatency
			//extData_row.add(String.valueOf(util.math.general.CV_Value(MouseClickLatencies))); // cv MouseClickLatency		
			
			//extData_row.add(String.valueOf(util.math.general.avgValue(MouseMove_goalReachSpeeds))); // AVG MouseMove_goalReachSpeed
			//extData_row.add(String.valueOf(util.math.general.stdValue(MouseMove_goalReachSpeeds))); // sd MouseMove_goalReachSpeed
			//extData_row.add(String.valueOf(util.math.general.CV_Value(MouseMove_goalReachSpeeds))); // cv MouseMove_goalReachSpeed					
			
			extData_row.add(KC);				// KC 
			extData_row.add(turnNumberKC);		// turnNumber of KC
			extData_row.add(turnType);			// turnType: 定義0/範例1/測驗2
			extData_row.add(turnResult);		// turnResult: 該turn答對1答錯2無需答題0
						
			extData_row.add(((ArrayList)SurveyCSV.get(i)).get(2)); //Valence
			extData_row.add(((ArrayList)SurveyCSV.get(i)).get(3)); //Arousal
			extData_row.add(((ArrayList)SurveyCSV.get(i)).get(4)); //Skill
			extData_row.add(((ArrayList)SurveyCSV.get(i)).get(5)); //Challenge
			
			/*
			 subjectName
			 AVG_keyStrokeDuration
			 AVG_MouseClickDuration
			 KC
			 turnNumber
			 turnType
			 turnResult
			 Valence 
			 Arousal
			 Skill
			 Challenge
			 */
			
			
			extData.add(extData_row);
		}
		
				
		
		
		return extData;
	}
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
				
		
		
		
        try {
        	FileTraversal_manager fm = new FileTraversal_manager();			
			Hashtable dir_tab = fm.traverse_and_build_two_level_dir_hashTable_in2ndDirListing(new File(inputDir));		
			Enumeration e = dir_tab.keys();
	        	              
	        ArrayList extData = new ArrayList();
	        ArrayList extData2 = new ArrayList();
	        
			
			int subjectNum = 0;
			String subjectName;
	        while( e.hasMoreElements() ){
	        	subjectNum++;
	        	subjectName = (String) e.nextElement();
	        	System.out.println("Subject: " + String.valueOf(subjectNum) + "," + subjectName);
	        	
	        	//以下這行只須執行一次(建立出Fixed檔即可):
	        	//Mouse_Move_CSV_FormatAdj((String) ((Hashtable)dir_tab.get(subjectName)).get("Mouse_Move.csv"));
	        	
	        	extData = preprocessor(extData,subjectNum,subjectName,(Hashtable)dir_tab.get(subjectName));
	        	extData2 = preprocessor2(extData2,subjectNum,subjectName,(Hashtable)dir_tab.get(subjectName));
	        	       	    	 	    		           
	        }			        	      
			
	        
	        fileWrite.write_2d_arraylist_string2file("preproc.csv", extData, true); // no append
	        fileWrite.write_2d_arraylist_string2file("preproc2.csv", extData2, true); // no append
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
	}

}
