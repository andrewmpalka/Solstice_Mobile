package com.example.andrewpalka.cardtacts;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by andrewpalka on 2/3/17.
 */

public class View_Holder extends RecyclerView.ViewHolder {

    CardView card;
    TextView title;
    TextView description;
    ImageView imageView;

    View_Holder(View itemView) {
        super(itemView);
        card = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }
}
