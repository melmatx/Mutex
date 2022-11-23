package com.example.mutex;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class ViewCredentialActivity extends AppCompatActivity {

    EditText et_label_entry2, et_name_entry2, et_user_entry2, et_pass_entry2;
    TextInputLayout layout_label_entry2, layout_name_entry2, layout_user_entry2, layout_pass_entry2;
    Button btn_copy, btn_saveChanges2, btn_discard2;

    Menu myMenu;

    int credID, userID;
    String label_entry2, name_entry2, user_entry2, pass_entry2;

    DatabaseHelper dbHelper;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_credential);

        et_label_entry2 = findViewById(R.id.et_label_entry2);
        et_name_entry2 = findViewById(R.id.et_name_entry2);
        et_user_entry2 = findViewById(R.id.et_user_entry2);
        et_pass_entry2 = findViewById(R.id.et_pass_entry2);
        layout_label_entry2 = findViewById(R.id.layout_label_entry2);
        layout_name_entry2 = findViewById(R.id.layout_name_entry2);
        layout_user_entry2 = findViewById(R.id.layout_user_entry2);
        layout_pass_entry2 = findViewById(R.id.layout_pass_entry2);
        btn_copy = findViewById(R.id.btn_copy);
        btn_saveChanges2 = findViewById(R.id.btn_saveChanges2);
        btn_discard2 = findViewById(R.id.btn_discard2);

        sp = getSharedPreferences(PinActivity.my_PREFERENCES, MODE_PRIVATE);

        String saved_username = sp.getString(LoginActivity.PREF_username, "");
        String saved_password = sp.getString(LoginActivity.PREF_password, "");

        dbHelper = new DatabaseHelper(this);

        Cursor cursor1 = dbHelper.getUserDetails(saved_username, saved_password);

        userID = cursor1.getInt(0);

        credID = getIntent().getIntExtra(CredentialManagerFragment.cred_ID, 0);

        Cursor cursor2 = dbHelper.viewCredentials(userID, credID);

        label_entry2 = cursor2.getString(1);
        name_entry2 = cursor2.getString(2);
        user_entry2 = cursor2.getString(3);
        pass_entry2 = cursor2.getString(4);

        et_label_entry2.setText(label_entry2);
        et_name_entry2.setText(name_entry2);
        et_user_entry2.setText(user_entry2);
        et_pass_entry2.setText(pass_entry2);

        et_label_entry2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_label_entry2.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_label_entry2.getText().toString().equals("")) {
                    layout_label_entry2.setError("Please enter a label!");
                }
            }
        });

        et_name_entry2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_name_entry2.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_name_entry2.getText().toString().equals("")) {
                    layout_name_entry2.setError("Please enter a name!");
                }
            }
        });

        et_user_entry2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_user_entry2.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_user_entry2.getText().toString().equals("")) {
                    layout_user_entry2.setError("Please enter a username or an email!");
                }
            }
        });

        et_pass_entry2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout_pass_entry2.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_pass_entry2.getText().toString().equals("")) {
                    layout_pass_entry2.setError("Please enter a password!");
                }
            }
        });
    }

    public void copyToClipboard(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Copy to Clipboard");
        builder.setItems(new CharSequence[] {"Copy Label", "Copy Name", "Copy Username/Email", "Copy Password"},
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            ClipData clip1 = ClipData.newPlainText("Label", label_entry2);
                            clipboard.setPrimaryClip(clip1);
                            Toast.makeText(ViewCredentialActivity.this, "Label copied to clipboard.", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            ClipData clip2 = ClipData.newPlainText("Name", name_entry2);
                            clipboard.setPrimaryClip(clip2);
                            Toast.makeText(ViewCredentialActivity.this, "Name copied to clipboard.", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            ClipData clip3 = ClipData.newPlainText("Username/Email", user_entry2);
                            clipboard.setPrimaryClip(clip3);
                            Toast.makeText(ViewCredentialActivity.this, "Username/Email copied to clipboard.", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            ClipData clip4 = ClipData.newPlainText("Password", pass_entry2);
                            clipboard.setPrimaryClip(clip4);
                            Toast.makeText(ViewCredentialActivity.this, "Password copied to clipboard.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
        builder.create().show();
    }

    public void saveChanges(View view) {
        String labelEdit = et_label_entry2.getText().toString();
        String nameEdit = et_name_entry2.getText().toString();
        String usernameEdit = et_user_entry2.getText().toString();
        String passwordEdit = et_pass_entry2.getText().toString();

        if (labelEdit.equals("")) {
            layout_label_entry2.setError("Please enter a label!");
            layout_label_entry2.requestFocus();
        }
        else if (nameEdit.equals("")) {
            layout_name_entry2.setError("Please enter a name!");
            layout_name_entry2.requestFocus();
        }
        else if (usernameEdit.equals("")) {
            layout_user_entry2.setError("Please enter a username or an email!");
            layout_user_entry2.requestFocus();
        }
        else if (passwordEdit.equals("")) {
            layout_pass_entry2.setError("Please enter a password!");
            layout_pass_entry2.requestFocus();
        }
        else {
            dbHelper = new DatabaseHelper(this);

            boolean isUpdatedEntry = dbHelper.updateCredential(userID, credID, labelEdit, nameEdit, usernameEdit, passwordEdit);
            if (isUpdatedEntry) {
                startActivity(getIntent());
                Toast.makeText(this, "Entry Updated!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Error updating changes!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void discardChanges(View view) {
        et_label_entry2.setEnabled(false);
        et_name_entry2.setEnabled(false);
        et_user_entry2.setEnabled(false);
        et_pass_entry2.setEnabled(false);
        et_label_entry2.setText(label_entry2);
        et_name_entry2.setText(name_entry2);
        et_user_entry2.setText(user_entry2);
        et_pass_entry2.setText(pass_entry2);
        btn_copy.setVisibility(View.VISIBLE);
        btn_saveChanges2.setVisibility(View.GONE);
        btn_discard2.setVisibility(View.GONE);

        myMenu.findItem(R.id.edit).setVisible(true);
        myMenu.findItem(R.id.edit).setEnabled(true);
        myMenu.findItem(R.id.delete).setVisible(false);
        myMenu.findItem(R.id.delete).setEnabled(false);

        Toast.makeText(this, "Changes Discarded!", Toast.LENGTH_SHORT).show();
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
            et_label_entry2.setEnabled(true);
            et_name_entry2.setEnabled(true);
            et_user_entry2.setEnabled(true);
            et_pass_entry2.setEnabled(true);
            btn_copy.setVisibility(View.GONE);
            btn_saveChanges2.setVisibility(View.VISIBLE);
            btn_discard2.setVisibility(View.VISIBLE);

            myMenu.findItem(R.id.edit).setVisible(false);
            myMenu.findItem(R.id.edit).setEnabled(false);
            myMenu.findItem(R.id.delete).setVisible(true);
            myMenu.findItem(R.id.delete).setEnabled(true);
            return true;
        }
        if(id == R.id.delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("This will delete the entry and it will be irreversible.");
            builder.setTitle("Delete Entry?");
            builder.setPositiveButton("OK", (dialog, which) -> {
                dbHelper = new DatabaseHelper(this);

                boolean isDeletedEntry = dbHelper.removeCredential(userID, credID);
                if (isDeletedEntry) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Entry Deleted!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Error deleting entry!", Toast.LENGTH_SHORT).show();
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