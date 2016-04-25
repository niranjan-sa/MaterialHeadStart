package com.blogreader.niranjansa.materialheadstart.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blogreader.niranjansa.materialheadstart.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public void registerToFirebase(View view)
    {

        Firebase ref = new Firebase("https://shining-torch-8290.firebaseio.com");
        final EditText userNameInput =(EditText)findViewById(R.id.usernameReg);
        final EditText mobileInput =(EditText)findViewById(R.id.mobileReg);
        final EditText emailInput =(EditText)findViewById(R.id.emailReg);
        final EditText passwordInput=(EditText)findViewById(R.id.passwordReg);
        EditText repasswordInput=(EditText)findViewById(R.id.repasswordReg);
        final TextView errorInput=(TextView)findViewById(R.id.errors);
        final String username= String.valueOf(userNameInput.getText());
        final String mobile= String.valueOf(mobileInput.getText());
        final String email= String.valueOf(emailInput.getText());
        final String password=""+passwordInput.getText();
        String repassword=""+repasswordInput.getText();

        if(username.isEmpty() || username.equals(""))
        {
            errorInput.setText("Enter user name");
            return;
        }
        if(mobile.length()!=10)
        {
            errorInput.setText("Enter 10 digit mobile no");
        }

        if(!isValidEmail(email))
        {
            errorInput.setText("enter valid Email ID");
            return;
        }

        if(password.length()<8)
        {
            errorInput.setText("password length must be greater than 8 characters");
            return;
        }

        if(!password.equals(repassword))
        {
            errorInput.setText("password don't match");
            return;
        }
        else
        {
            errorInput.setText("");
        }

        ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                errorInput.setText("Successfully registered with email id "+email);
                User user=new User(username,mobile,email,password);
                FirebaseConnection.setUser(user);
                String s=email;
                s= s.substring(0, s.indexOf("."));
                Firebase firebase=new Firebase(FirebaseConnection.getPath()+"/Users");
                firebase=firebase.child(s);
                firebase.setValue(user);

                onBackPressed();
                //System.out.println("Successfully created user account with uid: " + result.get("uid"));
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                errorInput.setText("Try again");
            }
        });
    }

}
