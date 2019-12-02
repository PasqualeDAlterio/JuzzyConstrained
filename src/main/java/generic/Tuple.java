package generic;

import java.io.Serializable;
import java.text.DecimalFormat;
/*
 * Range.java
 *
 * Created on 07 February 2006, 12:35
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

/**
 * Stores 2 values which are usually related in some way, such as a range,...
 * @author Unreal
 */
public class Tuple implements Comparable, Serializable
{
    private double left,right;
    
    /** Creates a new instance of Centroid */
    public Tuple() 
    {
        left = 0.0;
        right = 0.0;
    }
    
    public Tuple(double l, double r)
    {
        left = l;
        right = r;
    }
    
    public Tuple clone()
    {
        return new Tuple(left, right);
    }
    
    public void setLeft(double l)
    {
        left = l;
    }
    
    public void setRight(double r)
    {
        right = r;
    }
    

    /**
     * Sets the left and right points of this tuple to that of tuple t.
     * @param t
     */
    public void setEqual(Tuple t)
    {
        this.left = t.getLeft();
        this.right = t.getRight();
    }
    
    public double getLeft()
    {
        return left;
    }
    
    public double getRight()
    {
        return right;
    }

    public double getAverage()
    {
//        if(Double.isNaN(left))
//            return right;
        return (left+right)/2.0;
    }
    
    /**
     * Returns true if the parameter falls within the interval defined by the Tuple.
     * @param x
     * @return 
     */
    public boolean contains(double x)
    {
        return (x>=left && x<=right);
    }

    public double getSize()
    {
        //There was no abs but what if both values are negative?
        return Math.abs(right-left);
    }
    
//    public String toString()
//    {
//        return("lower = "+left+" and upper = "+right);
//    }
    
    public String toString()
    {   
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return "["+df.format(left)+", "+df.format(right)+"]";
    }
    
    /**
     *Adds an existing tuple to the current tuple by adding their left and right members respectively.
     */
    public Tuple add(Tuple x)
    {
        return new Tuple(this.getLeft()+x.getLeft(),this.getRight()+x.getRight());
    }
    

    /**
     *Comparison is done using right member of tuple!.
     */
    public int compareTo(Object o)
    {
//        if(this ==null) System.out.println("This is null");
//        if(o ==null) return 1;
//        Tuple t = (Tuple)o;
        if (this.getRight() < ((Tuple)o).getRight()) return -1;
        else if (this.getRight() > ((Tuple)o).getRight()) return 1;
                //if rights are the same, compare lefts
        else if(this.getLeft()<((Tuple)o).getLeft()) return -1;
        else if(this.getLeft()>((Tuple)o).getLeft()) return 1;
        else return 0;
    }    
    
    public static Tuple max(Tuple a, Tuple b)
    {
        if(Double.isNaN(a.right))
            return b;
        if(Double.isNaN(b.right))
            return a;
        return new Tuple(Math.max(a.left, b.left), Math.max(a.right, b.right));
    }
    
    public static Tuple union(Tuple a, Tuple b)
    {
        if(Double.isNaN(a.right))
            return b;
        if(Double.isNaN(b.right))
            return a;
        return new Tuple(Math.min(a.left, b.left), Math.max(a.right, b.right));
    }
    
    public static Tuple intersection(Tuple a, Tuple b)
    {
        if(Double.isNaN(a.right))
            return b;
        if(Double.isNaN(b.right))
            return a;
        return new Tuple(Math.max(a.left, b.left), Math.min(a.right, b.right));
    }
}
