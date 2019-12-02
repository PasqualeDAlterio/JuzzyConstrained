/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cit2;

import generic.Output;
import generic.Tuple;
import java.text.DecimalFormat;
import java.util.LinkedList;
import tools.Pair;
import type1.sets.T1MF_Interface;
import tools.JMathPlotter;

/**
 * Class that contains all the data required to build the explanation for the centroid
 * @author User
 */
public class ExplainableCentroid {
    //The centroid value
    private Tuple intervalCentroid;
    //Each of the pairs in the list, says which rule was used to do the inference on that specific consequent CIT2 set
    private LinkedList<Pair<CIT2, RuleExplanation>> leftEndpointExplaination, rightEndpointExplaination;
    //The aes that determine the interval centroid
    private T1MF_Interface min_es, max_es;
    //The output variable this centroid value refers to
    private Output output;
    
    /**
     * Constructor for the explanation
     * @param centroid_interval The centroid value
     * @param left_endpoint_explanation List with the consequent CIT2 that contributed to the left endpoint and the rule used in their inferencing
     * @param right_endpoint_explanation List with the consequent CIT2 that contributed to the right endpoint and the rule used in their inferencing
     * @param min_es AES that determines the left endpoint
     * @param max_es AES that determines the right endpoint
     * @param output The output variable this centroid value refers to
     */
    public ExplainableCentroid(Tuple centroid_interval, LinkedList<Pair<CIT2, RuleExplanation>> left_endpoint_explanation, LinkedList<Pair<CIT2, RuleExplanation>> right_endpoint_explanation,
                                T1MF_Interface min_es, T1MF_Interface max_es, Output output){
        intervalCentroid=centroid_interval;
        leftEndpointExplaination=left_endpoint_explanation;
        rightEndpointExplaination=right_endpoint_explanation;
        this.min_es=min_es;
        this.max_es=max_es;
        this.output=output;
    }
    
    /**
     * Constructor used when the explanation is not requested. Only the interval centroid value is stored
     * @param interval_centroid The interval centroid value
     */
    public ExplainableCentroid(Tuple interval_centroid)
    {
        this.intervalCentroid=interval_centroid;
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
        StringBuffer result=new StringBuffer("The left endpoint ("+df.format(intervalCentroid.getLeft())+") has been obtained from the following rules:\n");
        //Loop through all the consequent CIT2 used in the left endpoint and print the explanation for the respective rule used for the inference
        for(Pair<CIT2, RuleExplanation> current : leftEndpointExplaination)
            addRuleExplaination(current, result);
        result.append("\n");
        //Build the explanation for the left endpoint
        result.append("The right endpoint ("+df.format(intervalCentroid.getRight())+") has been obtained from the following rules:\n");
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
            text_explanation.append("For the "+current_rule.getLeft().getName()+" term: "+current_rule.getRight().toString()+"The firing value of the rule is "+current_rule.getRight().getRuleFiring()+"; the "+current_rule.getRight().printFiringLevelUsed()+" firing value has been used to obtain this endpoint\n");
    }
    
    /**
     * Plots the AES that determined the centroid value in intervalCentroid
     */
    public void plotEmbeddedSets()
    {
        System.out.println(max_es.getSupport());
        JMathPlotter.plotMFs("Output "+output.getName(), new T1MF_Interface[]{max_es, min_es}, output.getDomain(), 1000);
    }
    
        public T1MF_Interface getMin_es() {
        return min_es;
    }

    public T1MF_Interface getMax_es() {
        return max_es;
    }

    public Tuple getIntervalCentroid() {
        return intervalCentroid;
    }

    public LinkedList<Pair<CIT2, RuleExplanation>> getLeftEndpointExplaination() {
        return leftEndpointExplaination;
    }

    public LinkedList<Pair<CIT2, RuleExplanation>> getRightEndpointExplaination() {
        return rightEndpointExplaination;
    }
}
