package com.example.andrewpalka.cardtacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrewpalka.cardtacts.Model.Contact;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by andrewpalka on 2/3/17.
 */
public class Recycler_View_Adapter extends RecyclerView.Adapter<Recycler_View_Adapter.View_Holder> {


    List<Contact> itemList = Collections.emptyList();

    Context context;

    private String TAG = Recycler_View_Adapter.class.getSimpleName();



    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final Adapter_OnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface Adapter_OnClickHandler {
        void onClick(Contact selectedContact);
    }

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public Recycler_View_Adapter(Adapter_OnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public Recycler_View_Adapter(Context context, Adapter_OnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;
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


            holder.title.setText( itemList.get(position).name);
            holder.description.setText(itemList.get(position).phone.getPhone(0));

        Picasso.with(context).load(itemList.get(position).imageURL.getLargeImgURL())
                .placeholder(R.drawable.ic_action_movie)
                .into(holder.imageView);

    }

    public void animate(RecyclerView.ViewHolder viewHolder) {

        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }


    public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final CardView card;
        public final TextView title;
        public final TextView description;
        public final ImageView imageView;


        View_Holder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        int adapterPosition = getAdapterPosition();
            Contact selectedContact = itemList.get(adapterPosition);
            mClickHandler.onClick(selectedContact);

        }
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        if (itemList == null ) return 0;
        return itemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateContactData(List<Contact> data) {
// TODO: THIS IS WHERE THE CONTACT LIST SHOULD BE UPDATING
//        Log.d(TAG, "updateContactData: " + data.size());

        itemList = data;
//        Log.d(TAG, "updateContactData: " + itemList.size());
        notifyDataSetChanged();
    }


    /*
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
    */

}