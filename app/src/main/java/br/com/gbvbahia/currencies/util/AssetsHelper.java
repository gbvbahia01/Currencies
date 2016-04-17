package br.com.gbvbahia.currencies.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Guilherme on 4/17/16.
 */
public class AssetsHelper {

  public static final String PROPERTIES_FILE_NAME = "keys.properties";

  private AssetsHelper() {
  }

  public static String getKeyFromProperties(Context pContext, String pKey){
    AssetManager assManager = pContext.getResources().getAssets();
    Properties prop = new Properties();
    try{
      InputStream inputStream = assManager.open(PROPERTIES_FILE_NAME);
      prop.load(inputStream);
    }catch(IOException e){
      Log.e("ASSETS", "below - getKeyFromProperties: " + PROPERTIES_FILE_NAME, e);
      e.printStackTrace();
      Log.e("ASSETS", "above - getKeyFromProperties: " + PROPERTIES_FILE_NAME, e);
    }
    return prop.getProperty(pKey);
  }
}
