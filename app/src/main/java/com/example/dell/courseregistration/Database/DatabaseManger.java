package com.example.dell.courseregistration.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.dell.courseregistration.Database.DatabaseContract.BaseContract;

public class DatabaseManger extends SQLiteOpenHelper {

    static String DATABASE_NAME = "Students.db";
    static int VERSION = 1;

    public DatabaseManger(Context context) {
        super(context, DATABASE_NAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String DatabaseQuery = "CREATE TABLE "  + BaseContract.TABLE_NAME +" ( "
                                                + BaseContract.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                + BaseContract.COLUMN_NAME +" STRING NOT NULL, "
                                                + BaseContract.COLUMN_IMAGE +" BLOB, "
                                                + BaseContract.COLUMN_COURSES +" STRING NOT NULL, "
                                                + BaseContract.COLUMN_GRADE + " STRING NOT NULL, "
                                                + BaseContract.COLUMN_PHONE + " STRING, "
                                                + BaseContract.COLUMN_EMAIL + " STRING );";
        db.execSQL(DatabaseQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
