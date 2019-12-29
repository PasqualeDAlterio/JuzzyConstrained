/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
//import CIT2_Generator.T1MF_Shifted_MF;

/**
 * Class implementing a CIT2 fuzzy set
 * @author Pasquale
 */

public class CIT2 extends IntervalT2MF_Prototype{
    
    //The acceptable embedded sets of the CIT2. Implemented with lazy initialization
    private CIT2_Generator[] embeddedSets;
    //This field stores the IT2 equivalent (i.e. with the same FOU) of the current CIT2 set
    private IntervalT2MF_Generic equivalentIT2;
    //Generator set (GS) used to generate the CIT2
    private CIT2_Generator generatorSet;
    /*The number of shifts determines the number of acceptable embedded sets (AES) that will be embedded in the FOU. Each shift represents an AES
      The distance between the AES is equal to the shiftingStep. The size of the FOU, therefore, depends on boht the number of AES and the shiftingStep value
    */
    //Number of AES at the left of the generator set i.e. the AES obtained by shifting the generator set to the left
    private int leftAES;
    //Number of AES at the right of the generator set i.e. the AES obtained by shifting the generator set to the right
    private int rightAES;
    //Distance between the AES
    private double shiftingStep;
    //Random number generator, used to sample a random AES within the FOU in the continous case (i.e. unlimited number of AES)
    Random random_generator;
    
     /**
     * Constructor for the CIT2 fuzzy set.
     * 
     * @param name
     * @param generator_set The T1 generator set used to generate the CIT2 set
     * @param left_AES Number of AES at the left of the generator set i.e. the AES obtained by shifting the generator set to the left
     * @param right_AES Number of AES at the right of the generator set i.e. the AES obtained by shifting the generator set to the right
     * @param shifting_step distance between the AES
     */
    
    public CIT2(String name, CIT2_Generator generator_set, int left_AES, int right_AES, double shifting_step)
    {
        super(name);
        this.name=name;
        generatorSet=generator_set;
        shiftingStep=shifting_step;
        leftAES=left_AES;
        rightAES=right_AES;
        embeddedSets=allocateAES();
        initializeUpperbound();
        initializeLowerbound();
        equivalentIT2=new IntervalT2MF_Generic("IT2_from_"+name, uMF, lMF);
        Tuple generator_support=generator_set.getSupport();
        support=new Tuple(generator_support.getLeft()-(shifting_step*left_AES), generator_support.getRight()+(shifting_step*right_AES));
    }
    
     /**
     * Constructor for the CIT2 fuzzy set. Equivalent to CIT2(name, generator_set, 1, 1 shifting_step)
     * 
     * @param name The name of the set
     * @param generator_set The T1 generator set used to generate the CIT2 set
     * @param shifting_step Distance between the generator set and the leftmost/rightmost AES
     */
    
    public CIT2(String name, CIT2_Generator generator_set, double shifting_step)
    {
        this(name, generator_set, 1, 1, shifting_step);
    }
    
    public double getShiftingSize()
    {
        return shiftingStep;
    }
    
    
    /**
     * Sets the support set for the CIT2 set
     * @param support the Tuple containing the new support set
     */
    @Override
    public void setSupport(Tuple support)
    {
        this.support=support;
        //Once the support set has been changed for the CIT2 FS, also the support set of the AES needs to be updated
        if(embeddedSets!=null)
        {
            for(T1MF_Interface current_aes:embeddedSets)
            {
                current_aes.setSupport(TupleOperations.intersection(support, current_aes.getSupport()));
            }  
        }
        if(uMF!=null)
            uMF.setSupport(TupleOperations.intersection(support, uMF.getSupport()));
        if(lMF!=null)
            lMF.setSupport(TupleOperations.intersection(support, lMF.getSupport()));
    }
    
    public int getLeftShifts()
    {
        return leftAES;
    }
    
    public CIT2_Generator getGeneratorSet()
    {
        return generatorSet;
    }
    
    public int getRightShifts()
    {
        return rightAES;
    }
    
    private void initializeUpperbound()
    {
        uMF=new CIT2_Boundary(name, CIT2_Boundary_Type.UPPERBOUND, embeddedSets[0], embeddedSets[embeddedSets.length-1], generatorSet, leftAES*shiftingStep, shiftingStep*rightAES);
    }
    
    private void initializeLowerbound()
    {
        lMF=new CIT2_Boundary(name, CIT2_Boundary_Type.LOWERBOUND, embeddedSets[0], embeddedSets[embeddedSets.length-1], generatorSet, shiftingStep*rightAES, shiftingStep*leftAES);
    }
    
    public T1MF_Interface getUpperbound()
    {
        return uMF;
    }
    
    public T1MF_Interface getLowerbound()
    {
        return lMF;
    }
    
    public CIT2_Generator[] getEmbeddedSets()
    {
        return embeddedSets;
    }
    
    /**
     * Plots the AES of a collection of CIT2 sets
     * @param name name of the plot
     * @param sets CIT2 sets to plot
     * @param xAxisRange range (over the x-axis) of the values to plot
     * @param discretizationLevel discretization level to use to plot the sets
     */
    
    public static void plotCIT2AES(String name, CIT2[] sets, Tuple xAxisRange, int discretizationLevel)
    {
         T1MF_Interface[] sets_to_plot=new T1MF_Interface[sets.length*sets[0].getEmbeddedSets().length];
         int index=0;
         for(CIT2 current_ct2 : sets)
         {
            for(T1MF_Interface current_embedded_set : current_ct2.getEmbeddedSets())
            {
                sets_to_plot[index]=current_embedded_set;
                index++;
            }
         }
         JFreeChartPlotter.plotMFs(name, sets_to_plot, xAxisRange, discretizationLevel);
    }
    
    /**
     * Plots the AES of the current CIT2 set
     * @param name name of the plot
     * @param xAxisRange range (over the x-axis) of the values to plot
     * @param discretizationLevel discretization level to use to plot the sets
     */
    public void plotCIT2AES(String name, Tuple xAxisRange, int discretizationLevel)
    {
        plotCIT2AES(name, new CIT2[]{this}, xAxisRange, discretizationLevel);
    }
    
    /**
     * Plots the FOU of a collection of CIT2 sets
     * @param name name of the plot
     * @param sets CIT2 sets to plot
     * @param xAxisRange range (over the x-axis) of the values to plot
     * @param discretizationLevel discretization level to use to plot the sets
     */
    public static void plotCIT2(String name, CIT2[] sets, Tuple xAxisRange, int discretizationLevel)
    {
         JFreeChartPlotter.plotMFs(name, sets, xAxisRange, discretizationLevel);
    }
    
    /**
     * Plots the CIT2 set
     * @param xAxisRange range (over the x-axis) of the values to plot
     * @param discretizationLevel discretization level to use to plot the sets
     */
    public void plotCIT2(Tuple xAxisRange, int discretizationLevel)
    {
        plotCIT2(name, new CIT2[]{this}, xAxisRange, discretizationLevel);
    }
    
    
    /**
     * Computes the centroid of the set
     * @param discretization number of discretization steps to use
     * @return 
     */
    @Override
    public Tuple getCentroid(int discretization)
    {
        double generator_centroid=generatorSet.getDefuzzifiedCentroid(discretization);
        return new Tuple(generator_centroid-(leftAES*shiftingStep),generator_centroid+(rightAES*shiftingStep));
    }
    
    /**
     * Allocates the AES of the CIT2 set
     * @return 
     */
    private CIT2_Generator[] allocateAES()
    {
        CIT2_Generator[] shifts=new CIT2_Generator[leftAES+rightAES+1];
        String shift_name;
        int index=0;
        double offset=shiftingStep*leftAES*-1;
        //Allocates the AES at the left of the generator set
        for(int i=0;i<leftAES;i++)
        {
            shift_name=name+" left shift "+i;
            //Shift the generator to create an AES
            shifts[i]=generatorSet.shiftFunction(shift_name, offset);
            //shifts[i]=new T1MF_Shifted_MF(generatorSet, offset);
            offset+=shiftingStep;
            index++;
        }
        shifts[index]=generatorSet;
        index++;
        offset=shiftingStep;
        //Allocates the AES at the right of the generator set
        for(int i=0;i<rightAES;i++)
        {
            shift_name=name+" right shift "+i;
            //Shift the generator set to create an AES
            shifts[index]=generatorSet.shiftFunction(shift_name, offset);
            //shifts[index]=new T1MF_Shifted_MF(generatorSet, offset);
            offset+=shiftingStep;
            index++;
        }
        return shifts;
    }
    
    /**
     * Converts the current set into an equivalent (i.e. with the same FOU) IT2 set
     * @return the equivalent IT2 set (i.e. with the same FOU)
     */
    public IntervalT2MF_Interface toIT2()
    {
         return equivalentIT2;
    }
    
    /**
     * Samples a random AES
     * @return Random AES
     */
    public CIT2_Generator getRandomAES()
    {
        double left_max_shift, right_max_shift;
        CIT2_Generator result;
        left_max_shift=shiftingStep*leftAES*-1;
        right_max_shift=shiftingStep*rightAES;
        if(left_max_shift==right_max_shift)
            right_max_shift=left_max_shift+0.0001;
        double randomValue = ThreadLocalRandom.current().nextDouble(left_max_shift, right_max_shift);
        return generatorSet.shiftFunction("", randomValue);
        //return new T1MF_Shifted_MF(generatorSet, randomValue);
    }
    
    private Random getRandom()
    {
        if(random_generator==null)
            random_generator=new Random();
        return random_generator;
    }
    
    public CIT2_Generator getLeftmostES()
    {
        return embeddedSets[0];
    }
    
    public CIT2_Generator getRightmostES()
    {
        return embeddedSets[embeddedSets.length-1];
    }
    
}
