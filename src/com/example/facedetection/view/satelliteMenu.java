package com.example.facedetection.view;

import com.example.facedetection.R;

import android.R.anim;
import android.R.integer;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

public class satelliteMenu extends ViewGroup implements OnClickListener {
	
	private  static final int POS_LEFT_TOP = 0;
	private static final int POS_LEFT_BOTTOM= 1;
	private  static final int POS_RIGHT_TOP = 2;
	private  static final int POS_RIGHT_BOTTOM = 3;
	
	
	
	public enum Status{
		OPEN,CLOSE
	}
	public enum Position{
		LEFT_TOP,LEFT_BOTTOM,RIGHT_TOP,RIGHT_BOTTOM
		
		
		
	}
	
	private OnMenuItemClickListener mMenuClickListener;
	private int mRadius;
	
	

	public OnMenuItemClickListener getmMenuClickListener() {
		return mMenuClickListener;
	}

	public void setOnmMenuClickListener(OnMenuItemClickListener mMenuClickListener) {
		this.mMenuClickListener = mMenuClickListener;
	}

	private Status mCurrentStatus = Status.CLOSE;
	private Position mPosition = Position.RIGHT_BOTTOM;
	private View mCbutton;
	
	public interface onMenuClickListener{
		void onClick(View view, int pos);
	}
	
	public satelliteMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
		
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.ArcMenu,defStyle,0);
		
	    int pos =a.getInt(R.styleable.ArcMenu_position, POS_RIGHT_BOTTOM);
	    
	    switch (pos) {
		case POS_LEFT_BOTTOM:
			mPosition = Position.LEFT_BOTTOM;
			
			break;
		case POS_LEFT_TOP:
			mPosition = Position.LEFT_TOP;
			break;
		case POS_RIGHT_BOTTOM:
			mPosition = Position.RIGHT_BOTTOM;
			break;
		
		case POS_RIGHT_TOP:
			mPosition = Position.RIGHT_TOP;
			
		    break;
		    

		default:
			break;
		}
		mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
		Log.e("TAG","position"+mPosition+",radius "+mRadius);
		
		
		a.recycle();
	}
	
	
	
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		int count = getChildCount();
		for(int i=0; i<count; i++){
			measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	

	public satelliteMenu(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}

	public satelliteMenu(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		if(changed){
			layoutCButton();
			int count = getChildCount();
			for(int i=0; i<count-1;i++){
				View child = getChildAt(i+1);
				int cl = (int) (mRadius * Math.sin(Math.PI/2/(count-2)*i));
				int ct = (int) (mRadius*Math.cos(Math.PI/2/(count-2)*i));
				
				int cWidth = child.getMeasuredWidth();
				int cHeight = child.getMeasuredHeight();
				
				
				
				
				if (mPosition==Position.LEFT_BOTTOM||mPosition==Position.RIGHT_BOTTOM) {
					ct=getMeasuredHeight()-cHeight-ct;
					
				}
				
				if (mPosition==Position.RIGHT_TOP||mPosition==Position.RIGHT_BOTTOM) {
					ct=getMeasuredWidth()-cWidth-cl;
					
				}
				
				child.layout(cl, ct, cl+cWidth,ct+cHeight );
			}
			
		}

	}

	private void layoutCButton() {
		// TODO Auto-generated method stub
		mCbutton = getChildAt(0);
		mCbutton.setOnClickListener(this);
		int i = 0;
		int t = 0;
		
		int width = mCbutton.getMeasuredWidth();
		int height = mCbutton.getMeasuredHeight();
		
		
		switch (mPosition) {
		case LEFT_TOP:
			i=0;
			t=0;
			
			
			break;
			
		case LEFT_BOTTOM:
			i=0;
			t=getMeasuredHeight()-height;
			break;
			
		case RIGHT_BOTTOM:
			i=getMeasuredWidth()-width;
			t=0;
			break;
		
		case RIGHT_TOP:
			i=getMeasuredWidth()-width;
			t=getMeasuredHeight()-height;
			break;

		
		}
		mCbutton.layout(i, t, i+width, t+width);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		mCbutton = findViewById(R.id.id_button);
		if(mCbutton == null)
		{
			mCbutton = getChildAt(0);
		}
		
		rotateCButton(v, 0f, 360f, 300);
		
		toggleMenu(500);
		
		
		
	}

	private void toggleMenu(int duration) {
		// TODO Auto-generated method stub
		int count = getChildCount();
		
		
		for(int i=0; i<count-1; i++){
			final View childView = getChildAt(i+1);
			childView.setVisibility(View.VISIBLE);
			int cl = (int) (mRadius * Math.sin(Math.PI/2/(count-2)*i));
			int ct = (int) (mRadius*Math.cos(Math.PI/2/(count-2)*i));
			int xflag = 1;
			int yflag = 1;
			
			if (mPosition==Position.LEFT_TOP||mPosition==Position.LEFT_BOTTOM) {
				xflag= -1;
				
			}
			
			if (mPosition==Position.LEFT_TOP||mPosition==Position.RIGHT_TOP) {
				yflag=-1;
				
			}
			
			AnimationSet animationSet = new AnimationSet(true);
			
			Animation transAnim = null;
			
			if(mCurrentStatus==Status.CLOSE)
			{
				transAnim = new TranslateAnimation(xflag*cl, 0,yflag*ct,0);
				childView.setClickable(true);
				childView.setFocusable(true);
				
			}
			else {
				transAnim = new TranslateAnimation(0,xflag*cl ,0,yflag*ct);
				childView.setClickable(false);
				childView.setFocusable(false);
			}
			
			
			transAnim.setFillAfter(true);
			transAnim.setDuration(duration);
			
			transAnim.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation arg0) {
					// TODO Auto-generated method stub
					if(mCurrentStatus==Status.CLOSE){
						childView.setVisibility(View.GONE);
					}
				}
			});
			
			RotateAnimation rotateAnim = new RotateAnimation(0, 720,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
			rotateAnim.setDuration(duration);
			rotateAnim.setFillAfter(true);
			
			
			animationSet.addAnimation(rotateAnim);
			animationSet.addAnimation(transAnim);
			
			childView.startAnimation(animationSet);
			
			
			
		}
		changeStatue();
	}

	private void changeStatue() {
		// TODO Auto-generated method stub
		mCurrentStatus=(mCurrentStatus==Status.CLOSE?Status.OPEN:Status.CLOSE);
		
	}

	private void rotateCButton(View v, float start, float end, int duration) {
		// TODO Auto-generated method stub
		RotateAnimation animation = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(duration);
		animation.setFillAfter(true);
		v.startAnimation(animation);
	}

}
