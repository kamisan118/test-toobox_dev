package util.physics;

import java.util.ArrayList;

public class graphMining {

	
	public static double get_2d_length_of_path(ArrayList al_2D, int xcol, int ycol){
		double length_of_path = 0;
		
		for(int i = 1; i < al_2D.size(); i++){
			length_of_path += 
					Math.sqrt(
					Math.pow(Double.parseDouble((String)((ArrayList)al_2D.get(i)).get(xcol))
							- Double.parseDouble((String)((ArrayList)al_2D.get(i - 1)).get(xcol)), 2)
					+ Math.pow(Double.parseDouble((String)((ArrayList)al_2D.get(i)).get(ycol))
							- Double.parseDouble((String)((ArrayList)al_2D.get(i - 1)).get(ycol)), 2)
					);						
		}
		
		return length_of_path;
		
	}
	
	public static double get_2d_speed(ArrayList al_2D, int xcol, int ycol, int tcol){
		//注意單位是 "d/0.1s"
		double speed = get_2d_length_of_path(al_2D, xcol, ycol) 
				/ ((Long.parseLong((String)((ArrayList)al_2D.get(al_2D.size() - 1)).get(tcol))
						- Long.parseLong((String)((ArrayList)al_2D.get(0)).get(tcol)))/1000000.0D);		
				
		return speed;
		
	}
	
	public static double get_2d_displacement(ArrayList al_2D, int xcol, int ycol){

		double displacement = Math.sqrt(
				Math.pow(Double.parseDouble((String)((ArrayList)al_2D.get(al_2D.size() - 1)).get(xcol))
						- Double.parseDouble((String)((ArrayList)al_2D.get(0)).get(xcol)), 2)
				+ Math.pow(Double.parseDouble((String)((ArrayList)al_2D.get(al_2D.size() - 1)).get(ycol))
						- Double.parseDouble((String)((ArrayList)al_2D.get(0)).get(ycol)), 2)
				);		
		
		return displacement;
		
	}
	
	public static double get_2d_deltaT(ArrayList al_2D, int tcol){

		double deltaT = (Long.parseLong((String)((ArrayList)al_2D.get(al_2D.size() - 1)).get(tcol))
				- Long.parseLong((String)((ArrayList)al_2D.get(0)).get(tcol)));		
		
		return deltaT;
		
	}
	
	public static double get_2d_velocity(ArrayList al_2D, int xcol, int ycol, int tcol){
		//注意單位是 "d/0.1s"
		double velocity = get_2d_displacement(al_2D, xcol, ycol) 
				/ ((Long.parseLong((String)((ArrayList)al_2D.get(al_2D.size() - 1)).get(tcol))
						- Long.parseLong((String)((ArrayList)al_2D.get(0)).get(tcol)))/1000000.0D);		
		
		return velocity;
		
	}
	
	public static int get_Quadrant(int center_x, int center_y, int x, int y){
		if (((x - center_x) > 0) && ((y - center_x) > 0))
			return 1; // Q1
		else if (((x - center_x) < 0) && ((y - center_x) > 0))
			return 2; // Q2
		else if (((x - center_x) < 0) && ((y - center_x) < 0))
			return 3; // Q3
		else if (((x - center_x) > 0) && ((y - center_x) < 0))
			return 4; // Q4
		else
			return -1; // which means (x,y) it's on the axis.
	}
	
	public static int get_Quadrant(ArrayList al_1d, int center_x, int center_y, int xcol, int ycol){		
		
		return get_Quadrant(center_x, center_y, Integer.parseInt((String)al_1d.get(xcol)), Integer.parseInt(((String)al_1d.get(ycol))));
		
	}
	
	
	public static double get_r_of_clickTarget(ArrayList al_1d, String tarGoal, int xcol, int ycol){
		int center_x, center_y;		
		
		if (tarGoal.equals("[1]")){
			center_x = 101;
			center_y = 161;
		}
		else if (tarGoal.equals("[2]")){
			center_x = 106;
			center_y = 680;
		}
		else if (tarGoal.equals("[3]")){
			center_x = 978;
			center_y = 172;
		}
		else if (tarGoal.equals("[4]")){
			center_x = 983;
			center_y = 673;
		}
		else{
			center_x = -99999;
			center_y = -99999;		
		}
		
		double r = Math.sqrt(
				Math.pow(Integer.parseInt((String)al_1d.get(xcol)) - center_x, 2) 
				+ Math.pow(Integer.parseInt((String)al_1d.get(ycol)) - center_y, 2)
				);
				
		return r;		
	}
	
	public static double get_theta_of_clickTarget(ArrayList al_1d, String tarGoal, int xcol, int ycol){
		int center_x, center_y;		
		
		if (tarGoal.equals("[1]")){
			center_x = 101;
			center_y = 161;
		}
		else if (tarGoal.equals("[2]")){
			center_x = 106;
			center_y = 680;
		}
		else if (tarGoal.equals("[3]")){
			center_x = 978;
			center_y = 172;
		}
		else if (tarGoal.equals("[4]")){
			center_x = 983;
			center_y = 673;
		}
		else{
			center_x = -99999;
			center_y = -99999;		
		}
		
		int x = Integer.parseInt((String)al_1d.get(xcol)) - center_x;
		int y = Integer.parseInt((String)al_1d.get(ycol)) - center_y;
		
		double theta = -999;
    	if ((x > 0) && (y > 0))
    		theta = Math.atan((double)y/(double)x);
    	else if ((x > 0) && (y < 0))
    		theta = 2*Math.PI + Math.atan((double)y/(double)x);
    	else if((x < 0) && (y >= 0))
    		theta = Math.atan((double)y/(double)x) + Math.PI;
    	else if((x < 0) && (y < 0))
    		theta = Math.atan((double)y/(double)x) + Math.PI;
    	else if((x == 0) && (y > 0))    		
    		theta = Math.PI/2;
    	else if((x == 0) && (y < 0))    		
    		theta = -Math.PI/2;
    	else if((x == 0) && (y == 0))    		
    		theta = 0;
    	
		return theta;		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
