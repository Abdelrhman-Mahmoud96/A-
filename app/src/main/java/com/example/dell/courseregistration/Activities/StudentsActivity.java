package com.example.dell.courseregistration.Activities;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;

import android.support.v7.widget.SearchView;
import android.widget.Toast;


import com.example.dell.courseregistration.Adapters.ListCursorAdapter;

import com.example.dell.courseregistration.Database.DatabaseContract.BaseContract;
import com.example.dell.courseregistration.R;



public class StudentsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ListCursorAdapter listCursorAdapter ;
    int STUDENT_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentsActivity.this,EditActivity.class);
                startActivity(intent);
            }
        });


        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(STUDENT_LOADER_ID, null,this );
        ListView studentsList = findViewById(R.id.studentsList);
        listCursorAdapter = new ListCursorAdapter(this,null,0);
        studentsList.setAdapter(listCursorAdapter);
        studentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(StudentsActivity.this,String.valueOf(id) ,Toast.LENGTH_SHORT ).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.delete_all,menu );

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_student).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(),SearchActivity.class)));

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               if(!hasFocus)
               {
                   menu.findItem(R.id.search_student).collapseActionView();
               }

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.deleteAll :
                deleteAll();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
            BaseContract.COLUMN_ID,
            BaseContract.COLUMN_NAME,
            BaseContract.COLUMN_IMAGE,
            BaseContract.COLUMN_COURSES,
            BaseContract.COLUMN_EMAIL,
            BaseContract.COLUMN_PHONE,
            BaseContract.COLUMN_GRADE
        };
        CursorLoader loader = new CursorLoader(this, BaseContract.CONTENT_URI,projection,
                null,null,null);
            return loader;



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

    private void deleteAll() {

        getContentResolver().delete(BaseContract.CONTENT_URI,null ,null );
    }


}
