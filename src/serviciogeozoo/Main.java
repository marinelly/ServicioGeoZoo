/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serviciogeozoo;

import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Operations;

/**
 *
 * @author Migue
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new ServicioTCP(9090);
            //Operaciones.getInstance().getPatterns();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
