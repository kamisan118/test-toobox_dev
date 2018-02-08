package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import fsOp.fileWrite;

public class GoogleScholar_ResultConverter {

	public static void algo1_needFormat(){

		ArrayList<String> allstrs = new ArrayList();
		try{
		  // Open the file that is the first 
		  // command line parameter
		  FileInputStream fstream = new FileInputStream("inputFile.txt");
		  // Get the object of DataInputStream
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;
		  //Read File Line By Line
		  while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console
		  //System.out.println (strLine);
			  
			  allstrs.add(strLine);
			  
			  
		  }
		  //Close the input stream
		  in.close();
		  }catch (Exception e){//Catch exception if any
		  System.err.println("Error: " + e.getMessage());
		}
		
		int flag = 0;
		String title = null, authors = null, journal = null, Year = null, citation = null;
		ArrayList outputAL = new ArrayList();
		for (int i = 0; i < allstrs.size(); i++){
			//System.out.println((String)allstrs.get(i));
			
			
			flag++;
			
			if (((String)allstrs.get(i)).isEmpty()){
				ArrayList outputAL_row = new ArrayList();
				outputAL_row.add(title.replace(",",""));
				outputAL_row.add(authors.replace(",",""));
				outputAL_row.add(journal.replace(",",""));
				outputAL_row.add(Year.replace(",",""));
				outputAL_row.add(citation);
				
				outputAL.add(outputAL_row);
				flag = 0;
			}
			else if (flag == 1){
				title = (String)allstrs.get(i);
				citation = "0";
			}
			else if (flag == 2){
				authors = ((String)allstrs.get(i)).split(" - ")[0];
				
				journal = "";
				for(int j = 0; j < (((String)allstrs.get(i)).split(" - ")[1]).split(" ").length; j++){
					if (j == ((((String)allstrs.get(i)).split(" - ")[1]).split(" ").length - 1))
						Year = (((String)allstrs.get(i)).split(" - ")[1]).split(" ")[(((String)allstrs.get(i)).split(" - ")[1]).split(" ").length - 1];
					else
						journal = journal + (((String)allstrs.get(i)).split(" - ")[1]).split(" ")[j];
				}
				
				 
				
			}
			else if (flag == 6){
				if(((String)allstrs.get(i)).split(" ")[0].equals("�Q�ޥ�")) //"被引用"
					citation = ((String)allstrs.get(i)).split(" ")[1];								
			}
			

		}
		fileWrite.write_2d_arraylist_string2file("GoogleScholar_ResultConverter_output.csv", outputAL, true); // new file
	}
	
	
	
	public static void algo2_wholepage(){
		ArrayList tmpAL;
		
		ArrayList<String> allstrs = new ArrayList();
		try{
		  // Open the file that is the first 
		  // command line parameter
		  FileInputStream fstream = new FileInputStream("GoogleScholar_ResultConverter_input.txt");
		  // Get the object of DataInputStream
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;
		  //Read File Line By Line
		  while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console
		  //System.out.println (strLine);
			  
			  allstrs.add(strLine);
			  
			  
		  }
		  //Close the input stream
		  in.close();
		  }catch (Exception e){//Catch exception if any
		  System.err.println("Error: " + e.getMessage());
		}
		
		//將allstrs逆序
		tmpAL = allstrs;
		allstrs = new ArrayList();		
		for(int i = (tmpAL.size() - 1); i >= 0; i--)
			allstrs.add((String) tmpAL.get(i));
		
		
		
		int flag = -1;
		String title = null, authors = null, journal = null, Year = null, citation = null;
		ArrayList outputAL = new ArrayList();
		for (int i = 0; i < allstrs.size(); i++){
			//System.out.println((String)allstrs.get(i));
			
			flag--;
			
			String[] checkRow = allstrs.get(i).split(" ");
			if (checkRow[checkRow.length - 1].equals("�ޥ�")){ // 引用				
				
				if(((String)allstrs.get(i)).split(" ")[0].equals("�Q�ޥ�")) //"被引用"
					citation = ((String)allstrs.get(i)).split(" ")[1];
				else
					citation = "0";
				
				flag = 6;
				
			}
			else if (flag == 1)
				title = (String)allstrs.get(i);						
			//else if (flag == 2){
			else if ((((String)allstrs.get(i)).split(" - ")).length == 3){
				authors = ((String)allstrs.get(i)).split(" - ")[0];				
				journal = (((String)allstrs.get(i)).split(" - ")[1]).split(", ")[0];
				Year = (((String)allstrs.get(i)).split(" - ")[1]).split(", ")[1];
				flag = 2;
			}
			else if (flag == 0){
				ArrayList outputAL_row = new ArrayList();
				outputAL_row.add(title.replace(",",""));
				outputAL_row.add(authors.replace(",",""));
				outputAL_row.add(journal.replace(",",""));
				outputAL_row.add(Year.replace(",",""));
				outputAL_row.add(citation);
				
				outputAL.add(outputAL_row);
			}
			
			
		}
		//header:
		ArrayList outputAL_row = new ArrayList();
		outputAL_row.add("title");
		outputAL_row.add("authors");
		outputAL_row.add("journal");
		outputAL_row.add("Year");
		outputAL_row.add("citation");
		outputAL.add(outputAL_row);
		
		//將allstrs逆序
		tmpAL = outputAL;
		outputAL = new ArrayList();		
		for(int i = (tmpAL.size() - 1); i >= 0; i--)
			outputAL.add(tmpAL.get(i));
				
		fileWrite.write_2d_arraylist_string2file("GoogleScholar_ResultConverter_output.csv", outputAL, true); // new file
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//algo1();
		
		algo2_wholepage();
	}

}
