package com.codepath.syed.gridimagesearch.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;

import com.codepath.syed.gridimagesearch.adapters.EndlessScrollListener;
import com.codepath.syed.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.syed.gridimagesearch.models.ImageResult;
import com.codepath.syed.gridimagesearch.models.ImageViewData;
import com.codepath.syed.gridimagesearch.models.Settings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {

	private EditText etQuery;
	private GridView gvResults;
	private ArrayList<ImageResult> imageResults;
	private ImageResultsAdapter aImageResults;
	private String userQueryText;
	private String baseQuery = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&as_filetype=%s&imgtype=%s&imgsz=%s&h1=%s&imgcolor=%s&as_sitesearch=%s&q=";
	private String searchQuery;
//	private static final String FILE_TYPE = "as_filetype"; 
//	private static final String IMAGE_TYPE = "imgtype";
//	private static final String IMAGE_SIZE = "imgsz";
//	private static final String LANGUAGE = "h1";
//	private static final String IMAGE_COLOR = "imgcolor";
//	private static final String SITE_SEARCH = "as_sitesearch";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        
        // create the datasource
        imageResults= new ArrayList<ImageResult>();   
        
        // create the adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);
        
        // link the adapter
        gvResults.setAdapter(aImageResults);
        
        // Attach the listener to AdapterView onCreate
        gvResults.setOnScrollListener(new EndlessScrollListener(){

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				 // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
	        customLoadMoreDataFromApi(page); 
                // or customLoadMoreDataFromApi(totalItemsCount); 
			}
        	
        });
        
        //OnItemClickListener
        gvResults.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
                Intent intent = new Intent(SearchActivity.this, ImageViewActivity.class);
                
                ImageResult imageResult = (ImageResult)aImageResults.getItem(position);
                ImageViewData imageData = new ImageViewData(imageResult.fullUrl, imageResult.title);
                intent.putExtra("image", imageData);
                startActivity(intent);
            }
        });
    }
    
    // Network handling helpers
    private boolean isDeviceConnected(){
    	if(isNetworkAvailable() == Boolean.FALSE || (isOnline() == Boolean.FALSE)){
        	getActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));

			getActionBar().setTitle(R.string.network_error);
        	return false; 
        }
    	
    	return true;
    }
    
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    
    private Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
    private void setActionBar(){
    	getActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
		getActionBar().setTitle(R.string.app_name);
    }
    
 // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
      // This method probably sends out a network request and appends new data items to your adapter. 
      // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
      // Deserialize API response and then construct new objects to append to the adapter
    	
    	String query = searchQuery;
    	fetchMoreResults(query+userQueryText+"&start=" + offset*8);
    }
    
    private void setupViews(){
    	etQuery = (EditText) findViewById(R.id.etQuery);
    	etQuery.setText("cat");
    	gvResults = (GridView) findViewById(R.id.gvResults);
    }
    
    // called when button will be pressed
    public void OnImageSearch(View v){
    	readUpdatedSettings();
    	imageResults.clear(); // clear it... in case of new search
    	aImageResults.clear(); // clearing out the adapter in cae of new search.
    	userQueryText = etQuery.getText().toString().trim();
    	if(!userQueryText.isEmpty()){
    		buildQueryWithOptions();
    		fetchMoreResults(searchQuery+userQueryText+"&start=1");
    	}
    }
    
    //---------------------- action selection handlers
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_activity_menu, menu);
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    // action with ID action_refresh was selected
	    case R.id.action_refresh:
	    	imageResults.clear(); // clear it... in case of new search
	    	aImageResults.clear(); // clearing out the adapter in cae of new search.
	    	fetchMoreResults(searchQuery+userQueryText+"&start=1");
	      break;
	    // action with ID action_settings was selected
	    case R.id.action_settings:
	    	Intent i = new Intent(this, SettingsActivity.class);
	    	startActivityForResult(i, 200); 
	      break;
	    default:
	      break;
	    }

	    return true;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onOptionsMenuClosed(menu);
	}

	public void fetchMoreResults(String query){
    	// check network availability 
		//if(!isDeviceConnected())
		//	return;
		
		setActionBar();
		
    	// create the network client
    	AsyncHttpClient client = new AsyncHttpClient();
    	
    	// trigger the network request
    	client.get(query, new JsonHttpResponseHandler(){
    		@Override
    		public void onFailure(int statusCode, Header[] headers,
    				String responseString, Throwable throwable) {
    			
    			Log.e("ERROR", responseString);
    		}
    		
    		@Override
    		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
    			// TODO Auto-generated method stub
    			
    			//Log.i("Info: ", response.toString());
    			try {
					JSONArray imageResultJson = response.getJSONObject("responseData").getJSONArray("results");
					// when you make changes to the adapter. it does modifies the underlaying data.
					aImageResults.addAll(ImageResult.fromJSONArray(imageResultJson)); // we can directly add the data in the adapter.
					
					//ImageResult.parseCursor(response.getJSONObject("responseData").getJSONObject("cursor"));
					// aImageResults.notifyDataSetChanged(); - convensional way
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			Log.i("Info: ", imageResults.toString());
    		}
    	});
    }
	
	private void buildQueryWithOptions(){
		//"https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&as_filetype=%s&imgtype=%s&imgsz=%s&h1=%s&imgcolor=%s&as_sitesearch=%s";
		searchQuery = String.format(baseQuery, Settings.imageType, Settings.faceType, Settings.size, Settings.language, Settings.color, Settings.domain );
	}
	
	private void readUpdatedSettings(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(MODE_PRIVATE);
		if(preferences != null){
			Settings.domain = preferences.getString("domain", "");
			Settings.color = preferences.getString("color", "");
			Settings.faceType = preferences.getString("faceType", "");
			Settings.imageType = preferences.getString("imageType", "");
			Settings.language = preferences.getString("language", "");
			Settings.size = preferences.getString("size", "");
			
			String s = preferences.getString("ImageType", "");
			Log.i("INFO: ", preferences.getString("domain", ""));
			Log.i("INFO: ", preferences.getString("ImageType", ""));
		} else {
			Log.e("ERORR:", "preferences object is NULL");
		}
	}
}
