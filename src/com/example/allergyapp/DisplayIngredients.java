package com.example.allergyapp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.factual.driver.Factual;
import com.factual.driver.Query;
import com.factual.driver.ReadResponse;
import com.google.common.collect.Lists;

public class DisplayIngredients extends Activity {
	
	
	private final String OAUTH_KEY = "oQGYt52dXjhcfGJpq3QxufgRtNcNps0Kfa3DrpSk";
	private final String OAUTH_SECRET = "4beHgBGuELmqEed765JVNGXMnOSZI6DdfFRRaSn1";
	private Factual factual = new Factual(OAUTH_KEY,OAUTH_SECRET);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_ingredients);
		// Show the Up button in the action bar.
		setupActionBar();
		
		
		
		 Intent intent = getIntent();
		 String upc = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		 
		 //TextView myText = (TextView) findViewById(R.id.helloworld);
		 //myText.setText(upc);
		
		/*// Create the text view
	    TextView textView = (TextView) findViewById(R.id.helloworld);
	    textView.setTextSize(40);
	    textView.setText(upc);

	    // Set the text view as the activity layout
	    setContentView(textView);*/
	    
	    //resultText = (TextView) findViewById(R.id.resultText);
        FactualRetrievalTask task = new FactualRetrievalTask();
        Query query = new Query().search(upc);
        task.execute(query);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_ingredients, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected class FactualRetrievalTask extends AsyncTask<Query, Integer, List<ReadResponse>> {
		
		protected ProgressDialog mDialog;
		
		protected void onPreExecute(){
			 mDialog = new ProgressDialog(getApplicationContext());
             mDialog.setMessage("Loading...");
             mDialog.setCancelable(false);
             mDialog.show();
		}
		
		
		@Override
		protected List<ReadResponse> doInBackground(Query... params) {
			List<ReadResponse> results = Lists.newArrayList();
			for (Query q : params) {
				results.add(factual.fetch("products-cpg-nutrition", q));
			}
			return results;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(List<ReadResponse> responses) {
			//eventually may change these from for loops to only reference first result, debating this though so leave for now
			for (ReadResponse response : responses) {
				for (Map<String, Object> product : response.getData()) {
				
				//get the product name and brand
				String brand = (String) product.get("brand");
				String productname = (String) product.get("product_name");
				
				//test to make sure that the brand is being grabbed in case an error occours
				Log.d("ALLERGY APP", brand);
				
				// Get the text view and set it to the brand and product name
				TextView productText = (TextView) findViewById(R.id.resultText);
				productText.setText(brand + " " + productname);
				 
				 //this gets the ingredients for each product (it is given to us in a JSONArray)
				 JSONArray ingredients = (JSONArray) product.get("ingredients");
				 
				 //run through the list of ingredients and add each on to a string
				 //the defualt to string method adds brackets commas and quotations so that isn't really what I want
				 String items = "";
				 if(ingredients.length()!=0){
					 try {
						items =  ingredients.getString(0);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 for(int i = 0; i < ingredients.length(); i++){
					 //I had to add a try catch loop to silence an error, yay
					 try {
						 String ingredient = ingredients.getString(i).toLowerCase();
						 for (String splitWord : ingredient.split(" ")){
							 Log.d("ALLERGY APP", splitWord);
							 items = items + splitWord + ",";
						 }
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 //set the text field to ingredients
				 TextView productIngredients = (TextView) findViewById(R.id.ingredientList);
				 productIngredients.setText(items);
				 
				 //get the product image url
				JSONArray images = (JSONArray) product.get("image_urls");
				String imageUrl = null;
				if(images !=null){
					 
					 Log.d("ALLERGY APP", images.length() + " asdf");
					 try {
						 imageUrl = images.getString(0);
						 ImageView piv = (ImageView) findViewById(R.id.productimage);
						 new DownloadImageTask(piv).execute(imageUrl);
						 mDialog.dismiss();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d("ALLERGY APP",imageUrl);
				}else{
					Log.d("ALLERGY APP","no images");
					mDialog.dismiss();
				}
				
				//use image url to display image
				
				
				
				
				//break for now as only using first result
				 break;
				} 
				break;
			}
			//resultText.setText(sb.toString());
		}//end of on post execute
		
		//class to fetch product image asyncronously
		private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		    ImageView bmImage;

		    public DownloadImageTask(ImageView bmImage) {
		        this.bmImage = bmImage;
		    }

		    protected Bitmap doInBackground(String... urls) {
		        String urldisplay = urls[0];
		        Bitmap mIcon11 = null;
		        try {
		            InputStream in = new java.net.URL(urldisplay).openStream();
		            mIcon11 = BitmapFactory.decodeStream(in);
		        } catch (Exception e) {
		            Log.e("Error", e.getMessage());
		            e.printStackTrace();
		        }
		        return mIcon11;
		    }

		    protected void onPostExecute(Bitmap result) {
		        bmImage.setImageBitmap(result);
		    }
		}

	}//end of FactualRetrieval task class

}
