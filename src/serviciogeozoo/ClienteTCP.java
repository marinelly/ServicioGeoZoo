/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serviciogeozoo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Operations;

/**
 *
 * @author enino
 */
public class ClienteTCP extends Thread{

    private String                  nickName;
    private Socket                  S;
    private DataInputStream         entrada;
    private DataOutputStream        salida;

    public ClienteTCP(Socket s) throws Exception
    {
        //4. Recibo el cliente para que sea atendido.
        S = s;        
        entrada = new DataInputStream(S.getInputStream());
        salida = new DataOutputStream(S.getOutputStream());
        this.start();
    }


    
    

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                String msj = "";
                int b = -1;
                //5. Concateno la información del cliente
                while((b = entrada.read())!='\n')
                {
                    msj = msj+(char)b;
                }
                
                System.out.println(msj);
                String[] info = msj.split("/");
                if(info[0].equals("list")){
                    String s = Operations.getInstance().getList(Integer.parseInt(info[1]));
                    enviarMensaje(s);
                }else{
                    if(info[0].equals("visit")){
                        Operations.getInstance().guardar(info);    
                    }
                }
                
                
                
               
                

                
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void enviarMensaje(String rpta){
        System.out.println(rpta);
        try {
            salida.writeBytes(rpta);
            salida.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
