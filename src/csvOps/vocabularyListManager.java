package csvOps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import util.RuntimeProgExec;

public class vocabularyListManager {
	//parameters setting	
	static final String in_path = "voc.csv";
	static final String out_path = "voc_new.csv";
	
	
	public static void main(String[] args)  {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("voc_len_limit_l?");
		int voc_len_limit_l = sc.nextInt();		
		System.out.println("voc_len_limit_h?");		
		int voc_len_limit_h = sc.nextInt();		
		
		
		
		ArrayList voc_col = new ArrayList();
		ArrayList voc_row;
		String tmp01;
		
		//File Read
		try
	    {	             	             
	              //create BufferedReader to read csv file
	              BufferedReader br = new BufferedReader( new FileReader(in_path));
	              String strLine = "";
	              String last_strLine = "";
	              	              
	              while( (strLine = br.readLine()) != null){
	            	  if (strLine.equals(last_strLine)){
	            		  System.out.println("voc. repeated: " + strLine);
	            		  continue;	   //(1) eliminate重複者         	  
	            	  }
	            	  else if((strLine.length() < voc_len_limit_l) ||  //(2) eliminate太長或太短者
	            			  (strLine.length() > voc_len_limit_h)){
	            		  System.out.println("voc. too long/short: " + strLine);
	            		  continue;	            		  
	            	  }
	            	  
	            	  voc_row = new ArrayList();
	            	  voc_row.add(0, strLine);
	            	  
	            	  tmp01 = (RuntimeProgExec.runProgGetOutput("ydict " + strLine))[0];
	            	  if (tmp01.split("\n").length < 4){
	            		  System.out.println("no explaination can be fouf for voc.: " + strLine);
	            		  System.out.println("retrived info.: " + tmp01);
	            		  continue; 
	            	  }
	            	  
	            	  tmp01 = tmp01.split("\n")[3].split(";")[0].substring(12).replaceAll("\\[.*", ""); // 特殊處理
	            	  tmp01 = tmp01.replaceAll(".*】", ""); // 特殊處理2
	            	  voc_row.add(1, tmp01);	            	  
	            	  voc_col.add(voc_row);
	            	  
	            	  last_strLine = strLine;
	              }	      
	              
		    }
	    catch(Exception e)
	    {
	              System.out.println("Exception while reading csv file: " + e);                  
	    }
	    
	    
	    //Print Out
	    for(int i = 0; i < voc_col.size(); i++){
	    	voc_row = (ArrayList) voc_col.get(i);
	    	for(int j = 0; j < voc_row.size(); j++){
		    	System.out.print(voc_row.get(j));
		    	if ((j+1)!= voc_row.size())
		    		System.out.print(";");		
	    	}
	    	System.out.println("");

	    }
	    System.out.println("----PrintOut共" + String.valueOf(voc_col.size())+"字----");
	    	
	    
	    

	  //File Write
	    try
		{    
        	System.out.println("---Writting to File---");
        	
        	
        	String encoding = "BIG5";
            File textFile = new File(out_path);
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(textFile), encoding);            
			//FileWriter writer = new FileWriter(out_path, false);
			
			//header
			/*writer.append(String.valueOf(extData[0].length-2));    					
			writer.append(",");
			writer.append(String.valueOf(2));
			writer.append(",");
			writer.append(String.valueOf(extData.length));    					
			writer.append(",");
			writer.append("\n");
			writer.flush();*/
		
		    for(int i = 0; i < voc_col.size(); i++){
		    	voc_row = (ArrayList) voc_col.get(i);
	    			    
		    	for(int j = 0; j < voc_row.size(); j++){		    		
		    		writer.append((String)voc_row.get(j));
		    		if ((j+1)!= voc_row.size())
		    			writer.append(";");			    				    		
		    	}
		    	writer.append("\r\n"); //M$ new line = "\r\n" where UNIX new line "\n" only.
			    writer.flush();			    
		    }
						
		    writer.close();	
		}catch(Exception e1){System.out.println("Mistake in I/O of toolbox.outputPop: "+e1);}		
	    
		System.out.println("---Done---");
		
	}
	
	
	
	
	
}
