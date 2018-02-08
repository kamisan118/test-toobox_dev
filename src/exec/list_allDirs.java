package exec;

//test comment

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import fsOp.fileWrite;


public class list_allDirs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String dirPath = null;
		String[] extensions = null;
		
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get("list_allDirs.conf"), Charset.forName("UTF-8"));
			
//			for(String line:lines){
//				  System.out.println(line);
//			}
			
			dirPath = lines.get(0);
			System.out.println("dirPath = " + dirPath);	
			lines.remove(0);
			extensions = lines.toArray(new String[lines.size()]);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    File myFile = new File(dirPath);   
	    if(myFile == null || !myFile.exists()) {
	        System.out.println("Bad directory path!");
	        System.exit(-1);
	    }		
				
		ArrayList out2D_AL = new ArrayList(); 
		ArrayList out2D_AL_head = new ArrayList();
		out2D_AL_head.add("filename");
		out2D_AL_head.add("path");		
		out2D_AL.add(out2D_AL_head);
		
		try {
			System.out.println("Getting all files in " + myFile.getCanonicalPath()
					+ "with extension specified in list_allDirs.conf (including those in subdirectories)");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<File> files = (List<File>) FileUtils.listFiles(myFile, extensions, true);
		for (File file : files) {
			try {			
		    	ArrayList out2D_AL_row = new ArrayList();			    	
		    	out2D_AL_row.add(FilenameUtils.removeExtension(file.getName()).toUpperCase());	
		    	out2D_AL_row.add(file.getAbsolutePath());
		    	out2D_AL.add(out2D_AL_row);
		    	
				System.out.println("file: " + file.getCanonicalPath() + " done.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    
		fileWrite.write_2d_arraylist_string2file("allDirs.csv", out2D_AL, true); // no append
	}

}
