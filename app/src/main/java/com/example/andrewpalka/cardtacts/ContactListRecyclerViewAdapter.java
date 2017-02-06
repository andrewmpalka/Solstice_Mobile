package com.example.andrewpalka.cardtacts;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrewpalka.cardtacts.Model.Contact;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by andrewpalka on 2/3/17.
 */
public class ContactListRecyclerViewAdapter extends RecyclerView.Adapter<ContactListRecyclerViewAdapter.ContactViewHolder> {


    private List<Contact> mItemList = Collections.emptyList();

    private Context mContext;

    private String TAG = ContactListRecyclerViewAdapter.class.getSimpleName();

    private final AdapterOnClickHandler mClickHandler;


    public interface AdapterOnClickHandler {

        void onClick(Contact selectedContact);

    }

    public ContactListRecyclerViewAdapter(Context context, AdapterOnClickHandler clickHandler) {

        this.mContext = context;
        mClickHandler = clickHandler;

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_row, parent, false);
        ContactViewHolder holder = new ContactViewHolder(v);

//        animate(holder);


        return holder;

    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(mItemList.get(position).name);

        String[] splitStr = mItemList.get(position).phone.getWork().split("-");
        String s = "(" + splitStr[0] + ") " + splitStr[1] + "-" + splitStr[2];

        holder.description.setText(s);


        Picasso.with(mContext).load(mItemList.get(position).imageURL.getSmallImgURL())
                .placeholder(R.drawable.ic_action_name)
                .resize(120,120)
                .into(holder.imageView);

    }

    public void animate(RecyclerView.ViewHolder viewHolder) {

        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(mContext,
                R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);

    }


    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final CardView card;
        public final TextView title;
        public final TextView description;
        public final ImageView imageView;


        ContactViewHolder(View itemView) {

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
        Contact selectedContact = mItemList.get(adapterPosition);
         mClickHandler.onClick(selectedContact);

        }
    }

    @Override
    public int getItemCount() {

        //returns the number of elements the RecyclerView will display
        if (mItemList == null ) return 0;

        return mItemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        super.onAttachedToRecyclerView(recyclerView);

    }

    public void updateContactData(List<Contact> data) {

        mItemList = data;
        notifyDataSetChanged();

    }

}