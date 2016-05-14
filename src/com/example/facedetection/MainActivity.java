package com.example.facedetection;

import com.example.menn.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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



 
  

}
