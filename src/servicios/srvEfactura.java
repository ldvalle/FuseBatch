package servicios;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.io.*;

import entidades.VarGlobales;
import entidades.Efactura;
import dao.daoEFactura;

public class srvEfactura {

    private static Writer outLog =null;

    public boolean ProcesaEfactura(File fArchivo, VarGlobales vg){
        String sMsg="";
        SimpleDateFormat fechaF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        boolean iRcv=false;
        daoEFactura miDao = new daoEFactura();
        int iCant=0;

        if(! AbreArchivoLog(fArchivo, vg)){
            System.out.println("No se pudo abrir archivo de Log para archivo " + fArchivo.getName());
            return false;
        }

        int iFila=0;
        try{
            FileReader fr = new FileReader(fArchivo.getAbsolutePath());
            BufferedReader br =new BufferedReader(fr);
            String sLinea="";

            while((sLinea = br.readLine())!=null){
                if(iFila > 0){
                    String vLinea[] = sLinea.split("\\|");
                    if(vLinea.length == 8) {
                        Efactura reg = new Efactura();

                        reg.setCodEmpresa(vLinea[0].trim());
                        reg.setCuentaContrato(Long.parseLong(vLinea[1]));
                        reg.setEmail(vLinea[2].trim());
                        reg.setEstado(vLinea[3].trim());
                        reg.setExternalID(vLinea[4].trim());
                        reg.setRolModif(vLinea[5].trim());
                        reg.setFechaModif(fechaF.parse(vLinea[6].trim()));
                        reg.setTipoReparto(vLinea[6].trim());

                        reg.setEmail_1();
                        reg.setEmail_2();
                        reg.setEmail_3();

                        if(ValidaRegistro(sLinea, reg)){
                            if(reg.getEstado().trim().toUpperCase().equals("TRUE")){
                                //Alta-Modificacion
                                iCant=miDao.getExisteAdhesion(reg.getCuentaContrato());

                                if(iCant > 0){
                                    // Es un Update
                                    if(! miDao.procesaModificacion(reg)){
                                        sMsg= sLinea + "--ERR - Falló la Modificació\r\n";
                                        iRcv=GrabaLog(sMsg);
                                    }
                                }else{
                                    // Es un Insert
                                    if(! miDao.procesaAlta(reg)){
                                        sMsg= sLinea + "--ERR - Falló el Alta\r\n";
                                        iRcv=GrabaLog(sMsg);
                                    }
                                }
                            }else{
                                //Baja
                                iCant=miDao.getExisteAdhesion(reg.getCuentaContrato());
                                if(iCant<=0){
                                    sMsg= sLinea + "--ERR - Baja de cliente no adherido\r\n";
                                    iRcv=GrabaLog(sMsg);
                                }else {
                                    //Procesar la baja
                                    if(! miDao.procesaBaja(reg)){
                                        sMsg= sLinea + "--ERR - Falló la Baja\r\n";
                                        iRcv=GrabaLog(sMsg);
                                    }
                                }
                            }
                        }

                    }else{
                        sMsg= sLinea + "--ERR - Cantidad de Campos incorrecta\r\n";
                        iRcv=GrabaLog(sMsg);
                    }
                }

                iFila++;
            }
            fr.close();

        }catch (Exception ex){
            iRcv = CierraArchivoLog();
            System.out.println("Error leyendo archivo " + fArchivo.getName() + " fila " + iFila + " " + ex.getMessage());
            return false;
        }

        iRcv=CierraArchivoLog();

        return true;
    }

    private boolean AbreArchivoLog(File fIN, VarGlobales vg){
        String sNombreLog = fIN.getName() + ".log";
        String sPathFileLog=vg.pathLOG + sNombreLog;

        try{
            outLog = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sPathFileLog), "UTF-8"));
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean GrabaLog(String sLinea){
        try{
            outLog.write(sLinea);
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean CierraArchivoLog(){
        try{
            outLog.close();
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean ValidaRegistro(String sLinea, Efactura reg){
        String sMsg="";
        String sMail1="";
        String sMail2="";
        String sMail3="";
        boolean iRcv;
        daoEFactura miDao = new daoEFactura();

        if(reg.getEstado().trim().toUpperCase().equals("TRUE") && reg.getEmail().trim().equals("")){
            sMsg= sLinea + "--ERR - Es un alta y NO hay email.\r\n";
            iRcv=GrabaLog(sMsg);
            return false;
        }

        if(reg.getEmail_1()!= null)
            sMail1=reg.getEmail_1().trim();
        if(reg.getEmail_2()!= null)
            sMail2=reg.getEmail_2().trim();
        if(reg.getEmail_3()!= null)
            sMail3=reg.getEmail_3().trim();

        if(!sMail1.equals("") && sMail1!=null) {
            if (!reg.ValidaEmail(sMail1)) {
                sMsg = sLinea + "--ERR - Email 1 INVALIDO\r\n";
                iRcv = GrabaLog(sMsg);
                return false;
            }
        }

        if(!sMail2.equals("") && sMail2!=null) {
            if (!reg.ValidaEmail(sMail2)) {
                sMsg = sLinea + "--ERR - Email 2 INVALIDO\r\n";
                iRcv = GrabaLog(sMsg);
                return false;
            }
        }

        if(!sMail3.equals("") && sMail3!=null) {
            if (!reg.ValidaEmail(sMail3)) {
                sMsg = sLinea + "--ERR - Email 3 INVALIDO\r\n";
                iRcv = GrabaLog(sMsg);
                return false;
            }
        }

        //Validamos Cliente
        Integer iCant = miDao.getExisteCliente(reg.getCuentaContrato());
        if(iCant<=0){
            sMsg=sLinea + " -- ERR - Cliente NO existe\r\n";
            iRcv = GrabaLog(sMsg);
            return false;
        }


        return true;
    }
}
