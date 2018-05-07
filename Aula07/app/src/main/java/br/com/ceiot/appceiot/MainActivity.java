package br.com.ceiot.appceiot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    /*
    * TAG utilizada para log.
    * */
    private static final String TAG = MainActivity.class.getSimpleName();

    /*
     * Constante utilizada comon referência para o Bundle.
     */
    private static final String CEIOT_CALLBACKS_TEXT_KEY = "callbacks";


    //Constantes para logs dos respectivos eventos
    private static final String ON_CREATE = "onCreate";
    private static final String ON_START = "onStart";
    private static final String ON_RESUME = "onResume";
    private static final String ON_PAUSE = "onPause";
    private static final String ON_STOP = "onStop";
    private static final String ON_RESTART = "onRestart";
    private static final String ON_DESTROY = "onDestroy";
    private static final String ON_SAVE_INSTANCE_STATE = "onSaveInstanceState";

    /*
     * Este TextView irá apresentar os logs em tela
     */
    private TextView textView;

    private static final ArrayList<String> callbacks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO (0) Incializa Textview
        textView = (TextView) findViewById(R.id.textView_example);

        //TODO (4) Recupera valores salvos
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CEIOT_CALLBACKS_TEXT_KEY)) {
                String previousEvents = savedInstanceState
                        .getString(CEIOT_CALLBACKS_TEXT_KEY);
                textView.setText(previousEvents);
            }
        }

        //TODO (3) Recupera eventos da lista e limpa lista
        for (int i = callbacks.size() - 1; i >= 0; i--) {
            textView.append(callbacks.get(i) + "\n");
        }
        callbacks.clear();

        logAndAppend(ON_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        logAndAppend(ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        logAndAppend(ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        logAndAppend(ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        logAndAppend(ON_STOP);

        //TODO (1) Adiciona o evento ON_STOP
        callbacks.add(0, ON_STOP);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logAndAppend(ON_RESTART);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logAndAppend(ON_DESTROY);

        //TODO (2) Adiciona o evento ON_DESTROY
        callbacks.add(0, ON_DESTROY);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logAndAppend(ON_SAVE_INSTANCE_STATE);

        //TODO (5) Salva eventos
        String oldLogs = textView.getText().toString();
        outState.putString(CEIOT_CALLBACKS_TEXT_KEY, oldLogs);
    }


    /**
     * Logs to the console and appends the lifecycle method name to the TextView so that you can
     * view the series of method callbacks that are called both from the app and from within
     * Android Studio's Logcat.
     *
     * @param lifecycleEvent The name of the event to be logged.
     */
    private void logAndAppend(String lifecycleEvent) {
        Log.d(TAG, "Lifecycle Event: " + lifecycleEvent);

        textView.append(lifecycleEvent + "\n");
    }
}
