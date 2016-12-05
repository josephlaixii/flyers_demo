/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.flyers.db_software_incorporateion.db_flyers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uk.co.senab.photoview.PhotoView;

public class ViewPagerActivity extends AppCompatActivity {

	private ViewPager mViewPager;
	private ArrayList<Bitmap> arrBitMap = new ArrayList<>();
	private ArrayList<String> gurlUpdate = new ArrayList<>();
	private ArrayList<String> expiredate = new ArrayList<>();
	private String title;
	private int i;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_stores);
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);



		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Bundle extra3 = getIntent().getBundleExtra("btitle");
		title = extra3.getString("btitle");

		getSupportActionBar().setTitle(title);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);



		Bundle extra = getIntent().getBundleExtra("low");
		gurlUpdate = (ArrayList<String>) extra.getStringArrayList("gurl");

		Bundle extra2 = getIntent().getBundleExtra("bpos");
		i = extra2.getInt("bpos");


		if (!gurlUpdate.isEmpty()) {
			new ImageLoadTask(gurlUpdate.get(i)).execute();
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

		System.out.println("pressedback button");
	}

	public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

		private String url;
		Bitmap myBitmap;
		private String src;

		public ImageLoadTask(String url) {
			this.url = url;

		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			try {

				OkHttpClient client = new OkHttpClient();
				Request request = new Request.Builder()
						.url(url)
						.build();

				Response response2 = client.newCall(request).execute();

				if(response2.code() == 200) {

					Document document = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate")
							.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
							.maxBodySize(0)
							.method(org.jsoup.Connection.Method.GET)
							.referrer("http://www.google.com")
							.get();

					Elements img = document.getElementsByTag("img");


					for (Element el : img) {
						src = el.absUrl("src");
						URL newurl = new URL(src);
						myBitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
						arrBitMap.add(myBitmap);
					}

					int i2[] = {0, 1, arrBitMap.size() - 1};
					int ilen = i2.length;
					for (int j = 0; j < ilen; j++) {
						arrBitMap.remove(i2[j] - j);
					}

					Elements div = document.select("div.flyer-card");
					Elements flyercont = div.select("div.flyer-content");
					Elements flyerstore = flyercont.select("div.flyer-store");
					Elements flyerstoreinner = flyerstore.select("div.flyer-store-inner");
					Elements span = flyerstoreinner.select("span");

					for (Element el: span){
						expiredate.add(el.text());
					}

				}


				return myBitmap;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);

			if(!expiredate.isEmpty()) {
				Toolbar toolbarbottom = (Toolbar) findViewById(R.id.toolbar_bottom);
				setSupportActionBar(toolbarbottom);
				getSupportActionBar().setTitle(expiredate.get(0));
			}

			mViewPager.setAdapter(new SamplePagerAdapter(result,arrBitMap));
			System.out.println("down" + arrBitMap);
		}

	}

	static class SamplePagerAdapter extends PagerAdapter {


		private Bitmap bitmap;
		private ArrayList<Bitmap> sarrBitMap = new ArrayList<>();

		public SamplePagerAdapter(Bitmap bitmap, ArrayList<Bitmap> sarrBitMap) {
			this.bitmap = bitmap;
			this.sarrBitMap =sarrBitMap;
		}

		@Override
		public int getCount() {
			return (null != sarrBitMap ? sarrBitMap.size() : 0);
		}

		@Override
		public View instantiateItem(ViewGroup container, int i) {
			PhotoView photoView = new PhotoView(container.getContext());
			//photoView.setImageResource(sDrawables[position]);
			if(!sarrBitMap.isEmpty()) {
				photoView.setImageBitmap(sarrBitMap.get(i));
				// Now just add PhotoView to ViewPager and return it
			}
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
}
