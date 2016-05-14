package com.example.facedetection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity {

	protected static final int PICK_CODE = 333;
	private ImageView mPhoto;
	private Button mGetImage;
	private Button mDetect;
	private Button mClose;
	private TextView mTip;
	private View mWaiting;
	private String mCurrentPhotoStr;
	private Bitmap mpi;
	private Paint mPaint;
	
	private static final int MSG_SUCC = 222;
	private static final int MSG_ERROR= 111;
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

}
