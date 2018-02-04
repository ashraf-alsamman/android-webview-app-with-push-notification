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


public class ForgetPasswordActivity extends AppCompatActivity {



    private ProgressDialog progress;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



        TextView cancel= (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(ForgetPasswordActivity.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });










        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        if(sharedpreferences.getBoolean("logged",false) == true)
        {
            //redirect to home page
            startActivity(new Intent(ForgetPasswordActivity.this, HomeActivity.class));
        }
        else
        {
            // ask for login
        }

        /////     login start





        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();

    }





    public void SendPass(View View) {
        new PostClass(this).execute();

    }



    private class PostClass extends AsyncTask<String, Void, Void> {

        EditText email = (EditText) findViewById(R.id.email);
        public   String emailtext = email.getText().toString();


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
                URL url = new URL("http://dott.com/en/auth/sendMForgetpass");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();





                final String token_id = FirebaseInstanceId.getInstance().getToken();



                if (emailtext ==null){emailtext ="a";};
                StringBuilder urlParameters = new StringBuilder();
                urlParameters.append("email");
                urlParameters.append("=");
                urlParameters.append(emailtext);



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
                ForgetPasswordActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Log.d("myTag", responseOutput.toString());



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isFinishing()){

                                    new AlertDialog.Builder(ForgetPasswordActivity.this)
                                            .setTitle("Dott")
                                            .setMessage(responseOutput.toString())
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



                        if(responseOutput.toString().equals("0"))
                        {



                        }

                        else
                        {



                        //    Intent intent = new Intent(ForgetPasswordActivity.this, MainActivity.class);
                        //    ForgetPasswordActivity.this.startActivity(intent);
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