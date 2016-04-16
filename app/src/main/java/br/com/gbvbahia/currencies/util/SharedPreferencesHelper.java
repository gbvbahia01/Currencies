package br.com.gbvbahia.currencies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Guilherme on 4/16/16.
 */
public class SharedPreferencesHelper {

  private SharedPreferencesHelper() {
  }

  public static void setString(Context pContext, String key, String value){
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pContext);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(key, value);
    editor.commit();
  }

  public static String getString(Context pContext, String key){
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pContext);
    return preferences.getString(key, null);
  }
}
