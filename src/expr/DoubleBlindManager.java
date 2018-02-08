package expr;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import arrayOp.arrayTools;



public class DoubleBlindManager 
{
	public static void doubleblindseqGen() throws FileNotFoundException{
		
		Scanner sc = new Scanner(System.in);
		System.out.println("How many kind of tags do you want...?");
		int numTags = sc.nextInt();		
		System.out.println("And their names...? (e.g. tag1,tag2,tag3,...)");
		String[] names = sc.next().split(",");				
		System.out.println("How many elements in the sequence do you want...?");
		int numElement = sc.nextInt();				
		
		
		Integer[] NumberOfExam = new Integer[numElement];
		
		
		for(int i =0;i<numElement;i++){
			NumberOfExam[i] = (i % numTags) + 1;
		}
		
		NumberOfExam = arrayTools.randomSuffle(NumberOfExam);
		
		
		File file = new File("Number.txt");		
		PrintWriter PT = new PrintWriter(file);
		
		for(int i =0;i<numElement;i++)
		{	
			System.out.print(NumberOfExam[i]);
			if(i<9)
			{
				PT.println("第"+"0"+(i+1)+"位:"+NumberOfExam[i]);
			}
			else
			{
				PT.println("第"+(i+1)+"位:"+NumberOfExam[i]);
			}
		}		
		PT.close();
		
		file = new File("Select.txt");
		PT = new PrintWriter(file);
		
		Collections.shuffle(Arrays.asList(NumberOfExam));
		//NumberOfExam = arrayTools.randomSuffle(NumberOfExam);
		
		Collections.shuffle(Arrays.asList(names));
		for(int i=0; i<numTags;i++){
			PT.println(String.valueOf(i+1)+":"+names[i]);
		}
						
		PT.close();
	}
	
	
	public static void main(String[] args) throws Exception
	{
		doubleblindseqGen();		
		
	}

}

