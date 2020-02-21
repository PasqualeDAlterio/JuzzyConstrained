package additional_IT2MF;

import intervalType2.sets.IntervalT2MF_Prototype;
import type1.sets.T1MF_Interface;

/**
 * IT2 MF where the upper and lower boundary can have arbitrary shapes. No coherency checks are carried out to ensure that the upperbound
 * MF is always above the lowerbound
 * @author Pasquale
 */
public class IntervalT2MF_Generic extends IntervalT2MF_Prototype{
    
    public IntervalT2MF_Generic(String name, T1MF_Interface upperbound, T1MF_Interface lowerbound)
    {
        super(name);
        this.lMF = lowerbound;
        this.uMF = upperbound;
        this.support = upperbound.getSupport();
    }
    
}