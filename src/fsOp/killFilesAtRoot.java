package fsOp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//確認根目錄沒有csv檔, 有的話就刪掉.
public class killFilesAtRoot {

	public static void kill(){
		//確認根目錄沒有csv檔, 有的話就刪掉.
		FileTraversal_manager fm = new FileTraversal_manager();
		try {
			fm.traverse((new File("./")));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList deleteFile_list = new ArrayList();
		
		for(int i = 0; i < fm.traverse_log_table_file.size(); i++){
			//System.out.println(((File)fm.traverse_log_table_file.get(i)).toPath());
			
			//System.out.println(((File)fm.traverse_log_table_file.get(i)).toPath().toString());
			//if (((File)fm.traverse_log_table_file.get(i)).toPath().toString().matches(".*\\.\\\\src{1}.*"))
			if (((File)fm.traverse_log_table_file.get(i)).toPath().toString().matches("\\.\\\\[a-zA-Z0-9-_]*\\.csv")){
				//System.out.println(((File)fm.traverse_log_table_file.get(i)).toPath());
				deleteFile_list.add(((File)fm.traverse_log_table_file.get(i)));
			}				
		}
		
		if (deleteFile_list.size() != 0)
			System.out.println("--->> Cleaning root directory, deleting files listed below: ");
		
		for(int i = 0; i < deleteFile_list.size(); i++){
			
			boolean success = ((File)deleteFile_list.get(i)).delete();	
			if (!success)
				throw new IllegalArgumentException("Delete: deletion failed: " 
			+ ((File)deleteFile_list.get(i)).getPath().toString());
			else
				System.out.println(((File)deleteFile_list.get(i)).toPath().toString() + " deletion successful.");
				
		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		killFilesAtRoot.kill();
	}

}
