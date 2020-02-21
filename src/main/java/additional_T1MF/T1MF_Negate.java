package additional_T1MF;

import generic.Tuple;
import type1.sets.T1MF_Interface;
import type1.sets.T1MF_Prototype;

/**
 * T1MF modeling the negation of another T1MF (e.g. "not low")
 * @author Pasquale
 */
public class T1MF_Negate extends T1MF_Prototype {
    
    private final T1MF_Interface toNegate;
    
    public T1MF_Negate(String name, T1MF_Interface to_negate, Tuple support)
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
        if(x>=support.getLeft()&&x<=support.getRight())
            return 1-toNegate.getFS(x);
        return 0;
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
    
    public T1MF_Negate clone()
    {
        return new T1MF_Negate(name, toNegate, support);
    }
    
}
