/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import CIT2.CIT2;
import CIT2.CIT2_Antecedent;
import CIT2.CIT2_Consequent;
import CIT2.CIT2_Rule;
import CIT2.CIT2_Rulebase;
import CIT2_Explanations.ExplainableCentroid;
import generic.Input;
import generic.Output;
import generic.Tuple;
import intervalType2.sets.IntervalT2MF_Interface;
import tools.JFreeChartPlotter;
import CIT2_Generator.*;
import intervalType2.sets.IntervalT2MF_Intersection;
import intervalType2.sets.IntervalT2MF_Union;
import type1.sets.T1MF_Interface;

/**
 *
 * @author Pasquale
 */
public class Example {
    
    public static void main(String[] args)
    {
        new Example().runExample();
    }
    public void runExample()
    {

        double shifting_size_1=1, shifting_size_2=0.5;
        //Define the inputs
        Input food = new Input("Food Quality", new Tuple(0,10));      //a rating given by a person between 0 and 10
        Input service = new Input("Service Level", new Tuple(0,10));  //a rating given by a person between 0 and 10
        Output tip = new Output("Tip", new Tuple(0,30));               //a percentage for the tip

        //Set up the membership functions for each input and output
        T1MF_Generator_Triangular badFoodMF = new T1MF_Generator_Triangular("Bad",0.0, 0.0, 10);
        badFoodMF.setLeftShoulder(true);
        T1MF_Generator_Triangular greatFoodMF = new T1MF_Generator_Triangular("Great",0.0, 10.0, 10.0);
        greatFoodMF.setRightShoulder(true);
        
        T1MF_Generator_Gauangle unfriendlyServiceMF = 
                new T1MF_Generator_Gauangle("Unfriendly",0.0, 0.0, 6.0);
        unfriendlyServiceMF.setLeftShoulder(true);
        T1MF_Generator_Gauangle okServiceMF = 
                new T1MF_Generator_Gauangle("OK",2.5, 5.0, 7.5);
        T1MF_Generator_Gauangle friendlyServiceMF = 
                new T1MF_Generator_Gauangle("Friendly",4.0, 10.0, 10.0);
        friendlyServiceMF.setRightShoulder(true);

        T1MF_Generator_Gaussian lowTipMF = new T1MF_Generator_Gaussian("Low", 0.0, 6.0);
        lowTipMF.setLeftShoulder(true);
        T1MF_Generator_Gaussian mediumTipMF = new T1MF_Generator_Gaussian("Medium", 15.0, 6.0);
        T1MF_Generator_Gaussian highTipMF = new T1MF_Generator_Gaussian("High", 30.0, 6.0);
        highTipMF.setRightShoulder(true);
        
        
        //Generate the CIT2 MFs using the T1 sets as generator sets
        CIT2 cit2_badFoodMF = new CIT2(badFoodMF.getName(), 
                badFoodMF, shifting_size_2);
        CIT2 cit2_greatFoodMF = new CIT2(greatFoodMF.getName(),
                greatFoodMF, shifting_size_2);
        CIT2 cit2_unfriendlyServiceMF =  new CIT2(unfriendlyServiceMF.getName(), 
                unfriendlyServiceMF, shifting_size_2);
        CIT2 cit2_okServiceMF = new CIT2(okServiceMF.getName(), 
                okServiceMF, shifting_size_2);
        CIT2 cit2_friendlyServiceMF = new CIT2(friendlyServiceMF.getName(), 
                friendlyServiceMF, shifting_size_2);

        CIT2 cit2_lowTipMF = new CIT2(lowTipMF.getName(), lowTipMF, shifting_size_1);
        CIT2 cit2_mediumTipMF = new CIT2(mediumTipMF.getName(), mediumTipMF, shifting_size_1);
        CIT2 cit2_highTipMF = new CIT2(highTipMF.getName(), highTipMF, shifting_size_1);
        
        //Set up the antecedents and consequents
        CIT2_Antecedent badFood = 
                new CIT2_Antecedent(cit2_badFoodMF, food);
        CIT2_Antecedent greatFood = 
                new CIT2_Antecedent(cit2_greatFoodMF, food);

        CIT2_Antecedent unfriendlyService = 
                new CIT2_Antecedent(cit2_unfriendlyServiceMF, service);
        CIT2_Antecedent okService = 
                new CIT2_Antecedent(cit2_okServiceMF, service);
        CIT2_Antecedent friendlyService = 
                new CIT2_Antecedent(cit2_friendlyServiceMF, service);

        CIT2_Consequent lowTip = new CIT2_Consequent(cit2_lowTipMF, tip);
        CIT2_Consequent mediumTip = new CIT2_Consequent(cit2_mediumTipMF, tip);
        CIT2_Consequent highTip = new CIT2_Consequent(cit2_highTipMF, tip);
        
        //Set up the rulebase and add rules
        CIT2_Rulebase rulebase = new CIT2_Rulebase();
        rulebase.addRule(new CIT2_Rule(
                new CIT2_Antecedent[]{badFood, unfriendlyService}, lowTip));
        rulebase.addRule(new CIT2_Rule(
                new CIT2_Antecedent[]{badFood, okService}, lowTip));
        rulebase.addRule(new CIT2_Rule(
                new CIT2_Antecedent[]{badFood, friendlyService}, mediumTip));
        rulebase.addRule(new CIT2_Rule(
                new CIT2_Antecedent[]{greatFood, unfriendlyService}, lowTip));
        rulebase.addRule(new CIT2_Rule(
                new CIT2_Antecedent[]{greatFood, okService}, mediumTip));
        rulebase.addRule(new CIT2_Rule(
                new CIT2_Antecedent[]{greatFood, friendlyService}, highTip));
        
        //Set the input values
        food.setInput(7);
        service.setInput(8);
        
        Tuple constrained_centroid_sampling=rulebase.samplingDefuzzification(50);
        
        Tuple constrained_centroid_si=rulebase.switchIndexDefuzzification(100);
        
        ExplainableCentroid result=rulebase.explainableDefuzzification(100);

        JFreeChartPlotter.plotMFs("Food partitioning", new CIT2[]{cit2_badFoodMF,
            cit2_greatFoodMF},food.getDomain(), 1000);
        JFreeChartPlotter.plotMFs("Service partitioning", 
                new CIT2[]{cit2_friendlyServiceMF, cit2_okServiceMF, 
                    cit2_unfriendlyServiceMF}, service.getDomain(), 1000);
        JFreeChartPlotter.plotMFs("Tip partitioning", new CIT2[]{cit2_lowTipMF,
            cit2_mediumTipMF, cit2_highTipMF}, tip.getDomain(), 1000);
        
        T1MF_Interface left_aes=result.getMax_es(), right_aes=result.getMin_es();
        
        JFreeChartPlotter.plotMFs("Fired FOU", new 
                IntervalT2MF_Interface[]{rulebase.getFiredFOU()}, 
                tip.getDomain(), 1000);
        JFreeChartPlotter.plotMFs("AES determining the endpoints", new 
                T1MF_Interface[]{left_aes, right_aes}, tip.getDomain(), 1000);
        System.out.println("The recommended tip percentage is in the range: "
                +result.getConstrainedCentroid());
        //Print the explanations
        System.out.println(result.printableExplanation());
    }
    

}
