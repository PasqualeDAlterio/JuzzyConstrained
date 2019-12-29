/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CIT2;

import CIT2_Explanations.RuleExplanation;
import CIT2_Explanations.ExplainableCentroid;
import CIT2_Generator.CIT2_Generator;
import additional_T1MF.T1MF_InferenceResult;
import generic.*;
import intervalType2.sets.*;
import intervalType2.system.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import tools.JFreeChartPlotter;
import tools.MinimumTNorm;
import tools.Pair;
import type1.sets.*;
import type1.system.*;
/**
 * Class modelling a rulebase of CIT2 rules
 * @author Pasquale
 */
public class CIT2_Rulebase implements FuzzyRulebase{
    //The rules in the rulebase
    private final ArrayList<CIT2_Rule> CIT2Rules=new ArrayList<>();
    //Comparator to sort CIT2 set in ascending order by the left endpoint of their support set. Needed for switch index defuzzification
    static private final CIT2_SupportComparator CIT2Comparator=new CIT2_SupportComparator();
    
    /**
     * Constructor with multiple rules
     * @param rules the rules that make up the rulebase
     */
    public CIT2_Rulebase(CIT2_Rule[] rules)
    {
        CIT2Rules.addAll(Arrays.asList(rules));
    }
    /**
     * Builds a CIT2 rulebase with 0 rules
     */
    
    public CIT2_Rulebase()
    {
    }
    
    /**
     * Adds the current_rule to the rulebase
     * @param current_rule The rule to be added to the rulebase
     * @return true if the collection has changed (as specified by Collection.add())
     */
    public boolean addRule(CIT2_Rule current_rule)
    {
        return CIT2Rules.add(current_rule);
    }
    

    
    public ArrayList<CIT2_Rule> getRules()
    {
        return CIT2Rules;
    }
    
    @Override
    public double getCentroid()
    {
        return this.switchIndexDefuzzification(100).getAverage();
    }
    
    /**
     * Builds a CIT2 rulebase starting from a T1 one. They have the same rules but each T1 is used as a generator set to build the corresponding CIT2 set
     * @param t1_rulebase The rulebase to start from
     * @param left_AES Number of AES at the left of the generator set
     * @param right_AES Number of AES at the right of the generator set
     * @param displacement_as_uod_percentage Size of the displacement set in terms of the percentage of the UOD
     * @return The CIT2 rulebase built using the T1 sets in t1_rulebase as generator sets
     */
    static public CIT2_Rulebase fromT1Rulebase(T1_Rulebase t1_rulebase, int left_AES, int right_AES, double displacement_as_uod_percentage)
    {
        CIT2_Rule[] rules=new CIT2_Rule[t1_rulebase.getRules().size()];
        CIT2_Antecedent[] current_ct2_antecedents;
        CIT2_Consequent[] current_ct2_consequents;
        T1MF_Interface current_T1_set;
        double shifting_size;
        int i, k=0;
        //Maps each T1 set in the T1 rulebase to its CIT2 version
        HashMap<T1MF_Interface, CIT2> t1_to_ct2=new HashMap<>();
        //Convert each rule
        for(T1_Rule current_T1_rule : t1_rulebase.getRules())
        {
            current_ct2_antecedents=new CIT2_Antecedent[current_T1_rule.getAntecedents().length];
            i=0;
            //Converting each antecedent in the rule
            for(T1_Antecedent current_T1_antecedent : current_T1_rule.getAntecedents())
            {
                current_T1_set=current_T1_antecedent.getMF();
                //Checks if a CIT2 set has already been generated from the current set, if not, it creates one
                if(t1_to_ct2.get(current_T1_set)==null)
                {
                    shifting_size=(displacement_as_uod_percentage*current_T1_antecedent.getInput().getDomain().getSize()/200);
                    t1_to_ct2.put(current_T1_set, new CIT2(current_T1_set.getName()+" CIT2", (CIT2_Generator)current_T1_set, left_AES, right_AES, shifting_size));

                }
                current_ct2_antecedents[i]=new CIT2_Antecedent(t1_to_ct2.get(current_T1_set), current_T1_antecedent.getInput());
                i++;
            }
            i=0;
            current_ct2_consequents=new CIT2_Consequent[current_T1_rule.getConsequents().length];
            //Converting each consequent in the rule
            for(T1_Consequent current_T1_consequent : current_T1_rule.getConsequents())
            {
                current_T1_set=current_T1_consequent.getMF();
                //Checks if a CIT2 set has already been generated from the current set, if not, it creates one
                if(t1_to_ct2.get(current_T1_set)==null)
                {
                    shifting_size=(displacement_as_uod_percentage*current_T1_consequent.getOutput().getDomain().getSize()/200);
                    t1_to_ct2.put(current_T1_set, new CIT2(current_T1_set.getName()+" CIT2", (CIT2_Generator)current_T1_set, left_AES, right_AES, shifting_size));
                }

                current_ct2_consequents[i]=new CIT2_Consequent(t1_to_ct2.get(current_T1_set), current_T1_consequent.getOutput());
                i++;
            }
            //CURRENTLY ONLY SUPPORTING CIT2 RULES WITH A SINGLE CONSEQUENT
            rules[k]=new CIT2_Rule(current_ct2_antecedents, current_ct2_consequents[0]);
            k++;
        }
        return new CIT2_Rulebase(rules);
    }
    
    /**
     * Converts the current rulebase to an equivalent IT2 one (i.e. the IT2 sets involved have the same FOU as the CIT2 sets in the current rulebase)
     * @return An equivalent IT2 Rulebase
     */
    public IT2_Rulebase toIT2()
    {
        IT2_Antecedent[] antecedents;
        IT2_Consequent[] consequents;
        CIT2_Antecedent[] current_antecedents;
        CIT2_Consequent[] current_consequents;
        CIT2_Antecedent ct2_antecedent;
        CIT2_Consequent ct2_consequent;
        IT2_Rulebase rulebase=new IT2_Rulebase();
        int i;
        //Maps each CIT2 set into an IT2 equivalent
        HashMap<CIT2, IntervalT2MF_Interface> ct2_to_it2=new HashMap<>();
        for(CIT2_Rule current_rule : CIT2Rules)
        {
            current_antecedents=current_rule.getAntecedents();
            current_consequents=current_rule.getConsequents();
            antecedents=new IT2_Antecedent[current_antecedents.length];
            consequents=new IT2_Consequent[current_consequents.length];
            //Converts each antecedent
            for(i=0;i<current_antecedents.length;i++)
            {
                ct2_antecedent=current_antecedents[i];
                //If the current CIT2 hasn't been converted yet, it creates a converted version
                if(!ct2_to_it2.containsKey(ct2_antecedent.getCIT2()))
                    ct2_to_it2.put(ct2_antecedent.getCIT2(), ct2_antecedent.getCIT2().toIT2());
                antecedents[i]=new IT2_Antecedent(ct2_to_it2.get(ct2_antecedent.getCIT2()), ct2_antecedent.getInput());
            }
            //Converts each consequent
            for(i=0;i<current_consequents.length;i++)
            {
                ct2_consequent=current_consequents[i];
                //If the current CIT2 hasn't been converted yet, it creates a converted version
                if(!ct2_to_it2.containsKey(ct2_consequent.getCIT2()))
                    ct2_to_it2.put(ct2_consequent.getCIT2(), ct2_consequent.getCIT2().toIT2());
                consequents[i]=new IT2_Consequent(ct2_to_it2.get(ct2_consequent.getCIT2()), ct2_consequent.getOutput());
            }
            rulebase.addRule(new IT2_Rule(antecedents, consequents));
        }
        return rulebase;
    }
    
    /**
     * Runs the inference with the switch index approach and produces an explanation for the endpoints
     * @param discretization
     * @return The interval centroid and the explanation for its endpoints
     */
    public ExplainableCentroid explainableDefuzzification(int discretization)
    {
        return doSwitchIndexDefuzzification(discretization, true);
    }
    
    /**
     * Runs the inference and defuzzifies the output using the switch index approach. If buildExplanation is true, also produces an explanation that shows which rules generated the endpoints of the interval centroid
     * @param discretization The discretization level to use
     * @return The interval centroid with an explanation or a CentroidExplanation with only the interval centroid value if no explanation was requested
     */
    private ExplainableCentroid doSwitchIndexDefuzzification(int discretization, boolean buildExplanation)
    {
        //Each element of the map is of the form <Consequent MF, Pair<Firing Strenghts, Pair<Rule with the highest lowerbound firing w.r.t. Consequent MF, Rule with the highest upperbound firing w.r.t. Consequent MF>>>
        HashMap<CIT2, Pair<Tuple, Pair<CIT2_Rule, CIT2_Rule>>> truncation_heights=getExplainableFiringIntervals();
        //The array of consequents used in the rulebase
        CIT2[] consequent_mfs=truncation_heights.keySet().toArray(new CIT2[0]);
        //The Output variable of the system
        Output output=CIT2Rules.get(0).getConsequents()[0].getOutput();
        //Sorts the array in ascending order by the left endpoint of their support set. This is needed to give an index to each consequent set that partitions the output variable
        Arrays.sort(consequent_mfs, CIT2Comparator);
        T1MF_Interface current_es, aes_after_inference, min_aes=null, max_aes=null;
        double left_endpoint_centroid=Integer.MAX_VALUE, right_endpoint_centroid=Integer.MIN_VALUE, current_centroid;
        Tuple result=new Tuple(Double.NaN, Double.NaN);
        CIT2 current_ct2;
        int left_switch_index=-1, right_switch_index=-1;
        //Boolean variable that states whether the current iteration is computing the left or right endpoint of the centroid
        boolean computingLeftEndpoint=false;
        //Run twice, once to compute the left endpoint of the centroid and once to compute the right endpoint of the centroid
        for(int i=0;i<2;i++)
        {
            //Test all the possible switch index value to determine the AES with the lowest/highest T1 centroid value
            for(int curr_switch_index=0;curr_switch_index<consequent_mfs.length;curr_switch_index++)
            {
                current_es=null;
                //To build the AES, loop through all the consequents...
                for(int k=0;k<consequent_mfs.length;k++)
                {
                    current_ct2=consequent_mfs[k];
                    //Appliy the inference operator on an AES of the current consequent
                    aes_after_inference=inferenceOnConsequentAES(computingLeftEndpoint, curr_switch_index, k, current_ct2, truncation_heights.get(current_ct2).getLeft());
                    if(current_es==null)
                        current_es=aes_after_inference;
                    else
                    {
                        //Do the union of all the inferenced AES generated
                        if(aes_after_inference!=null)
                            current_es=new T1MF_Union(current_es, aes_after_inference);
                    }
                }
                if(current_es==null)
                    continue;
                //The AES generated after the union, is defuzzified
                current_centroid=current_es.getDefuzzifiedCentroid(discretization);
                //Test if its centroid values is a new candidate for the left endpoint of the interval centroid
                if(current_centroid<=left_endpoint_centroid)
                {
                    //Update the candidate for the left endpoint of the interval centroid
                    left_endpoint_centroid=current_centroid;
                    min_aes=current_es;
                    left_switch_index=curr_switch_index;
                }
                //Test if its centroid values is a new candidate for the left endpoint of the interval centroid
                if(current_centroid>=right_endpoint_centroid)
                {
                    //Update the candidate for the right endpoint of the interval centroid
                    right_endpoint_centroid=current_centroid;
                    max_aes=current_es;
                    right_switch_index=curr_switch_index;
                }
            }
            computingLeftEndpoint=true;
        }
        //Check that valid value have been produced (i.e. that left_endpoint and right_endpoint have been updated at least one)
        if(left_endpoint_centroid!=Integer.MAX_VALUE)
            result.setLeft(left_endpoint_centroid);
        else
            result.setLeft(Double.NaN);
        if(right_endpoint_centroid!=Integer.MIN_VALUE)
            result.setRight(right_endpoint_centroid);
        else
            result.setRight(Double.NaN);
        if(max_aes!=null)
            max_aes.setName("Max AES");
        if(min_aes!=null)
            min_aes.setName("Min AES");
        //If an explanation is requested, build it
        if(buildExplanation)
            return buildExplanation(result, consequent_mfs, truncation_heights, left_switch_index, right_switch_index, min_aes, max_aes, output);
        else
            //Otherwise, just return the centroid interval
            return new ExplainableCentroid(result);
    }
    
    
    /**
     * Builds the explanation for the centroid value given as an input. The explanation shows the rules that contributed to the generation of the endpoints of the centroid
     * @param centroid_value The centroid value for which an explanation must be built
     * @param consequent_mfs The array with all the consequent membership functions in the rulebase
     * @param firing_intervals The firing intervals with the consequent membership functions they refer to
     * @param left_switch_index The switch index that determines the left endpoint of the interval centroid
     * @param right_switch_index The switch index that determines the right endpoint of the interval centroid
     * @param min_aes The AES that determines the left endpoint of the interval centroid
     * @param max_aes The AES that determines the right endpoint of the interval centroid
     * @param output The output variable this centroid refers to
     * @return A CentroidExplanation containing the explanation for centroid_value
     */
    private ExplainableCentroid buildExplanation(Tuple centroid_value, CIT2[] consequent_mfs, HashMap<CIT2, Pair<Tuple, Pair<CIT2_Rule, CIT2_Rule>>> firing_intervals,
            int left_switch_index, int right_switch_index, T1MF_Interface min_aes, T1MF_Interface max_aes, Output output)
    {
        LinkedList<Pair<CIT2, RuleExplanation>> explaination_left_endpoint=new LinkedList<>(); 
        LinkedList<Pair<CIT2, RuleExplanation>> explaination_right_endpoint=new LinkedList<>();
        //Build explainations for left endpoint
        CIT2 current_consequent;
        CIT2_Rule current_rule;
        //Before the switch index...
        for(int i=0;i<left_switch_index;i++)
        {
            current_consequent=consequent_mfs[i];
            //... note that the left endpoint of the firing strength of the rule has been used...
            current_rule=firing_intervals.get(current_consequent).getRight().getRight();
            explaination_left_endpoint.add(new Pair(current_consequent, new RuleExplanation(current_rule, true)));
        }
        //...after the switch index...
        for(int i=left_switch_index;i<consequent_mfs.length;i++)
        {
            current_consequent=consequent_mfs[i];
            //...note that the right endpoint of the firing strenght of the rule has been used
            current_rule=firing_intervals.get(current_consequent).getRight().getLeft();
            explaination_left_endpoint.add(new Pair(current_consequent, new RuleExplanation(current_rule, false)));
        }
        //Build explainations for the right endpoint
        //Before the switch index...
        for(int i=0;i<right_switch_index;i++)
        {
            current_consequent=consequent_mfs[i];
            //...note that the right endpoint of the firing strength of the rule has been used...
            current_rule=firing_intervals.get(current_consequent).getRight().getLeft();
            explaination_right_endpoint.add(new Pair(current_consequent, new RuleExplanation(current_rule, false)));
        }
        //After the switch index...
        for(int i=right_switch_index;i<consequent_mfs.length;i++)
        {
            //...note that the left endpoint of the firing strength of the rule has been used
            current_consequent=consequent_mfs[i];
            current_rule=firing_intervals.get(current_consequent).getRight().getRight();
            explaination_right_endpoint.add(new Pair(current_consequent, new RuleExplanation(current_rule, true)));
        }
        return new ExplainableCentroid(centroid_value, explaination_left_endpoint, explaination_right_endpoint, min_aes, max_aes, output);
    }
    
    /**
     * Computes the interval centroid using the switch index approach
     * @param discretization The discretization level to use
     * @return The interval centroid as a Tuple
     */
    public Tuple switchIndexDefuzzification(int discretization)
    {
        //Run switch index saying that an explanation must not be produced
        return doSwitchIndexDefuzzification(discretization, false).getIntervalCentroid();
    }
    
//    Switch centroid function that does not generate the explanation    
//    public Tuple switchIndexCentroid(int discretization)
//    {
//        HashMap<CIT2, Tuple> truncation_heights=getGroupedTruncationHeights();
//        CIT2[] consequent_mfs=truncation_heights.keySet().toArray(new CIT2[0]);
//        Arrays.sort(consequent_mfs, CIT2Comparator);
//        T1MF_Interface current_es, chosen_es, min, max;
//        double min_centroid=Integer.MAX_VALUE, max_centroid=Integer.MIN_VALUE, current_centroid;
//        Tuple result=new Tuple(Double.NaN, Double.NaN);
//        CIT2 current_ct2;
//        for(int is_upper_to_lower=0;is_upper_to_lower<2;is_upper_to_lower++)
//        {
//            for(int curr_switch_set=0;curr_switch_set<consequent_mfs.length;curr_switch_set++)
//            {
//                current_es=null;
//                for(int k=0;k<consequent_mfs.length;k++)
//                {
//                    current_ct2=consequent_mfs[k];
//                    chosen_es=getChosenES(is_upper_to_lower, curr_switch_set, k, current_ct2, truncation_heights.get(current_ct2));
//                    if(current_es==null)
//                        current_es=chosen_es;
//                    else
//                    {
//                        if(chosen_es!=null)
//                            current_es=new T1MF_Union(current_es, chosen_es);
//                    }
//                }
//                if(current_es==null)
//                    continue;
//                current_centroid=current_es.getDefuzzifiedCentroid(discretization);
//                if(current_centroid<=min_centroid)
//                {
//                    min_centroid=current_centroid;
//                    min=current_es;
//                }
//                if(current_centroid>=max_centroid)
//                {
//                    max_centroid=current_centroid;
//                    max=current_es;
//                }
//            }  
//        }
//        if(min_centroid!=Integer.MAX_VALUE)
//            result.setLeft(min_centroid);
//        else
//            result.setLeft(Double.NaN);
//        if(max_centroid!=Integer.MIN_VALUE)
//            result.setRight(max_centroid);
//        else
//            
//            result.setRight(Double.NaN);
//        return result;
//    }
    
    
    /**
     * Applies the inference operator (implemented using the minimum operator) to an AES of a consequent CIT2 membership function
     * @param isLeftEndpoint States whether this function has been used in the generation of the left endpoint of the interval centroid
     * @param curr_switch_index The current switch index value
     * @param current_index The index of set
     * @param set The CIT2 consequent membership function to which the inference operator will be applied
     * @param truncations The truncation interval given by the firing of a rule
     * @return An AES of set to which the inference operator has been applied
     */
    private T1MF_Interface inferenceOnConsequentAES(boolean isLeftEndpoint, int curr_switch_index, int current_index, CIT2 set, Tuple truncations)
    {
        double truncation_height;
        T1MF_Interface chosen_es;
        //If the algorithm is computing the right endpoint of the interval centroid...
        if(isLeftEndpoint)
        {
            //... get the leftmost AES...
            chosen_es=set.getEmbeddedSets()[0];
            //... if the index of set is bigger than (or equal to) the switch index, use the lower value for the inference, otherwise use the higher one
            if(current_index>=curr_switch_index)
                truncation_height=truncations.getLeft();
            else
                truncation_height=truncations.getRight();
        }
        //If it is computing the right endpoint of the interval centroid...
        else
        {
            //...get the rightmost AES...
            chosen_es=set.getEmbeddedSets()[set.getEmbeddedSets().length-1];
            //... if the index of set is bigger than (or equal to) the switch index, use the higher value for the inference, otherwise use lower higher one
            if(current_index>=curr_switch_index)
            {
                truncation_height=truncations.getRight();
            }
            else
            {
                truncation_height=truncations.getLeft();   
            }
        }
        if(truncation_height==0)
            return null;
        //"Truncate" the AES (i.e. do the inference using the minimum operator) and return it
        return new T1MF_InferenceResult(set.getName(), chosen_es, truncation_height);
    }  
 
    
    /**
     * Determines the defuzzified interval centroid using the sampling technique.
     * samples number of instances of the CIT2 system are created: in each one each of the CIT2 sets involved is substituted with one of its AES, generating a T1 rulebase.
     * The minimum and maximum centroid values obtained among all the samples determine the left and right endpoints of the final interval centroid given as an output
     * @param samples Number of samples to produce
     * @return The interval centroid obtained through the sampling technique
     */
    public Tuple samplingDefuzzification(int samples)
    {
        T1_Rulebase sampled_rulebase;
        //Variable that checks if none of the samples systems produces a valid output
        boolean all_nan=true;
        Tuple interval_centroid=new Tuple(Integer.MAX_VALUE, Integer.MIN_VALUE);
        double current_centroid;
        Output single_output=CIT2Rules.get(0).getConsequents()[0].getOutput();
        //Build all the samples...
        for(int i=0;i<samples;i++)
        {
            //Sample a T1 rulebase from the CIT2 one
            sampled_rulebase=getSampledRulebase();
            //Get the T1 centroid value
            current_centroid=sampled_rulebase.centroidDefuzzification().get(single_output);
            if(Double.isNaN(current_centroid))
                continue;
            all_nan=false;
            //Update the candidate interval centroid, if the current value is greater than the right endpoint or smaller than the left endpoint
            interval_centroid.setLeft(Math.min(current_centroid, interval_centroid.getLeft()));
            interval_centroid.setRight(Math.max(current_centroid, interval_centroid.getRight()));
        }
        //Checks if any of the systems gave a valid output
        if(all_nan)
        {
            interval_centroid.setLeft(Double.NaN);
            interval_centroid.setRight(Double.NaN);
        }
        //Return the final centroid
        return interval_centroid;
    }
    
    /**
     * Samples a T1 rulebase from the current CIT2 one. The sample is obtained by replacing each of the CIT2 in the rulebase with one of its AES chosen randomly
     * @return The sampled T1 rulebase
     */
    private T1_Rulebase getSampledRulebase()
    {
        //Maps each CIT2 set into one of its AES chosen randomly
        HashMap<CIT2, T1MF_Interface> ct2_samples=new HashMap<>();
        ArrayList<T1_Rule> t1_rules=new ArrayList(CIT2Rules.size());
        CIT2 current_ct2;
        int i;
        T1_Antecedent[] current_antecedent_samples;
        T1_Consequent[] current_consequent_samples;
        //For each rule in the rulebase...
        for(CIT2_Rule current_rule : CIT2Rules)
        {
            current_antecedent_samples=new T1_Antecedent[current_rule.getAntecedents().length];
            current_consequent_samples=new T1_Consequent[current_rule.getConsequents().length];
            i=0;
            //...for each of the antecedents...
            for(CIT2_Antecedent current_antecedent : current_rule.getAntecedents())
            {
                //...if the CIT2 set hasn't been sampled, get a random AES
                current_ct2=current_antecedent.getCIT2();
                if(!ct2_samples.containsKey(current_ct2))
                    ct2_samples.put(current_ct2, current_ct2.getRandomAES());
                //Create a T1 antecedent with the sampled AES
                current_antecedent_samples[i]=new T1_Antecedent(ct2_samples.get(current_ct2), current_antecedent.getInput());
                i++;
            }
            i=0;
            //...for each of the consequents...
            for(CIT2_Consequent current_consequent : current_rule.getConsequents())
            {
                //...if the CIT2 set hasn't been sampled, get a random AES
                current_ct2=current_consequent.getCIT2();
                if(!ct2_samples.containsKey(current_ct2))
                    ct2_samples.put(current_ct2, current_ct2.getRandomAES());
                //Create a T1 consequent with the sampled AES
                current_consequent_samples[i]=new T1_Consequent(ct2_samples.get(current_ct2), current_consequent.getOutput());
                i++;
            }
            //Create a new T1 rule with the sampled antecedents and consequents
            t1_rules.add(new T1_Rule(current_antecedent_samples, current_consequent_samples));
        }
        //Create the T1 rulebase and add the rules to it
        T1_Rulebase sampled_rulebase=new T1_Rulebase(CIT2Rules.size());
        for(T1_Rule rule : t1_rules)
            sampled_rulebase.addRule(rule);
        return sampled_rulebase;
    }
 
    /**
     * Maps each CIT2 consequent set to the rules that generate their maximum firing interval, i.e. the rules that generate the highest left and right endpoints of the firing interval
     * @return 
     */
    private HashMap<CIT2, Pair<Tuple, Pair<CIT2_Rule, CIT2_Rule>>> getExplainableFiringIntervals()
    {
        //<Consequent MF, Pair<Firing Strenghts, Pair<Rule for LB, Rule for UB>>>
        HashMap<CIT2, Pair<Tuple, Pair<CIT2_Rule, CIT2_Rule>>> firing_intervals=new HashMap<>();
        Tuple current_cuts, old_firing_interval;
        Pair<Tuple, Pair<CIT2_Rule, CIT2_Rule>> old_map_value;
        //The rules generating the UB and LB truncation heights
        Pair<CIT2_Rule, CIT2_Rule> old_rules_generating_firing;
        CIT2_Rule[] new_rules=new CIT2_Rule[2];
        CIT2 current_consequent;
        //Loop through the rules
        for(CIT2_Rule current_rule : CIT2Rules)
        {
            //Get the consequent of the currrent rule
            current_consequent=current_rule.getConsequents()[0].getCIT2();
            //Get the firing interval of the current rule
            current_cuts=current_rule.getFiringStrength(MinimumTNorm.factoryMethod());
            //If there was no firing interval in the map for the current consequent CIT2, add it
            if(firing_intervals.get(current_consequent)==null)
                firing_intervals.put(current_consequent, new Pair(current_cuts, new Pair(current_rule, current_rule)));
            //Otherwise, check if an update is needed for the firing interval for the current consequent set in the map
            else
            {
                //Get the value currently in the map
                old_map_value=firing_intervals.get(current_consequent);
                //Store the rules that generate the current firing interval
                old_rules_generating_firing=old_map_value.getRight();
                //Store the old firing interval
                old_firing_interval=old_map_value.getLeft();
                //The array containing the rules that generate the left and right endpoints of the firing interval
                new_rules[0]=old_rules_generating_firing.getLeft();
                new_rules[1]=old_rules_generating_firing.getRight();
                //Check for lowerbound update
                if(old_firing_interval.getLeft()<current_cuts.getLeft())
                {
                    old_firing_interval.setLeft(current_cuts.getLeft());
                    new_rules[0]=current_rule;
                }
                //Check for upperbound update
                if(old_firing_interval.getRight()<current_cuts.getRight())
                {
                    old_firing_interval.setRight(current_cuts.getRight());
                    new_rules[1]=current_rule;
                }
                //Update the map
                firing_intervals.put(current_consequent, new Pair(old_firing_interval, new Pair(new_rules[0], new_rules[1])));
            }
        }
        return firing_intervals;
    }
    
    /**
     * Computes the fired FOU (i.e. the FOU of the fuzzy output) of the current rulebase
     * @return The IT2 FS representing the fired FOU
     */
    public IntervalT2MF_Interface getFiredFOU()
    {
        IntervalT2MF_Interface total_fired_fou=null, current_rule_fired_fou=null;
        //For each rule...
        for(CIT2_Rule current_rule : CIT2Rules)
        {
            //... compute the fired FOU of the single rule...
            current_rule_fired_fou=current_rule.getFiredFOU();
            //... and then update the total_fired_fou by doing the union
            if(current_rule_fired_fou!=null)
            {
                if(total_fired_fou==null)
                    total_fired_fou=current_rule_fired_fou;
                else
                    total_fired_fou=new IntervalT2MF_Union(total_fired_fou, current_rule_fired_fou);
            }
        }
        return total_fired_fou;
    }
   
     /**
      * Plots the variables (input and output ones) involved in the system. For each variable, the membership functions partitioning it are shown
      */
     public void plotSystem()
     {
        HashMap<Input, HashSet<CIT2>> input_to_sets=new HashMap<>();
        HashMap<Output, HashSet<CIT2>> output_to_sets=new HashMap<>();
        HashSet<CIT2> current_mapping;
        CIT2 current_set;
        Input current_input;
        Output current_output;
            for(CIT2_Rule current_rule : CIT2Rules)
            {
                for(CIT2_Antecedent current_antecendent : current_rule.getAntecedents())
                {
                    current_set=current_antecendent.getCIT2();
                    current_input=current_antecendent.getInput();
                    current_mapping=input_to_sets.get(current_input);
                    if(current_mapping==null)
                        current_mapping=new HashSet<>();
                    current_mapping.add(current_set);
                    input_to_sets.put(current_input, current_mapping);
                }
                for(CIT2_Consequent current_consequent : current_rule.getConsequents())
                {
                    current_set=current_consequent.getCIT2();
                    current_output=current_consequent.getOutput();
                    current_mapping=output_to_sets.get(current_output);
                    if(current_mapping==null)
                        current_mapping=new HashSet<>();
                    current_mapping.add(current_set);
                    output_to_sets.put(current_output, current_mapping);
                }
            }
            for(Input input : input_to_sets.keySet())
                JFreeChartPlotter.plotMFs("Partitioning of "+input.getName(), input_to_sets.get(input).toArray(new CIT2[0]), input.getDomain(), 1000);
            for(Output output : output_to_sets.keySet())
                JFreeChartPlotter.plotMFs("Partitioning of "+output.getName(), output_to_sets.get(output).toArray(new CIT2[0]), output.getDomain(), 1000);
     }
     
     /**
      * Builds a printable String showing the rules in the rulebase. For each rule the toString() method is invoked to build the final output String
      * @return The String showing the rules in the rulebase
      */
     @Override
     public String toString()
     {
         StringBuilder result=new StringBuilder();
         for(CIT2_Rule current_rule : CIT2Rules)
         {
             result.append(current_rule.toString());
             result.append("\n");
         }
         return result.toString();
     }
  
}

enum SwitchSetType{UPPER_TO_LOWER, LOWER_TO_UPPER}