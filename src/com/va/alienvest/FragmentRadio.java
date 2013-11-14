package com.va.alienvest;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;


/**
 * Created by test on 29.10.13.
 */
public class FragmentRadio extends Fragment implements Spinner.OnItemSelectedListener, Switch.OnCheckedChangeListener {

    DBHelper dbHelper;
    SQLiteDatabase db;
    Spinner spinnerCountry;
    Spinner spinnerGenre;
    Spinner spinnerLanguage;
    Switch switchFavorite;
    ListView lvStation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {

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

        // создаем массивы для адаптеров
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

    /*-------------------------------------устанавливаем обработчики нажатия----------------------*/
        spinnerGenre.setOnItemSelectedListener(this);
        spinnerLanguage.setOnItemSelectedListener(this);
        spinnerCountry.setOnItemSelectedListener(this);
        switchFavorite.setOnCheckedChangeListener(this);

        return view;

        // inflate the layout using the cloned inflater, not default inflater
        //return localInflater.inflate(R.layout.radio, group, false);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        setLvStation();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        setLvStation();
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

        if (spinnerGenrePosition == 0 && spinnerCountryPosition != 0  && spinnerLanguagePosition == 0 && !favoriteChecked) {
            selection = "id_country = ? ";
            selectionArgs = new String[] { Integer.toString(spinnerCountryPosition) };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition == 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition != 0  && !favoriteChecked) {
            selection = "id_language = ? ";
            selectionArgs = new String[] { Integer.toString(spinnerLanguagePosition) };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition == 0  && !favoriteChecked) {
            selection = "id_genre = ? ";
            selectionArgs = new String[] { Integer.toString(spinnerGenrePosition) };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition != 0 && spinnerLanguagePosition == 0  && !favoriteChecked) {
            selection = "id_genre = " + spinnerGenrePosition + " and id_country = " + spinnerCountryPosition;
            cStation = db.query("tblStation", null, selection, null, null, null, null);
        }

        else if (spinnerGenrePosition == 0 && spinnerCountryPosition != 0 && spinnerLanguagePosition != 0  && !favoriteChecked) {
            selection = "id_language = " + spinnerLanguagePosition + " and id_country = " + spinnerCountryPosition;
            cStation = db.query("tblStation", null, selection, null, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition != 0  && !favoriteChecked) {
            selection = "id_genre = " + spinnerGenrePosition  + " and id_language = " + spinnerLanguagePosition;
            cStation = db.query("tblStation", null, selection, null, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition != 0 && spinnerLanguagePosition != 0  && !favoriteChecked) {
            selection = "id_genre = " + spinnerGenrePosition  + " and id_language = " + spinnerLanguagePosition + " and id_country = " + spinnerCountryPosition;
            cStation = db.query("tblStation", null, selection, null, null, null, null);
        }

        else if (spinnerGenrePosition == 0 && spinnerCountryPosition != 0  && spinnerLanguagePosition == 0 && favoriteChecked) {
            selection = "id_country = ? AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerCountryPosition), (new Boolean(favoriteChecked).toString())};
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition == 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition != 0  && favoriteChecked) {
            selection = "id_language = ?  AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerLanguagePosition), (new Boolean(favoriteChecked).toString()) };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition == 0  && favoriteChecked) {
            selection = "id_genre = ?  AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerGenrePosition), (new Boolean(favoriteChecked).toString())  };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition != 0 && spinnerLanguagePosition == 0  && favoriteChecked) {
            selection = "id_genre = ? AND id_country = ?  AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerGenrePosition), Integer.toString(spinnerCountryPosition), (new Boolean(favoriteChecked).toString())  };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition == 0 && spinnerCountryPosition != 0 && spinnerLanguagePosition != 0  && favoriteChecked) {
            selection = "id_language = ? AND id_country = ?  AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerLanguagePosition), Integer.toString(spinnerCountryPosition), (new Boolean(favoriteChecked).toString())  };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition != 0  && favoriteChecked) {
            selection = "id_genre = ? AND id_language = ?  AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerGenrePosition), Integer.toString(spinnerLanguagePosition), (new Boolean(favoriteChecked).toString())  };
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else if (spinnerGenrePosition != 0 && spinnerCountryPosition != 0 && spinnerLanguagePosition != 0  && favoriteChecked) {
            selection = "id_genre = ? AND  id_country = ?  AND  id_language = ?  AND favorite = ?";
            selectionArgs = new String[] { Integer.toString(spinnerGenrePosition),  Integer.toString(spinnerCountryPosition), Integer.toString(spinnerLanguagePosition), (new Boolean(favoriteChecked).toString())};
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);

        }

        else if (spinnerGenrePosition == 0 && spinnerCountryPosition == 0 && spinnerLanguagePosition == 0  && favoriteChecked) {
            selection = "favorite = ?";
            selectionArgs = new String[] { new Boolean(favoriteChecked).toString()};
            cStation = db.query("tblStation", null, selection, selectionArgs, null, null, null);
        }

        else
            cStation = db.query("tblStation", null, null, null, null, null, null);

        String[] dateStation = new String[cStation.getCount()];
        boolean[] dateFavorite = new boolean[cStation.getCount()];

        // определяем номера столбцов по имени в выборке
        int nameColIndexStation = cStation.getColumnIndex("title");
        int nameColIndexFavorite = cStation.getColumnIndex("favorite");


        int index = 0;
        // ставим позицию курсора на первую строку выборки
        if (cStation.moveToFirst()) {
            do {
                //заполняем массив данных данными из табл.
                dateStation[index] = cStation.getString(nameColIndexStation);
                dateFavorite[index] = Boolean.parseBoolean(cStation.getString(nameColIndexFavorite));
                index++;
            } while (cStation.moveToNext());

            // создаем адаптер
            BoxAdapter adapter = new BoxAdapter(getActivity(), dateStation, dateFavorite);
            // ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.tvStation, dateStation);
            lvStation.setAdapter(adapter);
        }
        else
            lvStation.setAdapter(null);

        cStation.close();
    }
}