package com.va.alienvest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by test on 14.11.13.
 */
    /*----------------------------------------DB--------------------------------------------------*/
class DBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "myLog";

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
