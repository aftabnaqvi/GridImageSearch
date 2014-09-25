package com.codepath.syed.gridimagesearch.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.syed.gridimagesearch.models.ImageViewData;
import com.squareup.picasso.Picasso;

public class ImageViewActivity extends Activity {
	private TouchImageView ivFullImage; 
	private TextView tvTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        ivFullImage = (TouchImageView) findViewById(R.id.ivFullImage);
        tvTitle = (TextView) findViewById(R.id.tvImageTitle);
        
        Intent intent = getIntent();
        ImageViewData imageViewData = (ImageViewData)intent.getSerializableExtra("image");
        Picasso.with(this).load(imageViewData.url).into(ivFullImage); 
        tvTitle.setText(Html.fromHtml(imageViewData.title));
        getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	// not sure where to put this method.. for now leave it here.
		public DisplayMetrics getDisplayMetrics(){
			DisplayMetrics metrics = new DisplayMetrics();
			WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(metrics);
			return metrics;
		}
	public void shareItem() {
	    // Get access to bitmap image from view
	    ImageView ivImage = (ImageView) findViewById(R.id.ivFullImage);
	    // Get access to the URI for the bitmap
	    Uri bmpUri = getLocalBitmapUri(ivImage);
	    if (bmpUri != null) {
	        // Construct a ShareIntent with link to image
	        Intent shareIntent = new Intent();
	        shareIntent.setAction(Intent.ACTION_SEND);
	        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
	        shareIntent.setType("image/*");
	        // Launch sharing dialog for image
	        startActivity(Intent.createChooser(shareIntent, "Share Image"));	
	    } else {
	        // ...sharing failed, handle error
	    }
	}
	
	// Returns the URI path to the Bitmap displayed in specified ImageView
	private Uri getLocalBitmapUri(ImageView imageView) {
	    // Extract Bitmap from ImageView drawable
	    Drawable drawable = imageView.getDrawable();
	    Bitmap bmp = null;
	    if (drawable instanceof BitmapDrawable){
	       bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
	    } else {
	       return null;
	    }
	    // Store image to default external storage directory
	    Uri bmpUri = null;
	    try {
	        File file =  new File(Environment.getExternalStoragePublicDirectory(  
		        Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
	        file.getParentFile().mkdirs();
	        FileOutputStream out = new FileOutputStream(file);
	        bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
	        out.close();
	        bmpUri = Uri.fromFile(file);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return bmpUri;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.imageview_activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home:
			case R.id.action_close:
				this.finish();
				break;
			case R.id.action_share:
				shareItem();
				break;
		}
		
		return true;
	}
}
