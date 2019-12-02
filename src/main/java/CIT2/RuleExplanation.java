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
import tools.MinimumTNorm;
import tools.Pair;

/**
 * Class containing the information about a rule that will be used to create the explanation for the interval centroid
 * @author User
 */
public class RuleExplanation {
    
    //Enum that states whether the lower or the upper value of the firing interval has been used to do the inference on the consequent
    public enum FiringLevel{LOWER, UPPER};
    
    //The rule this structure refers to
    private final CIT2_Rule rule;
    //List with one element per antecedent in the rule. Each element is of the form <<MF name, Variable name>, Upper membership degree>
    private final LinkedList<Pair<Pair<String, String>, Double>> upperboundDegrees;
    //Same as above but w.r.t. the lowerbound
    private final LinkedList<Pair<Pair<String, String>, Double>> lowerboundDegrees;
    private final LinkedList<Pair<Pair<String, String>, Double>> truncationDegrees;
    //List with one element per
    private final LinkedList<Pair<Pair<String, String>, Tuple>> inputMembershipDegrees;
    //The output used in the consequent
    private final Output output;
    //Boolean that tells whether the upper and lower value of the firing interval was used in the inference
    private final boolean isUsingUpperFiring;
    //The value used for the inference
    private final double ruleFiringUsed;
    //The firing interval of the rule and it's ready-to-be-printed version (i.e. showing only the first 2 decimals)
    private final Tuple ruleFiring, formattedRuleFiring;
    //Enum static if the upper or lower value was used in the inference
    public final FiringLevel firingLevelUsed;
    
    /**
     * Constructor of the object
     * @param rule The rule it refers to
     * @param is_using_upper_firing Boolean stating whether the lower or upper value of the firing interval was used in the inference process
     */
    public RuleExplanation(CIT2_Rule rule, boolean is_using_upper_firing)
    {
        CIT2 current_MF;
        Tuple current_membership_degree;
        upperboundDegrees=new LinkedList<>();
        lowerboundDegrees=new LinkedList<>();
        inputMembershipDegrees=new LinkedList<>();
        isUsingUpperFiring=is_using_upper_firing;
        this.rule=rule;
        //For each antecedent...
        for(CIT2_Antecedent current_antecedent : rule.getAntecedents())
        {
            current_MF=current_antecedent.getCIT2();
            current_membership_degree=current_MF.getFS(current_antecedent.getInput().getInput());
            //Store the name of the antecedent name, the name of the input variable it refers to and the membership degree of the input
            //This stores more information that it is actually needed
            inputMembershipDegrees.add(new Pair(new Pair(current_MF.getName(), current_antecedent.getInput().getName()), current_membership_degree));
            upperboundDegrees.add(new Pair(new Pair(current_MF.getName(), current_antecedent.getInput().getName()), current_membership_degree.getRight()));
            lowerboundDegrees.add(new Pair(new Pair(current_MF.getName(), current_antecedent.getInput().getName()), current_membership_degree.getLeft()));
        }
        //Store the Output value the rule refers to
        output=rule.getConsequents()[0].getOutput();
        //Store the firing of the rule
        ruleFiring=rule.getFiringStrength(MinimumTNorm.factoryMethod());
        //Depending on the firing valueused, inizialised the attributes of the objet
        if(isUsingUpperFiring){
            truncationDegrees=upperboundDegrees;
            ruleFiringUsed=ruleFiring.getRight();
            firingLevelUsed=FiringLevel.UPPER;
        }
        else{
            truncationDegrees=lowerboundDegrees;
            ruleFiringUsed=ruleFiring.getLeft();
            firingLevelUsed=FiringLevel.LOWER;
        }
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        formattedRuleFiring=new Tuple(Double.valueOf(df.format(ruleFiring.getLeft())), Double.valueOf(df.format(ruleFiring.getRight())));
    }
    
        
    /**
     * Formats the information in the object in a printable format
     * @return The formatted String containing all the information needed for the explanation
     */
    @Override
    public String toString()
    {
        StringBuilder result=new StringBuilder();
        boolean first=true;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        for(Pair<Pair<String, String>, Tuple> current_element : inputMembershipDegrees)
        {
            if(!first)
                result.append(" AND ");
            else
            {
                first=false;
            }
            //Each explanation contains the membership degrees of the antecedents in the rule
            result.append(current_element.getLeft().getRight()+" IS "+current_element.getLeft().getLeft()+" ["+df.format(current_element.getRight().getLeft())+", "+df.format(current_element.getRight().getRight())+"]");
        }
        result.append("\n");
        return result.toString();
    }

    public CIT2_Rule getRule() {
        return rule;
    }

    public LinkedList<Pair<Pair<String, String>, Double>> getTruncationDegrees()
    {
        return truncationDegrees;
    }

    public Output getOutput() {
        return output;
    }
    
    public Tuple getRuleFiring()
    {
        return formattedRuleFiring;
    }
    
    public double getRuleFiringUsed()
    {
        return ruleFiringUsed;
    }
    
    public String printFiringLevelUsed()
    {
        return firingLevelUsed.toString();
    }
}