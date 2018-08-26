package gustavogr.iotsmartlock.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesColumn;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Position;
import com.anychart.anychart.ValueDataEntry;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gustavogr.iotsmartlock.Model.ActionLog;
import gustavogr.iotsmartlock.R;
import gustavogr.iotsmartlock.Util.RestUtil;

/**
 * autor: Gustavo Grossmann
 * data: Ago/2018
 * descrição: Atividade que gera os gráficos referentes à uma instalação
 */
public class ChartActivity extends AppCompatActivity{

    String nodeid;
    String nome;
    Integer chartId = 0;

    String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

    int manhaHoje = 0;
    int tardeHoje = 0;
    int noiteHoje = 0;

    int[] diasDoMes = new int[32];
    int[] mesesDoAno = new int[13];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();
        if (intent.hasExtra("nodeid")) {
            nodeid = intent.getStringExtra("nodeid");
        }
        if (intent.hasExtra("nome")) {
            nome = intent.getStringExtra("nome");
        }

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setTitle(nome);
        }

        searchLogsByNodeId(nodeid);
    }

    public void displayChart(Integer chartId){

       if (chartId == R.id.action_chart1 || chartId == R.id.action_chart2 || chartId == R.id.action_chart3 ) {
            setContentView(R.layout.activity_chart);
            AnyChartView anyChartView = findViewById(R.id.any_chart_view);
            anyChartView.setBackgroundColor("#7EB7FA");

            Cartesian cartesian = AnyChart.column();
            cartesian.setAnimation(true);
            List<DataEntry> data = new ArrayList<>();

            CartesianSeriesColumn column = null;

            if (chartId == R.id.action_chart1) {
                data.add(new ValueDataEntry("Manhã", manhaHoje));
                data.add(new ValueDataEntry("Tarde", tardeHoje));
                data.add(new ValueDataEntry("Noite", noiteHoje));

                column = cartesian.column(data);

                cartesian.setTitle("Qtde de vezes aberta (hoje) - " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                cartesian.getXAxis().setTitle("Período do dia");
                cartesian.getYAxis().setTitle("Quantidade");
                cartesian.getYScale().getTicks().setInterval(1);

            } else if (chartId == R.id.action_chart2){
                data.add(new ValueDataEntry("1", diasDoMes[1]));
                data.add(new ValueDataEntry("2", diasDoMes[2]));
                data.add(new ValueDataEntry("3", diasDoMes[3]));
                data.add(new ValueDataEntry("4", diasDoMes[4]));
                data.add(new ValueDataEntry("5", diasDoMes[5]));
                data.add(new ValueDataEntry("6", diasDoMes[6]));
                data.add(new ValueDataEntry("7", diasDoMes[7]));
                data.add(new ValueDataEntry("8", diasDoMes[8]));
                data.add(new ValueDataEntry("9", diasDoMes[9]));
                data.add(new ValueDataEntry("10", diasDoMes[10]));
                data.add(new ValueDataEntry("11", diasDoMes[11]));
                data.add(new ValueDataEntry("12", diasDoMes[12]));
                data.add(new ValueDataEntry("13", diasDoMes[13]));
                data.add(new ValueDataEntry("14", diasDoMes[14]));
                data.add(new ValueDataEntry("15", diasDoMes[15]));
                data.add(new ValueDataEntry("16", diasDoMes[16]));
                data.add(new ValueDataEntry("17", diasDoMes[17]));
                data.add(new ValueDataEntry("18", diasDoMes[18]));
                data.add(new ValueDataEntry("19", diasDoMes[19]));
                data.add(new ValueDataEntry("20", diasDoMes[20]));
                data.add(new ValueDataEntry("21", diasDoMes[21]));
                data.add(new ValueDataEntry("22", diasDoMes[22]));
                data.add(new ValueDataEntry("23", diasDoMes[23]));
                data.add(new ValueDataEntry("24", diasDoMes[24]));
                data.add(new ValueDataEntry("25", diasDoMes[25]));
                data.add(new ValueDataEntry("26", diasDoMes[26]));
                data.add(new ValueDataEntry("27", diasDoMes[27]));
                data.add(new ValueDataEntry("28", diasDoMes[28]));
                data.add(new ValueDataEntry("29", diasDoMes[29]));
                data.add(new ValueDataEntry("30", diasDoMes[30]));
                data.add(new ValueDataEntry("31", diasDoMes[31]));

                column = cartesian.column(data);

                cartesian.setTitle("Qtde de vezes aberta em " + getMesCorrentePorExtenso() + " (por dia)");
                cartesian.getXAxis().setTitle("Dia do mês");
                cartesian.getYAxis().setTitle("Quantidade");
                cartesian.getYScale().getTicks().setInterval(2);

            } else if(chartId == R.id.action_chart3){
                data.add(new ValueDataEntry("Janeiro", mesesDoAno[1]));
                data.add(new ValueDataEntry("Fevereiro", mesesDoAno[2]));
                data.add(new ValueDataEntry("Março", mesesDoAno[3]));
                data.add(new ValueDataEntry("Abril", mesesDoAno[4]));
                data.add(new ValueDataEntry("Maio", mesesDoAno[5]));
                data.add(new ValueDataEntry("Junho", mesesDoAno[6]));
                data.add(new ValueDataEntry("Julho", mesesDoAno[7]));
                data.add(new ValueDataEntry("Agosto", mesesDoAno[8]));
                data.add(new ValueDataEntry("Setembro", mesesDoAno[9]));
                data.add(new ValueDataEntry("Outubro", mesesDoAno[10]));
                data.add(new ValueDataEntry("Novembro", mesesDoAno[11]));
                data.add(new ValueDataEntry("Dezembro", mesesDoAno[12]));

                column = cartesian.column(data);

                cartesian.setTitle("Qtde de vezes aberta (por mês) - 2018");
                cartesian.getXAxis().setTitle("Mês");
                cartesian.getYAxis().setTitle("Quantidade");
            }

            cartesian.getTitle().setFontColor("black");
            cartesian.getXAxis().getTitle().setFontColor("black");
            cartesian.getXAxis().getLabels().setFontColor("black");
            cartesian.getYAxis().getTitle().setFontColor("black");
            cartesian.getYAxis().getLabels().setFontColor("black");
            cartesian.getYAxis().getLabels().setFormat("{%value}");

            column.setColor("#F18C11");
            column.getTooltip().setPosition(Position.CENTER_BOTTOM);
            cartesian.setBackground("#7EB7FA");
            anyChartView.setChart(cartesian);
        }
    }

    public void searchLogsByNodeId(String nodeid) {
       URL searchUrl = RestUtil.buildUrl("logs","orderBy=\"node\"&equalTo=\"" + nodeid + "\"");
       new NodeSearchTask().execute(searchUrl);
    }

    public class NodeSearchTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String result = null;
            try {
                result = RestUtil.doGet(searchUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && !result.equals("")) {
                List<ActionLog> listLogRecycler = RestUtil.parseActionLogJSONArray(result);
                calculaVezesAbertaHoje(listLogRecycler);
                calculaVezesAbertaNoMes(listLogRecycler);
                calculaVezesAbertaNoAno(listLogRecycler);
                if(chartId == 0){
                    displayChart(R.id.action_chart1);
                }
            } else {
                showErrorMessage();
            }
        }
    }

    private String getMesCorrentePorExtenso(){
        int mesCorrente = Integer.valueOf(currentDate.substring(3,5));
        if(mesCorrente == 1){
            return "Janeiro";
        } else if(mesCorrente == 2){
            return "Fevereiro";
        } else if(mesCorrente == 3){
            return "Março";
        } else if(mesCorrente == 4){
            return "Abril";
        } else if(mesCorrente == 5){
            return "Maio";
        } else if(mesCorrente == 6){
            return "Junho";
        } else if(mesCorrente == 7){
            return "Julho";
        } else if(mesCorrente == 8){
            return "Agosto";
        } else if(mesCorrente == 9){
            return "Setembro";
        } else if(mesCorrente == 10){
            return "Outubro";
        } else if(mesCorrente == 11){
            return "Novembro";
        } else if(mesCorrente == 12){
            return "Dezembro";
        }
        return null;
    }

    private void calculaVezesAbertaHoje(List<ActionLog> logs){
        manhaHoje = 0;
        tardeHoje = 0;
        noiteHoje = 0;

        if(logs != null && logs.size() > 0) {
            for (ActionLog log: logs) {
                if(log.getTopic().equals("state")){
                    if(log.getDate().equals(currentDate)){
                        int hour = Integer.valueOf(log.getTime().substring(0,2));
                        if(hour >= 0 && hour < 12){
                            manhaHoje++;
                        } else if(hour >= 12 && hour < 18){
                            tardeHoje++;
                        } else if(hour >= 18 && hour < 24){
                            noiteHoje++;
                        }
                    }
                }
            }
        }
    }

    private void calculaVezesAbertaNoMes(List<ActionLog> logs){
        diasDoMes = new int[32];
        int mesCorrente = Integer.valueOf(currentDate.substring(3,5));

        if(logs != null && logs.size() > 0) {
            for (ActionLog log: logs) {
                if(log.getTopic().equals("state")){

                    int mes = Integer.valueOf(log.getDate().substring(3,5));
                    if(mes == mesCorrente){
                        int dia = Integer.valueOf(log.getDate().substring(0,2));
                        diasDoMes[dia]++;
                    }
                }
            }
        }
    }

    private void calculaVezesAbertaNoAno(List<ActionLog> logs){
        mesesDoAno = new int[33];
        int anoCorrente = Integer.valueOf(currentDate.substring(6,10));

        if(logs != null && logs.size() > 0) {
            for (ActionLog log: logs) {
                if(log.getTopic().equals("state")){

                    int ano = Integer.valueOf(log.getDate().substring(6,10));
                    if(ano == anoCorrente){
                        int mes = Integer.valueOf(log.getDate().substring(3,5));
                        mesesDoAno[mes]++;
                    }
                }
            }
        }
    }

    private void showErrorMessage() {
        Toast.makeText(this,"ERRO", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.charts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        chartId = itemThatWasClickedId;
        displayChart(itemThatWasClickedId);
        return super.onOptionsItemSelected(item);
    }
}