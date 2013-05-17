package com.example.allergyapp;
// Remember to delete debug code
import java.io.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.example.allergyapp.MESSAGE";

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
    	IntentIntegrator.initiateScan(this);
    }
    
    //sends user to history activity
    public void history(View view) {
        Intent intent = new Intent(this, History.class);
        startActivity(intent);
    }
    
    //sends user to profile activity
    public void profile(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
    
    //sends user to enter upc activity
    public void enterUPC(View view) {
        Intent intent = new Intent(this, EnterUPC.class);
        startActivity(intent);
    }
	
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
	                    
	                    Intent intent = new Intent(this, DisplayIngredients.class);
	                    
	                    String message = upc;
	                    intent.putExtra(EXTRA_MESSAGE, message);
	                    startActivity(intent);
	                    
	                    //resultText = (TextView) findViewById(R.id.resultText);
	                    //FactualRetrievalTask task = new FactualRetrievalTask();
	                    //Query query = new Query().search(upc);
	                    //task.execute(query);
	                    
	                }
	            }
	            break;
	        }
	    }
	}

}
