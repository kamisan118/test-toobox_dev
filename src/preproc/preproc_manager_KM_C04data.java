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

public class preproc_manager_KM_C04data {

	private final int MouseEpisodeThreshold = 10; //unit: 1/100 sec.
	private final int KeyBoardEpisodeThreshold = 500; //unit: 1/100 sec.		
	
	
	public String[][] rawData_mergeTick_c04_single_subject(ArrayList path_list){
		
		ArrayList eventSeq_tick = new ArrayList();
		ArrayList eventSeq = new ArrayList();
		
		ArrayList FeedBack = null;
		ArrayList Keyboard = null;
		ArrayList MouseClickWheel = null;
		
		String fileName;
		for(int i=0; i < path_list.size(); i++){			
			fileName = new File(path_list.get(i).toString()).getName();
			if (fileName.equals("FeedBack.txt"))
				FeedBack = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());			
			else if (fileName.equals("Keyboard.txt"))
				Keyboard = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());
			else if (fileName.equals("MouseClick&Wheel.txt"))
				MouseClickWheel = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());
			
			
			//System.out.print(String.valueOf(FeedBack.size()));
		}
		
		//transformation of MouseClickWheel
		String str = null;
		for(int i=0; i < MouseClickWheel.size(); i++){
			str = (String)((ArrayList)MouseClickWheel.get(i)).get(3);
			
			 switch(str.charAt(0)) { 
	            case '0': 
	            	((ArrayList)MouseClickWheel.get(i)).add(3,"A");
	            	break;
	            case '1': 
	            	((ArrayList)MouseClickWheel.get(i)).add(3,"B"); 
	                break; 
	            case '2': 
	            	((ArrayList)MouseClickWheel.get(i)).add(3,"C");
	                break; 
	            case '3': 
	            	((ArrayList)MouseClickWheel.get(i)).add(3,"D");
	                break; 
	        }
			 
		}
		
		
		
		long note_ite_tick = 999999999999999999L;
		if (Long.parseLong(((String)((ArrayList)((ArrayList)MouseClickWheel.get(0))).get(2))) < note_ite_tick) 
			note_ite_tick = Long.parseLong(((String)((ArrayList)((ArrayList)MouseClickWheel.get(0))).get(2)));
		if (Long.parseLong(((String)((ArrayList)((ArrayList)Keyboard.get(0))).get(2))) < note_ite_tick) 
			note_ite_tick = Long.parseLong(((String)((ArrayList)((ArrayList)Keyboard.get(0))).get(2)));
		
		int ite_Keyboard = 0;
		int ite_MouseClickWheel = 0;
		int ite_FeedBack = 0;
		long tmp_f_comp_Keyboard;		
		long tmp_f_comp_MouseClickWheel;
		long tmp_f_comp_FeedBack;
		while(true){
			if ((ite_Keyboard == Keyboard.size())&&(ite_MouseClickWheel == MouseClickWheel.size())&&(ite_FeedBack == FeedBack.size()))break;
			
			if (ite_MouseClickWheel == MouseClickWheel.size())
				tmp_f_comp_MouseClickWheel = 999999999999999999L;
			else
				tmp_f_comp_MouseClickWheel = Long.parseLong(((String)((ArrayList)((ArrayList)MouseClickWheel.get(ite_MouseClickWheel))).get(2)));						
			if (ite_Keyboard == Keyboard.size())
				tmp_f_comp_Keyboard = 999999999999999999L;
			else
				tmp_f_comp_Keyboard = Long.parseLong(((String)((ArrayList)((ArrayList)Keyboard.get(ite_Keyboard))).get(2)));
			if (ite_FeedBack == FeedBack.size())
				tmp_f_comp_FeedBack = 999999999999999999L;
			else
				tmp_f_comp_FeedBack = Long.parseLong(((String)((ArrayList)((ArrayList)FeedBack.get(ite_FeedBack))).get(2)));			
			
			if ((tmp_f_comp_Keyboard >= tmp_f_comp_MouseClickWheel) && (tmp_f_comp_MouseClickWheel >= tmp_f_comp_FeedBack)){
				eventSeq_tick.add(String.valueOf(tmp_f_comp_FeedBack - note_ite_tick));
				eventSeq.add(((ArrayList)((ArrayList)FeedBack.get(ite_FeedBack))).get(3));
				ite_FeedBack++;		
			}else if ((tmp_f_comp_Keyboard >= tmp_f_comp_FeedBack) && (tmp_f_comp_FeedBack >= tmp_f_comp_MouseClickWheel)){
				eventSeq_tick.add(String.valueOf(tmp_f_comp_MouseClickWheel	- note_ite_tick));
				eventSeq.add(((ArrayList)((ArrayList)MouseClickWheel.get(ite_MouseClickWheel))).get(3));
				ite_MouseClickWheel++;		
			}else if ((tmp_f_comp_MouseClickWheel >= tmp_f_comp_FeedBack) && (tmp_f_comp_FeedBack >= tmp_f_comp_Keyboard)){
				eventSeq_tick.add(String.valueOf(tmp_f_comp_Keyboard - note_ite_tick));
				eventSeq.add(((ArrayList)((ArrayList)Keyboard.get(ite_Keyboard))).get(3));	
				ite_Keyboard++;	
			}else if ((tmp_f_comp_MouseClickWheel >= tmp_f_comp_Keyboard) && (tmp_f_comp_Keyboard >= tmp_f_comp_FeedBack)){
				eventSeq_tick.add(String.valueOf(tmp_f_comp_FeedBack - note_ite_tick));
				eventSeq.add(((ArrayList)((ArrayList)FeedBack.get(ite_FeedBack))).get(3));
				ite_FeedBack++;	
			}else if ((tmp_f_comp_FeedBack >= tmp_f_comp_Keyboard) && (tmp_f_comp_Keyboard >= tmp_f_comp_MouseClickWheel)){
				eventSeq_tick.add(String.valueOf(tmp_f_comp_MouseClickWheel	- note_ite_tick));
				eventSeq.add(((ArrayList)((ArrayList)MouseClickWheel.get(ite_MouseClickWheel))).get(3));
				ite_MouseClickWheel++;
			}else if ((tmp_f_comp_FeedBack >= tmp_f_comp_MouseClickWheel) && (tmp_f_comp_MouseClickWheel >= tmp_f_comp_Keyboard)){
				eventSeq_tick.add(String.valueOf(tmp_f_comp_Keyboard - note_ite_tick));
				eventSeq.add(((ArrayList)((ArrayList)Keyboard.get(ite_Keyboard))).get(3));	
				ite_Keyboard++;	
			}
		}
		
		
		String[][] output = new String[eventSeq_tick.size()][2];
		for(int i=0; i < eventSeq_tick.size(); i++){
			output[i][0] = (String)eventSeq_tick.get(i);
			output[i][1] = (String)eventSeq.get(i);
		}
		
		return output;
	}
			
	public double[][] feature_extract_c04_single_subject(ArrayList path_list, int interval_min, int dimNum, int feedback_max, double symbol_MissingData){		
		
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
			else if (fileName.equals("Keyboard.txt"))
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


		//[�D�{��!! ���Tick]
		//2012/01/13 14:00 �]�p��h: �Ҳդ�, �@�D�@�D��(speed low (cost), to enhance on flexibility)		
		
		//1: ���MousUpDown����left and right (�æP�ɧ�h�X�Ӫ�, e.g. ��Up�LDown�Φ�Down�LUp, ���F��)
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

			//�����ɭԳ̫�@��element�O"���U�h"XDDDD(�S������~~�n�F��)
			if((i == (MouseUpDown.size()-1))&&((String)((ArrayList)MouseUpDown.get(i)).get(3)).equals("0"))break;
			
			tmpAL2 = (ArrayList)MouseUpDown.get(i+1);
			while(!((String)tmpAL2.get(3)).equals("1")){
				i++;
				tmpAL2 = (ArrayList)MouseUpDown.get(i+1);
			}
									 
			while(!((String)((ArrayList)((ArrayList)MouseClickWheel.get(ite_mouse_click)))
					.get(2)).equals((String)tmpAL1.get(2))){								
				ite_mouse_click++;	
			}//�L�F�o�̴N�O���F~
			
			if (((String)((ArrayList)((ArrayList)MouseClickWheel.get(ite_mouse_click))).get(3)).equals("0")){
				MouseUpDown_l.add(tmpAL1);
				MouseUpDown_l.add(tmpAL2);
			}					
			else if (((String)((ArrayList)((ArrayList)MouseClickWheel.get(ite_mouse_click))).get(3)).equals("1")){
				MouseUpDown_r.add(tmpAL1);
				MouseUpDown_r.add(tmpAL2);
			}
		}*/
		

		//----------�o��u���n�}�lfeature extraction�F, �䤤"MouseClickWheel"�]���S���Ψ�ҥH�S����i�ӳ�!-----------
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
		// 2: ��Ѩ�ɶ��϶�����ƥX��, �M��A�ӧO���B�z(���X�n��feature) 

			tickBase = Long.parseLong(((String)((ArrayList)FeedBack.get(i)).get(2)));
			tickCoverStart = tickBase - (10000000L * 60L * interval_min); //�]��100 nm �� 1 tick

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
			
			feature_extract_c04_single_feedback_with_window_extracted((ArrayList)FeedBack.get(i), 
					Keyboard_window, MouseMove_window, MouseUpDown_r_window, 
					MouseUpDown_l_window,  returnData, i, symbol_MissingData);
		}
		
		
		return returnData;
		
	}
	
	
public double[][] feature_extract_latest_interval_c07(ArrayList path_list, int interval_min, int dimNum, double symbol_MissingData){		
				
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
				
		//[�D�{��!! ���Tick]
		//2012/01/13 14:00 �]�p��h: �Ҳդ�, �@�D�@�D��(speed low (cost), to enhance on flexibility)		
		
		//1: ���MousUpDown����left and right (�æP�ɧ�h�X�Ӫ�, e.g. ��Up�LDown�Φ�Down�LUp, ���F��)
		ArrayList tmpAL1 = null;
		ArrayList tmpAL2 = null;
		int ite_mouse_click = 0;
		for(int i=0; i < MouseUpDown.size(); i+=2){
			tmpAL1 = (ArrayList)MouseUpDown.get(i);						
			while(!((String)tmpAL1.get(3)).equals("0")){
				i++;
				tmpAL1 = (ArrayList)MouseUpDown.get(i);
			}

			//�����ɭԳ̫�@��element�O"���U�h"XDDDD(�S������~~�n�F��)
			if((i == (MouseUpDown.size()-1))&&((String)((ArrayList)MouseUpDown.get(i)).get(3)).equals("0"))break;
			
			tmpAL2 = (ArrayList)MouseUpDown.get(i+1);
			while(!((String)tmpAL2.get(3)).equals("1")){
				i++;
				tmpAL2 = (ArrayList)MouseUpDown.get(i+1);
			}
									 
			while(!((String)((ArrayList)((ArrayList)MouseClickWheel.get(ite_mouse_click)))
					.get(2)).equals((String)tmpAL1.get(2))){								
				ite_mouse_click++;	
			}//�L�F�o�̴N�O���F~
			
			if (((String)((ArrayList)((ArrayList)MouseClickWheel.get(ite_mouse_click))).get(3)).equals("0")){
				MouseUpDown_l.add(tmpAL1);
				MouseUpDown_l.add(tmpAL2);
			}					
			else if (((String)((ArrayList)((ArrayList)MouseClickWheel.get(ite_mouse_click))).get(3)).equals("1")){
				MouseUpDown_r.add(tmpAL1);
				MouseUpDown_r.add(tmpAL2);
			}
		}
		

		//----------�o��u���n�}�lfeature extraction�F, �䤤"MouseClickWheel"�]���S���Ψ�ҥH�S����i�ӳ�!-----------
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
		// 2: ��Ѩ�ɶ��϶�����ƥX��, �M��A�ӧO���B�z(���X�n��feature) 

			Calendar calendar = Calendar.getInstance();  
			long timeJava_ms = calendar.getTime().getTime();
			//calendar.set(Calendar.MILLISECOND, 0);
			calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));		
			calendar.set(0001, 1, 1, 0, 0, 0);
			long offset_Java_ms = calendar.getTime().getTime() * (-1);		
			long millis = 25056200000000L + (timeJava_ms+offset_Java_ms)*10000;// + System.nanoTime()/10;
			//25056200000000L�O���tune�X�Ӫ�..�F...�j���|�~�t�X��a...�����W��offset...
			tickBase = millis;
			//tickBase = Long.parseLong(((String)((ArrayList)FeedBack.get(i)).get(2)));
			tickCoverStart = tickBase - (10000000 * 60 * interval_min); //�]��100 nm �� 1 tick

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
			feature_extract_c04_single_feedback_with_window_extracted((ArrayList)fakeFeedBack, 
					Keyboard_window, MouseMove_window, MouseUpDown_r_window, 
					MouseUpDown_l_window,  returnData, i, symbol_MissingData);
		}
		
		
		return returnData;
		
	}
	
	private void feature_extract_c04_single_feedback_with_window_extracted(ArrayList FeedBack_single, ArrayList KeyBoard, 
			ArrayList MouseMove, ArrayList MouseUpDown_r, ArrayList MouseUpDown_l, double[][] returnData, int feedBack_ite, double symbol_MissingData){
		// 2012/01/13 14:01: �o��model�P�ɤ]allow����@�Ҳդ�, �t�Υu�n�@�Ӯɶ��϶��@�Ӯɶ��϶��h����ƴN�i�H�F		
		
				
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
			//�����ɭԳ̫�@��element�O"���U�h"XDDDD(�S������~~�n�F��)
			if((i == (MouseUpDown_l.size()-1))&&((String)((ArrayList)MouseUpDown_l.get(i)).get(3)).equals("0"))break;
			
			MouseDuration = MouseDuration + Long.parseLong(((String)
					((ArrayList)MouseUpDown_l.get(i+1)).get(2))) 
					- Long.parseLong(((String)((ArrayList)MouseUpDown_l.get(i)).get(2)));
			
			NumberOfMouseDuration++;
		}
		MouseDuration = MouseDuration / 100000L; // ��H 10^5 �^��Ǧ�(�ƹ����I�̧֤]�u��10^5�@��)		
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
				
				//�����ɭԳ̫�@��element�O"���U�h"XDDDD(�S������~~�n�F��)
				if((i == (MouseUpDown_r.size()-1))&&((String)((ArrayList)MouseUpDown_r.get(i)).get(3)).equals("0"))break;
				
				MouseDuration = MouseDuration + Long.parseLong(((String)
						((ArrayList)MouseUpDown_r.get(i+1)).get(2))) 
						- Long.parseLong(((String)((ArrayList)MouseUpDown_r.get(i)).get(2)));
				
				NumberOfMouseDuration++;
			}
		}
		MouseDuration = MouseDuration / 100000; // ��H 10^5 �^��Ǧ�(�ƹ����I�̧֤]�u��10^5�@��)		
		if (NumberOfMouseDuration != 0)
			returnData[feedBack_ite][dimIte] = (double) (MouseDuration/NumberOfMouseDuration);
		else //Missing Data
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
		dimIte++;
		//���o��dim�����F...�]���n��missData�L�h, �ݦ�S�����
		returnData[feedBack_ite][3] = (double) 0;
		
		
		
		//---Dim3, ---Dim4, ---Dim5		
		double Distsum = 0;			
		double minV_Order1 = 0;	// �bmodeling �[�t�� (�z�L1���t��)
		double avgV = 0;			// �bmodeling speed	
		
		double V_tmp_for_avg = 0;
		
		int dx;
		int dy;
		long dt;
		double dist_tmp;double V_tmp;
		
		tmp_AL_CAL = new ArrayList();
		//��D�X�Z��
		for(int i=0; i < (MouseMove.size()-1); i++){
						
			dt = Long.parseLong(((String)
					((ArrayList)MouseMove.get(i+1)).get(2))) 
					- Long.parseLong(((String)((ArrayList)MouseMove.get(i)).get(2)));
			dt = dt / 100000; // ��H 10^5 �^��Ǧ�(�ƹ����I�̧֤]�u��10^5�@��)
			
			//[Threshold Settings] n = MouseEpisodeThreshold �ӳ��H�U���~��O�P��episode�����I...!!
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
		long KeyBoardLatency_tmp = 0;
		int NumberOfKeyBoardLatency = 0;
		for(int i=0; i < (KeyBoard.size()-1); i++){
						
			KeyBoardLatency_tmp = Long.parseLong(((String)
					((ArrayList)KeyBoard.get(i+1)).get(2))) 
					- Long.parseLong(((String)((ArrayList)KeyBoard.get(i)).get(2)));
			
			KeyBoardLatency_tmp = KeyBoardLatency_tmp/1000;	// ��H 10^3 �^��Ǧ�(��L���I�̧֥u�n10^3�@��)
			
			//[Threshold Settings] n = KeyBoardEpisodeThreshold �ӳ��H�U���~��O�P��episode�����I...!!
			//n * 10^5 = n * 10^5 * 100ns = n * 10^5 * 10^-7 s = n * 10^-2 sec.
			// �u��KeyBoardEpisodeThreshold * 10^5�H�U���~��O�P��episode�����I--�~�O�ڭ̭n�䪺�F��
			if(KeyBoardLatency_tmp <= KeyBoardEpisodeThreshold){					
				KeyBoardLatency = KeyBoardLatency + KeyBoardLatency_tmp;
				NumberOfKeyBoardLatency++;
			}						
		}
		if (NumberOfKeyBoardLatency != 0)//Missing Data, ���-1
			returnData[feedBack_ite][dimIte] = (double) (KeyBoardLatency/NumberOfKeyBoardLatency);
		else
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
		dimIte++;
					
		

		
		
		
		
		

		
		//��valance�Parousal���̫���dim
		if (FeedBack_single.size() < 2){
		//������FeedBack -- ��ܲ{�b�O�n����

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
				//0���backward, �Ybackward�S���A��forward (�����n�O�Psubject)
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
			ans[i][0] = ite_subject;	
			if (((i+1) % feedback_max) == 0)ite_subject++;
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
				//0���backward, �Ybackward�S���A��forward (�����n�O�Psubject)
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
