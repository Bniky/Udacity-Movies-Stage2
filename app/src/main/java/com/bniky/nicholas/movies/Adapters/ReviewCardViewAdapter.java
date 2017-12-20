package com.bniky.nicholas.movies.Adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bniky.nicholas.movies.Data.ReviewsOfMovie;
import com.bniky.nicholas.movies.R;

import java.util.List;

/**
 * Created by Nicholas on 08/12/2017.
 */

public class ReviewCardViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ReviewsOfMovie> reviewsOfMovies;
    Context context;

    public ReviewCardViewAdapter(Context context, List<ReviewsOfMovie> reviewsOfMovies){
        this.reviewsOfMovies = reviewsOfMovies;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View row = layoutInflater.inflate(R.layout.review_cardview_layout, parent, false);
        CardHolderItem cv_item = new CardHolderItem(row);

        return cv_item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CardHolderItem)holder).tv_name.setText(reviewsOfMovies.get(position).getName());
        ((CardHolderItem)holder).tv_description.setText(reviewsOfMovies.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return reviewsOfMovies.size();
    }

    public class CardHolderItem extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_description;

        public CardHolderItem(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.name);
            tv_description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
