package com.example.allergyapp;

import java.util.List;
import java.util.Map;

import com.factual.driver.Factual;
import com.factual.driver.Query;
import com.factual.driver.ReadResponse;
import com.google.common.collect.Lists;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
//import com.factual.driver.Factual;
//import com.factual.driver.ReadResponse;
//import android.widget.Button;

public class MainActivity extends Activity {
	
	private final String OAUTH_KEY = "oQGYt52dXjhcfGJpq3QxufgRtNcNps0Kfa3DrpSk";
	private final String OAUTH_SECRET = "4beHgBGuELmqEed765JVNGXMnOSZI6DdfFRRaSn1";
	private Factual factual = new Factual(OAUTH_KEY,OAUTH_SECRET);
	private TextView resultText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	

	//View.OnClickListener scanDatShit = new View.OnClickListener(){
    public void scanBarcode(View v) {
    	//doesn't work, needs "Activity"
    	
    	IntentIntegrator.initiateScan(this);
    }
   // };
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("MyApp", "result method");
	    switch(requestCode) {
	        case IntentIntegrator.REQUEST_CODE: {
	            if (resultCode != RESULT_CANCELED) {
	                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
	                if (scanResult != null) {
	                    String upc = scanResult.getContents();
	                    //
	                    //put whatever you want to do with the code here
	                    System.out.println("Success: " + upc);
	                    Log.d("MyApp", "Success: " + upc);
	                    
	                    resultText = (TextView) findViewById(R.id.resultText);
	                    FactualRetrievalTask task = new FactualRetrievalTask();
	                    Query query = new Query().search(upc);
	                    task.execute(query);
	                    
	                }
	            }
	            break;
	        }
	    }
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
//				String address = (String) restaurant.get("address");
//				String phone = (String) restaurant.get("tel");
//				Number distance = (Number) restaurant.get("$distance");
				sb.append(brand);
				sb.append(System.getProperty("line.separator"));
				}  
			}
			resultText.setText(sb.toString());
		}

	} 

}
