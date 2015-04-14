/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructures;

import java.util.ArrayList;

/**
 *
 * @author Migue
 */
public class TNode {
    int id;
    int exId;
    ArrayList hijos;
    ArrayList aristas;
    double support;

    public TNode(int exhibicionId, int id,  double support) {
        this.exId = exhibicionId;
        hijos = new ArrayList<TNode> ();
        aristas = new ArrayList<float[]>();  
        this.id = id;
        this.support = support;
    }
    
    public TNode setChildren(int id, int exId, float min, float max, double support){
        TNode n = new TNode(exId, id, support);
        hijos.add(n);
        float[] m = new float [2];
        m[0] = min;
        m[1] = max;
        aristas.add(m);
        return n;
    }
    
    public ArrayList getChildren(){
        return hijos;
    }
    
    public ArrayList getEdges(){
        return aristas;
    }
    
    public int getChildrenSize(){
        return hijos.size();
    }

    public int getExId() {
        return exId;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }

    public int getId() {
        return id;
    }
    
    public String toString(){
        return exId+": "+support;
    }
}
