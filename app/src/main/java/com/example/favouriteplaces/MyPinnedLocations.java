package com.example.favouriteplaces;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;

public class MyPinnedLocations extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ModelClass> dataholders;
    ModelClass modelClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pinned_locations);

        setTitle("Saved Locations");

        recyclerView=findViewById(R.id.recyclerView);

        //Setting up layoutmanager for recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Fetching data. Syntax: Cursor obj= new sample_class(this).Method_Implemented_Inside_"sample_class"_to_Fetch_Data;
        Cursor cursors= new SQLiteDB_Manager(this).fetchRecords();
        dataholders=new ArrayList<>();

        while (cursors.moveToNext())//reading data one by one
        {
//            /*******************
//            Syntax: Sample_Class obj= new Sample_Class(obj_of_Cursor.getString(0),obj_of_Cursor.getString(1),...);
//            getString(0) = 0th index or 1st column of the database
//            getString(1) = 1st index or 2nd column of the database etc.
//             *******************/
            modelClass=new ModelClass(cursors.getString(1),cursors.getString(2));
            dataholders.add(modelClass);
        }

        //Setting up adapter
        MyAdapter myAdapter=new MyAdapter(dataholders);
        recyclerView.setAdapter(myAdapter);

        AlertDialog.Builder builder=new AlertDialog.Builder(MyPinnedLocations.this); //Pass any view to continue
        builder.setTitle("Tips");
        builder.setMessage("Tap on the place name from the list of saved locations to view the location on map.");
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builder.show();

    }
}