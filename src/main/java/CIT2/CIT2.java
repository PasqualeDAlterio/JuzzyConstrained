package CIT2;


import CIT2_Generator.CIT2_Generator;
import additional_IT2MF.IntervalT2MF_Generic;
import generic.*;
import tools.TupleOperations;
import type1.sets.T1MF_Interface;
import intervalType2.sets.IntervalT2MF_Interface;
import intervalType2.sets.IntervalT2MF_Prototype;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import tools.JFreeChartPlotter;

/**
 * Class implementing a CIT2 fuzzy set
 * @author Pasquale
 */

public class CIT2 extends IntervalT2MF_Prototype{

    //Generator set (GS) used to generate the CIT2
    private final CIT2_Generator generatorSet;
    private final Tuple displacementInterval;
    private final CIT2_Generator leftmostAES, rightmostAES;
    //Random number generator, used to sample a random AES within the FOU in the continous case (i.e. unlimited number of AES)
    Random random_generator;

    /**
     * Constructor for the CIT2 fuzzy set. Equivalent to CIT2(name, generator_set, new Tuple(shifting_step*-1, shifting_step))
     *
     * @param name The name of the set
     * @param generator_set The T1 generator set used to generate the CIT2 set
     * @param displacement_interval The displacement interval [a, b] (a<=0, b>=0) to use to generate the set
     */

    public CIT2(String name, CIT2_Generator generator_set, Tuple displacement_interval)
    {
        super(name);
        if(displacement_interval.getLeft()>0||displacement_interval.getRight()<0)
            throw new IllegalArgumentException("The displacement interval must be of the form [a,b] with a<=0 and b>=0");
        this.generatorSet=generator_set;
        this.displacementInterval=displacement_interval;
        leftmostAES=generatorSet.shiftFunction(generatorSet.getName()+" shifted by"+displacementInterval.getLeft(), displacementInterval.getLeft());
        rightmostAES=generator_set.shiftFunction(generatorSet.getName()+" shifted by "+displacementInterval.getRight(), displacementInterval.getRight());
        initializeUpperbound();
        initializeLowerbound();
        Tuple generator_support=generator_set.getSupport();
        //Make the support set "wider", allowing the whole FOU to fit in
        support=new Tuple(generator_support.getLeft()+displacementInterval.getLeft(), generator_support.getRight()+displacementInterval.getRight());
    }


     /**
     * Constructor for the CIT2 fuzzy set. Equivalent to CIT2(name, generator_set, new Tuple(shifting_step*-1, shifting_step))
     * 
     * @param name The name of the set
     * @param generator_set The T1 generator set used to generate the CIT2 set
     * @param shifting_step The value to use to generate the displacement interval [shifting_step*-1, shifting_step]
     */
    
    public CIT2(String name, CIT2_Generator generator_set, double shifting_step)
    {
        this(name, generator_set, new Tuple(shifting_step*-1, shifting_step));
    }
    
    public Tuple getDisplacementInterval()
    {
        return displacementInterval;
    }
    
    
    /**
     * Sets the support set for the CIT2 set
     * @param support The Tuple containing the new support set
     */
    @Override
    public void setSupport(Tuple support)
    {
        this.support=support;
        //Once the support set has been changed for the CIT2 FS, also the support set of the AES needs to be updated
        rightmostAES.setSupport(TupleOperations.intersection(support, rightmostAES.getSupport()));
        leftmostAES.setSupport(TupleOperations.intersection(support, leftmostAES.getSupport()));
        if(uMF!=null)
            uMF.setSupport(TupleOperations.intersection(support, uMF.getSupport()));
        if(lMF!=null)
            lMF.setSupport(TupleOperations.intersection(support, lMF.getSupport()));
    }

    public CIT2_Generator getGeneratorSet()
    {
        return generatorSet;
    }

    public CIT2_Generator getLeftmostAES()
    {
        return leftmostAES;
    }

    public CIT2_Generator getRightmostAES()
    {
        return rightmostAES;
    }

    private void initializeUpperbound()
    {
        uMF=new CIT2_Boundary(name, CIT2_Boundary_Type.UPPERBOUND, generatorSet, leftmostAES, rightmostAES, displacementInterval);
    }
    
    private void initializeLowerbound()
    {
        lMF=new CIT2_Boundary(name, CIT2_Boundary_Type.LOWERBOUND, generatorSet, leftmostAES, rightmostAES, displacementInterval);
    }
    
    /**
     * Plots the FOU of a collection of CIT2 sets
     * @param name Name of the plot
     * @param sets CIT2 sets to plot
     * @param xAxisRange Range of x values to plot (e.g. [0, 10])
     * @param discretizationLevel Discretization level to use to plot the sets
     */
    public static void plotCIT2(String name, CIT2[] sets, Tuple xAxisRange, int discretizationLevel)
    {
         JFreeChartPlotter.plotMFs(name, sets, xAxisRange, discretizationLevel);
    }
    
    /**
     * Plots the CIT2 set
     * @param xAxisRange Range of x values to plot (e.g. [0, 10])
     * @param discretizationLevel Discretization level to use to plot the sets
     */
    public void plotCIT2(Tuple xAxisRange, int discretizationLevel)
    {
        plotCIT2(name, new CIT2[]{this}, xAxisRange, discretizationLevel);
    }
    
    
    /**
     * Computes the centroid of the set
     * @param discretization Number of discretization steps to use
     * @return The Tuple containing the constrained centroid
     */
    @Override
    public Tuple getCentroid(int discretization)
    {
        double generator_centroid=generatorSet.getDefuzzifiedCentroid(discretization);
        //Returns the Tuple made of the centroid of the leftmost and the centroid of the rightmost AES
        return new Tuple(generator_centroid+displacementInterval.getLeft(),generator_centroid+displacementInterval.getRight());
    }

    /**
     * Converts the current set into an equivalent (i.e. with the same FOU) IT2 set
     * @return The equivalent IT2 set (i.e. with the same FOU)
     */
    public IntervalT2MF_Interface toIT2()
    {
         return new IntervalT2MF_Generic("IT2_from_"+name, uMF, lMF);
    }
    
    /**
     * Samples a random AES by shifting the generator set by a random value within the displacement interval (i.e. assumes the displacement interval is continuous)
     * @return A random AES
     */    
    public CIT2_Generator getRandomAES()
    {
        //If the displacement interval is empty (i.e. the CIT2 set is actually a T1 FS), simply return the generator set
        if(displacementInterval.getLeft()==displacementInterval.getRight())
            return generatorSet;
        double randomValue = ThreadLocalRandom.current().nextDouble(displacementInterval.getLeft(), displacementInterval.getRight());
        return generatorSet.shiftFunction("", randomValue);
        //return new T1MF_Shifted_MF(generatorSet, randomValue);
    }
    
    private Random getRandom()
    {
        if(random_generator==null)
            random_generator=new Random();
        return random_generator;
    }
    
}
