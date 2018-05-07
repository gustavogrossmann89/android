package com.example.gustavo.exemploaula2;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

        //Finaliza atividade e volta para a atividade principal.
        finish();
    }
}
