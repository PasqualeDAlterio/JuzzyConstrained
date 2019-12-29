/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CIT2_Generator;

import java.util.ArrayList;
import tools.Interval;
import type1.sets.T1MF_Gauangle;

/**
 *
 * @author User
 */
public class T1MF_Generator_Gauangle extends CIT2_Generator_Prototype{
    
    private T1MF_Gauangle gauangle;
    
    public T1MF_Generator_Gauangle(String name, double start, double center, double end)
    {
        super(name, new T1MF_Gauangle(name, start, center, end));
    }
    
    protected void typeCastT1MF()
    {
        gauangle=(T1MF_Gauangle)T1MF;
    }
    
    protected ArrayList<Interval> computeMinPoints()
    {
        ArrayList<Interval> min_points=new ArrayList<>();
        min_points.add(new Interval(gauangle.getStart()));
        min_points.add(new Interval(gauangle.getEnd()));
        return min_points;
    }
    
    protected ArrayList<Interval> computeMaxPoints()
    {
        ArrayList<Interval> max_points=new ArrayList<>();
        max_points.add(new Interval(gauangle.getPeak()));
        return max_points;
    }
    
    protected ArrayList<Interval> computeRightShoulderMinPoints()
    {
        ArrayList<Interval> min_points=new ArrayList<>();
        min_points.add(new Interval(gauangle.getStart()));
        return min_points;
    }
    
    protected ArrayList<Interval> computeRightShoulderMaxPoints()
    {
        ArrayList<Interval> max_points=new ArrayList<>();
        max_points.add(new Interval(gauangle.getPeak(), Double.POSITIVE_INFINITY));
        return max_points;  
    }
    
    protected ArrayList<Interval> computeLeftShoulderMinPoints()
    {
        ArrayList<Interval> min_points=new ArrayList<>();
        min_points.add(new Interval(gauangle.getEnd()));
        return min_points;
    }
    
    protected ArrayList<Interval> computeLeftShoulderMaxPoints()
    {
        ArrayList<Interval> max_points=new ArrayList<>();
        max_points.add(new Interval(Double.NEGATIVE_INFINITY, gauangle.getPeak()));
        return max_points; 
    }
    
//    @Override
//    public T1MF_Generator_Gauangle clone()
//    {
//        T1MF_Generator_Gauangle clone=new T1MF_Generator_Gauangle(getName(), gauangle.getStart(), gauangle.getPeak(), gauangle.getEnd());
//        clone.setLeftShoulder(isLeftShoulder);
//        clone.setRightShoulder(isRightShoulder);
//        clone.setSupport(this.support.clone());
//        return clone;
//    }

    @Override
    public CIT2_Generator shiftFunction(String name, double shifting_step)
    {
        CIT2_Generator shifted_function=new T1MF_Generator_Gauangle(name, gauangle.getStart()+shifting_step, gauangle.getPeak()+shifting_step, gauangle.getEnd()+shifting_step);
        setShiftedSupportSet(gauangle, shifted_function);
        return shifted_function;
    }
}
