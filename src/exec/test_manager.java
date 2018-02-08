package exec;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.J48;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimeZone;

import numerical.CentralDifference;
import numerical.inferfaces.Function;

import preproc.preproc_manager_KM_C04data;
import util.PDFTextParser;

import fsOp.CopyFile;
import fsOp.FileTraversal_manager;

public class test_manager {
	public static void main(String[] args) throws Throwable {
		
		Boolean a = new Boolean(false);
		a = true;
		if (a.booleanValue())System.out.println("true");
		else System.out.println("false");
		
		
		/*int window_size = 397;
		int endIndex = 0;
		
		
		Classifier cls = null;
		Evaluation eval = null;
		DataSource source;
		Instances train;
		Instances test;		
		double proNum[];		
		double outputReal[];
		double outputPred[];
				
		for (int endIndex_ite=1; endIndex_ite <=7 ; endIndex_ite+=1){
			switch(endIndex_ite) { 
	        case 1: 
	        	endIndex = 1588;
	        	break;
	        case 2: 
	        	endIndex = 1985;
	        	break;
	        case 3: 
	        	endIndex = 2382;
	        	break;
	        case 4: 
	        	endIndex = 2779;
	        	break;
	        case 5: 
	        	endIndex = 3176;
	        	break;
	        case 6: 
	        	endIndex = 3573;
	        	break;
	        case 7: 
	        	endIndex = 3970;
	        	break;
			}
			
			proNum = new double[window_size];
			for (int i=0; i < proNum.length; i+=1){
				proNum[i] = endIndex - 397 + i;
			}
			
			for (int selCls=1; selCls <=2 ; selCls+=1){
				
				for (int testAttr=1; testAttr <=3 ; testAttr+=1){

					source = new DataSource("end"+String.valueOf(endIndex)+"_train.csv");
					train = source.getDataSet();
					train.deleteAttributeAt(0);
					
					 // train classifier
					switch(selCls) { 
			        case 1: 
			        	cls = new weka.classifiers.functions.LinearRegression();
			        	break;
			        case 2:
			        	cls = new weka.classifiers.functions.RBFNetwork();
			        	break;
					}

					
					switch(testAttr) { 
			        case 1: 
			        	train.deleteAttributeAt(train.numAttributes()-1);
			   		 	train.deleteAttributeAt(train.numAttributes()-1);
			   		 	train.setClassIndex(train.numAttributes()-1);		 	
			        	break;
			        case 2:			        	
			        	train.deleteAttributeAt(train.numAttributes()-3);
			        	train.deleteAttributeAt(train.numAttributes()-1);   		 	
			   		 	train.setClassIndex(train.numAttributes()-1);		 			   		 	
			            break; 
			        case 3: 
			        	train.deleteAttributeAt(train.numAttributes()-3);
			   		 	train.deleteAttributeAt(train.numAttributes()-2);
			   		 	train.setClassIndex(train.numAttributes()-1);		 
			        	break;             
					}
					source = new DataSource("end"+String.valueOf(endIndex)+"_test"+String.valueOf(testAttr)+".csv");
					
					test = source.getDataSet();		
					test.setClassIndex(test.numAttributes()-1);
					 
					cls.buildClassifier(train);
					// evaluate classifier and print some statistics
					eval = new Evaluation(train);
					 
					outputPred = eval.evaluateModel(cls, test);
					outputReal = test.attributeToDoubleArray(test.numAttributes()-1);
					 
					try
					{    
				       	System.out.println("---Writting " + "end"+String.valueOf(endIndex)+"_train.csv" +" to File---");
						FileWriter writer = new FileWriter("output_matrix_"+String.valueOf(selCls)+"_"+String.valueOf(testAttr)+".csv", true);

						for (int i=0; i < outputPred.length; i+=1){
							writer.append(String.valueOf(proNum[i]));    					
							writer.append(",");
							writer.append(String.valueOf(outputPred[i]));
							writer.append(",");
							writer.append(String.valueOf(outputReal[i]));    					
							writer.append("\n");
							writer.flush();					
						}									
						writer.close();	
					}catch(Exception e1){System.out.println("Mistake in I/O of toolbox.outputPop: "+e1);}
				}
				
			}
		}*/
		
		
		System.out.println("---Done---");
		 
		 
		 //System.out.println(eval.toSummaryString("\nResults\n======\n", false));
		 //System.out.println(eval.getClassPriors());
		 
		 
		/*System.out.println(new File(new File("./").getCanonicalPath()).getName());
	
		//[PDF tran. text]
		PDFTextParser ptp = new PDFTextParser();
		String[] input = new String[2];
		input[0] = "p455-kim.pdf";
		input[1] = "p455-kim.txt";		
		ptp.main(input);*/
		
		//Java跟C#對轉tick
/*		Calendar calendar = Calendar.getInstance();    
		calendar.set(Calendar.MILLISECOND, 0); // Clear the millis part. Silly API.
		//calendar.set(0001, 1, 1, 0, 0, 0); // Note that months are 0-based
		calendar.set(2012, 1, 16, 15, 38, 0); // Note that months are 0-based
		Date date = calendar.getTime();
		long millis = date.getTime(); // Millis since Unix epoch
		System.out.println(String.valueOf(millis));*/
		
/*		Calendar calendar = Calendar.getInstance();  
		long timeJava_ms = calendar.getTime().getTime();
		//calendar.set(Calendar.MILLISECOND, 0);
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));		
		calendar.set(0001, 1, 1, 0, 0, 0);
		long offset_Java_ms = calendar.getTime().getTime() * (-1);		
		long millis = 25056200000000L + (timeJava_ms+offset_Java_ms)*10000;// + System.nanoTime()/10;
*/		//25056200000000L是手動tune出來的..幹...大概會誤差幾秒吧...不知名的offset...
		//System.out.println(String.valueOf(millis));
		
/*		Calendar calendar = Calendar.getInstance();    
		//calendar.set(Calendar.MILLISECOND, 0); // Clear the millis part. (被省略不知為何)
		calendar.set(2012, 1, 16, 15, 46, 0); // Note that months are 0-based
		
		long millis = calendar.getTime().getTime();
		calendar.set(0001, 1, 1, 0, 0, 0); // Note that months are 0-based	(C#的開始時間)
		millis = millis - calendar.getTime().getTime();
		System.out.println(String.valueOf(millis));
		*/
		
		
		
/*
		 [Daemon for C07]
		 * 1. get extracted features from [Feature Extraction Manager for C07]
		 * 1.1 copy files (Feedback記得只copy最後一個row)
		 * 1.2 delete files
		 * 1.3 return extract features
		 * 
		 * 2. 生CT_File		//處理(append col_n+1, 並調整row count)
		 * 3. 請XCSF處理 -- TrainAndTest -- col_n永遠都是testing set 
		 * 4. XCSF output the prediction
		 
		final String inputDir = "C:/DBoxs/prim/Dropbox" +
				"/特設-校務大小事/VBM Lab/1_投稿工作站" +
				"/MScript_C04 (Affect Det. K+M)/_expr/TP_Exp";
		final int interval_min = 3;
		final int featureNum = 6;
		final int feedback_max = -1; //最多取幾個feedback, 設-1表示max for all subjects
		final double symbol_MissingData = -10.0;
		
		int p = 0;
		while(true){
			System.out.println(p);
			Thread.sleep(2000);			
			p++;
			if (p>10000)break;
		}
		
		
		boolean status = new File("tmp").mkdir();
		System.out.print(String.valueOf(status));
		
		//copy files, then 去開個資料夾--把檔案放出來, 裝給path_list
		File[] filelist = new File(inputDir + "/Data").listFiles();
		for(int i = 0; i < filelist.length; i++)
			CopyFile.copyfile(filelist[i].getAbsolutePath(),"./tmp/"+filelist[i].getName());
		filelist = new File(inputDir + "/MouseMoveData").listFiles();
				for(int i = 0; i < filelist.length; i++)
			CopyFile.copyfile(filelist[i].getAbsolutePath(),"./tmp/"+filelist[i].getName());
				
		String[] filelist_str = new File("./tmp").list();
		ArrayList filelist_al = new ArrayList();		
		for(int i = 0; i < filelist_str.length; i++)filelist_al.add(filelist_str[i]);
				
		preproc_manager pm = new preproc_manager();
		Double[][] extData = pm.feature_extract_c04_single_subject
    	(filelist_al, interval_min, 
    			(2+featureNum), feedback_max, symbol_MissingData);

		filelist = new File("./tmp").listFiles();
		for(int i = 0; i < filelist.length; i++)status = filelist[i].delete();
		
		try
		{    			
			FileWriter writer = new FileWriter("preproc_output.csv", false);    			
			for (int i=0; i < extData.length; i+=1){
				//這地方可能要放headder
				writer.append(String.valueOf(1));
				writer.append(",");	
				for (int j=0; j < extData[0].length; j+=1){
					writer.append(String.valueOf(extData[i][j]));    					
					writer.append(",");	
				}
				writer.append("\n");
    		    writer.flush();
			}    			
		    writer.close();	
		}catch(Exception e1){System.out.println("Mistake in I/O of toolbox.outputPop: "+e1);}
		

		xcsf.applications.CT_File_Train_and_Test
		.main_assignable(args, ori_sIndex, ori_eIndex, 
				new_sIndex, new_eIndex, ite_time_toSwitch);
*/
	}
	

}


