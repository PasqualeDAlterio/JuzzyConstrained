/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cit2;

import generic.Tuple;
import type1.sets.T1MF_Prototype;

/**
 * Class representing a generic CIT2 generator set
 * @author Pasquale
 */
public abstract class CIT2_Generator_Prototype extends T1MF_Prototype implements CIT2_Generator{
    
    //The points of minimum of the function
    private Tuple[] minPoints;
    //The points of maximum of the function
    private Tuple[] maxPoints;
    
    
    public CIT2_Generator_Prototype(String name)
    {
        super(name);
    }
    
    @Override
    public abstract CIT2_Generator_Prototype clone();
    
    @Override
    public Tuple[] getMinPoints()
    {
        return minPoints;
    }
    
    @Override
    public Tuple[] getMaxPoints()
    {
        return maxPoints;
    }
    
   @Override
    public Tuple getAlphaCut(double alpha) 
    {
        throw new UnsupportedOperationException("Not supported yet.");        
    }

    @Override
    public double getPeak() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    @Override
    public CIT2_Generator shiftFunction(String name, double shifting_step)
    {
        return null;
    }
    
    
}
