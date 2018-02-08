package util.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Comparator;

class MyIntComparable implements Comparator<Integer>{
	 
    @Override
    public int compare(Integer o1, Integer o2) {
    	return o1 - o2;
        
    	//return (o1>o2 ? -1 : (o1==o2 ? 0 : 1));
    	// -1(<0)就是排前面, 0(==0)就是對等, 1(>0)就是排後面
    }
}

public class testingCollectionComparator {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        list.add(5);
        list.add(4);
        list.add(3);
        list.add(7);
        list.add(2);
        list.add(1);
        Collections.sort(list, new MyIntComparable());
        for (Integer integer : list) {
            System.out.println(integer);
        }
    }
}
