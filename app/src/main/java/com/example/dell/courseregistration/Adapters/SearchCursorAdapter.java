package com.example.dell.courseregistration.Adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.dell.courseregistration.Activities.EditActivity;
import com.example.dell.courseregistration.Database.DatabaseContract.BaseContract;
import com.example.dell.courseregistration.R;

import java.util.ArrayList;

public class SearchCursorAdapter extends CursorAdapter {

    Context context ;
   View item = null;
    public SearchCursorAdapter(Context c_context, Cursor cursor, int flags) {
        super(c_context, cursor,0);
        context = c_context;
    }

    @Override
    public View newView(Context context, Cursor cursor,  ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.student_card,parent,false );
        item = parent;
        return view;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        TextView nameView = view.findViewById(R.id.stdName);
        TextView coursesView  = view.findViewById(R.id.coursesNames);
        ImageView imageView = view.findViewById(R.id.stdImage);
        ImageView optionView = view.findViewById(R.id.itemOption);


        int nameColumn = cursor.getColumnIndexOrThrow(BaseContract.COLUMN_NAME);
        int imgColumn = cursor.getColumnIndexOrThrow(BaseContract.COLUMN_IMAGE);
        int courseColumn = cursor.getColumnIndexOrThrow(BaseContract.COLUMN_COURSES);
        String coursesString = cursor.getString(courseColumn);
        ArrayList items = EditActivity.convertStringToArray(coursesString);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i<items.size();i++)
        {
            if(i==items.size()-1)
            {
                builder.append(EditActivity.items[Integer.parseInt(items.get(i).toString())]);
            }
            else
            {
                builder.append(EditActivity.items[Integer.parseInt(items.get(i).toString())]+",");
            }

        }
        coursesView.setText(builder.toString());

        nameView.setText(cursor.getString(nameColumn));
        imageView.setImageBitmap( byteArrayToBitmap(cursor.getBlob(imgColumn)));

        optionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(context.getApplicationContext(),v);
                menu.getMenuInflater().inflate(R.menu.item_options,menu.getMenu());
                menu.show();

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.stdItemEdit:
                                editStudent(cursor.getInt(cursor.getColumnIndexOrThrow(BaseContract.COLUMN_ID)));
                                break;
                            case R.id.stdItemDelete:
                                deleteStudent(cursor.getInt(cursor.getColumnIndexOrThrow(BaseContract.COLUMN_ID)));
                                break;

                        }
                        return true;
                    }
                });
            }

        });


    }

    private Bitmap byteArrayToBitmap(byte[] img)
    {
        Bitmap userImage =  BitmapFactory.decodeByteArray(img,0 ,img.length );
        return userImage;
    }

    public void deleteStudent(long id)
    {
        String selection = BaseContract.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        context.getContentResolver().delete(BaseContract.CONTENT_URI,selection ,selectionArgs );
    }

    public void editStudent(long id)
    {
        Intent intent = new Intent(context,EditActivity.class);
        Uri currentUri = ContentUris.withAppendedId(BaseContract.CONTENT_URI,id );
        intent.setData(currentUri);
        context.startActivity(intent);
    }


}
