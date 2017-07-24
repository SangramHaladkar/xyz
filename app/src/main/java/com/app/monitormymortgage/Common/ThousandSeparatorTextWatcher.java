package com.app.monitormymortgage.Common;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Created by Sangram on 12/05/16.
 */
public class ThousandSeparatorTextWatcher implements TextWatcher {

    EditText editText;
    private final Pattern sPattern = Pattern.compile("^([0-9]{0,10}+)(\\.([0-9]{0,2}+)?)?$");
    private CharSequence mText = "";

    private boolean isValid(CharSequence s) {
        return sPattern.matcher(s).matches();
    }

    public ThousandSeparatorTextWatcher(final EditText editText) {
        this.editText = editText;


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

        if (str1.length() >= 10) {
            str1 = str1.substring(0, 10);
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
            editText.addTextChangedListener(this);
        }
    }
}
