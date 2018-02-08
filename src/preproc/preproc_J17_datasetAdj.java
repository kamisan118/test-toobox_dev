package preproc;

import java.util.ArrayList;

import fsOp.fileRead;
import fsOp.fileWrite;

public class preproc_J17_datasetAdj {

	/**
	 * @param args
	 */
	
	
	public static void funct1_within_subject() {
		ArrayList in2D_AL = fileRead.get2D_arraylist_string_from_file("datasetAdj_before.csv");
		ArrayList out2D_AL = new ArrayList(); 
		
		
		ArrayList out2D_AL_head = new ArrayList();
		out2D_AL_head.add("subject");
		out2D_AL_head.add("valence");
		out2D_AL_head.add("arousal");
		out2D_AL_head.add("voice");
		out2D_AL_head.add("feature_id");
		out2D_AL_head.add("value");
		out2D_AL.add(out2D_AL_head);
		
		for(int i = 1; i < in2D_AL.size(); i++){
			
			
			String subject = (String)((ArrayList)in2D_AL.get(i)).get(0);
			String valence = (String)((ArrayList)in2D_AL.get(i)).get(1);
			String arousal = (String)((ArrayList)in2D_AL.get(i)).get(2);
			String voice = (String)((ArrayList)in2D_AL.get(i)).get(3);
			
						
			for(int j = 4; j < ((ArrayList)in2D_AL.get(0)).size(); j++){
				ArrayList out2D_AL_row = new ArrayList();
				out2D_AL_row.add(subject);		//subject
				out2D_AL_row.add(valence);		//valence
				out2D_AL_row.add(arousal);		//arousal
				out2D_AL_row.add(voice);		//voice
				out2D_AL_row.add(String.valueOf(j-3));	//feature_id
				out2D_AL_row.add(((ArrayList)in2D_AL.get(i)).get(j)); //value				
				out2D_AL.add(out2D_AL_row);
			}
			
			
			
			
		}
		
		fileWrite.write_2d_arraylist_string2file("datasetAdj_after.csv", out2D_AL, true); // no append
	}
	
	
	
	public static void funct1() {
		ArrayList in2D_AL = fileRead.get2D_arraylist_string_from_file("datasetAdj_before.csv");
		ArrayList out2D_AL = new ArrayList(); 
		
		
		for(int i = 0; i < in2D_AL.size(); i++){
			
			
			String valence = (String)((ArrayList)in2D_AL.get(i)).get(0);
			String arousal = (String)((ArrayList)in2D_AL.get(i)).get(1);
			
						
			for(int j = 2; j < ((ArrayList)in2D_AL.get(0)).size(); j++){
				ArrayList out2D_AL_row = new ArrayList();
				out2D_AL_row.add(valence);
				out2D_AL_row.add(arousal);
				out2D_AL_row.add(String.valueOf(j-1));
				out2D_AL_row.add(((ArrayList)in2D_AL.get(i)).get(j));				
				out2D_AL.add(out2D_AL_row);
			}
			
			
			
			
		}
		
		fileWrite.write_2d_arraylist_string2file("datasetAdj_after.csv", out2D_AL, true); // no append
	}
	
	
	public static void funct2() {
		
		ArrayList in2D_AL = fileRead.get2D_arraylist_string_from_file("C:/Users/pmli/Desktop/_delete/data1.csv");
		ArrayList out2D_AL = new ArrayList(); 
		
		
		String group1 = "";
		
		ArrayList out2D_AL_row = null;
		String group2s = null;
		for(int i = 0; i < in2D_AL.size(); i++){
			
			
			if(!group1.equals((String)((ArrayList)in2D_AL.get(i)).get(0))){
				if(!group1.equals("")){
					out2D_AL_row.add(group2s);
					out2D_AL.add(out2D_AL_row);					
				}
				
				group1 = (String)((ArrayList)in2D_AL.get(i)).get(0);							
				out2D_AL_row = new ArrayList();
				out2D_AL_row.add(group1);
				group2s = "";
			}
			
			if(((String)((ArrayList)in2D_AL.get(i)).get(2)).equals("1"))
			{
				if (group2s.length() == 0)
					group2s = ((String)((ArrayList)in2D_AL.get(i)).get(1));
				else
					group2s = group2s + "_" + ((String)((ArrayList)in2D_AL.get(i)).get(1));			
			}
		}
		
		
		fileWrite.write_2d_arraylist_string2file("C:/Users/pmli/Desktop/_delete/data1after.csv", out2D_AL, true); // no append
	}
	
	
	public static void funct3() {
		
		ArrayList in2D_AL = fileRead.get2D_arraylist_string_from_file("C:/Users/pmli/Desktop/_delete/data1.csv");
		ArrayList out2D_AL = new ArrayList(); 
		
		
		String group1 = "";
		String group2 = "";
		String g1larger = "";
		
		ArrayList out2D_AL_row = null;
		
		for(int i = 0; i < in2D_AL.size(); i++){
						
			
			if(((String)((ArrayList)in2D_AL.get(i)).get(2)).equals("1"))
			{
				out2D_AL_row = new ArrayList();
				group1 = ((String)((ArrayList)in2D_AL.get(i)).get(0));
				group2 = ((String)((ArrayList)in2D_AL.get(i)).get(1));
				g1larger = ((String)((ArrayList)in2D_AL.get(i)).get(3));
				out2D_AL_row.add(group1);
				out2D_AL_row.add(group2);
				out2D_AL_row.add(g1larger);
				out2D_AL.add(out2D_AL_row);				
			}
		}
		
		ArrayList pairs = out2D_AL;
		out2D_AL = new ArrayList();
		
		for(int i = 0; i < 153; i++){
			out2D_AL_row = new ArrayList();
			out2D_AL_row.add(String.valueOf(i+1));
			out2D_AL_row.add("");
			out2D_AL_row.add("");
			out2D_AL.add(out2D_AL_row);
		}
		
		
		for(int i = 0; i < pairs.size(); i++){
			group1 = ((String)((ArrayList)pairs.get(i)).get(0));
			group2 = ((String)((ArrayList)pairs.get(i)).get(1));
			g1larger = ((String)((ArrayList)pairs.get(i)).get(2));
			
			String group1_largerthan_list = ((String)((ArrayList)out2D_AL.get(Integer.parseInt(group1)-1)).get(1));
			String group1_lowerthan_list = ((String)((ArrayList)out2D_AL.get(Integer.parseInt(group1)-1)).get(2));
			String group2_largerthan_list = ((String)((ArrayList)out2D_AL.get(Integer.parseInt(group2)-1)).get(1));
			String group2_lowerthan_list = ((String)((ArrayList)out2D_AL.get(Integer.parseInt(group2)-1)).get(2));
			
			if(g1larger.equals("1")){
				if (group1_largerthan_list.length() == 0)
					group1_largerthan_list = group1_largerthan_list + group2;
				else
					group1_largerthan_list = group1_largerthan_list + "_" + group2;
				
				((ArrayList)out2D_AL.get(Integer.parseInt(group1)-1)).set(1, group1_largerthan_list);
				
				if (group2_lowerthan_list.length() == 0)
				group2_lowerthan_list = group2_lowerthan_list + group1;
				else
					group2_lowerthan_list = group2_lowerthan_list + "_" + group1;					
							
				((ArrayList)out2D_AL.get(Integer.parseInt(group2)-1)).set(2, group2_lowerthan_list);				
			}
			else{
				
				if (group1_lowerthan_list.length() == 0)
					group1_lowerthan_list = group1_lowerthan_list + group2;
				else
					group1_lowerthan_list = group1_lowerthan_list + "_" + group2;					
								
				((ArrayList)out2D_AL.get(Integer.parseInt(group1)-1)).set(2, group1_lowerthan_list);
				
				if (group2_largerthan_list.length() == 0)
					group2_largerthan_list = group2_largerthan_list + group1;
				else
					group2_largerthan_list = group2_largerthan_list + "_" + group1;				
				
				((ArrayList)out2D_AL.get(Integer.parseInt(group2)-1)).set(1, group2_largerthan_list);				
			}		
		}
		
//		for(int i = 0; i < out2D_AL.size(); i++){
//			String group1_largerthan_list = ((String)((ArrayList)out2D_AL.get(Integer.parseInt(group1)-1)).get(1));
//			String group1_lowerthan_list = ((String)((ArrayList)out2D_AL.get(Integer.parseInt(group1)-1)).get(2));
//			String group2_largerthan_list = ((String)((ArrayList)out2D_AL.get(Integer.parseInt(group2)-1)).get(1));
//			String group2_lowerthan_list = ((String)((ArrayList)out2D_AL.get(Integer.parseInt(group2)-1)).get(2));
//			
//			group1_largerthan_list = group1_largerthan_list.substring(1);
//			group1_lowerthan_list = group1_lowerthan_list.substring(1);
//			group2_largerthan_list = group2_largerthan_list.substring(1);
//			group2_lowerthan_list = group2_lowerthan_list.substring(1);
//			
//			((ArrayList)out2D_AL.get(Integer.parseInt(group1)-1)).set(1, group1_largerthan_list);			
//			((ArrayList)out2D_AL.get(Integer.parseInt(group1)-1)).set(2, group1_lowerthan_list);
//			((ArrayList)out2D_AL.get(Integer.parseInt(group2)-1)).set(1, group2_largerthan_list);
//			((ArrayList)out2D_AL.get(Integer.parseInt(group2)-1)).set(2, group2_lowerthan_list);
//				
//		}
		
		
		fileWrite.write_2d_arraylist_string2file("C:/Users/pmli/Desktop/_delete/data1after.csv", out2D_AL, true); // no append
	}
	

	public static String searchForGroup2(String [] largerThanList, String [] lowerThanList, String group2) {
		
		for(int i = 0; i <largerThanList.length; i++){
			if (largerThanList[i].equals(group2))return "1";
		}
		
		for(int i = 0; i <lowerThanList.length; i++){
			if (lowerThanList[i].equals(group2))return "2";
		}
		
		return "0";
		//return 0(沒找到), 1(groupA larger than groupB), 2(groupA lower than groupB) 
	}
	
	public static void funct4() {
		
		ArrayList in2D_AL = fileRead.get2D_arraylist_string_from_file("C:/Users/pmli/Desktop/_delete/data1after.csv");
		ArrayList out2D_AL = new ArrayList(); 
		
		for(int i = 0; i < in2D_AL.size(); i++){
			ArrayList in2D_AL_row = (ArrayList)in2D_AL.get(i);
			ArrayList out2D_AL_row = new ArrayList();
			
			out2D_AL_row.add(in2D_AL_row.get(0));
			
			String [] largerThanList = ((String)in2D_AL_row.get(1)).split("_");
			String [] lowerThanList = ((String)in2D_AL_row.get(2)).split("_");
			
			for(int j = 0; j < 153; j++){				
				out2D_AL_row.add(searchForGroup2(largerThanList,lowerThanList,String.valueOf(j+1)));				
			}
			
			out2D_AL.add(out2D_AL_row);
		}
		
		
		fileWrite.write_2d_arraylist_string2file("C:/Users/pmli/Desktop/_delete/data1after2.csv", out2D_AL, true); // no append

	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//funct1();
		//funct2();
		//funct3();
		//funct4();
		
		funct1_within_subject();
		
		
		
	}

}
