package ppal;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.io.*;

import entidades.VarGlobales;
import servicios.srvEfactura;

public class FuseStart {
    static private String sOS;
    static VarGlobales eGlobal;
    static final String sMaskEFactura="EFactura_";

    public static void main(String[] args) {

        Date fechaInicio = new Date();
        SimpleDateFormat fechaF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        File filePath = new File("pathFiles.xml");

        if(!ValidaArgumentos(args)) {
            System.exit(1);
        }
        if(sOS.equals("DOS")) {
            Locale.setDefault(Locale.Category.FORMAT, java.util.Locale.US);
        }

        System.out.println("Cargando var globales");

        eGlobal=new VarGlobales(filePath, sOS);

        System.out.println("Revisando Directorio de trabajo ...");

        File dirWork = new File(eGlobal.pathIN);
        //String[] lstArchivos = dirWork.list();
        File lstArchivos[] = dirWork.listFiles();

        ////---- A procesar
        srvEfactura srvEF = new srvEfactura();

        for(int i=0; i < lstArchivos.length; i++){
            String sArchivo = lstArchivos[i].getName();

            if(sArchivo.indexOf(sMaskEFactura)!= -1){
                //Remitirlo a EFactura
                File fWork = new File(lstArchivos[i].getAbsolutePath());
                File fRepoOk =new File(eGlobal.pathOUT + lstArchivos[i].getName());
                File fRepoMal =new File(eGlobal.pathMALOS + lstArchivos[i].getName());

                if(srvEF.ProcesaEfactura(fWork, eGlobal)){
                    fWork.renameTo(fRepoOk);
                }else{
                    fWork.renameTo(fRepoMal);
                }
            }

        }

        System.out.println("Termino OK");

        Date fechaFin = new Date();

        System.out.println("Inicio: " + fechaF.format(fechaInicio));
        System.out.println("Fin:    " + fechaF.format(fechaFin));


    }

    static private Boolean ValidaArgumentos(String[] args) {

        if(args.length != 1) {
            System.out.println("Argumentos Invalidos");
            System.out.println("Plataforma: DOS, UNIX, LINUX");

            return false;
        }

        sOS=args[0];

        return true;
    }

}
