package com.example.honyaku;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class CameraView extends SurfaceView implements Callback, PictureCallback {

	private Camera camera;
	
	public CameraView(Context context) {
		super(context);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera = Camera.open();
			camera.setPreviewDisplay(holder);
		} catch(IOException e) {
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int f, int w, int h) {
		Camera.Parameters p = camera.getParameters();
		p.setPreviewSize(w,h);
		camera.setParameters(p);
		camera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
	}

	
	@Override
	public void onPictureTaken(byte[] data, Camera c) {
		Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, null);
		MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bmp, "", null);
		saveToFile(bmp);
		camera.startPreview();
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		if(me.getAction()==MotionEvent.ACTION_DOWN) {
			camera.takePicture(null,null,this);
		}
		return true;
	}
	
	private void saveToFile(Bitmap bitmap) {
	    if (!sdcardWriteReady()){
	    	Log.e(this.getClass().getSimpleName(), "SD card is not ready!");
	        return;
	    }

	    try {
	        File file = new File(Environment.getExternalStorageDirectory()
	                .getPath() + "/images/");
	        if (!file.exists()) {
	            file.mkdir();
	        }
	        String AttachName = file.getAbsolutePath() + "/"+System.currentTimeMillis() + ".jpg";
	        FileOutputStream out = new FileOutputStream(AttachName);
	        bitmap.compress(CompressFormat.JPEG, 100, out);
	        out.flush();
	        out.close();
	    } catch (Exception e) {
	    }
	}
	
	private boolean sdcardWriteReady() {
	    String state = Environment.getExternalStorageState();
	    return (Environment.MEDIA_MOUNTED.equals(state));
	}
	
}
