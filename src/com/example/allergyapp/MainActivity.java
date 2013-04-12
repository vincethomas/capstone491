package com.example.allergyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.factual.driver.Factual;
//import android.widget.Button;

public class MainActivity extends Activity {
	
	private Factual factual = new Factual("","");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Button scanButton = (Button) findViewById(R.id.scanButton);
		//scanButton.setOnClickListener(scanDatShit);
		//IntentIntegrator.initiateScan(this);
		Log.w("myApp", "no network");
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
	                    
	                    //put whatever you want to do with the code here
	                    System.out.println("Success: " + upc);
	                    Log.d("MyApp", "Success: " + upc);
	                    
	                }
	            }
	            break;
	        }
	    }
	}

}
