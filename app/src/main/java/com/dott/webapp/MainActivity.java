package com.dott.webapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// import android.support.design.widget.NavigationView;


public class MainActivity extends AppCompatActivity {
    EditText editText;


    private ProgressDialog progress;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();





        TextView ForgetPassword= (TextView) findViewById(R.id.ForgetPassword);
        assert ForgetPassword != null;
        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i=new Intent(MainActivity.this,ForgetPasswordActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


                ;
            }
        });





        TextView button= (TextView) findViewById(R.id.register);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i=new Intent(MainActivity.this,RegisterActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


 ;
            }
        });



        sharedpreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);

        if(sharedpreferences.getBoolean("logged",false) == true)
        {
            //redirect to home page
             startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }


        /////     login start





        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();

    }





    public void login(View View) {
        new PostClass(this).execute();

    }



    private class PostClass extends AsyncTask<String, Void, Void> {

        EditText password = (EditText) findViewById(R.id.password);
        EditText username = (EditText) findViewById(R.id.username);
        public   String usernametext = username.getText().toString();
        public   String passordtext =  password.getText().toString();

        private final Context context;

        public PostClass(Context c){

            this.context = c;
//            this.error = status;
//            this.type = t;
        }

        protected void onPreExecute(){
            progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {



                final TextView outputView = (TextView) findViewById(R.id.showOutput);
                URL url = new URL("http://dott.com/en/auth/update_token");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();





                final String token_id = FirebaseInstanceId.getInstance().getToken();

                StringBuilder urlParameters = new StringBuilder();
                urlParameters.append("username");
                urlParameters.append("=");
                urlParameters.append(usernametext);

                urlParameters.append("&");

                urlParameters.append("password");
                urlParameters.append("=");
                urlParameters.append(passordtext);

                urlParameters.append("&");

                urlParameters.append("token_id");
                urlParameters.append("=");
                urlParameters.append(token_id);


                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters.toString());
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator")  + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                final StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Log.d("myTag", token_id);

                      if(responseOutput.toString().equals("0"))
                       {

                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {

                                   if (!isFinishing()){
                                       new AlertDialog.Builder(MainActivity.this)
                                               .setTitle("Dott")
                                               .setMessage("username or password is incorrect.")
                                               .setCancelable(true)
                                               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       // Whatever...
                                                   }
                                               }).create().show();
                                   }
                               }
                           });

                       }

                       else
                       {

                           EditText password = (EditText) findViewById(R.id.password);
                           EditText username = (EditText) findViewById(R.id.username);
                           String user = username.getText().toString();
                           String pass = password.getText().toString();

                       // save login start
                      //     sharedpreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);
                           SharedPreferences.Editor editor = sharedpreferences.edit();
                           editor.putString("id", responseOutput.toString());
                           editor.putString("username", user);
                           editor.putString("password", pass);
                           editor.putBoolean("logged", true);
                           editor.commit();
                           // save login end

                           Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                       //    intent.putExtra("user", user);
                       //    intent.putExtra("pass", pass);
                           MainActivity.this.startActivity(intent);
                           finish();
                       }

                         progress.dismiss();
                    }
                });


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute() {
            progress.dismiss();
        }

    }




}