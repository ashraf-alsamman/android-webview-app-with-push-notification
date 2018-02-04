package com.dott.webapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

public class login extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigation;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        String Name = "name";
        String NameData = "name texttttt";

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Name, NameData);

        editor.commit();
////////////////

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        String user =  sharedpreferences.getString("username", "");
        String pass =  sharedpreferences.getString("password", "");



        //      initInstances();


/*
        Intent intent = getIntent();
        String user = intent.getStringExtra("user");
        String pass = intent.getStringExtra("pass");
*/
        ////////////////Webview start////////////////
        //////////////////////////////////


        final WebView wv = (WebView) findViewById(R.id.webView);

        //    WebView wv = new WebView(this);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url); //this is controversial - see comments and other answers
                return true;
            }
        });


        //   setContentView(wv);
        wv.getSettings().setJavaScriptEnabled(true);


        final StringBuilder postData = new StringBuilder();
        postData.append("username");postData.append("=");postData.append("ashraf.alsamman@gmail.com");
        postData.append("&");
        postData.append("password");  postData.append("="); postData.append("admin321");
        postData.append("&");
        postData.append("link");postData.append("=");postData.append("http://beta.dott.com/");

        wv.postUrl("http://dott.com/en/auth/mlogin", EncodingUtils.getBytes(postData.toString(), "BASE64"));

        wv.setWebViewClient(new WebViewClient());

        // test start
     //   String webUrl = wv.ur();
  //       TextView outputView = (TextView) findViewById(R.id.showOutput);
  //      outputView.setText(webUrl.toString() );

        // test end


      //  wv.loadUrl("http://dott.com/en");

        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();

                String hallostring = url.toString();
if(hallostring.equals("http://dott.com/en")||hallostring.equals("http://dott.com/ar")||hallostring.equals("http://dott.com/tl"))
{
           Log.v("TEST", "home");
           TextView outputView = (TextView) findViewById(R.id.showOutput);
 }
else
{
    String asubstring = hallostring.substring(19, 22);

    Log.v("TEST", asubstring);
    TextView outputView = (TextView) findViewById(R.id.showOutput);
    outputView.setText(asubstring );
}



                if(url.equals("http://dott.com/en/jobs/all")){
                    Toast.makeText(getApplicationContext(), "222", Toast.LENGTH_SHORT).show();
                    wv.loadUrl("https://www.google.com/");
                }
                else{
                    view.loadUrl(url);
                }
                return true;
            }
        });



        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(login.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

        navigation = (NavigationView) findViewById(R.id.navigation_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_1:
                     //   startActivity(new Intent(login.this, MainActivity.class));
                            wv.loadUrl("http://dott.com/en/events");
                        break;
                    case R.id.navigation_item_2:
                        //     startActivity(new Intent(GoogleActivity.this, ViralAndroidActivity.class));
                        break;
                    case R.id.navigation_item_3:
                        //     startActivity(new Intent(GoogleActivity.this,GoogleActivity.class));
                        break;
                    case R.id.navigation_item_4:
                        //    startActivity(new Intent(GoogleActivity.this, ViralAndroidActivity.class));
                        break;
                    case R.id.navigation_item_5:
                        //     startActivity(new Intent(GoogleActivity.this,GoogleActivity.class));
                        break;
                }
                return false;
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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


} // end