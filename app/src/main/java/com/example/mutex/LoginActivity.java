package com.example.mutex;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    public static final String PREF_username = "username";
    public static final String PREF_password = "password";
    public static final String PREF_isLogged = "isLogged";

    ActionBar actionBar;
    SwitchMaterial darkMode;
    EditText et_username2, et_password2;
    TextInputLayout layout_username2, layout_password2;
    Button btn_login, btn_registerAct;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        darkMode = findViewById(R.id.sw_darkMode);
        et_username2 = findViewById(R.id.et_username2);
        et_password2 = findViewById(R.id.et_password2);
        layout_username2 = findViewById(R.id.layout_username2);
        layout_password2 = findViewById(R.id.layout_password2);
        btn_login = findViewById(R.id.btn_login);
        btn_registerAct = findViewById(R.id.btn_registerAct);

        actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                darkMode.setChecked(true);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                darkMode.setChecked(false);
                break;
        }

        darkMode.setOnClickListener(v -> {
            if (darkMode.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        btn_login.setOnClickListener(v -> {
            String username = et_username2.getText().toString();
            String password = et_password2.getText().toString();

            if (username.equals("")) {
                layout_username2.setError("Please enter a username");
                layout_username2.requestFocus();
            }
            if (password.equals("")) {
                layout_password2.setError("Please enter a password");
                layout_password2.requestFocus();
            } else {
                DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this);
                boolean isExisting = dbHelper.validateLogin(username, password);
                if (isExisting) {
                    sp = getSharedPreferences(PinActivity.my_PREFERENCES, MODE_PRIVATE);

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(PREF_username, username);
                    editor.putString(PREF_password, password);
                    editor.putBoolean(PREF_isLogged, true);
                    editor.commit();

                    Intent intent = new Intent(this, PinActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isDisabled = dbHelper.checkDisabled(username, password);
                    if(isDisabled) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        builder.setMessage("Are you sure you want to re-enable this account?");
                        builder.setTitle("Disabled Account");
                        builder.setPositiveButton("OK", (dialog, which) -> {
                            Cursor cursor = dbHelper.getUserDetails(username, password);
                            int userID = cursor.getInt(0);

                            boolean isEnabled = dbHelper.enableUser(userID);
                            if(isEnabled) {
                                Toast.makeText(this, "Account re-enabled successfully! Please log-in again.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(this, "Error re-enabling account!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", (dialog, which) ->
                                Toast.makeText(this, "This account is disabled!", Toast.LENGTH_SHORT).show());

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else {
                        Toast.makeText(this, "Username/Password is incorrect!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_registerAct.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });


        et_username2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_username2.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_password2.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}