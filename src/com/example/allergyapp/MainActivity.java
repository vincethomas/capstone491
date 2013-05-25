package com.example.allergyapp;
// Remember to delete debug code

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.example.allergyapp.MESSAGE";
	SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		firstRunPreferences();
		Log.d("ALLERGY APP", "Aplication Started");
		
		setContentView(R.layout.activity_main);
		if(getFirstRun()){
			setRan();
			setupProfile();
		}
		
	}
	
	private void setupProfile(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
		.setMessage("Thank you for trying AllergyApp, Please setup your profile")
		.setCancelable(false)
		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity
				Intent intent = new Intent(MainActivity.this, Profile.class);
		        startActivity(intent);
			}
		  });
		AlertDialog alert = alertDialogBuilder.create();
        alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	//sets up storage for the first ran information
	private void firstRunPreferences(){
		Context mContext = this.getApplicationContext();
		mPrefs = mContext.getSharedPreferences("allergyAppPrefs",0);
	}
	
	//sets if the application has been run yet
	private void setRan(){
		SharedPreferences.Editor edit = mPrefs.edit();
		edit.putBoolean("firstRun", false);
		edit.commit();
	}
	
	//returns true, if this is the first time run
	private boolean getFirstRun(){
		return mPrefs.getBoolean("firstRun", true);
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
    

    
    //sends user to about activity
    public void about(View view) {
        Intent intent = new Intent(this, About.class);
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
	
    //method is run when scanner successfully scans a upc code
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch(requestCode) {
	        case IntentIntegrator.REQUEST_CODE: {
	            if (resultCode != RESULT_CANCELED) {
	                IntentResult scanResult = 
	                IntentIntegrator
	                .parseActivityResult(requestCode, resultCode, data);
	                if (scanResult != null) {
	                	
	                	//get the scanned upc
	                    String upc = scanResult.getContents();
	                    String message = upc;
	                    
	                    //send the upc to the new activity
	                    Intent intent = new Intent(this, DisplayIngredients.class);
	                    intent.putExtra(EXTRA_MESSAGE, message);
	                    startActivity(intent);
	                    
	                }
	            }
	            break;
	        }
	    }
	}

}
