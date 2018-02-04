package com.dott.webapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class Firebase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);


         class FirebaseInstanceIDService extends FirebaseInstanceIdService {

            @Override
            public void onTokenRefresh() {
                String token = FirebaseInstanceId.getInstance().getToken();





                Intent intent = new Intent(FirebaseInstanceIDService.this, MainActivity.class);
                intent.putExtra("token", token);

                FirebaseInstanceIDService.this.startActivity(intent);







            }




        }
        
        
        
        
        
    }
}
