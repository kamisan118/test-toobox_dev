package numerical;

import numerical.inferfaces.Function;

/*Numerical Differentiator Using Central Difference Method
downloaded from http://www.dreamincode.net/code/snippet6333.htm

//EXAMPLE USAGE
public class Example
{
  public static void main(String[] args)
  {
      // Create object passing user defined value for h
	CentralDifference d = new CentralDifference(0.001);
      
      // Create target function object
	Function func = new Function()
	{
	    public double eval(double x)
	    {
		return Math.pow(x, 5);
	    }
	};
      
      // Return first derivative where x = 3.2, using internal
      // optimization for h
	System.out.println(d.deriveFirst(func, 3.2, true));
  }
}*/

//Methods to estimate the first, second and third
//derivatives of a function using the
//central divided difference formulas.

public class CentralDifference
{
  // Value to represent the interval being used
  // in the calculations.

  // Usually the smaller this value, the better
  // however this can introduce truncation errors
  // and very unstable results with higher order
  // derivatives.
  private double h;

  // Create new object, setting initial value for h
  public CentralDifference()
  {
	h = 0.0001;
  }

  // Create object with user defined value for h
  public CentralDifference(double h)
  {
	this.h = h;
  }

  // Get the value of h
  public double getH()
  {
	return h;
  }

  // Set the value of h
  public void setH(double h)
  {
	this.h = h;
  }

  // Optimize the value of h depending on the point of
  // differentiation. Takes the sqrt of very small value
  // and transfers into and out of memory to make sure
  // value can be represented in binary.

  // Can only be used with first derivative as others
  // get very unstable and truncate easily with lower
  // values of h
  private double optimizeH(double x)
  {
	double h = Math.sqrt(2.2E-16) * x;
	double temp = x + h;
	return temp - x;
  }

  // Estimate the first derivative of the Function f at the point x.
  // If optimize is true, use internal optimization, otherwise return
  // default value of h
  public double deriveFirst(Function f, double x, boolean optimize)
  {
	double h = optimize ? optimizeH(x) : this.h;

	return (f.eval(x + h) - f.eval(x - h)) / (2 * h);
  }

  // Estimate the second derivative of the Function f at the point x.
  public double deriveSecond(Function f, double x)
  {
	return (f.eval(x + h) - 2 * f.eval(x) + f.eval(x - h)) / (h * h);
  }

  // Estimate the third derivative of the Function f at the point x.
  public double deriveThird(Function f, double x)
  {
	return (f.eval(x + 2 * h) - 3 * f.eval(x + h) + 3 * f.eval(x) - f.eval(x - h)) / (h * h * h);
  }
}


