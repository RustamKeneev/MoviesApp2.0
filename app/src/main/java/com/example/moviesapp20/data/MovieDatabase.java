package com.example.moviesapp20.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.moviesapp20.MovieDao;

@Database(entities = {Movie.class,FavoriteMovie.class},version = 3,exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    private static final String DB_NAME = "movies.db";
    private static MovieDatabase database;
    private static final Object LOCK = new Object();

    public static MovieDatabase getInstance(Context context){
        synchronized (LOCK) {
            if (database == null) {
                database = Room.databaseBuilder(context, MovieDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
            }
        }
        return database;
    }

    public abstract MovieDao movieDao();
}
