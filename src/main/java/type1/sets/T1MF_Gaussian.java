/*
 * T1MF_Gaussian.java
 *
 * Author: Christian Wagner
 * Copyright 2012 Christian Wagner All Rights Reserved.
 */

package type1.sets;

import cit2.CIT2_Generator;
import cit2.CIT2_Generator_Prototype;
import generic.Tuple;

/**
 * Class for Gaussian Type-1 Fuzzy Membership Functions.
 * @author Christian Wagner
 */
public class T1MF_Gaussian extends CIT2_Generator_Prototype
{
    private double mean;
    private double spread;
    private Tuple[] maxPoint;

    /**
     * Constructor setting the parameters for the Gaussian Membership Function.
     * The support is defined as 4 x spread to the left and to the right of the mean.
     * @param name
     * @param mean Mean
     * @param spread Spread / Standard Deviation
     */
    public T1MF_Gaussian(String name, double mean, double spread)
    {
        super(name);
        this.mean = mean;
        this.spread = spread;
        support = new Tuple((mean- 4*spread), mean+4*spread);
        maxPoint=new Tuple[]{new Tuple(mean, mean)};
    }
    
    public T1MF_Gaussian clone()
    {
        T1MF_Gaussian result= new T1MF_Gaussian(name, mean, spread);
        result.setLeftShoulder(isLeftShoulder);
        result.setRightShoulder(isRightShoulder);
        return result;
    }

    @Override
    public double getFS(double x) 
    {
        if(x>=this.getSupport().getLeft() && x<=this.getSupport().getRight())
        {
            if(isLeftShoulder && x<=mean) return 1.0;
            if(isRightShoulder && x>=mean) return 1.0;
            return Math.exp(-0.5*Math.pow(((x-mean)/spread),2));
        }
        else
            return 0.0;
    }	

    @Override
    public Tuple getSupport()
    {
        return support;
    }
    
    @Override
    public double getPeak()
    {
        return mean;
    }
    
    @Override
    public Tuple[] getMaxPoints()
    {
        return maxPoint;
    }
    
    @Override
    public Tuple[] getMinPoints()
    {
        return null;
    }

    public double getSpread()
    {
        return spread;
    }

    public double getMean() {
        return mean;
    }
    
    @Override
    public String toString()
    {
        String s = this.name+" - Gaussian with mean "+ this.mean+", standard deviation: "+this.spread;
        if(isLeftShoulder) s+=" (LeftShoulder)";
        if(isRightShoulder) s+=" (RightShoulder)";
        return s;
    }

    @Override
    public Tuple getAlphaCut(double alpha) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(Object o) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public CIT2_Generator shiftFunction(String name, double shifting_factor)
    {
        T1MF_Gaussian result=new T1MF_Gaussian(name, mean+shifting_factor, spread);
        if(isLeftShoulder)
        {
            //If it is a left shoulder, conceptually the support doesn't have an end on the left side, so it must be keept unchanged
            result.setLeftShoulder(true);
            result.setSupport(new Tuple(this.support.getLeft(), result.getSupport().getRight()));
        }
        if(isRightShoulder)
        {
            result.setRightShoulder(true);
            result.setSupport(new Tuple(result.getSupport().getLeft(), this.getSupport().getRight()));
        }
        return result;
    }
    
}
