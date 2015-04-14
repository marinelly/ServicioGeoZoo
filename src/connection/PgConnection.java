/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;




import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.*;
/**
 *
 * @author Migue
 */
public class PgConnection implements Serializable
{
    static private PgConnection c = null;
    private PgConnection()
    {
        try
        {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost/geo_zoo_20", "postgres", "123456");
            stm = con.createStatement();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static PgConnection getInstance(){
        if(c == null){
            c = new PgConnection();
        }
        return c;
    }

    public ResultSet getUsers()
    {
        ResultSet resultado = null;
        try
        {
            resultado = stm.executeQuery("Select id from usuario");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return resultado;
    }

    public ResultSet getTrajectory(int user_id)
    {
        ResultSet resultado = null;
        try
        {
            resultado = stm.executeQuery(" SELECT " 
+                                        "   seguimientos.secuencia, "
+                                        "   seguimientos.id_usuario, exhibiciones.nombre, exhibiciones.id, seguimientos.fecha_hora"
+                                        " FROM "
+                                        "   public.seguimientos, public.exhibiciones"
+                                        " WHERE"      
+                                        "   seguimientos.id_exhibicion = exhibiciones.id and seguimientos.id_usuario = "+ user_id+" and  seguimientos.id_comportamiento = 8"
+                                        " ORDER BY "
+                                        "   seguimientos.secuencia;"                       
                                              
                                        );
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public String getExhibicionName(int exId)
    {
        ResultSet resultado = null;
        String s = "";
        try
        {
            resultado = stm.executeQuery(" SELECT " 
+                                        "   E.nombre"
                    + "                    FROM"
                    + "                      exhibiciones E"
                    + "                    WHERE"
                    + "                      E.id = "+exId+";"                       
                                              
                                        );
            
            if(resultado.next()){
                s = resultado.getString("nombre");
            }
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return s;
    }
    
    public String getExhibicionRestante(String where )
    {
        ResultSet resultado = null;
        String s = "";
        try
        {
            resultado = stm.executeQuery(" SELECT " 
+                                        "   E.id, E.nombre "
                    + "                    FROM"
                    + "                      exhibiciones E "
                    
                    +  where                      
                                              
                                        );
            
            while(resultado.next()){
                s+=resultado.getString("nombre")+"/-2/"+resultado.getInt("id")+"@";
            }
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return s;
    }
    
    public String addTracking(String track )
    {
        String[] data = track.split("/");
        ResultSet resultado = null;
        ResultSet resultado2 = null;
        String s = "";
        try
        {
            resultado = stm.executeQuery("SELECT   max(seguimientos.id_usuario) FROM   public.seguimientos; "                     
                                              
                                        );
            int id_usuario = 0;
            if(resultado.next()){
                id_usuario = resultado.getInt("max");
            }
            id_usuario += 1;
            for (int i = 0; i < data.length; i++) {
                String exId = data[i];
                stm.executeUpdate("Insert into seguimientos (id_usuario, id_exhibicion, secuencia) values ("+id_usuario+", "+exId+", "+i+")"); 
                
            }
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return s;
    }
    
    
    public void cerrar()
    {
        try
        {
            stm.close();
            con.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public Connection con;
    public Statement stm;
    
}



