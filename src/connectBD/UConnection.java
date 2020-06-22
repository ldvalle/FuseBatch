package connectBD;

import java.sql.DriverManager;
import java.sql.Connection;
import java.util.ResourceBundle;

public class UConnection {

    private static Connection con = null;

    public static Connection getConnection(){
        String miKey="q1w2e3r4t5y6";
        ResourceBundle rb =null;
        String driver="";
        String url= "";
        String usr="";
        String pwd="";

        try{
            if(con == null){
                Runtime.getRuntime().addShutdownHook(new MiShDwnHook());

                rb = ResourceBundle.getBundle("jdbc");
                driver = rb.getString("driver");

                usr = Kripton.Desencriptar2(rb.getString("usr"), miKey);
                pwd = Kripton.Desencriptar2(rb.getString("pwd"), miKey);
                url = rb.getString("url");

                //usr = rb.getString("usr");
                //pwd = rb.getString("pwd");


                Class.forName(driver);
                //con = DriverManager.getConnection(url, usr, pwd);
                //con = DriverManager.getConnection("jdbc:mysql://localhost:3306/contacto", "root", "pepe");
                //con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/synergia", "batchsyn", "pepe");
                con = DriverManager.getConnection(url, usr, pwd);

            }
            return con;

        }catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("Error al crear conexion", ex);
        }
    }

    static class MiShDwnHook extends Thread{
        public void run(){
            try{
                Connection con = UConnection.getConnection();
                con.close();
            }catch(Exception ex){
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

}
