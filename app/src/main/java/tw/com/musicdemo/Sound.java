package tw.com.musicdemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

public class Sound {
    MediaPlayer music;
    SoundPool soundPool;
    Context context;
    int[] sourceid = {R.raw.videoplayback};
    Map<Integer,Integer> soundMap;

    public Sound(Context context){
        this.context = context;
        initMusic();
        initSound();
    }

    public void initSound() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,10);
        soundMap = new HashMap<Integer,Integer>();
        soundMap.put(sourceid[0], soundPool.load(context,sourceid[0],1 ) );
    }

    public void initMusic() {
        music = MediaPlayer.create(context,sourceid[0]);
        music.setLooping(true);
    }
    //播放音效
    public void playSound(int resid){
        Integer sid = soundMap.get(resid);
        if(sid !=null){
            soundPool.play(sid,1,9,1,1,1);
        }

    }

    public void setMusicStatus(boolean status){
        if (status){
            music.start();
        }else{
            music.stop();
        }
    }
    //不播了 釋放資源
    public void release(){
        music.release();
        soundPool.release();
        soundMap.clear();
    }





}
