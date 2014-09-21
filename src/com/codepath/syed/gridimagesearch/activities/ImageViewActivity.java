package com.codepath.syed.gridimagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.syed.gridimagesearch.models.ImageViewData;
import com.squareup.picasso.Picasso;

public class ImageViewActivity extends Activity {
	private ImageView ivFullImage; 
	private TextView tvTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        ivFullImage = (ImageView) findViewById(R.id.ivFullImage);
        tvTitle = (TextView) findViewById(R.id.tvImageTitle);
        
        Intent intent = getIntent();
        ImageViewData imageViewData = (ImageViewData)intent.getSerializableExtra("image");
        Picasso.with(this).load(imageViewData.url).into(ivFullImage); 
        tvTitle.setText(Html.fromHtml(imageViewData.title));
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
		}
		
		return true;
	}
}
