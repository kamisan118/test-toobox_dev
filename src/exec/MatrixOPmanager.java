package exec;

import java.util.ArrayList;
import java.util.Hashtable;

import fsOp.fileRead;
import fsOp.fileWrite;

public class MatrixOPmanager {
	
	public static ArrayList avgBaseOnLabels(ArrayList in2D_AL) {
		
		ArrayList in2D_AL_header = (ArrayList)in2D_AL.get(0);
		
		ArrayList out2D_AL = new ArrayList(); 
		out2D_AL.add(in2D_AL_header);
		
		Hashtable hl = new Hashtable();
		for(int i = 1; i < in2D_AL.size(); i++){
			
			String hashindex = (String)((ArrayList)in2D_AL.get(i)).get(0) + "_" + (String)((ArrayList)in2D_AL.get(i)).get(1) 
					 + "_" + (String)((ArrayList)in2D_AL.get(i)).get(2);
			
			if(!hl.containsKey(hashindex)){
				ArrayList newList = new ArrayList();
				newList.add(((ArrayList)in2D_AL.get(i)).get(3));
				hl.put(hashindex, newList);
			}
			else{
				ArrayList newList = (ArrayList) hl.get(hashindex);
				newList.add(((ArrayList)in2D_AL.get(i)).get(3));
			}						
		}
		
		//算平均s
		for (Object key : hl.keySet()) {
		    //System.out.println((String)key + ":" + hl.get(key));
			//System.out.println((String)key + ":" + util.math.general.avgValue((ArrayList)hl.get(key)));
			
			ArrayList out2D_AL_row = new ArrayList();
			String [] labels = ((String)key).split("_");
			out2D_AL_row.add(labels[0]);
			out2D_AL_row.add(labels[1]);
			out2D_AL_row.add(labels[2]);
			out2D_AL_row.add(String.valueOf(util.math.general.avgValue((ArrayList)hl.get(key))));
			
			out2D_AL.add(out2D_AL_row);
		}
				
		return out2D_AL;		
	}
	
	
	public static ArrayList MissingDataInsertAvg(ArrayList in2D_AL) {
		
		ArrayList in2D_AL_header = (ArrayList)in2D_AL.get(0);
		
		ArrayList out2D_AL = (ArrayList) in2D_AL.clone(); 
		
		
		ArrayList maxLabelNumbers = maxLabelIdCheck(in2D_AL);
		
		
		Hashtable hl = new Hashtable();
		for(int i = 1; i < in2D_AL.size(); i++){
			
			String hashindex = (String)((ArrayList)in2D_AL.get(i)).get(0) + "_" + (String)((ArrayList)in2D_AL.get(i)).get(1) 
					 + "_" + (String)((ArrayList)in2D_AL.get(i)).get(2);
			
			if(!hl.containsKey(hashindex)){
				ArrayList newList = new ArrayList();
				newList.add(((ArrayList)in2D_AL.get(i)).get(3));
				hl.put(hashindex, newList);
			}
			else{
				ArrayList newList = (ArrayList) hl.get(hashindex);
				newList.add(((ArrayList)in2D_AL.get(i)).get(3));
			}						
		}
		
		//subject的avgs. -- subject必須是col1
		Hashtable hl2 = new Hashtable();
		for(int i = 1; i < in2D_AL.size(); i++){
			
			String hashindex = (String)((ArrayList)in2D_AL.get(i)).get(0);
			
			if(!hl2.containsKey(hashindex)){
				ArrayList newList = new ArrayList();
				newList.add(((ArrayList)in2D_AL.get(i)).get(3));
				hl2.put(hashindex, newList);
			}
			else{
				ArrayList newList = (ArrayList) hl2.get(hashindex);
				newList.add(((ArrayList)in2D_AL.get(i)).get(3));
			}						
		}
		Hashtable subjectAvgs = new Hashtable();
		for (Object key : hl2.keySet()) {		    
			subjectAvgs.put((String)key, String.valueOf(util.math.general.avgValue((ArrayList)hl2.get(key))));
		}
		
		
		//暴力法塞Missing Data rows
		//maxLabelNumbers
		for(int i = 1; i <= Integer.parseInt((String)maxLabelNumbers.get(0)); i++){
			for(int j = 1; j <= Integer.parseInt((String)maxLabelNumbers.get(1)); j++){
				for(int k = 1; k <= Integer.parseInt((String)maxLabelNumbers.get(2)); k++){
					
					String checkingKey = String.valueOf(i) + "_" + String.valueOf(j) 
							 + "_" + String.valueOf(k);
					
					if(!hl.containsKey(checkingKey)){
						//System.out.println("Missing Data Found in: " + checkingKey);
						
						ArrayList out2D_AL_row = new ArrayList();
						String [] labels = checkingKey.split("_");
						out2D_AL_row.add(labels[0]);
						out2D_AL_row.add(labels[1]);
						out2D_AL_row.add(labels[2]);
						out2D_AL_row.add(String.valueOf(subjectAvgs.get(labels[0])));
						
						out2D_AL.add(out2D_AL_row);
						//System.out.println("Missing Data inserted to: " + checkingKey);
					}
					
					
				}
			}			
		}
		
		return out2D_AL;
	}
	
	
	public static ArrayList maxLabelIdCheck(ArrayList in2D_AL) {
		
		ArrayList maxLabelNumbers = new ArrayList();
		maxLabelNumbers.add("0");
		maxLabelNumbers.add("0");
		maxLabelNumbers.add("0");
		for(int i = 1; i < in2D_AL.size(); i++){
			
			for(int j = 0; j < 3; j++){
				if (Integer.parseInt((String) maxLabelNumbers.get(j)) < Integer.parseInt((String)((ArrayList)in2D_AL.get(i)).get(j)))
					maxLabelNumbers.set(j, (String)((ArrayList)in2D_AL.get(i)).get(j));
			}
									
		}
		
		System.out.println("maxLabelNumbers: " + maxLabelNumbers);
		return maxLabelNumbers;
	}
	
	
	public static ArrayList cellSwitch(ArrayList in1D_AL, int col1, int col2) {
		ArrayList in1D_ALc = (ArrayList)in1D_AL.clone();
		in1D_ALc.set(col1, in1D_AL.get(col2));
		in1D_ALc.set(col2, in1D_AL.get(col1));
		
		return in1D_ALc;
	}
	
	public static void colSwitch(ArrayList in2D_AL, int col1, int col2) {
		for(int i = 0; i < in2D_AL.size(); i++){
			ArrayList in2D_AL_row = (ArrayList)in2D_AL.get(i);
			in2D_AL.set(i, cellSwitch(in2D_AL_row,col1,col2));
		}				
	}
	
	public static boolean onTheList(ArrayList list, String element){
		for(int i = 0; i < list.size(); i++){
			if(((String)list.get(i)).equals(element))return true;
		}
		return false;
	}
	
	//remove subjects and rearrange the ids
	public static void removeSubject(ArrayList in2D_AL, ArrayList subject_list) {
		
		//remove subjects
		int i = 1;
		while(i < in2D_AL.size()){
			ArrayList in2D_AL_row = (ArrayList)in2D_AL.get(i);
			
			if(subject_list.contains((String)in2D_AL_row.get(0))){
			//if(onTheList(subject_list,(String)in2D_AL_row.get(0))){
				in2D_AL.remove(i);
				continue; //do not i++!
			}
						
			i++;
		}			
		
		//fileWrite.write_2d_arraylist_string2file("input_removeSubjects.csv", in2D_AL, true); // no append
		
		//rearrange the ids		
		int subjectIDused = 0;		
		i = 1;
		Hashtable subjectIDused_mapping = new Hashtable();
		while(i < in2D_AL.size()){
			ArrayList in2D_AL_row = (ArrayList)in2D_AL.get(i);
			
			if(!subjectIDused_mapping.containsKey(((String)in2D_AL_row.get(0)))){
				subjectIDused++;				
				subjectIDused_mapping.put(((String)in2D_AL_row.get(0)), String.valueOf(subjectIDused));
				in2D_AL_row.set(0, String.valueOf(subjectIDused));
			}
			else{				
				in2D_AL_row.set(0, subjectIDused_mapping.get(((String)in2D_AL_row.get(0))));				
			}
			
								
			i++;
		}
		
		//fileWrite.write_2d_arraylist_string2file("input_removeSubjects_adjed.csv", in2D_AL, true); // no append
		
		
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList in2D_AL = fileRead.get2D_arraylist_string_from_file("input.csv");
		ArrayList out2D_AL;
		
		ArrayList subject_remove_list = new ArrayList();	
		subject_remove_list.add("30");//6
		subject_remove_list.add("36");//ALL
		subject_remove_list.add("39");//6
		subject_remove_list.add("51");//6
		subject_remove_list.add("52");//5
		subject_remove_list.add("16");//4
		subject_remove_list.add("26");//4
		subject_remove_list.add("46");//4
		subject_remove_list.add("6");//3
//		subject_remove_list.add("41");//3
		subject_remove_list.add("2");//2
//		subject_remove_list.add("3");//2
//		subject_remove_list.add("24");//2
//		subject_remove_list.add("38");//2		
		subject_remove_list.add("42");//2
//		subject_remove_list.add("43");//2
//		subject_remove_list.add("44");//2
//		subject_remove_list.add("48");//2
//		subject_remove_list.add("1");//1
//		subject_remove_list.add("4");//1
//		subject_remove_list.add("8");//1
//		subject_remove_list.add("9");//1
//		subject_remove_list.add("11");//1
//		subject_remove_list.add("12");//1
//		subject_remove_list.add("14");//1
//		subject_remove_list.add("15");//1
//		subject_remove_list.add("18");//1
//		subject_remove_list.add("23");//1
//		subject_remove_list.add("25");//1
//		subject_remove_list.add("27");//1
//		subject_remove_list.add("28");//1
//		subject_remove_list.add("37");//1
//		subject_remove_list.add("45");//1
//		subject_remove_list.add("47");//1
//		subject_remove_list.add("49");//1
//		subject_remove_list.add("50");//1
		removeSubject(in2D_AL, subject_remove_list);		
		
		
		out2D_AL = MissingDataInsertAvg(in2D_AL);
		out2D_AL = avgBaseOnLabels(out2D_AL);
	
		colSwitch(out2D_AL,0,3);		
		out2D_AL.remove(0); // remove headers
		fileWrite.write_2d_arraylist_string2file("output2.csv", out2D_AL, true); // no append
		
	}

}
