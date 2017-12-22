package com.example_link.linkapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


class MySQLite extends SQLiteOpenHelper {
    private Context mContext;

    MySQLite(Context context) {
        super(context, "phone_book.db", null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table phone_book(_id integer primary key autoincrement, singer varchar, songname varchar, url varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Toast.makeText(mContext, "onUpgrade", Toast.LENGTH_SHORT).show();
    }
}