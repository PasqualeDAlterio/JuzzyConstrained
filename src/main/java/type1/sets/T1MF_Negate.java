/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package type1.sets;

import cit2.CIT2_Generator;
import cit2.CIT2_Generator_Prototype;
import generic.Tuple;

/**
 *
 * @author User
 */
public class T1MF_Negate extends CIT2_Generator_Prototype{
    
    private CIT2_Generator toNegate;
    
    public T1MF_Negate(String name, CIT2_Generator to_negate, Tuple support)
    {
        super(name);
        toNegate=to_negate;
        this.support=support;
    }
    
    
    public T1MF_Negate clone()
    {
        return new T1MF_Negate(name, toNegate.clone(), support.clone());
    }
    
    public double getRightmostMaxPoint()
    {
        return toNegate.getMinPoints()[toNegate.getMinPoints().length].getRight();
    }
    
    public double getLeftmostMaxPoint()
    {
        return toNegate.getMinPoints()[0].getLeft();
    }
    
    public Tuple[] getMinPoints()
    {
        return toNegate.getMaxPoints();
    }
    
    public Tuple[] getMaxPoints()
    {
        return toNegate.getMinPoints();
    }
    
     @Override
    public CIT2_Generator shiftFunction(String name, double shifting_factor)
    {
        return new T1MF_Negate(name, toNegate.shiftFunction(name, shifting_factor), support);
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
