package com.blogreader.niranjansa.materialheadstart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogreader.niranjansa.materialheadstart.R;

public class UserInfoActivity extends AppCompatActivity {
    User user;
    RelativeLayout r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        r=(RelativeLayout)findViewById(R.id.changePasswordRelLay);
        r.setVisibility(View.GONE);
        TextView username=(TextView)findViewById(R.id.usernameU);
        TextView email=(TextView)findViewById(R.id.emailU);
        TextView mobile=(TextView)findViewById(R.id.mobilenoU);
        User user=FirebaseConnection.getUser();
        if(user==null)
        {
            Toast.makeText(this,"please login",Toast.LENGTH_LONG).show();
            onBackPressed();
        }
        username.setText(user.getUsername());
        email.setText(user.getEmail());
        mobile.setText(user.getMobileNo());
    }

    public void showChangePasswordMenu(View view)
    {
        r.setVisibility(View.VISIBLE);
    }
}
