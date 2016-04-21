package br.com.gbvbahia.currencies;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Guilherme on 4/19/16.
 */
public class ExchangeActivityTest extends ActivityInstrumentationTestCase2<ExchangeActivity>{

  ExchangeActivity mExchangeActivity;
  Button mCalcButton;
  TextView mConvertedTextView;
  EditText mAmountEditText;
  Spinner mForSpinner, mHomSpinner;
  /**
   * Creates an {@link ActivityInstrumentationTestCase2}.
   *
   * @param ExchangeActivity.class The activity to test. This must be a class in the instrumentation
   *                      targetPackage specified in the AndroidManifest.xml
   */
  public ExchangeActivityTest() {
    super(ExchangeActivity.class);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();

    ArrayList<String> bogusCurrencies = new ArrayList<>();
    bogusCurrencies.add("USD|United States Dollar");
    bogusCurrencies.add("EUR|Euro");
    Intent intent = new Intent();
    intent.putExtra(SplashActivity.KEY_CURRENCIES_BUNDLE, bogusCurrencies);
    setActivityIntent(intent);

    mExchangeActivity = getActivity();
    mCalcButton = (Button) mExchangeActivity.findViewById(R.id.btn_calc);
    mConvertedTextView = (TextView) mExchangeActivity.findViewById(R.id.txt_converted);
    mAmountEditText = (EditText) mExchangeActivity.findViewById(R.id.edt_amount);
    mForSpinner = (Spinner) mExchangeActivity.findViewById(R.id.spn_for);
    mHomSpinner = (Spinner) mExchangeActivity.findViewById(R.id.spn_hom);
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }

  // ###################################
  // TEST METHODS
  // ###################################

  public void testInteger() throws Throwable {
    proxyCurrencyConverterTask("12");
  }

  public void testFloat() throws Throwable {
    proxyCurrencyConverterTask("12..3");
  }
// ###################################
// AUXILIARY METHODS
// ###################################

  public void proxyCurrencyConverterTask (final String str) throws Throwable {
    final CountDownLatch latch = new CountDownLatch(1);
    mExchangeActivity.setCallback(new TaskCallback() {
      @Override
      public void executionDone() {
        latch.countDown();
        assertEquals(convertToDouble(mConvertedTextView.getText().toString().
            substring(0, 5)),convertToDouble( str));
      } });
    runTestOnUiThread(new Runnable() {
      @Override
      public void run() {
        mAmountEditText.setText(str);
        mForSpinner.setSelection(0);
        mHomSpinner.setSelection(0);
        mCalcButton.performClick();
      } });
    latch.await(30, TimeUnit.SECONDS);
  }

  private double convertToDouble(String str) throws NumberFormatException{
    double dReturn = 0;
    try {
      dReturn = Double.parseDouble(str);
    } catch (NumberFormatException e) {
      throw e; }
    return dReturn;
  }
}
