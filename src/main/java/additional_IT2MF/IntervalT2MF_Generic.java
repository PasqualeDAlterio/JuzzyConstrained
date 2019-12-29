/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package additional_IT2MF;

import intervalType2.sets.IntervalT2MF_Prototype;
import type1.sets.T1MF_Interface;

/**
 *
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