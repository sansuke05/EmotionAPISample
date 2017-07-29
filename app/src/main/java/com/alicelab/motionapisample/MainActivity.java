package com.alicelab.motionapisample;

import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView emotion_txt;
    ImageView imageView;
    ConnectToEmotionAPI task;
    PlayingSounds ps;

    private SoundPool mSoundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);
        Button button2 = (Button)findViewById(R.id.button2);
        emotion_txt = (TextView)findViewById(R.id.emotion_text);
        imageView = (ImageView)findViewById(R.id.imageView);

        task = new ConnectToEmotionAPI(MainActivity.this);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                task.execute();
            }
        });

        ps = new PlayingSounds(MainActivity.this, "break1", mSoundPool);

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ps.play();
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mSoundPool != null) mSoundPool.release();
    }
}
