package com.va.alienvest;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;


/**
 * Created by test on 29.10.13.
 */
public class FragmentRadio extends Fragment implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    MediaPlayer mediaPlayer;

    DBHelper dbHelper;
    SQLiteDatabase db;

    Spinner spinnerCountry;
    Spinner spinnerGenre;
    Spinner spinnerLanguage;

    Switch switchFavorite;

    ListView lvStation;

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

        spinnerCountry = (Spinner)view.findViewById(R.id.country);
        spinnerGenre = (Spinner)view.findViewById(R.id.genre);
        spinnerLanguage = (Spinner)view.findViewById(R.id.language);

        switchFavorite = (Switch)view.findViewById(R.id.switch1);
        lvStation = (ListView)view.findViewById(R.id.station);

//        Button countryBtn = (Button)view.findViewById(R.id.button);
//        Button genreBtn = (Button)view.findViewById(R.id.button2);
//        Button languageBtn = (Button)view.findViewById(R.id.button3);
//
//        String countryData = getActivity().getIntent().getStringExtra("country");
//        String genreData = getActivity().getIntent().getStringExtra("genre");
//        String languageData = getActivity().getIntent().getStringExtra("language");
//
//        if (countryData != null)
//            countryBtn.setText(countryData);
//
//        if (genreData != null)
//            genreBtn.setText(genreData);
//
//        if (languageData != null)
//            languageBtn.setText(languageData);
//
//
//        countryBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), Option.class);
//                intent.putExtra("option", "country");
//                startActivity(intent);
//            }
//        });
//
//        genreBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), Option.class);
//                intent.putExtra("option", "genre");
//                startActivity(intent);
//            }
//        });
//
//        languageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), Option.class);
//                intent.putExtra("option", "language");
//                startActivity(intent);
//            }
//        });


        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(getActivity());
        // подключаемся к БД
        db = dbHelper.getWritableDatabase();

        //dbHelper.onUpgrade(db, 1, 2);

        // делаем запрос всех данных из таблицы, получаем Cursor
        Cursor cCountry = db.query("tblCountry", null, null, null, null, null, null);
        Cursor cGenre = db.query("tblGenre", null, null, null, null, null, null);
        Cursor cLanguage = db.query("tblLanguage", null, null, null, null, null, null);

        //создаем массивы для адаптеров
        String[] dateCountry = new String[cCountry.getCount()];
        String[] dateGenre = new String[cGenre.getCount()];
        String[] dateLanguage = new String[cLanguage.getCount()];


        // определяем номера столбцов по имени в выборке
        int nameColIndexCountry = cCountry.getColumnIndex("country");
        int nameColIndexGenre = cGenre.getColumnIndex("genre");
        int nameColIndexLanguage = cLanguage.getColumnIndex("language");

        int index = 0;
        // ставим позицию курсора на первую строку выборки
        cGenre.moveToFirst();
        do {
            //заполняем массив данных данными из табл.
            dateGenre[index] = cGenre.getString(nameColIndexGenre);
            index++;
        } while (cGenre.moveToNext());

        index = 0;
        cCountry.moveToFirst();
        do {
            dateCountry[index] = cCountry.getString(nameColIndexCountry);
            index++;
        } while (cCountry.moveToNext());

        index = 0;
        cLanguage.moveToFirst();
        do {
            dateLanguage[index] = cLanguage.getString(nameColIndexLanguage);
            index++;
        } while (cLanguage.moveToNext());


        ArrayAdapter<String> adapterGenre = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, dateGenre);
        spinnerGenre.setAdapter(adapterGenre);
        ArrayAdapter<String> adapterCountry = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, dateCountry);
        spinnerCountry.setAdapter(adapterCountry);
        ArrayAdapter<String> adapterLanguage = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, dateLanguage);
        spinnerLanguage.setAdapter(adapterLanguage);

        cGenre.close();
        cCountry.close();
        cLanguage.close();
    /*--------------------------------------устанавливаем обработчик нажатия y lvStation----------*/
        lvStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView tvText = (TextView) view.findViewById(android.R.id.text1);
                String query = "SELECT sourceURL FROM tblStation WHERE title = '" + tvText.getText().toString() + "'";

                Cursor cSourceURL = db.rawQuery(query, null);
                String dateSourceURL;
                cSourceURL.moveToFirst();
                dateSourceURL = cSourceURL.getString(cSourceURL.getColumnIndex("sourceURL"));
                cSourceURL.close();

                playStation(dateSourceURL);
            }
        });

    /*-------------------------------------устанавливаем обработчик нажатия y downbox-ов----------*/
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setLvStation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    /*----------------------------------favorite оброботчик switcher------------------------------*/
        switchFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setLvStation();
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

        int spinnerGenrePosition;
        int spinnerCountryPosition;
        int spinnerLanguagePosition;
        boolean favoriteChecked;

        spinnerGenrePosition = spinnerGenre.getSelectedItemPosition();
        spinnerCountryPosition = spinnerCountry.getSelectedItemPosition();
        spinnerLanguagePosition = spinnerLanguage.getSelectedItemPosition();
        favoriteChecked = switchFavorite.isChecked();

//        spinnerCountryPosition++;
//        spinnerGenrePosition++;
//        spinnerLanguagePosition++;

        if (spinnerGenrePosition == 0 && spinnerCountryPosition != 0  && spinnerLanguagePosition == 0 && favoriteChecked == false) {
            selection = "id_country = ? ";
            selectionArgs = new String[] { Integer.toString(spinnerCountryPosition) };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition == 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition != 0  && favoriteChecked == false) {
            selection = "id_language = ? ";
            selectionArgs = new String[] { Integer.toString(spinnerLanguagePosition) };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition == 0  && favoriteChecked == false) {
            selection = "id_genre = ? ";
            selectionArgs = new String[] { Integer.toString(spinnerGenrePosition) };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition != 0 && spinnerLanguagePosition == 0  && favoriteChecked == false) {
            selection = "id_genre = " + spinnerGenrePosition + " and id_country = " + spinnerCountryPosition;
            cStation = db.query("tblStation", null, selection, null, null, null, null);
        }

        else if (spinnerGenrePosition == 0 && spinnerCountryPosition != 0 && spinnerLanguagePosition != 0  && favoriteChecked == false) {
            selection = "id_language = " + spinnerLanguagePosition + " and id_country = " + spinnerCountryPosition;
            cStation = db.query("tblStation", null, selection, null, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition != 0  && favoriteChecked == false) {
            selection = "id_genre = " + spinnerGenrePosition  + " and id_language = " + spinnerLanguagePosition;
            cStation = db.query("tblStation", null, selection, null, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition != 0 && spinnerLanguagePosition != 0  && favoriteChecked == false) {
            selection = "id_genre = " + spinnerGenrePosition  + " and id_language = " + spinnerLanguagePosition + " and id_country = " + spinnerCountryPosition;
            cStation = db.query("tblStation", null, selection, null, null, null, null);
        }

        else if (spinnerGenrePosition == 0 && spinnerCountryPosition != 0  && spinnerLanguagePosition == 0 && favoriteChecked == true) {
            selection = "id_country = ? AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerCountryPosition), (new Boolean(favoriteChecked).toString())};
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition == 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition != 0  && favoriteChecked == true) {
            selection = "id_language = ?  AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerLanguagePosition), (new Boolean(favoriteChecked).toString()) };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition == 0  && favoriteChecked == true) {
            selection = "id_genre = ?  AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerGenrePosition), (new Boolean(favoriteChecked).toString())  };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition != 0 && spinnerLanguagePosition == 0  && favoriteChecked == true) {
            selection = "id_genre = ? AND id_country = ?  AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerGenrePosition), Integer.toString(spinnerCountryPosition), (new Boolean(favoriteChecked).toString())  };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition == 0 && spinnerCountryPosition != 0 && spinnerLanguagePosition != 0  && favoriteChecked == true) {
            selection = "id_language = ? AND id_country = ?  AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerLanguagePosition), Integer.toString(spinnerCountryPosition), (new Boolean(favoriteChecked).toString())  };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition != 0  && favoriteChecked == true) {
            selection = "id_genre = ? AND id_language = ?  AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerGenrePosition), Integer.toString(spinnerLanguagePosition), (new Boolean(favoriteChecked).toString())  };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition != 0 && spinnerLanguagePosition != 0  && favoriteChecked == true) {
            selection = "id_genre = ? AND  id_country = ?  AND  id_language = ?  AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerGenrePosition),  Integer.toString(spinnerCountryPosition), Integer.toString(spinnerLanguagePosition), (new Boolean(favoriteChecked).toString())};
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);

        }

        else if (spinnerGenrePosition == 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition == 0  && favoriteChecked == true) {
            selection = "favorite = ?";
            selectionArgs = new String[] { new Boolean(favoriteChecked).toString()};
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else
            cStation = db.query("tblStation", null, null, null, null, null, null);

        String[] dateStation = new String[cStation.getCount()];
        String[] dateFavorite = new String[cStation.getCount()];

        // определяем номера столбцов по имени в выборке
        int nameColIndexStation = cStation.getColumnIndex("title");
        int nameColIndexFavorite = cStation.getColumnIndex("favorite");


        int index = 0;
        // ставим позицию курсора на первую строку выборки
        if (cStation.moveToFirst()) {
            do {
                //заполняем массив данных данными из табл.
                dateStation[index] = cStation.getString(nameColIndexStation);
                dateFavorite[index] = cStation.getString(nameColIndexFavorite);
                index++;
            } while (cStation.moveToNext());

            // создаем адаптер
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_multichoice, dateStation);
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
                    "'id_genre' INTEGER NOT NULL ," +
                    "'id_language' INTEGER NOT NULL ," +
                    "'title' NVARCHAR NOT NULL," +
                    "'description' NVARCHAR NOT NULL," +
                    "'sourceURL' NVARCHAR NOT NULL , " +
                    "'favorite' BOOL NOT NULL  DEFAULT 'false');");

            db.execSQL("CREATE TABLE 'tblGenre' ('_id' INTEGER PRIMARY KEY  NOT NULL," +
                    "'genre' TEXT NOT NULL );");

            db.execSQL("CREATE TABLE 'tblLanguage' ('_id' INTEGER PRIMARY KEY  NOT NULL," +
                    "'language' TEXT NOT NULL );");

            db.execSQL("insert into tblGenre values(null, 'Any');");
            db.execSQL("insert into tblGenre values(null, 'Pop');");
            db.execSQL("insert into tblGenre values(null, 'Rock');");
            db.execSQL("insert into tblGenre values(null, 'D&B');");

            db.execSQL("insert into tblCountry values(null, 'Any');");
            db.execSQL("insert into tblCountry values(null, 'Russian Federation');");
            db.execSQL("insert into tblCountry values(null, 'USA');");
            db.execSQL("insert into tblCountry values(null, 'Germany');");

            db.execSQL("insert into tblLanguage values(null, 'Any');");
            db.execSQL("insert into tblLanguage values(null, 'Russian');");
            db.execSQL("insert into tblLanguage values(null, 'English');");
            db.execSQL("insert into tblLanguage values(null, 'German');");

            db.execSQL("insert into tblStation values(null, 1, 1, 1, 'RADIO RECORD', 'Description', 'http://air.radiorecord.ru:8101/rr_128', 'true');");
            db.execSQL("insert into tblStation values(null, 1, 2, 2, 'TRANCEMISSION', 'Description', 'http://air.radiorecord.ru:8102/tm_128', 'false');");
            db.execSQL("insert into tblStation values(null, 1, 3, 3, 'PIRATE STATION', 'Description', 'http://air.radiorecord.ru:8102/ps_128', 'false');");
            db.execSQL("insert into tblStation values(null, 2, 1, 1, 'VIP MIX', 'Description', 'http://air.radiorecord.ru:8102/vip_128', 'true');");
            db.execSQL("insert into tblStation values(null, 2, 2, 1, 'TEODOR HARDSTYLE', 'Description', 'http://air.radiorecord.ru:8102/teo_128', 'false');");
            db.execSQL("insert into tblStation values(null, 2, 3, 3, 'RECORD DANCECORE', 'Description', 'http://air.radiorecord.ru:8102/dc_128', 'false');");
            db.execSQL("insert into tblStation values(null, 3, 1, 1, 'RECORD BREAKS', 'Description', 'http://air.radiorecord.ru:8102/brks_128', 'true');");
            db.execSQL("insert into tblStation values(null, 3, 2, 2, 'RECORD CHILL-OUT', 'Description', 'http://air.radiorecord.ru:8102/chil_128', 'false');");
            db.execSQL("insert into tblStation values(null, 3, 3, 3, 'RECORD URBAN', 'Description', 'http://air.radiorecord.ru:8102/dub_128', 'false');");
            db.execSQL("insert into tblStation values(null, 1, 1, 1, 'СУПЕРДИСКОТЕКА 90-Х', 'Description', 'http://air.radiorecord.ru:8102/sd90_128', 'true');");
            db.execSQL("insert into tblStation values(null, 1, 2, 2, 'RECORD CLUB', 'Description', 'http://air.radiorecord.ru:8102/club_128', 'false');");
            db.execSQL("insert into tblStation values(null, 1, 3, 3, 'МЕДЛЯК FM', 'Description', 'http://air.radiorecord.ru:8102/mdl_128', 'false');");
            db.execSQL("insert into tblStation values(null, 2, 1, 3,'ГОП FM', 'Description', 'http://air.radiorecord.ru:8102/gop_128', 'true');");
            db.execSQL("insert into tblStation values(null, 3, 2, 2, 'PUMP N KLUBB', 'Description', 'http://air.radiorecord.ru:8102/pump_128', 'false');");
            db.execSQL("insert into tblStation values(null, 3, 3, 1, 'RUSSIAN MIX', 'Description', 'http://air.radiorecord.ru:8102/rus_128', 'false');");
            db.execSQL("insert into tblStation values(null, 2, 2, 1, 'YO! FM', 'Description', 'http://air.radiorecord.ru:8102/yo_128', 'true');");
            db.execSQL("insert into tblStation values(null, 1, 3, 2, 'RECORD DEEP', 'Description', 'http://air.radiorecord.ru:8102/deep_128', 'false');");
            db.execSQL("insert into tblStation values(null, 2, 4, 1, 'RECORD TRAP', 'Description', 'http://air.radiorecord.ru:8102/trap_128', 'false');");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, "--- upDate database ---");
        }
    }
}