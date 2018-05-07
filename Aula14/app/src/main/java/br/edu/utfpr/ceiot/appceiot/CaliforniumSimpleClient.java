package br.edu.utfpr.ceiot.appceiot;


import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.WebLink;

import java.util.Set;
import java.util.logging.Handler;


/**
 * TODO (4) Criada classe para manter as funções do COAP em um único lugar
 */
public class CaliforniumSimpleClient {

    final static String BASE_URL =
            "coap://californium.eclipse.org:5683/";
            //"coap://localhost:5683/";

    /**
     * Implementação da função get
     * @param path
     * @return
     */
    public String get(String path)
    {
        try {
            CoapClient client = new CoapClient(BASE_URL + path);
            CoapResponse response = client.get();
            if (response != null) {
                System.out.println(response.getCode());
                System.out.println(response.getOptions());
                System.out.println(response.getPayload());
                return response.getResponseText();
            } else {
                return "No response received.";
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Implementação do observe
     * @param path
     * @param handler
     * @return
     */
    public String observe(String path, CoapHandler handler)
    {
        try {
            CoapClient client = new CoapClient(BASE_URL + path);
            CoapObserveRelation relation = client.observe(handler);
            if (relation != null && relation.getCurrent() != null) {
                return relation.getCurrent().getResponseText();
            } else {
                return "No response received.";
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Implementação do discovery
     * @param path
     * @return
     */
    public static String discovery(String path){
       try {
           CoapClient client = new CoapClient(BASE_URL+path);
           Set<WebLink> set = client.discover();

           String itens = "";
           for (WebLink wl: set) {
               itens=itens+wl.getURI()+"\r\n";
           }
           return itens;
       }catch (Exception e)
       {
           e.printStackTrace();
       }
       return null;
    }
}
