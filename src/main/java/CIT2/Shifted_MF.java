/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cit2;

import generic.Tuple;
import type1.sets.T1MF_Prototype;
import type1.sets.T1MF_Interface;

/**
 * Represents a shifted membership function
 * @author psxpd2admin
 */
public class Shifted_MF extends T1MF_Prototype{
    //The membership function to shift
    private final T1MF_Interface originalMF;
    //The shifting factor
    private final double shiftingFactor;
    
    /**
     * The constructor of the shifted membership function
     * @param name The name of the shifted function
     * @param original_MF The membership function to shift
     * @param shifting_factor The real number representing the shifting factor (i.e. how much the function must be shifted)
     */
    
    public Shifted_MF(String name, T1MF_Interface original_MF, double shifting_factor)
    {
        super(name);
        originalMF=original_MF;
        shiftingFactor=shifting_factor;
        support=new Tuple(originalMF.getSupport().getLeft()+shiftingFactor, originalMF.getSupport().getRight()+shiftingFactor);
    }
    
    public Shifted_MF clone()
    {
        return new Shifted_MF(name, originalMF.clone(), shiftingFactor);
    }
    
    @Override
    public Tuple getSupport()
    {
        return support;
    }
    
    @Override
    public double getFS(double x)
    {
        //Use the shifting factor to compute the membership degree
        return originalMF.getFS(x-shiftingFactor);
    }
    
    @Override
    public double getPeak()
    {
        return originalMF.getPeak()+shiftingFactor;
    }
    
    @Override
    public Tuple getAlphaCut(double alpha) 
    {
        throw new UnsupportedOperationException("Not supported yet.");        
    } 
    
    @Override
    public int compareTo(Object a)
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    
}
