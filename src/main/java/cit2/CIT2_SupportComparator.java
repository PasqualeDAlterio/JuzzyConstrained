/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cit2;

import generic.Tuple;
import java.util.Comparator;

/**
 * Comparator that compares 2 CIT2 sets by comparing the left endpoints of their support sets
 * @author psxpd2admin
 */
public class CIT2_SupportComparator implements Comparator<CIT2>{
    
    /**
     * Compares the left endpoint of the support set of 
     * @param a the first set to compare
     * @param b the second set to compare
     * @return -1 if support_a.getLeft()<support_b.getLeft(); 1 support_b.getLeft()<support_a.getLeft(); 0 otherwise
     */
    @Override
    public int compare(CIT2 a, CIT2 b)
    {
        Tuple support_a=a.getSupport(), support_b=b.getSupport();
        if(support_a.getLeft()<support_b.getLeft())
            return -1;
        if(support_b.getLeft()<support_a.getLeft())
            return 1;
        if(a.equals(b))
            return 0;
        return a.hashCode()-b.hashCode();
    }
    
}
