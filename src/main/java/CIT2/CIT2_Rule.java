package CIT2;


import additional_IT2MF.IntervalT2MF_Generic;
import additional_T1MF.T1MF_InferenceResult;
import tools.InferenceType;
import tools.TNorm;
import generic.Tuple;
import intervalType2.sets.IntervalT2MF_Interface;
import intervalType2.system.*;
import java.util.ArrayList;
import type1.sets.*;
import type1.system.*;

/**
 * Rule using CIT2 sets as antecedent and consequents
 * @author Pasquale
 */
public class CIT2_Rule {
    private final InferenceType AND_OPERATOR=InferenceType.MIN;
    private final CIT2_Antecedent[] antecedents;
    private final CIT2_Consequent[] consequents;

    /**
     * Constructor with multiple consequents (currently not available because some operations only work single-consequent rules)
     * @param antecedents the antecedents of the rule
     * @param consequents the consequents of the rule
     */
    private CIT2_Rule(CIT2_Antecedent[] antecedents, CIT2_Consequent[] consequents)
    {
        this.antecedents=antecedents;
        this.consequents=consequents;
    }
    
    /**
     * Constrictor with a single consequent
     * @param antecedents the antecedents of the rule
     * @param consequent the consequent of the rule
     */
    public CIT2_Rule(CIT2_Antecedent[] antecedents, CIT2_Consequent consequent)
    {
        this(antecedents, new CIT2_Consequent[]{consequent});
    }
    
        public CIT2_Antecedent[] getAntecedents()
    {
        return antecedents;
    }
    
    public CIT2_Consequent[] getConsequents()
    {
        return consequents;
    }
    
    
    /**
     * Computes the T1 rule obtained by replacing each CIT2 set with its generator set
     * @return the rule obtained by replacing each CIT2 set with its generator set
     */
    public T1_Rule getGeneratorRule()
    {
        T1_Antecedent[] rule_antecedents=new T1_Antecedent[this.antecedents.length];
        T1_Consequent[] rule_consequents=new T1_Consequent[this.consequents.length];
        int index=0;
        //Replacing each CIT2 set in the antecedents with its generator set
        for(CIT2_Antecedent current_antecedent : this.antecedents)
        {
            rule_antecedents[index]=new T1_Antecedent(current_antecedent.getCIT2().getGeneratorSet(), current_antecedent.getInput());
            index++;
        }
        index=0;
        //Replacing each CIT2 set in the consequents with its generator set
        for(CIT2_Consequent current_consequent : this.consequents)
        {
            rule_consequents[index]=new T1_Consequent(current_consequent.getCIT2().getGeneratorSet(), current_consequent.getOutput());
            index++;
        }
        return new T1_Rule(rule_antecedents, rule_consequents);
    }
    
    /**
     * Converts the current rule to an equivalent IT2 (i.e. the IT2 sets in the rule are the equivalent of the CIT2 sets in the CIT2 rule)
     * @return the equivalent IT2 rule
     */
    public IT2_Rule toIT2()
    {
        IT2_Antecedent[] it2_antecedents=new IT2_Antecedent[antecedents.length];
        IT2_Consequent[] it2_consequents=new IT2_Consequent[consequents.length];
        //Convert each antecedent to an equivalent IT2 one
        for(int index=0;index<antecedents.length;index++)
        {
            it2_antecedents[index]=new IT2_Antecedent(antecedents[index].getCIT2().toIT2(), antecedents[index].getInput());
        }
        //Convert each consequent to an equivalent one
        for(int index=0;index<consequents.length;index++)
        {
            it2_consequents[index]=new IT2_Consequent(consequents[index].getCIT2().toIT2(), consequents[index].getOutput());
        }
        return new IT2_Rule(it2_antecedents, it2_consequents);
    }
    
    /**
     * Finds the firing interval of the rule
     * @param t_norm the T-norm used to implement
     * @return the Tuple containing the firing interval
     */
    public Tuple getFiringStrength(TNorm<Tuple> t_norm)
    {
        Tuple current_fuzzified_value;
        ArrayList<Tuple> fuzzified_inputs=new ArrayList<>(antecedents.length);
        for(CIT2_Antecedent current_antecedent: antecedents)
        {
            current_fuzzified_value=new Tuple();
            current_fuzzified_value.setRight(current_antecedent.getCIT2().getUMF().getFS(current_antecedent.getInput().getInput()));
            current_fuzzified_value.setLeft(current_antecedent.getCIT2().getLMF().getFS(current_antecedent.getInput().getInput()));
            fuzzified_inputs.add(current_fuzzified_value);
        }
        return t_norm.doTNorm(fuzzified_inputs);
    }
    

    /**
     * Makes a printable version of the current rule
     * @return A String representing the printable version of the rule
     */
    @Override
    public String toString()
    {
        StringBuilder result=new StringBuilder("IF ");
        int i=0;
        for(CIT2_Antecedent current_antecedent : antecedents)
        {
            result.append(current_antecedent.getInput().getName());
            result.append(" IS ");
            result.append(current_antecedent.getCIT2().getName());
            if(i+1<antecedents.length)
                result.append(" AND ");
            i++;
        }
        i=0;
        result.append(" THEN ");
        for(CIT2_Consequent current_consequent : consequents)
        {
            result.append(current_consequent.getOutput().getName());
            result.append(" IS ");
            result.append(current_consequent.getCIT2().getName());
            if(i+1<consequents.length)
                result.append(" AND ");
            i++;
        }
        return result.toString();
    }
    
     /**
     * Computes the fired FOU (i.e. the FOU of the fuzzy output) of the current rule
     * @return The IT2 FS representing the fired FOU
     */
    public IntervalT2MF_Interface getFiredFOU()
    {
        Tuple firing_interval;
        //Get the consequent MF
        CIT2 current_consequent=consequents[0].getCIT2();
        T1MF_Interface fired_upperbound, fired_lowerbound;
        //Compute the firing interval of the rule
        firing_interval=computeFiringInterval();
        //Apply the inference on the boundary MFs using the minimum operator
        fired_lowerbound=new T1MF_InferenceResult("Fired "+current_consequent.getName(), current_consequent.getLMF(), firing_interval.getLeft());
        fired_upperbound=new T1MF_InferenceResult("Fired "+current_consequent.getName(), current_consequent.getUMF(), firing_interval.getRight());
        return new IntervalT2MF_Generic("Fired FOU", fired_upperbound, fired_lowerbound);
    }
    
    /**
     * Computes the firing interval of the current rule
     * @return The firing interval as a Tuple
     */
    private Tuple computeFiringInterval()
    {
        Tuple result;
        //1,1 va bene sia per il prodotto (elemento neutro) che per il minimo (il valore di cut non può essere più alto di 1)
        result=new Tuple(1,1);
        double current_upperbound_fs, current_lowerbound_fs;
        for(CIT2_Antecedent current_antecedent: antecedents)
        {
            current_upperbound_fs=current_antecedent.getCIT2().getUMF().getFS(current_antecedent.getInput().getInput());
            current_lowerbound_fs=current_antecedent.getCIT2().getLMF().getFS(current_antecedent.getInput().getInput());
            if(AND_OPERATOR==InferenceType.MIN)
            {
                result.setRight(Math.min(result.getRight(), current_upperbound_fs));
                //Approssimom il lowerbound a 0 con un cut molto vicino a 0 dal momento che se l'intervallo di cut è [0,x], esiste un set troncato molto vicino a 0
                result.setLeft(Math.min(result.getLeft(), current_lowerbound_fs));
            }
            else
            {
                result.setLeft(result.getLeft()*current_lowerbound_fs);
                result.setRight(result.getRight()*current_upperbound_fs);
            }
        }
        return result;
    }
    
    
    
    //---------------------------------------------------Rule expansion section ------------------------------------------------------------
    

}
