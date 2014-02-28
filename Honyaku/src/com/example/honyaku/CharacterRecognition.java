package com.example.honyaku;

import java.util.ArrayList;

import jp.ne.nttdocomo.spm.api.common.exception.SdkException;
import jp.ne.nttdocomo.spm.api.common.exception.ServerException;
import jp.ne.nttdocomo.spm.api.common.http.AuthApiKey;
import jp.ne.nttdocomo.spm.api.recognition.LineCharacterRecognize;
import jp.ne.nttdocomo.spm.api.recognition.constants.ImageContentType;
import jp.ne.nttdocomo.spm.api.recognition.constants.Lang;
import jp.ne.nttdocomo.spm.api.recognition.data.RecognizeMessageData;
import jp.ne.nttdocomo.spm.api.recognition.data.RecognizePointData;
import jp.ne.nttdocomo.spm.api.recognition.data.RecognizeResultData;
import jp.ne.nttdocomo.spm.api.recognition.data.RecognizeShapeData;
import jp.ne.nttdocomo.spm.api.recognition.data.RecognizeWordData;
import jp.ne.nttdocomo.spm.api.recognition.data.RecognizeWordsData;
import jp.ne.nttdocomo.spm.api.recognition.param.CharacterRecognizeRequestParam;

public class CharacterRecognition {
	
	private String apikey;

	CharacterRecognition(String apikey){
		this.apikey = apikey;
	}
	
	RecognizeResultData getResult(String imgfile){
		try {
			// 開発者ポータルから取得したAPIキーの設定
			AuthApiKey.initializeAuth(this.apikey);
			
			// パラメータクラスを生成して、各項目を設定する
			CharacterRecognizeRequestParam param = new CharacterRecognizeRequestParam();
			param.setLang(Lang.CHARACTERS_JP);
			param.setFilePath(imgfile);
			param.setImageContentType(ImageContentType.IMAGE_JPEG);

			// 行画像認識要求クラスを生成してリクエストを実行する
			LineCharacterRecognize recognize = new LineCharacterRecognize();
			RecognizeResultData resultData = recognize.request(param);
			return resultData;

		} catch (SdkException e) {
			e.printStackTrace();
			System.out.println("エラーコード : " + e.getErrorCode());
			System.out.println("エラーメッセージ: " + e.getMessage());
		} catch (ServerException e) {
			e.printStackTrace();
			System.out.println("エラーコード : " + e.getErrorCode());
			System.out.println("エラーメッセージ: " + e.getMessage());
		}
		
		return null;
	}
	
	void printResult(RecognizeResultData resultData){
		
		// 解析結果の出力
		// 認識ジョブの出力
		System.out.println("認識ジョブの出力 :");
		System.out.println("  ジョブID : " + resultData.getJob().getId());
		System.out.println("  進行状況 : " + resultData.getJob().getStatus());
		System.out.println("  処理受け付け時刻 : "
				+ resultData.getJob().getQueueTime());

		// 抽出した全ての単語の情報の出力
		RecognizeWordsData wordsData = resultData.getWords();
		if (wordsData != null) {
			System.out.println("抽出した全ての単語の情報の出力 :");
			System.out.println("  抽出した全ての単語の情報の数 :" + wordsData.getCount());
			ArrayList<RecognizeWordData> wordList = wordsData.getWord();
			for (RecognizeWordData wordData : wordList) {
				System.out.println("    単語情報の出力 :");
				System.out.println("      単語の文字列 : " + wordData.getText());
				System.out.println("      単語のスコア : " + wordData.getScore());
				System.out.println("      単語のカテゴリ : "
						+ wordData.getCategory());

				System.out.println("      単語領域形状の出力 :");
				RecognizeShapeData shapeData = wordData.getShape();
				ArrayList<RecognizePointData> pointList = shapeData
						.getPoint();
				if (pointList == null) {
					continue;
				}
				System.out
						.println("        頂点の数 : " + shapeData.getCount());
				System.out.println("        頂点情報の出力 : "
						+ shapeData.getCount());
				for (RecognizePointData pointData : pointList) {
					System.out.println("          x座標, y座標(ピクセル単位) : "
							+ pointData.getX() + ", " + pointData.getY());
				}
			}
		}

		// エラーメッセージの出力
		RecognizeMessageData message = resultData.getMessage();
		if (message != null) {
			System.out.println("エラーメッセージ : " + message.getText());
		}
		
	}
	
}
