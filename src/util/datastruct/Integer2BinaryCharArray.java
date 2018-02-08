package util.datastruct;

public class Integer2BinaryCharArray {

	//參數1: 欲轉換的數字, 參數2: 欲轉換的Condition String長度
	public static char[] int2bCharAry(int inInt, int lengthOfCharAry){
				
		char[] outputAry = new char[lengthOfCharAry];
		
		//假設condition string是從左到右
		for(int i = (outputAry.length - 1); i >= 0 ; i--){
			if (inInt < 1)outputAry[i] = '0';
			else{
				outputAry[i] = (char) ('0' + inInt % 2);
				inInt = inInt >> 1;	
			}
		}

		return outputAry;		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i = 0; i < 1024; i++)System.out.println(String.valueOf(int2bCharAry(i,10)));
	}

}
