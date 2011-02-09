package com.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class TestCover extends Activity {
	 public final String tag="tag";
	private String version="v1.0.3";



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0,"version");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case 0:
				new AlertDialog.Builder(TestCover.this)
				.setTitle("about")
				.setMessage("Version: "+version)
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.show();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	    public void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.coverflow);
	     CoverFlow coverFlow=(CoverFlow) this.findViewById(R.id.test);


	     ImageAdapter coverImageAdapter = new ImageAdapter(this);
	     
//	     coverImageAdapter.createReflectedImages();
	     
	     coverFlow.setAdapter(coverImageAdapter);
	     
	     //卡片和卡片之間的間隙
	     coverFlow.setSpacing(-25);
	     //設定一開始被選擇的圖片是哪一張
	     coverFlow.setSelection(Integer.MAX_VALUE/2, true);
	     //滑動的速率
	     coverFlow.setAnimationDuration(1000);
	     coverFlow.setUnselectedAlpha(0.2f);
	     
//	     LinearLayout ll=(LinearLayout) this.findViewById(R.id.linearlayout);
//	     TextView tv=(TextView) this.findViewById(R.id.tv);
//	     TextView text= new TextView(this);
//	     text.setText("test");
//	     tv.setText("test");
//	     ll.addView(coverFlow);
	     coverFlow.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position=position%5;
				Toast.makeText(TestCover.this, "position: "+position, Toast.LENGTH_SHORT).show();
				
			}
	    	 
	     });
	     
	     
	     
	    }
	    
	 public class ImageAdapter extends BaseAdapter {
	     int mGalleryItemBackground;
	     private Context mContext;

//	     private FileInputStream fis;
	     
	     String[] apk_name={
	    		"WallBlackChanger",
	    		"SimpleMosaic",
	    		"Earth Defence",
	    		"Untangle Me",
	    		"Birdy Jumper",
	     };
	     
	     String[] apk_instruction={
		    		"壁紙チェンジャー",
		    		"モザイクソフト",
		    		"Earth defence game",
		    		"Uncross all lines on board",
		    		"Jump between spots to pass level",
		     };
	     
	     public Integer[] mImageIds = {
	             R.drawable.card,
	             R.drawable.card,
	             R.drawable.card,
	             R.drawable.card,
	             R.drawable.card
	     };
	     
	     public Integer[] apk_icon = {
	            R.drawable.a1,
	            R.drawable.a2,
	            R.drawable.a3,
	            R.drawable.a4,
	            R.drawable.a5,
	     };
	     
	     public Integer[] star = {
		            R.drawable.star,
		            R.drawable.nostar,
	     };

	     private ImageView[] mImages;
	     
	     public ImageAdapter(Context c) {
	      mContext = c;
	      mImages = new ImageView[mImageIds.length];
	      createReflectedImages();
	     }
	     
	  public boolean createReflectedImages() {
	          //The gap we want between the reflection and the original image
	          final int reflectionGap = 0;
	          
	          
	          int index = 0;
	          for (int imageId : mImageIds) {
	        Bitmap originalImage = BitmapFactory.decodeResource(getResources(),
	          imageId);
	           int width = originalImage.getWidth();
	           int height = originalImage.getHeight();
	
	     
	           //This will not scale but will flip on the Y axis
	           Matrix matrix = new Matrix();
	           //將XY軸倒轉
	           matrix.preScale(1, -1);
	           
	           //Create a Bitmap with the flip matrix applied to it.
	           //We only want the bottom half of the image
	           Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);
	           
	               
	           //Create a new bitmap with same width but taller to fit reflection
	           Bitmap bitmapWithReflection = Bitmap.createBitmap(width
	             , (height + height/2), Config.ARGB_8888);
	         
	          //Create a new Canvas with the bitmap that's big enough for
	          //the image plus gap plus reflection
	          Canvas canvas = new Canvas(bitmapWithReflection);
	          //Draw in the original image
	          canvas.drawBitmap(originalImage, 0, 0, null);
	          //Draw in the gap
	          Paint deafaultPaint = new Paint();
	          canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
	          //Draw in the reflection
	          canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);
	          
	          //Create a shader漸層 that is a linear gradient that covers the reflection
	          Paint paint = new Paint();
	          LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0,
	            bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
	            TileMode.CLAMP);
	          //Set the paint to use this shader (linear gradient)
	          paint.setShader(shader);
	          //Set the Transfer mode to be porter duff and destination in
	          paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	          //Draw a rectangle using the paint with our linear gradient
	          canvas.drawRect(0, height, width,
	            bitmapWithReflection.getHeight() + reflectionGap, paint);
	          
	          //畫上卡片的文字
	          Paint textpaint=new Paint();
	          textpaint.setColor(R.color.black);
	          Bitmap bitmap_apkicon=BitmapFactory.decodeResource(TestCover.this.getResources(), apk_icon[index]);
	          canvas.drawBitmap(bitmap_apkicon, 20, 20, textpaint);
	          
	          Bitmap bitmap_star=BitmapFactory.decodeResource(TestCover.this.getResources(), star[0]);
	          Bitmap bitmap_nostar=BitmapFactory.decodeResource(TestCover.this.getResources(), star[1]);
	          canvas.drawBitmap(bitmap_star, 80, 50, textpaint);
	          canvas.drawBitmap(bitmap_star, 95, 50, textpaint);
	          canvas.drawBitmap(bitmap_star, 110, 50, textpaint);
	          canvas.drawBitmap(bitmap_nostar, 125, 50, textpaint);
	          canvas.drawBitmap(bitmap_nostar, 140, 50, textpaint);
	          
	          canvas.drawText(apk_name[index], 80, 40, textpaint);
	          canvas.drawText(apk_instruction[index], 20, 90, textpaint);
	          
	          
	          ImageView imageView = new ImageView(mContext);
	          imageView.setImageBitmap(bitmapWithReflection);
	          imageView.setLayoutParams(new CoverFlow.LayoutParams(160, 200));
	          //如果下面這行打開，卡片會變超大
//	          imageView.setScaleType(ScaleType.MATRIX);
	          mImages[index++] = imageView;
//	          Log.i(tag, "now index is:"+index);
	         
	          }
	       return true;
	  }

	     public int getCount() {
//	         return mImageIds.length;
	    	 return Integer.MAX_VALUE;
	     }

	     public Object getItem(int position) {
	    	 
	         return position;
	     }

	     public long getItemId(int position) {
	    	
	         return position;
	     }

	     public View getView(int position, View convertView, ViewGroup parent) {
  
	      /*//Use this code if you want to load from resources	 
	         ImageView i = new ImageView(mContext);
	         Log.i(tag, "now position: "+position);
	         i.setImageResource(mImageIds[position]);
	         
	         i.setLayoutParams(new CoverFlow.LayoutParams(130, 130));
	         i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
	         
	         //Make sure we set anti-aliasing otherwise we get jaggies
	         BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
	         drawable.setAntiAlias(true);         
	    	  
	         return i;*/
//	    	 Log.i(tag, "position: "+position%mImageIds.length);
	    	
	    	 mImages[position%mImageIds.length].setId(position%mImageIds.length);
	    
//	    	 Log.i(tag, "position: "+position);
	    	 
	    	return mImages[position%mImageIds.length];	
	     }
	   /** Returns the size (0.0f to 1.0f) of the views
	* depending on the 'offset' to the center. */
//	      public float getScale(boolean focused, int offset) {
//	        /* Formula: 1 / (2 ^ offset) */
//	          return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset)));
//	      }

	 }
}