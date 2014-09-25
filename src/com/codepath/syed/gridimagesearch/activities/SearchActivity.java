package com.codepath.syed.gridimagesearch.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.codepath.syed.gridimagesearch.adapters.EndlessScrollListener;
import com.codepath.syed.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.syed.gridimagesearch.models.ImageResult;
import com.codepath.syed.gridimagesearch.models.ImageViewData;
import com.codepath.syed.gridimagesearch.models.Settings;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends FragmentActivity implements OnDataChangeEventListener{

	private EditText etQuery;
	private StaggeredGridView gvResults;
	private ArrayList<ImageResult> imageResults;
	private ImageResultsAdapter aImageResults;
	private String userQueryText;
	private String baseQuery = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&as_filetype=%s&imgtype=%s&imgsz=%s&h1=%s&imgcolor=%s&as_sitesearch=%s&q=";
	private String searchQuery;
	private int REQUEST_CODE = 200;
	
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
        
        // read setting from shared place.
        readUpdatedSettings();
        
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
        
     // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	String query = intent.getStringExtra(SearchManager.QUERY);
          	//buildQueryWithOptions();
          	//("INFO: OnCreate: ", searchQuery+query+"&start=1");
  			//fetchMoreResults(searchQuery+query+"&start=1");
          	searchImages(query);
        }
        
    }
    
    private void showAdvanceOptionsDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AdvanceOptionsDialog optionsDialog = AdvanceOptionsDialog.newInstance("Advance Filter Options");
        optionsDialog.show(fm, "advance_filter_options");

        if(optionsDialog.isChanged() == true){
        	readUpdatedSettings();
        }
    }
    
    // Network handling helpers
    private boolean isDeviceConnected(){
    	if(isNetworkAvailable() == Boolean.FALSE){
        	getActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));
			getActionBar().setTitle(R.string.network_error);
			Toast.makeText(getApplicationContext(), "Network Error.", Toast.LENGTH_SHORT).show();
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
    
//    private Boolean isOnline() { // casues hang.
//        try {
//            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
//            int returnVal = p1.waitFor();
//            boolean reachable = (returnVal==0);
//            return reachable;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return false;
//    }
    
    private void setActionBar(){
    	getActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
		getActionBar().setTitle(R.string.app_name);
    }
    
 // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
      // This method probably sends out a network request and appends new data items to your adapter. 
      // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
      // Deserialize API response and then construct new objects to append to the adapter
    	
    	String query = searchQuery + userQueryText + "&start=" + offset*8;
    	fetchMoreResults(query);
    }
    
    private void setupViews(){
    	gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
    }
    
    // called when button will be pressed
    public void searchImages(String queryString){
    	//showEditDialog();
    	imageResults.clear(); // clear it... in case of new search
    	aImageResults.clear(); // clearing out the adapter in cae of new search.
    	userQueryText = queryString;//etQuery.getText().toString().trim();
    	//if(!userQueryText.isEmpty()){
    		buildQueryWithOptions();
    		fetchMoreResults(searchQuery+userQueryText+"&start=1");
    		
    		Log.i("INFO: ", searchQuery+userQueryText+"&start=1");
    	//}
    }
    
    //---------------------- action selection handlers
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.search_activity_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
            	Log.i("INFO: query....", query);
            	// check network availability 
        		if(!isDeviceConnected())
        			return false;

        		setActionBar(); // sets actionbar to normal. Currently, we don't have any way to come back to normal.
                searchImages(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
    

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("INFO:", "data update received");
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
    			readUpdatedSettings(); 
    			Log.i("INFO:", "data update received");
    	}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    // action with ID action_refresh was selected
	    case R.id.action_refresh:
	    	imageResults.clear(); // clear it... in case of new search
	    	aImageResults.clear(); // clearing out the adapter in cae of new search
	    	buildQueryWithOptions();
	    	fetchMoreResults(searchQuery+userQueryText+"&start=1");
	      break;
	    // action with ID action_settings was selected
	    case R.id.action_settings:
	    	//Intent i = new Intent(this, SettingsActivity.class);
	    	//startActivityForResult(i, REQUEST_CODE); 
	    	showAdvanceOptionsDialog();
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
		if(!isDeviceConnected())
			return;
		
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
    				if(response==null)
    					return;
					JSONArray imageResultJson = response.getJSONObject("responseData").getJSONArray("results");
					// when you make changes to the adapter. it does modifies the underlying data.
					aImageResults.addAll(ImageResult.fromJSONArray(imageResultJson)); // we can directly add the data in the adapter.
					//ImageResult.parseCursor(response.getJSONObject("responseData").getJSONObject("cursor"));
					// aImageResults.notifyDataSetChanged(); - convensional way
    				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			//Log.i("Info: ", imageResults.toString());
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
			//("INFO: ", preferences.getString("domain", ""));
			//Log.i("INFO: ", preferences.getString("ImageType", ""));
		} else {
			Log.e("ERORR:", "preferences object is NULL");
		}
	}

	@Override
	public void dataChangeEvent(String s) {
		Log.i("INFO:", "data update received................");
		readUpdatedSettings(); 
	}
}

