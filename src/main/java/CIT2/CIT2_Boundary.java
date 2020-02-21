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
    
    //First and last embedded sets in the FOU
    private final CIT2_Generator firstEmbeddedSet, lastEmbeddedSet;
    //The generator set used to generate the set
    private final CIT2_Generator generatorSet;
    //Left and right shifting applied to the generator set to obtain the FOU
    private final double leftShifting, rightShifting;
    //Boundary type to specify if it is the lowerbound or the upperbound MF
    private final CIT2_Boundary_Type type;
    
    /**
     * Constructor of the boundary
     * @param name Name of the boundary
     * @param type Specifies if it is a lowerbound or an upperbound
     * @param first_embedded_set First embedded set in the FOU
     * @param last_embedded_set Last embedded set in the FOU
     * @param generator_set The generator set used to generate the set
     * @param left_shifting Maximum left shifting of the generator set to generate the FOU (i.e. left endpoint of the displacement set)
     * @param right_shifting Maximum right shifting of the generator set to generate the FOU (i.e. right endpoint of the displacement set)
     */
    public CIT2_Boundary(String name, CIT2_Boundary_Type type, CIT2_Generator first_embedded_set, CIT2_Generator last_embedded_set, CIT2_Generator generator_set, double left_shifting, double right_shifting)
    {
        super(name);
        this.type=type;
        firstEmbeddedSet=first_embedded_set;
        lastEmbeddedSet=last_embedded_set;
        generatorSet=generator_set;
        leftShifting=left_shifting;
        rightShifting=right_shifting;
        support=(new Tuple(first_embedded_set.getSupport().getLeft(), last_embedded_set.getSupport().getRight()));
        if(support.getLeft()>support.getRight())
            System.out.println("ERROR");
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
            partial_result=Math.min(firstEmbeddedSet.getFS(x), lastEmbeddedSet.getFS(x));
            if(min_max_point!=-1)
                return Math.min(partial_result, min_max_point);
            return partial_result;

        }
        partial_result=Math.max(firstEmbeddedSet.getFS(x), lastEmbeddedSet.getFS(x));
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
            if(current_interval.getLeft()-leftShifting<=x&&current_interval.getRight()+rightShifting>=x)
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
        return new CIT2_Boundary(name, type, firstEmbeddedSet.clone(), lastEmbeddedSet.clone(), generatorSet, leftShifting, rightShifting);
    }
    
}

