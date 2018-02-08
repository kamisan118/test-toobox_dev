package fsOp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;



public class FileTraversal_manager {
	public ArrayList traverse_log_table_dir = new ArrayList();	//�|�]�Aroot dir
	public ArrayList traverse_log_table_file = new ArrayList();
	
	//[Usage] -- 2014/1/14 17:19 by PM
	//*only for two level dirs
	//*all files are in the second level dirs, with no "files" standalone in the top level dir
	//*get all files in each second level dir by using the key (the name of second level dir) to get a arraylist from the hashtable
	
	/*[Usage] -- 2012/01/12 20:01 by PM
	 * FileTraversal_manager fm = new FileTraversal_manager();						
    Hashtable dir_tab = fm.traverse_and_build_two_level_dir
    (new File("D:/All_User/pmli/My Documents/dataBase/BenX_DataBase_snapshot_20120111/201201 ��L�P�ƹ������(14)/raw data"));*/
	public final Hashtable traverse_and_build_two_level_dir( final File f ) throws IOException {
					
		return traverse_and_build_two_level_dir_ArrayList_in2ndDirListing(f);
		
		
	}
	
	public final Hashtable traverse_and_build_two_level_dir_ArrayList_in2ndDirListing( final File f ) throws IOException {
		Hashtable dir_tab = new Hashtable();
		traverse(f);
		String dir_name;
		for(int i=0; i < traverse_log_table_dir.size(); i++){
			dir_name = ((File)traverse_log_table_dir.get(i)).getName();
			if (!dir_name.equals(f.getName()))
				dir_tab.put(dir_name, new ArrayList());
		}
		for(int i=0; i < traverse_log_table_file.size(); i++){
			((ArrayList)dir_tab.get((((File)traverse_log_table_file.get(i))
					.getParentFile().getName()))).add(((File)traverse_log_table_file.get(i)).getAbsolutePath());	//�`�N: �몺�Oabsolute path
		}
					
		return dir_tab;
	}
	
	
	public final Hashtable traverse_and_build_two_level_dir_hashTable_in2ndDirListing( final File f ) throws IOException {
		Hashtable dir_tab = new Hashtable();
		traverse(f);
		String dir_name;
		for(int i=0; i < traverse_log_table_dir.size(); i++){
			dir_name = ((File)traverse_log_table_dir.get(i)).getName();
			if (!dir_name.equals(f.getName()))
				dir_tab.put(dir_name, new Hashtable());
		}
		for(int i=0; i < traverse_log_table_file.size(); i++){
			((Hashtable)dir_tab.get((((File)traverse_log_table_file.get(i))
					.getParentFile().getName()))).put(((File)traverse_log_table_file.get(i))
							.getName(),((File)traverse_log_table_file.get(i)).getAbsolutePath());	//�`�N: �몺�Oabsolute path
		}
					
		return dir_tab;
	}
	
	
	/*[Usage] -- 2012/01/12 20:01 by PM
	FileTraversal_manager fm = new FileTraversal_manager() {
	    public void onFile( final File f ) {
	        System.out.println(f);
	    }
	};		
	fm.traverse(new File("D:/All_User/pmli/My Documents/dataBase/BenX_DataBase_snapshot_20120111/201201 ��L�P�ƹ������(14)/raw data (additional)/20120106 PM Shot1@BenX"));*/
    public final void traverse( final File f ) throws IOException {
        if (f.isDirectory()) {
        	traverse_log_table_dir.add(f);
                onDirectory(f);
                final File[] childs = f.listFiles();
                for( File child : childs ) {
                        traverse(child);
                }
                return;
        }       
        traverse_log_table_file.add(f);
        onFile(f);
    }

    public void onDirectory( final File d ) {
    }

    public void onFile( final File f ) {
      
    }
}
