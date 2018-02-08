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

public class preproc_C12_Weka_WFCV {
	public static void main(String[] args) throws Throwable {
		int window_size = 397;
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

					source = new DataSource("./WFCV dataset/end"+String.valueOf(endIndex)+"_train.csv");
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
					source = new DataSource("./WFCV dataset/end"+String.valueOf(endIndex)+"_test"+String.valueOf(testAttr)+".csv");
					
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
		}
		
		
		System.out.println("---Done---");
		 
	}
	

}


