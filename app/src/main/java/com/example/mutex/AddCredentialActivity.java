package com.example.mutex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class AddCredentialActivity extends AppCompatActivity {

    EditText et_label_entry, et_name_entry, et_user_entry, et_pass_entry;
    TextInputLayout layout_label_entry, layout_name_entry, layout_user_entry, layout_pass_entry;

    String labelEntry, nameEntry, userEntry, passEntry;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credential);

        et_label_entry = findViewById(R.id.et_label_entry);
        et_name_entry = findViewById(R.id.et_name_entry);
        et_user_entry = findViewById(R.id.et_user_entry);
        et_pass_entry = findViewById(R.id.et_pass_entry);
        layout_label_entry = findViewById(R.id.layout_label_entry);
        layout_name_entry = findViewById(R.id.layout_name_entry);
        layout_user_entry = findViewById(R.id.layout_user_entry);
        layout_pass_entry = findViewById(R.id.layout_pass_entry);

        et_label_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_label_entry.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_label_entry.getText().toString().equals("")) {
                    layout_label_entry.setError("Please enter a label!");
                }
            }
        });

        et_name_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_name_entry.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_name_entry.getText().toString().equals("")) {
                    layout_name_entry.setError("Please enter a name!");
                }
            }
        });

        et_user_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_user_entry.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_user_entry.getText().toString().equals("")) {
                    layout_user_entry.setError("Please enter a username or an email!");
                }
            }
        });

        et_pass_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_pass_entry.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_pass_entry.getText().toString().equals("")) {
                    layout_pass_entry.setError("Please enter a password!");
                }
            }
        });
    }

    public void addEntry(View view) {
        labelEntry = et_label_entry.getText().toString();
        nameEntry = et_name_entry.getText().toString();
        userEntry = et_user_entry.getText().toString();
        passEntry = et_pass_entry.getText().toString();

        if (labelEntry.equals("")) {
            layout_label_entry.setError("Please enter a label!");
            layout_label_entry.requestFocus();
        }
        else if (nameEntry.equals("")) {
            layout_name_entry.setError("Please enter a name!");
            layout_name_entry.requestFocus();
        }
        else if (userEntry.equals("")) {
            layout_user_entry.setError("Please enter a username or an email!");
            layout_user_entry.requestFocus();
        }
        else if (passEntry.equals("")) {
            layout_pass_entry.setError("Please enter a password!");
            layout_pass_entry.requestFocus();
        }
        else {
            sp = getSharedPreferences(PinActivity.my_PREFERENCES, MODE_PRIVATE);
            String saved_username = sp.getString(LoginActivity.PREF_username, "");
            String saved_password = sp.getString(LoginActivity.PREF_password, "");

            DatabaseHelper dbHelper = new DatabaseHelper(this);

            Cursor cursor = dbHelper.getUserDetails(saved_username, saved_password);
            int userID = cursor.getInt(0);

            CredentialModel credModel = new CredentialModel(-1, labelEntry, nameEntry, userEntry, passEntry, userID);

            boolean isSuccess = dbHelper.insertCredential(credModel);
            if (isSuccess) {
                Toast.makeText(this, "Entry Saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Failed to save entry!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}