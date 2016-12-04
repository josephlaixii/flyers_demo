package com.flyers.db_software_incorporateion.db_flyers;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;



import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String url = "http://flyers.smartcanucks.ca/";
    private ArrayList<String> pics = new ArrayList<String>();
    private ArrayList<String> title = new ArrayList<String>();
    private ArrayList<String> gurl = new ArrayList<String>();
    private List<String> nofrillsurl = new ArrayList<String>();
    private String foodurl;
    private String src,src2,src3;
    private RecyclerView my_recycler_view;
    private MainActivityAdapter adapter;
    private FlyerActivity flyerActivity;
    private Button b;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.app_bar_main );
        textView = (TextView) findViewById(R.id.text);


        setTitle("All Flyers");
        new Logo().execute();




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

    public class Logo extends AsyncTask<Void, Void, Void> {
        ArrayList<String> gurlUpdate = new ArrayList<String>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            for (int j = 0; j < gurl.size(); j++) {
                if(j > 51 && j < 481) {
                    gurlUpdate.add(gurl.get(j));
                }
            }
            my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
            my_recycler_view.setHasFixedSize(true);
            my_recycler_view.setLayoutManager(new GridLayoutManager(MainActivity.this,3));
            adapter = new MainActivityAdapter(MainActivity.this,pics,title,gurlUpdate);
            flyerActivity = new FlyerActivity();
            System.out.println("This is the gurlupdate" + gurlUpdate);
            my_recycler_view.setAdapter(adapter);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Connection.Response response = null;
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response2 = client.newCall(request).execute();

                if (response2.code() == 200) {
                    Document docsmart = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate")
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                            .maxBodySize(0)
                            .method(org.jsoup.Connection.Method.GET)
                            .referrer("http://www.google.com")
                            .get();

                    Elements div = docsmart.select("div.image-block");
                    Elements img = div.select("img");

                    for (Element el : img) {
                        src = el.absUrl("src");
                        pics.add(src);
                    }

                    Elements div2 = docsmart.select("div.title-area");
                    Elements span = div2.select("span");

                    for (Element el : span) {
                        src2 = el.text();
                        title.add(src2);
                    }

                    int c=0;
                    Elements ul = docsmart.select("ul");
                    Elements li = ul.select("li");
                    Elements a = li.select("a");
                    for (Element el : a) {
                        src3 = el.absUrl("href");
                        gurl.add(src3);

                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

    }


}
