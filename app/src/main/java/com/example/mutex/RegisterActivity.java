package com.example.mutex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    EditText et_name, et_username, et_password, et_confirmPass;
    TextInputLayout layout_name, layout_username, layout_password, layout_confirmPass;

    String name, username, password, confirm_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_name = findViewById(R.id.et_name);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_confirmPass = findViewById(R.id.et_confirmPass);
        layout_name = findViewById(R.id.layout_name);
        layout_username = findViewById(R.id.layout_username);
        layout_password = findViewById(R.id.layout_password);
        layout_confirmPass = findViewById(R.id.layout_confirmPass);

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_name.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_name.getText().toString().equals("")) {
                    layout_name.setError("Please enter a name!");
                }
            }
        });

        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_username.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_username.getText().toString().equals("")) {
                    layout_username.setError("Please enter a username or an email!");
                }
            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_password.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_password.getText().toString().equals("")) {
                    layout_password.setError("Please enter a password!");
                }
            }
        });

        et_confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_confirmPass.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_confirmPass.getText().toString().equals("")) {
                    layout_confirmPass.setError("Please confirm password!");
                }
            }
        });
    }

    public void registerAccount(View view) {
        name = et_name.getText().toString();
        username = et_username.getText().toString();
        password = et_password.getText().toString();
        confirm_pass = et_confirmPass.getText().toString();

        if (name.equals("")) {
            layout_name.setError("Please enter a name!");
            layout_name.requestFocus();
        }
        else if (username.equals("")) {
            layout_username.setError("Please enter a username or an email!");
            layout_username.requestFocus();
        }
        else if (password.equals("")) {
            layout_password.setError("Please enter a password!");
            layout_password.requestFocus();
        }
        else if (confirm_pass.equals("")) {
            layout_confirmPass.setError("Please confirm password!");
            layout_confirmPass.requestFocus();
        }
        else if (!password.equals(confirm_pass)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            et_confirmPass.setTransformationMethod(null);
            et_confirmPass.setSelection(et_confirmPass.length());
            layout_confirmPass.setError("Password is not the same!");
        }
        else if (password.length() < 8) {
            Toast.makeText(this, "You must have at least 8 characters in your password!", Toast.LENGTH_SHORT).show();
            layout_password.requestFocus();
        }
        else {
            boolean uppercaseCheck = false;
            boolean lowercaseCheck = false;
            boolean numberCheck = false;

            char ch;
            for(int i=0;i < password.length();i++) {
                ch = password.charAt(i);

                if(Character.isUpperCase(ch)) {
                    uppercaseCheck = true;
                }
                if(Character.isLowerCase(ch)) {
                    lowercaseCheck = true;
                }
                if(Character.isDigit(ch)) {
                    numberCheck = true;
                }
            }

            if(!uppercaseCheck || !lowercaseCheck || !numberCheck) {
                Toast.makeText(this, "You must have at least one uppercase letter, one lowercase letter, and one number in your password!", Toast.LENGTH_LONG).show();
            }
            else {
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                boolean isExisting = dbHelper.validateLogin(username, password);
                if (isExisting) {
                    Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
                }
                else {
                    UserModel userModel = new UserModel(-1, name, username, password, true);

                    dbHelper = new DatabaseHelper(this);

                    boolean isSuccess = dbHelper.insertUser(userModel);
                    if (isSuccess) {
                        Toast.makeText(this, "Account Registration Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(this, "Account Registration Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}