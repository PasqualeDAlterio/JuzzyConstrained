/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package type1.sets;

import generic.Tuple;

/**
 *
 * @author psxpd2admin
 */
public class T1MF_Common_Points extends T1MF_Prototype{
      
    private T1MF_Interface boundary;
    private T1MF_Interface switchSet;
    private Tuple support, support_union;
    
    public T1MF_Common_Points(String name, T1MF_Interface boundary, T1MF_Interface switch_set)
    {
        super(name);
        this.boundary=boundary;
        switchSet=switch_set;
        support_union=Tuple.union(boundary.getSupport(), switch_set.getSupport());
        support=Tuple.intersection(boundary.getSupport(), switch_set.getSupport());
    }
    
    public T1MF_Common_Points clone()
    {
        return new T1MF_Common_Points(name, boundary.clone(), (T1MF_Interface)switchSet.clone());
    }
    
    @Override
    public double getPeak()
    {
        return Math.max(boundary.getPeak(), switchSet.getPeak());
    }
    
    @Override
    public double getFS(double x)
    {
        double mf_1_fs=boundary.getFS(x);
        if(Math.abs(mf_1_fs-switchSet.getFS(x))<0.001)
            return mf_1_fs;
        return 0;
    }
    
    public T1MF_Interface getSwitchSet()
    {
        return switchSet;
    }
    
    @Override
    public Tuple getSupport()
    {
        return support;
    }
    
    public Tuple getUnionSupport()
    {
        return support_union;
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
    

}
