package com.va.alienvest;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

public class FullImageActivity extends Activity {
    /**
     * Created by Василий on 27.08.13.
     */
    private static MediaPlayer mp;
    Button play_button, pause_button;
    SeekBar seek_bar;
    TextView text_shown;
    TextView text_name_song;
    Handler seekHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);
        getInit();
        seekUpdation();
    }

    public void getInit() {

        seek_bar = (SeekBar)findViewById(R.id.seekBar);
        text_shown = (TextView)findViewById(R.id.legend);
        text_name_song = (TextView)findViewById(R.id.name_song);
        play_button = (Button) findViewById(R.id.play);
        pause_button = (Button) findViewById(R.id.pause);

        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });

        // get intent data
        Intent intent = getIntent();
        // Selected image id
        int position = intent.getExtras().getInt("id");
        ImageAdapter imageAdapter = new ImageAdapter(this);
        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        imageView.setImageResource(imageAdapter.mThumbIds[position]);

        switch (position){
            case 0:
                text_name_song.setText(R.string.chushie_ludi);
                text_shown.setText(R.string.chushie_ludi_legend);
                mp = MediaPlayer.create(this, R.raw.chushie_ludi);
                break;
            case 1:
                text_name_song.setText(R.string.groza);
                text_shown.setText(R.string.groza_legend);
                mp = MediaPlayer.create(this, R.raw.groza);
                break;
            case 2:
                text_name_song.setText(R.string.inoplanetynin);
                text_shown.setText(R.string.inoplanetynin_legend);
                mp = MediaPlayer.create(this, R.raw.inoplanetynin);
                break;
            case 3:
                text_name_song.setText(R.string.kukla_vudu);
                text_shown.setText(R.string.kukla_vudu_legend);
                mp = MediaPlayer.create(this, R.raw.kukla_vudu);
                break;
            case 4:
                text_name_song.setText(R.string.molitva);
                text_shown.setText(R.string.molitva_legend);
                mp = MediaPlayer.create(this, R.raw.molitva);
                break;
            case 5:
                text_name_song.setText(R.string.teni);
                text_shown.setText(R.string.teni_legend);
                mp = MediaPlayer.create(this, R.raw.teni);
                break;
            case 6:
                text_name_song.setText(R.string.voda_i_plamy);
                text_shown.setText(R.string.voda_i_plamy_legend);
                mp = MediaPlayer.create(this, R.raw.voda_i_plamy);
                break;
            case 7:
                text_name_song.setText(R.string.zorka);
                text_shown.setText(R.string.zorka_legend);
                mp= MediaPlayer.create(this, R.raw.zorka);
                break;
            default:
                Toast.makeText(this, "Ошибка открытия файла", Toast.LENGTH_LONG).show();
                break;
        }

    mp.start();
    seek_bar.setMax(mp.getDuration());
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    public void seekUpdation() {
        //seek_bar.setProgress(seek_bar.getProgress()+1);
        seek_bar.setProgress(mp.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
    }

    @Override
    public void onBackPressed () {
        mp.stop();
        this.finish();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play:
                if(!mp.isPlaying())
                    mp.start();
                break;
            case R.id.pause:
                if(mp.isPlaying())
                    mp.pause();
                break;
            case R.id.backward:
                mp.seekTo(mp.getCurrentPosition() - 10000);
                break;
            case R.id.forward:
                mp.seekTo(mp.getCurrentPosition() + 10000);
                break;
        }
    }
}