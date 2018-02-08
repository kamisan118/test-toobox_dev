package arrayOp;

import java.util.Arrays;


public class Array2DAppend {

	public static void main(String[] args) {

        int[][] a = new int[][] {{1, 2}, {3, 4}};
        int[][] b = new int[][] {{1, 2, 3}, {3, 4, 5}};
        
        System.out.println(Arrays.deepToString(a));
        System.out.println(Arrays.deepToString(b));
        System.out.println(Arrays.deepToString(append_intArray(a, b)));

    }

    public static int[][] append_intArray(int[][] a, int[][] b) {
        int[][] result = new int[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    
    public static double[][] append_dblArray(double[][] a, double[][] b) {
    	double[][] result = new double[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}