/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import cit2.CIT2;
import cit2.CIT2_Antecedent;
import cit2.CIT2_Consequent;
import cit2.CIT2_Rule;
import cit2.CIT2_Rulebase;
import cit2.ExplainableCentroid;
import generic.Input;
import generic.Output;
import generic.Tuple;
import intervalType2.sets.IntervalT2MF_Interface;
import tools.JFreeChartPlotter;
import type1.sets.T1MF_Gauangle;
import type1.sets.T1MF_Gaussian;
import type1.sets.T1MF_Interface;
import type1.sets.T1MF_Triangular;
import type1.system.T1_Antecedent;
import type1.system.T1_Consequent;
import type1.system.T1_Rule;
import type1.system.T1_Rulebase;

/**
 *
 * @author User
 */
public class Example {
    
    public static void main(String[] args)
    {
        new Example().runExample();
    }
    public void runExample()
    {
        Input food, service;
        Output tip;
        double shifting_size_1=1, shifting_size_2=0.5;
        //Define the inputs
        food = new Input("Food Quality", new Tuple(0,10));      //a rating given by a person between 0 and 10
        service = new Input("Service Level", new Tuple(0,10));  //a rating given by a person between 0 and 10
        tip = new Output("Tip", new Tuple(0,30));               //a percentage for the tip

        //Set up the membership functions (MFs) for each input and output
        T1MF_Triangular badFoodMF = new T1MF_Triangular("MF for bad food",0.0, 0.0, 10.0);
        T1MF_Triangular greatFoodMF = new T1MF_Triangular("MF for great food",0.0, 10.0, 10.0);
        
        T1MF_Gauangle unfriendlyServiceMF = new T1MF_Gauangle("MF for unfriendly service",0.0, 0.0, 6.0);
        unfriendlyServiceMF.setLeftShoulder(true);
        T1MF_Gauangle okServiceMF = new T1MF_Gauangle("MF for ok service",2.5, 5.0, 7.5);
        T1MF_Gauangle friendlyServiceMF = new T1MF_Gauangle("MF for friendly service",4.0, 10.0, 10.0);
        friendlyServiceMF.setRightShoulder(true);

        T1MF_Gaussian lowTipMF = new T1MF_Gaussian("Low tip", 0.0, 6.0);
        T1MF_Gaussian mediumTipMF = new T1MF_Gaussian("Medium tip", 15.0, 6.0);
        T1MF_Gaussian highTipMF = new T1MF_Gaussian("High tip", 30.0, 6.0);
        
        //Generate the CIT2 MFs using the T1 sets as generator sets
        CIT2 cit2_badFoodMF = new CIT2(badFoodMF.getName(), badFoodMF, shifting_size_2);
        CIT2 cit2_greatFoodMF = new CIT2(greatFoodMF.getName(), greatFoodMF, shifting_size_2);

        CIT2 cit2_unfriendlyServiceMF = new CIT2(unfriendlyServiceMF.getName(), unfriendlyServiceMF, shifting_size_2);
        CIT2 cit2_okServiceMF = new CIT2(okServiceMF.getName(), okServiceMF, shifting_size_2);
        CIT2 cit2_friendlyServiceMF = new CIT2(friendlyServiceMF.getName(), friendlyServiceMF, shifting_size_2);

        CIT2 cit2_lowTipMF = new CIT2(lowTipMF.getName(), lowTipMF, shifting_size_1);
        CIT2 cit2_mediumTipMF = new CIT2(mediumTipMF.getName(), mediumTipMF, shifting_size_1);
        CIT2 cit2_highTipMF = new CIT2(highTipMF.getName(), highTipMF, shifting_size_1);
        
        //Set up the antecedents and consequents - note how the inputs are associated...
        CIT2_Antecedent badFood = new CIT2_Antecedent(cit2_badFoodMF, food);
        CIT2_Antecedent greatFood = new CIT2_Antecedent(cit2_greatFoodMF, food);

        CIT2_Antecedent unfriendlyService = new CIT2_Antecedent(cit2_unfriendlyServiceMF, service);
        CIT2_Antecedent okService = new CIT2_Antecedent(cit2_okServiceMF, service);
        CIT2_Antecedent friendlyService = new CIT2_Antecedent(cit2_friendlyServiceMF, service);

        CIT2_Consequent lowTip = new CIT2_Consequent(cit2_lowTipMF, tip);
        CIT2_Consequent mediumTip = new CIT2_Consequent(cit2_mediumTipMF, tip);
        CIT2_Consequent highTip = new CIT2_Consequent(cit2_highTipMF, tip);
        
        //Set up the rulebase and add rules
        CIT2_Rulebase rulebase = new CIT2_Rulebase();
        rulebase.addRule(new CIT2_Rule(new CIT2_Antecedent[]{badFood, unfriendlyService}, lowTip));
        rulebase.addRule(new CIT2_Rule(new CIT2_Antecedent[]{badFood, okService}, lowTip));
        rulebase.addRule(new CIT2_Rule(new CIT2_Antecedent[]{badFood, friendlyService}, mediumTip));
        rulebase.addRule(new CIT2_Rule(new CIT2_Antecedent[]{greatFood, unfriendlyService}, lowTip));
        rulebase.addRule(new CIT2_Rule(new CIT2_Antecedent[]{greatFood, okService}, mediumTip));
        rulebase.addRule(new CIT2_Rule(new CIT2_Antecedent[]{greatFood, friendlyService}, highTip));
        
        //Set the input values
        food.setInput(7);
        service.setInput(8);
        
        ExplainableCentroid result=rulebase.explainableDefuzzification(1000);
        T1MF_Interface left_aes=result.getMax_es(), right_aes=result.getMin_es();
        JFreeChartPlotter.plotMFs("Food partitioning", new CIT2[]{cit2_badFoodMF, cit2_greatFoodMF}, food.getDomain(), 1000);
        JFreeChartPlotter.plotMFs("Service partitioning", new CIT2[]{cit2_friendlyServiceMF, cit2_okServiceMF, cit2_unfriendlyServiceMF}, service.getDomain(), 1000);
        JFreeChartPlotter.plotMFs("Tip partitioning", new CIT2[]{cit2_lowTipMF, cit2_mediumTipMF, cit2_highTipMF}, tip.getDomain(), 1000);
        JFreeChartPlotter.plotMFs("Resulting FOU", new IntervalT2MF_Interface[]{rulebase.getFiredFOU()}, tip.getDomain(), 1000);
        JFreeChartPlotter.plotMFs("AES determining the endpoints", new T1MF_Interface[]{left_aes, right_aes}, tip.getDomain(), 1000);
        System.out.println("The recommended tip percentage is in the range: "+result.getIntervalCentroid());
        //Print the explanations
        System.out.println(result.printableExplanation());
        
    }
}
