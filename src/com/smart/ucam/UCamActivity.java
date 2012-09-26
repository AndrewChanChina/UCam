package com.smart.ucam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class UCamActivity extends Activity implements ShutterCallback, PictureCallback{
	/** Called when the activity is first created. */

	private Camera mCamera;
	private CameraPreview mPreview;
	
	private Toast mToast;
	private String TAG = "UCamActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mCamera = CamUtility.getCameraInstance();
		mToast = Toast.makeText(this, "Sorry,ƒ„√ª”–…„œÒÕ∑£°", Toast.LENGTH_SHORT);
		if (mCamera == null) {
			mToast.show();
		} else {
			initCam();
		}

	}

	private void initCam() {
		
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout fl = (FrameLayout)this.findViewById(R.id.camera_preview);
		fl.addView(mPreview);
		
		CamUtility.setCameraDisplayOrientation(this, 0, mCamera);
		
	}

	@Override
	protected void onPause() {
		if (mCamera != null) {
			mCamera.unlock();
			mCamera.stopPreview();
			
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mCamera != null) {
			mCamera.lock();
			mCamera.startPreview();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if(mCamera != null){
			mCamera.release();
		}
		super.onDestroy();
	}

	public void onTakePic(View v) {
		if(mCamera != null){
			mCamera.startPreview();
			mCamera.takePicture(null, null, this);
		}
	}

	@Override
	public void onShutter() {
		
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		
		File pictureFile = CamUtility.getOutputMediaFile(CamUtility.MEDIA_TYPE_IMAGE);
        if (pictureFile == null){
            Log.d(TAG , "Error creating media file, check storage permissions: ");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        mToast.setText("Great");
        mToast.show();
        camera.startPreview();
		
	}

}