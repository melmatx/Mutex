package com.example.mutex;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class AccountSettingsActivity extends AppCompatActivity {

    public static final String PREF_selected_icon = "selectedIcon";
    public static final String PREF_checked_item = "checkedItem";
    TextView tv_current, tv_total;
    EditText et_name2, et_user2, et_pass2;
    TextInputLayout layout_name2, layout_user2, layout_pass2;
    ImageView iv_graphic_settings, iv_graphic_settings_2;
    Button btn_logout, btn_saveChanges, btn_discard;

    Menu myMenu;
    DatabaseHelper dbHelper;
    SharedPreferences sp;

    int userID, checkedItem;
    String name, username, password, selectedIcon, lastIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        tv_current = findViewById(R.id.tv_current);
        tv_total = findViewById(R.id.tv_total);
        et_name2 = findViewById(R.id.et_name2);
        et_user2 = findViewById(R.id.et_user2);
        et_pass2 = findViewById(R.id.et_pass2);
        layout_name2 = findViewById(R.id.layout_name2);
        layout_user2 = findViewById(R.id.layout_user2);
        layout_pass2 = findViewById(R.id.layout_pass2);
        iv_graphic_settings = findViewById(R.id.iv_graphic_settings);
        iv_graphic_settings_2 = findViewById(R.id.iv_graphic_settings_2);
        btn_logout = findViewById(R.id.btn_logout);
        btn_saveChanges = findViewById(R.id.btn_saveChanges);
        btn_discard = findViewById(R.id.btn_discard);

        //Get current account details
        sp = getSharedPreferences(PinActivity.my_PREFERENCES, MODE_PRIVATE);

        String saved_username = sp.getString(LoginActivity.PREF_username, "");
        String saved_password = sp.getString(LoginActivity.PREF_password, "");

        dbHelper = new DatabaseHelper(this);

        Cursor cursor1 = dbHelper.getUserDetails(saved_username, saved_password);

        if(cursor1.getCount() != 0) {
            userID = cursor1.getInt(0);
            name = cursor1.getString(1);
            username = cursor1.getString(2);
            password = cursor1.getString(3);
        }

        //Set current account details
        et_name2.setText(name);
        et_user2.setText(username);
        et_pass2.setText(password);


        //Set last saved profile icon
        sp = getSharedPreferences(PinActivity.my_PREFERENCES, MODE_PRIVATE);

        selectedIcon = sp.getString(PREF_selected_icon, "male");

        if (selectedIcon.equals("male")) {
            iv_graphic_settings_2.setVisibility(View.GONE);
            iv_graphic_settings.setVisibility(View.VISIBLE);
        }
        else if (selectedIcon.equals("female")) {
            iv_graphic_settings.setVisibility(View.INVISIBLE);
            iv_graphic_settings_2.setVisibility(View.VISIBLE);
        }
        lastIcon = selectedIcon;
        selectedIcon = lastIcon;
        checkedItem = sp.getInt(PREF_checked_item, 0);


        //Get credentials count
        Cursor cursor2 = dbHelper.getCredentials(userID);

        int credentialsCount = cursor2.getCount();

        tv_total.setText("Total credentials saved: " + credentialsCount);

        et_name2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_name2.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_name2.getText().toString().equals("")) {
                    layout_name2.setError("Please enter a name!");
                }
            }
        });

        et_user2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_user2.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_user2.getText().toString().equals("")) {
                    layout_user2.setError("Please enter a username or an email!");
                }
            }
        });

        et_pass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_pass2.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_pass2.getText().toString().equals("")) {
                    layout_pass2.setError("Please enter a password!");
                }
            }
        });
    }

    public void changeProfile(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change profile icon");
        builder.setSingleChoiceItems(R.array.changeProfile, checkedItem, (dialog, which) -> {
            switch (which) {
                case 0:
                    selectedIcon = "male";
                    checkedItem = 0;
                    break;
                case 1:
                    selectedIcon = "female";
                    checkedItem = 1;
                    break;
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            if(!lastIcon.equals(selectedIcon)) {
                if (selectedIcon.equals("male")) {
                    iv_graphic_settings_2.setVisibility(View.GONE);
                    iv_graphic_settings.setVisibility(View.VISIBLE);
                }
                else if (selectedIcon.equals("female")) {
                    iv_graphic_settings.setVisibility(View.INVISIBLE);
                    iv_graphic_settings_2.setVisibility(View.VISIBLE);
                }
                sp = getSharedPreferences(PinActivity.my_PREFERENCES, MODE_PRIVATE);

                SharedPreferences.Editor editor = sp.edit();
                editor.putString(PREF_selected_icon, selectedIcon);
                editor.putInt(PREF_checked_item, checkedItem);
                editor.commit();

                lastIcon = selectedIcon;
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void saveChanges(View view) {
        String nameEdit = et_name2.getText().toString();
        String usernameEdit = et_user2.getText().toString();
        String passwordEdit = et_pass2.getText().toString();

        if (nameEdit.equals("")) {
            layout_name2.setError("Please enter a name!");
            layout_name2.requestFocus();
        }
        else if (usernameEdit.equals("")) {
            layout_user2.setError("Please enter a username or an email!");
            layout_user2.requestFocus();
        }
        else if (passwordEdit.equals("")) {
            layout_pass2.setError("Please enter a password!");
            layout_pass2.requestFocus();
        }
        else if (passwordEdit.length() < 8) {
            Toast.makeText(this, "You must have at least 8 characters in your password!", Toast.LENGTH_SHORT).show();
            layout_pass2.requestFocus();
        }
        else {
            boolean uppercaseCheck = false;
            boolean lowercaseCheck = false;
            boolean numberCheck = false;

            char ch;
            for(int i=0;i < passwordEdit.length();i++) {
                ch = passwordEdit.charAt(i);

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
                dbHelper = new DatabaseHelper(AccountSettingsActivity.this);

                boolean isUpdatedUser = dbHelper.updateUser(userID , nameEdit, usernameEdit, passwordEdit);
                if (isUpdatedUser) {
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString(LoginActivity.PREF_username, usernameEdit);
                    editor.putString(LoginActivity.PREF_password, passwordEdit);

                    editor.commit();

                    startActivity(getIntent());
                    Toast.makeText(this, "Account Updated!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Error updating!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void discardChanges(View view) {
        tv_current.setText("Current Account:");
        et_name2.setEnabled(false);
        et_pass2.setEnabled(false);
        et_user2.setEnabled(false);
        et_name2.setText(name);
        et_user2.setText(username);
        et_pass2.setText(password);
        tv_total.setVisibility(View.VISIBLE);
        btn_logout.setVisibility(View.VISIBLE);
        btn_saveChanges.setVisibility(View.GONE);
        btn_discard.setVisibility(View.GONE);

        myMenu.findItem(R.id.edit).setVisible(true);
        myMenu.findItem(R.id.edit).setEnabled(true);
        myMenu.findItem(R.id.delete).setVisible(false);
        myMenu.findItem(R.id.delete).setEnabled(false);

        Toast.makeText(this, "Changes Discarded!", Toast.LENGTH_SHORT).show();
    }

    public void logOut(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to log out?");
        builder.setTitle("Log out");
        builder.setPositiveButton("OK", (dialog, which) -> {
            sp = getSharedPreferences(PinActivity.my_PREFERENCES, MODE_PRIVATE);

            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        myMenu = menu;
        MenuItem item = menu.findItem(R.id.edit);
        if (item != null) {
            item.setVisible(true);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.edit) {
            tv_current.setText("Edit Current Account:");
            et_name2.setEnabled(true);
            et_pass2.setEnabled(true);
            et_user2.setEnabled(true);
            tv_total.setVisibility(View.GONE);
            btn_logout.setVisibility(View.GONE);
            btn_saveChanges.setVisibility(View.VISIBLE);
            btn_discard.setVisibility(View.VISIBLE);

            myMenu.findItem(R.id.edit).setVisible(false);
            myMenu.findItem(R.id.edit).setEnabled(false);
            myMenu.findItem(R.id.delete).setVisible(true);
            myMenu.findItem(R.id.delete).setEnabled(true);
            return true;
        }
        if(id == R.id.delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("This will disable your account until further reconfiguration.");
            builder.setTitle("Disable Account?");
            builder.setPositiveButton("OK", (dialog, which) -> {
                dbHelper = new DatabaseHelper(AccountSettingsActivity.this);

                boolean isDisabledSuccess = dbHelper.disableUser(userID);
                if(isDisabledSuccess) {
                    sp = getSharedPreferences(PinActivity.my_PREFERENCES, MODE_PRIVATE);

                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();

                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();
                    Toast.makeText(this, "Account Disabled.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Error disabling!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}