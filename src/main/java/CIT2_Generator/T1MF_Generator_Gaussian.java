package CIT2_Generator;

import generic.Tuple;
import java.util.ArrayList;
import java.util.Collection;
import tools.Interval;
import type1.sets.T1MF_Gaussian;
import type1.sets.T1MF_Prototype;

/**
 *
 * @author Pasquale
 */
public class T1MF_Generator_Gaussian extends CIT2_Generator_Prototype{
    
    private T1MF_Gaussian gaussian;
    
    public T1MF_Generator_Gaussian(String name, double mean, double spread)
    {
        super(name, new T1MF_Gaussian(name, mean, spread));
    }
    
    protected void typeCastT1MF()
    {
        gaussian=(T1MF_Gaussian)T1MF;        
    }
    
    protected ArrayList<Interval> computeMinPoints()
    {
        return null;
    }
    
    protected ArrayList<Interval> computeMaxPoints()
    {
        ArrayList<Interval> max_points=new ArrayList<>();
        max_points.add(new Interval(gaussian.getMean()));
        return max_points;
    }
    
    protected ArrayList<Interval> computeRightShoulderMinPoints()
    {
        return null;
    }
    
    protected ArrayList<Interval> computeRightShoulderMaxPoints()
    {
        ArrayList<Interval> max_points=new ArrayList<>();
        max_points.add(new Interval(gaussian.getMean(), Double.POSITIVE_INFINITY));
        return max_points;
    }
    
    protected ArrayList<Interval> computeLeftShoulderMinPoints()
    {
        return null;
    }
    
    protected ArrayList<Interval> computeLeftShoulderMaxPoints()
    {
        ArrayList<Interval> max_points=new ArrayList<>();
        max_points.add(new Interval(Double.NEGATIVE_INFINITY, gaussian.getMean()));
        return max_points;
    }
    
    @Override
    public T1MF_Generator_Gaussian shiftFunction(String name, double shifting_factor)
    {
        T1MF_Generator_Gaussian result=new T1MF_Generator_Gaussian(name, gaussian.getMean()+shifting_factor, gaussian.getSpread());
        setShiftedSupportSet(T1MF, result);
        return result;
    }
    
    @Override
    public T1MF_Generator_Gaussian clone()
    {
        T1MF_Generator_Gaussian result= new T1MF_Generator_Gaussian(name, gaussian.getMean(), gaussian.getSpread());
        result.setLeftShoulder(isLeftShoulder);
        result.setRightShoulder(isRightShoulder);
        result.setSupport(support.clone());
        return result;
    }

}
