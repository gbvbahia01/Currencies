package br.com.gbvbahia.currencies;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SplashActivity extends Activity {
  public static final String KEY_CURRENCIES_BUNDLE = "key_arraylist";
  //url to currency codes used in this application
  public static final String URL_CODES = "http://openexchangerates.org/api/currencies.json";
  //ArrayList of currencies that will be fetched and passed into MainActivity
  private ArrayList<String> mCurrencies;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_splash);
    new FetchCodesTasks().execute(URL_CODES);
  }

  private class FetchCodesTasks extends AsyncTask<String, Void, JSONObject> {

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected JSONObject doInBackground(String... params) {
      try {
        Thread.currentThread().sleep(1000);
      } catch(InterruptedException e) {
        e.printStackTrace();
      }
      return new JSONParser().getJSONFromUrl(params[0]);
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p/>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param jsonObject The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
      try {
        if(jsonObject == null) {
          throw new JSONException("no data available");
        }
        Iterator iterator = jsonObject.keys();
        String key;
        mCurrencies = new ArrayList<>();
        while(iterator.hasNext()) {
          key = (String) iterator.next();
          mCurrencies.add(key + " | " + jsonObject.getString(key));
        }
        Intent mainIntent = new Intent(SplashActivity.this, ExchangeActivity.class);
        mainIntent.putExtra(KEY_CURRENCIES_BUNDLE, mCurrencies);
        startActivity(mainIntent);
        finish();
      } catch(JSONException e) {
        Toast.makeText(SplashActivity.this, "There's been a JSON exception: "
            + e.getMessage(), Toast.LENGTH_LONG).show();
        e.printStackTrace();
        finish();
      }
    }
  }
}
