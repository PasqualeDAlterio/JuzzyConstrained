package CIT2;

import CIT2_Generator.CIT2_Generator;
import generic.Tuple;
import java.util.Collection;
import tools.Interval;
import type1.sets.T1MF_Prototype;

/**
 * Class modelling the boundary of the FOU of a CIT2 FS.
 * @author Pasquale
 */
public class CIT2_Boundary extends T1MF_Prototype{

    //Leftmost and rightmost embedded sets in the FOU
    private final CIT2_Generator leftmostAES, rightmostAES;
    //The generator set used to generate the set
    private final CIT2_Generator generatorSet;
    //The displacement interval used to generate the CIT2 set
    private final Tuple displacementInterval;
    //Boundary type to specify if it is the lowerbound or the upperbound MF
    private final CIT2_Boundary_Type type;
    
    /**
     * Constructor of the boundary
     * @param name Name of the boundary
     * @param type Specifies if it is a lowerbound or an upperbound
     * @param leftmost_aes Leftmost embedded set in the FOU
     * @param rightmost_aes Rightmost embedded set in the FOU
     * @param generator_set The generator set used to generate the set
     * @param displacement_interval The displacement interval used to generate the set
     */
    public CIT2_Boundary(String name, CIT2_Boundary_Type type,  CIT2_Generator generator_set, CIT2_Generator leftmost_aes, CIT2_Generator rightmost_aes, Tuple displacement_interval)
    {
        super(name);
        this.type=type;
        leftmostAES =leftmost_aes;
        rightmostAES =rightmost_aes;
        generatorSet=generator_set;
        displacementInterval=displacement_interval;
        support=(new Tuple(leftmost_aes.getSupport().getLeft(), rightmost_aes.getSupport().getRight()));
    }

    /**
     * Computes the membership degree of the value x
     * @param x A value in the UOD
     * @return The double representing the membership degree of x
     */
    @Override
    public double getFS(double x) 
    {
        //The set of intervals represents the maximum (if this object is an upper boundary) or the minimum (if it is a lower boundary) points of the generator set
        //These points plus the generator set itself are sufficient to determine the boundary functions of the CIT2 FS
        Collection<Interval> intervals_to_check;
        double min_max_point, partial_result;
        //In the lowerbound, the points of minimum are required; in the upperbound the points of maximum are required
        if(type==CIT2_Boundary_Type.LOWERBOUND)
            intervals_to_check=generatorSet.getMinPoints();
        else
            intervals_to_check=generatorSet.getMaxPoints();
        //Checks if the membership degree of x is determined by one of the minimum/maximum points
        min_max_point=inIntervals(x, intervals_to_check, type);
        if(type==CIT2_Boundary_Type.LOWERBOUND)
        {
            //The lowerbound is given by the min membership degree of the first and last ES and the potential min points in the range [x-leftShifting; x+rigthShifting]
            partial_result=Math.min(leftmostAES.getFS(x), rightmostAES.getFS(x));
            if(min_max_point!=-1)
                return Math.min(partial_result, min_max_point);
            return partial_result;

        }
        partial_result=Math.max(leftmostAES.getFS(x), rightmostAES.getFS(x));
        if(min_max_point!=-1)
            //The upperbound is given by the max membership degree of the first and last ES and the potential max points in the range [x-leftShifting; x+rigthShifting]
            return Math.max(partial_result, min_max_point);
        return partial_result;
    }
    
    /**
     * Checks whether x belongs to one of the intervals. If it does, it returns its membership degree w.r.t the boundary function; it returns -1 otherwise
     * @param x The value to check
     * @param intervals The intervals that x could belong to
     * @return The membership degree of x w.r.t the boundary function, if it belongs to one of the intervals, -1 otherwise
     */
    private double inIntervals(double x, Collection<Interval> intervals, CIT2_Boundary_Type type)
    {
        if(intervals==null)
            return -1;
        double final_membership_value=-1, current_membership_degree;
        for(Interval current_interval : intervals)
        {
            if(current_interval.getLeft()+displacementInterval.getLeft()<=x&&current_interval.getRight()+displacementInterval.getRight()>=x)
            {
                current_membership_degree=generatorSet.getFS(current_interval.getLeft());
                if(type==CIT2_Boundary_Type.LOWERBOUND&&(current_membership_degree<final_membership_value||final_membership_value==-1))
                    final_membership_value=current_membership_degree;
                if(type==CIT2_Boundary_Type.UPPERBOUND&&(current_membership_degree>final_membership_value||final_membership_value==-1))
                    final_membership_value=current_membership_degree;
            }
        }
        return final_membership_value;
    }
            
    @Override
    public Tuple getAlphaCut(double alpha) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
            
    @Override
    public double getPeak() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
            
    @Override
    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
        
    public CIT2_Boundary clone()
    {
        return new CIT2_Boundary(name, type, leftmostAES.clone(), rightmostAES.clone(), generatorSet.clone(), displacementInterval.clone());
    }
    
}

