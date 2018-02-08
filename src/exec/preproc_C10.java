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

public class preproc_C10 {
	public static void main(String[] args) throws IOException {

		final String inputDir = "D:/All_User/pmli/My Documents/DataBase/BenX_DataBase_snapshot_20120111/201201 鍵盤與滑鼠情緒偵測(16)/raw data";
		final int interval_min = 3;
		final int featureNum = 5;
		final int feedback_max =50; //最多取幾個feedback, 設-1表示max for all subjects
		final double symbol_MissingData = -10.0;
		
		FileTraversal_manager fm = new FileTraversal_manager();						
        Hashtable dir_tab = fm.traverse_and_build_two_level_dir
        (new File(inputDir));
        
        Enumeration e = dir_tab.keys();
        preproc_manager_KM_C04data pm = new preproc_manager_KM_C04data();
        
        
                
        String[][] extData = null; 		
		
		int subjectNum = 0;
		String subjectName;
        while( e.hasMoreElements() ){
        	subjectNum++;
        	subjectName = (String) e.nextElement();
        	System.out.println("Subject: " + String.valueOf(subjectNum) + "," + subjectName);
        	
        	extData = pm.rawData_mergeTick_c04_single_subject((ArrayList)dir_tab.get(subjectName));
        	
        	 try
     		{    
             	System.out.println("---Writting to File---");
     			FileWriter writer = new FileWriter("preproc_output_" + subjectName + ".csv", false);

     			for (int i=0; i < extData.length; i+=1){
     				writer.append(extData[i][0]);    					
 					writer.append(",");
 					writer.append(extData[i][1]);    					 					
     				writer.append("\n");
     			    writer.flush();
     			}    						
     		    writer.close();	
     		}catch(Exception e1){System.out.println("Mistake in I/O of toolbox.outputPop: "+e1);}
     		
        	        	    	 	    		           
        }		
        
		
       
		System.out.println("---Done---");
		
	}
}
