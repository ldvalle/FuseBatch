package entidades;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VarGlobales {
    public String   pathIN;
    public String   pathOUT;
    public String   pathLOG;
    public String   pathMALOS;
    public String   env_SO;

    public VarGlobales (File archivo, String sOS){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document document = documentBuilder.parse(archivo);

            document.getDocumentElement().normalize();
            NodeList listaPaths = document.getElementsByTagName("PathFiles");

            for (int temp = 0; temp < listaPaths.getLength(); temp++) {
                Node nodo = listaPaths.item(temp);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodo;
                    pathIN=element.getElementsByTagName("origen_files").item(0).getTextContent();
                    pathOUT=element.getElementsByTagName("destino_files").item(0).getTextContent();
                    pathLOG=element.getElementsByTagName("log_files").item(0).getTextContent();
                    pathMALOS=element.getElementsByTagName("malos_files").item(0).getTextContent();
                }
            }
            env_SO = sOS;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
