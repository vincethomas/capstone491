package com.example.allergyapp;

import java.io.*;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

public class Profile extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.activity_profile);
		getUserData();
		// Show the Up button in the action bar.
		setupActionBar();
		
	}
	
	//must be public for button call
	//take in view
	public void saveUserData(View v) {
		String filename = "ALLERGYAPPDATA";
		String filenameCheckboxes = "ALLERGIES";
		
		
		//ingredients string (add all potential ingredients to string for writing to file)
		String allergicIngredients = "";
		ArrayList<String> allergies = new ArrayList<String>();
		
		//go through each checkbox and see which are checked
		CheckBox glutencb = (CheckBox)findViewById(R.id.glutencheckbox);
		if(glutencb.isChecked()){
			allergicIngredients += getString(R.string.gluten);
			allergies.add("glutencheckbox");
		}
		CheckBox wheatcb = (CheckBox)findViewById(R.id.wheatcheckbox);
		if(wheatcb.isChecked()){
			allergicIngredients += getString(R.string.wheat);
			allergies.add("wheatcheckbox");
		}
		CheckBox nutscb = (CheckBox)findViewById(R.id.nutcheckbox);
		if(nutscb.isChecked()){
			allergicIngredients += getString(R.string.nuts);
			allergies.add("nutcheckbox");
		}
		CheckBox dairycb = (CheckBox)findViewById(R.id.dairycheckbox);
		if(dairycb.isChecked()){
			allergicIngredients += getString(R.string.dairy);
			allergies.add("dairycheckbox");
		}
		CheckBox shellfishcb = (CheckBox)findViewById(R.id.shellfishcheckbox);
		if(shellfishcb.isChecked()){
			allergicIngredients += getString(R.string.shellfish);
			allergies.add("shellfishcheckbox");
		}
		
		//convert allergies arraylist to string to be saved
		String allergiesString = null;
		if(allergies.size() > 0){
			allergiesString = allergies.get(0);
			for(int i = 1; i < allergies.size(); i ++){
				allergiesString += "," + allergies.get(i);
			}
		}
		
		
		FileOutputStream outputStream;
		try {
		  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
		  outputStream.write(allergicIngredients.getBytes());
		  outputStream.close();
		  alert();
		  if(allergiesString != null){
			  //if a checkbox was checked save the string
			  FileOutputStream os = openFileOutput(filenameCheckboxes, Context.MODE_PRIVATE);
			  os.write(allergiesString.getBytes());
			  os.close();
		  }
		  
		} catch (Exception e) {
		  e.printStackTrace();
		}
		
	}
	
	private void alert(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
		.setMessage("Allergy Profile Saved.")
		.setCancelable(false)
		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity
				Profile.this.finish();
			}
		  });
		AlertDialog alert = alertDialogBuilder.create();
        alert.show();
	}
	
	public void cancel(View v){
		this.finish();
	}
	
	private void getUserData() {
		
		String filename = "ALLERGIES";
		//get allergies from file and set checkboxes
		try {
			FileInputStream input = openFileInput(filename);
			
			String allergString = convertStreamToString(input);
			
			Log.d("ALLERGY APP", "getUserData: " + allergString);
			
			//turn allergies into array
			String[] allergies = allergString.split(",");
			for(String a : allergies){
				
				Log.d("ALLERGY APP", a);
				int id = getResources().getIdentifier(a.trim(), "id", this.getPackageName());
				Log.d("ALLERGY APP", ""+id);
				//Log.d("ALLERGY APP", "dariy id = " + R.id.dairycheckbox);
				CheckBox cb = (CheckBox) findViewById(id);
				
				//Log.d("ALLERGY APP", a + ": " + cb.isChecked());
				//cb.getText();
				
				cb.setChecked(true);
			}
			
		} catch (Exception e) {
				Log.d("ALLERGY APP", "Exception: " + e);
				e.printStackTrace();
		}
	}
	
	private String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line).append("\n");
	    }
	    return sb.toString();
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
		getMenuInflater().inflate(R.menu.profile, menu);
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

}
