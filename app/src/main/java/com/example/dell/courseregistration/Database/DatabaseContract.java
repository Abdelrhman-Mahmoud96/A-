package com.example.dell.courseregistration.Database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public DatabaseContract(){}

    public static class BaseContract
    {
        public static final String TABLE_NAME = "students";
        /*Columns name*/
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_COURSES = "courses";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_GRADE =  "grade";

        /* Content Uri*/
        public static final String CONTENT_AUTHORITY = "com.example.dell.courseregistration";
        public static final String CONTENT_BASE = "content://"+ CONTENT_AUTHORITY;
        public static final Uri BASE_URI = Uri.parse(CONTENT_BASE);
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,TABLE_NAME );

        public static final String LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

    }

}
