package tools;

import generic.Tuple;
import java.text.DecimalFormat;

/**
 *
 * @author Pasquale
 */
public class Interval extends Tuple{
    
    public Interval(double left_endpoint, double right_endpoint)
    {
        super(left_endpoint, right_endpoint);
        if(left_endpoint>right_endpoint)
        {
            setLeft(right_endpoint);
            setRight(left_endpoint);
        }
    }
    
    public Interval(double point)
    {
        super(point, point);
    }
    
    public String toString()
    {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return ("["+df.format(getLeft())+","+df.format(getRight())+"]");
    }
    
    public boolean inInterval(double x)
    {
        return (x>=getLeft()&&x<=getRight());
    }

}
