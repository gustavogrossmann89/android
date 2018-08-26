package gustavogr.iotsmartlock.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import gustavogr.iotsmartlock.MQTT.AndroidMqttClient;
import gustavogr.iotsmartlock.Model.Node;
import gustavogr.iotsmartlock.MQTT.MqttCallbackHandler;
import gustavogr.iotsmartlock.R;
import gustavogr.iotsmartlock.Util.Helper;
import gustavogr.iotsmartlock.Util.RestUtil;

/**
 * autor: Gustavo Grossmann
 * data: Ago/2018
 * descrição: Atividade que apresenta as informações de determinada instalação
 */
public class NodeActivity extends AppCompatActivity {

    private String mqttServerURL = "iotsmartlock.mooo.com";
    private String mqttServerPort = "1883";
    AndroidMqttClient mqttClient;

    TextView nomeTextView;
    TextView descricaoTextView;
    TextView mqttidTextView;
    TextView dataTextView;
    TextView lockStatusTextView;
    TextView alarmStatusTextView;
    TextView installationStatusTextView;
    Switch switchRadioLockBtn;
    Switch switchRadioAlarmBtn;

    String id;
    String userid;
    String nome;
    String descricao;
    String mqttid;
    String lockstatus;
    String alarmstatus;
    String installationstatus;
    String data;

    private ImageView imageView;
    private Uri fileUploadPath;
    private Uri fileDownloadPath;
    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Intent intent = getIntent();
        if (intent.hasExtra(Node.NODE_ID)) {
            id = intent.getStringExtra(Node.NODE_ID);
        }
        if (intent.hasExtra(Node.NODE_USERID)) {
            userid = intent.getStringExtra(Node.NODE_USERID);
        }
        if (intent.hasExtra(Node.NODE_NOME)) {
            nome = intent.getStringExtra(Node.NODE_NOME);
        }
        if (intent.hasExtra(Node.NODE_MQTTID)) {
            mqttid = intent.getStringExtra(Node.NODE_MQTTID);
        }
        if (intent.hasExtra(Node.NODE_DESCRICAO)) {
            descricao = intent.getStringExtra(Node.NODE_DESCRICAO);
        }
        if (intent.hasExtra(Node.NODE_LOCKSTATUS)) {
            lockstatus = intent.getStringExtra(Node.NODE_LOCKSTATUS);
        }
        if (intent.hasExtra(Node.NODE_ALARMSTATUS)) {
            alarmstatus = intent.getStringExtra(Node.NODE_ALARMSTATUS);
        }
        if (intent.hasExtra(Node.NODE_INSTALLATIONSTATUS)) {
            installationstatus = intent.getStringExtra(Node.NODE_INSTALLATIONSTATUS);
        }
        if (intent.hasExtra(Node.NODE_DTATUALIZACAO)) {
            data = intent.getStringExtra(Node.NODE_DTATUALIZACAO);
        }

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setTitle("Detalhes da instalação");
        }

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        Button chartButton = (Button) findViewById(R.id.chartBtn);
        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chartTaskIntent = new Intent(NodeActivity.this, ChartActivity.class);
                chartTaskIntent.putExtra("nodeid", mqttid);
                chartTaskIntent.putExtra("nome", nome);
                startActivity(chartTaskIntent);
            }
        });

        Button reportButton = (Button) findViewById(R.id.reportBtn);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reportTaskIntent = new Intent(NodeActivity.this, ReportActivity.class);
                reportTaskIntent.putExtra("nodeid", mqttid);
                reportTaskIntent.putExtra("nome", nome);
                startActivity(reportTaskIntent);
            }
        });

        refreshScreen();

        try {
            mqttClient = new AndroidMqttClient(this, mqttServerURL, mqttServerPort,
                    new MqttCallBackActivity(this,"NodeActivityMqttCallback"));
            IMqttToken token = mqttClient.connect();

        } catch (MqttException e) {
            e.printStackTrace();
        }

        downloadImage();
        //StorageReference gsReference = storage.getReferenceFromUrl("gs://iotsmartlockgg.appspot.com/images/-LKnjvyiAAq9d1MxVVCN");
        //Glide.with(this).load(gsReference).into(imageView);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem/foto para a sua instalação"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ) {
            fileUploadPath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUploadPath);
                imageView.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if(fileUploadPath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Salvando...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + id);
            ref.putFile(fileUploadPath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Helper.displayMessageToast(NodeActivity.this, "Imagem salva com sucesso");
                        return;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Helper.displayMessageToast(NodeActivity.this, "Sistema indisponível!");
                        return;
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Salvando: "+(int)progress+"%");
                    }
                });
        }
    }

    private void downloadImage() {

        StorageReference ref = storage.getReferenceFromUrl("gs://iotsmartlockgg.appspot.com/images/").child(id);

        if(ref != null) {
            try {
                final File localFile = File.createTempFile("images", "jpg");
                ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imageView.setImageBitmap(bitmap);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        URL searchUrl = RestUtil.buildUrl("nodes/" + id,null);
        new NodeActivity.NodeRefreshTask().execute(searchUrl);
    }

    public void refreshValues(Node node) {
        id = node.getId();
        userid = node.getUserid();
        nome = node.getNome();
        mqttid = node.getMqttid();
        descricao = node.getDescricao();
        data = node.getDtatualizacao();
        lockstatus = node.getLockstatus();
        alarmstatus = node.getAlarmstatus();
        installationstatus = node.getInstallationstatus();
    }

    public void refreshScreen() {
        nomeTextView = (TextView) findViewById(R.id.node_nome);
        nomeTextView.setText(nome);
        mqttidTextView = (TextView) findViewById(R.id.node_mqttid);
        mqttidTextView.setText("MQTT ID: " + mqttid);
        descricaoTextView = (TextView) findViewById(R.id.node_descricao);
        descricaoTextView.setText(descricao);
        dataTextView = (TextView) findViewById(R.id.node_dtatualizacao);
        dataTextView.setText("Última iteração em: " + data);

        lockStatusTextView = (TextView) findViewById(R.id.node_lock_status);
        alarmStatusTextView = (TextView) findViewById(R.id.node_alarm_status);
        installationStatusTextView = (TextView) findViewById(R.id.node_installation_status);

        switchRadioLockBtn = (Switch) findViewById(R.id.node_switchLockBtn);
        switchRadioAlarmBtn = (Switch) findViewById(R.id.node_switchAlarmBtn);

        if(lockstatus.equals("0")){
            switchRadioLockBtn.setChecked(true);
            lockStatusTextView.setTextColor(Color.RED);
        } else {
            switchRadioLockBtn.setChecked(false);
            lockStatusTextView.setTextColor(Color.parseColor("#FF309128"));
        }

        if(alarmstatus.equals("1")){
            switchRadioAlarmBtn.setChecked(true);
            alarmStatusTextView.setTextColor(Color.RED);
        } else {
            switchRadioAlarmBtn.setChecked(false);
            alarmStatusTextView.setTextColor(Color.parseColor("#FF309128"));
        }

        if(installationstatus.equals("1")){
            installationStatusTextView.setText("Instalação: FECHADA");
        } else {
            installationStatusTextView.setText("Instalação: ABERTA");
        }
    }

    public void publishLock() {
        String status = null;
        if(switchRadioLockBtn.isChecked()) {
            status = switchRadioLockBtn.getTextOn().toString();
        } else {
            status = switchRadioLockBtn.getTextOff().toString();
        }
        try {
            mqttClient.publishMessage(status,0,mqttid + "/lock");
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishAlarm() {
        String status = null;
        if(switchRadioAlarmBtn.isChecked()) {
            status = switchRadioAlarmBtn.getTextOn().toString();
        } else {
            status = switchRadioAlarmBtn.getTextOff().toString();
        }
        try {
            mqttClient.publishMessage(status,0,mqttid + "/alarm");
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void changeLockStatus(View view) {
        publishLock();
        URL createUrl = RestUtil.buildUrl("nodes/" + id,null);
        new NodeActivity.NodeUpdateTask().execute(createUrl);
        refresh();
    }

    public void changeAlarmStatus(View view) {
        publishAlarm();
        URL createUrl = RestUtil.buildUrl("nodes/" + id,null);
        new NodeActivity.NodeUpdateTask().execute(createUrl);
        refresh();
    }

    public void deleteNode() {
        URL createUrl = RestUtil.buildUrl("nodes/" + id,null);
        new NodeActivity.NodeDeleteTask().execute(createUrl);
        finish();
    }

    public class NodeRefreshTask extends AsyncTask<URL, Void, String> {

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
                Node node = RestUtil.parseJSON(result);
                refreshValues(node);
                refreshScreen();
            } else {
                showErrorMessage();
            }
        }
    }

    public class NodeUpdateTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL createUrl = urls[0];

            if(switchRadioLockBtn.isChecked()) {
                lockstatus = switchRadioLockBtn.getTextOn().toString();
            } else {
                lockstatus = switchRadioLockBtn.getTextOff().toString();
            }
            if(switchRadioAlarmBtn.isChecked()) {
                alarmstatus = switchRadioAlarmBtn.getTextOn().toString();
            } else {
                alarmstatus = switchRadioAlarmBtn.getTextOff().toString();
            }
            String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            String result = null;
            try {
                result = RestUtil.updateOnFirebase(createUrl,new Node(id, userid, nome, descricao, mqttid, lockstatus, alarmstatus, installationstatus, data));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public class NodeDeleteTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL createUrl = urls[0];

            if(switchRadioLockBtn.isChecked()) {
                lockstatus = switchRadioLockBtn.getTextOn().toString();
            } else {
                lockstatus = switchRadioLockBtn.getTextOff().toString();
            }
            if(switchRadioAlarmBtn.isChecked()) {
                alarmstatus = switchRadioAlarmBtn.getTextOn().toString();
            } else {
                alarmstatus = switchRadioAlarmBtn.getTextOff().toString();
            }

            String result = null;
            try {
                result = RestUtil.deleteOnFirebase(createUrl,new Node(id, userid, nome, descricao, mqttid, lockstatus, alarmstatus, installationstatus, data));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    private void showErrorMessage() {
        Toast.makeText(this,"ERRO", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_edit) {

            Context context = NodeActivity.this;
            Class destinationActivity = EditNodeActivity.class;
            Intent startChildActivityIntent = new Intent(context, destinationActivity);
            startChildActivityIntent.putExtra(Node.NODE_ID, id);
            startChildActivityIntent.putExtra(Node.NODE_USERID, userid);
            startChildActivityIntent.putExtra(Node.NODE_NOME, nome);
            startChildActivityIntent.putExtra(Node.NODE_DESCRICAO, descricao);
            startChildActivityIntent.putExtra(Node.NODE_MQTTID, mqttid);
            startChildActivityIntent.putExtra(Node.NODE_LOCKSTATUS, lockstatus);
            startChildActivityIntent.putExtra(Node.NODE_ALARMSTATUS, alarmstatus);
            startChildActivityIntent.putExtra(Node.NODE_INSTALLATIONSTATUS, installationstatus);
            startChildActivityIntent.putExtra(Node.NODE_DTATUALIZACAO, data);

            startActivity(startChildActivityIntent);
            return true;
        } else if (itemThatWasClickedId == R.id.action_delete) {
            AlertDialog diaBox = AskOption();
            diaBox.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                .setTitle("Excluir")
                .setMessage("Deseja excluir essa instalação?")
                .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteNode();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }

    public class MqttCallBackActivity extends MqttCallbackHandler {
        public MqttCallBackActivity(Context context, String clientHandle) {
            super(context, clientHandle);
        }
    }
}