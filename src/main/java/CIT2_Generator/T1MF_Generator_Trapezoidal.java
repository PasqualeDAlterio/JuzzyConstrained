package CIT2_Generator;

import generic.Tuple;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import tools.Interval;
import type1.sets.T1MF_Trapezoidal;

/**
 *
 * @author Pasquale
 */
public class T1MF_Generator_Trapezoidal extends CIT2_Generator_Prototype{
        
    private T1MF_Trapezoidal trapezoid;
    
    public T1MF_Generator_Trapezoidal(String name, double a, double b, double c, double d)
    {
        this(name, new double[]{a, b, c, d});
    }
    
    public T1MF_Generator_Trapezoidal(String name, double[] parameters, double[] yLevels)
    {
        super(name, new T1MF_Trapezoidal(name, parameters, yLevels));
    }
    
    public T1MF_Generator_Trapezoidal(String name, double[] parameters)
    {
        this(name, parameters, new double[]{1,1});
    }
    
    protected void typeCastT1MF()
    {
        trapezoid=(T1MF_Trapezoidal)T1MF;            
    }
    
    @Override
    protected ArrayList<Interval> computeMinPoints()
    {
        ArrayList<Interval> min_points=new ArrayList<>();
        min_points.add(new Interval(trapezoid.getA()));
        min_points.add(new Interval(trapezoid.getD()));
        return min_points;
    }
    
    @Override
    protected ArrayList<Interval> computeMaxPoints()
    {
        ArrayList<Interval> max_points=new ArrayList<>();
        max_points.add(new Interval(trapezoid.getB(), trapezoid.getC()));
        return max_points;
    }
    
    protected ArrayList<Interval> computeRightShoulderMinPoints()
    {
        ArrayList<Interval> min_points=new ArrayList<>();
        min_points.add(new Interval(trapezoid.getA()));
        return min_points;        
    }
    
    protected ArrayList<Interval> computeRightShoulderMaxPoints()
    {
        ArrayList<Interval> max_points=new ArrayList<>();
        max_points.add(new Interval(trapezoid.getB(), Double.POSITIVE_INFINITY));
        return max_points;
    }
    
    protected ArrayList<Interval> computeLeftShoulderMinPoints()
    {
        ArrayList<Interval> min_points=new ArrayList<>();
        min_points.add(new Interval(trapezoid.getD()));
        return min_points;
    }
    
    protected ArrayList<Interval> computeLeftShoulderMaxPoints()
    {
        ArrayList<Interval> max_points=new ArrayList<>();
        max_points.add(new Interval(Double.NEGATIVE_INFINITY, trapezoid.getC()));    
        return max_points;
    }
    
    @Override
    public T1MF_Generator_Trapezoidal clone()
    {
        T1MF_Generator_Trapezoidal result= new T1MF_Generator_Trapezoidal(name, trapezoid.getParameters(), trapezoid.getyLevels().clone());
        result.setLeftShoulder(isLeftShoulder);
        result.setRightShoulder(isRightShoulder);
        result.setSupport(this.support.clone());
        return result;
    }
    
    @Override
    public T1MF_Generator_Trapezoidal shiftFunction(String name, double shifting_factor)
    {
        T1MF_Generator_Trapezoidal result= new T1MF_Generator_Trapezoidal(name, new double[]{trapezoid.getA()+shifting_factor, trapezoid.getB()+shifting_factor, trapezoid.getC()+shifting_factor, trapezoid.getD()+shifting_factor}, trapezoid.getyLevels());
        setShiftedSupportSet(T1MF, result);
        return result;
    }
    
}
