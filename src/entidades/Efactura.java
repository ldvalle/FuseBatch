package entidades;

import java.util.Date;

public class Efactura {

    private String  codEmpresa;
    private Long    cuentaContrato;
    private String  email;
    private String  estado;
    private String  externalID;
    private String  rolModif;
    private Date    fechaModif;
    private String  tipoReparto;

    private String  email_1;
    private String  email_2;
    private String  email_3;

    //Setters and Getters
    public String getCodEmpresa(){ return codEmpresa; }
    public void setCodEmpresa( String codEmpresa ){ this.codEmpresa = codEmpresa; }

    public Long getCuentaContrato(){ return cuentaContrato; }
    public void setCuentaContrato(Long cuentaContrato ){ this.cuentaContrato = cuentaContrato; }

    public String getEmail(){ return email; }
    public void setEmail(String email ){ this.email = email; }

    public String getEstado(){ return estado; }
    public void setEstado( String estado ){ this.estado = estado; }

    public String getExternalID(){ return externalID; }
    public void setExternalID(String externalID ){ this.externalID = externalID; }

    public String getRolModif(){return rolModif; }
    public void setRolModif( String rolModif ){this.rolModif = rolModif; }

    public Date getFechaModif(){ return fechaModif; }
    public void setFechaModif( Date fechaModif ){this.fechaModif = fechaModif; }

    public String getTipoReparto(){ return tipoReparto; }
    public void setTipoReparto(String tipoReparto ){ this.tipoReparto = tipoReparto; }


    public String getEmail_1(){ return email_1; }
    public void setEmail_1(){
        String vMails[] = getEmail().split(";");
        if(vMails.length > 0)
            this.email_1 = vMails[0];
    }

    public String getEmail_2(){ return email_2; }
    public void setEmail_2(){
        String vMails[] = getEmail().split(";");
        if(vMails.length > 1)
            this.email_2 = vMails[1];
    }

    public String getEmail_3(){ return email_3; }
    public void setEmail_3(){
        String vMails[] = getEmail().split(";");
        if(vMails.length > 2)
            this.email_3 = vMails[2];
    }

    public boolean ValidaEmail(String eMail){
        // Que no sea vacio
        if(eMail.trim()==""){
            return false;
        }

        int i=0;
        int iCantArroba=0;
        int iCantPunto=0;

        for(i=0; i < eMail.length(); i++){
            char c = eMail.charAt(i);
            int iCode = (int) c;

            if(iCode >=1 && iCode <= 45)
                return false;

            if(iCode == 47)
                return false;

            if(iCode >=58 && iCode <= 63)
                return false;

            if(iCode >=91 && iCode <= 96 && iCode != 95)
                return false;

            if(iCode >=126 && iCode <= 255)
                return false;

            if(c=='@')
                iCantArroba++;

            if(c=='.')
                iCantPunto++;
        }

        int largo=email.length();

        //Que no termine en punto
        if(email.substring(largo,largo)==".")
            return false;
        //que tenga una arroba
        if(iCantArroba != 1)
            return false;
        // que tenga al menos un punto
        if(iCantPunto == 0)
            return false;
        // que not tenga '..'
        if(eMail.indexOf("..") !=-1)
            return false;
        // que not tenga '.@'
        if(eMail.indexOf(".@") !=-1)
            return false;
        // que not tenga '@.'
        if(eMail.indexOf("@.") !=-1)
            return false;
        // que not tenga '@.'
        if(eMail.indexOf("@.") !=-1)
            return false;
        // que not tenga '@_'
        if(eMail.indexOf("@_") !=-1)
            return false;
        // que not tenga '@-'
        if(eMail.indexOf("@-") !=-1)
            return false;

        return true;
    }

}
