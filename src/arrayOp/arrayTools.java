package arrayOp;

import java.util.Arrays;
import java.util.Collections;

public class arrayTools {
	
	public static Integer[] randomSuffle(Integer[] inputArr){
		Collections.shuffle(Arrays.asList(inputArr));

		/*for (int i=0;i<num;i++)
			System.out.println(String.valueOf(a[i]));*/
		return inputArr;
	}
	
	
	
}
