package expr;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

import fsOp.fileWrite;

public class labouchere {
	// C:\DBoxs\prim\Dropbox\WKK\_WorkStation\201508 [研究] 輪盤賭博下注法\lrouG.txt

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*Rules and instructions for Labouchere simulation:
			- Bet 18 numbers or an EC bet (i.e. red/black)
			- Start number sequence is: 1-2-3-4-5-6
			- After a win: remove first and last number from the list
			- After a loss: add bet amount to the end of the list
			- Restart sequence if betamount is larger than 200*/
			
		Random randomGenerator = new Random();
		double capital_orig = 200;
		
		ArrayList outputCSV = new ArrayList();
		ArrayList outputCSV_row;
		
		for (int game_id = 1; game_id <= 1000000; game_id++){
			
			ArrayList numSeq = new ArrayList();
			numSeq.add("1");
			numSeq.add("2");
			numSeq.add("3");
			numSeq.add("4");
			numSeq.add("5");
			numSeq.add("6");
						
			double capital = capital_orig;
			double baseBet = 200/(1+2+3+4+5+6);
			double Bet = -99;
			double MDDest = 0;	// MDD 自原始 Capital
			
			//System.out.println("===Starting with capital: " + String.valueOf(capital) + " ===");
			outputCSV_row = new ArrayList();
			
			for (int idx = 1; idx <= 100000000; idx++){
				
				
				if((capital_orig > capital) && ((capital - capital_orig) < MDDest)){
					MDDest = capital - capital_orig;
				}
				
				
				if(numSeq.size() == 0){
					//System.out.println("You Win!");
					break;
				}
				else if(numSeq.size() == 1){
					Bet = Double.valueOf((String) numSeq.get(0))*baseBet;
				}
				else{
					Bet = (Double.valueOf((String) numSeq.get(0)) 
							+ Double.valueOf((String) numSeq.get(numSeq.size()-1)))*baseBet;										
				}
				
				if(capital < Bet){
					//System.out.println("EOG! No money to bet minimum bet.");					
					break;
				}
							
				if (randomGenerator.nextBoolean()){
					capital = capital + Bet;
					numSeq.remove(0);
					if (numSeq.size() > 0)numSeq.remove(numSeq.size()-1);
					
					//System.out.println("idx = " + idx + ": Win, current capital = " + String.valueOf(capital));
				}
				else{
					capital = capital - Bet;
					numSeq.add(String.valueOf(Double.valueOf((String) numSeq.get(0)) 
						+ Double.valueOf((String) numSeq.get(numSeq.size()-1))));
					
					//System.out.println("idx = " + idx + ": Lose, current capital = " + String.valueOf(capital));
				}
				
			}
			//System.out.println(MDDest);
			outputCSV_row.add(String.valueOf(MDDest));
			
			if(capital < Bet)
				outputCSV_row.add("Lose");
			else if (capital >= (capital + 200))
				outputCSV_row.add("Win");
			else
				outputCSV_row.add("capital = "+String.valueOf(capital));
			
			outputCSV.add(outputCSV_row);
			
		}
		
		
		fileWrite.write_2d_arraylist_string2file("labouchere-sim.csv", outputCSV, true); // no append
		
		
	}

}
