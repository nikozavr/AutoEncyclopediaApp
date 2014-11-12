package ru.nikozavr.auto;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.util.LruCache;

import java.io.File;
import java.sql.Date;

import ru.nikozavr.utils.DiskLruCache;

/**
 * Created by nikozavr on 2/21/14.
 */
public class AutoEncyclopedia extends Application {

    SharedPreferences profile;

    SharedPreferences.Editor prof_ed;

    private static Context _context;

    int PRIVATE_MODE = 0;

    public static final String TAG_SUCCESS = "success";
    public static final String TAG_COUNT = "count";
    public static final String TAG_MARQS = "marqs";
    public static final String TAG_MARQ_INFO = "marq_info";
    public static final String TAG_MODEL_INFO = "model_info";
    public static final String TAG_MODELS = "models";
    public static final String TAG_GENERS = "geners";

    public static final String TAG_ID_MARQ = "id_marq";
    public static final String TAG_ID_MODEL = "id_model";
    public static final String TAG_ID_GENER = "id_gener";

    public static final String TAG_NAME = "name";
    public static final String TAG_ID_PIC_AUTO = "id_pic_auto";
    public static final String TAG_ID_PIC_MARQ = "id_pic_marq";
    public static final String TAG_PICS = "pics";
    public static final String TAG_PIC_URL = "pic_url";
    public static final String TAG_PROD_DATE = "prod_date";
    public static final String TAG_OUT_DATE = "out_date";

    public static final String TAG_COUNTRY = "country";
    public static final String TAG_INFO = "info";
    public static final String TAG_WEB = "web";

    private static final String PREF_NAME = "AutoEncyclopediaPref";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_LOGIN = "login";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_PERMISSION = "permission";


    private LruCache<String, Bitmap> mMemoryCache;

    private DiskLruCache mDiskLruCache;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 50; // 10MB
    private static final String DISK_CACHE_SUBDIR = "thumbnails";

    @Override
    public void onCreate()
    {
        super.onCreate();
        profile = this.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        prof_ed = profile.edit();

        _context = getApplicationContext();

        prof_ed.putBoolean(IS_LOGIN, false);

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void AutoEncyclopedia(){
        super.onCreate();
        _context = getApplicationContext();
    }

    public static Context getAppContext() {
        return _context;
    }

    public void createLoginSession(String login, String email, String permission){
        prof_ed.putBoolean(IS_LOGIN,true);

        prof_ed.putString(KEY_LOGIN, login);

        prof_ed.putString(KEY_EMAIL, email);

        prof_ed.putString(KEY_PERMISSION, permission);

        prof_ed.commit();
    }

    public String[] getUserDetails(){
        String[] user = new String[3];

        user[0] = profile.getString(KEY_LOGIN,null);
        user[1] = profile.getString(KEY_EMAIL,null);
        user[2] = profile.getString(KEY_PERMISSION,null);

        // return user
        return user;
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        prof_ed.clear();
        prof_ed.commit();
    }

    public boolean isLoggedIn(){
        return profile.getBoolean(IS_LOGIN, false);
    }

    public boolean isOnline(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static String convertdate(Date date){
        if(date != null){
            return date.toString().substring(0,4);
        }else
            return "н.в.";
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

}
