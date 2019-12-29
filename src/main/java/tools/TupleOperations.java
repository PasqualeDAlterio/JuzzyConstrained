package tools;

import generic.Tuple;

public class TupleOperations {

    private TupleOperations()
    {

    }

    public static Tuple intersection(Tuple a, Tuple b)
    {
        if(Double.isNaN(a.getRight()))
            return b;
        if(Double.isNaN(b.getRight()))
            return a;
        return new Tuple(Math.max(a.getLeft(), b.getLeft()), Math.min(a.getRight(), b.getRight()));
    }

    public static Tuple union(Tuple a, Tuple b)
    {
        if(Double.isNaN(a.getRight()))
            return b;
        if(Double.isNaN(b.getRight()))
            return a;
        return new Tuple(Math.min(a.getLeft(), b.getLeft()), Math.max(a.getRight(), b.getRight()));
    }
}
