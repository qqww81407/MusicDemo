package tw.com.musicdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnPlay,btnPause,BackgroundMusic,networkMusic,nextPage;
    private SoundPool spool;
    private int sourceid;
    MainActivity context = this;
    Sound sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }

    private void findViews(){
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        BackgroundMusic = findViewById(R.id.BackgroundMusic);
        networkMusic = findViewById(R.id.networkMusic);
        nextPage = findViewById(R.id.nextPage);

        //版本確認 5.0前後有差
        //Android5.0
        if(Build.VERSION.SDK_INT>=21){

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(10);
            AudioAttributes.Builder attr = new AudioAttributes.Builder();

            attr.setLegacyStreamType(AudioManager.STREAM_MUSIC);

            builder.setAudioAttributes(attr.build());

            spool = builder.build();


        }else{
            //5.0之前
            spool = new SoundPool(8,AudioManager.STREAM_MUSIC,9);
        }

        sourceid = spool.load(this,R.raw.videoplayback,1);

        btnPlay.setOnClickListener(v -> {
            playSound(0);
        });
        btnPause.setOnClickListener(v -> {
            spool.pause(sourceid);
        });

        networkMusic.setOnClickListener(v ->{
            playUrlMusic();
        });

        BackgroundMusic.setOnClickListener(v -> {
            Sound sound = new Sound(context);
            sound.playSound(R.raw.videoplayback);
            sound.setMusicStatus(true);
        });

        nextPage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intent);
        });

    }

    protected void onDestroy() {
        super.onDestroy();
        if (sound != null){
            sound.setMusicStatus(false);
            sound.release();
        }
    }

    private void playUrlMusic() {

        MediaPlayer player = new MediaPlayer();
        try {

            player.setDataSource("https://r1---sn-3cgv-un56.googlevideo.com/videoplayback?expire=1629028822&ei=dq0YYZr3L6n93LUPxamt2Ag&ip=139.162.36.231&id=o-AGf9qGglDTH6jJfiS0lv_OCrG_KcC30px7pfUP4ZjHiR&itag=18&source=youtube&requiressl=yes&vprv=1&mime=video%2Fmp4&ns=zjPBSFpC3TGnNEDgVMlwuC0G&gir=yes&clen=17280897&ratebypass=yes&dur=286.162&lmt=1589674078364461&fexp=24001373,24007246&c=WEB&txp=5531332&n=YzB8sE-9iTEGpvthmlL&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cvprv%2Cmime%2Cns%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRQIhAKY32lzlYCivI_67YSmy_Y5iWjZcbAt9L-PPd4E_zST4AiA0DYr1OzU88wp_6Z8PBT12Og06nBd_GhlWVCJ63XyfNQ==&title=%E4%B8%80%E9%A6%96%E5%A5%BD%E8%81%BD%E7%9A%84%E6%97%A5%E8%AA%9E%E6%AD%8C%E3%80%8A%E5%A4%A9%E4%B9%8B%E5%BC%B1%E3%80%8BAkie%E7%A7%8B%E7%BB%98%E3%80%90%E4%B8%AD%E6%97%A5%E6%AD%8C%E8%A9%9ELyrics%E3%80%91&redirect_counter=1&rm=sn-npoz67e&req_id=52b4c41c53d0a3ee&cms_redirect=yes&ipbypass=yes&mh=i8&mip=123.192.160.40&mm=31&mn=sn-3cgv-un56&ms=au&mt=1629007004&mv=m&mvi=1&pl=22&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AG3C_xAwRgIhAKl92WaU7mU0IoumpXasjFxq323JvUY-L_EPFmCDEXKnAiEA2jjiIPMR9TsEJ4I9yzU1CuKZn_ETThVeaCz3Bbw7fQ4%3D");
            player.prepareAsync();
            player.setOnPreparedListener(playLis);


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    MediaPlayer.OnPreparedListener playLis = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();  //檔案已經準備好開始播放
        }
    };

    private void playSound(int playtime) {
        //播放音樂
        AudioManager am = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        //抓取最大音量
        float maxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //抓取目前的音量
        float nowVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);

        //設定左右聲道的範圍(0.0-1.0之間)
        float vol = nowVolumn/maxVolumn;
        spool.play(sourceid,vol,vol,1,playtime,1);


    }
}


