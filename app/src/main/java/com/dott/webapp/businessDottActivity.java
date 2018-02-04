package com.dott.webapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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



public class businessDottActivity extends AppCompatActivity {

    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_dott);





        // load all bus
        // get length of all bus and divid on 30
        // divid on lengh


        new PostClass(this).execute();



    }



    public void login(View View) {
        new PostClass(this).execute();

    }



    private class PostClass extends AsyncTask<String, Void, Void> {
/*
        EditText password = (EditText) findViewById(R.id.password);
        public   String passordtext =  password.getText().toString();
*/
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
                URL url = new URL("http://dott.com/mobile/buz/list");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                //      final String token_id = FirebaseInstanceId.getInstance().getToken();
                StringBuilder urlParameters = new StringBuilder();
                urlParameters.append("limit");urlParameters.append("=");urlParameters.append(1);
                urlParameters.append("&");
                urlParameters.append("offset");urlParameters.append("=");urlParameters.append(2);




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


// json start
                businessDottActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        String strJson = responseOutput.toString();
                        Log.d("myTag", strJson);

                        try
                        {

                            JSONArray jArray = new JSONArray(strJson);
                            JSONObject jObj=new JSONObject ();
                            String[] data=new String[jArray.length()];

                            for(int i=0;i<jArray.length();i++)
                            {
                                jObj= jArray.getJSONObject(i);
                                data[i]=json_data.getString("value");

                            }

                             adapter = new GameAdapter(this, R.layout.activity_business_dott, data);
                            listView1 = (ListView)findViewById(R.id.ListViewId);
                            listView1.setAdapter(adapter);
                        }
                        catch(Exception e)
                        {
                        }








                        try {
                            JSONObject jObject = new JSONObject(strJson);
                            String aJsonString = jObject.getString("staus");
                            String messageArray = jObject.getString("message");

                            final StringBuilder message = new StringBuilder();
                            JSONArray jArr = new JSONArray(messageArray);
                            for (int i=0; i < jArr.length(); i++) {
                                String  obj =  jArr.getString(i);
                                message.append(obj.toString());
                                message.append("\n");
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

                businessDottActivity.this.runOnUiThread(new Runnable() {

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












}
