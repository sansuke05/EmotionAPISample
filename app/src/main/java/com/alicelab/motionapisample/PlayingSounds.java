package com.alicelab.motionapisample;

import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by user on 2017/07/29.
 */

public class PlayingSounds {

    private MainActivity main_;
    private String filename_;
    private SoundPool mSoundPool;
    private int sound_id;

    public PlayingSounds(MainActivity main, String filename, SoundPool pool){
        main_ = main;
        filename_ = filename;
        mSoundPool = pool;
    }

    public void play(){
        Resources res = main_.getResources();
        int mediaId = res.getIdentifier(filename_, "raw", main_.getPackageName());

        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (0 == status){
                    //再生
                    mSoundPool.play(sound_id, 1.0F, 1.0F, 0, 0, 1.0F);
                }
            }
        });

        sound_id = mSoundPool.load(main_,mediaId,1);
    }
}
