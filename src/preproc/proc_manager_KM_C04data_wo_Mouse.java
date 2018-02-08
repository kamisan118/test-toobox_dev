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

public class proc_manager_KM_C04data_wo_Mouse {

	
	private final int KeyBoardEpisodeThreshold = 30000000; //unit: 10^-7 sec.; 30000000 means 3 seconds		
	
	
			
	public double[][] feature_extract_c04_single_subject(ArrayList path_list, int interval_min, int dimNum, int feedback_max, double symbol_MissingData){		
		
		ArrayList FeedBack = null;
		ArrayList Keyboard = null;
		
		String fileName;
		for(int i=0; i < path_list.size(); i++){			
			fileName = new File(path_list.get(i).toString()).getName();
			if (fileName.equals("FeedBack.txt"))
				FeedBack = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());			
			else if (fileName.equals("Keyboard.txt"))
				Keyboard = fileRead.get2D_arraylist_string_from_file(path_list.get(i).toString());
			
			//System.out.print(String.valueOf(FeedBack.size()));
		}
		

		int max_ite_of_FB;
		if (feedback_max==-1)max_ite_of_FB = FeedBack.size();
		else max_ite_of_FB = feedback_max;


		//[�D�{��!! ���Tick]
		//2012/01/13 14:00 �]�p��h: �Ҳդ�, �@�D�@�D��(speed low (cost), to enhance on flexibility)				


		//----------�o��u���n�}�lfeature extraction�F, �䤤"MouseClickWheel"�]���S���Ψ�ҥH�S����i�ӳ�!-----------
		double[][] returnData = new double[max_ite_of_FB][dimNum];
		
		
		ArrayList Keyboard_window = null;			
		
		int ite_Keyboard = 0;
		
		long tickBase;
		long tickCoverStart;
		

		for(int i=0; i < max_ite_of_FB; i++){	
		// 2: ��Ѩ�ɶ��϶�����ƥX��, �M��A�ӧO���B�z(���X�n��feature) 

			tickBase = Long.parseLong(((String)((ArrayList)FeedBack.get(i)).get(2)));
			tickCoverStart = tickBase - (10000000L * 60L * interval_min); //�]��100 nm �� 1 tick

			Keyboard_window = new ArrayList();
			
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
			
			
			feature_extract_c04_single_feedback_with_window_extracted((ArrayList)FeedBack.get(i), 
					Keyboard_window,  returnData, i, symbol_MissingData);
		}
		
		
		return returnData;
		
	}
	
	

	
	private void feature_extract_c04_single_feedback_with_window_extracted(ArrayList FeedBack_single, ArrayList KeyBoard, double[][] returnData, int feedBack_ite, double symbol_MissingData){
		// 2012/01/13 14:01: �o��model�P�ɤ]allow����@�Ҳդ�, �t�Υu�n�@�Ӯɶ��϶��@�Ӯɶ��϶��h����ƴN�i�H�F		
		
		int dimIte = 0;	// for feature modulization.
		
		/* 
		 * 1: Avg. of Key Press Latency.
		 * 2: Std. of Key Press Latency.
		 * 3: Avg. of Keyboard Speeds
		 * 4: Std. of Keyboard Speeds
		 * 5: Number of KeyEvents
		 */
		
		//作法--透過組合一個2D的ArrayList來達到
		ArrayList onlyEpisode = new ArrayList();
		ArrayList onlyEpisode_speed = new ArrayList();		
				
		long KeyBoardLatency = 0;
		long KeyBoardLatency_tmp = 0;
		ArrayList onlyEpisode_speed_cal = new ArrayList();
		for(int i=0; i < (KeyBoard.size()-1); i++){
			
			
			KeyBoardLatency_tmp = Long.parseLong(((String)
					((ArrayList)KeyBoard.get(i+1)).get(2))) 
					- Long.parseLong(((String)((ArrayList)KeyBoard.get(i)).get(2)));			
			
			//[Threshold Settings] n = KeyBoardEpisodeThreshold �ӳ��H�U���~��O�P��episode�����I...!!
			//n * 10^5 = n * 10^5 * 100ns = n * 10^5 * 10^-7 s = n * 10^-2 sec.
			// �u��KeyBoardEpisodeThreshold * 10^5�H�U���~��O�P��episode�����I--�~�O�ڭ̭n�䪺�F��
			if(KeyBoardLatency_tmp <= KeyBoardEpisodeThreshold){				
				ArrayList onlyEpisode_row = new ArrayList();			
				onlyEpisode_row.add(String.valueOf(KeyBoardLatency_tmp));
				onlyEpisode.add(onlyEpisode_row);
				
				onlyEpisode_speed_cal.add(String.valueOf(KeyBoardLatency_tmp));
			}{
				if (onlyEpisode_speed_cal.size() != 0)
					onlyEpisode_speed.add(String.valueOf((1.0/util.math.general.avgValue(onlyEpisode_speed_cal)*10000000))); // calculate the speed
				onlyEpisode_speed_cal = new ArrayList(); 
			}
		}
		if (onlyEpisode.size() >= 2)//Missing Data, ���-1
		{
			returnData[feedBack_ite][dimIte] = util.math.general.avgValue(onlyEpisode, 0);
			dimIte++;
			returnData[feedBack_ite][dimIte] = util.math.general.stdValue(onlyEpisode, 0);
			dimIte++;
			returnData[feedBack_ite][dimIte] = util.math.general.avgValue(onlyEpisode_speed);
			dimIte++;
			returnData[feedBack_ite][dimIte] = util.math.general.stdValue(onlyEpisode_speed);
			dimIte++;
			returnData[feedBack_ite][dimIte] = onlyEpisode.size();			
		}
		else{
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
			dimIte++;
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
			dimIte++;
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
			dimIte++;
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
			dimIte++;
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
			dimIte++;
			returnData[feedBack_ite][dimIte] = symbol_MissingData;
		}
			
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
