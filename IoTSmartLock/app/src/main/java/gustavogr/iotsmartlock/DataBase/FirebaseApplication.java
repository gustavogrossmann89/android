package gustavogr.iotsmartlock.DataBase;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import gustavogr.iotsmartlock.Activity.ListActivity;
import gustavogr.iotsmartlock.Activity.LoginActivity;
import gustavogr.iotsmartlock.Util.Helper;

/**
 * autor: Gustavo Grossmann
 * data: Ago/2018
 * descrição: Métodos do FIrebase, para realizar a autenticação de usuário no app
 */
public class FirebaseApplication extends Application {

    private static final String TAG = FirebaseApplication.class.getSimpleName();
    public FirebaseAuth firebaseAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth = FirebaseAuth.getInstance();
    }

    public String getFirebaseUserAuthenticateId() {
        String userId = null;
        if (firebaseAuth.getCurrentUser() != null) {
            userId = firebaseAuth.getCurrentUser().getUid();
        }
        return userId;
    }

    public void checkUserLogin(final Context context) {
        if (firebaseAuth.getCurrentUser() != null) {
            Intent listIntent = new Intent(context, ListActivity.class);
            context.startActivity(listIntent);
        }
    }

    public void isUserCurrentlyLogin(final Context context) {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (null != user) {
                    Intent listIntent = new Intent(context, ListActivity.class);
                    context.startActivity(listIntent);
                } else {
                    Intent loginIntent = new Intent(context, LoginActivity.class);
                    context.startActivity(loginIntent);
                }
            }
        };
    }

    public void createNewUser(final Context context, String email, String password, final TextView errorMessage) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Helper.displayMessageToast(context, "Erro no registro de novo usuário");
                        } else {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            Intent listIntent = new Intent(context, ListActivity.class);
                            listIntent.putExtra("userid", task.getResult().getUser().getUid());
                            context.startActivity(listIntent);
                        }
                    }
                });
    }

    public void loginAUser(final Context context, String email, String password, final TextView errorMessage) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Helper.displayMessageToast(context, "Usuário e/ou senha inválidos");
                        } else {

                            Intent listIntent = new Intent(context, ListActivity.class);
                            listIntent.putExtra("userid", task.getResult().getUser().getUid());
                            context.startActivity(listIntent);
                        }
                    }
                });
    }
}
