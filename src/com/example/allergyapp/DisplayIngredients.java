package com.example.allergyapp;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
		 
	    //create the query and execute the asyncronous retrieval task
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
	

	
	//asyncronous task that runs while looking up product info
	protected class FactualRetrievalTask extends AsyncTask<Query, Integer, List<ReadResponse>> {
		ProgressDialog mDialog;

		//sets up loading icon so user understands what is going on
		//loading dialogue does prevent user input
		@Override
		protected void onPreExecute(){
			mDialog = new ProgressDialog(DisplayIngredients.this);
			mDialog.setMessage("Loading...");
	        mDialog.setCancelable(false);
	        mDialog.show();
		}
		
		//this is the method that actually sends the query
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

		//this method runs when results come back from factual
		//this updates the text in the display ingredients activity
		@Override
		protected void onPostExecute(List<ReadResponse> responses) {
			boolean foundProduct = false;
			//eventually may change these from for loops to only reference first result, debating this though so leave for now
			for (ReadResponse response : responses) {
				for (Map<String, Object> product : response.getData()) {
					
				foundProduct=true;
				
				//product information vars
				String brand = "";
				String productname = "";
				JSONArray ingredients = new JSONArray();
				
				//if product is found
				foundProduct=true;
				
				//string for product items
				String items = "";	
				
				
				//get the product name, brand, and ingredients
				if(product.get("brand") != null)
					brand = (String) product.get("brand");
				
				if( product.get("product_name") != null)
					productname = (String) product.get("product_name");
				
				if(product.get("ingredients")!= null)
					ingredients = (JSONArray) product.get("ingredients");
				
				
				//Get the text view and set it to the brand and product name
				TextView productText = (TextView) findViewById(R.id.resultText);
				productText.setText(brand + " " + productname);
				 
				 
				 //run through the list of ingredients and add each on to a string	 
				 for(int i = 0; i < ingredients.length(); i++){
					 //I had to add a try catch loop to silence an error, yay
					 try {
						 items = items + ingredients.getString(i) + ",";
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 Log.d("ALLERGY APP", brand + " " + productname);
				 Log.d("ALLERGY APP", items);
				 //get user's allergy ingredients
				 String userAllergies = getUserData();
				 
				 //returns results equal in current ingredients and user's allergies
				 String fr = getResults(items, userAllergies);
				 String flaggedResults = "";
				 String[] flaggedResultsArray = fr.split(",");
				 for (int i = 0; i < flaggedResultsArray.length; i++){
					 if (!(flaggedResults.contains(flaggedResultsArray[i]))){
						 flaggedResults = flaggedResults + flaggedResultsArray[i] + ", ";
					 }
				 }
				 
				 //gets item names for printing
				 String outputItems = "";
				 String flaggedItems = "";
					 for(int i = 0; i < ingredients.length(); i++){
						 //I had to add a try catch loop to silence an error, yay
						 try {
							 String ingredient = ingredients.getString(i);
							 if (flaggedResults.contains(ingredient.toLowerCase())){
								 flaggedItems = flaggedItems + ingredient.replaceAll("[^a-zA-Z0-9 ]+","") + ", ";
							 } else {
								 outputItems = outputItems + ingredient.replaceAll("[^a-zA-Z0-9 ]+","") + ", ";
							 }
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 }
					 //trim off trailing comma and space
					 if (outputItems.length() > 2){
						 outputItems = outputItems.substring(0, outputItems.length()-2);
					 }
					 if (flaggedItems.length() > 2){
						 flaggedItems = flaggedItems.substring(0, flaggedItems.length()-2);
					 }
				 
				 //set the text field to ingredients
				 TextView flaggedIngredients = (TextView) findViewById(R.id.flaggedList);
				 flaggedIngredients.setText(flaggedItems);
				 TextView productIngredients = (TextView) findViewById(R.id.ingredientList);
				 productIngredients.setText(outputItems);
				 
				 //get the product image url
				JSONArray images = (JSONArray) product.get("image_urls");
				String imageUrl = null;
				if(images !=null){
					 
					try {
						 imageUrl = images.getString(0);
						 ImageView piv = (ImageView) findViewById(R.id.productimage);
						 new DownloadImageTask(piv, mDialog).execute(imageUrl);
						 //mDialog.dismiss();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d("ALLERGY APP",imageUrl);
					mDialog.dismiss();
				}else{
					Log.d("ALLERGY APP","no images");
					mDialog.dismiss();
				}
				
				//break for now as only using first result
				 break;
				} 
				break;
			}
			//if no product was found dismiss the loading screen
			if(!foundProduct){
				mDialog.dismiss();
				//popup alert dialogue to take user back to main page
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayIngredients.this);
				alertDialogBuilder
				.setMessage("Sorry, the product was not found, returning to main page")
				.setCancelable(false)
				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						DisplayIngredients.this.finish();
					}
				  });
				AlertDialog alert = alertDialogBuilder.create();
                alert.show();
				
				//replace loading text
				TextView productText = (TextView) findViewById(R.id.resultText);
				productText.setText("Product Not Found");
			}
		}//end of on post execute
		
		//method loads the user data from the saved file
		//TODO check to make sure file exists
		private String getUserData() {
			String filename = "ALLERGYAPPDATA";
			try {
				FileInputStream input = openFileInput(filename);
				String ingredients = convertStreamToString(input);
				return ingredients;
			} catch (Exception e) {
				Log.d("ALLERGY APP", "Exception: " + e);
				e.printStackTrace();
				return null;
			}
		}
		
		//helper method for getting user data returns file data as string
		private String convertStreamToString(InputStream is) throws Exception {
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    StringBuilder sb = new StringBuilder();
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		      sb.append(line).append("\n");
		    }
		    return sb.toString();
		}
		
		//method for checking for igredients that match potential allergens and returning them as as string
		public String getResults (String ingredients, String allergies) {
			String flaggedIngredients = "";
			if(allergies != null && allergies != "" && ingredients != null && ingredients != ""){
				String[] ing = ingredients.split(",");
				String[] all = allergies.split(",");
				for (String a : all){
					for (String i : ing){
						i = i.toLowerCase().trim();
						a = a.toLowerCase().trim();
						if (a.equals(i) || i.contains(a)){
							flaggedIngredients = flaggedIngredients + i + ",";
						}
					}
				}
			}
			return flaggedIngredients;
		}
		
		//class to fetch product image asyncronously
		private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		    ImageView bmImage;
		    ProgressDialog mDialog;

		    public DownloadImageTask(ImageView bmImage, ProgressDialog mDialog) {
		        this.bmImage = bmImage;
		        this.mDialog = mDialog;
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
		        mDialog.dismiss();
		    }
		}

	}//end of FactualRetrieval task class

}
