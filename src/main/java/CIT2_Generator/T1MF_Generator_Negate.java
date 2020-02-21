package CIT2_Generator;

import generic.Tuple;
import java.util.Collection;
import tools.Interval;
import type1.sets.T1MF_Prototype;

/**
 *
 * @author Pasquale
 */
public class T1MF_Generator_Negate extends T1MF_Prototype implements CIT2_Generator{
    
    private CIT2_Generator toNegate;
    
    public T1MF_Generator_Negate(String name, CIT2_Generator to_negate)
    {
        super(name);
        toNegate=to_negate;
        this.support=to_negate.getSupport();
    }
    
    public T1MF_Generator_Negate shiftFunction(String name, double value)
    {
        return new T1MF_Generator_Negate(name, toNegate.shiftFunction(name, value));
    }
    
    public Collection<Interval> getMinPoints()
    {
        return toNegate.getMaxPoints();
    }
    
    public Collection<Interval> getMaxPoints()
    {
        return toNegate.getMinPoints();
    }
    
    public double getPeak()
    {
        throw new UnsupportedOperationException("Not supported yet");
    }
    
    public Tuple getAlphaCut(double x)
    {
        throw new UnsupportedOperationException("Not supported yet");
    }
    
    public double getFS(double x)
    {
        return 1-toNegate.getFS(x);
    }
    
    public int compareTo(Object x)
    {
        throw new UnsupportedOperationException("Not supported yet");
    }
    
    public T1MF_Generator_Negate clone()
    {
        return new T1MF_Generator_Negate(name, toNegate.clone());
    }
}
