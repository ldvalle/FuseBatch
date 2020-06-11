package dao;

import connectBD.UConnection;
import entidades.Efactura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Vector;
import java.util.Date;

public class daoEFactura {

    public Integer getExisteCliente(long lNroCliente){
        Integer iCant=0;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            con = UConnection.getConnection();
            st = con.prepareStatement(SEL_EXISTE_CLIENTE);
            st.setLong(1, lNroCliente);
            rs=st.executeQuery();
            if(rs.next()) {
                iCant=rs.getInt(1);
            }
            rs.close();
            st.close();

        }catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return iCant;
    }

    public Integer getExisteAdhesion(long lNroCliente){
        Integer iCant=0;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            con = UConnection.getConnection();
            st = con.prepareStatement(SEL_CLIENTE_ADHERIDO);
            st.setLong(1, lNroCliente);
            rs=st.executeQuery();
            if(rs.next()) {
                iCant=rs.getInt(1);
            }
            rs.close();
            st.close();

        }catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return iCant;
    }

    public boolean procesaAlta(Efactura reg){
        Connection con = null;
        PreparedStatement st = null;

        String sDatoNuevo = reg.getEmail();
        String sDatoViejo = "NO TENIA";

        try{
            con = UConnection.getConnection();
            con.setAutoCommit(false);

            // El Insert
            st = con.prepareStatement(INS_ADHESION);
            st.setLong(1, reg.getCuentaContrato());
            st.setString(2, reg.getEmail_1());
            st.setString(3, reg.getEmail_2());
            st.setString(4, reg.getEmail_3());
            st.setString(5, reg.getTipoReparto());

            st.executeUpdate();

            st=null;

            // El Modif
            st = con.prepareStatement(INS_MODIF);
            st.setLong(1, reg.getCuentaContrato());
            st.setString(2, sDatoViejo);
            st.setString(3, sDatoNuevo);

            con.commit();

        }catch(SQLException sqle){
            try {
                con.rollback();
                System.out.println("Rollback establecido");
            }catch(SQLException exSQL) {
                exSQL.printStackTrace();
            }
            sqle.printStackTrace();
            throw new RuntimeException(sqle);
        }finally{
            try{
                if(st != null) st.close();
            }catch(Exception ex){
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }

        return true;
    }

    public boolean procesaModificacion(Efactura reg) {
        Connection con = null;
        PreparedStatement st = null;
        String sDatoNuevo = reg.getEmail();
        String sDatoViejo = "MODIFICO VALORES";

        try{
            con = UConnection.getConnection();
            con.setAutoCommit(false);

            // El Update
            st = con.prepareStatement(UPD_ADHESION);
            st.setString(1, reg.getEmail_1());
            st.setString(2, reg.getEmail_2());
            st.setString(3, reg.getEmail_3());
            st.setString(4, reg.getTipoReparto());
            st.setLong(5, reg.getCuentaContrato());

            st.executeUpdate();

            st=null;

            // El Modif
            st = con.prepareStatement(INS_MODIF);
            st.setLong(1, reg.getCuentaContrato());
            st.setString(2, sDatoViejo);
            st.setString(3, sDatoNuevo);

            con.commit();

        }catch(SQLException sqle){
            try {
                con.rollback();
                System.out.println("Rollback establecido");
            }catch(SQLException exSQL) {
                exSQL.printStackTrace();
            }
            sqle.printStackTrace();
            throw new RuntimeException(sqle);
        }finally{
            try{
                if(st != null) st.close();
            }catch(Exception ex){
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }

        return true;
    }

    public boolean procesaBaja(Efactura reg) {
        Connection con = null;
        PreparedStatement st = null;
        String sDatoNuevo = "BAJA";
        String sDatoViejo = "";

        try{
            con = UConnection.getConnection();
            con.setAutoCommit(false);

            // El Update
            st = con.prepareStatement(BAJA_ADHESION);
            st.setLong(1, reg.getCuentaContrato());

            st.executeUpdate();

            st=null;

            // El Modif
            st = con.prepareStatement(INS_MODIF);
            st.setLong(1, reg.getCuentaContrato());
            st.setString(2, sDatoViejo);
            st.setString(3, sDatoNuevo);

            con.commit();

        }catch(SQLException sqle){
            try {
                con.rollback();
                System.out.println("Rollback establecido");
            }catch(SQLException exSQL) {
                exSQL.printStackTrace();
            }
            sqle.printStackTrace();
            throw new RuntimeException(sqle);
        }finally{
            try{
                if(st != null) st.close();
            }catch(Exception ex){
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }

        return true;
    }

    private static final String SEL_EXISTE_CLIENTE = "SELECT COUNT(*) cant FROM cliente WHERE numero_cliente = ? ";

    private static final String SEL_CLIENTE_ADHERIDO = "SELECT COUNT(*) FROM clientes_digital " +
            "WHERE numero_cliente = ? " +
            "AND fecha_alta <= TODAY " +
            "AND (fecha_baja IS NULL OR fecha_baja > TODAY) ";

    private static final String INS_ADHESION = "INSERT INTO clientes_digital( " +
            "numero_cliente, " +
            "email_1, " +
            "email_2, " +
            "email_3, " +
            "sin_papel, " +
            "rol_creador, " +
            "fecha_alta " +
            ")VALUES( ?, ?, ?, ?, ?, 'SALESFORCE', CURRENT) ";

    private static final String UPD_ADHESION = "UPDATE clientes_digital SET " +
            "email_1 = ?, " +
            "email_2 = ?, " +
            "email_3 = ?, " +
            "sin_papel = ?, " +
            "rol_modif = 'SALESFORCE', " +
            "fecha_modif = CURRENT " +
            "WHERE numero_cliente = ? " +
            "AND fecha_alta <= TODAY " +
            "AND (fecha_baja IS NULL OR fecha_baja > TODAY) ";

    private static final String INS_MODIF = "INSERT INTO modif (" +
            "numero_cliente," +
            "tipo_orden," +
            "ficha," +
            "fecha_modif," +
            "tipo_cliente," +
            "codigo_modif," +
            "dato_anterior," +
            "dato_nuevo," +
            "proced," +
            "dir_ip" +
            ")VALUES(" +
            "?, 'MOD', 'SALESFORCE', CURRENT, 'A', 269, ?, ?, 'BATCH') ";

    private static final String BAJA_ADHESION = "UPDATE clientes_digital SET " +
            "rol_baja = 'SALESFORCE', " +
            "fecha_baja = CURRENT " +
            "WHERE numero_cliente = ? " +
            "AND fecha_alta <= TODAY " +
            "AND (fecha_baja IS NULL OR fecha_baja > TODAY) ";

}
