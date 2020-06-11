package ppal;

//import servicios.sfcContrato;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class FuseEfactura {

    static private String sOS;

    public static void main(String[] args) {
/*
        sfcContrato miSrv = new sfcContrato();
        SimpleDateFormat fechaF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date fechaInicio = new Date();

        if(!ValidaArgumentos(args)) {
            System.exit(1);
        }
        if(sOS.equals("DOS")) {
            Locale.setDefault(Locale.Category.FORMAT, java.util.Locale.US);
        }

        System.out.println("Procesando Contrato ...");

        if(!miSrv.ProcesaContrato(iEstadoCliente, iModoExtraccion, iTipoArchivos, sOS)) {
            System.out.println("Fallo el proceso");
            System.exit(1);
        }

        System.out.println("Termino OK");

        Date fechaFin = new Date();

        System.out.println("Inicio: " + fechaF.format(fechaInicio));
        System.out.println("Fin:    " + fechaF.format(fechaFin));
*/
    }

    static private Boolean ValidaArgumentos(String[] args) {

        if(args.length != 41) {
            System.out.println("Argumentos Invalidos");
            System.out.println("Plataforma: DOS, UNIX, LINUX");

            return false;
        }

        sOS=args[1];

        return true;
    }


}
