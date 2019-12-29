/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CIT2_Generator;

import generic.Tuple;
import java.util.Collection;
import java.util.TreeSet;
import tools.Interval;
import type1.sets.T1MF_Interface;

/**
 *
 * @author psxpd2admin
 */
public interface CIT2_Generator extends T1MF_Interface, Cloneable{
    
    //Max intervals (e.g. upper base of a trapezoid)
    public Collection<Interval> getMaxPoints();
    //Min points with a membership greater than 0
    public Collection<Interval> getMinPoints();

    
    //Returns the current MF shifted by a shifting factor. It is necessary to generate the AESs
    public CIT2_Generator shiftFunction(String name, double shifting_factor);
}
