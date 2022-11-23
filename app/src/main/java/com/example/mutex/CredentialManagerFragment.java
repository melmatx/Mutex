package com.example.mutex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CredentialManagerFragment extends Fragment {

    public static final String cred_ID = "credID";
    ViewGroup root;
    LinearLayout layout_container;
    ConstraintLayout empty_container;
    FloatingActionButton fab_add;

    SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_credential_manager, container, false);
        layout_container = root.findViewById(R.id.layout_container);
        empty_container = root.findViewById(R.id.empty_container);
        fab_add = root.findViewById(R.id.fab_add);

        fab_add.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddCredentialActivity.class);
            startActivity(intent);
        });

        sp = getActivity().getSharedPreferences(PinActivity.my_PREFERENCES, Context.MODE_PRIVATE);
        String saved_username = sp.getString(LoginActivity.PREF_username, "");
        String saved_password = sp.getString(LoginActivity.PREF_password, "");

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        Cursor cursor1 = dbHelper.getUserDetails(saved_username, saved_password);
        int userID = cursor1.getInt(0);

        Cursor cursor2 = dbHelper.getCredentials(userID);
        if(cursor2.getCount() != 0) {
            empty_container.setVisibility(View.GONE);

            do {
                CardView cv = new CardView(getContext());

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
                lp.setMargins(30, 30, 30, 30);

                LinearLayout ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setPadding(30, 30, 30, 30);
                cv.setLayoutParams(lp);

                int credID = cursor2.getInt(0);

                ll.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), ViewCredentialActivity.class);
                    intent.putExtra(cred_ID, credID);
                    startActivity(intent);
                });

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int[] attrs = new int[]{R.attr.selectableItemBackground};
                    TypedArray typedArray = getContext().obtainStyledAttributes(attrs);
                    int selectableItemBackground = typedArray.getResourceId(0, 0);
                    typedArray.recycle();

                    ll.setForeground(getContext().getDrawable(selectableItemBackground));
                    ll.setClickable(true);
                }

                TextView tv_label = new TextView(getContext());
                TextView tv_name = new TextView(getContext());
                TextView tv_user = new TextView(getContext());
                tv_label.setTextAppearance(getContext(), R.style.TextAppearance_AppCompat_Title);
                tv_label.setText(cursor2.getString(1));
                tv_name.setText("Name: " + cursor2.getString(2));
                tv_user.setText("Username/Email: " + cursor2.getString(3));

                ll.addView(tv_label);
                ll.addView(tv_name);
                ll.addView(tv_user);
                cv.addView(ll);
                layout_container.addView(cv);
            }
            while(cursor2.moveToNext());
        }
        return root;
    }
}