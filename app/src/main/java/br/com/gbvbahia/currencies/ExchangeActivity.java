package br.com.gbvbahia.currencies;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import br.com.gbvbahia.currencies.util.AssetsHelper;
import br.com.gbvbahia.currencies.util.NetworkHelper;
import br.com.gbvbahia.currencies.util.SharedPreferencesHelper;

public class ExchangeActivity extends AppCompatActivity
    implements AdapterView.OnItemSelectedListener {

  private static final String FOR = "FOR_CURRENCY";
  private static final String HOM = "HOM_CURRENCY";

  private Button mCalcButton;
  private TextView mConvertedTextView;
  private EditText mAmountEditText;
  private Spinner mForSpinner, mHomSpinner;
  private String[] mCurrencies;
  private ProgressBar mProgress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setIcon(R.mipmap.ic_launcher);

    referenceViewObjects();
    mProgress.setVisibility(View.INVISIBLE);
    fillCurrencies();
    defineAdapter();

    mHomSpinner.setOnItemSelectedListener(this);
    mForSpinner.setOnItemSelectedListener(this);

    defineSpinnersValues(savedInstanceState);

    OpenExchange.mKey = AssetsHelper.getKeyFromProperties(this,
        OpenExchange.PROP_KEY_OPEN_EXCHANGE);

    mCalcButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new CurrencyConverterTask().execute(OpenExchange.URL_BASE + OpenExchange.mKey);
      }
    });
  }

  // ###################################
  // MENU
  // ###################################

  /**
   * Initialize the contents of the Activity's standard options menu.  You
   * should place your menu items in to <var>menu</var>.
   * <p/>
   * <p>This is only called once, the first time the options menu is
   * displayed.  To update the menu every time it is displayed, see
   * {@link #onPrepareOptionsMenu}.
   * <p/>
   * <p>The default implementation populates the menu with standard system
   * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
   * they will be correctly ordered with application-defined menu items.
   * Deriving classes should always call through to the base implementation.
   * <p/>
   * <p>You can safely hold on to <var>menu</var> (and any items created
   * from it), making modifications to it as desired, until the next
   * time onCreateOptionsMenu() is called.
   * <p/>
   * <p>When you add items to the menu, you can implement the Activity's
   * {@link #onOptionsItemSelected} method to handle them there.
   *
   * @param menu The options menu in which you place your items.
   * @return You must return true for the menu to be displayed;
   * if you return false it will not be shown.
   * @see #onPrepareOptionsMenu
   * @see #onOptionsItemSelected
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  /**
   * This hook is called whenever an item in your options menu is selected.
   * The default implementation simply returns false to have the normal
   * processing happen (calling the item's Runnable or sending a message to
   * its Handler as appropriate).  You can use this method for any items
   * for which you would like to do processing without those other
   * facilities.
   * <p/>
   * <p>Derived classes should call through to the base class for it to
   * perform the default menu handling.</p>
   *
   * @param item The menu item that was selected.
   * @return boolean Return false to allow normal menu processing to
   * proceed, true to consume it here.
   * @see #onCreateOptionsMenu
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch(id) {
      case R.id.mnu_invert:
        invertCurrencies();
        break;
      case R.id.mnu_codes:
        NetworkHelper.launchBrowser(this, SplashActivity.URL_CODES);
        break;
      case R.id.mnu_exit:
        finish();
        break;
    }
    return true;
  }

  // ###################################
  // AdapterView.OnItemSelectedListener
  // ###################################

  /**
   * <p>Callback method to be invoked when an item in this view has been
   * selected. This callback is invoked only when the newly selected
   * position is different from the previously selected position or if
   * there was no selected item.</p>
   * <p/>
   * Impelmenters can call getItemAtPosition(position) if they need to access the
   * data associated with the selected item.
   *
   * @param parent   The AdapterView where the selection happened
   * @param view     The view within the AdapterView that was clicked
   * @param position The position of the view in the adapter
   * @param id       The row id of the item that is selected
   */
  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    switch(parent.getId()) {
      case R.id.spn_for:
        defineKeys(FOR, extractCodeFromCurrency((String) mForSpinner.getSelectedItem()));
        break;
      case R.id.spn_hom:
        defineKeys(HOM, extractCodeFromCurrency((String) mHomSpinner.getSelectedItem()));
        break;
      default:
        break;
    }
    mConvertedTextView.setText("");
  }

  /**
   * Callback method to be invoked when the selection disappears from this
   * view. The selection can disappear for instance when touch is activated
   * or when the adapter becomes empty.
   *
   * @param parent The AdapterView that now contains no selected item.
   */
  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    //do nothing it is a SLUG :)
  }

  // ###################################
  // PRIVATE METHODS
  // ###################################
  private void referenceViewObjects() {
    mConvertedTextView = (TextView) findViewById(R.id.txt_converted);
    mAmountEditText = (EditText) findViewById(R.id.edt_amount);
    mCalcButton = (Button) findViewById(R.id.btn_calc);
    mForSpinner = (Spinner) findViewById(R.id.spn_for);
    mHomSpinner = (Spinner) findViewById(R.id.spn_hom);
    mProgress = (ProgressBar) findViewById(R.id.progressBar);
  }

  private void fillCurrencies() {
    ArrayList<String> arrayList = (ArrayList<String>)
        getIntent().getSerializableExtra(SplashActivity.KEY_CURRENCIES_BUNDLE);
    Collections.sort(arrayList);
    mCurrencies = arrayList.toArray(new String[arrayList.size()]);
  }

  private void defineAdapter() {
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_closed,
        mCurrencies);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mHomSpinner.setAdapter(arrayAdapter);
    mForSpinner.setAdapter(arrayAdapter);
  }

  private void defineSpinnersValues(Bundle savedInstanceState) {
    if(savedInstanceState == null && (SharedPreferencesHelper.getString(this, FOR) == null
        && SharedPreferencesHelper.getString(this, HOM) == null)) {
      mForSpinner.setSelection(findPositionGivenCode("CNY", mCurrencies));
      mHomSpinner.setSelection(findPositionGivenCode("USD", mCurrencies));
      defineKeys(FOR, "CNY");
      defineKeys(HOM, "USD");
    } else {
      mForSpinner.setSelection(findPositionGivenCode(SharedPreferencesHelper.getString(this, FOR),
          mCurrencies));
      mHomSpinner.setSelection(findPositionGivenCode(SharedPreferencesHelper.getString(this, HOM),
          mCurrencies));
    }
  }

  private void invertCurrencies() {
    int nFor = mForSpinner.getSelectedItemPosition();
    int nHom = mHomSpinner.getSelectedItemPosition();
    mForSpinner.setSelection(nHom);
    mHomSpinner.setSelection(nFor);
    mConvertedTextView.setText("");
    defineKeys(FOR, extractCodeFromCurrency((String) mForSpinner.getSelectedItem()));
    defineKeys(HOM, extractCodeFromCurrency((String) mHomSpinner.getSelectedItem()));
  }

  private int findPositionGivenCode(String code, String[] currencies) {
    for(int i = 0; i < currencies.length; i++) {
      if(extractCodeFromCurrency(currencies[i]).equalsIgnoreCase(code)) {
        return i;
      }
    }
    return 0;
  }

  private String extractCodeFromCurrency(String currency) {
    return currency.substring(0, 3);
  }

  private void defineKeys(String key, String value) {
    SharedPreferencesHelper.setString(this, key, value);
  }

  // ###################################
  // INNER CLASS OPEN EXCHANGE
  // ###################################
  private static class OpenExchange {

    public static final String RATES = "rates";
    public static final String URL_BASE = "http://openexchangerates.org/api/latest.json?app_id=";
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00000");
    public static final String PROP_KEY_OPEN_EXCHANGE = "open_key";
    private static String mKey = null;
  }
  // ###################################
  // INNER CLASS CurrencyConverterTask
  // ###################################

  /**
   * <1-String, 2-Void, 3-JSONObject><br>
   * 1 parameter (String) is a parameter of doInBackground() method<br>
   * 2 parameter is use to send progress updates to the onProgressUpdate() method<br>
   * 3 parameter is a return value of doInBackground() and an input parameter
   * of the onPostExecute() methods.
   */
  private class CurrencyConverterTask extends AsyncTask<String, Void, JSONObject> {
    private ProgressDialog progressDialog;
    /**
     * Runs on the UI thread before {@link #doInBackground}.
     *
     * @see #onPostExecute
     * @see #doInBackground
     */
    @Override
    protected void onPreExecute() {
      String strAmount = mAmountEditText.getText().toString();
      if(strAmount == null || strAmount.isEmpty()){
        Toast.makeText(ExchangeActivity.this,
            ExchangeActivity.this.getString(R.string.amount_null),
                Toast.LENGTH_LONG).show();
        super.cancel(true);
        return;
      }
      mProgress.setVisibility(View.VISIBLE);
      progressDialog = new ProgressDialog(ExchangeActivity.this);
      progressDialog.setTitle(ExchangeActivity.this.getString(R.string.calculating_result));
      progressDialog.setMessage(ExchangeActivity.this.getString(R.string.one_moment_please));
      progressDialog.setCancelable(true);
      progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
          ExchangeActivity.this.getString(R.string.cancel),
          new DialogInterface.OnClickListener() {
            /**
             * This method will be invoked when a button in the dialog is clicked.
             *
             * @param dialog The dialog that received the click.
             * @param which  The button that was clicked (e.g.
             *               {@link DialogInterface#BUTTON1}) or the position
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
              CurrencyConverterTask.this.cancel(true);
              progressDialog.dismiss();
              mProgress.setVisibility(View.INVISIBLE);
            }
          });
      progressDialog.show();
    }

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
      return new JSONParser().getJSONFromUrl(params[0]);
    }

    /**
     * Runs on the UI thread after {@link #publishProgress} is invoked.
     * The specified values are the values passed to {@link #publishProgress}.
     *
     * @param values The values indicating progress.
     * @see #publishProgress
     * @see #doInBackground
     */
    @Override
    protected void onProgressUpdate(Void... values) {
      super.onProgressUpdate(values);
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
      double dCalculated = 0.0;
      String strForCode =
          extractCodeFromCurrency(mCurrencies[mForSpinner.getSelectedItemPosition()]);
      String strHomCode =
          extractCodeFromCurrency(mCurrencies[mHomSpinner.getSelectedItemPosition()]);
      String strAmount = mAmountEditText.getText().toString();
      try{
        if(jsonObject == null){
          throw new JSONException("no data available");
        }
        JSONObject jsonRates = jsonObject.getJSONObject(OpenExchange.RATES);
        if(strHomCode.equalsIgnoreCase("USD")){
          dCalculated = Double.parseDouble(strAmount) / jsonRates.getDouble(strForCode);
        }else if(strForCode.equalsIgnoreCase("USD")){
          dCalculated = Double.parseDouble(strAmount) * jsonRates.getDouble(strHomCode);
        }else{
          dCalculated = Double.parseDouble(strAmount) * jsonRates.getDouble(strHomCode)
              / jsonRates.getDouble(strForCode);
        }
      }catch(JSONException je){
        Toast.makeText(ExchangeActivity.this,
            ExchangeActivity.this.getString(R.string.error_to_conver_json),
            Toast.LENGTH_LONG).show();
      }
      mConvertedTextView.setText(OpenExchange.DECIMAL_FORMAT.format(dCalculated)
      + " " + strHomCode);
      mProgress.setVisibility(View.INVISIBLE);
      progressDialog.dismiss();
    }
  }
}
