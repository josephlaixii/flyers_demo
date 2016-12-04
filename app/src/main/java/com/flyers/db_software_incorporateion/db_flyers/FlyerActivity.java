package com.flyers.db_software_incorporateion.db_flyers;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.flyers.db_software_incorporateion.db_flyers.eventbus.FlyerArrayBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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


public class FlyerActivity extends AppCompatActivity {

    public static final String BASE_URL = "";

     private ArrayList<String> pics = new ArrayList<String>();
    private List<String> nofrillsurl = new ArrayList<String>();
    private ArrayList<String> gurlUpdate = new ArrayList<>();
    private String foodurl;
    private String src,asrc;
    private RecyclerView my_recycler_view;
    private FlyerAdapter adapter;
    private Logo2 mTask;
    TextView textView;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_stores);

        Bundle extra = getIntent().getBundleExtra("low");
        gurlUpdate = (ArrayList<String>) extra.getStringArrayList("gurl");

        Bundle extra2 = getIntent().getBundleExtra("bpos");
        i = extra2.getInt("bpos");

        System.out.println("The pos" + i);

        mTask = (Logo2) new Logo2().execute();

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

                System.out.println("THis is the status Code " +  response2.code());
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
                        nofrillsurl.add(urla);


                    }

                    Request request2 = new Request.Builder()
                            .url(nofrillsurl.get(1))
                            .build();

                    Response response3 = client.newCall(request2).execute();

                        if(response3.code() == 200) {
                            // Connect to the web site
                            Document document = Jsoup.connect(nofrillsurl.get(1)).header("Accept-Encoding", "gzip, deflate")
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

                            int i2[] = {0,1,pics.size()-1};
                            int ilen = i2.length;
                            for (int j = 0; j < ilen; j++) {
                                pics.remove(i2[j]-j);
                            }

                        }else{
                            getCallingActivity();
                        }

                }else{
                    getCallingActivity();

                }

            } catch (IOException e) {
                e.printStackTrace();
                getCallingActivity();
                System.out.println("This is error" + e);
            }
            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

//            if(nofrillsurl.get(1) != null){
//                foodurl = nofrillsurl.get(1);
//            }
//
//
//            textView.setText(nofrillsurl.get(0));

            my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
            my_recycler_view.setHasFixedSize(true);
            my_recycler_view.setLayoutManager(new LinearLayoutManager(FlyerActivity.this,LinearLayoutManager.HORIZONTAL,false));
            adapter = new FlyerAdapter(FlyerActivity.this,pics,foodurl);
            my_recycler_view.setAdapter(adapter);
            System.out.println("DONE DFSFDSFSDFSDF : " + foodurl);
        }
    }

}
