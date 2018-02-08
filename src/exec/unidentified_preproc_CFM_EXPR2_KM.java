package exec;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import arrayOp.Array2DAppend;

import preproc.unidentified_preproc_manager_CFM_EXPR2_data;
import fsOp.FileTraversal_manager;
import fsOp.fileRead;

public class unidentified_preproc_CFM_EXPR2_KM {
	public static void main(String[] args) throws IOException {

		//ArrayList Feedback = fileRead.get2D_arraylist_string_from_file("C:/DBoxs/prim/Dropbox/特設-校務大小事/VBM Lab/201202-04 [CFM] EXPR/_實驗籌備與設計/20120301 WH前測settings/data/result/Male-TP-23.txt","\t");
		
		final String inputDir = "C:/DBoxs/prim/Dropbox/特設-校務大小事/VBM Lab/201202-04 [CFM] EXPR/_實驗籌備與設計/20120301 WH前測settings/data/_preproc/rawData";
		//final String inputDir = "./raw data";
		
		int interval_sec = 25;
		if (args.length > 0)interval_sec = Integer.parseInt(args[0]);
		
		final int featureNum = 7;
		final int feedback_max =100; //最多取幾個feedback, 設-1表示max for all subjects
		final double symbol_MissingData = -10.0;
		
		FileTraversal_manager fm = new FileTraversal_manager();						
        Hashtable dir_tab = fm.traverse_and_build_two_level_dir
        (new File(inputDir));
        
        Enumeration e = dir_tab.keys();
        unidentified_preproc_manager_CFM_EXPR2_data pm = new unidentified_preproc_manager_CFM_EXPR2_data();
        
        
                
        double[][] extData = null; 		
		
		int subjectNum = 0;
		String subjectName;
        while( e.hasMoreElements() ){
        	subjectNum++;
        	subjectName = (String) e.nextElement();
        	System.out.println("Subject: " + String.valueOf(subjectNum) + "," + subjectName);
        	
        	if (subjectNum == 1)
        		extData = pm.feature_extract_CFM_EXPR2_single_subject
                    	((ArrayList) dir_tab.get(subjectName), interval_sec, 
                    			(2+featureNum), feedback_max, symbol_MissingData);
        	else
        		extData = Array2DAppend.append_dblArray(extData,
        				pm.feature_extract_CFM_EXPR2_single_subject
                	((ArrayList) dir_tab.get(subjectName), interval_sec, 
                			(2+featureNum), feedback_max, symbol_MissingData));        	    	 	    		           
        }		
        
        //pm.missingData_proc_backward(extData, symbol_MissingData);
        //extData = pm.subjectid_add(extData, subjectNum, feedback_max);
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
