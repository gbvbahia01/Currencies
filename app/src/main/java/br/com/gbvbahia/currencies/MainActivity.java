package br.com.gbvbahia.currencies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import br.com.gbvbahia.currencies.util.NetworkHelper;

public class MainActivity extends AppCompatActivity {

  private Button mCalcButton;
  private TextView mConvertedTextView;
  private EditText mAmountEditText;
  private Spinner mForSpinner, mHomSpinner;
  private String[] mCurrencies;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    referenceViewObjects();
    fillCurrencies();
    defineAdapter();
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
    switch(id){
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

  // ###################################
  // PRIVATE METHODS
  // ###################################

  private void defineAdapter() {
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
        R.layout.spinner_closed, mCurrencies);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mHomSpinner.setAdapter(arrayAdapter);
    mForSpinner.setAdapter(arrayAdapter);
  }

  private void fillCurrencies() {
    ArrayList<String> arrayList =
        (ArrayList<String>) getIntent().getSerializableExtra(SplashActivity.KEY_CURRENCIES_BUNDLE);
    Collections.sort(arrayList);
    mCurrencies = arrayList.toArray(new String[arrayList.size()]);
  }

  private void referenceViewObjects() {
    mConvertedTextView = (TextView) findViewById(R.id.txt_converted);
    mAmountEditText = (EditText) findViewById(R.id.edt_amount);
    mCalcButton = (Button) findViewById(R.id.btn_calc);
    mForSpinner = (Spinner) findViewById(R.id.spn_for);
    mHomSpinner = (Spinner) findViewById(R.id.spn_hom);
  }

  private void invertCurrencies(){
    int nFor = mForSpinner.getSelectedItemPosition();
    int nHom = mHomSpinner.getSelectedItemPosition();
    mForSpinner.setSelection(nHom);
    mHomSpinner.setSelection(nFor);
    mConvertedTextView.setText("");
  }
}
