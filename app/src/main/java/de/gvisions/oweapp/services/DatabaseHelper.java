package de.gvisions.oweapp.services;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import de.gvisions.oweapp.R;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    private final static String DB_NAME = "oweapp";
    private final static int DB_VERSION = 30;

    public DatabaseHelper(Context fragmentList) {
        super(
                fragmentList,
                DB_NAME,
                null,
                DB_VERSION);
        this.context=fragmentList;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

            db.execSQL("      create table owe(\n" +
                    "        id integer primary key autoincrement,\n" +
                    "        contacturi varchar(100),\n" +
                    "        what varchar(500),\n" +
                    "        fromto varchar(500),\n" +
                    "        desc varchar(999),\n" +
                    "        type varchar(50),\n" +
                    "        deadline varchar(50)\n" +
                    "      )");

    }

    public void onUpgrade(SQLiteDatabase db, int VOld, int VNew) {
        Log.d("DB_DEBUG", String.valueOf(VNew));
        switch(VNew)
        {


            default: break;
        }

    }

}