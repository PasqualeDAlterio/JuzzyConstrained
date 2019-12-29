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
public class T1MF_Shifted_MF extends T1MF_Prototype implements CIT2_Generator{
    
    private final CIT2_Generator MFtoShift;
    private final double shiftingFactor;
    private ArrayList<Interval> minPoints, maxPoints;
    
    public T1MF_Shifted_MF(CIT2_Generator mf_to_shift, double shifting_factor)
    {
        super(mf_to_shift.getName()+" shifted by "+shifting_factor);
        MFtoShift=mf_to_shift;
        shiftingFactor=shifting_factor;
        initializeMinMaxPoints();
        initializeSupportSet();
        isLeftShoulder=MFtoShift.isLeftShoulder();
        isRightShoulder=MFtoShift.isRightShoulder();
        support=new Tuple(MFtoShift.getSupport().getLeft()-shifting_factor, MFtoShift.getSupport().getRight()-shifting_factor);
        //MFtoShift.setSupport(new Tuple(MFtoShift.getSupport().getLeft()-shifting_factor, MFtoShift.getSupport().getRight()-shifting_factor));
    }

    public CIT2_Generator shiftFunction(String a, double b)
    {
        return null;
    }

    public void setLeftShoulder(boolean value)
    {
        if(value==isLeftShoulder)
            return;
        MFtoShift.setLeftShoulder(value);
        isLeftShoulder=value;
        initializeSupportSet();
    }
    
    public void setRightShoulder(boolean value)
    {
        if(value==isRightShoulder)
            return;
        MFtoShift.setRightShoulder(value);
        isRightShoulder=value;
        initializeSupportSet();
    }
    
    private final void initializeSupportSet()
    {
        double left, right;
        //If the MF to shift is a shoulder, the  left endpoint of the support set must not be changed
        if(MFtoShift.isLeftShoulder()&&shiftingFactor>0)
            left=MFtoShift.getSupport().getLeft();
        else
            left=MFtoShift.getSupport().getLeft()+shiftingFactor;
        if(MFtoShift.isRightShoulder()&&shiftingFactor<0)
            right=MFtoShift.getSupport().getRight();
        else
            right=MFtoShift.getSupport().getRight()+shiftingFactor;
        support=new Tuple(left, right);
    }
    
    private final void initializeMinMaxPoints()
    {
        minPoints=shiftIntervals(MFtoShift.getMinPoints());
        maxPoints=shiftIntervals(MFtoShift.getMaxPoints());
    }
    
    private ArrayList<Interval> shiftIntervals(Collection<Interval> intervals)
    {
        if(intervals==null)
            return null;
        ArrayList<Interval> result=new ArrayList<>(intervals.size());
        for(Interval current_interval : intervals)
            result.add(new Interval(current_interval.getLeft()+shiftingFactor, current_interval.getRight()+shiftingFactor));
        return result;
    }
    
    public Collection<Interval> getMinPoints()
    {
        return minPoints;
    }
    
    public Collection<Interval> getMaxPoints()
    {
        return maxPoints;
    }
    
    public double getPeak()
    {
        return MFtoShift.getPeak()+shiftingFactor;
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
    
    
    public double getFS(double x)
    {
        //System.out.println(MFtoShift.getSupport().getRight()+" "+support.getRight());
        Tuple mf_support=MFtoShift.getSupport();
        MFtoShift.setSupport(this.support);
        double membership_degree= MFtoShift.getFS(x-shiftingFactor);
        MFtoShift.setSupport(mf_support);
        return membership_degree;
    }
}
