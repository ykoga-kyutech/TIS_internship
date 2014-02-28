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
			// �J���҃|�[�^������擾����API�L�[�̐ݒ�
			AuthApiKey.initializeAuth(this.apikey);
			
			// �p�����[�^�N���X�𐶐����āA�e���ڂ�ݒ肷��
			CharacterRecognizeRequestParam param = new CharacterRecognizeRequestParam();
			param.setLang(Lang.CHARACTERS_JP);
			param.setFilePath(imgfile);
			param.setImageContentType(ImageContentType.IMAGE_JPEG);

			// �s�摜�F���v���N���X�𐶐����ă��N�G�X�g�����s����
			LineCharacterRecognize recognize = new LineCharacterRecognize();
			RecognizeResultData resultData = recognize.request(param);
			return resultData;

		} catch (SdkException e) {
			e.printStackTrace();
			System.out.println("�G���[�R�[�h : " + e.getErrorCode());
			System.out.println("�G���[���b�Z�[�W: " + e.getMessage());
		} catch (ServerException e) {
			e.printStackTrace();
			System.out.println("�G���[�R�[�h : " + e.getErrorCode());
			System.out.println("�G���[���b�Z�[�W: " + e.getMessage());
		}
		
		return null;
	}
	
	void printResult(RecognizeResultData resultData){
		
		// ��͌��ʂ̏o��
		// �F���W���u�̏o��
		System.out.println("�F���W���u�̏o�� :");
		System.out.println("  �W���uID : " + resultData.getJob().getId());
		System.out.println("  �i�s�� : " + resultData.getJob().getStatus());
		System.out.println("  �����󂯕t������ : "
				+ resultData.getJob().getQueueTime());

		// ���o�����S�Ă̒P��̏��̏o��
		RecognizeWordsData wordsData = resultData.getWords();
		if (wordsData != null) {
			System.out.println("���o�����S�Ă̒P��̏��̏o�� :");
			System.out.println("  ���o�����S�Ă̒P��̏��̐� :" + wordsData.getCount());
			ArrayList<RecognizeWordData> wordList = wordsData.getWord();
			for (RecognizeWordData wordData : wordList) {
				System.out.println("    �P����̏o�� :");
				System.out.println("      �P��̕����� : " + wordData.getText());
				System.out.println("      �P��̃X�R�A : " + wordData.getScore());
				System.out.println("      �P��̃J�e�S�� : "
						+ wordData.getCategory());

				System.out.println("      �P��̈�`��̏o�� :");
				RecognizeShapeData shapeData = wordData.getShape();
				ArrayList<RecognizePointData> pointList = shapeData
						.getPoint();
				if (pointList == null) {
					continue;
				}
				System.out
						.println("        ���_�̐� : " + shapeData.getCount());
				System.out.println("        ���_���̏o�� : "
						+ shapeData.getCount());
				for (RecognizePointData pointData : pointList) {
					System.out.println("          x���W, y���W(�s�N�Z���P��) : "
							+ pointData.getX() + ", " + pointData.getY());
				}
			}
		}

		// �G���[���b�Z�[�W�̏o��
		RecognizeMessageData message = resultData.getMessage();
		if (message != null) {
			System.out.println("�G���[���b�Z�[�W : " + message.getText());
		}
		
	}
	
}
