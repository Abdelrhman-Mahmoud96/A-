package com.example.dell.courseregistration.Database;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.dell.courseregistration.Activities.SearchActivity;
import com.example.dell.courseregistration.Database.DatabaseContract.BaseContract;


public class DatabaseProvider extends ContentProvider {


    static final int STUDENT_ID = 101;
    static final int STUDENT_NAME = 102;
    static final int STUDENTS = 100;
    DatabaseManger databaseManger ;
    SQLiteDatabase database ;
    static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(BaseContract.CONTENT_AUTHORITY,BaseContract.TABLE_NAME ,STUDENTS );
        uriMatcher.addURI(BaseContract.CONTENT_AUTHORITY,BaseContract.TABLE_NAME+"/#" ,STUDENT_ID );
        uriMatcher.addURI(BaseContract.CONTENT_AUTHORITY,BaseContract.TABLE_NAME+"/#",STUDENT_NAME );

    }
    @Override
    public boolean onCreate() {
        databaseManger = new DatabaseManger(getContext());
          return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        int match = uriMatcher.match(uri);
        database = databaseManger.getReadableDatabase();
        Cursor cursor;
        projection = new String[]{BaseContract.COLUMN_ID,
                BaseContract.COLUMN_NAME,
                BaseContract.COLUMN_GRADE,
                BaseContract.COLUMN_IMAGE,
                BaseContract.COLUMN_COURSES,
                BaseContract.COLUMN_PHONE,
                BaseContract.COLUMN_EMAIL };

        switch (match)
        {
            case STUDENTS :
              cursor =  database.query(BaseContract.TABLE_NAME,projection ,null ,
                        null,null ,null , sortOrder);
              break;

            case STUDENT_ID:
                selection = BaseContract.COLUMN_ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(BaseContract.TABLE_NAME,projection ,
                        selection,selectionArgs ,null ,null ,
                        sortOrder);
                break;

            case STUDENT_NAME:
                selection = BaseContract.COLUMN_NAME+"=?";
                cursor = database.query(BaseContract.TABLE_NAME,projection ,
                        selection,selectionArgs ,null ,null ,
                        sortOrder);
                break;

            default:
               throw new IllegalArgumentException("UNKNOWN URI "+uri+ " with match " + match);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri );
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int match = uriMatcher.match(uri);
        switch (match)
        {
            case STUDENTS :
                return BaseContract.LIST_TYPE;
            case STUDENT_ID:
                return BaseContract.ITEM_TYPE;
            default:
                throw new IllegalArgumentException("UNKNOWN URI "+uri+ " with match " + match);

        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int match = uriMatcher.match(uri);
        database = databaseManger.getWritableDatabase();
        long id;
        switch (match)
        {
            case STUDENTS:
                if (values.getAsString(BaseContract.COLUMN_NAME) == null)
                {
                    throw new IllegalArgumentException("Name can't be empty");
                }
                if (values.getAsString(BaseContract.COLUMN_COURSES) == null)
                {
                    throw new IllegalArgumentException("courses can't be empty");
                }
                if (values.getAsString(BaseContract.COLUMN_GRADE) == null)
                {
                    throw new IllegalArgumentException("grade can't be empty");
                }
                if (values.getAsString(BaseContract.COLUMN_PHONE) == null)
                {
                    throw new IllegalArgumentException("phone can't be empty");
                }
                id = database.insert(BaseContract.TABLE_NAME,null ,values );
                 if (id == -1)
                 {
                     Log.e("INSERT ERROR :","failed to insert new student check the info " );
                     return null;
                 }
                 break;

            default:
                 throw new IllegalArgumentException("UNKNOWN URI "+uri+ " with match " + match);
        }
        getContext().getContentResolver().notifyChange(uri,null );
        return ContentUris.withAppendedId(uri,id );
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match = uriMatcher.match(uri);
        int id;
        switch (match)
        {
            case STUDENTS:
                id = database.delete(BaseContract.TABLE_NAME,selection ,selectionArgs );
                getContext().getContentResolver().notifyChange(uri,null );
                return id;

            case STUDENT_ID:
                selection = BaseContract.COLUMN_ID+"=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                id =  database.delete(BaseContract.TABLE_NAME,selection ,selectionArgs );
                getContext().getContentResolver().notifyChange(uri,null );
                return id;


               default:
                throw new IllegalArgumentException("UNKNOWN URI "+uri+ " with match " + match);

        }



    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match = uriMatcher.match(uri);
        database = databaseManger.getWritableDatabase();

        switch (match)
        {
            case STUDENT_ID:
                selection = BaseContract.COLUMN_ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateStudent(uri,values ,selection ,selectionArgs );
            case STUDENTS:
               return updateStudent(uri,values ,null ,null );

            default:
                throw new IllegalArgumentException("UNKNOWN URI "+uri+ " with match " + match);
        }



    }

    private int updateStudent(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        if (values.getAsString(BaseContract.COLUMN_NAME) == null)
        {
            throw new IllegalArgumentException("Name can't be empty");
        }
        if (values.getAsString(BaseContract.COLUMN_COURSES) == null)
        {
            throw new IllegalArgumentException("courses can't be empty");
        }
        if (values.getAsString(BaseContract.COLUMN_GRADE) == null)
        {
            throw new IllegalArgumentException("grade can't be empty");
        }
        if (values.getAsString(BaseContract.COLUMN_PHONE) == null)
        {
            throw new IllegalArgumentException("phone can't be empty");
        }
        if (values.size() == 0)
        {
            return 0;
        }

        getContext().getContentResolver().notifyChange(uri,null );
        return database.update(BaseContract.TABLE_NAME,values ,selection,selectionArgs );
    }
}
