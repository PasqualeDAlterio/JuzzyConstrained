/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CIT2;

import generic.Output;
import generic.Tuple;
import tools.TupleOperations;

/**
 * CIT2 consequent
 * @author Pasquale
 */
public class CIT2_Consequent {
    
    private final CIT2 mf;
    private final Output output;
    
    /**
     * Consequent constructor
     * @param mf the CIT2 set to use
     * @param output the output variable it refers to
     */
    
    public CIT2_Consequent(CIT2 mf, Output output)
    {
        this.mf=mf;
        this.output=output;
        this.mf.setSupport(TupleOperations.intersection(mf.getSupport(), output.getDomain()));
    }
    
    @Override
    public CIT2_Consequent clone()
    {
        Output new_output=new Output(output.getName(), output.getDomain());
        new_output.setDomain(output.getDomain());
        return new CIT2_Consequent(mf, new_output);
    }
         
    public CIT2 getCIT2()
    {
        return mf;
    }
    
    public Output getOutput()
    {
        return output;
    }
    
}
