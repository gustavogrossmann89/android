package br.edu.utfpr.ceiot.appceiot;

/**
 * Created by leona on 05/03/2018.
 */

import android.database.Cursor;
import android.net.Uri;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by leona on 05/03/2018.
 */

public class RestUtil {

    final static String BASE_URL = "https://ceiot-android-app.firebaseio.com/";
    //final static String BASE_URL = "https://aulaceiotmobile.firebaseio.com/";
    final static String PARAM_ORDER_BY = "orderBy";
    final static String NOME = "nome";
    final static String EQUAL_TO = "equalTo";

    /**
     * Constroi a URL a ser utilizada.
     *
     * @param query valor utilziado para consulta
     * @return URL montada
     */
    public static URL buildUrl(String entity, String query) {
        Uri builtUri = Uri.parse(BASE_URL+ "/"  + entity +".json" ).buildUpon()
//                .appendQueryParameter(PARAM_ORDER_BY, NOME)
//                .appendQueryParameter(EQUAL_TO, query)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Retorna um JSON a partir de uma requisição HTTP
     * @param url
     * @return String JSON
     * @throws IOException
     */
    public static String doGet(URL url) throws IOException
    {
        StringBuilder sb = new StringBuilder("");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                case HttpURLConnection.HTTP_CREATED:
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    bufferedReader.close();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return sb.toString();
    }

    public static String doPost(URL url, String json) throws IOException
    {
        StringBuilder sb = new StringBuilder("");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            //urlConnection.getOutputStream().write(json.getBytes());

            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(json);
            writer.flush();
            writer.close();
            outputStream.close();

            int responseCode=urlConnection.getResponseCode();

            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                case HttpURLConnection.HTTP_CREATED:
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    bufferedReader.close();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return sb.toString();
    }

    //TODO (23) Envio para a base.
    public static String saveOnFirebase(URL url,String name, String description)
    {
        JSONObject jsonObject= new JSONObject();
        try {

            jsonObject.put("name", name);
            jsonObject.put("description", description);

            return doPost(url,jsonObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

}
