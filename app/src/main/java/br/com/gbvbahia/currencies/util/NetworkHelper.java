package br.com.gbvbahia.currencies.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * Created by Guilherme on 4/16/16.
 */
public class NetworkHelper {
  private NetworkHelper() {
  }

  /**
   * For uuse this method you need to put this permission in AndroidManifest.xml file<br>
   * android.permission. ACCESS_NETWORK_STATE
   * @param pContext
   */
  public static boolean isOnline(Context pContext){
    ConnectivityManager cManager =
        (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cManager.getActiveNetworkInfo();
    if(netInfo != null && netInfo.isConnectedOrConnecting()){
      return true;
    }
    return false;
  }

  public static void launchBrowser(Context pContext, String pUri){
    if(isOnline(pContext)){
      Uri uri = Uri.parse(pUri);
      Intent intent = new Intent(Intent.ACTION_VIEW, uri);
      pContext.startActivity(intent);
    }
  }
}
