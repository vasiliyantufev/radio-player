package com.va.alienvest;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by test on 11.11.13.
 */
public class BoxAdapter extends BaseAdapter implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{

    MediaPlayer mediaPlayer;

    private static final String LOG_TAG = "myLog";

    private final Activity context;
    private final String[] stationValues;
    private final boolean[] favoriteValues;

    DBHelper dbHelper;
    SQLiteDatabase db;

    public BoxAdapter(Activity context, String[] stationValues, boolean[] favoriteValues) {
        this.context = context;
        this.stationValues = stationValues;
        this.favoriteValues = favoriteValues;
    }

    @Override
    public int getCount() {
        return stationValues.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup group) {

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(context);
        // подключаемся к БД
        db = dbHelper.getWritableDatabase();


        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item, group, false);

        final TextView tvStation = (TextView)rowView.findViewById(R.id.tvStation);
        ImageButton ImgBtn = (ImageButton)rowView.findViewById(R.id.btnDescription);
        CheckBox chkFavorite = (CheckBox)rowView.findViewById(R.id.chkFavorite);

        tvStation.setText(stationValues[position]);
        if (favoriteValues[position]) {
            chkFavorite.setChecked(true);
        }
        else chkFavorite.setChecked(false);

        Log.e(LOG_TAG, "lv");

        tvStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TextView tvText = (TextView) view.findViewById(android.R.id.text1);
                String query = "SELECT sourceURL FROM tblStation WHERE title = '" + tvStation.getText().toString() + "'";

                Cursor cSourceURL = db.rawQuery(query, null);
                String dateSourceURL;
                cSourceURL.moveToFirst();
                dateSourceURL = cSourceURL.getString(cSourceURL.getColumnIndex("sourceURL"));
                cSourceURL.close();

                playStation(dateSourceURL);

                Log.e(LOG_TAG, "tv");
            }
        });

        chkFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(LOG_TAG, "chk");
            }
        });

        return rowView;
    }

    /*----------------------------------------PLAY------------------------------------------------*/

    public void playStation(String station) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(station);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}