package com.example.honyaku;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import jp.ne.nttdocomo.spm.api.recognition.data.RecognizeResultData;
import android.annotation.SuppressLint;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Environment;

@SuppressLint("NewApi")
public class AsyncLoader extends AsyncTaskLoader<RecognizeResultData> {

	static final String URL = "https://api.apigw.smt.docomo.ne.jp/characterRecognition/v1/scene?APIKEY=";
	static final String APIKEY = "53366d555543786e616442564577494850774e7372467a507a3752662f314c756577423553633565467439";

	public AsyncLoader(Context context) {
		super(context);
	}
	
	@Override
	public RecognizeResultData loadInBackground() {
		
		/* docomo SDKを使わないとき。終了はしないがunknown hostエラーとなって結果が得られない */
		HttpClient httpClient = new DefaultHttpClient();
//		HttpPost post = new HttpPost(URL + APIKEY);
		HttpPost post = new HttpPost("https://api.apigw.smt.docomo.ne.jp/characterRecognition/v1/scene?APIKEY=53366d555543786e616442564577494850774e7372467a507a3752662f314c756577423553633565467439");
		MultipartEntity entity = new MultipartEntity();
		try {
			File file = new File(Environment.getExternalStorageDirectory()
	                .getPath() + "/images/image.jpg");
//			File file = new File("/storage/emulated/0/images/images.jpg");
			entity.addPart("image", new FileBody(file.getAbsoluteFile()));

			post.setEntity(entity);
			HttpResponse response = httpClient.execute(post);
			int status = response.getStatusLine().getStatusCode();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		/* ここまで */
		
		/* docomo SDKを使うとき。Could not find method org.apache....等が出て終了する。　*/
//		File file = new File(Environment.getExternalStorageDirectory() .getPath() + "/images/"); 
//		 if (!file.exists()) { 
//			 file.mkdir(); 
//		 } 
//		 String AttachName = file.getAbsolutePath() + "/" + "image.jpg";
//	//	 String AttachName ="/storage/emulated/0/images/image.jpg";
//		 
//		 System.out.println("loadInbackground called"); // 文字認識 。ここでエラー
//		 CharacterRecognition crec = new CharacterRecognition(APIKEY);
//		 RecognizeResultData result = crec.getResult(AttachName); //
//		 crec.printResult(result);
//		 
//		 return result;
	}
	 
	 
}
