package com.dott.webapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedsActivity extends AppCompatActivity {

    // upload start
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Uri[] results = null;

            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;

        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            if (requestCode == FILECHOOSER_RESULTCODE) {

                if (null == this.mUploadMessage) {
                    return;

                }

                Uri result = null;

                try {
                    if (resultCode != RESULT_OK) {

                        result = null;

                    } else {

                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }

                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;

            }
        }

        return;
    }

    /// uplaod end
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigation;

    private int currentApiVersion;
    private ProgressBar progress;

    private AVLoadingIndicatorView avi;

     WebView wv ;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String indicator=getIntent().getStringExtra("indicator");
        avi= (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator(indicator);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.dott);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        new userInfo(this).execute();





        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) { startActivity(new Intent(FeedsActivity.this, HomeActivity.class)); }   });

        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) { startActivity(new Intent(FeedsActivity.this, BusinessesActivity.class)); }   });

        Button button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) { startActivity(new Intent(FeedsActivity.this, JobsActivity.class)); }   });

        Button button4 = (Button)findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) { startActivity(new Intent(FeedsActivity.this, FeedsActivity.class)); }   });

        Button button5 = (Button)findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) { startActivity(new Intent(FeedsActivity.this, EventsActivity.class)); }   });

        Button button6 = (Button)findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) { startActivity(new Intent(FeedsActivity.this, NewsActivity.class)); }   });

        Button button7 = (Button)findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) { startActivity(new Intent(FeedsActivity.this, BlogActivity.class)); }   });














        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        if(sharedpreferences.getBoolean("logged",false) == true)
        {
            //redirect to home page

        }
        else
        {
            // ask for login
        //    startActivity(new Intent(FeedsActivity.this, MainActivity.class));
        }





        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        String user =  sharedpreferences.getString("username", "");
        String pass =  sharedpreferences.getString("password", "");


////////////////

        ////////////////Webview start////////////////



         wv = (WebView) findViewById(R.id.webView);
        wv.setWebChromeClient(new MyWebViewClient());

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(100);
        progress.setVisibility(View.VISIBLE);





/*

        //    WebView wv = new WebView(this);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url); //this is controversial - see comments and other answers
                return true;
            }
        });
*/

        //   setContentView(wv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setUserAgentString("dottwebview");

        /// upload start
        wv.getSettings().setAllowFileAccess(true);
        wv.setWebViewClient(new PQClient());
        wv.setWebChromeClient(new PQChromeClient());
        /// upload end

        final StringBuilder postData = new StringBuilder();
        postData.append("username"); postData.append("=");postData.append(user);postData.append("&");
        postData.append("password"); postData.append("=");postData.append(pass);

        postData.append("&");postData.append("link");postData.append("=");
        postData.append("http://dott.com/en/feeds");
        wv.postUrl("http://dott.com/en/auth/mlogin", EncodingUtils.getBytes(postData.toString(), "BASE64"));

        //    wv.loadUrl("http://dott.com/en");
    //    wv.setWebViewClient(new WebViewClient());



        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Toast.makeText(getApplicationContext(), "Network", Toast.LENGTH_SHORT).show();

  // facebook start
          String hallostring = url.toString();

//                                                                             
                if (hallostring.length()>37)
                {
                    String facebook_link = hallostring.substring(0, 24);
                    if(facebook_link.equals("https://www.facebook.com"))
                    {
                        wv.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; One Build/KTU84L.H4) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36 [FB_IAB/FB4A;FBAV/28.0.0.20.16;]\n");
                        //     Toast.makeText(getApplicationContext(), "it is facebook", Toast.LENGTH_SHORT).show();
                    }

                }

                if(url.contains("https://www.facebook.com/dialog/return"))
                {
                    Toast.makeText(getApplicationContext(), "return", Toast.LENGTH_SHORT).show();
                    wv.goBack();
                    // wv.getSettings().setUserAgentString("dottwebview");
                    return  true;
                }

// facebook end
                if(hallostring.equals("http://dott.com/en")||hallostring.equals("http://dott.com/ar")||hallostring.equals("http://dott.com/tl")||hallostring.equals("http://dott.com"))
                {
                    //     startActivity(new Intent(HomeActivity.this, login.class));
                }
                else
                {

                    String asubstring = hallostring.substring(19, 22);




                    if (asubstring.equals("job"))
                    {
                      //  startActivity(new Intent(FeedsActivity.this, JobsActivity.class));
                    }

                    if (asubstring.equals("fee"))
                    {
                      //  startActivity(new Intent(FeedsActivity.this, FeedsActivity.class));
                    }

                    if (asubstring.equals("eve"))
                    {
                      //  startActivity(new Intent(FeedsActivity.this, EventsActivity.class));
                    }

                    if (asubstring.equals("job"))
                    {
                       // startActivity(new Intent(FeedsActivity.this, JobsActivity.class));
                    }

                    if (asubstring.equals("new"))
                    {
                       // startActivity(new Intent(FeedsActivity.this, NewsActivity.class));
                    }

                    if (asubstring.equals("blo"))
                    {
                      //  startActivity(new Intent(FeedsActivity.this, BlogActivity.class));
                    }

                    if (asubstring.equals("aut"))
                    {
                        startActivity(new Intent(FeedsActivity.this, MainActivity.class));
                    }

                    if (asubstring.equals("bus"))
                    {
                       // startActivity(new Intent(FeedsActivity.this, BusinessesActivity.class));
                    }



                }




                    view.loadUrl(url);

                return true;
            }
        });


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(FeedsActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

        navigation = (NavigationView) findViewById(R.id.navigation_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                int id = menuItem.getItemId();
                sharedpreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);
                final String userid =    sharedpreferences.getString("id","1")  ;
                switch (id) {

                    case R.id.navigation_item_1:
                        //   startActivity(new Intent(login.this, MainActivity.class));
                        FeedsActivity.this.progress.setProgress(0);
                        wv.loadUrl("http://dott.com/en/profile-"+userid+"");
                        break;
                    case R.id.navigation_item_2:
                        wv.loadUrl("http://dott.com/en/profile-"+userid+"/about");
                        break;
                    case R.id.navigation_item_3:
                        wv.loadUrl("http://dott.com/en/profile-"+userid+"/friends");
                        break;
                    case R.id.navigation_item_4:
                        wv.loadUrl("http://dott.com/en/profile-"+userid+"/albums");
                        break;
                    case R.id.navigation_item_5:
                        wv.loadUrl("http://dott.com/en/profile-"+userid+"/videos");
                        break;
                    case R.id.navigation_item_6:
                        wv.loadUrl("http://dott.com/en/profile-"+userid+"/resume");
                        break;
                    case R.id.navigation_item_7:
                        wv.loadUrl("http://dott.com/en/profile-"+userid+"/businesses");
                        break;

                    case R.id.logout:
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean("logged", false);
                        editor.commit();
                        startActivity(new Intent(FeedsActivity.this, MainActivity.class));
                        break;
                }
                return false;
            }
        });

    }

    public void GetUserInfo(View View) {
        new userInfo(this).execute();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (wv.canGoBack()) {
                        wv.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

















    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            FeedsActivity.this.setValue(newProgress);
            super.onProgressChanged(view, newProgress);
            if (newProgress  == 100) {
                //  progress.setVisibility(View.INVISIBLE);
                //    ImageView iimageView = (ImageView) findViewById(R.id.lImage);
                //    iimageView.setVisibility(View.GONE);
            }
        }
    }
    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.web_view, menu);
            return true;
        }
    */




    public void setValue(int progress) {
        this.progress.setProgress(progress);
        TextView imageView = (TextView) findViewById(R.id.imageView);
        if (progress>=100) // code to be added
        {
            //     this.progress.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);

            stopAnim();
        } // code to be added
        if (progress==0) // code to be added
        {
            this.progress.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            startAnim();
        } // code to be added


    }

    void startAnim(){
        // avi.show();
        avi.smoothToShow();
    }

    void stopAnim(){
        // avi.hide();
        avi.smoothToHide();
    }





































    private class userInfo extends AsyncTask<String, Void, Void> {



        private final Context context;

        public userInfo(Context c){

            this.context = c;
//            this.error = status;
//            this.type = t;
        }

        protected void onPreExecute(){

        }

        @Override
        protected Void doInBackground(String... params) {
            try {



                final TextView outputView = (TextView) findViewById(R.id.showOutput);
                URL url = new URL("http://dott.com/mobile/user_info");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();


                sharedpreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);
                final String id =    sharedpreferences.getString("id","1")  ;





                StringBuilder urlParameters = new StringBuilder();


                urlParameters.append("id");
                urlParameters.append("=");
                urlParameters.append(id);


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
                FeedsActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Log.d("myTag", "");

                        //     TextView outputView = (TextView) findViewById(R.id.outputView);


// json start
                        String strJson = responseOutput.toString();
                        try {
                            JSONObject jObject = new JSONObject(strJson);
                            String name = jObject.getString("name");
                            //   String cover = jObject.getString("cover");
                            String profile = jObject.getString("profile");
                            String country = jObject.getString("country");
                            String city = jObject.getString("city");

                            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
                            View headerView = navigationView.inflateHeaderView(R.layout.drawer_header);
                            ImageView iv = (ImageView)headerView.findViewById(R.id.profile_image);
                            Picasso.with(FeedsActivity.this)
                                    .load(profile)
                                    .into(iv);
                            TextView profile_name = (TextView)headerView.findViewById(R.id.profile_name);
                            profile_name.setText(name);

                            TextView profile_location = (TextView)headerView.findViewById(R.id.profile_location);

                            profile_location.setText(country + " , "+ city);

                            //      outputView.setText(aJsonString);


                            if(profile.toString().equals("false"))
                            {
                                //    outputView.setText("error" );

                            }

                            else
                            {
                                //     outputView.setText("good" );

                                // save user profile data start
                                //   sharedpreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);
                                //    SharedPreferences.Editor editor = sharedpreferences.edit();
                                //      editor.putString("id", responseOutput.toString());
                                //   editor.putString("name", name);
                                //   editor.putString("cover", password);
                                //  editor.putString("profile", password);
                                //  editor.putString("country", password);
                                //   editor.putString("city", password);
                                //       editor.putBoolean("logged", true);
                                //     editor.commit();
                                // save login end

                                //        startActivity(new Intent(HomeActivity.this, AfterRegisterActivity.class));

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

// json end

                        //   progress.dismiss();
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
            //    progress.dismiss();
        }

    }

    /// upload start
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    public class PQChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            FeedsActivity.this.setValue(newProgress);
            super.onProgressChanged(view, newProgress);
            if (newProgress  == 100) {
                //  progress.setVisibility(View.INVISIBLE);
                //    ImageView iimageView = (ImageView) findViewById(R.id.lImage);
                //    iimageView.setVisibility(View.GONE);
            }
        }


        // For Android 5.0
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePath;

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e(TAG, "Unable to create Image File", ex);
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");

            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

            return true;

        }

        // openFileChooser for Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

            mUploadMessage = uploadMsg;
            // Create AndroidExampleFolder at sdcard
            // Create AndroidExampleFolder at sdcard

            File imageStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)
                    , "AndroidExampleFolder");

            if (!imageStorageDir.exists()) {
                // Create AndroidExampleFolder at sdcard
                imageStorageDir.mkdirs();
            }

            // Create camera captured image file path and name
            File file = new File(
                    imageStorageDir + File.separator + "IMG_"
                            + String.valueOf(System.currentTimeMillis())
                            + ".jpg");

            mCapturedImageURI = Uri.fromFile(file);

            // Camera capture image intent
            final Intent captureIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");

            // Create file chooser intent
            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");

            // Set camera intent to file chooser
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                    , new Parcelable[] { captureIntent });

            // On select image call onActivityResult method of activity
            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);


        }




    }




    public class PQClient extends WebViewClient {
        ProgressDialog progressDialog;

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // If url contains mailto link then open Mail Intent
            if (url.contains("mailto:")) {

                // Could be cleverer and use a regex
                //Open links in new browser
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                // Here we can open new activity

                return true;

            }else {

                // Stay within this webview and load url
                view.loadUrl(url);
                return true;
            }
        }

        //Show loader on url load


        // Called when all page resources loaded

    }

    /// upload end


} // end