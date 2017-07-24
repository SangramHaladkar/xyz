package com.app.monitormymortgage.Common;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.app.monitormymortgage.Activity.AddMortgageActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Created by Sangram on 10/05/16.
 */
public class CustomTextWatcherInterestRate implements TextWatcher {
    EditText editText;
    private final Pattern sPattern = Pattern.compile("^([0-9]{0,2}+)(\\.([0-9]{0,2}+)?)?$");
    private CharSequence mText = "";
    NumberFormat nf;
    DecimalFormat df;
    DecimalFormat f;

    public CustomTextWatcherInterestRate(final EditText editText) {
        this.editText = editText;
        nf = NumberFormat.getInstance();
        df = (DecimalFormat) nf;
        df.applyPattern("#,###.00");
        f = new DecimalFormat("0.00");
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
                    symbols.setGroupingSeparator(',');
                    DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
                    StringTokenizer lst = new StringTokenizer(editText.getText().toString(), ".");
                    String str1 = editText.getText().toString();
                    String str2 = "";
                    if (lst.countTokens() > 1) {
                        str1 = lst.nextToken();
                        str2 = lst.nextToken();
                    }

                    if (str2.length() >= 3) {
                        str2 = str2.substring(0, 3);
                    } else if (str2.length() <= 0) {
                        str2 = "00";
                    }


                    if (str1.contains(".")) {
                        str1 = str1 + str2;
                    } else {
                        str1 = str1 + "." + str2;
                    }

                    try {
                        editText.removeTextChangedListener(CustomTextWatcherInterestRate.this);
                        String formattedString = df.format(Double.valueOf(str1).doubleValue());
                        if (formattedString.contains(".")) {
                            String[] arr = formattedString.split("\\.");
                            if (arr[0].length() <= 0) {
                                formattedString = "0." + arr[1];
                            }

                        }
                        editText.setText(formattedString);
                        editText.addTextChangedListener(CustomTextWatcherInterestRate.this);
                        // CR-4 changes
                        if (!editText.getText().toString().equals("")) {
                            if (!AddMortgageActivity.operatorTextView.getText().equals("")) {
                                double total = 0.00;
                                AddMortgageActivity.interestOne = Double.parseDouble(editText.getText().toString());
                                Log.v("TAG", "" + AddMortgageActivity.interestOne);
                                if (!AddMortgageActivity.operatorTextView.getText().toString().equalsIgnoreCase("") && AddMortgageActivity.operatorTextView.getText().toString().equalsIgnoreCase("+")) {
                                    total = AddMortgageActivity.primeRate + AddMortgageActivity.interestOne;
                                } else {
                                    total = AddMortgageActivity.primeRate + (-AddMortgageActivity.interestOne);
                                }
                                // Round up to nearest 2 decimal digit.
                                total = total * 100;
                                total = Math.round(total);
                                total = total / 100;
                                String totalStr = String.valueOf(total);
                                AddMortgageActivity.variableInterestRateTextView.setText(String.valueOf(f.format(total)));
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    StringTokenizer lst = new StringTokenizer(editText.getText().toString(), ".");
                    String str1 = editText.getText().toString();
                    String str2 = "";
                    if (lst.countTokens() > 1) {
                        str1 = lst.nextToken();
                        str2 = lst.nextToken();
                    }

                    if (str2.length() >= 2) {
                        str2 = str2.substring(0, 2);
                    } else if (str2.length() <= 0) {
                        str2 = "00";
                    }
                    if (str1.length() > 0) {
                        if (!str1.contains(".")) {
                            str1 = str1 + "." + str2;
                        }
                    }
                    editText.removeTextChangedListener(CustomTextWatcherInterestRate.this);
                    editText.setText(str1.replace(",", ""));
                    editText.addTextChangedListener(CustomTextWatcherInterestRate.this);

                }
            }
        });

    }

    private boolean isValid(CharSequence s) {
        return sPattern.matcher(s).matches();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        StringTokenizer lst = new StringTokenizer(s.toString(), ".");
        String str1 = s.toString();
        String str2 = "";
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }

        if (str1.length() >= 2) {
            str1 = str1.substring(0, 2);
        }
        if (str2.length() > 0) {
            if (str2.length() >= 2) {
                str2 = str2.substring(0, 2);
            } else if (str2.length() <= 0) {
                str2 = "00";
            }
            str1 = str1 + "." + str2;
        }
        mText = str1;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!isValid(s)) {
            editText.removeTextChangedListener(this);
            editText.setText(mText);
            editText.setSelection(editText.length());
            editText.addTextChangedListener(this);
        }
    }
}
