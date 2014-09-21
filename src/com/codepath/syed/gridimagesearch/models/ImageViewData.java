package com.codepath.syed.gridimagesearch.models;

import java.io.Serializable;

public class ImageViewData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String url;
	public String title;
	
	public ImageViewData(String url, String title) {
		this.url = url;
		this.title = title;
	}
}
