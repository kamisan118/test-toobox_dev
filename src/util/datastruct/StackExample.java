package util.datastruct;

import java.util.Iterator;
import java.util.Stack;

public class StackExample {

    public static void main(String[] args) {

        Stack<String> sk=new Stack<String>();

        sk.push("a");
        sk.push("c");
        sk.push("e");
        sk.push("d");

        Iterator it=sk.iterator();

        System.out.println("Size before pop() :"+sk.size());

        while(it.hasNext())
        {
            String iValue=(String)it.next();
            System.out.println("Iterator value :"+iValue);
        }

        // get and remove last element from stack
        String value =(String)sk.pop();

        System.out.println("value :"+value);

        System.out.println("Size After pop() :"+sk.size());

    }
}

/*	
		Size before pop() :4
		Iterator value :a
		Iterator value :c
		Iterator value :e
		Iterator value :d
		value :d
		Size After pop() :3
*/
