package com.codepath.syed.gridimagesearch.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.syed.gridimagesearch.activities.R;
import com.codepath.syed.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

	static class ViewHolder{
		SquareImageView ivImage;
		TextView tvTitle;
	}
	
	private static int height; // device height
	private static int width; // device width

	public ImageResultsAdapter(Context context, List<ImageResult> images) {
		super(context, android.R.layout.simple_list_item_1, images);
		// TODO Auto-generated constructor stub
		DisplayMetrics metrics = getDisplayMetrics();
		width = metrics.widthPixels;
		height = metrics.heightPixels;
		
//		if(photo.imageHeight > metrics.heightPixels/2){
//			photoHeight = metrics.heightPixels/2; 
//		} else {
//			photoHeight = photo.imageHeight;
//		}
//		
//		// We need to adjust the height if the width of the bitmap is
//		// smaller than the view width, otherwise the image will be boxed.
//		final double viewWidthToBitmapWidthRatio = (double)photo.imageWidth / (double)photo.imageHeight;
//		imagePhoto.getLayoutParams().height = photoHeight*(int)viewWidthToBitmapWidthRatio;
	
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// Step-1 - get the data item.
		ImageResult result = getItem(position);
				
		ViewHolder vh = null;
		if(convertView == null){
			vh = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false); // false mean don't attach - double check.
			vh.ivImage = (SquareImageView)convertView.findViewById(R.id.ivImage); 
			vh.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
			convertView.setTag(vh);
		} else { // recycled item
			vh = (ViewHolder)convertView.getTag();
		}
		
		// setting thumbnail image 	
		if(vh.ivImage != null){
			vh.ivImage.setImageResource(0);
			//vh.ivImage.getLayoutParams().height = width/5;
			//vh.ivImage.getLayoutParams().width = width/4;
			//Picasso.with(getContext()).load(result.thumbUrl).resize(width/3, width/3).centerCrop().into(vh.ivImage);
			//Picasso.with(getContext()).load(result.thumbUrl).resize(width/3, width/3).centerInside().into(vh.ivImage);
			//Picasso.with(getContext()).load(result.thumbUrl).resize(width/4, width/5).centerInside().into(vh.ivImage); // good ratio
			
			Picasso.with(getContext()).load(result.thumbUrl).into(vh.ivImage); // maintains aspect ratio.
		}
		
		// setting title of the picture
		if(vh.tvTitle != null){
			vh.tvTitle.setText(Html.fromHtml(result.title));
		}
		
		return convertView;
	}
	
	// not sure where to put this method.. for now leave it here.
	public DisplayMetrics getDisplayMetrics(){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}
}

class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}
