/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package additional_T1MF;

import CIT2_Generator.CIT2_Generator;
import generic.Tuple;
import type1.sets.T1MF_Prototype;

/**
 *
 * @author User
 */
public class T1MF_Negate extends T1MF_Prototype {
    
    private CIT2_Generator toNegate;
    
    public T1MF_Negate(String name, CIT2_Generator to_negate, Tuple support)
    {
        super(name);
        toNegate=to_negate;
        this.support=support;
    }
    
    
    @Override
    public double getPeak()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public double getFS(double x)
    {
        return 1-toNegate.getFS(x);
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
