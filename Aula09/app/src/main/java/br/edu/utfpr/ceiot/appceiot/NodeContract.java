package br.edu.utfpr.ceiot.appceiot;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * TODO (5) Criar a classe NodeContract
 *  Esta classe será utilizada pelo ContentProvider
 */
public class NodeContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "br.edu.utfpr.ceiot.appceiot";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_NODES = "nodes";

    /**
     * TODO (6) Criar a classe Node
     * Esta classe será utilizada para recuperar dados do SQLite
     */
    public static final class Node implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NODES).build();

        public static final String TABLE_NAME = "node";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";

    }
}
