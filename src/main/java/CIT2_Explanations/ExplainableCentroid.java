package CIT2_Explanations;

import CIT2.CIT2;
import generic.Output;
import generic.Tuple;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedList;
import tools.JFreeChartPlotter;
import tools.Pair;
import additional_T1MF.T1MF_InferenceResult;
import intervalType2.sets.IntervalT2MF_Interface;
import java.util.ArrayList;
import java.util.List;
import type1.sets.T1MF_Interface;
import type1.sets.T1MF_Union;

/**
 * Class that contains all the data required to build the explanation for the centroid
 * @author Pasquale
 */
public class ExplainableCentroid {
    //The centroid value
    private Tuple constrainedCentroid;
    //Each of the pairs in the list, says which rule was used to do the inference on that specific consequent CIT2 set
    private LinkedList<Pair<CIT2, RuleExplanation>> leftEndpointExplaination, rightEndpointExplaination;
    //The aes that determine the interval centroid
    private T1MF_Interface min_es, max_es;
    //The resulting FOU obtained after the inferencing step
    private IntervalT2MF_Interface firedFOU;
    //The output variable this centroid value refers to
    private Output output;
    
    /**
     * Constructor for the explanation
     * @param centroid_interval The centroid value
     * @param fired_fou The fired FOU
     * @param left_endpoint_explanation List with the consequent CIT2 that contributed to the left endpoint and the rule used in their inferencing
     * @param right_endpoint_explanation List with the consequent CIT2 that contributed to the right endpoint and the rule used in their inferencing
     * @param min_es AES that determines the left endpoint
     * @param max_es AES that determines the right endpoint
     * @param output The output variable this centroid value refers to
     */
    public ExplainableCentroid(Tuple centroid_interval, IntervalT2MF_Interface fired_fou, LinkedList<Pair<CIT2, RuleExplanation>> left_endpoint_explanation, LinkedList<Pair<CIT2, RuleExplanation>> right_endpoint_explanation,
                                T1MF_Interface min_es, T1MF_Interface max_es, Output output){
        constrainedCentroid=centroid_interval;
        leftEndpointExplaination=left_endpoint_explanation;
        rightEndpointExplaination=right_endpoint_explanation;
        this.min_es=min_es;
        this.max_es=max_es;
        this.output=output;
        this.firedFOU=fired_fou;
    }
    
    /**
     * Constructor used when the explanation is not requested. Only the interval centroid value is stored
     * @param interval_centroid The interval centroid value
     */
    public ExplainableCentroid(Tuple interval_centroid)
    {
        this.constrainedCentroid=interval_centroid;
    }
    
    /**
     * Function that organizes all the information of the current object in a printable string
     * @return The string containing the explanation for the current centroid
     */
    public String printableExplanation()
    {
        //Use DecimalFormat to avoid very long doubles
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        //Build the explanation for the left endpoint
        StringBuffer result=new StringBuffer("The leftmost centroid ("+df.format(constrainedCentroid.getLeft())+") is obtained from firing the following rules:\n");
        //Loop through all the consequent CIT2 used in the left endpoint and print the explanation for the respective rule used for the inference
        for(Pair<CIT2, RuleExplanation> current : leftEndpointExplaination)
            addRuleExplaination(current, result);
        result.append("\n");
        //Build the explanation for the left endpoint
        result.append("The rightmost centroid ("+df.format(constrainedCentroid.getRight())+") is obtained from firing the following rules:\n");
        //Loop through all the consequent CIT2 used in the right endpoint and print the explanation for the respective rule used for the inference
        for(Pair<CIT2, RuleExplanation> current : rightEndpointExplaination)
            addRuleExplaination(current, result);
        return result.toString();
    }
    
    /**
     * Adds the explanation of a single rule to build the final explanation returned by printFirings
     * @param current_rule The current rule and explanation to add to the final explanation
     * @param text_explanation The explanation built so far to which the explanation of the current rule will be added
     */
    private void addRuleExplaination(Pair<CIT2, RuleExplanation> current_rule, StringBuffer text_explanation)
    {
        if(current_rule.getRight().getRuleFiringUsed()>0)
            text_explanation.append(current_rule.getLeft().getName()+": "+current_rule.getRight().getRuleFiringUsed()+" "+current_rule.getRight().toString()+" using the "+current_rule.getRight().printFiringLevelUsed()+" membership degree of each input terms\n");
    }
    
    /**
     * Plots the AES that determined the centroid value in intervalCentroid
     */
    public void plotEmbeddedSets()
    {
        System.out.println(max_es.getSupport());
        JFreeChartPlotter.plotMFs("AES determining the centroid endpoints", new T1MF_Interface[]{max_es, min_es}, output.getDomain(), 1000);
    }
    
        public T1MF_Interface getMin_es() {
        return min_es;
    }

    public T1MF_Interface getMax_es() {
        return max_es;
    }

    public Tuple getConstrainedCentroid() {
        return constrainedCentroid;
    }

    public LinkedList<Pair<CIT2, RuleExplanation>> getLeftEndpointExplaination() {
        return leftEndpointExplaination;
    }

    public LinkedList<Pair<CIT2, RuleExplanation>> getRightEndpointExplaination() {
        return rightEndpointExplaination;
    }
    
    /**
     * Functions that plots different steps in the generation of the AES determining the endpoints of the constrained centroid.
     */
    public void plotAESGeneration()
    {
        List<CIT2> fou_to_plot=getOutputPartitioning();
        CIT2[] array_fou_partitioning=fou_to_plot.toArray(new CIT2[0]);
        //Plot the partitioning of the outptu variable
        JFreeChartPlotter.plotMFs("Partitioning of the Output Variable", array_fou_partitioning, output.getDomain(), 1000);
        //Plot the fired FOU
        JFreeChartPlotter.plotMFs("Fired FOU", new IntervalT2MF_Interface[]{firedFOU}, output.getDomain(), 1000);
        //Plot the generation of the AES determining the left endpoint of the centroid
        AESGenerationPlotting(array_fou_partitioning, true);
        //Plot the generation of the AES determining the right endpoint of the centroid 
        AESGenerationPlotting(array_fou_partitioning, false);
        
    }
    
    /**
     * Handles the plotting of the generation of one of the AES determining the enpdoints
     * @param output_partitioning The CIT2 sets partitioning the output variable
     * @param isLeftAES True if the generation for the AES determining the left endpoint needs to be generated. False if it is for the AES determining the right endpoint
     */
    private void AESGenerationPlotting(CIT2[] output_partitioning, boolean isLeftAES)
    {
        JFreeChartPlotter.plotFOUAES("Firing of the consequent CIT2 sets", output_partitioning, getFiredAES(output_partitioning, isLeftAES), output.getDomain(), 1000);
        T1MF_Interface left_endpoint_aes=null;
        //For each of the CIT2 partitioning the output variable, one AES is selected...
        for(T1MF_Interface current_fired_aes : getFiredAES(output_partitioning, isLeftAES))
        {
            //...the union of all the AES is then computed...
            if(left_endpoint_aes!=null)
                left_endpoint_aes=new T1MF_Union(current_fired_aes, left_endpoint_aes);
            else
                left_endpoint_aes=current_fired_aes;
        }
        //... and plotted
        JFreeChartPlotter.plotMFs("Acceptable embedded set obtained", new T1MF_Interface[]{left_endpoint_aes}, output.getDomain(), 1000);
    }
    
//    private IntervalT2MF_Interface[] addFiredFOU(CIT2[] output_variable_partitioning)
//    {
//        IntervalT2MF_Interface[] result= new IntervalT2MF_Interface[output_variable_partitioning.length+1];
//        for(int i=0;i<output_variable_partitioning.length;i++)
//            result[i]=output_variable_partitioning[i];
//        result[result.length-1]=firedFOU;
//        return result;
//    }
//    
    /**
     * Returns the CIT2 sets partitioning the output variable
     * @return A List with the sets partitioning the output variable
     */
    private List<CIT2> getOutputPartitioning()
    {
        HashSet<CIT2> rule_consequents=new HashSet<>();
        //Loop through all the rules determining the explanations and store their consequent sets
        for(Pair<CIT2, RuleExplanation> current_pair : leftEndpointExplaination)
            rule_consequents.add(current_pair.getLeft());
        for(Pair<CIT2, RuleExplanation> current_pair : rightEndpointExplaination)
            rule_consequents.add(current_pair.getLeft());
        return new ArrayList<>(rule_consequents);
    }
    
    /**
     * For each of the CIT2 partitioning the output, selects one of its AES and applies the inference operator on it
     * @param output_partitioning The CIT2 sets partitioning the output variable
     * @param isLeftExplanation True if the generation for the AES determining the left endpoint needs to be generated. False if it is for the AES determining the right endpoint
     * @return The selected T1 AES of the input CIT2 sets after the inference
     */
    private T1MF_Interface[] getFiredAES(CIT2[] output_partitioning, boolean isLeftExplanation)
    {
        CIT2 current_consequent;
        Pair<CIT2, RuleExplanation> current_explanation;
        LinkedList<Pair<CIT2, RuleExplanation>> explanations;
        T1MF_Interface aes;
        if(isLeftExplanation)
            explanations=leftEndpointExplaination;
        else
            explanations=rightEndpointExplaination;
        T1MF_Interface[] fired_aes=new T1MF_Interface[explanations.size()];
        int i=0;
        //Loop through the CIT2 sets partitioning the output...
        for(CIT2 current_set : output_partitioning)
        {
            //For current_set, find the structure containing informations about the rule it belonged to and the firing strength
            current_explanation=findExplanation(current_set, explanations);
            current_consequent=current_explanation.getLeft();
            //Depending on which endpoint is being computed, select the leftmost or rightmost AES
            if(isLeftExplanation)
                aes=current_consequent.getLeftmostAES();
            else
                aes=current_consequent.getRightmostAES();
            fired_aes[i]=new T1MF_InferenceResult("Inferenced "+current_consequent.getName(), aes, current_explanation.getRight().getRuleFiringUsed());
            i++;
        }
        return fired_aes;
    }

    /**
     * Finds the rule and the explanation used to do the inference on the CIT2 set passed as an input
     * @param set The set for which the rule and explanation is required
     * @param explanations The List of all the explanations
     * @return The RuleExplanation for the current set
     */
    private Pair<CIT2, RuleExplanation> findExplanation(CIT2 set, LinkedList<Pair<CIT2, RuleExplanation>> explanations)
    {
        for(Pair<CIT2, RuleExplanation> current_explanation : explanations)
        {
            if(set.equals(current_explanation.getLeft()))
                return current_explanation;
        }
        return null;
    }
    
}
