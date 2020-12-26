package com.example.favouriteplaces;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDB_Manager extends SQLiteOpenHelper
{
        //Setting up database name
        private static final String dbname="dbcontacts.db";

        //Since values are predefined inside super(), so "@Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version" is not required in the parameter of constructor below
        public SQLiteDB_Manager(@Nullable Context context)
        {
            super(context, dbname, null, 1);
        }

        //process on creation of database
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            String qry="create table tbl_place (id integer primary key autoincrement, name text, latitude text, longitude text)";
            db.execSQL(qry);
        }

        //process on upgrading the system
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            String qry="DROP TABLE IF EXISTS tbl_place";
            db.execSQL(qry);
            onCreate(db);
        }

        //User-defined method, to add record in the database through user
        public String addRecord(String name, String latitude, String longitude)
        {
            //Manually passing object of SQLiteDatabase since the object is not available in the parameter of the method
            SQLiteDatabase db=this.getWritableDatabase();

            //ContentValues
            ContentValues values=new ContentValues();

            //The Syntax: values.put("Column_Name_Of_Database", parameter_used_in_the_method)
            values.put("name",name);
            values.put("latitude",latitude);
            values.put("longitude",longitude);

            //Inserting data into database and receiving the result of operation -1 or +1
            float res=db.insert("tbl_place", null, values);

            //Checking result
            if (res==-1)
                return "Insertion Failed";
            else
                return "Insertion Successful";
        }

        //Fetching record operation. Return type "Cursor"
        public Cursor fetchRecords()
        {
            SQLiteDatabase db=this.getReadableDatabase();

            String qry="select * from tbl_place";

            //Cursor
            Cursor c_obj=db.rawQuery(qry,null);

            return c_obj;
        }

        //Fetching record from a specific row
        public Cursor fetchRecordsFromSelectedRow(int column_index)
        {
            SQLiteDatabase db= this.getReadableDatabase();

            String qry="select * from tbl_place where id="+column_index;

            Cursor cursor= db.rawQuery(qry, null);

            return cursor;
        }
    }
