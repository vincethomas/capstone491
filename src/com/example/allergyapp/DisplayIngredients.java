package com.example.allergyapp;

import java.util.List;
import java.util.Map;

import com.factual.driver.Factual;
import com.factual.driver.Query;
import com.factual.driver.ReadResponse;
import com.google.common.collect.Lists;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class DisplayIngredients extends Activity {
	
	
	private final String OAUTH_KEY = "oQGYt52dXjhcfGJpq3QxufgRtNcNps0Kfa3DrpSk";
	private final String OAUTH_SECRET = "4beHgBGuELmqEed765JVNGXMnOSZI6DdfFRRaSn1";
	private Factual factual = new Factual(OAUTH_KEY,OAUTH_SECRET);
	private TextView resultText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_ingredients);
		// Show the Up button in the action bar.
		setupActionBar();
		
		 Intent intent = getIntent();
		 String upc = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		// Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(40);
	    textView.setText(upc);

	    // Set the text view as the activity layout
	    setContentView(textView);
	    
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
			StringBuffer sb = new StringBuffer();
			for (ReadResponse response : responses) {
				for (Map<String, Object> product : response.getData()) {
				String brand = (String) product.get("product_name");
				Log.d("ALLERGY APP", brand);
//				String address = (String) restaurant.get("address");
//				String phone = (String) restaurant.get("tel");
//				Number distance = (Number) restaurant.get("$distance");
				//sb.append(brand);
				//sb.append(System.getProperty("line.separator"));
				}  
			}
			//resultText.setText(sb.toString());
		}

	} 

}
