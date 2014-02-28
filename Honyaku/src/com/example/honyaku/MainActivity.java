package com.example.honyaku;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.ne.nttdocomo.spm.api.recognition.data.RecognizeResultData;
import jp.ne.nttdocomo.spm.api.recognition.data.RecognizeWordsData;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements LoaderCallbacks<RecognizeResultData> {
	
	private Button buttonCapture;
	private Button buttonRecapture;
	private Button buttonDone;
	private ImageView topImageView;
	private ImageView capturedImageView;
	private ImageView capturedImageView2;
	private TextView resultTextView;
	
	private Bitmap capturedImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViews();
		setListeners();

			System.out.println("start translation");
			// AsyncLoaderを使う
			Bundle bundle = new Bundle();
			// loaderの初期化
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
////		try{
//			if ( loader.getId() == 0 ) {
//				if (body != null) {
//					// 抽出した全ての単語の情報の出力
//					RecognizeWordsData wordsData = body.getWords();
//					if (wordsData != null) {
//						System.out.println("抽出した全ての単語の情報の出力 :");
//						System.out.println("  抽出した全ての単語の情報の数 :" + wordsData.getCount());
//					}
////					System.out.println("認識ジョブの出力 :");
////					System.out.println("  ジョブID : " + body.getJob().getId());
////					System.out.println("  進行状況 : " + body.getJob().getStatus());
////					System.out.println("  処理受け付け時刻 : "
////							+ body.getJob().getQueueTime());
//				}
//			}	
////		}catch(NullPointerException e){
////		}
		
	}

	@Override
	public void onLoaderReset(Loader<RecognizeResultData> loader) {
	}
	
	protected void findViews(){
		buttonCapture = (Button)findViewById(R.id.buttonCapture);
//		buttonRecapture = (Button)findViewById(R.id.buttonRecapture);
//		buttonDone = (Button)findViewById(R.id.buttonDone);
		topImageView = (ImageView)findViewById(R.id.imageView1);
		topImageView.setImageResource(R.drawable.olympiclogo2);
	}
	
	protected void setListeners(){
		buttonCapture.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(
					intent,
					100);
				System.out.println("capture");
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
			capturedImage = 
				(Bitmap) data.getExtras().get("data");
			saveToFile(capturedImage);
			
			setContentView(R.layout.activity_select);
			capturedImageView = (ImageView)findViewById(R.id.imageView2);
			capturedImageView.setImageBitmap(capturedImage);
			
			buttonRecapture = (Button)findViewById(R.id.buttonRecapture);
			buttonDone = (Button)findViewById(R.id.buttonDone);
			
			buttonRecapture.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(
						intent,
						100);
					System.out.println("recapture");
				}
			});
			buttonDone.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					System.out.println("Start Character Translation");
					setContentView(R.layout.activity_result);
					capturedImageView2 = (ImageView)findViewById(R.id.imageView3);
					resultTextView = (TextView)findViewById(R.id.resultTextView);
					if(capturedImage != null)
						capturedImageView2.setImageBitmap(capturedImage);

					resultTextView.setTextSize(42.0f);
					resultTextView.setText("< Result >");
				}
			});
		}
	}
	
	public void saveToFile(Bitmap bitmap) {
	    if (!sdcardWriteReady()){
	        return;
	    }

	    try {
	        File file = new File(Environment.getExternalStorageDirectory()
	                .getPath() + "/images/");
	        if (!file.exists()) {
	            file.mkdir();
	        }
	        String AttachName = file.getAbsolutePath() + "/"+"image.jpg";
	        System.out.println(AttachName);
//	        String AttachName = file.getAbsolutePath() + "/"+System.currentTimeMillis() + ".jpg";
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
