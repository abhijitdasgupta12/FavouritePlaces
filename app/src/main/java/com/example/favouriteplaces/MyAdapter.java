package com.example.favouriteplaces;

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

    //Constructor
    public MyAdapter(ArrayList<ModelClass> dataholder)
    {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position)
    {
        holder.name.setText(dataholder.get(position).getName());
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
