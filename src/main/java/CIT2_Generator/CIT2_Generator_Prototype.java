/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CIT2_Generator;

import generic.Tuple;
import java.util.ArrayList;
import java.util.Collection;
import tools.Interval;
import type1.sets.T1MF_Interface;
import type1.sets.T1MF_Prototype;

/**
 *
 * @author User
 */
public abstract class CIT2_Generator_Prototype extends T1MF_Prototype implements CIT2_Generator{
    
    protected Collection<Interval> minPoints, maxPoints;
    protected T1MF_Interface T1MF;
    
    public CIT2_Generator_Prototype(String name, T1MF_Interface t1_mf)
    {
        super(name);
        this.T1MF=t1_mf;
        typeCastT1MF();
        minPoints=computeMinPoints();
        maxPoints=computeMaxPoints();
        if(T1MF.isLeftShoulder())
            setLeftShoulder(true);
        if(T1MF.isRightShoulder())
            setRightShoulder(true);
    }
    
    public Tuple getSupport()
    {
        return T1MF.getSupport();
    }
    
    public void setSupport(Tuple support)
    {
        T1MF.setSupport(support);
    }
    
    public double getPeak()
    {
        return T1MF.getPeak();
    }
    
    public double getFS(double x)
    {
        return T1MF.getFS(x);
    }
    
    public Collection<Interval> getMinPoints()
    {
        return minPoints;
    }
    
    public Collection<Interval> getMaxPoints()
    {
        return maxPoints;
    }
    
    @Override
    public Tuple getAlphaCut(double alpha) 
    {
        return T1MF.getAlphaCut(alpha);
    }

    @Override
    public int compareTo(Object o) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    public void setLeftShoulder(boolean value)
    {
        if(value==isLeftShoulder)
            return;
        T1MF.setLeftShoulder(value);
        isLeftShoulder=value;
        if(value==true)
        {
            maxPoints=computeLeftShoulderMaxPoints();
            minPoints=computeLeftShoulderMinPoints();
            isRightShoulder=false;
            T1MF.setRightShoulder(false);
        }
        else
        {
            maxPoints=computeMaxPoints();
            minPoints=computeMinPoints();
        }
    }
    
    
    @Override
    public void setRightShoulder(boolean value)
    {
        if(value==isRightShoulder)
            return;
        T1MF.setRightShoulder(value);
        isRightShoulder=value;
        if(value==true)
        {
            maxPoints=computeRightShoulderMaxPoints();
            minPoints=computeRightShoulderMinPoints();
            isLeftShoulder=false;
            T1MF.setLeftShoulder(false);
        }
        else
        {
            maxPoints=computeMaxPoints();
            minPoints=computeMinPoints();
        }
    }
    
    protected abstract void typeCastT1MF();
    
    protected abstract Collection<Interval> computeLeftShoulderMaxPoints();
    
    protected abstract Collection<Interval> computeRightShoulderMaxPoints();
    
    protected abstract Collection<Interval> computeLeftShoulderMinPoints();
    
    protected abstract Collection<Interval> computeRightShoulderMinPoints();
    
    protected abstract Collection<Interval> computeMaxPoints();
    
    protected abstract Collection<Interval> computeMinPoints();
    
    protected void setMinPoints(Collection<Interval> min_points)
    {
        minPoints=min_points;
    }
    
    protected void setMaxPoints(Collection<Interval> max_points)
    {
        maxPoints=max_points;
    }
    
    
//    public abstract CIT2_Generator_Prototype clone();
    
    protected static void setShiftedSupportSet(T1MF_Interface original, CIT2_Generator shifted)
    {
        if(original.isLeftShoulder())
        {
            //If it is a left shoulder, conceptually the support doesn't have an end on the left side, so it must be keept unchanged
            shifted.setLeftShoulder(true);
            shifted.setSupport(new Tuple(original.getSupport().getLeft(), shifted.getSupport().getRight()));
        }
        if(original.isRightShoulder())
        {
            shifted.setRightShoulder(true);
            shifted.setSupport(new Tuple(shifted.getSupport().getLeft(), original.getSupport().getRight()));
        }
    }
}
