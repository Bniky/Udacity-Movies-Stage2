package com.bniky.nicholas.movies.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bniky.nicholas.movies.data.MovieTrailer;
import com.bniky.nicholas.movies.R;

import java.util.List;

/**
 * Created by Nicholas on 05/12/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<MovieTrailer> items;
    Context context;

    //Number of trailer
    int i = 1;

    public TrailerAdapter(Context context, List<MovieTrailer> items){
        this.context = context;
        this.items = items;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View row = layoutInflater.inflate(R.layout.trailer_list, parent, false);
        Item item = new Item(row);

        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String trailer_p = "Trailer " + (position);
        ((Item)holder).textView.setText(trailer_p);

        final int pos = position;
        ((Item) holder).constraintLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                watchYoutubeVideo(context, items.get(pos).getKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Item extends RecyclerView.ViewHolder{
        TextView textView;
        ConstraintLayout constraintLayout;
        public Item(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.list);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.constrain_layout);
        }
    }

    //Watch on Youtube app or Web app if available
    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
