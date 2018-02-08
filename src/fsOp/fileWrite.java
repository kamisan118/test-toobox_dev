package fsOp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class fileWrite {
	
	//[Usage] 2013/01/10 14:00 by PM
	public static void write_2d_arraylist_string2file(String path, ArrayList data, boolean isNewFile)
	{
		write_2d_arraylist_string2file(path, "", data, isNewFile);
	}
	
	public static void write_2d_arraylist_string2file(String path, ArrayList data, boolean isNewFile, String delimeter)
	{
		write_2d_arraylist_string2file(path, "", data, isNewFile, delimeter);
	}
	
	//header�Ц۳�"\n"
		public static void write_2d_arraylist_string2file(String path, String header, ArrayList data, boolean isNewFile, String delimeter)
		{
			
			ArrayList al_col_tmp;
			
			try
			{    
	        	//System.out.println("---Writting to File---");
				FileWriter writer = new FileWriter(path, (!isNewFile));
				
				//header			    				
				writer.append(header);			
				writer.flush();
			
				for (int i=0; i < data.size(); i+=1){
					al_col_tmp = (ArrayList) data.get(i);
					writer.append((String)al_col_tmp.get(0));
					for (int j=1; j < al_col_tmp.size(); j+=1){
						writer.append(delimeter);	
						writer.append((String)al_col_tmp.get(j));    										
					}
					writer.append("\n");
				    writer.flush();
				}    						
			    writer.close();	
			}catch(Exception e){System.out.println("Mistake in I/O: "+e);}

			
			
		}
	
	
	//header�Ц۳�"\n"
	public static void write_2d_arraylist_string2file(String path, String header, ArrayList data, boolean isNewFile)
	{
		
		ArrayList al_col_tmp;
		
		try
		{    
        	//System.out.println("---Writting to File---");
			FileWriter writer = new FileWriter(path, (!isNewFile));
			
			//header			    				
			writer.append(header);			
			writer.flush();
		
			for (int i=0; i < data.size(); i+=1){
				al_col_tmp = (ArrayList) data.get(i);
				writer.append((String)al_col_tmp.get(0));
				for (int j=1; j < al_col_tmp.size(); j+=1){
					writer.append(",");	
					writer.append((String)al_col_tmp.get(j));    										
				}
				writer.append("\n");
			    writer.flush();
			}    						
		    writer.close();	
		}catch(Exception e){System.out.println("Mistake in I/O: "+e);}

		
		
	}
	
	
	
}
