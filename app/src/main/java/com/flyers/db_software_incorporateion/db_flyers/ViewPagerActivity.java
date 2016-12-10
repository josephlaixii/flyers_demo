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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uk.co.senab.photoview.PhotoView;

public class ViewPagerActivity extends AppCompatActivity {
	private final String KEY_RECYCLER_STATE = "recycler_state";
	private static Bundle mBundleRecyclerViewState;
	private static Bundle onCreatesavedInstanceState;
	private ViewPager mViewPager;
	private ArrayList<Bitmap> arrBitMap = new ArrayList<>();
	private ArrayList<String> gurlUpdate = new ArrayList<>();
	private ArrayList<String> gurlUpdate2 = new ArrayList<>();
	private ArrayList<String> expiredate = new ArrayList<>();
	private String title,src3;
	private int i;
	private Bitmap myBitmap;
	public static Toolbar toolbar;
	OkHttpClient client,client2;
	Request request,request2;
	private ImageLoadTask mTask;
	Response response2,response3;
	Button button,button2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_listof_stores);
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		button = (Button) findViewById(R.id.button);
		button2 = (Button) findViewById(R.id.button2);

		onCreatesavedInstanceState = savedInstanceState;


		 toolbar = (Toolbar) findViewById(R.id.toolbar);
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
			mTask = (ImageLoadTask) new ImageLoadTask(gurlUpdate.get(i)).execute();
//			System.out.println("new ImageLoadTask(gurlUpdate.get(i)).execute()22" + gurlUpdate.get(i));
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
	protected void onDestroy() {
		super.onDestroy();
		mTask.cancel(true);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mTask.cancel(true);
	}

	@Override

	protected void onRestart() {
		super.onRestart();
		mTask.cancel(true);
		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this,
				MainActivity.class));

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		mTask.cancel(true);
		myBitmap.recycle();
		arrBitMap.clear();
		gurlUpdate2.clear();
		expiredate.clear();
		deleteCache(ViewPagerActivity.this);
		finish();
		System.out.println("pressedback button");
	}


	public static void deleteCache(Context context) {
		try {
			File dir = context.getCacheDir();
			deleteDir(dir);
		} catch (Exception e) {}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
			return dir.delete();
		} else if(dir!= null && dir.isFile()) {
			return dir.delete();
		} else {
			return false;
		}
	}

	public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

		private String url;

		private String src;

		public ImageLoadTask(String url) {
			this.url = url;

		}

		public void displayExceptionMessage(String msg)
		{
			Toast.makeText(ViewPagerActivity.this, msg, Toast.LENGTH_SHORT).show();
		}



		@Override
		protected Bitmap doInBackground(Void... params) {
			try {

				if(!url.equals("")) {
					client = new OkHttpClient();
					request = new Request.Builder()
							.url(url)
							.build();

					response2 = client.newCall(request).execute();
				}

				if(response2.code() == 200) {

					Document document = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate")
							.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
							.maxBodySize(0)
							.method(org.jsoup.Connection.Method.GET)
							.referrer("http://www.google.com")
							.timeout(6000)
							.get();


					Elements div = document.select("div.flyer-card");
					Elements flyercont = div.select("div.flyer-content");
					Elements flyerstore = flyercont.select("div.flyer-store");
					Elements flyerstoreinner = flyerstore.select("div.flyer-store-inner");
					Elements span = flyerstoreinner.select("span");


				for (Element el: span){
					expiredate.add(el.text());
				}


					Document document2 = Jsoup.connect(url+"/all").header("Accept-Encoding", "gzip, deflate")
							.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
							.maxBodySize(0)
							.method(org.jsoup.Connection.Method.GET)
							.referrer("http://www.google.com")
							.timeout(6000)
							.get();



					Elements img = document2.getElementsByTag("img");


					for (Element el : img) {
						src = el.absUrl("src");



						URL url = new URL(src);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setDoInput(true);
						connection.connect();
						connection.setConnectTimeout(20000);
						connection.setReadTimeout(20000);
						InputStream input = new BufferedInputStream(connection.getInputStream());

						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inJustDecodeBounds = false;
						options.inSampleSize = 1;
						options.inPreferredConfig = Bitmap.Config.RGB_565;
						options.inScaled=false;
						options.inTargetDensity = 1;
						options.inDensity = 1;
						options.inInputShareable=true;
						options.inDither=false;
						options.inTempStorage=new byte[64 * 1024];
//						int heightRatio = (int)Math.ceil(options.outHeight/(float)2000);
//						int widthRatio = (int)Math.ceil(options.outWidth/(float)2000);
//
//						if (heightRatio > 1 || widthRatio > 1)
//						{
//							if (heightRatio > widthRatio)
//							{
//								options.inSampleSize = heightRatio;
//							} else {
//								options.inSampleSize = widthRatio;
//							}
//						}
						deleteCache(ViewPagerActivity.this);
						myBitmap = BitmapFactory.decodeStream(input,null,options);

						arrBitMap.add(myBitmap);
						input.close();


					}


					int i2[] = {0, 1, arrBitMap.size() - 1};
					int ilen = i2.length;
					for (int j = 0; j < ilen; j++) {
						arrBitMap.remove(i2[j] - j);
					}


				}




				return myBitmap;

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error" + e.toString());

				mTask.cancel(true);
				myBitmap.recycle();
				arrBitMap.clear();
				gurlUpdate2.clear();
				expiredate.clear();

				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						final Toast tag = Toast.makeText(ViewPagerActivity.this, "Internet Connection Lost", Toast.LENGTH_SHORT);
						tag.show();
						new CountDownTimer(3000, 1000)
						{

							public void onTick(long millisUntilFinished) {tag.show();}
							public void onFinish() {tag.show();}

						}.start();

					}
				});
			}


			return null;


		}



		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			mViewPager.setAdapter(new SamplePagerAdapter(result,arrBitMap));
			mBundleRecyclerViewState = new Bundle();
			mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, onCreatesavedInstanceState);
			if(!expiredate.isEmpty()) {
				Toolbar toolbarbottom = (Toolbar) findViewById(R.id.toolbar_bottom);
				setSupportActionBar(toolbarbottom);
				getSupportActionBar().setTitle(expiredate.get(0));
				setSupportActionBar(toolbar);

			}


			System.out.println("down" + arrBitMap);
		}


	}



	 class SamplePagerAdapter extends PagerAdapter {


		private Bitmap bitmap;
		private ArrayList<Bitmap> sarrBitMap = new ArrayList<>();
		private Toolbar toolbar;
		 private int ipos=0;

		public SamplePagerAdapter(Bitmap bitmap, ArrayList<Bitmap> sarrBitMap) {
			this.bitmap = bitmap;
			this.sarrBitMap =sarrBitMap;

		}

		@Override
		public int getCount() {
			return (null != sarrBitMap ? sarrBitMap.size() : 0);
		}

		 private int getItem(int i) {
			 return mViewPager.getCurrentItem() + i;
		 }

		@Override
		public View instantiateItem(final ViewGroup container, final int i) {
			final PhotoView photoView = new PhotoView(container.getContext());
			toolbar = ViewPagerActivity.toolbar;

			if(!sarrBitMap.isEmpty()) {
				photoView.setImageBitmap(sarrBitMap.get(i));
				// Now just add PhotoView to ViewPager and return it
			}



			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(getItem(+1), true);
				}
			});

			button2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(getItem(-1), true);


				}
			});

			getSupportActionBar().setSubtitle((mViewPager.getCurrentItem()+1) + " of " + (sarrBitMap.size()));
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int i, Object object) {
			getSupportActionBar().setSubtitle(mViewPager.getCurrentItem()+1 + " of " + (sarrBitMap.size()));
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
}
