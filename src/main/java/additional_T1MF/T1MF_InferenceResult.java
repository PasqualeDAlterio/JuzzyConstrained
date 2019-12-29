/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package additional_T1MF;

import generic.Tuple;
import type1.sets.T1MF_Interface;
import type1.sets.T1MF_Prototype;


/**
 *
 * @author Pasquale
 */
public class T1MF_InferenceResult extends T1MF_Prototype {
    
    private final double firingStrength;
    private final T1MF_Interface consequent;
    
    public T1MF_InferenceResult clone()
    {
        return new T1MF_InferenceResult(name, consequent, firingStrength);
    }
    
    public T1MF_InferenceResult(String name, T1MF_Interface consequent, double firing_strength)
    {
        super(name);
        this.support=consequent.getSupport();
        this.firingStrength=firing_strength;
        this.consequent=consequent;
    }
    
    public double getFiringStrength()
    {
        return firingStrength;
    }
    
    @Override
    public double getPeak()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public double getFS(double x)
    {
        return Math.min(consequent.getFS(x), firingStrength);
    }
    
    @Override
    public Tuple getSupport()
    {
        return support;
    }
    
    @Override
    public int compareTo(Object o) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }  
    
    @Override
    public Tuple getAlphaCut(double alpha) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
