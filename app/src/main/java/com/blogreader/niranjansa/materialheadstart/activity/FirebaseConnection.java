package com.blogreader.niranjansa.materialheadstart.activity;

import android.util.Log;

import com.blogreader.niranjansa.materialheadstart.model.User;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by vintej on 23/4/16.
 */
public class FirebaseConnection {

    final static ArrayList<User> userArray=new ArrayList<User>();
    private static User user;
    private static AuthData authData;
    private static String path;
    static boolean b=false;

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        FirebaseConnection.path = path;
    }
    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        FirebaseConnection.user = user;
    }


    public static AuthData getAuthData() {
        return authData;
    }

    public static void setAuthData(AuthData authData) {
        FirebaseConnection.authData = authData;
    }



    public static void loginToFirebase(final String emailID, final String passwd)
    {

        Firebase ref = new Firebase(path);
        ref.authWithPassword(emailID, passwd, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authDatas) {
                authData = authDatas;

                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
            }
        });

    }
    public static void getAllUsers() {

        Firebase ref = new Firebase(path + "/Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user1 = postSnapshot.getValue(User.class);
                    userArray.add(user1);
                    Log.i("User", user1.getEmail());

                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


    }
    public static void setUserInfo(final String email)
    {
        String s=email.substring(0,email.indexOf("."));
        Firebase ref=new Firebase(path+"/Users/"+s);

        ;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user1=snapshot.getValue(User.class);
                user=user1;
                /*for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user1 = postSnapshot.getValue(User.class);
                   if(user1.getEmail().equals(email))
                   {
                       user=user1;
                       Log.i("user",""+user.getEmail());
                       return;
                   }
                }*/
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


    }


}
