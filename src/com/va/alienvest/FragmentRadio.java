package com.va.alienvest;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


/**
 * Created by test on 29.10.13.
 */
public class FragmentRadio extends Fragment implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    MediaPlayer mediaPlayer;

    DBHelper dbHelper;
    SQLiteDatabase db;
    Spinner spinnerStyle;
    Spinner spinnerCountry;
    ListView lvStation;
    AudioManager am;
    private static final String LOG_TAG = "myLog";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {



        mediaPlayer = new MediaPlayer();

        // create ContextThemeWrapper from the original Activity Context with the custom theme
        // final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Panel);
        // clone the inflater using the ContextThemeWrapper
        // LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        // LayoutInflater view2 = inflater.cloneInContext(contextThemeWrapper);

        View view = inflater.inflate(R.layout.radio, null);

        spinnerStyle = (Spinner)view.findViewById(R.id.style);
        spinnerCountry = (Spinner)view.findViewById(R.id.country);

        View Btn1 = view.findViewById(R.id.button);
        Button Btn2 = (Button)view.findViewById(R.id.button2);
        Button Btn3 = (Button)view.findViewById(R.id.button3);

        Intent intentRadio = getActivity().getIntent();

        String countryOption = intentRadio.getStringExtra("country");
        String genreOption = intentRadio.getStringExtra("genre");
        String languageOption = intentRadio.getStringExtra("language");

        //Btn1.setText(countryOption);
        Btn2.setText(genreOption);
        Btn3.setText(languageOption);


        Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Option.class);
                intent.putExtra("option", "country");
                startActivity(intent);
                //Toast.makeText(getActivity(), "btn1", Toast.LENGTH_LONG);
            }
        });

        Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Option.class);
                intent.putExtra("option", "genre");
                startActivity(intent);
                //Toast.makeText(getActivity(), "btn2", Toast.LENGTH_LONG);
            }
        });

        Btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Option.class);
                intent.putExtra("option", "language");
                startActivity(intent);
                //Toast.makeText(getActivity(), "btn3", Toast.LENGTH_LONG);
            }
        });

        lvStation = (ListView)view.findViewById(R.id.station);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(getActivity());
        // подключаемся к БД
        db = dbHelper.getWritableDatabase();

        //dbHelper.onUpgrade(db, 1, 2);

        // делаем запрос всех данных из таблицы, получаем Cursor
        Cursor cStyle = db.query("tblStyle", null, null, null, null, null, null);
        Cursor cCountry = db.query("tblCountry", null, null, null, null, null, null);

        //создаем массивы для адаптеров
        String[] dateStyle = new String[cStyle.getCount()];
        String[] dateCountry = new String[cStyle.getCount()];

        // определяем номера столбцов по имени в выборке
        int nameColIndexStyle = cStyle.getColumnIndex("style");
        int nameColIndexCountry = cCountry.getColumnIndex("country");

        int index = 0;

        // ставим позицию курсора на первую строку выборки
        cStyle.moveToFirst();
        do {
            //заполняем массив данных данными из табл.
            dateStyle[index] = cStyle.getString(nameColIndexStyle);
            index++;
        } while (cStyle.moveToNext());

        index = 0;
        cCountry.moveToFirst();
        do {
            dateCountry[index] = cCountry.getString(nameColIndexCountry);
            index++;
        } while (cCountry.moveToNext());


        ArrayAdapter<String> adapterStyle = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, dateStyle);
        spinnerStyle.setAdapter(adapterStyle);
        ArrayAdapter<String> adapterCountry = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, dateCountry);
        spinnerCountry.setAdapter(adapterCountry);

        cStyle.close();
        cCountry.close();

    /*--------------------------------------устанавливаем обработчик нажатия y lvStation----------*/
        lvStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView tvText = (TextView) view.findViewById(android.R.id.text1);
                String query = "SELECT stream FROM tblStation WHERE station = '" + tvText.getText().toString() + "'";

                Cursor cStream = db.rawQuery(query, null);
                String dateStream;
                cStream.moveToFirst();
                dateStream = cStream.getString(cStream.getColumnIndex("stream"));
                cStream.close();

                playStation(dateStream);
            }
        });

    /*-------------------------------------устанавливаем обработчик нажатия y downbox-ов----------*/
        spinnerStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                setLvStation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg) {
            }
        });

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                setLvStation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg) {
            }
        });
    /*--------------------------------------------------------------------------------------------*/
        return view;


        // inflate the layout using the cloned inflater, not default inflater
        //return localInflater.inflate(R.layout.radio, group, false);
    }

    /*---------------------------------- по умолчанию mediaPlayer---------------------------------*/
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    /*----------------------------------------заполняем станции station---------------------------*/
    public void setLvStation() {

        Cursor cStation;
        String selection = null;
        String[] selectionArgs = null;

        int spinnerStylePosition;
        int spinnerCountryPosition;

        spinnerStylePosition = spinnerStyle.getSelectedItemPosition();
        spinnerCountryPosition = spinnerCountry.getSelectedItemPosition();

        if (spinnerStylePosition == 0 && spinnerCountryPosition == 0)
            cStation = db.query("tblStation", null, null, null, null, null, null);

        else if (spinnerStylePosition == 0 && spinnerCountryPosition != 0 ) {
            selection = "id_country = ? ";
            selectionArgs = new String[] { Integer.toString(++spinnerCountryPosition) };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerStylePosition != 0 && spinnerCountryPosition == 0 ) {
            selection = "id_style = ? ";
            selectionArgs = new String[] { Integer.toString(++spinnerStylePosition) };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else {
            ++spinnerCountryPosition;
            ++spinnerStylePosition;
            //selection = "where station = 'PIRATE STATION'";// + tvText.getText().toString();

            selection = "id_style = " + spinnerStylePosition + " and id_country = " + spinnerCountryPosition;
            cStation = db.query("tblStation", null, selection, null, null, null, null);
        }

        String[] dateStation = new String[cStation.getCount()];

        // определяем номера столбцов по имени в выборке
        int nameColIndexStation = cStation.getColumnIndex("station");

        int index = 0;
        // ставим позицию курсора на первую строку выборки
        if (cStation.moveToFirst()) {
            do {
                //заполняем массив данных данными из табл.
                dateStation[index] = cStation.getString(nameColIndexStation);
                index++;
            } while (cStation.moveToNext());

            // создаем адаптер
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, dateStation);
            lvStation.setAdapter(adapter);
        }
        else
            lvStation.setAdapter(null);

        cStation.close();
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

    /*----------------------------------------DB--------------------------------------------------*/
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "DB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");

            // создаем таблицу с полями
            db.execSQL("CREATE TABLE 'tblCountry' ('_id' INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL," +
                    "'country' TEXT NOT NULL )");

            db.execSQL("CREATE TABLE 'tblStation' ('_id' INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL, " +
                    "'id_country' INTEGER NOT NULL ," +
                    "'id_style' INTEGER NOT NULL ," +
                    "'station' NVARCHAR NOT NULL," +
                    "'stream' NVARCHAR NOT NULL , " +
                    "'favorite' BOOL NOT NULL  DEFAULT 'false');");

            db.execSQL("CREATE TABLE 'tblStyle' ('_id' INTEGER PRIMARY KEY  NOT NULL," +
                    "'style' TEXT NOT NULL );");

            db.execSQL("insert into tblStyle values(null, 'Any');");
            db.execSQL("insert into tblStyle values(null, 'Pop');");
            db.execSQL("insert into tblStyle values(null, 'Rock');");
            db.execSQL("insert into tblStyle values(null, 'D&B');");

            db.execSQL("insert into tblCountry values(null, 'Any');");
            db.execSQL("insert into tblCountry values(null, 'Russian');");
            db.execSQL("insert into tblCountry values(null, 'USA');");
            db.execSQL("insert into tblCountry values(null, 'Germany');");


            db.execSQL("insert into tblStation values(null, 1, 1, 'RADIO RECORD', 'http://air.radiorecord.ru:8101/rr_128', 'false');");
            db.execSQL("insert into tblStation values(null, 1, 2, 'TRANCEMISSION', 'http://air.radiorecord.ru:8102/tm_128', 'false');");
            db.execSQL("insert into tblStation values(null, 1, 3, 'PIRATE STATION', 'http://air.radiorecord.ru:8102/ps_128', 'false');");
            db.execSQL("insert into tblStation values(null, 2, 1, 'VIP MIX', 'http://air.radiorecord.ru:8102/vip_128', 'false');");
            db.execSQL("insert into tblStation values(null, 2, 2, 'TEODOR HARDSTYLE', 'http://air.radiorecord.ru:8102/teo_128', 'false');");
            db.execSQL("insert into tblStation values(null, 2, 3, 'RECORD DANCECORE', 'http://air.radiorecord.ru:8102/dc_128', 'false');");
            db.execSQL("insert into tblStation values(null, 3, 1, 'RECORD BREAKS', 'http://air.radiorecord.ru:8102/brks_128', 'false');");
            db.execSQL("insert into tblStation values(null, 3, 2, 'RECORD CHILL-OUT', 'http://air.radiorecord.ru:8102/chil_128', 'false');");
            db.execSQL("insert into tblStation values(null, 3, 3, 'RECORD URBAN', 'http://air.radiorecord.ru:8102/dub_128', 'false');");
            db.execSQL("insert into tblStation values(null, 1, 1, 'СУПЕРДИСКОТЕКА 90-Х', 'http://air.radiorecord.ru:8102/sd90_128', 'false');");
            db.execSQL("insert into tblStation values(null, 1, 2, 'RECORD CLUB', 'http://air.radiorecord.ru:8102/club_128', 'false');");
            db.execSQL("insert into tblStation values(null, 1, 3, 'МЕДЛЯК FM', 'http://air.radiorecord.ru:8102/mdl_128', 'false');");
            db.execSQL("insert into tblStation values(null, 2, 4, 'ГОП FM', 'http://air.radiorecord.ru:8102/gop_128', 'false');");
            db.execSQL("insert into tblStation values(null, 3, 4, 'PUMP N KLUBB', 'http://air.radiorecord.ru:8102/pump_128', 'false');");
            db.execSQL("insert into tblStation values(null, 4, 4, 'RUSSIAN MIX', 'http://air.radiorecord.ru:8102/rus_128', 'false');");
            db.execSQL("insert into tblStation values(null, 4, 2, 'YO! FM', 'http://air.radiorecord.ru:8102/yo_128', 'false');");
            db.execSQL("insert into tblStation values(null, 4, 3, 'RECORD DEEP', 'http://air.radiorecord.ru:8102/deep_128', 'false');");
            db.execSQL("insert into tblStation values(null, 4, 4, 'RECORD TRAP', 'http://air.radiorecord.ru:8102/trap_128', 'false');");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, "--- upDate database ---");
        }
    }
}
