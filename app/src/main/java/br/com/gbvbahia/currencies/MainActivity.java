package br.com.gbvbahia.currencies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  private Button mCalcButton;
  private TextView mConvertedTextView;
  private EditText mAmountEditText;
  private Spinner mForSpinner, mHomSpinner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    referenceViewObjects();
  }

  private void referenceViewObjects() {
    mConvertedTextView = (TextView) findViewById(R.id.txt_converted);
    mAmountEditText = (EditText) findViewById(R.id.edt_amount);
    mCalcButton = (Button) findViewById(R.id.btn_calc);
    mForSpinner = (Spinner) findViewById(R.id.spn_for);
    mHomSpinner = (Spinner) findViewById(R.id.spn_hom);
  }
}
