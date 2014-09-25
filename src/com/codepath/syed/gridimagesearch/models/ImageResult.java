package com.codepath.syed.gridimagesearch.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

// our model.
public class ImageResult {
	public String fullUrl;
	public String thumbUrl;
	public String title;
	public int height;
	public int width;
	
	//public static Cursor cursor; // should have only once instance of this clss
	
	public static class Cursor{ // all static members.
		public static long resultCount;
		public static long currentPageIndex;
		public static String moreResultsUrl;
		public static double searchResultTimeDuration;
	}
	
	public ImageResult(JSONObject json){
		try{
			this.fullUrl = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
			this.title = json.getString("title");
			this.width = json.getInt("width");
			this.height = json.getInt("height");
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	// NOTE: Guide. Look at Week 3 - converting JSON to Model pattern.
	// parses an individual item and array of JSON items.
	// Takes an array of JSON images and return arraylist of image results.
	public static ArrayList<ImageResult> fromJSONArray(JSONArray array){
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		
		for(int i=0; i<array.length(); i++){
			try{
				Log.i("INFO:===========", array.toString());
				results.add(new ImageResult(array.getJSONObject(i)));
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		return results;
	}
	
	public static void parseCursor(JSONObject jsonCursor){
		try{
			Cursor.moreResultsUrl = jsonCursor.getString("moreResultsUrl");
			Cursor.currentPageIndex = jsonCursor.getInt("currentPageIndex");
			Cursor.resultCount = jsonCursor.getInt("resultCount");
			Cursor.searchResultTimeDuration = jsonCursor.getDouble("searchResultTime");
		} catch(JSONException e){
			e.printStackTrace();
		}
	}
}
