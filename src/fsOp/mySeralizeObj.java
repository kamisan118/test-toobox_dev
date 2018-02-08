package fsOp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class mySeralizeObj {

	
	private Object theContent;
	private String theClassName;
	private String theDescription;
	
	
	
	public void setObject(Object inObj, String inObjName, String inObjDescription) {		
		this.theContent = inObj;
		this.theClassName = inObjName;
		this.theDescription = inObjDescription;
	}
	
	public void setObject(Object inObj, String inObjName) {		
		setObject(inObj, inObjName, "N/A");		
	}
	
	public void setObject(Object inObj) {
		setObject(inObj, "N/A", "N/A");
	}
	
	
	public static void saveObject(String file_path, Object inObj) {
	      try
	      {
	         FileOutputStream fileOut =
	         new FileOutputStream(file_path);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(inObj);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in "+file_path);
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }	
		
	}
	
	public void saveObject(String file_path) {
		saveObject(file_path, theContent);
	}
	
	public static Object loadObject(String file_path) {
		Object outObj;
		
        try
        {
           FileInputStream fileIn = new FileInputStream("filename.ser");
           ObjectInputStream in = new ObjectInputStream(fileIn);
           outObj = in.readObject();
           in.close();
           fileIn.close();           
        }catch(IOException i)
        {        	           
           i.printStackTrace();
           return null;
        }catch(ClassNotFoundException c)
        {
           System.out.println("XClassifierSet class not found");
           c.printStackTrace();
           return null;
        }
        
        return outObj;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
//	public class XClassifierSet implements Serializable
//	http://www.tutorialspoint.com/java/java_serialization.htm
	
	
	            //load [P]
//	            try
//	            {
//	               FileInputStream fileIn = new FileInputStream("filename.ser");
//	               ObjectInputStream in = new ObjectInputStream(fileIn);
//	               pop = (XClassifierSet) in.readObject();
//	               in.close();
//	            }catch(IOException i)
//	            {
//	               fileIn.close();
//	               i.printStackTrace();
//	               return;
//	            }catch(ClassNotFoundException c)
//	            {
//	               System.out.println("XClassifierSet class not found");
//	               c.printStackTrace();
//	               return;
//	            }

	                		//save [P]
	//假設已有XClassifierSet pop;
//	                	      try
//	                	      {
//	                	         FileOutputStream fileOut =
//	                	         new FileOutputStream("filename.ser");
//	                	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
//	                	         out.writeObject(pop);
//	                	         out.close();
//	                	         fileOut.close();
//	                	         System.out.printf("Serialized data is saved in filename.ser");
//	                	      }catch(IOException i)
//	                	      {
//	                	          i.printStackTrace();
//	                	      }


}
