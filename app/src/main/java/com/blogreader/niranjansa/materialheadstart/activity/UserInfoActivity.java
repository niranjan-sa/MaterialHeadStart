package com.blogreader.niranjansa.materialheadstart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogreader.niranjansa.materialheadstart.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class UserInfoActivity extends AppCompatActivity {
    private User user;
    private RelativeLayout r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     //   getSupportActionBar().setTitle("User Information");

        r=(RelativeLayout)findViewById(R.id.changePasswordRelLay);
        r.setVisibility(View.GONE);
        TextView username=(TextView)findViewById(R.id.usernameU);
        TextView email=(TextView)findViewById(R.id.emailU);
        TextView mobile=(TextView)findViewById(R.id.mobilenoU);
        user=FirebaseConnection.getUser();
        if(user==null)
        {
            Toast.makeText(this,"please login",Toast.LENGTH_LONG).show();
            onBackPressed();
        }
        username.setText(user.getUsername());
        email.setText(user.getEmail());
        mobile.setText(user.getMobileNo());
    }

    public void showChangePasswordMenu(View view) {
        r.setVisibility(View.VISIBLE);
        final TextView message=(TextView)findViewById(R.id.messageU);
        message.setText("");
    }
    public void changePassword(View view)
    {
        EditText oldPassInput=(EditText)findViewById(R.id.currPassU);
        EditText newPassInput=(EditText)findViewById(R.id.newPassU);
        final TextView message=(TextView)findViewById(R.id.messageU);
        String oldPass=""+oldPassInput.getText();
        String newPass=""+newPassInput.getText();
        Firebase firebase=new Firebase(FirebaseConnection.getPath());
        firebase.changePassword(user.getEmail(), oldPass, newPass, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                r.setVisibility(View.GONE);
                message.setText("Successfully Changed password.");

            }
            @Override
            public void onError(FirebaseError firebaseError) {
                r.setVisibility(View.GONE);
                message.setText("Wrong password entered. Please try again");


            }
        });

    }
}
