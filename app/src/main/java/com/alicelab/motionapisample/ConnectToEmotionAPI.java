package com.alicelab.motionapisample;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sansuke05 on 2017/07/29.
 */

public class ConnectToEmotionAPI extends AsyncTask<Void, Void, JSONObject> {

    private MainActivity main_;
    private ProgressDialog progressDialog_ = null;

    public ConnectToEmotionAPI(MainActivity main){
        super();
        main_ = main;
    }

    @Override
    protected void onPreExecute() {

        // 進捗ダイアログを開始
        progressDialog_ = new ProgressDialog(main_);
        progressDialog_.setMessage("Now Loading ...");
        progressDialog_.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog_.setCancelable(true);
        progressDialog_.show();
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        HttpURLConnection con = null;
        URL url = null;
        String urlStr = "https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize";
        String key = "a15cbe969cd14719b199b57d4a4c57c6";
        DataOutputStream os = null;
        BufferedReader reader;
        JSONObject json = null;

        try {
            url = new URL(urlStr);
            con = (HttpURLConnection)url.openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(20000);
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/octet-stream");
            con.setRequestProperty("Ocp-Apim-Subscription-Key", key);

            // make request body
            Resources r = main_.getResources();
            Bitmap bmp = BitmapFactory.decodeResource(r, R.drawable.face_small);

            byte[] byteArray;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byteArray = bos.toByteArray();

            os = new DataOutputStream(con.getOutputStream());
            for(int i =  0 ; i < byteArray.length;i++){
                os.writeByte(byteArray[i]);
            }

            con.connect();
            os.close();

            int status = con.getResponseCode();

            switch (status) {
                case HttpURLConnection.HTTP_OK:
                    InputStream in = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    String readStr = new String();

                    while (null != (line = reader.readLine())){
                        readStr += line;
                    }
                    Log.d("EmotionAPI","read string: " + readStr);

                    in.close();

                    json = new JSONArray(readStr).getJSONObject(0);
                    break;

                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    break;
                default:
                    break;
            }

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);

        String str = "";
        try {
            str = result.getJSONObject("scores").getString("happiness");
        } catch (Exception e){
            e.printStackTrace();
        }
        main_.imageView.setImageResource(R.drawable.face_sample);

        Log.d("EmotionAPI","happiness score: " + str);

        if (isSmile(str)) {
            main_.emotion_txt.setText("素敵な笑顔です");
        } else {
            main_.emotion_txt.setText("笑顔ではありません");
        }

        if (progressDialog_.isShowing()) {

            // 進捗ダイアログを終了
            progressDialog_.dismiss();
        }

    }

    public boolean isSmile(String strValue){

        double value = Double.parseDouble(strValue);
        if (value > 0.5) return true;
        else return false;
    }
}
