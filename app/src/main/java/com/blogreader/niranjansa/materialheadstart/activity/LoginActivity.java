package com.blogreader.niranjansa.materialheadstart.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blogreader.niranjansa.materialheadstart.R;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends AppCompatActivity {

    Firebase ref;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        path=getResources().getString(R.string.firebaselink);
        Log.i("d", path);

        final TextView errorInput=(TextView)findViewById(R.id.messagelogin);
     //   if(FirebaseConnection.getAuthData()!=null &&FirebaseConnection.getAuthData().getAuth()!=null &&ref!=null);
      //  {
     //       //errorInput.setText("You are already logined" + FirebaseConnection.getAuthData().getAuth().getUid());
     //   }
       // Firebase.setAndroidContext(this);

    }


    public void authenticateUser(View view) {
        EditText emailInput =(EditText)findViewById(R.id.emailLog);
        EditText passwordInput=(EditText)findViewById(R.id.passwordLog);
        final String email= String.valueOf(emailInput.getText());
        final TextView errorInput=(TextView)findViewById(R.id.messagelogin);

        String password=""+passwordInput.getText();
       // FirebaseConnection.loginToFirebase(email,password);
        Log.i("email",email+password);
        ref = new Firebase(path);
        ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                FirebaseConnection.setAuthData(authData);
                FirebaseConnection.setUserInfo(email);
                errorInput.setText("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());

                onBackPressed();

            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                errorInput.setText("couldnt login. please check internet connection");
            }
        });



    }

}
