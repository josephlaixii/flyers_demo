package com.flyers.db_software_incorporateion.db_flyers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.flyers.db_software_incorporateion.db_flyers.R.id.swipeContainer;


public class FlyerActivity extends AppCompatActivity {

    public static final String BASE_URL = "";

     private ArrayList<String> pics = new ArrayList<String>();
    private List<String> flyerurl = new ArrayList<String>();
    private ArrayList<String> gurlUpdate = new ArrayList<>();
    private ArrayList<String> expiredate = new ArrayList<>();
    private ArrayList<String> h3tag = new ArrayList<>();
    private ArrayList<String> flyerimagetag = new ArrayList<>();
    private ArrayList<String> gurlUpdate2 = new ArrayList<>();

    private String foodurl;
    private String src,asrc,title;
    private RecyclerView my_recycler_view;
    private FlyerAdapter adapter;
    private Logo2 mTask;
    TextView textView;
    int i = 0;
    Intent intent;
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_stores_flyeractivity);

        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(FlyerActivity.this,LinearLayoutManager.VERTICAL,false));

        adapter = new FlyerAdapter(FlyerActivity.this,expiredate,h3tag,flyerimagetag,gurlUpdate2,title);
        my_recycler_view.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extra3 = getIntent().getBundleExtra("btitle");
        title = extra3.getString("btitle");

        getSupportActionBar().setTitle(title + " " + "Flyers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);





        Bundle extra = getIntent().getBundleExtra("low");
        gurlUpdate = (ArrayList<String>) extra.getStringArrayList("gurl");

        Bundle extra2 = getIntent().getBundleExtra("bpos");
        i = extra2.getInt("bpos");





        mTask = (Logo2) new Logo2().execute();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
            }
        });
        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                // Your code to refresh the list here.

                // Make sure you call swipeContainer.setRefreshing(false)

                // once the network request has completed successfully.
                new Logo2().execute();
                System.out.println("hello");

            }

        });

    }

    public void saveArrayToPreference(String prestring,String key,ArrayList<String> stringArrayList,
                                      SharedPreferences pref,SharedPreferences.Editor editor){
        if(this != null) {
            pref = this.getSharedPreferences(prestring, MODE_PRIVATE);
            editor = pref.edit();
            editor.putInt(key, stringArrayList.size());

            for(int i=0;i<stringArrayList.size();i++)
            {
                editor.remove("Status_" + i);
                editor.putString("Status_" + i, stringArrayList.get(i));
            }
//
            editor.apply();
        }

    }

    public void loadArray(String prestring, String key, SharedPreferences pref, ArrayList<String> list){
        pref = this.getSharedPreferences(prestring,MODE_PRIVATE);
        int size = pref.getInt(key,0);
        for(int i=0;i<size;i++)
        {
            list.add(pref.getString("Status_" + i, null));
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = my_recycler_view.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            my_recycler_view.getLayoutManager().onRestoreInstanceState(listState);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mTask.cancel(true);
        System.out.println("pressedback button");
    }

    // Logo AsyncTask
    private class Logo2 extends AsyncTask<Void, Void, Void> {
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




                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(gurlUpdate.get(i))
                        .build();

                Response response2 = client.newCall(request).execute();

                System.out.println("THis is the status Code " +  gurlUpdate.get(i));
                if(response2.code()==200) {
                    Document docsmart = Jsoup.connect(gurlUpdate.get(i)).header("Accept-Encoding", "gzip, deflate")
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                            .maxBodySize(0)
                            .method(org.jsoup.Connection.Method.GET)
                            .referrer("http://www.google.com")
                            .get();

                    Elements div = docsmart.select("div.flyer-card");
                    Elements a = div.select("a");
                    Elements href = a.select("href");
                    String urla;


                    for (Element el2 : a) {

                        urla = el2.attr("abs:href");
                        urla += "/all";

                        System.out.println("A TAG Found!");
                        System.out.println("A TAG attribute is :" + urla);
                        flyerurl.add(urla);


                    }

                    if (flyerurl.get(1) != null) {
                        Request request2 = new Request.Builder()
                                .url(flyerurl.get(1))
                                .build();

                        Response response3 = client.newCall(request2).execute();

                        if (response3.code() == 200) {
                            // Connect to the web site
                            Document document = Jsoup.connect(flyerurl.get(1)).header("Accept-Encoding", "gzip, deflate")
                                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                                    .maxBodySize(0)
                                    .method(org.jsoup.Connection.Method.GET)
                                    .referrer("http://www.google.com")
                                    .get();


                            Elements img = document.getElementsByTag("img");


                            for (Element el : img) {
                                src = el.absUrl("src");
                                pics.add(src);

                            }

                            int i2[] = {0, 1, pics.size() - 1};
                            int ilen = i2.length;
                            for (int j = 0; j < ilen; j++) {
                                pics.remove(i2[j] - j);
                            }

                            Elements flyercont = div.select("div.flyer-content");
                            Elements flyerstore = flyercont.select("div.flyer-store");
                            Elements flyerstoreinner = flyerstore.select("div.flyer-store-inner");

                            Elements divflyerimagetag = div.select("div.flyer-image");
                            Elements flyerimage = divflyerimagetag.select("img");

                            Elements span = flyerstoreinner.select("span");
                            Elements h3 = flyerstoreinner.select("h3");

                            for (Element el: flyerimage){
                                flyerimagetag.add(el.absUrl("src"));
                            }

                            for (Element el: span){
                                expiredate.add(el.text());
                            }

                            for (Element el: h3){
                                h3tag.add(el.text());
                            }


                            Elements div2 = document.select("div.flyer-card");
                            Elements flyercont2 = div2.select("div.flyer-content");
                            Elements flyerstore2 = flyercont2.select("div.flyer-store");
                            Elements flyerstoreinner2 = flyerstore2.select("div.flyer-store-inner");


                            Elements a2 = flyercont.select("a");

                            for (Element el : a2) {

                                gurlUpdate2.add(el.absUrl("href"));

                            }

//                            System.out.println("This is the expire date " + gurlUpdate2);
//                            System.out.println("This is the expire date " + expiredate);
//                            System.out.println("This is the expire date " + h3tag);
                        } else {
//                            intent = getIntent();
//                            finish();
//                            startActivity(intent);
                        }
                    }else{
//                        intent = getIntent();
//                        finish();
//                        startActivity(intent);
                    }

                }else{
//                    intent = getIntent();
//                    finish();
//                    startActivity(intent);

                }

            } catch (IOException e) {
                e.printStackTrace();

                System.out.println("This is error" + e);

            }
            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
            my_recycler_view.setHasFixedSize(true);
            my_recycler_view.setLayoutManager(new LinearLayoutManager(FlyerActivity.this,LinearLayoutManager.VERTICAL,false));

            adapter = new FlyerAdapter(FlyerActivity.this,expiredate,h3tag,flyerimagetag,gurlUpdate2,title);
            my_recycler_view.setAdapter(adapter);
            System.out.println("pics man" +pics);
            System.out.println("DONE DFSFDSFSDFSDF : " + foodurl);
            swipeContainer.setRefreshing(false);
        }
    }

}
