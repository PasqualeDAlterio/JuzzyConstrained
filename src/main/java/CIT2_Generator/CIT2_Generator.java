package CIT2_Generator;

import generic.Tuple;
import java.util.Collection;
import java.util.TreeSet;
import tools.Interval;
import type1.sets.T1MF_Interface;

/**
 *
 * @author Pasquale
 */
public interface CIT2_Generator extends T1MF_Interface, Cloneable{
    
    //Max intervals (e.g. upper base of a trapezoid)
    Collection<Interval> getMaxPoints();
    //Min points with a membership greater than 0
    Collection<Interval> getMinPoints();

    CIT2_Generator clone();
    
    //Returns the current MF shifted by a shifting factor. It is necessary to generate the AESs
    CIT2_Generator shiftFunction(String name, double shifting_factor);
}
