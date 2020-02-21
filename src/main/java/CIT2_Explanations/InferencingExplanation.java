package CIT2_Explanations;

import generic.Tuple;

/**
 * Contains the name of the MF, the Tuple used to do the inference and the name of the variable it refers to
 * @author Pasquale
 */
class InferencingExplanation {
    
    private final String MFName, variableName;
    private final Tuple inferencingValue;
    
    public InferencingExplanation(String mf_name, String variable_name, Tuple inferencing_value)
    {
        MFName=mf_name;
        variableName=variable_name;
        inferencingValue=inferencing_value;
    }

    public String getMFName() {
        return MFName;
    }

    public String getVariableName() {
        return variableName;
    }

    public Tuple getInferencingValue() {
        return inferencingValue;
    }
    
}
