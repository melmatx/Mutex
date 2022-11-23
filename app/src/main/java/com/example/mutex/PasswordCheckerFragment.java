package com.example.mutex;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PasswordCheckerFragment extends Fragment {

    ViewGroup root;
    EditText et_typePass;
    TextView tv_results, tv_strength;
    CheckBox cb_uppercase, cb_lowercase, cb_numbers, cb_symbols;
    Button btn_checkPass, btn_reset2;
    View divider_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_password_checker, container, false);
        et_typePass = root.findViewById(R.id.et_typePass);
        tv_results = root.findViewById(R.id.tv_results);
        tv_strength = root.findViewById(R.id.tv_strength);
        cb_uppercase = root.findViewById(R.id.cb_uppercase);
        cb_lowercase = root.findViewById(R.id.cb_lowercase);
        cb_numbers = root.findViewById(R.id.cb_numbers);
        cb_symbols = root.findViewById(R.id.cb_symbols);
        btn_checkPass = root.findViewById(R.id.btn_checkPass);
        btn_reset2 = root.findViewById(R.id.btn_reset2);
        divider_layout = root.findViewById(R.id.divider_layout);

        btn_checkPass.setEnabled(false);
        btn_reset2.setEnabled(false);

        et_typePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_checkPass.setEnabled(!et_typePass.getText().toString().equals(""));
                btn_reset2.setEnabled(!et_typePass.getText().toString().equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_typePass.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus)
            {
                et_typePass.callOnClick();
            }
            else {
                divider_layout.setVisibility(View.VISIBLE);
            }
        });
        et_typePass.setOnClickListener(v -> {
            if(tv_results.getVisibility() != View.VISIBLE) {
                divider_layout.setVisibility(View.GONE);
            }
        });

        btn_checkPass.setOnClickListener(v -> {
            hideKeyboardFrom(getContext(), et_typePass);

            String typedPass = et_typePass.getText().toString();

            btn_reset2.setEnabled(true);

            cb_uppercase.setChecked(false);
            cb_lowercase.setChecked(false);
            cb_numbers.setChecked(false);
            cb_symbols.setChecked(false);

            tv_results.setVisibility(View.VISIBLE);
            tv_strength.setVisibility(View.VISIBLE);
            cb_uppercase.setVisibility(View.VISIBLE);
            cb_lowercase.setVisibility(View.VISIBLE);
            cb_numbers.setVisibility(View.VISIBLE);
            cb_symbols.setVisibility(View.VISIBLE);
            divider_layout.setVisibility(View.VISIBLE);

            char ch;
            for(int i=0;i < typedPass.length();i++) {
                ch = typedPass.charAt(i);

                if(Character.isUpperCase(ch)) {
                    cb_uppercase.setChecked(true);
                }
                if(Character.isLowerCase(ch)) {
                    cb_lowercase.setChecked(true);
                }
                if(Character.isDigit(ch)) {
                    cb_numbers.setChecked(true);
                }
            }
            Pattern p = Pattern.compile("[^A-Za-z0-9]");
            Matcher m = p.matcher(typedPass);
            if(m.find()) {
                cb_symbols.setChecked(true);
            }

            int checked = 0;
            if(cb_uppercase.isChecked()) {
                checked += 1;
            }
            if(cb_lowercase.isChecked()) {
                checked += 1;
            }
            if(cb_numbers.isChecked()) {
                checked += 1;
            }
            if(cb_symbols.isChecked()) {
                checked += 1;
            }

            if(checked == 3 && (typedPass.length() <= 8 && typedPass.length() > 6)) {
                tv_strength.setText("Medium");
            }
            else if(checked >= 3 && typedPass.length() >= 16) {
                tv_strength.setText("Very Strong");
            }
            else if(checked >= 3 && typedPass.length() >= 8) {
                tv_strength.setText("Strong");
            }
            else if(checked >= 2 || (typedPass.length() <= 8 && typedPass.length() > 4)) {
                tv_strength.setText("Weak");
            }
            else {
                tv_strength.setText("Very Weak");
            }
        });

        btn_reset2.setOnClickListener(v -> {
            hideKeyboardFrom(getContext(), et_typePass);

            btn_reset2.setEnabled(false);

            et_typePass.setText("");
            tv_strength.setText("");

            cb_uppercase.setChecked(false);
            cb_lowercase.setChecked(false);
            cb_numbers.setChecked(false);
            cb_symbols.setChecked(false);

            tv_results.setVisibility(View.INVISIBLE);
            tv_strength.setVisibility(View.INVISIBLE);
            cb_uppercase.setVisibility(View.INVISIBLE);
            cb_lowercase.setVisibility(View.INVISIBLE);
            cb_numbers.setVisibility(View.INVISIBLE);
            cb_symbols.setVisibility(View.INVISIBLE);
        });

        return root;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}