package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
 

//注意cmd.務必是program才可以..
public class RuntimeProgExec {
public StreamWrapper getStreamWrapper(InputStream is, String type){
            return new StreamWrapper(is, type);
}
private class StreamWrapper extends Thread {
    InputStream is = null;
    String type = null;          
    String message = null;
 
    public String getMessage() {
            return message;
    }
 
    StreamWrapper(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }
 
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ( (line = br.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            message = buffer.toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();  
        }
    }
}
  
//this is where the action is
public static String[] runProgGetOutput(String cmd) {
            Runtime rt = Runtime.getRuntime();
            RuntimeProgExec rte = new RuntimeProgExec();
            StreamWrapper error, output;
 
            try {
                        Process proc = rt.exec(cmd);            			
            			
                        error = rte.getStreamWrapper(proc.getErrorStream(), "ERROR");
                        output = rte.getStreamWrapper(proc.getInputStream(), "OUTPUT");
                        int exitVal = 0;
 
                        error.start();
                        output.start();
                        error.join(3000);
                        output.join(3000);
                        exitVal = proc.waitFor();
                        //System.out.println("Output: "+output.message+"\nError: "+error.message);
                        String[] outputArr = new String[2];
                        outputArr[0] = output.message;
                        outputArr[1] = error.message;
                        
                        return outputArr;
            
            } catch (InterruptedException e) {
                        e.printStackTrace();
            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
			return null;
            
}
 
// this is where the action is
public static void main(String[] args) {
            Runtime rt = Runtime.getRuntime();
            RuntimeProgExec rte = new RuntimeProgExec();
            StreamWrapper error, output;
 
            try {
                        Process proc = rt.exec("ping localhost");            			
            			
                        error = rte.getStreamWrapper(proc.getErrorStream(), "ERROR");
                        output = rte.getStreamWrapper(proc.getInputStream(), "OUTPUT");
                        int exitVal = 0;
 
                        error.start();
                        output.start();
                        error.join(3000);
                        output.join(3000);
                        exitVal = proc.waitFor();
                        System.out.println("Output: "+output.message+"\nError: "+error.message);
            } catch (IOException e) {
                        e.printStackTrace();
            } catch (InterruptedException e) {
                        e.printStackTrace();
            }
            }
}