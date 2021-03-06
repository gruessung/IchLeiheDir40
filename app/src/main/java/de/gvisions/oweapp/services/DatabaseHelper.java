package de.gvisions.oweapp.services;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.gvisions.oweapp.R;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    private final static String DB_NAME = "oweapp";
    private final static int DB_VERSION = 33;

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
                    "        deadline varchar(50),\n" +
                    "        foto varchar(500),\n" +
                    "        erstellt varchar(50)\n" +
                    "      )");

    }

    public void onUpgrade(SQLiteDatabase db, int VOld, int VNew) {
        Log.d("DB_DEBUG", String.valueOf(VNew));
        switch(VNew)
        {
            case 31:
                db.execSQL("alter table owe add column foto varchar(500) default 0");
                break;
            case 33:
                db.execSQL("alter table owe add column erstellt varchar(50) default 0");
                break;


            default: break;
        }

    }



}