package com.example.mutex;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class PinActivity extends AppCompatActivity {

    public static final String my_PREFERENCES = "UserPrefs";
    public static final String PREF_pin = "pin";

    ActionBar actionBar;
    SwitchMaterial darkMode2;
    ConstraintLayout cl_choosePin, cl_enterPin;
    EditText et_pin, et_pin2;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        cl_choosePin = findViewById(R.id.cl_choosePin);
        cl_enterPin = findViewById(R.id.cl_enterPin);
        et_pin = findViewById(R.id.et_pin);
        et_pin2 = findViewById(R.id.et_pin2);
        darkMode2 = findViewById(R.id.sw_darkMode2);

        sp = getSharedPreferences(my_PREFERENCES, MODE_PRIVATE);

        boolean loggedIn = sp.getBoolean(LoginActivity.PREF_isLogged, false);

        if(loggedIn) {
            boolean isChangePin = getIntent().getBooleanExtra(MainActivity.EXTRA_MESSAGE, false);
            String savedPin = sp.getString(PREF_pin, "");

            if(savedPin.equals("") || isChangePin) {
                cl_enterPin.setVisibility(View.GONE);
                cl_choosePin.setVisibility(View.VISIBLE);
            }
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                darkMode2.setChecked(true);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                darkMode2.setChecked(false);
                break;
        }

        darkMode2.setOnClickListener(v -> {
            if (darkMode2.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    public void startMain(View view) {
        sp = getSharedPreferences(my_PREFERENCES, MODE_PRIVATE);

        String savedPin = sp.getString(PREF_pin, "");
        String enteredPin = et_pin2.getText().toString();

        if(enteredPin.length() == 4) {
            if(savedPin.equals(et_pin2.getText().toString())) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Incorrect Pin!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Pin should be 4 digits!", Toast.LENGTH_SHORT).show();
        }
    }

    public void choosePin(View view) {
        sp = getSharedPreferences(my_PREFERENCES, MODE_PRIVATE);

        String pin = et_pin.getText().toString();

        if(pin.length() == 4) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(PREF_pin, pin);
            editor.commit();

            cl_choosePin.setVisibility(View.GONE);
            cl_enterPin.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(this, "Pin should be 4 digits!", Toast.LENGTH_SHORT).show();
        }
    }

    public void forgotPin(View view) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        final EditText userBox = new EditText(this);
        userBox.setHint("Username/Email");
        layout.addView(userBox);

        final EditText passBox = new EditText(this);
        passBox.setHint("Password");
        layout.addView(passBox);

        builder1.setView(layout);
        builder1.setTitle("Please enter saved login:");
        builder1.setPositiveButton("OK", (dialog, which) -> {
            sp = getSharedPreferences(PinActivity.my_PREFERENCES, MODE_PRIVATE);

            String saved_username = sp.getString(LoginActivity.PREF_username, "");
            String saved_password = sp.getString(LoginActivity.PREF_password, "");

            String username = userBox.getText().toString();
            String password = passBox.getText().toString();

            if(username.equals(saved_username) && password.equals(saved_password)) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);

                builder2.setMessage("This will reset your pin. Changes can't be undone.");
                builder2.setTitle("Reset Pin?");
                builder2.setPositiveButton("OK", (dialog2, which2) -> {
                    et_pin.setText("");
                    et_pin2.setText("");
                    cl_enterPin.setVisibility(View.GONE);
                    cl_choosePin.setVisibility(View.VISIBLE);

                    Toast.makeText(this, "Please reset your pin!", Toast.LENGTH_SHORT).show();
                });
                builder2.setNegativeButton("Cancel", null);

                AlertDialog dialog2 = builder2.create();
                dialog2.show();
            }
            else {
                Toast.makeText(this, "Wrong username/password!", Toast.LENGTH_SHORT).show();
            }
        });
        builder1.setNegativeButton("Cancel", null);

        AlertDialog dialog1 = builder1.create();
        dialog1.show();
    }
}