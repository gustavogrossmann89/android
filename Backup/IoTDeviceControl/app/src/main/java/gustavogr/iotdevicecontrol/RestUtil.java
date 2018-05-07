package gustavogr.iotdevicecontrol;

import android.net.Uri;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RestUtil {

    final static String BASE_URL = "https://iotsensorcontrol.firebaseio.com/";
    final static String PARAM_ORDER_BY = "orderBy";
    final static String NOME = "nome";
    final static String EQUAL_TO = "equalTo";

    public static URL buildUrl(String entity, String query) {
        Uri builtUri = Uri.parse(BASE_URL+ "/"  + entity +".json" ).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

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

    public static List<Node> parseJSONArray(String json) {
        List<Node> listNode = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.names();
            for (int i = 0; i <jsonArray.length(); i++) {
                JSONObject obj = jsonObject.getJSONObject(jsonArray.getString(i));
                String nome = obj.getString("nome");
                String descricao = obj.getString("descricao");
                String topicomqtt = obj.getString("topicomqtt");
                String status = obj.getString("status");
                Node node = new Node(nome,descricao,topicomqtt, status);
                listNode.add(node);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return listNode;
    }

    public static String saveOnFirebase(URL url, Node node)
    {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("nome", node.getNome());
            jsonObject.put("descricao", node.getDescricao());
            jsonObject.put("topicomqtt", node.getTopicomqtt());
            jsonObject.put("status", node.getStatus());

            return doPost(url,jsonObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
