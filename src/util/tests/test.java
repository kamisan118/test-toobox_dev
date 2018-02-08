package util.tests;

import java.io.IOException;

public class test {

	public static void main(String[] args)  {
		System.out.println("hello!");
		for(int i=0;i<100000;i++)
			System.out.print("\r");
	//System.out.print("\033[2J");
	//cc();
	
/*	try {
		//Runtime.getRuntime().exec("c:\\windows\\notepad.exe");
		//Runtime.getRuntime().exec("cls");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	}
	
	public static void cc() {
		//final static String ESC = "\033[";
		String ESC = "\033[";
		System.out.print(ESC + "2J");
		
		//Runtime.exec("cls");
		System.out.print("\f");
    }
}