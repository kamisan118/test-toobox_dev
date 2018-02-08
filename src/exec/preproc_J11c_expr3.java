package exec;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import arrayOp.Array2DAppend;

import preproc.preproc_manager_KM_C04data;
import preproc.proc_manager_KM_C04data_wo_Mouse;
import fsOp.FileTraversal_manager;

public class preproc_J11c_expr3 {
	public static void main(String[] args) throws IOException {
		
		final String inputDir = "D:/ALL_Users/pmli/workspace/toobox_dev/raw_data";//"./raw data";
		
		final int interval_min = 10;
		final int featureNum = 5;
		final int feedback_max = 50; //�̦h��X��feedback, �]-1���max for all subjects
		final double symbol_MissingData = -99.99;
		
		FileTraversal_manager fm = new FileTraversal_manager();						
        Hashtable dir_tab = fm.traverse_and_build_two_level_dir(new File(inputDir));
        
        Enumeration e = dir_tab.keys();
        
        
        proc_manager_KM_C04data_wo_Mouse pm = new proc_manager_KM_C04data_wo_Mouse();        
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
