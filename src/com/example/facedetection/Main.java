package com.example.facedetection;

big valia

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.welcome);
		
		   final Intent it = new Intent(this, MainActivity.class); //ÏÂÒ»²½×ªÏòMainctivity
	        Timer timer = new Timer();
	        TimerTask task = new TimerTask() {
	            @Override
	            public void run() {
	                startActivity(it); //Ö´ÐÐÒâÍ¼
	            }
	        };
	        timer.schedule(task, 1000 * 3);
	}

}
