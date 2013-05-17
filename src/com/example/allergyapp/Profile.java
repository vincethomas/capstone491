package com.example.allergyapp;

import java.io.*;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

public class Profile extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile);
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	//must be public for button call
	//take in view
	public void saveUserData(View v) {
		String filename = "ALLERGYAPPDATA";
		String string = "Hello world!";
		
		//ingredients string (add all potential ingredients to string for writing to file)
		String allergicIngredients = "";
		
		CheckBox glutencb = (CheckBox)findViewById(R.id.glutencheckbox);
		if(glutencb.isChecked()){
			allergicIngredients += getString(R.string.gluten);	
		}
		CheckBox wheatcb = (CheckBox)findViewById(R.id.wheatcheckbox);
		if(wheatcb.isChecked()){
			allergicIngredients += getString(R.string.wheat);	
		}
		CheckBox nutscb = (CheckBox)findViewById(R.id.nutcheckbox);
		if(wheatcb.isChecked()){
			allergicIngredients += getString(R.string.nuts);	
		}
		CheckBox dairycb = (CheckBox)findViewById(R.id.dairycheckbox);
		if(wheatcb.isChecked()){
			allergicIngredients += getString(R.string.dairy);
		}
		CheckBox shellfishcb = (CheckBox)findViewById(R.id.shellfishcheckbox);
		if(shellfishcb.isChecked()){
			allergicIngredients += getString(R.string.shellfish);
		}
		
		FileOutputStream outputStream;
		try {
		  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
		  outputStream.write(allergicIngredients.getBytes());
		  outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
		getUserData();
	}
	
	private void getUserData() {
		String filename = "ALLERGYAPPDATA";
		try {
			FileInputStream input = openFileInput(filename);
			String debuger = convertStreamToString(input);
			Log.d("ALLERGY APP", "getUserData: " + debuger);
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
