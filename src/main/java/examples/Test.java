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
import generic.Input;
import generic.Output;
import generic.Tuple;
import intervalType2.system.IT2_Rulebase;
import java.util.Random;
import tools.JFreeChartPlotter;
import type1.sets.T1MF_Interface;
import type1.sets.T1MF_Triangular;

/**
 *
 * @author User
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        test();
    }
    public static void test(){
     Input input_1=new Input("1.", new Tuple(-2,12));
        Input input_2=new Input("2.", new Tuple(-2,12));
        Input input_3=new Input("3.", new Tuple(-2,12));
        Input input_4=new Input("4.", new Tuple(-2,12));
        Input input_5=new Input("5.", new Tuple(-2,12));
        Input input_6=new Input("6.", new Tuple(-2,12));
        Input input_7=new Input("7.", new Tuple(-2,12));
        Output output=new Output("", new Tuple(-2,12));
        T1MF_Triangular one=new T1MF_Triangular("ONE", 0,1,2);
        T1MF_Triangular two=new T1MF_Triangular("TWO", 1,2,3);
        T1MF_Triangular three=new T1MF_Triangular("THREE",2,3,4);
        
        T1MF_Triangular four=new T1MF_Triangular("FOUR", 3,4,5);
        T1MF_Triangular five=new T1MF_Triangular("FIVE", 4,5,6);
        T1MF_Triangular six=new T1MF_Triangular("SIX", 5,6,7);
        T1MF_Triangular seven=new T1MF_Triangular("SEVEN", 6,7,8);
        
        CIT2 one_ct2=new CIT2("1", one, 1, 1, 0.25);
        CIT2 two_ct2=new CIT2("2", two, 1,1,0.25);
        CIT2 three_ct2=new CIT2("3", three, 1,1,0.25);
        CIT2 four_ct2=new CIT2("4", four,1,1,0.25);
        CIT2 five_ct2=new CIT2("5", five, 1,1,0.25);
        CIT2 six_ct2=new CIT2("6", six, 1,1,0.25);
        CIT2 seven_ct2=new CIT2("7", seven,1,1,0.25);
        one_ct2.plotCIT2(input_1.getDomain(), 1000);
        //CIT2.plotCIT2Boundaries("", new CIT2[]{one_ct2, two_ct2, three_ct2, four_ct2, five_ct2, six_ct2, seven_ct2}, new Tuple(0,10), 1000);

        CIT2_Antecedent ant_1=new CIT2_Antecedent(one_ct2, input_1);
        CIT2_Antecedent ant_2=new CIT2_Antecedent(two_ct2, input_2);
        CIT2_Antecedent ant_3=new CIT2_Antecedent(three_ct2, input_3);
        CIT2_Antecedent ant_4=new CIT2_Antecedent(four_ct2, input_4);
        CIT2_Antecedent ant_5=new CIT2_Antecedent(five_ct2, input_5);
        CIT2_Antecedent ant_6=new CIT2_Antecedent(six_ct2, input_6);
        CIT2_Antecedent ant_7=new CIT2_Antecedent(seven_ct2, input_7);
        CIT2_Consequent cons_1=new CIT2_Consequent(one_ct2, output);
        CIT2_Consequent cons_2=new CIT2_Consequent(two_ct2, output);
        CIT2_Consequent cons_3=new CIT2_Consequent(three_ct2, output);
        CIT2_Consequent cons_4=new CIT2_Consequent(four_ct2, output);
        CIT2_Consequent cons_5=new CIT2_Consequent(five_ct2, output);
        CIT2_Consequent cons_6=new CIT2_Consequent(six_ct2, output);
        CIT2_Consequent cons_7=new CIT2_Consequent(seven_ct2, output);
        
        CIT2_Rule[] rules=new CIT2_Rule[7];
        rules[0]=new CIT2_Rule(new CIT2_Antecedent[]{ant_1}, cons_1);
        rules[1]=new CIT2_Rule(new CIT2_Antecedent[]{ant_2}, cons_2);
        rules[2]=new CIT2_Rule(new CIT2_Antecedent[]{ant_3},cons_3);
        rules[3]=new CIT2_Rule(new CIT2_Antecedent[]{ant_4}, cons_4);
        rules[4]=new CIT2_Rule(new CIT2_Antecedent[]{ant_5}, cons_5);
        rules[5]=new CIT2_Rule(new CIT2_Antecedent[]{ant_6}, cons_6);
        rules[6]=new CIT2_Rule(new CIT2_Antecedent[]{ant_7}, cons_7);
        CIT2_Rulebase rulebase=new CIT2_Rulebase(rules);
        
        Random rand=new Random();
        
        IT2_Rulebase it2_rulebase=rulebase.toIT2();
        long startTime=System.currentTimeMillis();
        double km, ekm;
        for(int i=0;i<1;i++)
        {
            input_1.setInput(0.01+(2-0.01)*rand.nextDouble());
            input_2.setInput(1.01+(3-1.01)*rand.nextDouble());
            input_3.setInput(2.01+(4-2.01)*rand.nextDouble());
            input_4.setInput(3.01+(5-3.01)*rand.nextDouble());
            input_5.setInput(4.01+(6-4.01)*rand.nextDouble());
            input_6.setInput(5.01+(7-5.01)*rand.nextDouble());
            input_7.setInput(6.01+(8-6.01)*rand.nextDouble());
//            rulebase.sampleCentroid(50);
            
//            System.out.println(rulebase.explainableDefuzzification(100).printFirings());
            //rulebase.plotSystem();
            JFreeChartPlotter.plotMFs("Test", new T1MF_Interface[]{rulebase.explainableDefuzzification(1000).getMax_es(), rulebase.explainableDefuzzification(1000).getMin_es()}, input_1.getDomain(), 1000);
//            generator_system.getCentroid();
//            it2_rulebase.getCentroid();
        }
        long endTime=System.currentTimeMillis();
        System.out.println(endTime-startTime);
    }
    
}
