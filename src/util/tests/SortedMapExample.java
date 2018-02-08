package util.tests;

import java.util.*;

public class SortedMapExample{

	  public static void main(String[] args) {

	  SortedMap map = new TreeMap();

	  // Add some elements:
	  map.put("3.1", "Two");
	  map.put("10", "One");
	  map.put("3.2", "Five");
	  map.put("4", "Four");
	  map.put("3", "Three");

	  // Display the lowest key:
	  System.out.println("The lowest key value is: " + map.firstKey());

	  // Display the highest key:
	  System.out.println("The highest key value is: " + map.lastKey());

	  // Display All key value
	  System.out.println("All key value is:\n" + map);

	  // Display the headMap:
	  System.out.println("The head map is:\n" + map.headMap("4"));

	  // Display the tailMap:
	  System.out.println("The tail map is:\n" + map.tailMap("4"));

	  // keySet method returns a Set view of the keys contained in this map.
	  Iterator iterator = map.keySet().iterator();
	  while (iterator.hasNext()) {
	  Object key = iterator.next();
	  System.out.println("key : " + key + " value :" + map.get(key));
	  }
	  }
	}
