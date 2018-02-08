package fsOp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class fileRead {
	
	/*[Usage] 2012/01/12 20:15 by PM
	ArrayList dataSet = fileRead.get2D_arraylist_string_from_file("FeedBack.txt");
	System.out.print("test");*/
	public static ArrayList get2D_arraylist_string_from_file(String path){
		return get2D_arraylist_string_from_file(path,",","");
	}
	
	public static ArrayList get2D_arraylist_string_from_file(String path, String sep){
		return get2D_arraylist_string_from_file(path,sep,"");
	}

	public static ArrayList get2D_arraylist_string_from_file(String path, String sep, String CharSet){
		
		ArrayList al_col_head = new ArrayList();
		ArrayList al_col_tmp;
		
		try
	      {	             	             
	              //create BufferedReader to read csv file
	              
				//old br
				//BufferedReader br = new BufferedReader( new FileReader(path));
			
				//new br (since 2014/3/23)
				BufferedReader br;
				if(CharSet.length() == 0){
					br = new BufferedReader( new FileReader(path));					
				}
				else{
					br = new BufferedReader(
							new InputStreamReader(new FileInputStream(path), CharSet));
				}
				 
	              String strLine = "";
	              StringTokenizer st = null;                  
	                        
	              while( (strLine = br.readLine()) != null){	              
	            	  al_col_tmp = new ArrayList();
	            	  
	            	  //(2013/11/09 change becasue it doesn't recognize empty field.
	            	  /*st = new StringTokenizer(strLine, sep);
	            	  while(st.hasMoreTokens())
                      {
	            		  al_col_tmp.add(st.nextToken().trim());  
                      }*/
	            	  
	            	  
	            	  String[] tmpStrAry = strLine.split(sep, -1);
	            	  for(int i = 0; i < tmpStrAry.length; i++)
	            		  al_col_tmp.add(tmpStrAry[i].trim());	            		  
	            	  
	            	  al_col_head.add(al_col_tmp);
	              }	                
	    }
	    catch(Exception e)
	    {
	              System.out.println("Exception while reading csv file: " + e);                  
	    }
	    
		return al_col_head;
	}
	

	
	
}
