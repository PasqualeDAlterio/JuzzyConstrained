/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

/**
 *
 * @author User
 */
public class Pair <T,K>{
    
    private final T left;
    private final K right;
    
    public Pair(T left, K right)
    {
        this.left=left;
        this.right=right;
    }

    public T getLeft() {
        return left;
    }

    public K getRight() {
        return right;
    }
    
}
