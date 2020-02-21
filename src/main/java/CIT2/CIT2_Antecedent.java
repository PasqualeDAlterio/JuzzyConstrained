package CIT2;

import generic.Input;
import generic.Tuple;

/**
 * CIT2 antecedent
 * @author Pasquale
 */

public class CIT2_Antecedent {
    private final CIT2 set;
    private final Input input;
    
    /**
     * Constructor for the antecedent
     * @param set The CIT2 to use as an antecedent
     * @param input The input variable it refers to
     */
    public CIT2_Antecedent(CIT2 set, Input input)
    {
        this.set=set;
        set.setSupport(new Tuple(Math.max(set.getSupport().getLeft(), input.getDomain().getLeft()), Math.min(set.getSupport().getRight(), input.getDomain().getRight())));
        this.input=input;
    }
    
    public CIT2 getCIT2()
    {
        return set;
    }
    
    public Input getInput()
    {
        return input;
    }

    @Override
    public CIT2_Antecedent clone()
    {
        Input new_input=new Input();
        new_input.setDomain(input.getDomain());
        new_input.setInput(input.getInput());
        new_input.setName(input.getName());
        return new CIT2_Antecedent(set, new_input);
    }
    
}
