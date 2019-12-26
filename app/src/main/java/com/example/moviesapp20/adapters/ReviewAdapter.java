package com.example.moviesapp20.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp20.R;
import com.example.moviesapp20.data.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    private ArrayList<Review> reviews;

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review,parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.author_text_view.setText(review.getAuthor());
        holder.content_text_view.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView author_text_view;
        TextView content_text_view;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            author_text_view = itemView.findViewById(R.id.author_text_view);
            content_text_view = itemView.findViewById(R.id.content_text_view);
        }
    }
}
