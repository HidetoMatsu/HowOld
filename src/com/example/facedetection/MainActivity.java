package com.example.facedetection;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class MainActivity extends Activity {
	
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what){
			case MSG_SUCC:
				mWaiting.setVisibility(View.GONE);
				JSONObject rs = (JSONObject)msg.obj;
				prepareBitmap(rs);
				mPhoto.setImageBitmap(mpi);
				break;
			case MSG_ERROR:
				mWaiting.setVisibility(View.GONE);
				String errorMsg =(String) msg.obj;
				if(TextUtils.isEmpty(errorMsg))
				{
					mTip.setText("error");
				}
				else{
					mTip.setText(errorMsg);
				}
				
				break;
			
			}
			super.handleMessage(msg);
		}
	
	
	
	};
	
	
	
	private void prepareBitmap(JSONObject rs){
		
		Bitmap bitmap = Bitmap.createBitmap(mpi.getWidth(),mpi.getHeight(),mpi.getConfig());
		Canvas canv = new Canvas(bitmap); 
		canv.drawBitmap(mpi, 0, 0,null);
	
		try{
		JSONArray faces =rs.getJSONArray("face");
		int faceCount = faces.length();
		mTip.setTag("find "+faceCount);
		
		for(int i=0; i<faceCount;i++)
		{
			JSONObject face = faces.getJSONObject(i);
			JSONObject posiobj = face.getJSONObject("position");
			float x = (float) posiobj.getJSONObject("center").getDouble("x");
			float y = (float) posiobj.getJSONObject("center").getDouble("y");
			
			float w = (float) posiobj.getDouble("width");
		    float h = (float) posiobj.getDouble("height");
			
			
			x=x/100*bitmap.getWidth();
			y=y/100*bitmap.getHeight();
			w=w/100*bitmap.getWidth();
			h=h/100*bitmap.getHeight();
			
			mPaint.setColor(0xffffffff);
			mPaint.setStrokeWidth(3);
			canv.drawLine(x-w/2,y-h/2, x-w/2,y+h/2,mPaint);
			canv.drawLine(x-w/2,y-h/2, x+w/2,y-h/2,mPaint);
			canv.drawLine(x+w/2,y-h/2, x+w/2,y+h/2,mPaint);
			canv.drawLine(x-w/2,y+h/2, x+w/2,y+h/2,mPaint);
			
			int age = face.getJSONObject("attribute").getJSONObject("age").getInt("value");
			String gender = face.getJSONObject("attribute").getJSONObject("gender").getString("value");
			
            Bitmap ageBitmap = buildAgeBitmap(age, "Male".equals(gender));
			
			
			int ageWidth = ageBitmap.getWidth();
			int ageHeight = ageBitmap.getHeight();
			
			if(bitmap.getWidth()<mPhoto.getWidth()&&bitmap.getHeight()<mPhoto.getHeight())
			{
				float ratio = Math.max(bitmap.getWidth()*1.0f/mPhoto.getWidth(),bitmap.getHeight()*1.0f/mPhoto.getHeight());
				ageBitmap = Bitmap.createScaledBitmap(ageBitmap, (int)(ageWidth*ratio), (int)(ageHeight*ratio), false);
			}
			canv.drawBitmap(ageBitmap, x-ageBitmap.getWidth()/2,y-h/2-ageBitmap.getHeight(), null);
			mTip.setText("age:"+age+"gender:"+gender);
			mpi = bitmap;
			
		}
		}
		catch(JSONException e){
			e.printStackTrace();
		}
	
		
	}
		
		
		

	  private Bitmap buildAgeBitmap(int age, boolean isMale) {
			// TODO Auto-generated method stub
	    	TextView tv = (TextView) mWaiting.findViewById(R.id.id_age_gen);
	    	tv.setText(age+"");
	    	
	    	if(isMale){
	    		tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male), null, null, null);
	    	}
	    	else{
	    		tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female),null, null, null);
	    	}
	    	tv.setDrawingCacheEnabled(true);
	    	Bitmap bitmap = Bitmap.createBitmap(tv.getDrawingCache());
	    	tv.destroyDrawingCache();
			return bitmap;
		}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);
        
        Button btnButton1,btnButton2;
        
        btnButton1 = (Button) findViewById(R.id.btn1);
        btnButton2 = (Button) findViewById(R.id.btn2);
        
        btnButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i("TAG","motherfucker");
				System.exit(0);
				
			}
		});
        
         AppConnect.getInstance("cdbb4bf16bbd4862672fd2008fcec98c","google",this);
        
        
        LinearLayout adlayout =(LinearLayout)findViewById(R.id.AdLinearLayout);
        AppConnect.getInstance(this).showBannerAd(this, adlayout);
        
        AppConnect.getInstance(this).showPopAd(this);
        
        
        
         protected void onActivityResult(int requestCode,int resultCode, Intent intent){
    	if(requestCode==PICK_CODE){
    		Uri uri = intent.getData();
    		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
    		cursor.moveToFirst();
    		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
    		mCurrentPhotoStr = cursor.getString(idx);
    		cursor.close();
    		
    		
    		BitmapFactory.Options options = new BitmapFactory.Options();
    		options.inJustDecodeBounds= true;
    		BitmapFactory.decodeFile(mCurrentPhotoStr,options);
    		
    		double ratio = Math.max(options.outWidth*1.0d/1024f, options.outHeight*1.0d/1024f);
    		options.inSampleSize=(int) Math.ceil(ratio);
    		options.inJustDecodeBounds=false ;
    		mpi=BitmapFactory.decodeFile(mCurrentPhotoStr, options);
    		mPhoto.setImageBitmap(mpi);
    		mTip.setText("Click Detect");
    		
    		
    	}
    	super.onActivityResult(requestCode, resultCode, intent);
    }
      
    }
    
     private Bitmap buildAgeBitmap(int age, boolean isMale) {
			// TODO Auto-generated method stub
	    	TextView tv = (TextView) mWaiting.findViewById(R.id.id_age_gen);
	    	tv.setText(age+"");
	    	
	    	if(isMale){
	    		tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male), null, null, null);
	    	}
	    	else{
	    		tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female),null, null, null);
	    	}
	    	tv.setDrawingCacheEnabled(true);
	    	Bitmap bitmap = Bitmap.createBitmap(tv.getDrawingCache());
	    	tv.destroyDrawingCache();
			return bitmap;
		}
    
    
    
    
    
    	private void prepareBitmap(JSONObject rs){
		
		Bitmap bitmap = Bitmap.createBitmap(mpi.getWidth(),mpi.getHeight(),mpi.getConfig());
		Canvas canv = new Canvas(bitmap); 
		canv.drawBitmap(mpi, 0, 0,null);
	
		try{
		JSONArray faces =rs.getJSONArray("face");
		int faceCount = faces.length();
		mTip.setTag("find "+faceCount);
		
		for(int i=0; i<faceCount;i++)
		{
			JSONObject face = faces.getJSONObject(i);
			JSONObject posiobj = face.getJSONObject("position");
			float x = (float) posiobj.getJSONObject("center").getDouble("x");
			float y = (float) posiobj.getJSONObject("center").getDouble("y");
			
			float w = (float) posiobj.getDouble("width");
		    float h = (float) posiobj.getDouble("height");
			
			
			x=x/100*bitmap.getWidth();
			y=y/100*bitmap.getHeight();
			w=w/100*bitmap.getWidth();
			h=h/100*bitmap.getHeight();
			
			mPaint.setColor(0xffffffff);
			mPaint.setStrokeWidth(3);
			canv.drawLine(x-w/2,y-h/2, x-w/2,y+h/2,mPaint);
			canv.drawLine(x-w/2,y-h/2, x+w/2,y-h/2,mPaint);
			canv.drawLine(x+w/2,y-h/2, x+w/2,y+h/2,mPaint);
			canv.drawLine(x-w/2,y+h/2, x+w/2,y+h/2,mPaint);
			
			int age = face.getJSONObject("attribute").getJSONObject("age").getInt("value");
			String gender = face.getJSONObject("attribute").getJSONObject("gender").getString("value");
			
            Bitmap ageBitmap = buildAgeBitmap(age, "Male".equals(gender));
			
			
			int ageWidth = ageBitmap.getWidth();
			int ageHeight = ageBitmap.getHeight();
			
			if(bitmap.getWidth()<mPhoto.getWidth()&&bitmap.getHeight()<mPhoto.getHeight())
			{
				float ratio = Math.max(bitmap.getWidth()*1.0f/mPhoto.getWidth(),bitmap.getHeight()*1.0f/mPhoto.getHeight());
				ageBitmap = Bitmap.createScaledBitmap(ageBitmap, (int)(ageWidth*ratio), (int)(ageHeight*ratio), false);
			}
			canv.drawBitmap(ageBitmap, x-ageBitmap.getWidth()/2,y-h/2-ageBitmap.getHeight(), null);
			mTip.setText("age:"+age+"gender:"+gender);
			mpi = bitmap;
			
		}
		}
		catch(JSONException e){
			e.printStackTrace();
		}
	
		
	}


 
  

}
