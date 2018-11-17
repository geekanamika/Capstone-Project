package com.example.android.udacitycapstoneproject.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.udacitycapstoneproject.data.local.dao.FavDao;
import com.example.android.udacitycapstoneproject.data.local.model.Article;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
@Database(entities = {Article.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavDao favDao();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "NewsDb";
    private static volatile AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, AppDatabase.DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }
}
