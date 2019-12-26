package com.example.moviesapp20.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp20.OnPosterClickListener;
import com.example.moviesapp20.OnReachEndListener;
import com.example.moviesapp20.R;
import com.example.moviesapp20.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private OnPosterClickListener onPosterClickListener;
    private OnReachEndListener onReachEndListener;

    public MovieAdapter() {
        movies = new ArrayList<>();
    }

    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (movies.size() >= 20 && position > movies.size() - 4 && onReachEndListener !=null){
            onReachEndListener.onReachEnd();
        }
        Movie movie = movies.get(position);
        Picasso.get().load(movie.getPosterPath()).into(holder.image_view_small_poster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void clear(){
        this.movies.clear();
        notifyDataSetChanged();
    }
    public class MovieViewHolder extends RecyclerView.ViewHolder{
        private ImageView image_view_small_poster;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            image_view_small_poster = itemView.findViewById(R.id.image_view_small_poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPosterClickListener !=null){
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setMovies( List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addMovies( List<Movie> movies){
         this.movies.addAll(movies);
         notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
