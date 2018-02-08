package preproc.J16;

import java.util.ArrayList;

import fsOp.fileRead;
import fsOp.fileWrite;

public class preproc_100B_CFM_EXPR2_notfinished {

	
	public static void firstStep_getKeystrokeMix(){
		//building "key_id,key,d_tick,u_tick"
				ArrayList keystrokeAL = fileRead.get2D_arraylist_string_from_file("E:/_DataBase/201203 100B CFM_EXPR2 (24+7)/建表s from raw data/keystroke.csv", ",");
				ArrayList keyUD_AL = fileRead.get2D_arraylist_string_from_file("E:/_DataBase/201203 100B CFM_EXPR2 (24+7)/建表s from raw data/keyUD.csv", ",");
				
				ArrayList keystrokeMix_asTPformat = new ArrayList();
				
				
				
				for(int i=0; i<keystrokeAL.size();i++){
					ArrayList keystrokeMix_asTPformat_row = new ArrayList();
					
					ArrayList keystrokeAL_row = (ArrayList)keystrokeAL.get(i);
					String current_key_id = ((String)keystrokeAL_row.get(0));						
					String d_tick = (String)keystrokeAL_row.get(2);
					String currentkey = ((String)keystrokeAL_row.get(3)).substring(((String)keystrokeAL_row.get(3)).length()-1);
								
					keystrokeMix_asTPformat_row.add(current_key_id);
					keystrokeMix_asTPformat_row.add(currentkey);
					keystrokeMix_asTPformat_row.add(d_tick);
								
					
					//locate: (key, D)
					int keyUD_AL_ite = 0;
					while(!((String)((ArrayList)keyUD_AL.get(keyUD_AL_ite)).get(2)).equals(d_tick)){
						keyUD_AL_ite++;
					}
					 
										
					int cDowns = 0;			
					int backward_steps = 3;
					while((keyUD_AL_ite != 0) && (backward_steps != 0)){
						if(!((String)((ArrayList)keyUD_AL.get(keyUD_AL_ite - (4 - backward_steps))).get(3)).equals("0")){
							break;
						}
						cDowns++;
						backward_steps--;
					}
					
					
					while((keyUD_AL_ite < keyUD_AL.size())){
						if(((String)((ArrayList)keyUD_AL.get(keyUD_AL_ite)).get(3)).equals("1")){
							if(cDowns == 0)
								break;
							else{
								cDowns--;
							}
						}								
						keyUD_AL_ite++;
					}			
					
					if(keyUD_AL_ite == keyUD_AL.size()){
						System.out.println("Invalid Case in the detection of KeyStrokeMix");
						keystrokeMix_asTPformat_row.add("N/A");
					}				
					else{
						String u_tick = (String)((ArrayList)keyUD_AL.get(keyUD_AL_ite)).get(2);
						keystrokeMix_asTPformat_row.add(u_tick);
					}
					
					keystrokeMix_asTPformat.add(keystrokeMix_asTPformat_row);
					
				}
				
				
				fileWrite.write_2d_arraylist_string2file("keystrokeMix_asTPformat.csv", keystrokeMix_asTPformat, true); // no append
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//firstStep_getKeystrokeMix();
		String targetSeq = "24357980";
		
		ArrayList keystrokeMix_asTPformat_AL = fileRead.get2D_arraylist_string_from_file("keystrokeMix_asTPformat.csv", ",");
		ArrayList pic_results_w_isCorrect_AL = fileRead.get2D_arraylist_string_from_file("E:/_DataBase/201203 100B CFM_EXPR2 (24+7)/建表s from raw data/pic_results.csv", ",");
		
		int keystrokeMix_asTPformat_AL_ite;
		for(int i = 0; i < pic_results_w_isCorrect_AL.size(); i++){
			ArrayList pic_results_w_isCorrect_AL_row = (ArrayList)pic_results_w_isCorrect_AL.get(i);
			String key_id = (String)pic_results_w_isCorrect_AL_row.get(0);
			
			keystrokeMix_asTPformat_AL_ite = 0;
			
			while(!((String)((ArrayList)keystrokeMix_asTPformat_AL.get(keystrokeMix_asTPformat_AL_ite)).get(0)).equals(key_id)){
				keystrokeMix_asTPformat_AL_ite++;
				
				if(keystrokeMix_asTPformat_AL_ite == keystrokeMix_asTPformat_AL.size()){
					System.out.println("Can't find the sequence with key_id: " + key_id);
					break;
				}					
			}
			
			String isCorrect = "N/A";
			if(keystrokeMix_asTPformat_AL_ite == keystrokeMix_asTPformat_AL.size()){
				isCorrect = "N/A";
			}
			else{
				String currentSeq = "";
				while(((String)((ArrayList)keystrokeMix_asTPformat_AL.get(keystrokeMix_asTPformat_AL_ite)).get(0)).equals(key_id)){
					String currentKey = ((String)((ArrayList)keystrokeMix_asTPformat_AL.get(keystrokeMix_asTPformat_AL_ite)).get(1));
					currentSeq = currentSeq.concat(currentKey);				
					keystrokeMix_asTPformat_AL_ite++;
				}
				isCorrect = "0";
				if(currentSeq.equals(targetSeq)){
					isCorrect = "1";
				}	
			}

			pic_results_w_isCorrect_AL_row.add(isCorrect);
			
		}
		
		
		fileWrite.write_2d_arraylist_string2file("pic_results_w_isCorrect.csv", pic_results_w_isCorrect_AL, true); // no append
		
		
	}

}
