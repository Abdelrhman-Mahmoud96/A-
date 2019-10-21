package com.example.dell.courseregistration.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.courseregistration.R;
import com.example.dell.courseregistration.Database.DatabaseContract.BaseContract;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri updateUri;
    EditText nameView;
    EditText gradeView;
    EditText phoneView;
    EditText emailView;
    ImageView imageView;
    final int GALLERY_REQUEST_CODE = 200;
    int STUDENT_EDIT_LOADER = 2;
    boolean haschanged = false;
    ArrayList itemSelected = new ArrayList();
    public static String[] items = {"Math","Science","physics"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        updateUri = intent.getData();



        if(updateUri != null)
        {
            setTitle("Update Student");
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(STUDENT_EDIT_LOADER, null, this);
        }

         nameView  = findViewById(R.id.editStdName);
         gradeView = findViewById(R.id.editStdGrade);
         phoneView = findViewById(R.id.editStdPhone);
         emailView = findViewById(R.id.editStdEmail);
         imageView = findViewById(R.id.editStdImage);
         Button courses = findViewById(R.id.editCourses);

         View.OnTouchListener onTouchListener = new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 haschanged = true;
                 return false;
             }
         };


         nameView.setOnTouchListener(onTouchListener);
         gradeView.setOnTouchListener(onTouchListener);
         phoneView.setOnTouchListener(onTouchListener);
         emailView.setOnTouchListener(onTouchListener);


         FloatingActionButton addImageBtn = findViewById(R.id.addImage);

         addImageBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 haschanged = true;
                 Intent intent = new Intent(Intent.ACTION_PICK);
                 intent.setType("image/*");
                 String[] mimeTypes = {"image/jpeg", "image/png"};
                 intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);

                 startActivityForResult(intent, GALLERY_REQUEST_CODE);
             }
         });

         courses.setOnClickListener(new View.OnClickListener() {

             boolean [] checkedItems = {false,false,false};

             @Override
             public void onClick(View v) {

                 haschanged = true;

                 AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                 builder.setTitle("Select Your Courses");
                 if(itemSelected.isEmpty())
                 {
                     builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                             if(isChecked)
                             {
                                 itemSelected.add(which);
                                 checkedItems[which] = true;
                             }
                             else if (itemSelected.contains(which))
                             {
                                 itemSelected.remove(which);
                                 checkedItems[which] = false;
                             }
                         }
                     });
                 }
                 else
                 {
                     for (int i = 0; i<itemSelected.size();i++)
                     {
                         checkedItems[Integer.parseInt(itemSelected.get(i).toString())] = true;
                     }

                     builder.setMultiChoiceItems(items,checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                             if(isChecked)
                             {
                                 itemSelected.add(which);
                                 checkedItems[which] = true;
                             }
                             else if (itemSelected.contains(which))
                             {
                                 itemSelected.remove(which);
                                 checkedItems[which] = false;
                             }
                         }
                     });
                 }

                 builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                     }
                 });


                 AlertDialog alertDialog = builder.create();
                 alertDialog.show();
             }
         });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                Uri selectedImage = data.getData();
                imageView.setImageURI(selectedImage);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_student,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.saveStudent:
                if (TextUtils.isEmpty(nameView.getText()))
                {
                    Toast.makeText(this,"student should have name" ,Toast.LENGTH_SHORT ).show();
                }
                else if (TextUtils.isEmpty(gradeView.getText()))
                {
                    Toast.makeText(this,"student should have grade" ,Toast.LENGTH_SHORT ).show();
                }

               else if (TextUtils.isEmpty(phoneView.getText()))
                {
                    Toast.makeText(this,"student should have phone number" ,Toast.LENGTH_SHORT ).show();
                }
                else if(itemSelected.size() == 0)
                {
                    Toast.makeText(this,"you must pick on course at least" ,Toast.LENGTH_SHORT ).show();
                }

                else

                {
                    saveStudent();
                    finish();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(haschanged == false)
        {
            super.onBackPressed();
            return;
        }
        else
        {
            DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            };

            showDialog(onClickListener);
        }

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

            return new CursorLoader(this, updateUri, projection, null,null, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.moveToFirst())

        {   int nameColumn  = data.getColumnIndex(BaseContract.COLUMN_NAME);
            int gradeColumn = data.getColumnIndex(BaseContract.COLUMN_GRADE);
            int phoneColumn = data.getColumnIndex(BaseContract.COLUMN_PHONE);
            int emailColumn = data.getColumnIndex(BaseContract.COLUMN_EMAIL);
            int imageColumn = data.getColumnIndex(BaseContract.COLUMN_IMAGE);
            int coursesColumn = data.getColumnIndex(BaseContract.COLUMN_COURSES);

            nameView.setText(data.getString(nameColumn));
            gradeView.setText(data.getString(gradeColumn));
            phoneView.setText(data.getString(phoneColumn));
            emailView.setText(data.getString(emailColumn));

            byte[] img = data.getBlob(imageColumn);
            Bitmap userImage = byteArrayToBitmap(img);
            imageView.setImageBitmap(userImage);

            String course = data.getString(coursesColumn);
            itemSelected = convertStringToArray(course);



        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void saveStudent()
    {
        ContentValues values = new ContentValues();


            values.put(BaseContract.COLUMN_NAME,nameView.getText().toString().trim());
            values.put(BaseContract.COLUMN_GRADE,gradeView.getText().toString().trim());
            values.put(BaseContract.COLUMN_PHONE,phoneView.getText().toString().trim());
            values.put(BaseContract.COLUMN_EMAIL,emailView.getText().toString().trim());

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        if(bitmapDrawable != null)
        {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            byte[] userImage = bitmapToByteArray(bitmap);
            values.put(BaseContract.COLUMN_IMAGE,userImage );
        }

            String courses = convertArrayToString(itemSelected);
            values.put(BaseContract.COLUMN_COURSES,courses);



        if (updateUri != null)
        {
          int rowEffected = getContentResolver().update(updateUri,values,null ,null );
          if (rowEffected == 0)
          {
              Toast.makeText(this,"failed to update the student" ,Toast.LENGTH_SHORT );
          }
          else
          {
              Toast.makeText(this,"student info successfully updated" ,Toast.LENGTH_SHORT );
          }
        }
        else {
            getContentResolver().insert(BaseContract.CONTENT_URI, values);
        }


    }

    private byte[] bitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100 ,byteArrayOutputStream );
        return byteArrayOutputStream.toByteArray();
    }

    private Bitmap byteArrayToBitmap(byte[] img)
    {
        Bitmap userImage =  BitmapFactory.decodeByteArray(img,0 ,img.length );
        return userImage;
    }

    private String convertArrayToString(ArrayList arrayList)
    {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i < arrayList.size() )
        {   if(i == arrayList.size()-1)
            {
                builder.append(String.valueOf(arrayList.get(i)));
                i++;
             }
             else {
            builder.append(String.valueOf(arrayList.get(i))+",");
            i++;
        }

        }
        return builder.toString();
    }

    public static ArrayList convertStringToArray(String courses)
    {
        ArrayList checkedCourses = new ArrayList();
        String[] coursesString = courses.split(",");
        for (int i = 0; i<coursesString.length;i++)
        {
            checkedCourses.add(Integer.parseInt(coursesString[i]));
        }
        return checkedCourses;
    }

    private void showDialog(DialogInterface.OnClickListener dialogInterface)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("are you sure want to exit ?");
        builder.setPositiveButton("Yes",dialogInterface );
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog!= null)
                {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
