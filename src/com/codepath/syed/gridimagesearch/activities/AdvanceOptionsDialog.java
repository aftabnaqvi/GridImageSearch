package com.codepath.syed.gridimagesearch.activities;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class AdvanceOptionsDialog extends DialogFragment implements OnClickListener{

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	private Spinner spinnerSize;
	private Spinner spinnerColor;
	private Spinner spinnerFaceType;
	private Spinner spinnerImageType;
	private Spinner spinnerLanguage;
	private EditText etDomain;
	private boolean bOptionsChanged = false;
	
	OnDataChangeEventListener dataChangeListener;
	public AdvanceOptionsDialog() {
		// Empty constructor required for DialogFragment
	}
	
	public static AdvanceOptionsDialog newInstance(String title) {
		AdvanceOptionsDialog frag = new AdvanceOptionsDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.advance_filter_options, container);
		
		
		getDialog().setTitle("Advance Filter Options");
		
		etDomain = (EditText)view.findViewById(R.id.etDomian);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
		String storedDomain = preferences.getString("domain", "");
		etDomain.setText(storedDomain);
		
		spinnerSize = (Spinner) view.findViewById(R.id.spinnerSize);
		spinnerColor = (Spinner) view.findViewById(R.id.spinnerColor);
		spinnerFaceType = (Spinner) view.findViewById(R.id.spinnerFaceType);
		spinnerImageType = (Spinner) view.findViewById(R.id.spinnerImageType);
		
		Button btnSave = (Button) view.findViewById(R.id.btnSave);
		Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
		btnSave.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
		addItemsInSpinnerSize();
		addItemsInSpinnerColor();
		addItemsInSpinnerFaceType();
		//addItemsInSpinnerLanguage();
		addItemsInSpinnerImageType();
		
		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			dataChangeListener = (OnDataChangeEventListener) activity;
	       } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
	        }
	}

	@Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btnSave:
        	onOkay();
        	dataChangeListener.dataChangeEvent("dummy");
        	dismiss();
            break;
        case R.id.btnCancel:
        	dismiss();
        	break;
        }
    }
	
	// add items into addItemsOnSpinnerSize dynamically
	public void addItemsInSpinnerSize() {
		
		List<String> list = new ArrayList<String>();
		list.add("small");
		list.add("medium");
		list.add("large");
		list.add("xlarge");
		list.add("xxlarge");
		list.add("huge");
	
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
			R.layout.spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		
		if(spinnerSize != null){
			spinnerSize.setAdapter(dataAdapter);
			spinnerSize.setSelection(list.indexOf(PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString("size", "")));
			
		}
		else
		{
			Log.i("IFO: ", "spinnerSize is NULL");
		}
	}
	
	// add items into addItemsOnSpinnerColor dynamically
	public void addItemsInSpinnerColor() {
		
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
		list.add("yello");
	
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
				R.layout.spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerColor.setAdapter(dataAdapter);
		
		spinnerColor.setSelection(list.indexOf(PreferenceManager.getDefaultSharedPreferences(getDialog().getContext()).getString("color", "")));
	}	
	
	// add items into  addItemsInSpinnerFaceType dynamically
	public void addItemsInSpinnerFaceType() {
		
		List<String> list = new ArrayList<String>();
		list.add("face");
		list.add("photo");
		list.add("clipart");
		list.add("lineart");
	
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
				R.layout.spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerFaceType.setAdapter(dataAdapter);
		
		spinnerFaceType.setSelection(list.indexOf(PreferenceManager.getDefaultSharedPreferences(getDialog().getContext()).getString("faceType", "")));
	}

	// add items into  addItemsInSpinnerFaceType dynamically
	public void addItemsInSpinnerImageType() {
		
		List<String> list = new ArrayList<String>();
		list.add("jpg");
		list.add("png");
		list.add("gif");
		list.add("bmp");
	
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
				R.layout.spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerImageType.setAdapter(dataAdapter);
		
		spinnerImageType.setSelection(list.indexOf(PreferenceManager.getDefaultSharedPreferences(getDialog().getContext()).getString("imageType", "")));
	}
	
	private void onSettingsSave(){
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getDialog().getContext()).edit();
		
		editor.putString("size", (String)spinnerSize.getSelectedItem());
		editor.putString("faceType", (String)spinnerFaceType.getSelectedItem());
		editor.putString("imageType", (String)spinnerImageType.getSelectedItem());
		editor.putString("color", (String)spinnerColor.getSelectedItem());
		editor.putString("domain", etDomain.getText().toString());
		editor.commit();
	}

	public void onOkay() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getDialog().getContext()); 
		bOptionsChanged = preferences.getString("imageType", "").compareTo((String) spinnerImageType.getSelectedItem()) != 0 ||
				preferences.getString("faceType", "").compareTo((String) spinnerFaceType.getSelectedItem())  != 0 || 
				preferences.getString("color", "").compareTo((String) spinnerColor.getSelectedItem())  != 0 || 
				preferences.getString("size", "").compareTo((String) spinnerSize.getSelectedItem())  != 0 || 
				preferences.getString("domain", "").compareTo(etDomain.getText().toString()) != 0;
		
		if(bOptionsChanged)
			onSettingsSave();
		
		Log.i("INFO: ", "value of bChanged : " + bOptionsChanged); 
	}
	
	public boolean isChanged(){
		return bOptionsChanged;
	}

	public void setTargetFragment(SearchActivity searchActivity,
			int rEQUEST_CODE) {
		// TODO Auto-generated method stub
		
	}
}
