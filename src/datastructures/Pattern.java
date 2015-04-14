/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructures;

import java.util.ArrayList;

/**
 *
 * @author Familia Rodriguez
 */
public class Pattern {

   
    private ArrayList   S;
    private ArrayList   A;
    private float       supp;
    
    
    public Pattern(){
        S = new ArrayList();
        A = new ArrayList();
    }
    
    public void insertElement(int regionid, int[] tempAnnotation){
        S.add(regionid);
        A.add(tempAnnotation);
    }
    
    public float getSupp() {
        return supp;
    }

    public void setSupp(float supp) {
        this.supp = supp;
    }
    
    public int size(){
        return S.size();
    }
    
    public int get(int i){
        return ((Integer)S.get(i)).intValue();
    }
    
    @Override
    public String toString(){
        return S.toString()+": " + supp +"\n";
    }
    
    
    
}
