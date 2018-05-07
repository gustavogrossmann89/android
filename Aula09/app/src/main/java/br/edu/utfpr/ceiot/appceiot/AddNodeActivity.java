package br.edu.utfpr.ceiot.appceiot;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * TODO (18) Criar uma segunda atividade para a tela de add um node.
 */
public class AddNodeActivity extends AppCompatActivity {

    //Declarar elementos do layout
    private EditText nameEditText;
    private EditText descEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_node);

        nameEditText = (EditText) findViewById(R.id.name_editText);
        descEditText = (EditText) findViewById(R.id.desc_editText);

    }

    /**
     * Salva o node.
     * @param view
     */
    public void onClickAddNode(View view) {

        String name = nameEditText.getText().toString();
        String description = descEditText.getText().toString();

        if (name.length() == 0) {
            Toast.makeText(this,"Name Empty",Toast.LENGTH_LONG);
            return;
        } else if (description.length() == 0) {
            Toast.makeText(this,"Description Empty",Toast.LENGTH_LONG);
            return;
        }

        // Inserir dados por meio do contentProvider
        ContentValues contentValues = new ContentValues();
        contentValues.put(NodeContract.Node.COLUMN_NAME, name);
        contentValues.put(NodeContract.Node.COLUMN_DESCRIPTION, description);

        Uri uri = getContentResolver().insert(NodeContract.Node.CONTENT_URI, contentValues);

        if(uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }

        //TODO (25) Chama o serviço para salvar no firebase
        saveOnFirebase(name,description);

        //Finaliza atividade e volta para a atividade principal.
        finish();
    }

    /**
     * TODO (24) Monta intent para acionar o serviço para salvar no firebase
     * @param name
     * @param description
     */
    public void saveOnFirebase(String name, String description)
    {
        Intent sendDataIntentService = new Intent(this, SendDataIntentService.class);
        sendDataIntentService.setAction(NodeTasks.ACTION_SEND_NODE_TO_FIREBASE);
        sendDataIntentService.putExtra(NodeContract.Node.COLUMN_NAME,name);
        sendDataIntentService.putExtra(NodeContract.Node.COLUMN_DESCRIPTION,description);
        startService(sendDataIntentService);
    }
}
