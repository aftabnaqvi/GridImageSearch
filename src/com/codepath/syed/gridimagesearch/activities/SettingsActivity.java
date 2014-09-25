package com.codepath.syed.gridimagesearch.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnItemSelectedListener{
	private Spinner spinnerSize;
	private Spinner spinnerColor;
	private Spinner spinnerFaceType;
	private Spinner spinnerImageType;
	private Spinner spinnerLanguage;
	private EditText etDomain;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
 
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
		
		addItemsInSpinnerSize();
		addItemsInSpinnerColor();
		addItemsInSpinnerFaceType();
		addItemsInSpinnerLanguage();
		addItemsInSpinnerImageType();
		
		etDomain = (EditText)findViewById(R.id.etDomian);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String storedDomain = preferences.getString("domain", "");
		etDomain.setText(storedDomain);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	 
	// add items into addItemsOnSpinnerSize dynamically
	public void addItemsInSpinnerSize() {
		spinnerSize = (Spinner) findViewById(R.id.spinnerSize);
		List<String> list = new ArrayList<String>();
		list.add("small");
		list.add("medium");
		list.add("large");
		list.add("xlarge");
		list.add("xxlarge");
		list.add("huge");
	
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinnerSize.setAdapter(dataAdapter);
		spinnerSize.setSelection(list.indexOf(PreferenceManager.getDefaultSharedPreferences(this).getString("size", "")));
	}
	
	// add items into addItemsOnSpinnerColor dynamically
	public void addItemsInSpinnerColor() {
		spinnerColor = (Spinner) findViewById(R.id.spinnerColor);
		List<String> list = new ArrayList<String>();
		list.add("black");
		list.add("blue");
		list.add("brown");
		list.add("gray");
		list.add("green");
		list.add("orange");
		list.add("pink");
		list.add("purple");
		list.add("red");
		list.add("teal");
		list.add("white");
		list.add("yellow");
	
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerColor.setAdapter(dataAdapter);
		
		spinnerColor.setSelection(list.indexOf(PreferenceManager.getDefaultSharedPreferences(this).getString("color", "")));
		
	}	
	
	// add items into  addItemsInSpinnerFaceType dynamically
	public void addItemsInSpinnerFaceType() {
		spinnerFaceType = (Spinner) findViewById(R.id.spinnerFaceType);
		List<String> list = new ArrayList<String>();
		list.add("face");
		list.add("photo");
		list.add("clipart");
		list.add("lineart");
	
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerFaceType.setAdapter(dataAdapter);
		
		spinnerFaceType.setSelection(list.indexOf(PreferenceManager.getDefaultSharedPreferences(this).getString("faceType", "")));
	}
	
	// add items into  addItemsInSpinnerFaceType dynamically
	public void addItemsInSpinnerLanguage() {
		spinnerLanguage = (Spinner) findViewById(R.id.spinnerLanguage);
		List<String> list = new ArrayList<String>();
		list.add("en");
		list.add("ur");
		list.add("fr");
		list.add("jp");
		list.add("sv"); // swedish
	
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLanguage.setAdapter(dataAdapter);
		
		spinnerLanguage.setSelection(list.indexOf(PreferenceManager.getDefaultSharedPreferences(this).getString("language", "")));
	}	

	// add items into  addItemsInSpinnerFaceType dynamically
	public void addItemsInSpinnerImageType() {
		spinnerImageType = (Spinner) findViewById(R.id.spinnerImageType);
		List<String> list = new ArrayList<String>();
		list.add("jpg");
		list.add("png");
		list.add("gif");
		list.add("bmp");
	
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerImageType.setAdapter(dataAdapter);
		
		spinnerImageType.setSelection(list.indexOf(PreferenceManager.getDefaultSharedPreferences(this).getString("imageType", "")));
	}
	
	// -- action bar menu handlers
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings_activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_save:
				onSettingsSave();
				break;
			case android.R.id.home:
	            // app icon in action bar clicked; goto parent activity.
	            this.finish();
	           // NavUtils.navigateUpFromSameTask(this); // it recreates the parent activity. 
	            return true;
			default:
				break;
		}
		
		return true;
	}

	private void onSettingsSave(){
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		
		editor.putString("size", (String)spinnerSize.getSelectedItem());
		editor.putString("faceType", (String)spinnerFaceType.getSelectedItem());
		editor.putString("imageType", (String)spinnerImageType.getSelectedItem());
		editor.putString("language", (String)spinnerLanguage.getSelectedItem());
		editor.putString("color", (String)spinnerColor.getSelectedItem());
		editor.putString("domain", etDomain.getText().toString());
		editor.commit();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this); 
		boolean bChanged = preferences.getString("imageType", "").compareTo((String) spinnerImageType.getSelectedItem()) != 0 ||
				preferences.getString("language", "").compareTo((String) spinnerLanguage.getSelectedItem())  != 0 || 
				preferences.getString("faceType", "").compareTo((String) spinnerFaceType.getSelectedItem())  != 0 || 
				preferences.getString("color", "").compareTo((String) spinnerColor.getSelectedItem())  != 0 || 
				preferences.getString("size", "").compareTo((String) spinnerSize.getSelectedItem())  != 0 || 
				preferences.getString("domain", "").compareTo(etDomain.getText().toString()) != 0;
		
		
		Intent intent = new Intent(this, SearchActivity.class);
		intent.putExtra("changed", bChanged); // just returning the flag.

		if(bChanged)
			onSettingsSave();
		
		Log.i("INFO: ", "value of bChanged : " + bChanged); 
		// Activity finished, return the intent
		setResult(RESULT_OK, intent);
		super.finish();
	}
}
