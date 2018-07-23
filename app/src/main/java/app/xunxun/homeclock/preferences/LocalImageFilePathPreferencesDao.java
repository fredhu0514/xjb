package app.xunxun.homeclock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by fengdianxun on 15-6-24.
 */
public class LocalImageFilePathPreferencesDao {
    private static final String KEY = "LocalImageFilePathPreferencesDao";

    public static void set(Context context, String text) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY, text);
        editor.apply();
    }

    public static String get(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(KEY, "");
    }

}