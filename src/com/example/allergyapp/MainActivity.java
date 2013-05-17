package com.example.allergyapp;
// Remember to delete debug code
import java.io.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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


	//switches to barcode scanner if internet connection is available
    public void scanBarcode(View v) {
    	if(isNetworkAvailable()){
    		IntentIntegrator.initiateScan(this);
    	}else{
    		//no internet so display dialoguebox isntead
    		noInternet();
    	}
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
    
    //sends user to enter upc activity if internet connection is available
    public void enterUPC(View view) {
    	if(isNetworkAvailable()){
    		Intent intent = new Intent(this, EnterUPC.class);
    		startActivity(intent);
	    }else{
	    	//no internet so display dialoguebox isntead
			noInternet();
		}
    }
    
    //check to see if there is internet connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    //method to display dialogue box for when no internet connection is found
    private void noInternet(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
		.setMessage("Allergy App needs an internet connection to work")
		.setCancelable(false)
		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity
				dialog.cancel();
			}
		  });
		AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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
