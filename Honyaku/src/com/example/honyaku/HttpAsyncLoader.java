package com.example.honyaku;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class HttpAsyncLoader extends AsyncTaskLoader<String> {

    private String url = null; // WebAPI��URL

    public HttpAsyncLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public String loadInBackground() {
    	
    	HttpClient httpClient = new DefaultHttpClient();
    	
    	 try {
    	        String responseBody = httpClient.execute(new HttpGet(this.url), // �i1�j
    	        		

    	            // UTF-8�Ńf�R�[�h���邽��handleResponse���I�[�o�[���C�h���� �i2�j
    	            new ResponseHandler<String>() {

    	                @Override
    	                public String handleResponse(HttpResponse response)
    	                    throws ClientProtocolException, IOException {

    	                    // ���X�|���X�R�[�h���A
    	                    // HttpStatus.SC_OK�iHTTP 200�j�̏ꍇ�̂݁A���ʂ�Ԃ�
    	                    if (HttpStatus.SC_OK ==
    	                        response.getStatusLine().getStatusCode()){
    	                            return EntityUtils.toString(
    	                                response.getEntity(), "UTF-8"); // �i3�j
    	                    }
    	                    return null;
    	                }
    	            });
    	        Log.e(this.getClass().getSimpleName(), responseBody);
    	        return responseBody;
    	    }
    	    catch (Exception e) {
    	        Log.e(this.getClass().getSimpleName(),e.getMessage());
    	    }
    	    finally {
    	        // �ʐM�I�����́A�ڑ������ �i4�j
    	        httpClient.getConnectionManager().shutdown();
    	    }
    	    return null;
    }
}