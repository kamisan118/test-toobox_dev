package preproc.J16;

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
import fsOp.fileRead;
import fsOp.fileWrite;
import fsOp.killFilesAtRoot;


class MyIntComparable_CFM_EXPR2 implements Comparator<ArrayList>{
	int tarCol;
	int picCol;
	Hashtable ht_pic_avgs;
	
	public MyIntComparable_CFM_EXPR2(int picCol, int tarCol, Hashtable ht_pic_avgs){
		this.picCol = picCol;
		this.tarCol = tarCol;
		this.ht_pic_avgs = ht_pic_avgs;
	}
	
    @Override
    public int compare(ArrayList o1, ArrayList o2) {
    	float t1;
    	float t2;
    	
    	//因為不會有"N/A"所以不再需要這個檢查
    	t1 = Float.parseFloat(((String)o1.get(tarCol)));
    	t2 = Float.parseFloat(((String)o2.get(tarCol)));
/*    	if (((String)o1.get(tarCol)).equals("N/A")){
    		t1 = Float.parseFloat(((String)ht_pic_avgs.get(((String)o1.get(picCol)))));
    	}    		
    	else
    		t1 = Float.parseFloat(((String)o1.get(tarCol)));
    	
    	if (((String)o2.get(tarCol)).equals("N/A"))
        	t2 = Float.parseFloat(((String)ht_pic_avgs.get(((String)o2.get(picCol)))));
        else
        	t2 = Float.parseFloat(((String)o2.get(tarCol)));*/
        	
    	//return (int)(t1 - t2);
    	return (t1>t2 ? 1 : (t1<t2 ? -1 : 0));
    	
    	//return (o1>o2 ? -1 : (o1==o2 ? 0 : 1));
    	// -1(<0)就是排前面, 0(==0)就是對等, 1(>0)就是排後面
    }
}

public class preproc_manager_100B_CFM_EXPR2 {

	/**
	 * @param args
	 */
	
		
	public static void test_main_backup_singleSubject(){

		ArrayList al_in = fileRead.get2D_arraylist_string_from_file("Male-9617072-24.txt", "\t");		
		ArrayList al_tmp = new ArrayList(), al_output_tmp_row;
		
		LabVIEWtick2CSharp t1 = new LabVIEWtick2CSharp();		
		t1.SetLabVIEWTimeBasesWithCSharp((String) ((ArrayList)al_in.get(1)).get(1), 
				Long.parseLong((String) ((ArrayList)al_in.get(1)).get(2)));
		
		
		
		for (int i=3; i < al_in.size(); i+=2){
			al_tmp.add(al_in.get(i+1));	
		}
		
		
		int key_id = 1;
		int subjectNum = 1;
		long TrialStartTimeCSharpTick = t1.LabVIEWtick2CSharp_exec((String) ((ArrayList)al_in.get(1)).get(2));
		ArrayList al_output = new ArrayList();
		for (int i=0; i < al_tmp.size(); i+=1)
		{
			al_output_tmp_row = new ArrayList();
			
			al_output_tmp_row.add(String.valueOf(key_id));//key_id			
			al_output_tmp_row.add(String.valueOf(TrialStartTimeCSharpTick));//TrialStartTimeCSharpTick (C#); plus 18 sec. each time.
			al_output_tmp_row.add(((ArrayList)al_tmp.get(i)).get(0));//picNum
			al_output_tmp_row.add(String.valueOf(subjectNum));//subject
			al_output_tmp_row.add(((ArrayList)al_tmp.get(i)).get(1));//V
			al_output_tmp_row.add(((ArrayList)al_tmp.get(i)).get(2));//A
			
			al_output.add(al_output_tmp_row);
			key_id++;
			TrialStartTimeCSharpTick = TrialStartTimeCSharpTick + 290000000L;
		}
		fileWrite.write_2d_arraylist_string2file("Male-9617072-24b.csv", al_output, false);
	}
	
	public static void pic_results_gen(){
		long TickBias = 3000000L; // Mouse與SAM的交界部分要修正 >> 透過把邊界向後移0.3sec (3000000: tick)的方式 -- 這是察看實驗數據得到的數字
		final String inputDir = "E:/_DataBase/201203 100B CFM_EXPR2 (24+7)/raw data";

		FileTraversal_manager fm = new FileTraversal_manager();						
		Hashtable dir_tab;
		
		ArrayList Time_Of4Completes_plus_SAM_Valid = new ArrayList();
		
		try {
			dir_tab = fm.traverse_and_build_two_level_dir(new File(inputDir));
			Enumeration e = dir_tab.keys();
			
			int subjectNum = 0, key_id = 0, key_id_for_timeTaskComplete = 0, gender = -1;
			long TrialStartTimeCSharpTick, TrialStartTimeCSharpTick_Next;;
			
			ArrayList al_in, al_tmp, al_tmp2, al_output_tmp_row;
    		LabVIEWtick2CSharp t1;
    		ArrayList al_output = new ArrayList();
    		
    		String dir_name;
    		String file_path;
    		
    		String valence, arousal;
    		
    		ArrayList id_timeTaskcomplete = new ArrayList();
    		
			while( e.hasMoreElements() )
			{
				dir_name = (String) e.nextElement();
				
				if(dir_name.matches("[0-9]{3,}")){
				//if(dir_name.matches("[0-9]{3,}_[A-Z]{1,3}")){
					//>> 後來直接在raw_data改..
					
					//找出data file, 依照format
					al_tmp = ((ArrayList)dir_tab.get(dir_name));					
					file_path = "NO INPUT";
					
					for(int i = 0; i< al_tmp.size(); i++){
						if(((String)al_tmp.get(i)).contains("-" + dir_name + "-"))
							file_path = (String)al_tmp.get(i);
					}
						
		        	subjectNum++;
		        
		        	
		        	if ((new File(file_path)).getName().split("-")[0].equals("Male"))
		        		gender = 1;
		        	else if ((new File(file_path)).getName().split("-")[0].equals("Female"))
		        		gender = 0;
		        	else
		        		gender = -1;
		        	
		    		al_in = fileRead.get2D_arraylist_string_from_file(file_path, "\t", "Big5");		
		    		al_tmp = new ArrayList();
		    		
		    		t1 = new LabVIEWtick2CSharp();		
		    		t1.SetLabVIEWTimeBasesWithCSharp((String) ((ArrayList)al_in.get(1)).get(1), 
		    				Long.parseLong((String) ((ArrayList)al_in.get(1)).get(2)));
		    		
		    		
		    		for (int i=3; i < al_in.size(); i+=2)
		    		{
		    			
		    			key_id_for_timeTaskComplete++;
		    			ArrayList tmpRow = new ArrayList();
		    			tmpRow.add(String.valueOf(key_id_for_timeTaskComplete)); // 順序: event_id, keyboard task complete time, mouse task complete time
		    			tmpRow.add(String.valueOf(t1.LabVIEWtick2CSharp_exec((String)((ArrayList)al_in.get(i)).get(0))));
		    			tmpRow.add(String.valueOf(t1.LabVIEWtick2CSharp_exec((String)((ArrayList)al_in.get(i)).get(2))));		    			
		    			id_timeTaskcomplete.add(tmpRow);
		    			
		    			al_tmp.add(al_in.get(i+1));		    			
		    		}
		    		
		    		TrialStartTimeCSharpTick_Next = t1.LabVIEWtick2CSharp_exec((String) ((ArrayList)al_in.get(1)).get(2));	    		
		    		
		    		
		    		for (int i=0; i < al_tmp.size(); i+=1)
		    		{
		    			TrialStartTimeCSharpTick = TrialStartTimeCSharpTick_Next;
		    			key_id++;
		    			al_output_tmp_row = new ArrayList();
		    			
		    			al_output_tmp_row.add(String.valueOf(key_id));//key_id			
		    			
		    			al_output_tmp_row.add(String.valueOf(TrialStartTimeCSharpTick));
		    			//TrialStartTimeCSharpTick (C#); plus 18 sec. each time.
		    			
		    			al_output_tmp_row.add(((ArrayList)al_tmp.get(i)).get(0));//picNum
		    			al_output_tmp_row.add(String.valueOf(subjectNum));//subject
		    			al_output_tmp_row.add(dir_name);//subject學號
		    			al_output_tmp_row.add(String.valueOf(gender));//subject 性別
		    			
		    			valence = (String)((ArrayList)al_tmp.get(i)).get(1);
		    			if (valence.contains("Inf")) valence = "N/A";
		    			arousal = (String)((ArrayList)al_tmp.get(i)).get(2);
		    			if (arousal.contains("Inf")) arousal = "N/A";
		    			al_output_tmp_row.add(valence);//V
		    			al_output_tmp_row.add(arousal);//A
		    			
		    			al_output.add(al_output_tmp_row);    			
		    			TrialStartTimeCSharpTick_Next = TrialStartTimeCSharpTick + 290000000L; // --> 29秒/Trial
		    			
		    			ArrayList Time_Of4Completes_plus_SAM_Valid_row = new ArrayList();
	    				/* "Time_Of4Completes_plus_SAM_Valid.csv"
	    				 * key_id
	    				 * Time_Complete_KeyBoardTask	// 完成keyboard task所花的時間
	    				 * Time_Complete_MouseTask		// 完成mouse task所花的時間
	    				 * Time_Complete_KMtask			// 完成keyboard + mouse task所花的時間
	    				 * Time_FOR_SAM					// "留了"多少時間來填SAM -- 有可能沒填完
	    				 * SAM_Valid					// SAM有填到而且有效1 (只要有一個N/A就不算), o.w., 0
	    				 */
	    				Time_Of4Completes_plus_SAM_Valid_row.add(String.valueOf(key_id));		    				
	    				Time_Of4Completes_plus_SAM_Valid_row.add(String.valueOf(Long.parseLong(((String)((ArrayList)id_timeTaskcomplete.get(key_id - 1)).get(1))) - TrialStartTimeCSharpTick));
	    				Time_Of4Completes_plus_SAM_Valid_row.add(String.valueOf(Long.parseLong(((String)((ArrayList)id_timeTaskcomplete.get(key_id - 1)).get(2))) - Long.parseLong(((String)((ArrayList)id_timeTaskcomplete.get(key_id - 1)).get(1)))));
	    				Time_Of4Completes_plus_SAM_Valid_row.add(String.valueOf(Long.parseLong(((String)((ArrayList)id_timeTaskcomplete.get(key_id - 1)).get(2))) - TrialStartTimeCSharpTick));
	    				Time_Of4Completes_plus_SAM_Valid_row.add(String.valueOf(TrialStartTimeCSharpTick_Next - Long.parseLong(((String)((ArrayList)id_timeTaskcomplete.get(key_id - 1)).get(2)))));
	    				if (valence.equals("N/A") || arousal.equals("N/A"))
	    					Time_Of4Completes_plus_SAM_Valid_row.add("0");
	    				else
	    					Time_Of4Completes_plus_SAM_Valid_row.add("1");	    				
	    				Time_Of4Completes_plus_SAM_Valid.add(Time_Of4Completes_plus_SAM_Valid_row);
	    				
	    					
		    			boolean isNotLastRow = ((i+1) != al_tmp.size());
		    						    				
	    				KB_results_gen(isNotLastRow, TrialStartTimeCSharpTick, TrialStartTimeCSharpTick_Next, 
	    						((ArrayList)dir_tab.get("Data")), dir_name, key_id); // v3.0.0以前用的, 取整段trial來看Keyboard, 建議改成下面：
	    				/*KB_results_gen(TrialStartTimeCSharpTick, Long.parseLong(((String)((ArrayList)id_timeTaskcomplete.get(key_id)).get(1)))), 
						((ArrayList)dir_tab.get("Data")), dir_name, key_id);*/  
	    				//---比較strict, 但是擔心這麼做會讓接近endTick的資料extract時出問題
	    				
	    				
	    				//append the list
	    				((ArrayList)dir_tab.get("Data")).addAll(((ArrayList)dir_tab.get("MouseMoveData")));
	    				
	    				//mouse的部分比較strick一點, 接近endtick的部分如果有超過就直接不要了!
	    				// <key_id - 1, 因為array index需要減1>
	    				mouse_results_gen(isNotLastRow, Long.parseLong(((String)((ArrayList)id_timeTaskcomplete.get(key_id - 1)).get(1))), 
	    						Long.parseLong(((String)((ArrayList)id_timeTaskcomplete.get(key_id - 1)).get(2))), TickBias, 
	    						((ArrayList)dir_tab.get("Data")), dir_name, key_id);
	    				
/*	    				// <key_id - 1, 因為array index需要減1>	
	    				SAM_mouse_results_gen(Long.parseLong(((String)((ArrayList)id_timeTaskcomplete.get(key_id - 1)).get(2))), 
	    						TrialStartTimeCSharpTick_Next, TickBias, 
	    						((ArrayList)dir_tab.get("Data")), dir_name, key_id);*/
		    			
		    				
		    		}
		    		
				}
				    			
			}
			
			fileWrite.write_2d_arraylist_string2file("pic_results.csv", al_output, true); // no append
			fileWrite.write_2d_arraylist_string2file("id_timeTaskcomplete.csv", id_timeTaskcomplete, true); // no append
			fileWrite.write_2d_arraylist_string2file("Time_Of4Completes_plus_SAM_Valid.csv", Time_Of4Completes_plus_SAM_Valid, true); // no append
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	
	public static void pic_results_gen2_addRankings(String in_file, String out_file, int picCol, int tarCol){
		ArrayList al_pic_result; 
		al_pic_result = fileRead.get2D_arraylist_string_from_file(in_file, ",");
				
		Hashtable ht_pic = new Hashtable();
		ArrayList pic, ite1;
		
		//s1: 先把它做成一張表 -- 在於s2做處理
		String record; 		
		for(int i = 0; i < al_pic_result.size(); i++){
			ite1 = (ArrayList) al_pic_result.get(i);
			
			// pic -- tarCol e.g. valence,arousal
			record = (String)ite1.get(tarCol);			
					
			if (!ht_pic.containsKey(ite1.get(picCol))){
				pic = new ArrayList();
				pic.add(record); 
				ht_pic.put(ite1.get(picCol), pic);
			}
			else{
				((ArrayList)ht_pic.get(ite1.get(picCol))).add(record);
			}				
		}
		
		//s2, 計算每張圖片的avg valence or avg arousal 
		Hashtable ht_pic_avgs = new Hashtable();
				
		Enumeration keys = ht_pic.keys();
		while( keys.hasMoreElements() ) {
			  String key = (String) keys.nextElement();
			  ArrayList value = (ArrayList) ht_pic.get(key);
			  float rankColAvgTmp = 0;
			  int rankColAvgTmp_count = 0;
			  for(int i = 0; i < value.size(); i++){
				  if (!((String)value.get(i)).equals("N/A")){
					  rankColAvgTmp = rankColAvgTmp + Float.parseFloat((String)value.get(i));
					  rankColAvgTmp_count++;  
				  }				  
			  }
			  rankColAvgTmp = rankColAvgTmp / (float) rankColAvgTmp_count;
			  ht_pic_avgs.put(key, String.valueOf(rankColAvgTmp));			  
		}		
		//至此, 取得tarCol的avg. values of each pic.
				
		//[Modified tarCol]
		//這邊會把tarCol所有相同rating, 以及N/A全部濾掉 -- 變成avgTarCol數值; (最後輸出的時候, file裡的tarCol就是這個改過的tarCol)
		String subject = "-1";
		Hashtable sameValues = null;
		ArrayList sameValues_al_tmp;
		ArrayList al_pic_result2 = new ArrayList(); 
		for(int i = 0; i < al_pic_result.size(); i++){
			if (!subject.equals(((String)((ArrayList) al_pic_result.get(i)).get(3)))){
				if (!subject.equals("-1")){
					//enumerate all keys, 若al的size > 1者, 就對所有al內elements填上tarCol, 對picture的avg values.
					Enumeration keys_sameValues = sameValues.keys();
					while( keys_sameValues.hasMoreElements() ) {
						String key = (String) keys_sameValues.nextElement();
						ArrayList value = (ArrayList) sameValues.get(key);
						
						if ((value.size() == 1) && (!((String)((ArrayList)value.get(0)).get(tarCol)).equals("N/A")))
							al_pic_result2.add(value.get(0));
						else{
							for(int j = 0; j < value.size(); j++){
								((ArrayList)value.get(j)).set(tarCol, ((String)ht_pic_avgs.get(((String)((ArrayList)value.get(j)).get(picCol)))));
								al_pic_result2.add(value.get(j));
							}
						}
					}
					
				}
				if (subject.equals("-2"))break; //呼應下面的if (i == (al_pic_result.size()-1)){
				
				subject = ((String)((ArrayList) al_pic_result.get(i)).get(3));
				sameValues = new Hashtable();
			}			
			if (!sameValues.containsKey(((String)((ArrayList) al_pic_result.get(i)).get(tarCol)))){
				sameValues_al_tmp = new ArrayList();
				sameValues_al_tmp.add(((ArrayList) al_pic_result.get(i)));
				sameValues.put((((String)((ArrayList) al_pic_result.get(i)).get(tarCol))), sameValues_al_tmp);
			}else{
				((ArrayList)sameValues.get((((String)((ArrayList) al_pic_result.get(i)).get(tarCol)))))
				.add(((ArrayList) al_pic_result.get(i)));
			}
			
			//最後一個subject也得做到
			if (i == (al_pic_result.size()-1)){
				subject = "-2";//String.valueOf((Integer.parseInt(subject) + 1));
				i = i - 1; // backward, 為了去把al_pic_result_tmp_for_single_subject pull出來
			}
		}
		
		al_pic_result = al_pic_result2;
		
		//s3: 做ranking (並於需要的時候計算平均值 of 每張圖片)
		ArrayList al_pic_result_out = new ArrayList(); 
		ArrayList al_pic_result_tmp_for_single_subject = null;
		subject = "-1";
		for(int i = 0; i < al_pic_result.size(); i++){
			//ite1 = (ArrayList) al_pic_result.get(i);
			
			if (!subject.equals(((String)((ArrayList) al_pic_result.get(i)).get(3)))){
				if (!subject.equals("-1")){
					Collections.sort(al_pic_result_tmp_for_single_subject, 
							new MyIntComparable_CFM_EXPR2(picCol, tarCol, ht_pic_avgs));	
					
					for(int j = 0; j < al_pic_result_tmp_for_single_subject.size(); j++){
						((ArrayList)al_pic_result_tmp_for_single_subject.get(j)).add(String.valueOf(j+1)); // 加入ranking結果
						al_pic_result_out.add(al_pic_result_tmp_for_single_subject.get(j));
					}
						
				}
				
				if (subject.equals("-2"))break; //呼應下面的if (i == (al_pic_result.size()-1)){
				
				subject = ((String)((ArrayList) al_pic_result.get(i)).get(3));
				al_pic_result_tmp_for_single_subject = new ArrayList();
			}
			al_pic_result_tmp_for_single_subject.add(al_pic_result.get(i));
			
			//最後一個subject也得做到
			if (i == (al_pic_result.size()-1)){
				subject = "-2";//String.valueOf((Integer.parseInt(subject) + 1));
				i = i - 1; // backward, 為了去把al_pic_result_tmp_for_single_subject pull出來
			}				
		}
		
		fileWrite.write_2d_arraylist_string2file(out_file, al_pic_result_out, true); // no append
	}
	
	public static void KB_results_gen(boolean isNotlastRow, long StartTick, long EndTick, ArrayList file_tab, String subject_id, int key_id){
		long defEventTick = 700000L; //注意，每台電腦不太一樣...		長按濾除
		//最麻煩的是...這種連續性的記錄居然可以比人打(當多按鍵連續放開時)的還要慢orz
		
		//用來記RT_keyboard
		ArrayList RT_keyboard_event = new ArrayList();
		ArrayList tmp01;
		
		
		String KeyStrokeFilePath = "";
		String KeyUDFilePath = "";
		
		ArrayList al_KeyStroke, al_KeyUD, al_extract_KeyStroke = new ArrayList(), al_extract_KeyUD = new ArrayList();		
		
		for (int i = 0; i < file_tab.size(); i++){						
			if (((String)file_tab.get(i)).contains(subject_id)){
				if (((String)file_tab.get(i)).contains("KeyStroke"))
					KeyStrokeFilePath = ((String)file_tab.get(i));
				else if (((String)file_tab.get(i)).contains("KeyUp&Down"))
					KeyUDFilePath = ((String)file_tab.get(i));
			}
		}
		//System.out.println(file1);
		//System.out.println(file2);
		
		al_KeyStroke = fileRead.get2D_arraylist_string_from_file(KeyStrokeFilePath, ",");
		al_KeyUD = fileRead.get2D_arraylist_string_from_file(KeyUDFilePath, ",");
		
		
		//先定位到起始點
		int i = 0; 
		while(true){
			if (Long.parseLong((String) ((ArrayList)al_KeyStroke.get(i)).get(2)) > StartTick)
				break;
			i++;
		}
		int j = 0;
		while(true){
			if (Long.parseLong((String) ((ArrayList)al_KeyUD.get(j)).get(2)) > StartTick)
				break;
			j++;
		}
		
		
		/*
		 * 算最早的Keypress距離StartTick的時間間隔 (也就是RT)
		 */
		tmp01 = new ArrayList();
		tmp01.add(String.valueOf(key_id));	// col1: id label
		tmp01.add(String.valueOf((Long.parseLong((String) ((ArrayList)al_KeyStroke.get(i)).get(2)) - StartTick - 110000000L))); // col2: RT_keyboard // 11藐視"Get Ready" + "秀圖片"
		RT_keyboard_event.add(tmp01);
		fileWrite.write_2d_arraylist_string2file("RT_keyboard_event.csv", RT_keyboard_event, false); // append
		
		
		//再一路至endTick塞進輸出, 以及key_id insert.
		//同時把連按太久產生的重複keystroke & keyUD去掉剩一個 (這種都是 每次加不定量 (10000~500000) ticks
		Hashtable lastTick = new Hashtable();
		//long lastTick = 0;
		while(true){
			if((!isNotlastRow) && ((i) == (al_KeyStroke.size())))break;
			if (Long.parseLong((String) ((ArrayList)al_KeyStroke.get(i)).get(2)) > EndTick)
				break;
			
			if(lastTick.containsKey((((ArrayList)al_KeyStroke.get(i)).get(3))))
				if ((Long.parseLong(((String)((ArrayList)al_KeyStroke.get(i)).get(2))) 
					- Long.parseLong((String)lastTick.get(((ArrayList)al_KeyStroke.get(i)).get(3)))) <= defEventTick){
					//注意，每台電腦不太一樣...		長按濾除
					i++;
					continue;	
				}
			
						
			((ArrayList)al_KeyStroke.get(i)).add(0, String.valueOf(key_id));
			//((ArrayList)al_KeyStroke.get(i)).remove(2); // 不要"時間"
						
			al_extract_KeyStroke.add(al_KeyStroke.get(i));
			
			lastTick.put(((ArrayList)al_KeyStroke.get(i)).get(4), ((ArrayList)al_KeyStroke.get(i)).get(3));
			//lastTick = Long.parseLong(((String)((ArrayList)al_KeyStroke.get(i)).get(3)));
			i++;
		}
		//lastTick = 0;
		int pressedKeyNum = 0;
		while(true){
			if((!isNotlastRow) && ((j) == (al_KeyUD.size())))break;
			if (Long.parseLong((String) ((ArrayList)al_KeyUD.get(j)).get(2)) > EndTick)
				break;
				//if (pressedKeyNum == 0)break;
				//if (!((String) ((ArrayList)al_KeyUD.get(j)).get(3)).equals("1")) // 偵測是不是有還沒Release的Key
			
			
			if(lastTick.containsKey((((ArrayList)al_KeyUD.get(j)).get(3))))		
				if (((String)((ArrayList)al_KeyUD.get(j)).get(3)).equals("0")){
					if ((Long.parseLong(((String)((ArrayList)al_KeyUD.get(j)).get(2))) 
							- Long.parseLong((String)lastTick.get(((ArrayList)al_KeyUD.get(j)).get(3)))) <= defEventTick){
							//注意，每台電腦不太一樣...		長按濾除
							j++;
							continue;	
					}	
				}
				else if(((String)((ArrayList)al_KeyUD.get(j)).get(3)).equals("1"))
					if (pressedKeyNum == 0){
						j++;
						continue;	
					}
			
			
			//記錄目前被同時按下幾個key
			if (((String)((ArrayList)al_KeyUD.get(j)).get(3)).equals("0"))
				pressedKeyNum++;
			if (((String)((ArrayList)al_KeyUD.get(j)).get(3)).equals("1"))
				pressedKeyNum--;
			
			
			((ArrayList)al_KeyUD.get(j)).add(0, String.valueOf(key_id));
			//((ArrayList)al_KeyUD.get(j)).remove(2); // 不要"時間"
			al_extract_KeyUD.add(al_KeyUD.get(j));
			
			lastTick.put(((ArrayList)al_KeyUD.get(j)).get(4), ((ArrayList)al_KeyUD.get(j)).get(3));
			//lastTick = Long.parseLong(((String)((ArrayList)al_KeyUD.get(j)).get(3)));
			j++;
						
		}
		
		
		fileWrite.write_2d_arraylist_string2file("keystroke.csv", al_extract_KeyStroke, false); // append
		fileWrite.write_2d_arraylist_string2file("keyUD.csv", al_extract_KeyUD, false); // append
	
		
	}
	
	
	public static void mouse_results_gen(boolean isNotlastRow, long StartTick, long EndTick, long TickBias, ArrayList file_tab, String subject_id, int key_id){
		//是固定的，但不確定是否每台電腦不太一樣... 性質相對於keyboard穩定
		long defMoveEventTick = 80000L;
		EndTick = EndTick + TickBias;
				
		String mouseClickFilePath = "";
		String mouseUDFilePath = "";
		String mouseMoveFilePath = "";
		
		ArrayList al_mouseClick, al_mouseUD, al_mouseMove, 
		al_extract_mouseClick = new ArrayList(), al_extract_mouseUD = new ArrayList(), al_extract_mouseMove = new ArrayList();
		
		for (int i = 0; i < file_tab.size(); i++){						
			if (((String)file_tab.get(i)).contains(subject_id)){
				if (((String)file_tab.get(i)).contains("MouseClick&Wheel.txt"))
					mouseClickFilePath = ((String)file_tab.get(i));
				else if (((String)file_tab.get(i)).contains("MouseUp&Down.txt"))
					mouseUDFilePath = ((String)file_tab.get(i));
				else if (((String)file_tab.get(i)).contains("MouseMove.txt"))
					mouseMoveFilePath = ((String)file_tab.get(i));
			}
		}
		
		
		al_mouseClick = fileRead.get2D_arraylist_string_from_file(mouseClickFilePath, ",");
		al_mouseUD = fileRead.get2D_arraylist_string_from_file(mouseUDFilePath, ",");
		al_mouseMove = fileRead.get2D_arraylist_string_from_file(mouseMoveFilePath, ",");
		
		
		ArrayList RT_MouseClick = new ArrayList();
		ArrayList RT_MouseMove = new ArrayList();
		
		//先定位到起始點
		int i = 0; 
		while(true){
			if (Long.parseLong((String) ((ArrayList)al_mouseClick.get(i)).get(2)) > StartTick){
				ArrayList tmpRow = new ArrayList();
				tmpRow.add(String.valueOf((key_id)));
				tmpRow.add(String.valueOf(Long.parseLong((String) ((ArrayList)al_mouseClick.get(i)).get(2)) - StartTick));
				RT_MouseClick.add(tmpRow);
				break;
			}
				
			i++;
		}
		int j = 0;
		while(true){
			if (Long.parseLong((String) ((ArrayList)al_mouseUD.get(j)).get(2)) > StartTick)
				break;
			j++;
		}
		int k = 0;
		while(true){
			if (Long.parseLong((String) ((ArrayList)al_mouseMove.get(k)).get(2)) > StartTick){
				ArrayList tmpRow = new ArrayList();
				tmpRow.add(String.valueOf((key_id)));
				tmpRow.add(String.valueOf(Long.parseLong((String) ((ArrayList)al_mouseMove.get(k)).get(2)) - StartTick));
				RT_MouseMove.add(tmpRow);
				break;
			}
			k++;
		}
		
		fileWrite.write_2d_arraylist_string2file("RT_MouseClick.csv", RT_MouseClick, false); // append
		fileWrite.write_2d_arraylist_string2file("RT_MouseMove.csv", RT_MouseMove, false); // append
		
		
	
	
		//boolean isPress = false;
		while(true){
			if((!isNotlastRow) && (j == (al_mouseUD.size() - 1)))break;
			if (Long.parseLong((String) ((ArrayList)al_mouseUD.get(j)).get(2)) > EndTick)
				break;
			
			
			((ArrayList)al_mouseUD.get(j)).add(0, String.valueOf(key_id));
			al_extract_mouseUD.add(al_mouseUD.get(j));			
			j++;
		}
		
		while(true){
			if((!isNotlastRow) && (k == (al_mouseMove.size() - 1)))break;
			if (Long.parseLong((String) ((ArrayList)al_mouseMove.get(k)).get(2)) > EndTick)
				break;
			
			
			((ArrayList)al_mouseMove.get(k)).add(0, String.valueOf(key_id));
			al_extract_mouseMove.add(al_mouseMove.get(k));

			k++;
		}
		
			
		
		
		//fileWrite.write_2d_arraylist_string2file("mouseClick.csv", al_extract_mouseClick, false); // append
		fileWrite.write_2d_arraylist_string2file("mouseUD.csv", al_extract_mouseUD, false); // append
		fileWrite.write_2d_arraylist_string2file("mouseMove.csv", al_extract_mouseMove, false); // append
	
	
		
	}
	
	
	public static void SAM_mouse_results_gen(long StartTick, long EndTick, long TickBias, ArrayList file_tab, String subject_id, int key_id){
		//是固定的，但不確定是否每台電腦不太一樣... 性質相對於keyboard穩定
		long defMoveEventTick = 80000L; 
				
		String mouseClickFilePath = "";
		String mouseUDFilePath = "";
		String mouseMoveFilePath = "";
		
		ArrayList al_mouseClick, al_mouseUD, al_mouseMove, 
		al_extract_mouseClick = new ArrayList(), al_extract_mouseUD = new ArrayList(), al_extract_mouseMove = new ArrayList();
		
		for (int i = 0; i < file_tab.size(); i++){						
			if (((String)file_tab.get(i)).contains(subject_id)){
				if (((String)file_tab.get(i)).contains("MouseClick&Wheel.txt"))
					mouseClickFilePath = ((String)file_tab.get(i));
/*				else if (((String)file_tab.get(i)).contains("MouseUp&Down.txt"))
					mouseUDFilePath = ((String)file_tab.get(i));*/
				else if (((String)file_tab.get(i)).contains("MouseMove.txt"))
					mouseMoveFilePath = ((String)file_tab.get(i));
			}
		}
		
		
		al_mouseClick = fileRead.get2D_arraylist_string_from_file(mouseClickFilePath, ",");
		al_mouseMove = fileRead.get2D_arraylist_string_from_file(mouseMoveFilePath, ",");
		
		ArrayList RT_SAM_MouseClick = new ArrayList();
		ArrayList RT_SAM_MouseMove = new ArrayList();
		
		//先定位到起始點
		int i = 0; 
		while(true){
			if (Long.parseLong((String) ((ArrayList)al_mouseClick.get(i)).get(2)) > (StartTick+TickBias)){
				ArrayList tmpRow = new ArrayList();
				tmpRow.add(String.valueOf((key_id)));
				tmpRow.add(String.valueOf(Long.parseLong((String) ((ArrayList)al_mouseClick.get(i)).get(2)) - StartTick));
				RT_SAM_MouseClick.add(tmpRow);
				break;
			}
				
			i++;
		}
		int k = 0;
		while(true){
			if (Long.parseLong((String) ((ArrayList)al_mouseMove.get(k)).get(2)) > (StartTick+TickBias)){
				ArrayList tmpRow = new ArrayList();
				tmpRow.add(String.valueOf((key_id)));
				tmpRow.add(String.valueOf(Long.parseLong((String) ((ArrayList)al_mouseMove.get(k)).get(2)) - StartTick));
				RT_SAM_MouseMove.add(tmpRow);
				break;
			}
			k++;
		}
		
		
		fileWrite.write_2d_arraylist_string2file("RT_SAM_MouseClick.csv", RT_SAM_MouseClick, false); // append
		fileWrite.write_2d_arraylist_string2file("RT_SAM_MouseMove.csv", RT_SAM_MouseMove, false); // append
	}
	
	public static void mouseClick_features_gen(String mouseUDPath){
		ArrayList al_extract_mouseUD = fileRead.get2D_arraylist_string_from_file(mouseUDPath, ",");
		ArrayList al_cleaned_mouseUD_D = new ArrayList(), al_cleaned_mouseUD_U = new ArrayList();
		ArrayList al_feature_extract_mouseUD = new ArrayList();
		
		//先做個修正, 依據
		/*前處理原則：有的時候發現還是會有0但沒有1 (意思是有press但沒有release); 
		>> 處理方式：以第一個0為準, 中間的0全部不理; 若有連1的情形, 則取第一個1.; 若不成對(有0無1, 或有1無0, 丟掉.)
		*/
		//把所有異常資料給濾除, 呈一個乾淨的arraylist of mouseUD
		String lastState = "-1";;
		ArrayList lastAL = null;
		String LastID = "-1", CurID = "-1";
		for(int i = 0; i < al_extract_mouseUD.size(); i++){
			CurID = ((String)((ArrayList)al_extract_mouseUD.get(i)).get(0)); 			
			if (!CurID.equals(LastID)){
				LastID = CurID;
				
				//結束的時候若停在0, 表示有0沒有等到1, 要把最後一個down entry丟掉
				if (lastState.equals("0")){
					al_cleaned_mouseUD_D.remove(al_cleaned_mouseUD_D.size()-1);
				}
				
				lastState = "-1";				
			}
			
			if (lastState.equals("-1")){
				if (((String)((ArrayList)al_extract_mouseUD.get(i)).get(4)).equals("0")){
					lastState = "0";
					al_cleaned_mouseUD_D.add((ArrayList)al_extract_mouseUD.get(i));
				}				
			}
			else if(lastState.equals("0")){
				if (((String)((ArrayList)al_extract_mouseUD.get(i)).get(4)).equals("1")){
					lastState = "1";
					al_cleaned_mouseUD_U.add((ArrayList)al_extract_mouseUD.get(i));
				}
			}
			else if(lastState.equals("1")){
				if (((String)((ArrayList)al_extract_mouseUD.get(i)).get(4)).equals("0")){
					lastState = "0";
					al_cleaned_mouseUD_D.add((ArrayList)al_extract_mouseUD.get(i));
				}
			}					
		}
		//因為LastID技巧--所以要補做：
		//結束的時候若停在0, 表示有0沒有等到1, 要把最後一個down entry丟掉
		if (lastState.equals("0")){
			al_cleaned_mouseUD_D.remove(al_cleaned_mouseUD_D.size()-1);
		}

		
		/*//just for testing... intermediate results
		fileWrite.write_2d_arraylist_string2file("al_cleaned_mouseUD_D.csv", al_cleaned_mouseUD_D, true); // new file
		fileWrite.write_2d_arraylist_string2file("al_cleaned_mouseUD_U.csv", al_cleaned_mouseUD_U, true); // new file
*/		
		ArrayList durationAL = new ArrayList();
		for(int i = 0; i < al_cleaned_mouseUD_D.size(); i++){
			ArrayList durationAL_row = new ArrayList();
			durationAL_row.add(((ArrayList)al_cleaned_mouseUD_D.get(i)).get(0));
			durationAL_row.add(String.valueOf(Long.parseLong((String)((ArrayList)al_cleaned_mouseUD_U.get(i)).get(3)) 
					- Long.parseLong((String)((ArrayList)al_cleaned_mouseUD_D.get(i)).get(3))));
			durationAL.add(durationAL_row);			
		}
		/*//just for testing... intermediate results
		fileWrite.write_2d_arraylist_string2file("durationAL.csv", durationAL, true); // new file
*/		
		LastID = "-1"; CurID = "-1";
		ArrayList durationAL_row_checkingList = null;
		for(int i = 0; i < durationAL.size(); i++){
		/*
			- is_4click?					// 不一定只有4 click -- 但理論上來說應該是
			- four_click_durations (共4筆)	// 沒有latency適用 -- 因為沒有double click task
			- N								// click數; 隱含有"error clicks"以及"焦躁多click"的概念
			- N_avg duration				// for all click events, 因為沒有cross click task
			- N_std duration				// for all click events, 因為沒有cross click task
			- N_CV duration					// for all click events, 因為沒有cross click task
			
			--> 為了mouseMove需要, 特別製作4個click的press時間點的tick
			- [1] press tick
			- [2] press tick
			- [3] press tick
			- [4] press tick
		 */			
			CurID = ((String)((ArrayList)durationAL.get(i)).get(0)); 			
			if (!CurID.equals(LastID)){
				
			
				if(!LastID.equals("-1")){
					ArrayList mouseUD_features_row = new ArrayList();
					//當不是-1時作一些處理....>>>
					//針對durationAL_row_checkingList做計算...
					mouseUD_features_row.add(LastID);
					if(durationAL_row_checkingList.size() == 4){ // is_4click?					
						mouseUD_features_row.add("1");
						
						// four_click_durations (共4筆) -- 取前四個
						mouseUD_features_row.add(((String)((ArrayList)durationAL_row_checkingList.get(0)).get(1)));
						mouseUD_features_row.add(((String)((ArrayList)durationAL_row_checkingList.get(1)).get(1)));
						mouseUD_features_row.add(((String)((ArrayList)durationAL_row_checkingList.get(2)).get(1)));
						mouseUD_features_row.add(((String)((ArrayList)durationAL_row_checkingList.get(3)).get(1)));
					}
					else{
						mouseUD_features_row.add("0");
						// four_click_durations (共4筆) 
						mouseUD_features_row.add("N/A");
						mouseUD_features_row.add("N/A");
						mouseUD_features_row.add("N/A");
						mouseUD_features_row.add("N/A");
					}
					
					// N
					mouseUD_features_row.add(String.valueOf(durationAL_row_checkingList.size()));					
					// N_avg duration
					mouseUD_features_row.add(String.valueOf(util.math.general.avgValue(durationAL_row_checkingList, 1)));
					// N_std duration
					mouseUD_features_row.add(String.valueOf(util.math.general.stdValue(durationAL_row_checkingList, 1)));
					// N_CV duration
					mouseUD_features_row.add(String.valueOf(util.math.general.CV_Value(durationAL_row_checkingList, 1)));
					
					//--->do it for mouseMove feature extraction
					if(durationAL_row_checkingList.size() == 4){ // is_4click?	
						// Click1_press tick
						mouseUD_features_row.add((String)((ArrayList)al_cleaned_mouseUD_D.get(i-4)).get(3));
						// Click2_press tick
						mouseUD_features_row.add((String)((ArrayList)al_cleaned_mouseUD_D.get(i-3)).get(3));
						// Click3_press tick
						mouseUD_features_row.add((String)((ArrayList)al_cleaned_mouseUD_D.get(i-2)).get(3));
						// Click4_press tick
						mouseUD_features_row.add((String)((ArrayList)al_cleaned_mouseUD_D.get(i-1)).get(3));
					}
					else{
						mouseUD_features_row.add("N/A");
						mouseUD_features_row.add("N/A");
						mouseUD_features_row.add("N/A");
						mouseUD_features_row.add("N/A");
					}
						
					
					al_feature_extract_mouseUD.add(mouseUD_features_row);
				}
				durationAL_row_checkingList = new ArrayList();
				LastID = CurID;
			}
			
			durationAL_row_checkingList.add(durationAL.get(i));
		}		
		//注意要特別處理LastID = lastid的部分----
		if(!LastID.equals("-1")){
			ArrayList mouseUD_features_row = new ArrayList();
			//當不是-1時作一些處理....>>>
			//針對durationAL_row_checkingList做計算...
			mouseUD_features_row.add(LastID);
			if(durationAL_row_checkingList.size() == 4){ // is_4click?					
				mouseUD_features_row.add("1");
				
				// four_click_durations (共4筆) -- 取前四個
				mouseUD_features_row.add(((String)((ArrayList)durationAL_row_checkingList.get(0)).get(1)));
				mouseUD_features_row.add(((String)((ArrayList)durationAL_row_checkingList.get(1)).get(1)));
				mouseUD_features_row.add(((String)((ArrayList)durationAL_row_checkingList.get(2)).get(1)));
				mouseUD_features_row.add(((String)((ArrayList)durationAL_row_checkingList.get(3)).get(1)));
			}
			else{
				mouseUD_features_row.add("0");
				// four_click_durations (共4筆) -- 取前四個
				mouseUD_features_row.add("N/A");
				mouseUD_features_row.add("N/A");
				mouseUD_features_row.add("N/A");
				mouseUD_features_row.add("N/A");
			}
			
			// N
			mouseUD_features_row.add(String.valueOf(durationAL_row_checkingList.size()));					
			// N_avg duration
			mouseUD_features_row.add(String.valueOf(util.math.general.avgValue(durationAL_row_checkingList, 1)));
			// N_std duration
			mouseUD_features_row.add(String.valueOf(util.math.general.stdValue(durationAL_row_checkingList, 1)));
			// N_CV duration
			mouseUD_features_row.add(String.valueOf(util.math.general.CV_Value(durationAL_row_checkingList, 1)));
			
			//--->do it for mouseMove feature extraction
			if(durationAL_row_checkingList.size() == 4){ // is_4click?	
				// Click1_press tick
				mouseUD_features_row.add((String)((ArrayList)al_cleaned_mouseUD_D.get(durationAL.size()-4)).get(3));
				// Click2_press tick
				mouseUD_features_row.add((String)((ArrayList)al_cleaned_mouseUD_D.get(durationAL.size()-3)).get(3));
				// Click3_press tick
				mouseUD_features_row.add((String)((ArrayList)al_cleaned_mouseUD_D.get(durationAL.size()-2)).get(3));
				// Click4_press tick
				mouseUD_features_row.add((String)((ArrayList)al_cleaned_mouseUD_D.get(durationAL.size()-1)).get(3));
			}
			else{
				mouseUD_features_row.add("N/A");
				mouseUD_features_row.add("N/A");
				mouseUD_features_row.add("N/A");
				mouseUD_features_row.add("N/A");
			}
				
			
			al_feature_extract_mouseUD.add(mouseUD_features_row);
		}
		
		fileWrite.write_2d_arraylist_string2file("mouseUD_features.csv", al_feature_extract_mouseUD, true); // new file
	}
	
	
	public static void mouseMove_features_gen(String mouseMovePath, String mouseClickFeaturesPath){
		ArrayList al_extract_mouseMove = fileRead.get2D_arraylist_string_from_file(mouseMovePath, ",");
		ArrayList al_extract_mouseClick_features = fileRead.get2D_arraylist_string_from_file(mouseClickFeaturesPath, ",");
		ArrayList al_feature_extract_mouseMove = new ArrayList();
				

		//對mouseMove events做標籤, 確認是第幾個click時的event (tag在col2)
		ArrayList tmpMouseMoveAL = new ArrayList();
		int mouseClick_i = 0, mouseClick_j = 10; //j: 10~13
		
		//確認有哪個event_id不在al_extract_mouseClick_features上
		ArrayList MouseClickEvent_id_al = new ArrayList();
		for (int i = 0; i < al_extract_mouseClick_features.size(); i++){
			MouseClickEvent_id_al.add(((String)((ArrayList)al_extract_mouseClick_features.get(i)).get(0)));			
		}
		
		for (int i = 0; i < al_extract_mouseMove.size(); i++){
	
			// 確認mouseMove event_id是不是在MouseClickEvent_id_al上
			if (!MouseClickEvent_id_al.contains(((String)((ArrayList)al_extract_mouseMove.get(i)).get(0))))
				continue;
			//若mouseClick已經舉到沒有event_id了, 就break. --- 終止條件(al_extract_mouseClick_features會先瓶頸)
			if (mouseClick_i == al_extract_mouseClick_features.size())break;
					
			
			//當id不相等, 一直跳i, 跳到id相等
			if(!((String)((ArrayList)al_extract_mouseClick_features.get(mouseClick_i)).get(0))
					.equals(((String)((ArrayList)al_extract_mouseMove.get(i)).get(0)))){
				//確認是不是mouseMove event_id已超越mouseClick event_id
				if (Integer.parseInt(((String)((ArrayList)al_extract_mouseClick_features.get(mouseClick_i)).get(0)))
				 > Integer.parseInt(((String)((ArrayList)al_extract_mouseMove.get(i)).get(0))))					
					continue;
				else{
					mouseClick_i++;
					mouseClick_j = 10;
					i--; continue; //回上一步					
				}
			}
			
			//若is_4click = false, 跳過, 換下個id
			if(((String)((ArrayList)al_extract_mouseClick_features.get(mouseClick_i)).get(1)).equals("0")){
				mouseClick_i++;
				mouseClick_j = 10;
				i--; continue; //回上一步
			}
			
			if(Long.parseLong(((String)((ArrayList)al_extract_mouseMove.get(i)).get(3))) 
					> Long.parseLong(((String)((ArrayList)al_extract_mouseClick_features.get(mouseClick_i)).get(mouseClick_j)))){
				
				if (mouseClick_j < 13)
					mouseClick_j++;
				else{
					mouseClick_i++;
					mouseClick_j = 10;
				}
				
				i--; continue; //回上一步				
			}
			
			//常態:			
			((ArrayList)al_extract_mouseMove.get(i)).add(1, String.valueOf(mouseClick_j - 9));
			tmpMouseMoveAL.add(al_extract_mouseMove.get(i));

				
		}
		al_extract_mouseMove = tmpMouseMoveAL;
		
		ArrayList mouseMove_slice_event_id = new ArrayList();
		ArrayList mouseMove_slice_Click1 = new ArrayList(), 
				mouseMove_slice_Click2 = new ArrayList(), 
				mouseMove_slice_Click3 = new ArrayList(), mouseMove_slice_Click4 = new ArrayList();
		String LastID = "-1", CurID = "-1";		
		for(int i = 0; i < al_extract_mouseMove.size(); i++){
			CurID = (String)((ArrayList)al_extract_mouseMove.get(i)).get(0);
			
			if(!CurID.equals(LastID)){
				
				//別忘了LastID要做一樣的處理, 放在最後				
				if((!LastID.equals("-1"))){
					//----這邊要放features計算-----
					al_feature_extract_mouseMove.add(mouseMove_features_gen_singleRow(mouseMove_slice_event_id, mouseMove_slice_Click1, mouseMove_slice_Click2, mouseMove_slice_Click3, mouseMove_slice_Click4));										
				}				
				
				LastID = CurID;
				mouseMove_slice_event_id = new ArrayList();
				mouseMove_slice_Click1 = new ArrayList();
				mouseMove_slice_Click2 = new ArrayList(); 
				mouseMove_slice_Click3 = new ArrayList();
				mouseMove_slice_Click4 = new ArrayList();
			}
			
			mouseMove_slice_event_id.add(al_extract_mouseMove.get(i));
			switch (Integer.parseInt((String)((ArrayList)al_extract_mouseMove.get(i)).get(1))){
				case 1:
					mouseMove_slice_Click1.add(al_extract_mouseMove.get(i));
					break;
				case 2:
					mouseMove_slice_Click2.add(al_extract_mouseMove.get(i));
					break;
				case 3:
					mouseMove_slice_Click3.add(al_extract_mouseMove.get(i));
					break;
				case 4:
					mouseMove_slice_Click4.add(al_extract_mouseMove.get(i));
					break;
			}
			
		}
		if(!LastID.equals("-1")){
			al_feature_extract_mouseMove.add(mouseMove_features_gen_singleRow(mouseMove_slice_event_id, mouseMove_slice_Click1, mouseMove_slice_Click2, mouseMove_slice_Click3, mouseMove_slice_Click4));
		}
		
		
		/*//testing
		fileWrite.write_2d_arraylist_string2file("al_extract_mouseMove_tmp.csv", al_extract_mouseMove, true); // new file
*/		
		
		fileWrite.write_2d_arraylist_string2file("mouseMove_features.csv", al_feature_extract_mouseMove, true); // new file
	}
	
	public static ArrayList mouseMove_features_gen_singleRow(ArrayList mouseMove_slice_event_id, ArrayList mouseMove_slice_Click1, ArrayList mouseMove_slice_Click2, ArrayList mouseMove_slice_Click3, ArrayList mouseMove_slice_Click4){
		int center_x = 600, center_y = 400, xcol = 5, ycol = 6, tcol = 4; // parameters
		
		ArrayList al_feature_extract_mouseMove_row = new ArrayList();
		
		//event_id
		int key_id = Integer.parseInt((String)((ArrayList)mouseMove_slice_event_id.get(0)).get(0));
		al_feature_extract_mouseMove_row.add(String.valueOf(key_id));
		
		//feature: 總移動路徑長度
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_length_of_path(mouseMove_slice_event_id, xcol, ycol)));
		//feature: 總移動位移長度
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_displacement(mouseMove_slice_event_id, xcol, ycol)));	
		//feature: 總移動時間
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_deltaT(mouseMove_slice_event_id, tcol)));
		//feature: 總移動速率
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_speed(mouseMove_slice_event_id, xcol, ycol, tcol)));
		//feature: 總移動速度
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_velocity(mouseMove_slice_event_id, xcol, ycol, tcol)));

		
		//feature: 按燈順序_Click1
		String quadrant = "";
		switch (util.physics.graphMining.get_Quadrant((ArrayList)mouseMove_slice_Click1.get(mouseMove_slice_Click1.size() - 1), center_x, center_y, xcol, ycol)){
			case 1: 
				quadrant = "[3]";
				al_feature_extract_mouseMove_row.add(quadrant);				
				break;
			case 2: 
				quadrant = "[1]";
				al_feature_extract_mouseMove_row.add(quadrant);
				break;
			case 3: 
				quadrant = "[2]";
				al_feature_extract_mouseMove_row.add(quadrant);
				break;
			case 4: 
				quadrant = "[4]";
				al_feature_extract_mouseMove_row.add(quadrant);
				break;
		}
		//feature: click1對target的r
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_r_of_clickTarget((ArrayList)mouseMove_slice_Click1.get(mouseMove_slice_Click1.size() - 1), quadrant, xcol, ycol)));
		//feature: click1對target的theta
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_theta_of_clickTarget((ArrayList)mouseMove_slice_Click1.get(mouseMove_slice_Click1.size() - 1), quadrant, xcol, ycol)));
		
		
		//feature: 按燈順序_Click2
		switch (util.physics.graphMining.get_Quadrant((ArrayList)mouseMove_slice_Click2.get(mouseMove_slice_Click2.size() - 1), center_x, center_y, xcol, ycol)){
			case 1: 
				quadrant = "[3]";
				al_feature_extract_mouseMove_row.add(quadrant);				
				break;
			case 2: 
				quadrant = "[1]";
				al_feature_extract_mouseMove_row.add(quadrant);
				break;
			case 3: 
				quadrant = "[2]";
				al_feature_extract_mouseMove_row.add(quadrant);
				break;
			case 4: 
				quadrant = "[4]";
				al_feature_extract_mouseMove_row.add(quadrant);
				break;
		}
		//feature: click2對target的r
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_r_of_clickTarget((ArrayList)mouseMove_slice_Click2.get(mouseMove_slice_Click2.size() - 1), quadrant, xcol, ycol)));
		//feature: click2對target的theta
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_theta_of_clickTarget((ArrayList)mouseMove_slice_Click2.get(mouseMove_slice_Click2.size() - 1), quadrant, xcol, ycol)));
		
		
		//feature: 按燈順序_Click3
		switch (util.physics.graphMining.get_Quadrant((ArrayList)mouseMove_slice_Click3.get(mouseMove_slice_Click3.size() - 1), center_x, center_y, xcol, ycol)){
			case 1: 
				quadrant = "[3]";
				al_feature_extract_mouseMove_row.add(quadrant);				
				break;
			case 2: 
				quadrant = "[1]";
				al_feature_extract_mouseMove_row.add(quadrant);
				break;
			case 3: 
				quadrant = "[2]";
				al_feature_extract_mouseMove_row.add(quadrant);
				break;
			case 4: 
				quadrant = "[4]";
				al_feature_extract_mouseMove_row.add(quadrant);
				break;
		}
		//feature: click3對target的r
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_r_of_clickTarget((ArrayList)mouseMove_slice_Click3.get(mouseMove_slice_Click3.size() - 1), quadrant, xcol, ycol)));
		//feature: click3對target的theta
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_theta_of_clickTarget((ArrayList)mouseMove_slice_Click3.get(mouseMove_slice_Click3.size() - 1), quadrant, xcol, ycol)));
				
		//feature: 按燈順序_Click4
		switch (util.physics.graphMining.get_Quadrant((ArrayList)mouseMove_slice_Click4.get(mouseMove_slice_Click4.size() - 1), center_x, center_y, xcol, ycol)){
			case 1: 
				quadrant = "[3]";
				al_feature_extract_mouseMove_row.add(quadrant);				
				break;
			case 2: 
				quadrant = "[1]";
				al_feature_extract_mouseMove_row.add(quadrant);
				break;
			case 3: 
				quadrant = "[2]";
				al_feature_extract_mouseMove_row.add(quadrant);
				break;
			case 4: 
				quadrant = "[4]";
				al_feature_extract_mouseMove_row.add(quadrant);
				break;
		}
		//feature: click4對target的r
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_r_of_clickTarget((ArrayList)mouseMove_slice_Click4.get(mouseMove_slice_Click4.size() - 1), quadrant, xcol, ycol)));
		//feature: click4對target的theta
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_theta_of_clickTarget((ArrayList)mouseMove_slice_Click4.get(mouseMove_slice_Click4.size() - 1), quadrant, xcol, ycol)));		

		
		//feature: 移動路徑長度_Click1
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_length_of_path(mouseMove_slice_Click1, xcol, ycol)));
		//feature: 移動路徑長度_Click2
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_length_of_path(mouseMove_slice_Click2, xcol, ycol)));
		//feature: 移動路徑長度_Click3
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_length_of_path(mouseMove_slice_Click3, xcol, ycol)));
		//feature: 移動路徑長度_Click4
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_length_of_path(mouseMove_slice_Click4, xcol, ycol)));

		//feature: 移動位移長度_Click1
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_displacement(mouseMove_slice_Click1, xcol, ycol)));
		//feature: 移動位移長度_Click2
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_displacement(mouseMove_slice_Click2, xcol, ycol)));
		//feature: 移動位移長度_Click3
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_displacement(mouseMove_slice_Click3, xcol, ycol)));
		//feature: 移動位移長度_Click4
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_displacement(mouseMove_slice_Click4, xcol, ycol)));		
	
		//feature: 移動所花時間_Click1
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_deltaT(mouseMove_slice_Click1, tcol)));
		//feature: 移動所花時間_Click2
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_deltaT(mouseMove_slice_Click2, tcol)));
		//feature: 移動所花時間_Click3
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_deltaT(mouseMove_slice_Click3, tcol)));
		//feature: 移動所花時間_Click4
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_deltaT(mouseMove_slice_Click4, tcol)));
		
		//feature: 移動速率_Click1
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_speed(mouseMove_slice_Click1, xcol, ycol, tcol)));
		//feature: 移動速率_Click2
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_speed(mouseMove_slice_Click2, xcol, ycol, tcol)));
		//feature: 移動速率_Click3
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_speed(mouseMove_slice_Click3, xcol, ycol, tcol)));
		//feature: 移動速率_Click4
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_speed(mouseMove_slice_Click4, xcol, ycol, tcol)));

		//feature: 移動速度_Click1
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_velocity(mouseMove_slice_Click1, xcol, ycol, tcol)));
		//feature: 移動速度_Click2
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_velocity(mouseMove_slice_Click2, xcol, ycol, tcol)));
		//feature: 移動速度_Click3
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_velocity(mouseMove_slice_Click3, xcol, ycol, tcol)));
		//feature: 移動速度_Click4
		al_feature_extract_mouseMove_row.add(String.valueOf(util.physics.graphMining.get_2d_velocity(mouseMove_slice_Click4, xcol, ycol, tcol)));

		
		
		return al_feature_extract_mouseMove_row;
	}
	
	
	public static void KB_results_comb(String keyPath, String keyUDPath){
		ArrayList al_extract_KeyStroke, al_extract_KeyUD;
		al_extract_KeyStroke = fileRead.get2D_arraylist_string_from_file(keyPath, ",");
		al_extract_KeyUD = fileRead.get2D_arraylist_string_from_file(keyUDPath, ",");
		
		
		int j = 0;
		String LastID = "";
		Queue<String> LastKey = null;
				
		for(int i = 0; i < al_extract_KeyUD.size(); i++){
			
			if (!((String)((ArrayList)al_extract_KeyUD.get(i)).get(0)).equals(LastID)){
				LastID = (String)((ArrayList)al_extract_KeyUD.get(i)).get(0);				
				LastKey = new LinkedList<String>();
			}
			
			if (((String)((ArrayList)al_extract_KeyUD.get(i)).get(4)).equals("0")){
				while(true){
					
					if (((String)((ArrayList)al_extract_KeyStroke.get(j)).get(3)).trim().equals
					(((String)((ArrayList)al_extract_KeyUD.get(i)).get(3)).trim())){
						
						LastKey.offer((((String)((ArrayList)al_extract_KeyStroke.get(j)).get(4))));
						
						((ArrayList)al_extract_KeyUD.get(i)).add(4, (((String)((ArrayList)al_extract_KeyStroke.get(j)).get(4))));
						
						j++;
						break;
					}					
					j++;
				}	
			}
			else if (((String)((ArrayList)al_extract_KeyUD.get(i)).get(4)).equals("1")){
				((ArrayList)al_extract_KeyUD.get(i)).add(4, LastKey.poll());				
			}
			
		}
		
		
		fileWrite.write_2d_arraylist_string2file("keystrokeUD.csv", al_extract_KeyUD, true); // new file
	}
	
	public static void J16_newOutput(String pic_results_path, String RT_keyboard_event_path, String keystrokeUD_isSuccessSeq_path){
		
		ArrayList outputAL = new ArrayList();
		
		ArrayList pic_results = fileRead.get2D_arraylist_string_from_file(pic_results_path);
		ArrayList RT_keyboard_event = fileRead.get2D_arraylist_string_from_file(RT_keyboard_event_path);
		ArrayList keystrokeUD_isSuccessSeq = fileRead.get2D_arraylist_string_from_file(keystrokeUD_isSuccessSeq_path);
		
//		//把key都改成只有數字
//		for(int i = 0; i < keystrokeUD_isSuccessSeq.size(); i++){
//			ArrayList keystrokeUD_isSuccessSeq_row = (ArrayList)keystrokeUD_isSuccessSeq.get(i);
//			keystrokeUD_isSuccessSeq_row.set(5, ((String)keystrokeUD_isSuccessSeq_row.get(5)).substring(((String)keystrokeUD_isSuccessSeq_row.get(5)).length()-1));
//		}
		
		//format: "subject,gender,picture,valence,arousal,RT,correct(key-level),feature,value"
		for(int i = 0; i < pic_results.size(); i++){
			ArrayList pic_results_row = (ArrayList)pic_results.get(i);
			String current_key_id = (String)pic_results_row.get(0);
		
			ArrayList outputAL_row_front = new ArrayList();			
			String subject = (String)pic_results_row.get(3);
			String gender = (String)pic_results_row.get(5);
			String picture = (String)pic_results_row.get(2);
			String valence = (String)pic_results_row.get(6);
			String arousal = (String)pic_results_row.get(7);
			
			outputAL_row_front.add(subject);
			outputAL_row_front.add(gender);
			outputAL_row_front.add(picture);
			outputAL_row_front.add(valence);
			outputAL_row_front.add(arousal);
			
			String RT = "N/A";
			for(int j = 0; j < RT_keyboard_event.size(); j++){
				if(((String)((ArrayList)RT_keyboard_event.get(j)).get(0)).equals(current_key_id)){
					RT = (String)((ArrayList)RT_keyboard_event.get(j)).get(1);
					break;
				}					
			}
			outputAL_row_front.add(RT);
						
			String isCorrect = "N/A";
			int keystrokeUD_isSuccessSeq_ite = 0;
			while(keystrokeUD_isSuccessSeq_ite < keystrokeUD_isSuccessSeq.size()){
				if(((String)((ArrayList)keystrokeUD_isSuccessSeq.get(keystrokeUD_isSuccessSeq_ite)).get(0)).equals(current_key_id)){
					isCorrect = (String)((ArrayList)keystrokeUD_isSuccessSeq.get(keystrokeUD_isSuccessSeq_ite)).get(1);
					break;
				}					
				keystrokeUD_isSuccessSeq_ite++;
			}
			outputAL_row_front.add(isCorrect);
			
			if(isCorrect.equals("N/A")){
				System.out.println("keystrokeUD_isSuccessSeq_ite: Can't find the key_id: "+current_key_id);
				continue;
			}			
			
			
			if(isCorrect.equals("0")){				
				while(true){
					ArrayList outputAL_row = (ArrayList)outputAL_row_front.clone();	
					outputAL_row.add("N/A"); // blank
					outputAL_row.add("N/A"); // blank
					outputAL.add(outputAL_row);
					
					keystrokeUD_isSuccessSeq_ite++;
					if((keystrokeUD_isSuccessSeq_ite == keystrokeUD_isSuccessSeq.size())
							||(!((String)((ArrayList)keystrokeUD_isSuccessSeq.get(keystrokeUD_isSuccessSeq_ite)).get(0)).equals(current_key_id)))
						break;					
				}					
			}
			else{
				//"24357980";
				ArrayList key_id_seq = new ArrayList();
				while(true){
					key_id_seq.add(((ArrayList)keystrokeUD_isSuccessSeq.get(keystrokeUD_isSuccessSeq_ite)));
					keystrokeUD_isSuccessSeq_ite++;
					if((keystrokeUD_isSuccessSeq_ite == keystrokeUD_isSuccessSeq.size())
							||(!((String)((ArrayList)keystrokeUD_isSuccessSeq.get(keystrokeUD_isSuccessSeq_ite)).get(0)).equals(current_key_id)))
						break;					
				}
				
				//Single Duration
				ArrayList outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("2D");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad2", "0", "NumPad2", "1"));
				outputAL.add(outputAL_row);
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("4D");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad4", "0", "NumPad4", "1"));
				outputAL.add(outputAL_row);				
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("3D");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad3", "0", "NumPad3", "1"));
				outputAL.add(outputAL_row);
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("5D");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad5", "0", "NumPad5", "1"));
				outputAL.add(outputAL_row);
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("7D");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad7", "0", "NumPad7", "1"));
				outputAL.add(outputAL_row);
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("9D");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad9", "0", "NumPad9", "1"));
				outputAL.add(outputAL_row);
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("8D");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad8", "0", "NumPad8", "1"));
				outputAL.add(outputAL_row);
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("0D");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad0", "0", "NumPad0", "1"));
				outputAL.add(outputAL_row);
				
				//Single Latency
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("2_4L");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad2", "1", "NumPad4", "0"));
				outputAL.add(outputAL_row);				
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("4_3L");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad4", "1", "NumPad3", "0"));
				outputAL.add(outputAL_row);				
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("3_5L");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad3", "1", "NumPad5", "0"));
				outputAL.add(outputAL_row);				
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("5_7L");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad5", "1", "NumPad7", "0"));
				outputAL.add(outputAL_row);				
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("7_9L");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad7", "1", "NumPad9", "0"));
				outputAL.add(outputAL_row);				
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("9_8L");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad9", "1", "NumPad8", "0"));
				outputAL.add(outputAL_row);				
				outputAL_row = (ArrayList)outputAL_row_front.clone();
				outputAL_row.add("8_0L");
				outputAL_row.add(getFeaturesFrom_key_id_seq(key_id_seq, "NumPad8", "1", "NumPad0", "0"));
				outputAL.add(outputAL_row);
								
			}
			
		}
				
		fileWrite.write_2d_arraylist_string2file("100B_CFM_EXPR_preprocDone.csv", outputAL, true); // new file
		
	}
	
	
	public static String getFeaturesFrom_key_id_seq(ArrayList keystrokeUD_isSuccessSeq_one_seq, String st_key, String st_ud, String ed_key, String ed_ud){
		Long st_tick = (long) -1;
		Long ed_tick = (long) -1;
		
		for(int i = 0; i < keystrokeUD_isSuccessSeq_one_seq.size(); i++){
			ArrayList keystrokeUD_isSuccessSeq_one_seq_row = (ArrayList)keystrokeUD_isSuccessSeq_one_seq.get(i);
			if(((String)keystrokeUD_isSuccessSeq_one_seq_row.get(5)).equals(st_key)
					&&((String)keystrokeUD_isSuccessSeq_one_seq_row.get(6)).equals(st_ud))
				st_tick = Long.parseLong(((String)keystrokeUD_isSuccessSeq_one_seq_row.get(4)));
			if(((String)keystrokeUD_isSuccessSeq_one_seq_row.get(5)).equals(ed_key)
					&&((String)keystrokeUD_isSuccessSeq_one_seq_row.get(6)).equals(ed_ud))
				ed_tick = Long.parseLong(((String)keystrokeUD_isSuccessSeq_one_seq_row.get(4)));
		}
		
		if((st_tick == -1)||(ed_tick == -1))System.out.println("Error in getFeaturesFrom_key_id_seq, please check the code");
		
		return String.valueOf(ed_tick - st_tick);
		
	}
	
	public static void KB_features_tagSuccess(String keyAndUD_Path){
		ArrayList al_extract_KeyUD, queue = new ArrayList();;
		al_extract_KeyUD = fileRead.get2D_arraylist_string_from_file(keyAndUD_Path, ",");
		
		int LastID = -1, CurrentID;
		int ite_key= 1;
		String CurrentKey = "";
		ArrayList KeySeq = new ArrayList();
		KeySeq.add("N/A");
		KeySeq.add("NumPad2");
		KeySeq.add("NumPad4");
		KeySeq.add("NumPad3");
		KeySeq.add("NumPad5");
		KeySeq.add("NumPad7");
		KeySeq.add("NumPad9");
		KeySeq.add("NumPad8");
		KeySeq.add("NumPad0");
		
		boolean isSuccessSeq = true;
		 
		for(int i = 0; i < al_extract_KeyUD.size(); i++){
			CurrentID = Integer.parseInt(((String)((ArrayList)al_extract_KeyUD.get(i)).get(0)));
			if (CurrentID != LastID){
				
				if (ite_key != KeySeq.size()) // 確認是不是沒把seq打完
					isSuccessSeq = false;
				
				if ((queue.size() != 0)) // 不然會沒有i-1這個element
					if (!((String)((ArrayList)al_extract_KeyUD.get(i-1)).get(5)).equals("1")) // 確認是不是有把Key都Release
						isSuccessSeq = false;
				
				if (isSuccessSeq)
					for (int j = 0; j < queue.size(); j++)
						((ArrayList)queue.get(j)).add(1, "1"); //isSuccessSeq
				else
					for (int j = 0; j < queue.size(); j++)
						((ArrayList)queue.get(j)).add(1, "0"); //isSuccessSeq				
				
				ite_key = 1; // 1~8 = 2, 4, 3, 5, 7, 9, 8, 0
				isSuccessSeq = true;
				queue = new ArrayList();
				LastID = CurrentID;
			}
			
			queue.add(al_extract_KeyUD.get(i));
			
			if (((String)((ArrayList)al_extract_KeyUD.get(i)).get(5)).equals("0"))
				if ((ite_key < KeySeq.size()) && (((String)((ArrayList)al_extract_KeyUD.get(i)).get(4)).equals((String)KeySeq.get(ite_key))))
					ite_key++;
				else
					isSuccessSeq = false;
				
		}
		//別忘了最後一個event也要補處理 (因為是使用if (CurrentID != LastID))
		if (isSuccessSeq)
			for (int j = 0; j < queue.size(); j++)
				((ArrayList)queue.get(j)).add(1, "1"); //isSuccessSeq
		else
			for (int j = 0; j < queue.size(); j++)
				((ArrayList)queue.get(j)).add(1, "0"); //isSuccessSeq		
		
		
		fileWrite.write_2d_arraylist_string2file("keystrokeUD_isSuccessSeq.csv", al_extract_KeyUD, true); // new file
		
	}
		
	public static void KB_features_DL_brute(String keyAndUD2_Path){
		ArrayList al_extract_KeyUD, al_extract_KeyUD_features = new ArrayList();
		al_extract_KeyUD = fileRead.get2D_arraylist_string_from_file(keyAndUD2_Path, ",");
		
		// 1~8 = 2, 4, 3, 5, 7, 9, 8, 0
		//Duration
		KB_features_DL_brute_SelKeyUD("NumPad2", "0", "NumPad2", "1", al_extract_KeyUD, al_extract_KeyUD_features, true);		
		KB_features_DL_brute_SelKeyUD("NumPad2", "0", "NumPad4", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "0", "NumPad3", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "0", "NumPad5", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "0", "NumPad7", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "0", "NumPad9", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "0", "NumPad8", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "0", "NumPad0", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);

		KB_features_DL_brute_SelKeyUD("NumPad4", "0", "NumPad4", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad4", "0", "NumPad3", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad4", "0", "NumPad5", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad4", "0", "NumPad7", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad4", "0", "NumPad9", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad4", "0", "NumPad8", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad4", "0", "NumPad0", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad3", "0", "NumPad3", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad3", "0", "NumPad5", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad3", "0", "NumPad7", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad3", "0", "NumPad9", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad3", "0", "NumPad8", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad3", "0", "NumPad0", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad5", "0", "NumPad5", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad5", "0", "NumPad7", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad5", "0", "NumPad9", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad5", "0", "NumPad8", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad5", "0", "NumPad0", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad7", "0", "NumPad7", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "0", "NumPad9", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "0", "NumPad8", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "0", "NumPad0", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad9", "0", "NumPad9", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad9", "0", "NumPad8", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad9", "0", "NumPad0", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		
		KB_features_DL_brute_SelKeyUD("NumPad8", "0", "NumPad8", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad8", "0", "NumPad0", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad0", "0", "NumPad0", "1", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		//Lactency
		KB_features_DL_brute_SelKeyUD("NumPad2", "1", "NumPad4", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);		
		KB_features_DL_brute_SelKeyUD("NumPad2", "1", "NumPad3", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "1", "NumPad5", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "1", "NumPad7", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "1", "NumPad9", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "1", "NumPad8", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad2", "1", "NumPad0", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad4", "1", "NumPad3", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad4", "1", "NumPad5", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad4", "1", "NumPad7", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad4", "1", "NumPad9", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad4", "1", "NumPad8", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad4", "1", "NumPad0", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad3", "1", "NumPad5", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad3", "1", "NumPad7", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad3", "1", "NumPad9", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad3", "1", "NumPad8", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad3", "1", "NumPad0", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad5", "1", "NumPad7", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad5", "1", "NumPad9", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad5", "1", "NumPad8", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad5", "1", "NumPad0", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad7", "1", "NumPad9", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "1", "NumPad8", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad7", "1", "NumPad0", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
				
		KB_features_DL_brute_SelKeyUD("NumPad9", "1", "NumPad8", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		KB_features_DL_brute_SelKeyUD("NumPad9", "1", "NumPad0", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
		KB_features_DL_brute_SelKeyUD("NumPad8", "1", "NumPad0", "0", al_extract_KeyUD, al_extract_KeyUD_features, false);
		
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
		int isSuccessSeq = 0;
		boolean isLastSeqSuccess = false;
		
		int ite_outputAL = 0;
		
		//過程中會把non-success的seq.都去除不計~
		for(int i = 0; i < al_extract_KeyUD.size(); i++){
			CurrentID = Integer.parseInt(((String)((ArrayList)al_extract_KeyUD.get(i)).get(0)));			
			if (CurrentID != LastID){				
				if (isLastSeqSuccess){
					if (new_featureTab){
						tmpAL = new ArrayList();
						tmpAL.add(String.valueOf(LastID));
						tmpAL.add(String.valueOf(isSuccessSeq));
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
				isSuccessSeq = Integer.parseInt(((String)((ArrayList)al_extract_KeyUD.get(i)).get(1)));				
				LastID = CurrentID;				
			}
			
			if (isSuccessSeq == 0){
				isLastSeqSuccess = false;

				do{
					i++;
					CurrentID = Integer.parseInt(((String)((ArrayList)al_extract_KeyUD.get(i)).get(0)));
				}while(CurrentID == LastID);
				i = i - 1; // 因為for loop時iterator會再加一~
				
			}else
				isLastSeqSuccess = true;
			
			calTab.put(((String)((ArrayList)al_extract_KeyUD.get(i)).get(5))
					+((String)((ArrayList)al_extract_KeyUD.get(i)).get(6)), ((ArrayList)al_extract_KeyUD.get(i)).get(4));
			
		}
		//別忘了最後一個event也要補處理 (因為是使用if (CurrentID != LastID))
		if (isLastSeqSuccess){
			if (new_featureTab){
				tmpAL = new ArrayList();
				tmpAL.add(String.valueOf(LastID));
				tmpAL.add(String.valueOf(isSuccessSeq));
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
	
	
	
	public static void KB_features_Err(String keyAndUD2_Path, String keystrokeUD_extrac_features1){
		ArrayList al_extract_KeyUD, al_extract_KeyUD_features1, al_extract_KeyUD_featuresALL;
		al_extract_KeyUD = fileRead.get2D_arraylist_string_from_file(keyAndUD2_Path, ",");
		al_extract_KeyUD_features1 = fileRead.get2D_arraylist_string_from_file(keystrokeUD_extrac_features1, ",");
		
		//幫success的id全補0
		for(int i = 0; i < al_extract_KeyUD_features1.size(); i++){
			((ArrayList)al_extract_KeyUD_features1.get(i)).add("0");
			((ArrayList)al_extract_KeyUD_features1.get(i)).add("0");
			((ArrayList)al_extract_KeyUD_features1.get(i)).add("0");
			((ArrayList)al_extract_KeyUD_features1.get(i)).add("0");
		}
		
		al_extract_KeyUD_featuresALL = (ArrayList) al_extract_KeyUD_features1.clone();
		
		//把success的id事先全部去除 (增速)
		for(int i = 0; i < al_extract_KeyUD.size(); i++)
			if (((String)((ArrayList)al_extract_KeyUD.get(i)).get(1)).equals("1")){
				al_extract_KeyUD.remove(i);
				i = -1;
			}
		
		
		int CurrentID, LastID = -1;		
		int[] NumSeqCounting = null;
		int type1num = 0, type2num = 0, type3num = 0, type4num = 0;
		
		
		//為了計算type4num
		String[] LCSREF = {"NumPad2", "NumPad4", "NumPad3", "NumPad5", "NumPad7", "NumPad9", "NumPad8", "NumPad0"};
		ArrayList<String> LCS_Customer = null;
		
		ArrayList rowAL;
		
		for(int i = 0; i < al_extract_KeyUD.size(); i++){
			CurrentID = Integer.parseInt(((String)((ArrayList)al_extract_KeyUD.get(i)).get(0)));			
			if (CurrentID != LastID){	
				if (i != 0){
					/*要做一些計算Error並且存檔的事情; 並在i == al_extract_KeyUD.size()時要把最後一個項目加進去,
					因為是"if (CurrentID != LastID)"寫法		
					type1: 漏打目標char -- 個數 // 目標char沒打到的個數
					type2: 多打目標char -- 個數 // 目標char被多打的個數
					type3: 多非目標char -- 個數 // 非目標char被打出來的個數				
					type4: (8 - Longest Common Subsequence的長度) */ // 參考: http://www.csie.ntnu.edu.tw/~u91029/LongestCommonSubsequence.html
					// type0: - <其它：系統Err, 不計入USER ERROR> -- 直接濾除不包含於Data中
					type1num = 0;type2num = 0;type3num = 0;type4num = 0;					
					for (int j = 1; j < NumSeqCounting.length; j++){
						if (NumSeqCounting[j] == 0)type1num++;
					}					
					for (int j = 1; j < NumSeqCounting.length; j++){
						if (NumSeqCounting[j] > 1)type2num = type2num + (NumSeqCounting[j]-1);
					}					
					type3num = NumSeqCounting[0];				
					type4num = 8 - myLCS.LongestCommonSubsequence(LCSREF, LCS_Customer.toArray(new String[LCS_Customer.size()])).size();
					
					rowAL = new ArrayList();
					rowAL.add(String.valueOf(LastID));
					rowAL.add("0");
					
					for(int j = 0; j < (((ArrayList)al_extract_KeyUD_features1.get(0)).size() - 4 - 2);j++) // 好features全部留空
						rowAL.add("0");
					
					rowAL.add(String.valueOf(type1num));
					rowAL.add(String.valueOf(type2num));
					rowAL.add(String.valueOf(type3num));
					rowAL.add(String.valueOf(type4num));
					al_extract_KeyUD_featuresALL.add(rowAL);
				}
				
				LCS_Customer = new ArrayList();
				NumSeqCounting = new int[9]; //[0]是other
				LastID = CurrentID; 
			}
			
			
			
			if (((String)((ArrayList)al_extract_KeyUD.get(i)).get(6)).equals("0")){
				LCS_Customer.add((String) ((ArrayList)al_extract_KeyUD.get(i)).get(5)); //為了計算type4num
				
				//OTHER-2-4-3-5-7-9-8-0
				if (((String)((ArrayList)al_extract_KeyUD.get(i)).get(5)).equals("NumPad2"))
					NumSeqCounting[1]++; 
				else if (((String)((ArrayList)al_extract_KeyUD.get(i)).get(5)).equals("NumPad4"))
					NumSeqCounting[2]++;
				else if (((String)((ArrayList)al_extract_KeyUD.get(i)).get(5)).equals("NumPad3"))
					NumSeqCounting[3]++;
				else if (((String)((ArrayList)al_extract_KeyUD.get(i)).get(5)).equals("NumPad5"))
					NumSeqCounting[4]++;
				else if (((String)((ArrayList)al_extract_KeyUD.get(i)).get(5)).equals("NumPad7"))
					NumSeqCounting[5]++;
				else if (((String)((ArrayList)al_extract_KeyUD.get(i)).get(5)).equals("NumPad9"))
					NumSeqCounting[6]++;
				else if (((String)((ArrayList)al_extract_KeyUD.get(i)).get(5)).equals("NumPad8"))
					NumSeqCounting[7]++;
				else if (((String)((ArrayList)al_extract_KeyUD.get(i)).get(5)).equals("NumPad0"))
					NumSeqCounting[8]++;
				else
					NumSeqCounting[0]++;
			}
			
			
			
		}
		/*要做一些計算Error並且存檔的事情; 並在i == al_extract_KeyUD.size()時要把最後一個項目加進去,
		因為是"if (CurrentID != LastID)"寫法		
		type1: 漏打目標char -- 個數 // 目標char沒打到的個數
		type2: 多打目標char -- 個數 // 目標char被多打的個數
		type3: 多非目標char -- 個數 // 非目標char被打出來的個數				
		type4: (8 - Longest Common Subsequence的長度) */ // 參考: http://www.csie.ntnu.edu.tw/~u91029/LongestCommonSubsequence.html
		// type0: - <其它：系統Err, 不計入USER ERROR> -- 直接濾除不包含於Data中
		type1num = 0;type2num = 0;type3num = 0;type4num = 0;					
		for (int j = 1; j < NumSeqCounting.length; j++){
			if (NumSeqCounting[j] == 0)type1num++;
		}					
		for (int j = 1; j < NumSeqCounting.length; j++){
			if (NumSeqCounting[j] > 1)type2num = type2num + (NumSeqCounting[j]-1);
		}					
		type3num = NumSeqCounting[0];				
		type4num = 8 - myLCS.LongestCommonSubsequence(LCSREF, LCS_Customer.toArray(new String[LCS_Customer.size()])).size();
		
		rowAL = new ArrayList();
		rowAL.add(String.valueOf(LastID));
		rowAL.add("0");
		
		for(int j = 0; j < (((ArrayList)al_extract_KeyUD_features1.get(0)).size() - 4 - 2);j++) // 好features全部留空
			rowAL.add("0");
		
		rowAL.add(String.valueOf(type1num));
		rowAL.add(String.valueOf(type2num));
		rowAL.add(String.valueOf(type3num));
		rowAL.add(String.valueOf(type4num));
		al_extract_KeyUD_featuresALL.add(rowAL);
		
				
		//fileWrite.write_2d_arraylist_string2file("keystrokeUD_extrac_features1(補0).csv", al_extract_KeyUD_features1, true); // new file
		fileWrite.write_2d_arraylist_string2file("keystrokeUD_extrac_features2.csv", al_extract_KeyUD_featuresALL, true); // new file
		
	}
	
	 
	//toobox_dev_v2.7.2新寫: 不補零, missing data改成各subject的平均數值s
	public static void KB_features_Err_missDataHandle(String pic_results, String keystrokeUD_extrac_features2){
		ArrayList al_pic_results, al_extract_KeyUD_features2;		
		
		al_pic_results = fileRead.get2D_arraylist_string_from_file(pic_results, ",");
		al_extract_KeyUD_features2 = fileRead.get2D_arraylist_string_from_file(keystrokeUD_extrac_features2, ",");
		
		ArrayList rowAL, subject_ids = null, subject_features;
		String subject, CurrentID;
		long avgValue;
		for(int i = 0; i < al_extract_KeyUD_features2.size(); i++){
			rowAL = (ArrayList)al_extract_KeyUD_features2.get(i);
			if (((String)rowAL.get(1)).equals("0")){
				CurrentID = (String)rowAL.get(0);
				
				for(int j = 0; j < al_pic_results.size(); j++){
					if (((String)((ArrayList)al_pic_results.get(j)).get(0)).equals((CurrentID))){
						subject = (String)((ArrayList)al_pic_results.get(j)).get(3);
						subject_ids = new ArrayList();
						for(int k = 0; k < al_pic_results.size(); k++){
							if (((String)((ArrayList)al_pic_results.get(k)).get(3)).equals((subject)))
								subject_ids.add((String)((ArrayList)al_pic_results.get(k)).get(0));
						}
						break;
					}					
				}
				
				subject_features = new ArrayList();
				for (int j = 0; j < al_extract_KeyUD_features2.size(); j++){
					if (subject_ids.contains(((String)((ArrayList)al_extract_KeyUD_features2.get(j)).get(0))))
						if (((String)((ArrayList)al_extract_KeyUD_features2.get(j)).get(1)).equals("1"))
							//因為剩下的同subject打的isSuccessSeq = false的 -- 都要用同樣的平均數值 -- 只看isSuccessSeq的
							subject_features.add((ArrayList)al_extract_KeyUD_features2.get(j));						
				}
				
				for (int j = 2; j < (((ArrayList)al_extract_KeyUD_features2.get(0)).size() - 4); j++){
				
					avgValue = 0L;
					for (int k = 0; k < subject_features.size(); k++){
						avgValue = avgValue + Long.parseLong(((String)((ArrayList)subject_features.get(k)).get(j)));
					}
					avgValue = avgValue / subject_features.size();
				
					((ArrayList)al_extract_KeyUD_features2.get(i)).set(j, String.valueOf(avgValue));
				}
			}
			
		}
		
		fileWrite.write_2d_arraylist_string2file("keystrokeUD_ffeatures.csv", al_extract_KeyUD_features2, true); // new file
	}
	
	public static void main(String[] args) {
			
		killFilesAtRoot.kill(); //只有要做pic_results_gen()時才有需要打開.
		
		/* "KB_results_gen"會包在"pic_results_gen"中一起執行, generate 3個table
		 * 
		 * input: 一個directory, 裡面都是directories(以學號命名)
		 * procedure: 校準csharp ticks, 時間以及label所有的events, output combined keyboard/mouse data, 並tag上event_ids; 
		 * 同時將所有task的complete time, 與RT & time complete related features都算出來
		 * 
		 * output: pic_results.csv, keyUD.csv, keystroke.csv, RT_keyboard_event.csv,
		 * mouseUD.csv, mouseMove.csv
		 * id_timeTaskcomplete.csv (輸出每個trial中task完成的時間 -- 用以mouse tasks and SAM對齊),   
		 * RT_MouseClick.csv
		 * RT_MouseMove.csv
		 * RT_SAM_MouseClick.csv
		 * RT_SAM_MouseMove.csv
		 * Time_Of4Completes_plus_SAM_Valid.csv
		 * 
		 * P.S. mouseClick.csv因為只有左鍵沒啥用所以不做...
		 */
		pic_results_gen();
		
		
		/* input: mouseUD.csv
		 * procedure: 把mouseClick相關features都算出; 並輸出ClickN_Ticks x4 共"mouseMove_features_gen()"使用 
		 * output: mouseUD_features.csv 
		 */
		mouseClick_features_gen("mouseUD.csv");
		
		/* input: mouseMove.csv, mouseUD_features.csv 
		 * procedure: 透過"mouseUD_features.csv"對4 clicks的時間定位, 加諸人工製成的"位置定位", 把mouseMove相關features都算出... 
		 * output: mouseMove_features.csv 
		 */
		mouseMove_features_gen("mouseMove.csv", "mouseUD_features.csv");
		
		
		
		/* input: pic_results.csv; (但不依定要是它 -- 只要specify pic_col, 以及欲ranking的col就行了)
		 * procedure: 針對每個人對picture的valence, arousal做ranking
		 * 若ranking相同時, valence與arousal
		 * output: pic_results2_withRankings.csv // Ranking資訊會排在"所有"col的"最後面" 
		 */
		//6,7 
		//pic_results_gen2_addRankings("pic_results.csv", "pic_results2_withRankings6.csv", 2, 6);
		//pic_results_gen2_addRankings("pic_results2_withRankings6.csv",  "pic_results2_withRankings67.csv", 2, 7);
		
		/* input: "keystroke.csv", "keyUD.csv"
		 * procedure: 將兩個檔案mix起來, 標示key及updown的時間
		 * output: keystrokeUD.csv
		 */
		KB_results_comb("keystroke.csv", "keyUD.csv");
		
		/* input: keystrokeUD.csv
		 * procedure: 辨識出是否為isSuccessSeq, 若是就填1 否填0
		 * output: keystrokeUD_isSuccessSeq.csv
		 */		
		KB_features_tagSuccess("keystrokeUD.csv");	
				
		
		///------------前置作業完畢，開始輸出feautures-----------
		///------------前置作業完畢，開始輸出feautures-----------
		///------------前置作業完畢，開始輸出feautures-----------
		
		
		/*(2014/3/23)新寫的function; 透過
		pic_results.csv
		RT_keyboard_event.csv
		keystrokeUD_isSuccessSeq.csv
		整理出"100B_CFM_EXPR_preprocDone.csv", format: "subject,gender,picture,valence,arousal,RT,correct(key-level),feature,value"*/
		J16_newOutput("pic_results.csv","RT_keyboard_event.csv", "keystrokeUD_isSuccessSeq.csv");
		
		
		
		/* input: keystrokeUD_isSuccessSeq.csv
		 * procedure: 針對success的seqs把所有組合的latency與duration算出features;
		 * 注意--not success的seqs是沒被看也沒被output的.
		 * output: keystrokeUD_extrac_features1.csv
		 */				
		//KB_features_DL_brute("keystrokeUD_isSuccessSeq.csv");
		
		/* input: keystrokeUD_extrac_features1.csv
		 * procedure: 針對not success的seqs計算not success專用的features;
		 * 並補上所有組合的latency與duration算出features (補0); success的部分, not success專用features都補0
		 * output: keystrokeUD_extrac_features2.csv (這是完整combined的鍵盤features)
		 */				
		//KB_features_Err("keystrokeUD_isSuccessSeq.csv", "keystrokeUD_extrac_features1.csv");
		
		/* input: pic_results.csv, keystrokeUD_extrac_features2.csv
		 * procedure: not success seq.的部分, missing data不補0, 改補"subject"該feature的平均值;
		 * output: keystrokeUD_ffeatures.csv (這是完整combined的鍵盤features)
		 */	
		//KB_features_Err_missDataHandle("pic_results.csv", "keystrokeUD_extrac_features2.csv");
		

		
		/* --- 還沒做的功能s --- 
		 * 目前都手動XD 並且combine 所有基礎資訊 (pic_results.csv); 是最終結果
		 */
		
	}

}
