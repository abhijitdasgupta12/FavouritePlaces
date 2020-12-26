package com.example.favouriteplaces;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    //ArrayList
    ArrayList<ModelClass> dataholder;

    private Context context;

    //Constructor
    public MyAdapter(ArrayList<ModelClass> dataholder, Context context)
    {
        this.dataholder = dataholder;
        this.context= context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position)
    {
        holder.name.setText(dataholder.get(position).getName());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context.getApplicationContext(), MapsActivity.class);
                intent.putExtra("place_ID", position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.i("MY INFORMATION", String.valueOf(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    //User defined viewholder class
    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            //Typecasting
            name=itemView.findViewById(R.id.locationName);
        }
    }
}
