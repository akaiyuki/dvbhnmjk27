package com.syaona.petalierapp.core;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



/**
 * Created by smartwavedev on 1/20/16.
 */
public class AppController extends Application {

    private static AppController mInstance;
    private static PRequestQueue mRequestQueue;
    private SQLiteDatabase mDatabase;
//    private DaoMaster mDaoMaster;
//    private DaoSession mDaoSession;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static PRequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    @Override
    public void onCreate() {
        super.onCreate();


//        if(!BuildConfig.DEBUG) {
//            Fabric.with(this, new Crashlytics());
//        }

        mInstance = this;
//        initDatabase();
//        JodaTimeAndroid.init(this);
        PSharedPreferences.init(mInstance);
        mRequestQueue = new PRequestQueue(getApplicationContext());


    }

//    public void initDatabase() {
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "partyphile-db", null);
//        mDatabase = helper.getWritableDatabase();
//        mDaoMaster = new DaoMaster(mDatabase);
//        mDaoSession = mDaoMaster.newSession();
//    }

//    public DaoMaster getDaoMaster() {
//        return mDaoMaster;
//    }

//    public DaoSession getDaoSession() {
//        return mDaoSession;
//    }

    public void deleteCache() {
        Cursor c = mDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {

                if (!((c.getString(0)).equalsIgnoreCase("android_metadata")
                        || (c.getString(0)).equalsIgnoreCase("sqlite_sequence"))) {
                    PDebug.logDebug("ORM_DB", "table name:" + c.getString(0));
                    mDatabase.execSQL("delete from " + c.getString(0));
                }
                c.moveToNext();
            }
        }
    }


}
