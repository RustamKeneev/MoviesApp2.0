 package com.example.moviesapp20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviesapp20.adapters.ReviewAdapter;
import com.example.moviesapp20.adapters.TrailerAdapter;
import com.example.moviesapp20.data.FavoriteMovie;
import com.example.moviesapp20.data.MainViewModel;
import com.example.moviesapp20.data.Movie;
import com.example.moviesapp20.data.Review;
import com.example.moviesapp20.data.Trailer;
import com.example.moviesapp20.utils.JSONUtils;
import com.example.moviesapp20.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

 public class DetailMoviesActivity extends AppCompatActivity {

    private ImageView big_poster_image_view;
    private ImageView imageViewAddRoFavorite;
    private TextView label_title_text_view;
    private TextView title_text_view;
    private TextView original_label_text_view;
    private TextView original_title_text_view;
    private TextView label_rating_text_view;
    private TextView rating_text_view;
    private TextView label_release_date;
    private TextView release_date_text_view;
    private TextView label_description;
    private TextView overview_text_view;
    private RecyclerView recycler_view_trailers;
    private RecyclerView recycler_view_reviews;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;
    private ScrollView scroll_view_info;

    private int id;
    private Movie movie;
    private FavoriteMovie favoriteMovie;
    private MainViewModel viewModel;
    private static String langauge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movies);
        initAndBuildViews();
    }

     private void initAndBuildViews() {

         langauge = Locale.getDefault().getLanguage();
         big_poster_image_view = findViewById(R.id.big_poster_image_view);
         imageViewAddRoFavorite = findViewById(R.id.imageViewAddRoFavorite);
         label_title_text_view = findViewById(R.id.label_title_text_view);
         title_text_view = findViewById(R.id.title_text_view);
         original_label_text_view = findViewById(R.id.original_label_text_view);
         original_title_text_view = findViewById(R.id.original_title_text_view);
         label_rating_text_view = findViewById(R.id.label_rating_text_view);
         rating_text_view = findViewById(R.id.rating_text_view);
         label_release_date = findViewById(R.id.label_release_date);
         release_date_text_view = findViewById(R.id.release_date_text_view);
         label_description = findViewById(R.id.label_description);
         overview_text_view = findViewById(R.id.overview_text_view);
         recycler_view_trailers = findViewById(R.id.recycler_view_trailers);
         recycler_view_reviews = findViewById(R.id.recycler_view_reviews);
         scroll_view_info = findViewById(R.id.scroll_view_info);

         scroll_view_info.smoothScrollTo(0,0);




         Intent intent = getIntent();
         if (intent !=null && intent.hasExtra("id")){
             id = intent.getIntExtra("id",-1);
         }else {
             finish();
         }

         viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
         movie = viewModel.getMovieById(id);
         Picasso.get().load(movie.getBigPosterPath()).placeholder(R.drawable.video).into(big_poster_image_view);
         title_text_view.setText(movie.getTitle());
         original_title_text_view.setText(movie.getOriginalTitle());
         overview_text_view.setText(movie.getOverview());
         release_date_text_view.setText(movie.getReleaseDate());
         rating_text_view.setText(Double.toString(movie.getVoteAverage()));
         setFavorite();
         trailerAdapter = new TrailerAdapter();
         recycler_view_trailers.setLayoutManager(new LinearLayoutManager(this));
         recycler_view_trailers.setAdapter(trailerAdapter);
         JSONObject jsonObjectTrailers = NetworkUtils.getJSONForVideos(movie.getId(),langauge);
         ArrayList<Trailer> trailers = JSONUtils.getTrailerFromJSON(jsonObjectTrailers);
         trailerAdapter.setTrailers(trailers);
         trailerAdapter.setOnTrailerClickListener(new OnTrailerClickListener() {
             @Override
             public void onTrailerClick(String url) {
                Intent intentTotrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentTotrailer);
             }
         });

         reviewAdapter = new ReviewAdapter();
         recycler_view_reviews.setLayoutManager(new LinearLayoutManager(this));
         recycler_view_reviews.setAdapter(reviewAdapter);
         JSONObject jsonObjectReviews = NetworkUtils.getJSONForReviews(movie.getId(),langauge);
         ArrayList<Review> reviews = JSONUtils.getReviewsFromJSON(jsonObjectReviews);
         reviewAdapter.setReviews(reviews);

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

     public void onClickChangeFavorite(View view) {
            favoriteMovie = viewModel.getFavoriteMovieById(id);
         if (favoriteMovie == null){
             viewModel.insertFavoriteMovie(new FavoriteMovie(movie));
             Toast.makeText(this, R.string.add_to_favorite, Toast.LENGTH_SHORT).show();
         }else {
             viewModel.deleteFavoriteMovie(favoriteMovie);
             Toast.makeText(this, R.string.remove_from_favorite, Toast.LENGTH_SHORT).show();
         }
         setFavorite();
     }
     private void setFavorite(){
         favoriteMovie = viewModel.getFavoriteMovieById(id);
         if (favoriteMovie == null){
             imageViewAddRoFavorite.setImageResource(R.drawable.ic_star_green);
         }else {
             imageViewAddRoFavorite.setImageResource(R.drawable.ic_star_yellow);
         }
     }
 }
