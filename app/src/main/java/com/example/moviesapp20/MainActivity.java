package com.example.moviesapp20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviesapp20.adapters.MovieAdapter;
import com.example.moviesapp20.data.MainViewModel;
import com.example.moviesapp20.data.Movie;
import com.example.moviesapp20.utils.JSONUtils;
import com.example.moviesapp20.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private Switch switch_sort;
    private TextView top_rated_text_view;
    private TextView popularity_text_view;
    private RecyclerView recycler_view_posters;
    private ProgressBar progress_bar_loading;
    private MovieAdapter movieAdapter;
    private MainViewModel mainViewModel;
    private LoaderManager loaderManager;
    private static final String TAG = "ololo";
    private static final int LOADER_ID = 500;
    private static int page = 1;
    private static boolean isLoading = false;
    private static int methodOfSort;
    private static String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAndBuildViews();
    }

    private int getColumnCount(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return width / 185 > 2 ? width /185 : 2;
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
        loaderManager = LoaderManager.getInstance(this);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        language = Locale.getDefault().getLanguage();
        switch_sort = findViewById(R.id.switch_sort);
        top_rated_text_view = findViewById(R.id.top_rated_text_view);
        popularity_text_view = findViewById(R.id.popularity_text_view);
        recycler_view_posters = findViewById(R.id.recycler_view_posters);
        progress_bar_loading = findViewById(R.id.progress_bar_loading);

        recycler_view_posters.setLayoutManager(new GridLayoutManager(this,getColumnCount()));
        movieAdapter = new MovieAdapter();
        recycler_view_posters.setAdapter(movieAdapter);
        switch_sort.setChecked(true);
        switch_sort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                page =1;
                setMethodOfSort(isChecked);
            }
        });
        switch_sort.setChecked(false);
        movieAdapter.setOnPosterClickListener(new OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie = movieAdapter.getMovies().get(position);
                Intent intent = new Intent(MainActivity.this,DetailMoviesActivity.class);
                intent.putExtra("id",movie.getId());
                startActivity(intent);
            }
        });
        movieAdapter.setOnReachEndListener(new OnReachEndListener() {
            @Override
            public void onReachEnd() {
                if (!isLoading){
                    downloadData(methodOfSort,page);
                }
            }
        });
        LiveData<List<Movie>> moviesFromLiveData = mainViewModel.getMovies();
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (page ==1){
                movieAdapter.setMovies(movies);
                }
            }
        });
    }

    public void onClickSetPopularity(View view) {
        setMethodOfSort(false);
        switch_sort.setChecked(false);
    }

    public void onClickSetTopRated(View view) {
        setMethodOfSort(true);
        switch_sort.setChecked(true);
    }
    private void setMethodOfSort(boolean isTopRated){

        if (isTopRated){
            top_rated_text_view.setTextColor(getResources().getColor(R.color.colorAccent));
            popularity_text_view.setTextColor(getResources().getColor(R.color.white));
            methodOfSort = NetworkUtils.TOP_RATED;
        }else {
            methodOfSort = NetworkUtils.POPULARITY;
            top_rated_text_view.setTextColor(getResources().getColor(R.color.white));
            popularity_text_view.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        downloadData(methodOfSort,page);
    }

    private void downloadData(int methodOfSort,int page){
        URL url = NetworkUtils.buildURL(methodOfSort,page,language);
        Bundle bundle = new Bundle();
        bundle.putString("url",url.toString());
        loaderManager.restartLoader(LOADER_ID,bundle,this);
     }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this,args);
        jsonLoader.setOnStartLoadingListener(new OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                progress_bar_loading.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(data);
        if (movies !=null && !movies.isEmpty()){
            if (page == 1) {
                mainViewModel.deleteAllMovies();
                movieAdapter.clear();
            }
            for (Movie movie :  movies){
                mainViewModel.insertMovie(movie);
            }
            movieAdapter.addMovies(movies);
            page++;
        }
        isLoading = false;
        progress_bar_loading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}
