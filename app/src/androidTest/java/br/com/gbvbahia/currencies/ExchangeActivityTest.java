package br.com.gbvbahia.currencies;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
   * @param activityClass The activity to test. This must be a class in the instrumentation
   *                      targetPackage specified in the AndroidManifest.xml
   */
  public ExchangeActivityTest() {
    super(ExchangeActivity.class);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
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
}
