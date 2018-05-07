package br.edu.utfpr.ceiot.appceiot;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * TODO (0) Criar a atividade principal (MainActivity).
 * TODO (13) Implementar LoaderManager
 */
public class MainActivity extends AppCompatActivity
        implements
        LoaderManager.LoaderCallbacks<Cursor> {

    //Constantes declaradas
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int NODE_LOADER_ID = 0;

    //TODO (3) Declarar itens para RecyclerVier
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NodeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO (4) Inicializar RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewNodes);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NodeAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        //TODO (12) Inicializar botao add
        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);

        //TODO (17) Implementar acao pra adicionar Node
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent addTaskIntent = new Intent(MainActivity.this, AddNodeActivity.class);
                startActivity(addTaskIntent);
            }
        });

        /*
            TODO (20) Adicionar "deslizar para deletar"
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int id = (int) viewHolder.itemView.getTag();

                String stringId = Integer.toString(id);
                Uri uri = NodeContract.Node.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                getContentResolver().delete(uri, null, null);

                getSupportLoaderManager().restartLoader(NODE_LOADER_ID, null, MainActivity.this);

            }
        }).attachToRecyclerView(mRecyclerView);

        //TODO (15) Inicialzia o Loader
        getSupportLoaderManager().initLoader(NODE_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(NODE_LOADER_ID, null, this);
    }

    /**
     * TODO (16) Implementar Loader
     * Inicializa o Loader e realzia a consulta local.
     * @param id
     * @param args
     * @return
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            //Inicializa cursor
            Cursor mData = null;
            @Override
            protected void onStartLoading() {
                if (mData != null) {
                    deliverResult(mData);
                } else {
                    forceLoad();
                }
            }
            @Nullable
            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(NodeContract.Node.CONTENT_URI,
                            null,
                            null,
                            null,
                            NodeContract.Node._ID);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
