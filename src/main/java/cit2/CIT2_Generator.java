/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cit2;

import generic.Tuple;
import type1.sets.T1MF_Interface;

/**
 *
 * @author psxpd2admin
 */
public interface CIT2_Generator extends T1MF_Interface, Cloneable{
    
    //Max intervals (e.g. upper base of a trapezoid)
    public abstract Tuple[] getMaxPoints();
    //Min points with a membership greater than 0
    public abstract Tuple[] getMinPoints();

    
    //Returns the current MF shifted by a shifting factor
    public abstract CIT2_Generator shiftFunction(String name, double shifting_factor);
    @Override
    public CIT2_Generator clone();
}
