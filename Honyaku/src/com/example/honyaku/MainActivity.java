package com.example.honyaku;

import jp.ne.nttdocomo.spm.api.recognition.data.RecognizeResultData;
import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements LoaderCallbacks<RecognizeResultData> {
	
	private Button button1;
	private ImageView imageView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		findViews();
//		setListeners();

		// AsyncLoaderÇégÇ§
		Bundle bundle = new Bundle();
//    	bundle.putString("url", "https://api.apigw.smt.docomo.ne.jp/characterRecognition/v1/scene?APIKEY=53366d555543786e616442564577494850774e7372467a507a3752662f314c756577423553633565467439");
		// loaderÇÃèâä˙âª
		getLoaderManager().initLoader(0, bundle, this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public Loader<RecognizeResultData> onCreateLoader(int id, Bundle bundle) {
		AsyncLoader loader = new AsyncLoader(this);
		loader.forceLoad();
		return loader;
	}
	
	@Override
	public void onLoadFinished(Loader<RecognizeResultData> loader,
			RecognizeResultData body) {
		// TODO Auto-generated method stub
		if ( loader.getId() == 0 ) {
			if (body != null) {
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<RecognizeResultData> loader) {
	}
	
	protected void findViews(){
		button1 = (Button)findViewById(R.id.button1);
		imageView1 = (ImageView)findViewById(R.id.imageView1);
	}
	
	protected void setListeners(){
		button1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(
					intent,
					100);
			}
		});
	}
	
	@Override
	protected void onActivityResult(
		int requestCode, 
		int resultCode, 
		Intent data) {
		
		if(100 == requestCode 
			&& resultCode == Activity.RESULT_OK ){
			Bitmap capturedImage = 
				(Bitmap) data.getExtras().get("data");
			imageView1.setImageBitmap(capturedImage);
		}
	}
	
}
