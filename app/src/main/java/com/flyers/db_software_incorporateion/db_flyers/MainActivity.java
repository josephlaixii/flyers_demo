package com.flyers.db_software_incorporateion.db_flyers;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String url = "http://flyers.smartcanucks.ca/food-basics-canada";
    ArrayList<String> pics = new ArrayList<String>();
    List<String> nofrillsurl = new ArrayList<String>();
    String foodurl;
    String src,asrc;
    private RecyclerView my_recycler_view;
    private MainActivityAdapter adapter;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        new Logo().execute();





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Logo AsyncTask
    private class Logo extends AsyncTask<Void, Void, Void> {
        Bitmap bitmap,bitmap2;
        InputStream input,input2;
        ArrayList<String> array_image = new ArrayList<String>();
        private Dialog mProgressDialog;

        protected void onPreExecute() {
            super.onPreExecute();


        }


        protected Void doInBackground(Void... params) {

            Connection.Response response = null;
            try {


                response = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .method(org.jsoup.Connection.Method.GET)
                        .referrer("http://www.google.com")
                        .timeout(9000)
                        .execute();

                int statusCode = response.statusCode();
                System.out.println("THis is the status Code foodbasic " + statusCode);
                if(statusCode==200) {
                    Document docsmart = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate")
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                            .maxBodySize(0)
                            .method(org.jsoup.Connection.Method.GET)
                            .referrer("http://www.google.com")
                            .timeout(9000).get();

                    Elements div = docsmart.select("div.flyer-card");
                    Elements a = div.select("a");
                    Elements href = a.select("href");
                    String urla;


                    for (Element el2 : a) {

                        //for each element get the srs url
                        //asrc = el2.select("li > a").toString();
                        urla = el2.attr("abs:href");
                        urla += "/all";

                        System.out.println("A TAG Found!");
                        System.out.println("A TAG attribute is :" + urla);
                        nofrillsurl.add(urla);


                    }

                    // Connect to the web site
                    Document document = Jsoup.connect(nofrillsurl.get(1)).header("Accept-Encoding", "gzip, deflate")
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                            .maxBodySize(0)
                            .method(org.jsoup.Connection.Method.GET)
                            .referrer("http://www.google.com")
                            .timeout(5000)
                            .get();

                    // Using Elements to get the class data
                    Elements img = document.getElementsByTag("img");
                    //Elements div = document.select("div.bg-flyer-container");
                    // Locate the src attribute
                    String imgSrc = img.attr("src");


                    for (Element el : img) {

                        //for each element get the srs url
                        src = el.absUrl("src");

                        System.out.println("Image Found!");
                        System.out.println("src attribute is : " + src);
                        pics.add(src);


                    }
                }else{
                    System.out.println("received error code : " + statusCode);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(nofrillsurl.get(1) != null){
                foodurl = nofrillsurl.get(1);
            }
            //logoimg.setImageBitmap(bitmap);

            textView.setText(nofrillsurl.get(0));
            int i[] = {0,1,pics.size()-1};
            for (int j = 0; j < i.length; j++) {
                pics.remove(i[j]-j);
            }
            my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
            my_recycler_view.setHasFixedSize(true);
            my_recycler_view.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
            adapter = new MainActivityAdapter(MainActivity.this,pics,foodurl);
            my_recycler_view.setAdapter(adapter);
            System.out.println("DONE DFSFDSFSDFSDF : " + foodurl);
        }
    }


}
