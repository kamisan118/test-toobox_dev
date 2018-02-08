package exec;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import arrayOp.Array2DAppend;

import preproc.preproc_manager_KM_C04data;
import fsOp.FileTraversal_manager;

public class preproc_C04 {
	public static void main(String[] args) throws IOException {

		//final String inputDir = "D:/All_User/pmli/My Documents/DataBase/BenX_DataBase_snapshot_20120111/201201 ��L�P�ƹ������(16)/raw data";
		final String inputDir = "D:/ALL_Users/pmli/workspace/toobox_dev/raw_data";//"./raw data";
		
		int interval_min = 3;
		if (args.length > 0)interval_min = Integer.parseInt(args[0]);
		
		final int featureNum = 5;
		final int feedback_max =-1; //�̦h��X��feedback, �]-1���max for all subjects
		final double symbol_MissingData = -10.0;
		
		FileTraversal_manager fm = new FileTraversal_manager();						
        Hashtable dir_tab = fm.traverse_and_build_two_level_dir
        (new File(inputDir));
        
        Enumeration e = dir_tab.keys();
        preproc_manager_KM_C04data pm = new preproc_manager_KM_C04data();
        
        
                
        double[][] extData = null; 		
		
		int subjectNum = 0;
		String subjectName;
        while( e.hasMoreElements() ){
        	subjectNum++;
        	subjectName = (String) e.nextElement();
        	System.out.println("Subject: " + String.valueOf(subjectNum) + "," + subjectName);
        	
        	if (subjectNum == 1)
        		extData = pm.feature_extract_c04_single_subject
                    	((ArrayList) dir_tab.get(subjectName), interval_min, 
                    			(2+featureNum), feedback_max, symbol_MissingData);
        	else
        		extData = Array2DAppend.append_dblArray(extData,
        				pm.feature_extract_c04_single_subject
                	((ArrayList) dir_tab.get(subjectName), interval_min, 
                			(2+featureNum), feedback_max, symbol_MissingData));        	    	 	    		           
        }		
        
        
        
        //pm.missingData_proc_backward(extData, symbol_MissingData);
        extData = pm.subjectid_add(extData, subjectNum, feedback_max);	//if feedback_max = -1, then no use of this function.
        //extData = pm.subjectid_add_dummy(extData, subjectNum, feedback_max);
    	//extData = pm.missingData_eliminate(extData, symbol_MissingData);
    	
		
        try
		{    
        	System.out.println("---Writting to File---");
			FileWriter writer = new FileWriter("preproc_output.csv", false);

			//header
			writer.append(String.valueOf(extData[0].length-2));    					
			writer.append(",");
			writer.append(String.valueOf(2));
			writer.append(",");
			writer.append(String.valueOf(extData.length));    					
			writer.append(",");
			writer.append("\n");
			writer.flush();
		
			for (int i=0; i < extData.length; i+=1){
				
				for (int j=0; j < extData[0].length; j+=1){
					writer.append(String.valueOf(extData[i][j]));    					
					writer.append(",");	
				}
				writer.append("\n");
			    writer.flush();
			}    						
		    writer.close();	
		}catch(Exception e1){System.out.println("Mistake in I/O of toolbox.outputPop: "+e1);}
		
		System.out.println("---Done---");
		
	}
}
