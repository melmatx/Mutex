package com.example.mutex;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Random;

public class PasswordGeneratorFragment extends Fragment {

    ViewGroup root;
    EditText et_generatedPass, et_length;
    SwitchMaterial sw_uppercase, sw_lowercase, sw_number, sw_symbol;
    FloatingActionButton fab_copy;
    Button btn_generate, btn_reset;

    String random_string = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_password_generator, container, false);
        et_generatedPass = root.findViewById(R.id.et_generatedPass);
        et_length = root.findViewById(R.id.et_length);
        sw_uppercase = root.findViewById(R.id.sw_uppercase);
        sw_lowercase = root.findViewById(R.id.sw_lowercase);
        sw_number = root.findViewById(R.id.sw_number);
        sw_symbol = root.findViewById(R.id.sw_symbol);
        fab_copy = root.findViewById(R.id.fab_copy);
        btn_generate = root.findViewById(R.id.btn_generate);
        btn_reset = root.findViewById(R.id.btn_reset);

        char[] uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] lowercase = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] numbers = "0123456789".toCharArray();
        char[] symbols = "~`!@#$%^&*()_-+={[}]|\\:;\"'<,>.?/".toCharArray();

        fab_copy.setEnabled(false);
        btn_reset.setEnabled(false);

        fab_copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Generated Password", random_string);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(getActivity(), "Generated password copied to clipboard.", Toast.LENGTH_SHORT).show();
        });

        sw_uppercase.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!sw_uppercase.isChecked() && !sw_lowercase.isChecked() && !sw_number.isChecked() && !sw_symbol.isChecked()) {
                sw_uppercase.setChecked(true);
                Toast.makeText(getActivity(), "At least one should be selected!", Toast.LENGTH_SHORT).show();
            }
            btn_reset.setEnabled(!sw_uppercase.isChecked() || !sw_lowercase.isChecked() || !sw_number.isChecked() || sw_symbol.isChecked());
        });

        sw_lowercase.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!sw_uppercase.isChecked() && !sw_lowercase.isChecked() && !sw_number.isChecked() && !sw_symbol.isChecked()) {
                sw_lowercase.setChecked(true);
                Toast.makeText(getActivity(), "At least one should be selected!", Toast.LENGTH_SHORT).show();
            }
            btn_reset.setEnabled(!sw_uppercase.isChecked() || !sw_lowercase.isChecked() || !sw_number.isChecked() || sw_symbol.isChecked());
        });

        sw_number.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!sw_uppercase.isChecked() && !sw_lowercase.isChecked() && !sw_number.isChecked() && !sw_symbol.isChecked()) {
                sw_number.setChecked(true);
                Toast.makeText(getActivity(), "At least one should be selected!", Toast.LENGTH_SHORT).show();
            }
            btn_reset.setEnabled(!sw_uppercase.isChecked() || !sw_lowercase.isChecked() || !sw_number.isChecked() || sw_symbol.isChecked());
        });

        sw_symbol.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!sw_uppercase.isChecked() && !sw_lowercase.isChecked() && !sw_number.isChecked() && !sw_symbol.isChecked()) {
                sw_symbol.setChecked(true);
                Toast.makeText(getActivity(), "At least one should be selected!", Toast.LENGTH_SHORT).show();
            }
            btn_reset.setEnabled(!sw_uppercase.isChecked() || !sw_lowercase.isChecked() || !sw_number.isChecked() || sw_symbol.isChecked());
        });

        et_length.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_reset.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_length.getText().toString().equals("16")) {
                    btn_reset.setEnabled(false);
                }
            }
        });

        btn_generate.setOnClickListener(v -> {
            String strLength = et_length.getText().toString();

            if(strLength.equals("")) {
                Toast.makeText(getActivity(), "Please enter a length!", Toast.LENGTH_SHORT).show();
            }
            else {
                int intLength = Integer.parseInt(strLength);

                StringBuilder sb1 = new StringBuilder();

                if(sw_uppercase.isChecked()) {
                    sb1.append(uppercase);
                }

                if(sw_lowercase.isChecked()) {
                    sb1.append(lowercase);
                }

                if(sw_number.isChecked()) {
                    sb1.append(numbers);
                }

                if(sw_symbol.isChecked()) {
                    sb1.append(symbols);
                }

                char[] chars = sb1.toString().toCharArray();

                StringBuilder sb2 = new StringBuilder(intLength);
                Random random = new Random();
                for (int i = 0; i < intLength; i++)
                {
                    char c1 = chars[random.nextInt(chars.length)];
                    sb2.append(c1);
                }
                random_string = sb2.toString();
                et_generatedPass.setText(random_string);

                fab_copy.setEnabled(true);
                btn_reset.setEnabled(true);
            }
        });

        btn_reset.setOnClickListener(v -> {
            et_generatedPass.setText("");
            et_length.setText("16");
            sw_uppercase.setChecked(true);
            sw_lowercase.setChecked(true);
            sw_number.setChecked(true);
            sw_symbol.setChecked(false);
            fab_copy.setEnabled(false);
            btn_reset.setEnabled(false);
        });

        return root;
    }
}