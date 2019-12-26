package com.example.moviesapp20.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favorite_movies")
public class FavoriteMovie extends Movie {

    public FavoriteMovie(int uniqueId,int id, int voteCount, String title, String originalTitle, String overview,
                         String bigPosterPath, String posterPath, String backdropPath, double voteAverage, String releaseDate) {
        super(uniqueId,id, voteCount, title, originalTitle, overview, bigPosterPath, posterPath, backdropPath, voteAverage, releaseDate);
    }

    @Ignore
    public FavoriteMovie(Movie movie){
        super(movie.getUniqueId(),movie.getId(),movie.getVoteCount(),movie.getTitle(),movie.getOriginalTitle(),
                movie.getOverview(),movie.getPosterPath(),movie.getBigPosterPath(),movie.getBackdropPath(),
                movie.getVoteAverage(),movie.getReleaseDate());
    }
}
