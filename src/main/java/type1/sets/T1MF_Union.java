/*
 * Created on 10 October 2012, 15:57
 *
 * Author: Christian Wagner
 * Copyright 2012 Christian Wagner All Rights Reserved.
 */
package type1.sets;

import generic.Tuple;

/**
 *
 * @author Christian Wagner
 */
public class T1MF_Union extends T1MF_Prototype
{
    private T1MF_Interface setA, setB;
    
    public T1MF_Union(T1MF_Interface setA, T1MF_Interface setB)
    {
        super("Union: "+setA.getName()+"_"+setB.getName());
//        if(setA.getSupport()==null||(setA instanceof T1MF_Constant &&(((T1MF_Constant)setA).getValue()==0)))
//            this.support=setB.getSupport();
//        else if(setB.getSupport()==null||(setB instanceof T1MF_Constant &&(((T1MF_Constant)setB).getValue()==0)))
//            this.support=setA.getSupport();
//        else
//            this.support = new Tuple(Math.min(setA.getSupport().getLeft(),setB.getSupport().getLeft()),
//                Math.max(setA.getSupport().getRight(),setB.getSupport().getRight()));
        this.support=Tuple.union(setA.getSupport(), setB.getSupport());
        this.setA = setA;
        this.setB = setB;
    }
    
    public T1MF_Union clone()
    {
        return new T1MF_Union(setA.clone(), setB.clone());
    }

    @Override
    public double getFS(double x) {
        return Math.max(setA.getFS(x), setB.getFS(x));
    }

    @Override
    public Tuple getAlphaCut(double alpha) 
    {
        throw new UnsupportedOperationException("Not supported yet.");        
    }

    @Override
    public double getPeak() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }   
    
    @Override
    public void setSupport(Tuple s)
    {
        support=s;
    }
}
