package com.example.android.udacitycapstoneproject.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.android.udacitycapstoneproject.data.local.dao.FavDao;
import com.example.android.udacitycapstoneproject.data.local.model.Article;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
@Database(entities = {Article.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavDao favDao();


//    private volatile AppDatabase sInstance;
//
//    public AppDatabase(Context context, String name) {
//        sInstance = Room.databaseBuilder(context.getApplicationContext(),
//                AppDatabase.class, name).build();
//        return sInstance;
//    }



//    public static AppDatabase getInstance(Context context, String name) {
//
////        if (sInstance == null) {
////            synchronized (LOCK) {
////                if (sInstance == null) {
////
////                }
////            }
////        }
//        return sInstance;
//    }
}
