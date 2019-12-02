/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cit2;

import generic.Tuple;
import type1.sets.T1MF_Interface;
import type1.sets.T1MF_Prototype;

/**
 * Class modelling the boundary of the FOU of a CIT2 FS.
 * @author Pasquale
 */
public class FOUBoundaryCIT2 extends T1MF_Prototype{
    
    //First and last embedded sets in the FOU
    private T1MF_Interface firstEmbeddedSet, lastEmbeddedSet;
    //The generator set used to generate the set
    private CIT2_Generator generatorSet;
    //Left and right shifting applied to the generator set to obtain the FOU
    private double leftShifting, rightShifting;
    //Boundary type to specify if it is the lowerbound or the upperbound MF
    private CIT2_Boundary_Type type;
    
    /**
     * Constructor of the boundary
     * @param name name of the boundary
     * @param type specifies if it is a lowerbound or an upperbound
     * @param first_embedded_set first embedded set in the FOU
     * @param last_embedded_set last embedded set in the FOU
     * @param generator_set the generator set used to generate the set
     * @param left_shifting maximum left shifting of the generator set to generate the FOU
     * @param right_shifting maximum right shifting of the generator set to generate the FOU
     */
    public FOUBoundaryCIT2(String name, CIT2_Boundary_Type type, T1MF_Interface first_embedded_set, T1MF_Interface last_embedded_set, CIT2_Generator generator_set, double left_shifting, double right_shifting)
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
        //Each tuple represents a range. Each range has been obtained by shifting a minimum/maximum point of the generator set using the displacement set [-a, b].
        //For a minimum/maximum point k, its Tuple will be [k-a, b+b]; if x is within the Tuple of k, it's membership degree is the same one of k
        Tuple[] min_max_shifted_values;
        double shifted_min_max_md;
        //In the lowerbound, the points of minimum are required; in the upperbound the points of maximum are required
        if(type==CIT2_Boundary_Type.LOWERBOUND)
            min_max_shifted_values=generatorSet.getMinPoints();
        else
            min_max_shifted_values=generatorSet.getMaxPoints();
        //Checks if x is one of the minimum/maximum points or one of its shifts
        shifted_min_max_md=inIntervals(x, min_max_shifted_values);
        //If x is one of the shifted minimum/maximum points, return the membership degree of that minimum/maximum shifted point
        if(shifted_min_max_md!=-1)
            return shifted_min_max_md;
        //When evaluating the lowerbound/upperbound and x is not whitin any of the "shifted minimum/maximum points", its membership degree is given by the minimum/maximum membership degree
        //of the leftmost and rightmost AES
        if(type==CIT2_Boundary_Type.LOWERBOUND)
            return Math.min(firstEmbeddedSet.getFS(x), lastEmbeddedSet.getFS(x));
        return Math.max(firstEmbeddedSet.getFS(x), lastEmbeddedSet.getFS(x));
    }
    
    /**
     * Checks whether x belongs to one of the intervals. If it does, it returns its membership degree w.r.t the boundary function; it returns -1 otherwise
     * @param x The value to check
     * @param intervals The intervals that x could belong to
     * @return The membership degree of x w.r.t the boundary function, if it belongs to one of the intervals, -1 otherwise
     */
    public double inIntervals(double x, Tuple[] intervals)
    {
        if(intervals==null)
            return -1;
        for(Tuple current_interval : intervals)
        {
            if(x>=current_interval.getLeft()-leftShifting&&x<=current_interval.getRight()+rightShifting)
                return generatorSet.getFS(current_interval.getLeft());
        }
        return -1;
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
    
        
    public FOUBoundaryCIT2 clone()
    {
        return new FOUBoundaryCIT2(name, type, firstEmbeddedSet.clone(), lastEmbeddedSet.clone(), generatorSet.clone(), leftShifting, rightShifting);
    }
    
}

