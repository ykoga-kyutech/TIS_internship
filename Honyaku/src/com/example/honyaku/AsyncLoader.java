package com.example.honyaku;

import jp.ne.nttdocomo.spm.api.recognition.data.RecognizeResultData;
import android.annotation.SuppressLint;
import android.content.AsyncTaskLoader;
import android.content.Context;

@SuppressLint("NewApi")
public class AsyncLoader extends AsyncTaskLoader<RecognizeResultData> {

	static final String APIKEY = "53366d555543786e616442564577494850774e7372467a507a3752662f314c756577423553633565467439";
	
	public AsyncLoader(Context context) {
		super(context);
	}

	@Override
	public RecognizeResultData loadInBackground() {
		// TODO Auto-generated method stub

		// 文字認識 。ここでエラー
		CharacterRecognition crec = new CharacterRecognition(APIKEY);
		RecognizeResultData result = crec.getResult();
//		crec.printResult(result);
		
		return result;
	}

}
