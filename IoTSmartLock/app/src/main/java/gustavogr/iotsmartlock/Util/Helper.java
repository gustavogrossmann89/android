package gustavogr.iotsmartlock.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * autor: Gustavo Grossmann
 * data: Ago/2018
 * descrição: Helper para disponibilização de mensagens
 */
public class Helper {

    public static final String NAME = "Name";
    public static final String EMAIL = "Email";

    public static final int SELECT_PICTURE = 2000;

    public static boolean isValidEmail(String email){
        if(email.contains("@")){
            return true;
        }
        return false;
    }

    public static void displayMessageToast(Context context, String displayMessage){
        Toast.makeText(context, displayMessage, Toast.LENGTH_LONG).show();
    }
}
