package tools;

import generic.Tuple;
import java.util.List;

/**
 * Minimum T-Norm used in IT2 inference implemented using the singleton design pattern
 * @author Pasquale
 */
public class MinimumTNorm implements TNorm<Tuple>{
    
    static private MinimumTNorm instance=null;
    
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
    
    public static MinimumTNorm getInstance()
    {
        if(instance==null)
            instance=new MinimumTNorm();
        return instance;
    }
}
