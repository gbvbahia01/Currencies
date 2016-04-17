package br.com.gbvbahia.currencies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import br.com.gbvbahia.currencies.util.NetworkHelper;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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

    mHomSpinner.setOnItemSelectedListener(this);
    mForSpinner.setOnItemSelectedListener(this);
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
      switch(parent.getId()){
        case R.id.spn_for:
          //TODO
          break;
        case R.id.spn_hom:
          //TODO
          break;

        default:
          break;
      }
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

  private int findPositionGivenCode(String code, String[] currencies){
    for(int i = 0; i < currencies.length; i++){
      if(extractCodeFromCurrency(currencies[i]).equalsIgnoreCase(code)){
        return i;
      }
    }
    return 0;
  }

  private String extractCodeFromCurrency(String currency) {
    return currency.substring(0,3);
  }
}
