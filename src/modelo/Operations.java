/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import connection.PgConnection;
import datastructures.Pattern;
import datastructures.TNode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Migue
 */
public class Operations {
    
    static private Operations op = null;
    TNode root ;
    HashMap ptree;
    private Operations(){
        root = new TNode(-1, 0, 0);
        ptree = new HashMap();
        try {
            generarArbol();
        } catch (SQLException ex) {
            Logger.getLogger(Operations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static public Operations getInstance(){
        if(op == null){
           
            op = new Operations();
        
        }
        
        return op;    
    }
    
    private ArrayList<Pattern> getPatterns() throws SQLException{
        ArrayList<Pattern> patterns = new ArrayList();
        
        /*
         * Get patterns (tracking instances from DB and store in arraylist!
         * Each element of the arraylist will have another arraylist with a 
         * tracking pattern in it in the form of Ex1 -> Ex2 -> Exn 
         * Where each Exi is an Pnode object containg 
         * 
         */
        
        ResultSet users = PgConnection.getInstance().getUsers();
        ArrayList ids = new ArrayList();
        
        while(users.next()){
            ids.add(users.getInt("id"));
        }
        
        for (int i = 0; i < ids.size(); i++) {
            int user_id = ((Integer) ids.get(i)).intValue();
            ResultSet trajectory = PgConnection.getInstance().getTrajectory(user_id);
            Pattern p = new Pattern();
            while(trajectory.next()){
                int poi_id =  trajectory.getInt("id");
                int[] t = new int[2];
                t[0] = 0;
                t[1] = 0;
                p.insertElement(poi_id, t);
            }
            
            patterns.add(p);
            
        }
        //System.out.println(patterns.size());
        //System.out.println(patterns);   
                
        
        return patterns;
    }
    
    private boolean containedPattern(Pattern outter, Pattern inner){
        int lastFoundIndex = 0;
        int matchCount = 0;
        for (int i = 0; i < outter.size(); i++) {
            int outterRegionId = outter.get(i);
            boolean sw = false;
            for (int j = lastFoundIndex; (j < inner.size() && !sw); j++) {
                int innerRegionId = inner.get(j);
                if(outterRegionId == innerRegionId){
                    matchCount += 1;
                    sw = true;
                    lastFoundIndex = j+1;
                }
            }
        }
        if(matchCount == outter.size()) return true;
        return false;
    }
    
    private void generarArbol() throws SQLException{
        
        
        ArrayList<Pattern> patterns = getPatterns();
        
        for (int i = 0; i < patterns.size(); i++) {
            Pattern outter = patterns.get(i);
            int matches = 0;
            for (int j = 0; j < patterns.size(); j++) {
                Pattern inner = patterns.get(j);
                if(i != j && containedPattern(outter, inner) == true){
                    matches+=1;
                }
            }
            outter.setSupp(((float)matches));
        }
        
        
       
        int id = 1;
        for (Pattern p : patterns) {
            TNode current = root;            
            for (int i = 0; i < p.size(); i++) {
                int exId = p.get(i);
                ArrayList<TNode> children = current.getChildren();
                boolean sw = false;
                int foundIndex = 0;
                for (int j = 0; (j < children.size() && !sw ); j++) {
                    TNode tn = children.get(j);
                    if(tn.getExId() == exId){
                        sw = true;
                        foundIndex = j;
                    }
                }
                if(sw == false){
                    current = current.setChildren(id, exId, 0, 0, p.getSupp());
                    ptree.put(id, current);
                    id += 1;
                }
                else{
                    if(current.getSupport() < p.getSupp()) current.setSupport(p.getSupp());
                    current = children.get(foundIndex);
                }
            }
                        
        }
        
        Collections.sort(root.getChildren(), new TnodeComparator());
        ptree.put(-1, root);
        ptree.put(-2, new TNode(-1, 0, 0));
        for(int k=1; k<id; k++){
            TNode n = (TNode) ptree.get(k);
            Collections.sort(n.getChildren(), new TnodeComparator());
            
            
        }
       
        
    }
    
    
    
    public String getList(int treeId){
        ArrayList sugested =  ((TNode)ptree.get(treeId)).getChildren();
        String r = "";
        String q = "";
        if(sugested.size() > 0) q = " where ";
        
        for (int i = 0; i < sugested.size(); i++) {
            TNode n = ((TNode) sugested.get(i));
            r += PgConnection.getInstance().getExhibicionName(n.getExId())+"/"+n.getId()+"/"+n.getExId()+"@";
        }
        
        for (int i = 0; i < sugested.size(); i++) {
            TNode n = ((TNode) sugested.get(i));
            q += " E.id != "+n.getExId();
            if(i != sugested.size()-1){
                q += " and ";
            }
        }
        
        r+= PgConnection.getInstance().getExhibicionRestante(q);
        
        return r; //((TNode)ptree.get(treeId)).getChildren();
    }
    
    public void guardar(String[] datos){
        String str = "";
        for (int i = 1; i < datos.length; i++) {
            str = str+datos[i]+"/";
        }
        
        PgConnection.getInstance().addTracking(str);
        
    }
    
    
    private class TnodeComparator implements Comparator{
   
        @Override
        public int compare(Object Tn1, Object Tn2){

          

                double supp1 = ((TNode)Tn1).getSupport();        
                double supp2 = ((TNode)Tn2).getSupport();  

                if(supp1 > supp2)
                    return -1;
                else if(supp1 < supp2)
                    return 1;
                else
                    return 0;    
            }

        }
    
}
