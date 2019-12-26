package com.example.moviesapp20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.moviesapp20.adapters.MovieAdapter;
import com.example.moviesapp20.data.FavoriteMovie;
import com.example.moviesapp20.data.MainViewModel;
import com.example.moviesapp20.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMoviesActivity extends AppCompatActivity {

    private RecyclerView favorite_movies_recycler_view;
    private MovieAdapter movieAdapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        initAndBuildViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item_main:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.itemFavorite:
                Intent intentToFavorite = new Intent(this,FavoriteMoviesActivity.class);
                startActivity(intentToFavorite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initAndBuildViews() {
        favorite_movies_recycler_view = findViewById(R.id.favorite_movies_recycler_view);
        favorite_movies_recycler_view.setLayoutManager(new GridLayoutManager(this,2));
        movieAdapter = new MovieAdapter();
        favorite_movies_recycler_view.setAdapter(movieAdapter);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        LiveData<List<FavoriteMovie>> favoriteMovies = viewModel.getFavoriteMovies();
        favoriteMovies.observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {
                List<Movie> movies = new ArrayList<>();
                if (favoriteMovies !=null){
                    movies.addAll(favoriteMovies);
                    movieAdapter.setMovies(movies);
                }
            }
        });
        movieAdapter.setOnPosterClickListener(new OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie = movieAdapter.getMovies().get(position);
                Intent intent = new Intent(FavoriteMoviesActivity.this,DetailMoviesActivity.class);
                intent.putExtra("id",movie.getId());
                startActivity(intent);
            }
        });

    }
}
