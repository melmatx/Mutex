package com.example.mutex;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.MUTEX.MESSAGE";

    BottomNavigationView bottomNavigationView;
    ConstraintLayout constraintLayout;
    Snackbar sb_exit;

    private ViewPager2 viewPager2;

    CredentialManagerFragment credentialManagerFragment;
    PasswordGeneratorFragment passwordGeneratorFragment;
    PasswordCheckerFragment passwordCheckerFragment;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewpager2);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        constraintLayout = findViewById(R.id.constraintLayout);
        sb_exit = Snackbar.make(constraintLayout, "Press back again to exit", Snackbar.LENGTH_SHORT);
        sb_exit.setAction("Dismiss", v -> sb_exit.dismiss());

        viewPager2.setUserInputEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.credential_manager) {
                viewPager2.setCurrentItem(0,false);
            }
            else if (id == R.id.password_generator) {
                viewPager2.setCurrentItem(1,false);
            }
            else if (id == R.id.password_checker) {
                viewPager2.setCurrentItem(2,false);
            }
            return false;
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.credential_manager).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.password_generator).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.password_checker).setChecked(true);
                        break;
                }
            }
        });

        setupViewPager(viewPager2);

    }

    private void setupViewPager(ViewPager2 viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        credentialManagerFragment = new CredentialManagerFragment();
        passwordGeneratorFragment = new PasswordGeneratorFragment();
        passwordCheckerFragment = new PasswordCheckerFragment();

        adapter.addFragment(credentialManagerFragment);
        adapter.addFragment(passwordGeneratorFragment);
        adapter.addFragment(passwordCheckerFragment);

        viewPager.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.account_settings) {
            Intent intent = new Intent(this, AccountSettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.change_pin) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("This will allow you to change the pin for the current session.");
            builder.setTitle("Change Pin?");
            builder.setPositiveButton("OK", (dialog, which) -> {
                Intent intent = new Intent(this, PinActivity.class);
                intent.putExtra(EXTRA_MESSAGE, true);
                startActivity(intent);
            });
            builder.setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        if(id == R.id.reset_entry) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("This will delete all the credentials of the current account.");
            builder.setTitle("Reset Entries?");
            builder.setPositiveButton("OK", (dialog, which) -> {
                sp = getSharedPreferences(PinActivity.my_PREFERENCES, Context.MODE_PRIVATE);
                String saved_username = sp.getString(LoginActivity.PREF_username, "");
                String saved_password = sp.getString(LoginActivity.PREF_password, "");

                DatabaseHelper dbHelper = new DatabaseHelper(this);

                Cursor cursor1 = dbHelper.getUserDetails(saved_username, saved_password);
                int userID = cursor1.getInt(0);

                boolean isReset = dbHelper.removeAllCredentials(userID);
                if(isReset) {
                    Toast.makeText(this, "Entry Reset Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                }
                else {
                    Toast.makeText(this, "No Entries Found!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        if(id == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (sb_exit.isShown()) {
            finishAffinity();
        } else {
            sb_exit.show();
        }
    }
}