package gustavogr.iotsmartlock.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesColumn;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAnchor;
import com.anychart.anychart.HoverMode;
import com.anychart.anychart.Position;
import com.anychart.anychart.ValueDataEntry;
import com.anychart.anychart.TooltipPositionMode;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gustavogr.iotsmartlock.Model.ActionLog;
import gustavogr.iotsmartlock.R;
import gustavogr.iotsmartlock.Util.RestUtil;

public class ChartActivity extends AppCompatActivity {

    String nodeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(nodeid)) {
            nodeid = intent.getStringExtra(nodeid);
        }

        searchLogsByNodeId(nodeid);
        displayChart(R.id.action_chart1);
    }

    public void displayChart(int chartId){
        if (chartId == R.id.action_chart1) {
            setContentView(R.layout.activity_node_chart);
            AnyChartView anyChartView = findViewById(R.id.any_chart_view);

            Cartesian cartesian = AnyChart.column();

            List<DataEntry> data = new ArrayList<>();
            data.add(new ValueDataEntry("Manhã", 17));
            data.add(new ValueDataEntry("Tarde", 17));
            data.add(new ValueDataEntry("Noite", 34));

            CartesianSeriesColumn column = cartesian.column(data);

            column.getTooltip()
                    .setPosition(Position.CENTER_BOTTOM)
                    .setAnchor(EnumsAnchor.CENTER_BOTTOM).setOffsetY(0d);

            cartesian.setAnimation(true);
            cartesian.setTitle("Aberturas da instalação (hoje) - " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

            cartesian.getXAxis().setTitle("Período do dia");
            cartesian.getYAxis().setTitle("Aberturas");

            cartesian.getYAxis().getLabels().setFormat("{%Value}{groupsSeparator: }");

            anyChartView.setChart(cartesian);

        } else if (chartId == R.id.action_chart2) {
            setContentView(R.layout.activity_node_chart);
            AnyChartView anyChartView = findViewById(R.id.any_chart_view);

            Cartesian cartesian = AnyChart.column();

            List<DataEntry> data = new ArrayList<>();
            data.add(new ValueDataEntry("1", 17));
            data.add(new ValueDataEntry("2", 17));
            data.add(new ValueDataEntry("3", 17));
            data.add(new ValueDataEntry("4", 17));
            data.add(new ValueDataEntry("5", 17));
            data.add(new ValueDataEntry("6", 17));
            data.add(new ValueDataEntry("7", 17));
            data.add(new ValueDataEntry("8", 17));
            data.add(new ValueDataEntry("9", 17));
            data.add(new ValueDataEntry("10", 17));
            data.add(new ValueDataEntry("11", 17));
            data.add(new ValueDataEntry("12", 17));
            data.add(new ValueDataEntry("13", 17));
            data.add(new ValueDataEntry("14", 34));
            data.add(new ValueDataEntry("15", 34));
            data.add(new ValueDataEntry("16", 34));
            data.add(new ValueDataEntry("17", 34));
            data.add(new ValueDataEntry("18", 34));
            data.add(new ValueDataEntry("19", 34));
            data.add(new ValueDataEntry("20", 34));
            data.add(new ValueDataEntry("21", 34));
            data.add(new ValueDataEntry("22", 34));
            data.add(new ValueDataEntry("23", 34));
            data.add(new ValueDataEntry("24", 34));
            data.add(new ValueDataEntry("25", 34));
            data.add(new ValueDataEntry("26", 34));
            data.add(new ValueDataEntry("27", 34));
            data.add(new ValueDataEntry("28", 34));
            data.add(new ValueDataEntry("29", 34));
            data.add(new ValueDataEntry("30", 34));
            data.add(new ValueDataEntry("31", 34));

            CartesianSeriesColumn column = cartesian.column(data);

            column.getTooltip()
                    .setPosition(Position.CENTER_BOTTOM)
                    .setAnchor(EnumsAnchor.CENTER_BOTTOM).setOffsetY(0d);

            cartesian.setAnimation(true);

            cartesian.setTitle("Aberturas da instalação (por dia) - Agosto");

            cartesian.getXAxis().setTitle("Dia do mês");
            cartesian.getYAxis().setTitle("Aberturas");

            cartesian.getYAxis().getLabels().setFormat("{%Value}{groupsSeparator: }");

            anyChartView.setChart(cartesian);
        } else if (chartId == R.id.action_chart3) {
            setContentView(R.layout.activity_node_chart);
            AnyChartView anyChartView = findViewById(R.id.any_chart_view);

            Cartesian cartesian = AnyChart.column();

            List<DataEntry> data = new ArrayList<>();
            data.add(new ValueDataEntry("Janeiro", 17));
            data.add(new ValueDataEntry("Fevereiro", 17));
            data.add(new ValueDataEntry("Março", 34));
            data.add(new ValueDataEntry("Abril", 17));
            data.add(new ValueDataEntry("Maio", 17));
            data.add(new ValueDataEntry("Junho", 17));
            data.add(new ValueDataEntry("Julho", 17));
            data.add(new ValueDataEntry("Agosto", 17));
            data.add(new ValueDataEntry("Setembro", 17));
            data.add(new ValueDataEntry("Outubro", 17));
            data.add(new ValueDataEntry("Novembro", 17));
            data.add(new ValueDataEntry("Dezembro", 17));

            CartesianSeriesColumn column = cartesian.column(data);

            column.getTooltip()
                    .setPosition(Position.CENTER_BOTTOM)
                    .setAnchor(EnumsAnchor.CENTER_BOTTOM).setOffsetY(0d);

            cartesian.setAnimation(true);
            cartesian.setTitle("Aberturas da instalação (por mês) - 2018");

            cartesian.getXAxis().setTitle("Mês");
            cartesian.getYAxis().setTitle("Aberturas");

            cartesian.getYAxis().getLabels().setFormat("{%Value}{groupsSeparator: }");

            anyChartView.setChart(cartesian);
        } else if (chartId == R.id.action_chart4) {
            setContentView(R.layout.activity_node_chart);
            AnyChartView anyChartView = findViewById(R.id.any_chart_view);

            Cartesian cartesian = AnyChart.column();

            List<DataEntry> data = new ArrayList<>();
            data.add(new ValueDataEntry("2017", 17));
            data.add(new ValueDataEntry("2018", 34));

            CartesianSeriesColumn column = cartesian.column(data);

            column.getTooltip()
                    .setTitleFormat("{%X}")
                    .setPosition(Position.CENTER_BOTTOM)
                    .setAnchor(EnumsAnchor.CENTER_BOTTOM)
                    .setOffsetX(0d)
                    .setOffsetY(5d)
                    .setFormat("${%Value}{groupsSeparator: }");

            cartesian.setAnimation(true);
            cartesian.setTitle("Alterações do estado da trava (por mês)");

            cartesian.getYScale().setMinimum(0d);

            cartesian.getYAxis().getLabels().setFormat("${%Value}{groupsSeparator: }");

            cartesian.getTooltip().setPositionMode(TooltipPositionMode.POINT);
            cartesian.getInteractivity().setHoverMode(HoverMode.BY_X);

            cartesian.getXAxis().setTitle("Alterações");
            cartesian.getYAxis().setTitle("Mês");

            anyChartView.setChart(cartesian);
        }
    }

    public void searchLogsByNodeId(String nodeid)
    {
       URL searchUrl = RestUtil.buildUrl("logs","orderBy=\"node\"&equalTo=\"00124B000F28C303\"");
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
            Log.d("Resultado do GET", result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && !result.equals("")) {
                List<ActionLog> listLogRecycler = RestUtil.parseActionLogJSONArray(result);
                Log.d("Lista", listLogRecycler.toString());
            } else {
                showErrorMessage();
            }
        }
    }

    private void showErrorMessage() {
        Toast.makeText(this,"ERRO", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.charts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        displayChart(itemThatWasClickedId);
        return super.onOptionsItemSelected(item);
    }
}
