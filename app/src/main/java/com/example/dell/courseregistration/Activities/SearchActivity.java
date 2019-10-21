package com.example.dell.courseregistration.Activities;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;


import android.widget.ListView;
import android.widget.Toast;


import com.example.dell.courseregistration.Adapters.ListCursorAdapter;

import com.example.dell.courseregistration.Adapters.SearchCursorAdapter;
import com.example.dell.courseregistration.Database.DatabaseContract.BaseContract;
import com.example.dell.courseregistration.Database.DatabaseManger;
import com.example.dell.courseregistration.Database.DatabaseProvider;
import com.example.dell.courseregistration.R;



public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    SearchCursorAdapter listCursorAdapter ;
    int SEARCH_LOADER_ID = 3;
    String query = "" ;
    CursorLoader searchLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(SEARCH_LOADER_ID, null,this );
        ListView studentsList = findViewById(R.id.searchList);
        listCursorAdapter = new SearchCursorAdapter(this,null,0);
        studentsList.setAdapter(listCursorAdapter);

//        handelIntent(getIntent());

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        Intent intent = getIntent() ;

        if(Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            query = intent.getStringExtra(SearchManager.QUERY);
            String[] projection = {
                    BaseContract.COLUMN_ID,
                    BaseContract.COLUMN_NAME,
                    BaseContract.COLUMN_IMAGE,
                    BaseContract.COLUMN_COURSES,
                    BaseContract.COLUMN_EMAIL,
                    BaseContract.COLUMN_PHONE,
                    BaseContract.COLUMN_GRADE};


            String selection = BaseContract.COLUMN_NAME +" like '%"+ query;
            String[] selectionArgs = {query};
            CursorLoader searchLoader = new CursorLoader(this, BaseContract.CONTENT_URI,projection,
                    selection,null,null);



            return searchLoader;

        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        listCursorAdapter.swapCursor(data);
        listCursorAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        listCursorAdapter.swapCursor(null);
    }




}
