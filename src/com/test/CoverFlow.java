package com.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class CoverFlow extends Gallery {
	 private static String tag="tag";

	/**
	* Graphics Camera used for transforming the matrix of ImageViews
	*/
	    private Camera mCamera = new Camera();

	    /**
	* The maximum angle the Child ImageView will be rotated by
	* 以y軸為旋轉標準，數字越大時，左右2邊的卡片會越小
	*/
	    private int mMaxRotationAngle = 120/*60*/;
	    
	    /**
	* The maximum zoom on the centre Child
	* 整個子視圖的放大比例
	*/
	    private int mMaxZoom = -240/*-120*/;
	    
	    /**
	* The Centre of the Coverflow
	*/
	    private int mCoveflowCenter;
		/** The Left of the Coverflow
		*/
		private int mCoveflowLeft;	 
		ArrayList<Integer> array_num=new ArrayList<Integer>();
		
		
		
	 public CoverFlow(Context context) {
	  super(context);
	  this.setStaticTransformationsEnabled(true);
	  //當這個setStaticTransformationsEnabled設為True時，
	  //每當子圖要畫時，都會先去呼叫getChildStaticTransformation(View, android.view.animation.Transformation)
	 }

	 public CoverFlow(Context context, AttributeSet attrs) {
	  super(context, attrs);
	        this.setStaticTransformationsEnabled(true);
	 }
	 
	  public CoverFlow(Context context, AttributeSet attrs, int defStyle) {
	   super(context, attrs, defStyle);
	   this.setStaticTransformationsEnabled(true);
	  }
	  
//	    /**
//	* Get the max rotational angle of the image
//	* @return the mMaxRotationAngle
//	*/
//	 public int getMaxRotationAngle() {
//	  return mMaxRotationAngle;
//	 }

//	 /**
//	* Set the max rotational angle of each image
//	* @param maxRotationAngle the mMaxRotationAngle to set
//	*/
//	 public void setMaxRotationAngle(int maxRotationAngle) {
//	  mMaxRotationAngle = maxRotationAngle;
//	 }

//	 /**
//	* Get the Max zoom of the centre image
//	* @return the mMaxZoom
//	*/
//	 public int getMaxZoom() {
//	  return mMaxZoom;
//	 }

//	 /**
//	* Set the max zoom of the centre image
//	* @param maxZoom the mMaxZoom to set
//	*/
//	 public void setMaxZoom(int maxZoom) {
//	  mMaxZoom = maxZoom;
//	 }

 /**取得CoverFlow在整個螢幕正中央的x值
	* Get the Centre of the Coverflow
	* @return The centre of this Coverflow.
	*/
	    private int getCenterOfCoverflow() {
	        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
	    }
	    
	    /**取得CoverFlow最左邊的Left值
		* Get the Left of the Coverflow
		* @return The centre of this Coverflow.
		*/
		    private int getLeftOfCoverflow() {
//		    	Log.i(tag, "Left: "+getLeft());
		        return this.getLeft();
		    }

		    //覆寫onFling且return false時，能確保一次只滑一張圖
		    @Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
		
//			return super.onFling(e1, e2, velocityX, velocityY);
			return false;
		}

/**
	*該類別在回傳每個子視圖的各個中心點位置在螢幕的x何處
	* Get the Centre of the View
	* @return The centre of the given view.
	* 
	*/
	    private static int getCenterOfView(View view) {
//	    	Log.i(tag, "childLeft: "+view.getLeft()+", childWidth: "+view.getWidth());
	        return view.getLeft() + view.getWidth() / 2;
	    }
	    /**
	* {@inheritDoc}
	*
	* @see #setStaticTransformationsEnabled(boolean)
	* 每當Gallery要畫子視圖時，都會先呼叫這個函式
	*/
	 protected boolean getChildStaticTransformation(View child, Transformation t) {
	  
	  final int childCenter = getCenterOfView(child);
	  final int childWidth = child.getWidth() ;
	  int rotationAngle = 0;
	  
//	  final int childLeft =  child.getLeft();
//	  Log.i(tag, "childLeft"+child.getLeft());
	  

	  
	  t.clear();
	 
	  t.setTransformationType(Transformation./*TYPE_ALPHA*/TYPE_BOTH/*TYPE_MATRIX*/);//Transform type is matrix
	  int alpha_value=255;
	  
	        if (childCenter == mCoveflowCenter) {//if child center location equal layout center location
	        	
	        	
	            transformImageBitmap((ImageView) child, t, 0,alpha_value); //transform to angle0 and matrix慢慢轉Y角度到0，也就是放大成原圖
	            Log.i(tag, "id: "+child.getId());
	          
	            
	        } else /*if (childCenter < mCoveflowCenter)*/{
	        	//當Child的中心點不是在Cover的中心點，開始計算要變形(縮小)的角度((Cover的中間-Child的中間)/Child寬度)*120
	        	//rotationAngle算出來若為負，代表Child在CoverCenter的右邊
	            rotationAngle = (int) (((float) (mCoveflowCenter - childCenter)/ childWidth) * mMaxRotationAngle);
//	            Log.i(tag, "rotationAngle:"+rotationAngle);
	            
	          	//如果rotationAngle>120，代表是從原圖往左移動的太超過
	            if (Math.abs(rotationAngle) > mMaxRotationAngle) {
	            	//如果計算出來的角是小於0，判斷是右邊還是左邊，分別縮小最小的120
	             rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle : mMaxRotationAngle;
	             
	            }
	           if(array_num.size()<5){
	        	   transformImageBitmap((ImageView) child, t, rotationAngle,alpha_value);
	           }else if(array_num.size()==5){
	        	   
//	    	    	   Log.i(tag, "array_num.get(0): "+array_num.get(0));
//	    	    	   Log.i(tag, "array_num.get(2): "+array_num.get(2));
	    		       //換算多少x軸會降低0.1f的Alpha
	    		       int unit=(array_num.get(2)-array_num.get(0))/255;
//	    		       Log.i(tag, "unit: "+unit);
	    		       
	    		       if(child.getLeft()<array_num.get(2)){
	    		    	   alpha_value=255-((array_num.get(2)-child.getLeft())/unit/2);
//	    		    	   Log.i(tag, "alpha_value: "+alpha_value);
	    		    	  
	    		       }
	    		       
	    		       if(child.getLeft()>array_num.get(2)){
	    		    	   alpha_value=255+((array_num.get(2)-child.getLeft())/unit/2);
	    		    	   
	    		       }
	    		       
	    		       transformImageBitmap((ImageView) child, t, rotationAngle,alpha_value);
	           }
	            
	        }
	        

	        
	        //程式被寫死了:<5,只能放5張圖
	       if(array_num.size()<5){

	    	   array_num.add(child.getLeft());
			   Collections.sort(array_num); 
			   if(array_num.size()==5){
//		    	   Log.i(tag, "array_num.get(2): "+array_num.get(2));
		    	   //array_num.get(2)是中間的大卡片的左側x軸
		       } 
	       }
	       


	             
	  return true;
	 }

	 /**
	* This is called during layout when the size of this view has changed. If
	* you were just added to the view hierarchy, you're called with the old
	* values of 0.
	*
	* @param w Current width of this view.
	* @param h Current height of this view.
	* @param oldw Old width of this view.
	* @param oldh Old height of this view.
	*/
	     protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	      mCoveflowCenter = getCenterOfCoverflow();
	      mCoveflowLeft = getLeftOfCoverflow();
	      super.onSizeChanged(w, h, oldw, oldh);
	     }
	  

	    
		/**
	* Transform the Image Bitmap by the Angle passed
	*
	* @param imageView ImageView the ImageView whose bitmap we want to rotate
	* @param t transformation
	* @param rotationAngle the Angle by which to rotate the Bitmap
	*/
	     private void transformImageBitmap(ImageView child, Transformation t, int rotationAngle,int alpha) {
	      mCamera.save();
	     
//	      t.setAlpha(buffer+=20);//寫在這邊沒效果
	      final Matrix imageMatrix = t.getMatrix();
	      final int imageHeight = child.getLayoutParams().height;;
	      final int imageWidth = child.getLayoutParams().width;
	      final int rotation = Math.abs(rotationAngle);
	                     
	      mCamera.translate(0.0f, 0.0f, 100.0f);
	         
	      //As the angle of the view gets less, zoom in放大
	      //這裡控制了中間3組
	      if ( rotation < mMaxRotationAngle ) {
	       float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
	       //影響了中間3組的位置，其中Z代表放大和縮小
//	       Log.i(tag, "zoomAmount: "+zoomAmount);
	     
//	     Log.i(tag, "(int)((255/9)*alpha*10)"+(int)((255/9)*alpha*10));
	       child.setAlpha(alpha);   
	       
	       
	       //如果沒有這行，就沒有變形了
	       mCamera.translate(0.0f/*zoomAmount/100*5*/, 0.0f/*zoomAmount/100*5*/, /*0.0f*/zoomAmount);
	      }
	      
	      //原本程式是照Y值來旋轉
//	      mCamera.rotateY(rotationAngle);  //上下變形
//	      mCamera.rotateZ(rotationAngle);  //左右擺動
	      mCamera.getMatrix(imageMatrix);
	      


	      //动画擴散點設為從中點往外擴散 
	      imageMatrix.preTranslate(-(imageWidth/2), -(imageHeight/2));

	      //移动动画起始点，把参考点（0,0）移动到View中间
	      imageMatrix.postTranslate((imageWidth/2), (imageHeight/2));
	      mCamera.restore();
	      
	 }
}
