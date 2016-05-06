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


 
  

}
