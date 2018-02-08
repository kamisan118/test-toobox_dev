package preproc;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.TimeZone;

import fsOp.*;

public class unidentified_preproc_manager_CFM_EXPR2_data {

	//[plz. delete it]
	private final int MouseEpisodeThreshold = 10; //unit: 1/100 sec.
	//[plz. delete it]
	private final int KeyBoardEpisodeThreshold = 500; //unit: 1/100 sec.		
	
	
			
	//[plz. delete it]
	public double[][] feature_extract_CFM_EXPR2_single_subject(ArrayList path_list, int interval_sec, int dimNum, int feedback_max, double symbol_MissingData){		
		
		ArrayList FeedBack = null;
		ArrayList Keyboard = null;
		ArrayList MouseClickWheel = null;
		ArrayList MouseMove = null;
		ArrayList MouseUpDown = null;
		ArrayList MouseUpDown_r = new ArrayList();
		ArrayList MouseUpDown_l = new ArrayList();
		
		String fileName;
		for(int i=0; i < path_list.size(); i++){			
			fileName = new File(path_list.get(i).toString()).getName();
			if (fileName.equals("FeedBack.txt"))
				FeedBack = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());			
			else if (fileName.equals("KeyStroke.txt"))
				Keyboard = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());
			else if (fileName.equals("MouseClick&Wheel.txt"))
				MouseClickWheel = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());
			else if (fileName.equals("MouseMove.txt"))
				MouseMove = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());
			else if (fileName.equals("MouseUp&Down.txt"))
				MouseUpDown = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());
			
			//System.out.print(String.valueOf(FeedBack.size()));
		}
		

		int max_ite_of_FB;
		if (feedback_max==-1)max_ite_of_FB = FeedBack.size();
		else max_ite_of_FB = feedback_max;


		//[主程式!! 基於Tick]
		//2012/01/13 14:00 設計原則: 模組化, 一道一道做(speed low (cost), to enhance on flexibility)		
		
		//1: 先把MousUpDown分成left and right (並同時把多出來的, e.g. 有Up無Down或有Down無Up, 給幹掉)
		ArrayList tmpAL1 = null;
		ArrayList tmpAL2 = null;
		int ite_mouse_click = 0;
		String note_ite_tick = null;
		String note_ite_label = null;

		// [after v1.5.0]
		// utilize the fact that --- 
		// From "MouseClick&Wheel.txt" to "MouseUp&Down.txt"
		// there is no "missing data", however, 
		// data losing in "MouseUp&Down.txt" are found .. with no "pair",
		// that is, exists 0, without 1 correspondingly.
		for(int i=0; i < MouseClickWheel.size(); i++){
			//set "note_ite_tick" and "note_ite_label" first, then find the corresponding "durations" 
			note_ite_tick = ((String)((ArrayList)((ArrayList)MouseClickWheel
					.get(i))).get(2));
			note_ite_label = ((String)((ArrayList)((ArrayList)MouseClickWheel
					.get(i))).get(3));
			
			if (note_ite_label.equals("0")){
				
				do{
					tmpAL1 = (ArrayList)MouseUpDown.get(ite_mouse_click);
					ite_mouse_click++;
				}while(!((String)tmpAL1.get(2)).equals(note_ite_tick));
						
				if (ite_mouse_click == MouseUpDown.size())
					break;
				else{
					tmpAL2 = (ArrayList)MouseUpDown.get(ite_mouse_click);
					if (((String)tmpAL2.get(3)).equals("1")){
						MouseUpDown_l.add(tmpAL1);
						MouseUpDown_l.add(tmpAL2);					
						ite_mouse_click++;
					}					
				}
				
			}
			else if (note_ite_label.equals("1")){				
				
				do{
					tmpAL1 = (ArrayList)MouseUpDown.get(ite_mouse_click);
					ite_mouse_click++;
				}while(!((String)tmpAL1.get(2)).equals(note_ite_tick));				
				
				if (ite_mouse_click == MouseUpDown.size())
					break;
				else{
					tmpAL2 = (ArrayList)MouseUpDown.get(ite_mouse_click);
					if (((String)tmpAL2.get(3)).equals("1")){
						MouseUpDown_r.add(tmpAL1);
						MouseUpDown_r.add(tmpAL2);					
						ite_mouse_click++;
					}					
				}
			}
			
		}
		
		/*//[v1.5.0 and previous method of handling]
		for(int i=0; i < MouseUpDown.size(); i+=2){
			tmpAL1 = (ArrayList)MouseUpDown.get(i);						
			while(!((String)tmpAL1.get(3)).equals("0")){
				i++;
				tmpAL1 = (ArrayList)MouseUpDown.get(i);
			}

			//有的時候最後一個element是"按下去"XDDDD(沒有成對~~要幹掉)
			if((i == (MouseUpDown.size()-1))&&((String)((ArrayList)MouseUpDown.get(i)).get(3)).equals("0"))break;
			
			tmpAL2 = (ArrayList)MouseUpDown.get(i+1);
			while(!((String)tmpAL2.get(3)).equals("1")){
				i++;
				tmpAL2 = (ArrayList)MouseUpDown.get(i+1);
			}
									 
			while(!((String)((ArrayList)((ArrayList)MouseClickWheel.get(ite_mouse_click)))
					.get(2)).equals((String)tmpAL1.get(2))){								
				ite_mouse_click++;	
			}//過了這裡就是找到了~
			
			if (((String)((ArrayList)((ArrayList)MouseClickWheel.get(ite_mouse_click))).get(3)).equals("0")){
				MouseUpDown_l.add(tmpAL1);
				MouseUpDown_l.add(tmpAL2);
			}					
			else if (((String)((ArrayList)((ArrayList)MouseClickWheel.get(ite_mouse_click))).get(3)).equals("1")){
				MouseUpDown_r.add(tmpAL1);
				MouseUpDown_r.add(tmpAL2);
			}
		}*/
		

		//----------這邊真的要開始feature extraction了, 其中"MouseClickWheel"因為沒有用到所以沒有放進來喔!-----------
		double[][] returnData = new double[max_ite_of_FB][dimNum];
		
		
		ArrayList Keyboard_window = null;			
		ArrayList MouseMove_window = null;		
		ArrayList MouseUpDown_r_window = null;
		ArrayList MouseUpDown_l_window = null;
		
		int ite_Keyboard = 0;
		int ite_MouseMove = 0;
		int ite_MouseUpDown_r = 0;
		int ite_MouseUpDown_l = 0;		
		
		long tickBase;
		long tickCoverStart;
		

		for(int i=0; i < max_ite_of_FB; i++){	
		// 2: 先萃取時間區間的資料出來, 然後再個別做處理(做出要的feature) 

			tickBase = Long.parseLong(((String)((ArrayList)FeedBack.get(i)).get(2)));
			tickCoverStart = tickBase - (10000000L * 1L * interval_sec); //因為100 nm 為 1 tick

			Keyboard_window = new ArrayList();
			MouseMove_window = new ArrayList();
			MouseUpDown_r_window = new ArrayList();
			MouseUpDown_l_window = new ArrayList();
			
			while((ite_Keyboard != Keyboard.size())
					&&(Long.parseLong(((String)((ArrayList)Keyboard
							.get(ite_Keyboard)).get(2))) < tickBase)){
				
				if (Long.parseLong(((String)((ArrayList)Keyboard
						.get(ite_Keyboard)).get(2))) >= tickCoverStart){
					
					//abandon, if the record is duplicated!
					if (ite_Keyboard != 0)
						if (((String)((ArrayList)Keyboard.get(ite_Keyboard)).get(2))
						.equals(((String)((ArrayList)Keyboard.get(ite_Keyboard-1)).get(2)))){
							ite_Keyboard++;
							continue;
						}						
					Keyboard_window.add(Keyboard.get(ite_Keyboard));
				}
				ite_Keyboard++;
			}
			while((ite_MouseMove != MouseMove.size())&&(Long.parseLong(((String)((ArrayList)MouseMove.get(ite_MouseMove)).get(2))) < tickBase)){
				if (Long.parseLong(((String)((ArrayList)MouseMove
						.get(ite_MouseMove)).get(2))) >= tickCoverStart){
					//abandon, if the record is duplicated!
					if (ite_MouseMove != 0)
						if (((String)((ArrayList)MouseMove.get(ite_MouseMove)).get(2))
						.equals(((String)((ArrayList)MouseMove.get(ite_MouseMove-1)).get(2)))){
							ite_MouseMove++;
							continue;
						}
					MouseMove_window.add(MouseMove.get(ite_MouseMove));
				}
					
				
				ite_MouseMove++;				
			}
			while((ite_MouseUpDown_r != MouseUpDown_r.size())&&(Long.parseLong(((String)((ArrayList)MouseUpDown_r.get(ite_MouseUpDown_r)).get(2))) < tickBase)){
				if (Long.parseLong(((String)((ArrayList)MouseUpDown_r.get(ite_MouseUpDown_r)).get(2))) >= tickCoverStart)
					MouseUpDown_r_window.add(MouseUpDown_r.get(ite_MouseUpDown_r));
				
				ite_MouseUpDown_r++;				
			}
			while((ite_MouseUpDown_l != MouseUpDown_l.size())&&(Long.parseLong(((String)((ArrayList)MouseUpDown_l.get(ite_MouseUpDown_l)).get(2))) < tickBase)){
				if (Long.parseLong(((String)((ArrayList)MouseUpDown_l.get(ite_MouseUpDown_l)).get(2))) >= tickCoverStart)
					MouseUpDown_l_window.add(MouseUpDown_l.get(ite_MouseUpDown_l));
				
				ite_MouseUpDown_l++;				
			}
			
			feature_extract_CFM_EXPR2_single_feedback_with_window_extracted((ArrayList)FeedBack.get(i), 
					Keyboard_window, MouseMove_window, MouseUpDown_r_window, 
					MouseUpDown_l_window,  returnData, i, symbol_MissingData);
		}
		
		
		return returnData;
		
	}
	
	//[plz. delete it]
	public double[][] feature_extract_latest_interval_c07(ArrayList path_list, int interval_sec, int dimNum, double symbol_MissingData){		
				
		ArrayList Keyboard = null;
		ArrayList MouseClickWheel = null;
		ArrayList MouseMove = null;
		ArrayList MouseUpDown = null;
		ArrayList MouseUpDown_r = new ArrayList();
		ArrayList MouseUpDown_l = new ArrayList();
		
		String fileName;
		for(int i=0; i < path_list.size(); i++){			
			fileName = new File(path_list.get(i).toString()).getName();			
			if (fileName.equals("Keyboard.txt"))
				Keyboard = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());
			else if (fileName.equals("MouseClick&Wheel.txt"))
				MouseClickWheel = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());
			else if (fileName.equals("MouseMove.txt"))
				MouseMove = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());
			else if (fileName.equals("MouseUp&Down.txt"))
				MouseUpDown = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());
			
			//System.out.print(String.valueOf(FeedBack.size()));
		}
				
		//[主程式!! 基於Tick]
		//2012/01/13 14:00 設計原則: 模組化, 一道一道做(speed low (cost), to enhance on flexibility)		
		
		//1: 先把MousUpDown分成left and right (並同時把多出來的, e.g. 有Up無Down或有Down無Up, 給幹掉)
		ArrayList tmpAL1 = null;
		ArrayList tmpAL2 = null;
		int ite_mouse_click = 0;
		for(int i=0; i < MouseUpDown.size(); i+=2){
			tmpAL1 = (ArrayList)MouseUpDown.get(i);						
			while(!((String)tmpAL1.get(3)).equals("0")){
				i++;
				tmpAL1 = (ArrayList)MouseUpDown.get(i);
			}

			//有的時候最後一個element是"按下去"XDDDD(沒有成對~~要幹掉)
			if((i == (MouseUpDown.size()-1))&&((String)((ArrayList)MouseUpDown.get(i)).get(3)).equals("0"))break;
			
			tmpAL2 = (ArrayList)MouseUpDown.get(i+1);
			while(!((String)tmpAL2.get(3)).equals("1")){
				i++;
				tmpAL2 = (ArrayList)MouseUpDown.get(i+1);
			}
									 
			while(!((String)((ArrayList)((ArrayList)MouseClickWheel.get(ite_mouse_click)))
					.get(2)).equals((String)tmpAL1.get(2))){								
				ite_mouse_click++;	
			}//過了這裡就是找到了~
			
			if (((String)((ArrayList)((ArrayList)MouseClickWheel.get(ite_mouse_click))).get(3)).equals("0")){
				MouseUpDown_l.add(tmpAL1);
				MouseUpDown_l.add(tmpAL2);
			}					
			else if (((String)((ArrayList)((ArrayList)MouseClickWheel.get(ite_mouse_click))).get(3)).equals("1")){
				MouseUpDown_r.add(tmpAL1);
				MouseUpDown_r.add(tmpAL2);
			}
		}
		

		//----------這邊真的要開始feature extraction了, 其中"MouseClickWheel"因為沒有用到所以沒有放進來喔!-----------
		double[][] returnData = new double[1][dimNum];
		
		
		ArrayList Keyboard_window = null;			
		ArrayList MouseMove_window = null;		
		ArrayList MouseUpDown_r_window = null;
		ArrayList MouseUpDown_l_window = null;
		
		int ite_Keyboard = 0;
		int ite_MouseMove = 0;
		int ite_MouseUpDown_r = 0;
		int ite_MouseUpDown_l = 0;		
		
		long tickBase;
		long tickCoverStart;
		

		for(int i=0; i < 1; i++){	
		// 2: 先萃取時間區間的資料出來, 然後再個別做處理(做出要的feature) 

			Calendar calendar = Calendar.getInstance();  
			long timeJava_ms = calendar.getTime().getTime();
			//calendar.set(Calendar.MILLISECOND, 0);
			calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));		
			calendar.set(0001, 1, 1, 0, 0, 0);
			long offset_Java_ms = calendar.getTime().getTime() * (-1);		
			long millis = 25056200000000L + (timeJava_ms+offset_Java_ms)*10000;// + System.nanoTime()/10;
			//25056200000000L是手動tune出來的..幹...大概會誤差幾秒吧...不知名的offset...
			tickBase = millis;
			//tickBase = Long.parseLong(((String)((ArrayList)FeedBack.get(i)).get(2)));
			tickCoverStart = tickBase - (10000000 * 1L * interval_sec); //因為100 nm 為 1 tick

			Keyboard_window = new ArrayList();
			MouseMove_window = new ArrayList();
			MouseUpDown_r_window = new ArrayList();
			MouseUpDown_l_window = new ArrayList();
			
			while((ite_Keyboard != Keyboard.size())&&(Long.parseLong(((String)((ArrayList)Keyboard.get(ite_Keyboard)).get(2))) < tickBase)){
				if (Long.parseLong(((String)((ArrayList)Keyboard.get(ite_Keyboard)).get(2))) >= tickCoverStart)
					Keyboard_window.add(Keyboard.get(ite_Keyboard));
				
				ite_Keyboard++;				
			}
			while((ite_MouseMove != MouseMove.size())&&(Long.parseLong(((String)((ArrayList)MouseMove.get(ite_MouseMove)).get(2))) < tickBase)){
				if (Long.parseLong(((String)((ArrayList)MouseMove.get(ite_MouseMove)).get(2))) >= tickCoverStart)
					MouseMove_window.add(MouseMove.get(ite_MouseMove));
				
				ite_MouseMove++;				
			}
			while((ite_MouseUpDown_r != MouseUpDown_r.size())&&(Long.parseLong(((String)((ArrayList)MouseUpDown_r.get(ite_MouseUpDown_r)).get(2))) < tickBase)){
				if (Long.parseLong(((String)((ArrayList)MouseUpDown_r.get(ite_MouseUpDown_r)).get(2))) >= tickCoverStart)
					MouseUpDown_r_window.add(MouseUpDown_r.get(ite_MouseUpDown_r));
				
				ite_MouseUpDown_r++;				
			}
			while((ite_MouseUpDown_l != MouseUpDown_l.size())&&(Long.parseLong(((String)((ArrayList)MouseUpDown_l.get(ite_MouseUpDown_l)).get(2))) < tickBase)){
				if (Long.parseLong(((String)((ArrayList)MouseUpDown_l.get(ite_MouseUpDown_l)).get(2))) >= tickCoverStart)
					MouseUpDown_l_window.add(MouseUpDown_l.get(ite_MouseUpDown_l));
				
				ite_MouseUpDown_l++;				
			}
			
			
			ArrayList fakeFeedBack = new ArrayList();
			fakeFeedBack.add("-10");
			feature_extract_CFM_EXPR2_single_feedback_with_window_extracted((ArrayList)fakeFeedBack, 
					Keyboard_window, MouseMove_window, MouseUpDown_r_window, 
					MouseUpDown_l_window,  returnData, i, symbol_MissingData);
		}
		
		
		return returnData;
		
	}
	
	//[plz. delete it]
	private void feature_extract_CFM_EXPR2_single_feedback_with_window_extracted(ArrayList FeedBack_single, ArrayList KeyBoard, 
			ArrayList MouseMove, ArrayList MouseUpDown_r, ArrayList MouseUpDown_l, double[][] returnData, int feedBack_ite, double symbol_MissingData){
		// 2012/01/13 14:01: 這種model同時也allow之後作模組化, 系統只要一個時間區間一個時間區間去收資料就可以了		
		
				
		ArrayList tmp_AL_CAL = null;
		ArrayList tmpAL1 = null;
		
		int dimIte = 0;	// for feature modulization.
		
		
		//---Dim1
		//[FEATURE] Avg. of Left Click Duration.
		long MouseDuration = 0;
		int NumberOfMouseDuration = 0;
		for(int i=0; i < MouseUpDown_l.size(); i+=2){
			tmpAL1 = (ArrayList)MouseUpDown_l.get(i);
			while(!((String)tmpAL1.get(3)).equals("0")){
				i++;
				tmpAL1 = (ArrayList)MouseUpDown_l.get(i);
			}			
			//有的時候最後一個element是"按下去"XDDDD(沒有成對~~要幹掉)
			if((i == (MouseUpDown_l.size()-1))&&((String)((ArrayList)MouseUpDown_l.get(i)).get(3)).equals("0"))break;
			
			MouseDuration = MouseDuration + Long.parseLong(((String)
					((ArrayList)MouseUpDown_l.get(i+1)).get(2))) 
					- Long.parseLong(((String)((ArrayList)MouseUpDown_l.get(i)).get(2)));
			
			NumberOfMouseDuration++;
		}
		MouseDuration = MouseDuration / 100000L; // 先除以 10^5 回基準位(滑鼠打點最快也只到10^5一次)		
		if (NumberOfMouseDuration != 0)
			returnData[feedBack_ite][dimIte] = (double) (MouseDuration/NumberOfMouseDuration);
		else //Missing Data
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
		dimIte++;
		
		//---Dim2
		//[FEATURE] Avg. of Right Click Duration.
		MouseDuration = 0;
		NumberOfMouseDuration = 0;
		if (MouseUpDown_r.size() != 1){
			for(int i=0; i < MouseUpDown_r.size(); i+=2){
				tmpAL1 = (ArrayList)MouseUpDown_r.get(i);
				while(!((String)tmpAL1.get(3)).equals("0")){
					i++;				
					tmpAL1 = (ArrayList)MouseUpDown_r.get(i);
				}
				
				//有的時候最後一個element是"按下去"XDDDD(沒有成對~~要幹掉)
				if((i == (MouseUpDown_r.size()-1))&&((String)((ArrayList)MouseUpDown_r.get(i)).get(3)).equals("0"))break;
				
				MouseDuration = MouseDuration + Long.parseLong(((String)
						((ArrayList)MouseUpDown_r.get(i+1)).get(2))) 
						- Long.parseLong(((String)((ArrayList)MouseUpDown_r.get(i)).get(2)));
				
				NumberOfMouseDuration++;
			}
		}
		MouseDuration = MouseDuration / 100000; // 先除以 10^5 回基準位(滑鼠打點最快也只到10^5一次)		
		if (NumberOfMouseDuration != 0)
			returnData[feedBack_ite][dimIte] = (double) (MouseDuration/NumberOfMouseDuration);
		else //Missing Data
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
		dimIte++;
		//先把這個dim關掉了...因為好像missData過多, 看似沒什麼用
		returnData[feedBack_ite][3] = (double) 0;
		
		
		
		//---Dim3, ---Dim4, ---Dim5		
		double Distsum = 0;			
		double minV_Order1 = 0;	// 在modeling 加速度 (透過1階差分)
		double avgV = 0;			// 在modeling speed	
		
		double V_tmp_for_avg = 0;
		
		int dx;
		int dy;
		long dt;
		double dist_tmp;double V_tmp;
		
		tmp_AL_CAL = new ArrayList();
		//先求出距離
		for(int i=0; i < (MouseMove.size()-1); i++){
						
			dt = Long.parseLong(((String)
					((ArrayList)MouseMove.get(i+1)).get(2))) 
					- Long.parseLong(((String)((ArrayList)MouseMove.get(i)).get(2)));
			dt = dt / 100000; // 先除以 10^5 回基準位(滑鼠打點最快也只到10^5一次)
			
			//[Threshold Settings] n = MouseEpisodeThreshold 個單位以下的才算是同個episode的打點...!!
			//n * 10^5 = n * 10^5 * 100ns = n * 10^5 * 10^-7 s = n * 10^-2 sec.    
			if ((dt > MouseEpisodeThreshold) || (dt < 1))continue;
			
			dx = Integer.parseInt((((String)
					((ArrayList)MouseMove.get(i+1)).get(3))))
					- Integer.parseInt((((String)((ArrayList)MouseMove.get(i)).get(3))));			
			dy = Integer.parseInt((((String)
					((ArrayList)MouseMove.get(i+1)).get(4))))
					- Integer.parseInt((((String)((ArrayList)MouseMove.get(i)).get(4))));
			
			dist_tmp = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));			
			Distsum = Distsum + dist_tmp;
			
			tmp_AL_CAL.add(dist_tmp/(double)dt); // add in the velocity.
		}
		
		//calculate distances to index the velocities.
		for(int i=0; i < tmp_AL_CAL.size(); i++){
			
			if (i < (tmp_AL_CAL.size() - 1)){
				V_tmp = (Double)tmp_AL_CAL.get(i+1) 
				- (Double)tmp_AL_CAL.get(i);
				
				V_tmp = Math.abs(V_tmp);	//magnitude of force.
				
				if (V_tmp > minV_Order1)minV_Order1 = V_tmp;
			}
				
			V_tmp_for_avg = V_tmp_for_avg + (Double)tmp_AL_CAL.get(i);
		}				
		avgV = V_tmp_for_avg / tmp_AL_CAL.size();

		//Missing Data
		if (tmp_AL_CAL.size() == 0){
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
			returnData[feedBack_ite][dimIte+1] = symbol_MissingData;
			returnData[feedBack_ite][dimIte+2] = symbol_MissingData;
		}
		else{
			returnData[feedBack_ite][dimIte] = Distsum;			
			returnData[feedBack_ite][dimIte+1] = avgV;
			returnData[feedBack_ite][dimIte+2] = minV_Order1;			
		}
		dimIte+=3;
		
		
		//---Dim6	
		//[FEATURE] Avg. of Key Press Latency.
		long KeyBoardLatency = 0;
		long KeyBoardLatency_square = 0;
		long[] KeyBoardLatency_tmp = new long[KeyBoard.size()-1];

		for(int i=0; i < (KeyBoard.size()-1); i++){
						
			KeyBoardLatency_tmp[i] = (Long.parseLong(((String)
					((ArrayList)KeyBoard.get(i+1)).get(2))) 
					- Long.parseLong(((String)((ArrayList)KeyBoard.get(i)).get(2))))/1000;	// 先除以 10^3 回基準位(鍵盤打點最快只要10^3一次)
						
		}
		long sum = 0L;
		double avg = 0L;
		double std = 0L;
		if ((KeyBoard.size()-1) != 0)//Missing Data, 先填-1
		{
			for(int i=0; i < KeyBoardLatency_tmp.length; i++){
				sum = sum + KeyBoardLatency_tmp[i];
			}
			avg = (double)sum/KeyBoardLatency_tmp.length;
			for(int i=0; i < KeyBoardLatency_tmp.length; i++){
				std = std + (KeyBoardLatency_tmp[i]-avg)*(KeyBoardLatency_tmp[i]-avg);				
			}
			std = std/(KeyBoardLatency_tmp.length-1);
			std = Math.sqrt(std);
			
			returnData[feedBack_ite][dimIte] = (double)sum;
			dimIte++;
			returnData[feedBack_ite][dimIte] = (double)avg;
			dimIte++;
			returnData[feedBack_ite][dimIte] = (double)std;
			dimIte++;
		}
		else{
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
			dimIte++;
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
			dimIte++;
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
			dimIte++;
		}


		
		//把valance與arousal放到最後兩個dim
		if (FeedBack_single.size() < 2){
		//給假的FeedBack -- 表示現在是要做假

			//---Dim of "the one before last one"
			returnData[feedBack_ite][returnData[0].length-2] = -10.0;
			//---Dim of "the last one"
			returnData[feedBack_ite][returnData[0].length-1] = -10.0;
		}
		else{
			//---Dim of "the one before last one"
			returnData[feedBack_ite][returnData[0].length-2] = Double.parseDouble((String) (FeedBack_single.get(3)));
			//---Dim of "the last one"
			returnData[feedBack_ite][returnData[0].length-1] = Double.parseDouble((String) (FeedBack_single.get(4)));
		}
		
	}
	
	//must be debuged according to format change in 
	/*public void missingData_proc_backward(Double[][] dataset, double symbol_MissingData){
		
		for(int i=0; i < dataset.length; i++){
			for(int j=0; j < dataset[0].length; j++){
				//0先看backward, 若backward沒有再看forward (必須要是同subject)
				if (dataset[i][j] == symbol_MissingData){
					if (i > 0)						
						dataset[i][j] = dataset[i-1][j];
					else{
						int k = 0;
						while(dataset[i+k][j] == symbol_MissingData)k++;
						dataset[i][j] = dataset[i+k][j];
					}
						
				}
			}
			
		}
		
		
	}*/
	
	
	public double[][] subjectid_add(double[][] dataset, int subjectNum, int feedback_max){
		
		ArrayList noteRowWanted = new ArrayList();
		int ite_subject = 0;
		
		double[][] ans = new double[dataset.length][1 + dataset[0].length];
		for(int i=0; i < dataset.length; i++){
			if (((i+1) % feedback_max) == 0)ite_subject++;
			ans[i][0] = ite_subject;	
			for(int j=1; j < (ans[0].length); j++)
				ans[i][j] = dataset[i][j-1];
		}
		
		
		return ans;
	}
	
	public double[][] subjectid_add_dummy(double[][] dataset, int subjectNum, int feedback_max){
		
		ArrayList noteRowWanted = new ArrayList();
		int ite_subject = 0;
		
		double[][] ans = new double[dataset.length][subjectNum + dataset[0].length];
		for(int i=0; i < dataset.length; i++){
			if (((i+1) % feedback_max) == 0)ite_subject++;
			for(int j=0; j < subjectNum; j++){
				if (j==ite_subject)ans[i][j] = 1;
				else ans[i][j] = 0;
			}		
			for(int j=subjectNum; j < (ans[0].length); j++)
				ans[i][j] = dataset[i][j-subjectNum];
		}
		
		
		return ans;
	}
	
	public double[][] missingData_eliminate(double[][] dataset, double symbol_MissingData){
			
		ArrayList noteRowWanted = new ArrayList();
		double[][] ans;
		for(int i=0; i < dataset.length; i++){
			for(int j=0; j < dataset[0].length; j++){
				//0先看backward, 若backward沒有再看forward (必須要是同subject)
				if (dataset[i][j] == symbol_MissingData){					
					break;
				}else
					if (j==(dataset[0].length-1))noteRowWanted.add(i);				
			}		
		}
		ans = new double[noteRowWanted.size()][dataset[0].length];
		for(int i=0; i < noteRowWanted.size(); i++){
			for(int j=0; j < ans[0].length; j++){
				ans[i][j] = dataset[(Integer) noteRowWanted.get(i)][j];
			}		
		}
		
		return ans;
	}
	
}
