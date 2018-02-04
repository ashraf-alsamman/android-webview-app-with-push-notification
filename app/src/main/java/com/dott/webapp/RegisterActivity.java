package com.dott.webapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


// import android.support.design.widget.NavigationView;


public class RegisterActivity extends AppCompatActivity {
    EditText editText;

    private ProgressDialog progress;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView login= (TextView) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(RegisterActivity.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
             }
        });


        sharedpreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("first_time", false);
        editor.commit();



        /////     login start


        // login end







     //   TextView outputView = (TextView) findViewById(R.id.showOutput);
     //   outputView.setText(sharedpreferences.getString("name", ""));


     //   FirebaseMessaging.getInstance().subscribeToTopic("test");
    //    FirebaseInstanceId.getInstance().getToken();

    }





    public void sendPostRequest(View View) {
        new PostClass(this).execute();

        sharedpreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);
/*
        if(sharedpreferences.getBoolean("logged",false) == true)
        {
            //redirect to home page
            startActivity(new Intent(RegisterActivity.this, AfterRegisterActivity.class));
        }
        */
    }



    private class PostClass extends AsyncTask<String, Void, Void> {

        EditText name_EditText = (EditText) findViewById(R.id.name);
        EditText phone_EditText = (EditText) findViewById(R.id.phone);
        EditText email_EditText = (EditText) findViewById(R.id.email);
        EditText password_EditText = (EditText) findViewById(R.id.password);
        EditText password_confirmation_EditText = (EditText) findViewById(R.id.password_confirmation);
        TextView date_of_birth_EditText = (TextView) findViewById(R.id.date_of_birth);







        ///////////////////////
        public   String name = name_EditText.getText().toString();
        public   String phone =  phone_EditText.getText().toString();
        public   String email =  email_EditText.getText().toString();
        public   String password =  password_EditText.getText().toString();
        public   String password_confirmation =  password_confirmation_EditText.getText().toString();
        public   String date_of_birth =  date_of_birth_EditText.getText().toString();

        RadioGroup radioGenderGroup = (RadioGroup) findViewById(R.id.gender);
        int selectedId = radioGenderGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        RadioButton  radioGenderButton = (RadioButton) findViewById(selectedId);
        public   String gender =  radioGenderButton.getText().toString();





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



            //    final TextView outputView = (TextView) findViewById(R.id.showOutput);
                URL url = new URL("http://dott.com/en/auth/mregister");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                //      final String token_id = FirebaseInstanceId.getInstance().getToken();
                StringBuilder urlParameters = new StringBuilder();
                urlParameters.append("name");urlParameters.append("=");urlParameters.append(name);
                urlParameters.append("&");
                urlParameters.append("phone");urlParameters.append("=");urlParameters.append(phone);
                urlParameters.append("&");
                urlParameters.append("email");urlParameters.append("=");urlParameters.append(email);
                urlParameters.append("&");
                urlParameters.append("password");urlParameters.append("=");urlParameters.append(password);
                urlParameters.append("&");
                urlParameters.append("password_confirmation");urlParameters.append("=");urlParameters.append(password_confirmation);
                urlParameters.append("&");
                urlParameters.append("date_of_birth");urlParameters.append("=");urlParameters.append(date_of_birth);
                urlParameters.append("&");
                urlParameters.append("gender");urlParameters.append("=");urlParameters.append(gender);



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


                String asubstring = responseOutput.toString().substring(9, 13);

                if(asubstring.contains("true")) {
                    //     outputView.setText("good" );

                    // save login start
                    sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    //      editor.putString("id", responseOutput.toString());
                    editor.putString ("username", email);
                    editor.putString ("password", password);
                    editor.putBoolean("logged", true);
                    editor.commit();
                    // save login end


                    startActivity(new Intent(RegisterActivity.this, AfterRegisterActivity.class));
                    finish();
                }


// json start
                RegisterActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        String strJson = responseOutput.toString();
                        try {
                            JSONObject jObject = new JSONObject(strJson);
                            String aJsonString = jObject.getString("staus");
                            String messageArray = jObject.getString("message");

                           final StringBuilder message = new StringBuilder();
                            JSONArray jArr = new JSONArray(messageArray);
                            for (int i=0; i < jArr.length(); i++) {
                                String obj =  jArr.getString(i);
                                message.append(obj.toString());
                                message.append("\n");
                            }

                            if(aJsonString.toString().equals("false"))
                            {

                                runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                            if (!isFinishing()){

                                                                new AlertDialog.Builder(RegisterActivity.this)
                                                                        .setTitle("Dott")
                                                                        .setMessage(message)
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

////////////////////////////////////////////////
                            }





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

// json end


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

    private class GetClass extends AsyncTask<String, Void, Void> {

        private final Context context;

        public GetClass(Context c){
            this.context = c;
        }

        protected void onPreExecute(){
            progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

           //     final TextView outputView = (TextView) findViewById(R.id.showOutput);
                URL url = new URL("http://requestb.in/1cs29cy1");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                String urlParameters = "fizz=buzz";
                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                //output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator")  + "Type " + "GET");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                RegisterActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                   //     outputView.setText(output);
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

//        protected void onPostExecute() {
//            progress.dismiss();
//        }

    }





    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }






}
