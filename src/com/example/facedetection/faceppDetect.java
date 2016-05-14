package com.example.facedetection;

public class faceppDetect {
	
	package com.example.menn;



	import java.io.ByteArrayOutputStream;

	import org.json.JSONObject;

	import com.facepp.error.FaceppParseException;
	import com.facepp.http.HttpRequests;
	import com.facepp.http.PostParameters;

	import android.graphics.Bitmap;
	import android.util.Log;

	public class faceppDetect {
		
		
		public interface CallBack{
			
			void success(JSONObject result);
			void error(FaceppParseException exception);
			
			
		}
		
		public static void detect(final Bitmap bt,final faceppDetect.CallBack callBack){
			
			new Thread(new Runnable(){
				@Override
				public void run(){
				
				    try {
				    	HttpRequests requests = new HttpRequests(constant.key,constant.secret,true, true);
						Bitmap bmSmall = Bitmap.createBitmap(bt,0,0,bt.getWidth(),bt.getHeight());
						ByteArrayOutputStream stream= new ByteArrayOutputStream();
					    bmSmall.compress(Bitmap.CompressFormat.JPEG,100,stream);
					    byte[] arrays = stream.toByteArray();
					    PostParameters params = new PostParameters();	
					    params.setImg(arrays);
						JSONObject jsonobject = requests.detectionDetect(params);
						Log.i("bttty","attachhhhhhhheeeeeeeeeeeee");
						Log.i("TAG",jsonobject.toString());
						
						if(callBack != null)
						{
							callBack.success(jsonobject);
						}
						
					} catch (FaceppParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						

						if(callBack != null)
						{
							callBack.error(e);
						}
						
					}
				}
				
				
			}).start();
			
		}

	}


}
