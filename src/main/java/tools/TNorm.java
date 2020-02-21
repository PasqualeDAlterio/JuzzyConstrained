package tools;

import generic.Tuple;
import java.util.List;

/**
 * Interface representing a T-Norm 
 * @author Pasquale
 */
public interface TNorm<T>
{
    //Applies the TNorm operator to the list of elements
    T doTNorm(List<T> elements);
}
