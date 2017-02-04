package com.example.andrewpalka.cardtacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.andrewpalka.cardtacts.Model.Contact;
import com.example.andrewpalka.cardtacts.Model.Data;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by andrewpalka on 2/3/17.
 */
public class Recycler_View_Adapter extends RecyclerView.Adapter<View_Holder> {

    List<Data> list = Collections.emptyList();

    List<Contact> itemList = Collections.emptyList();

    Context context;

    private String TAG = Recycler_View_Adapter.class.getSimpleName();


    public Recycler_View_Adapter(List<Data> list, Context context, List<Contact> itemList) {
        this.list = list;
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_row, parent, false);
        View_Holder holder = new View_Holder(v);

        animate(holder);


        return holder;

    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView


        if (getItemCount() > list.size()) {

            holder.title.setText( itemList.get(position).name);
            holder.description.setText(itemList.get(position).company);


        }
        holder.title.setText(list.get(position).title);
        holder.description.setText(list.get(position).description);
//        holder.imageView.setImageResource(list.get(position).imageId);

        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png")
                .placeholder(list.get(position).imageId)
                .into(holder.imageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Context context = v.getContext();
                            Intent intent = new Intent(context, DetailActivity.class);
//                            intent.putExtra(ContactDetailFragment.ARG_ITEM_ID, holder.mItem.id)
                            context.startActivity(intent);
                    }
                });


    }

    public void animate(RecyclerView.ViewHolder viewHolder) {

        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        if (itemList.isEmpty()) {
            Log.d(TAG, "getItemCount: " + itemList.size());
            return list.size();
        }


        return itemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateContactData(ArrayList<Contact> data) {
// TODO: THIS IS WHERE THE CONTACT LIST SHOULD BE UPDATING
        Log.d(TAG, "updateContactData: " + data.size());

        itemList = data;
        Log.d(TAG, "updateContactData: " + itemList.size());
        notifyDataSetChanged();
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Data data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Data data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

}