/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CIT2_Generator;

import generic.Tuple;
import java.util.ArrayList;
import java.util.TreeSet;
import tools.Interval;
import type1.sets.T1MF_Prototype;
import type1.sets.T1MF_Triangular;

/**
 *
 * @author User
 */
public class T1MF_Generator_Triangular extends CIT2_Generator_Prototype{
    
    private T1MF_Triangular triangle;
    
    public T1MF_Generator_Triangular(String name, double start, double peak, double end)
    {
        super(name, new T1MF_Triangular(name, start, peak, end));
        //System.out.println(triangle.isLeftShoulder()+"2 "+start+" "+peak);
    }
    
    protected void typeCastT1MF()
    {
        triangle=(T1MF_Triangular)T1MF;            
    }
    
    @Override
    protected ArrayList<Interval> computeMinPoints()
    {
        ArrayList<Interval> min_points=new ArrayList<>();
        min_points.add(new Interval(triangle.getStart()));
        min_points.add(new Interval(triangle.getEnd()));
        return min_points;
    }
    
    @Override
    protected ArrayList<Interval> computeMaxPoints()
    {
        ArrayList<Interval> max_points=new ArrayList<>();
        max_points.add(new Interval(triangle.getPeak()));
        return max_points;
    }
    
    protected ArrayList<Interval> computeRightShoulderMinPoints()
    {
        ArrayList<Interval> min_points=new ArrayList<>();
        min_points.add(new Interval(triangle.getStart()));
        return min_points;        
    }
    
    protected ArrayList<Interval> computeRightShoulderMaxPoints()
    {
        ArrayList<Interval> max_points=new ArrayList<>();
        max_points.add(new Interval(triangle.getPeak(), Double.POSITIVE_INFINITY));
        return max_points;
    }
    
    protected ArrayList<Interval> computeLeftShoulderMinPoints()
    {
        ArrayList<Interval> min_points=new ArrayList<>();
        min_points.add(new Interval(triangle.getEnd()));
        return min_points;
    }
    
    protected ArrayList<Interval> computeLeftShoulderMaxPoints()
    {
        ArrayList<Interval> max_points=new ArrayList<>();
        max_points.add(new Interval(Double.NEGATIVE_INFINITY, triangle.getPeak()));    
        return max_points;
    }   
    
//    public T1MF_Generator_Triangular clone()
//    {
//        T1MF_Generator_Triangular result= new T1MF_Generator_Triangular(name, triangle.getStart(), triangle.getPeak(), triangle.getEnd());
//        result.setLeftShoulder(true);
//        result.setRightShoulder(true);
//        result.setSupport(support.clone());
//        return result;
//    }
    
    @Override
    public CIT2_Generator shiftFunction(String name, double shifting_factor)
    {
        T1MF_Generator_Triangular result= new T1MF_Generator_Triangular(name, triangle.getStart()+shifting_factor, triangle.getPeak()+shifting_factor, triangle.getEnd()+shifting_factor);
        setShiftedSupportSet(T1MF, result);
        return result;
    }
}
