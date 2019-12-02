/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import generic.Tuple;
import java.util.List;

/**
 * Minimum T-Norm implemented using the factory design pattern
 * @author psxpd2admin
 */
public class MinimumTNorm implements TNorm{
    
    static private MinimumTNorm factoryElement=null;
    
    private MinimumTNorm()
    {
        
    }
    
    @Override
    public Tuple doTNorm(List<Tuple> values)
    {
        Tuple result=new Tuple(1, 1);
        for(Tuple current_tuple : values)
        {
            result.setLeft(Math.min(result.getLeft(), current_tuple.getLeft()));
            result.setRight(Math.min(result.getRight(), current_tuple.getRight()));
        }
        return result;
    }
    
    public static MinimumTNorm factoryMethod()
    {
        if(factoryElement==null)
            factoryElement=new MinimumTNorm();
        return factoryElement;
    }
}
