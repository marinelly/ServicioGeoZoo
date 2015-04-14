/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serviciogeozoo;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import modelo.Operations;

/**
 *
 * @author enino
 */
public class ServicioTCP extends Thread{

    private ArrayList clientes;

    //1. Definimos el objeto ServerSocket
    private ServerSocket SS;

    public ServicioTCP(int puerto) throws Exception
    {
        Operations.getInstance();
        //2. Alojamos el servicio en el puerto pasado por parametro
        SS = new ServerSocket(puerto);
        clientes = new ArrayList();
        //Permite iniciar las actividades definidas dentro de run
        this.start();
    }

    @Override
    public void run()
    {
            System.out.println("SERVIDOR ARRIBA ESPERANDO CLIENTES");
            while(true)
            {
                  try{
                        //3. Aceptamos los clientes
                        Socket cliente = SS.accept();
                        clientes.add(new ClienteTCP(cliente));
                        System.out.println("cliente aceptado");
                        
                  }catch(Exception e){
                      
                            e.printStackTrace();
                  }
            }
        
    }

   
}





