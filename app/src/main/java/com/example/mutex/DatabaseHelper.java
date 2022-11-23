package com.example.mutex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String USERS_TABLE = "tblUsers";
    public static final String COLUMN_USER_ID = "userID";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_IS_ACTIVE = "isActive";

    public static final String CREDENTIALS_TABLE = "tblCredentials";
    public static final String COLUMN_CREDENTIAL_ID = "credID";
    public static final String COLUMN_CREDENTIAL_LABEL = "credLabel";
    public static final String COLUMN_CREDENTIAL_NAME = "credName";
    public static final String COLUMN_CREDENTIAL_USERNAME = "credUsername";
    public static final String COLUMN_CREDENTIAL_PASSWORD = "credPassword";


    public DatabaseHelper(@Nullable Context context) {
        super(context, "mutex.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String usersTableQuery = "CREATE TABLE " + USERS_TABLE + " (" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " + COLUMN_USERNAME + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_IS_ACTIVE + " BOOL)";

        String credentialsTableQuery = "CREATE TABLE " + CREDENTIALS_TABLE + " (" + COLUMN_CREDENTIAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CREDENTIAL_LABEL + " TEXT, " + COLUMN_CREDENTIAL_NAME + " TEXT, " + COLUMN_CREDENTIAL_USERNAME + " TEXT, " + COLUMN_CREDENTIAL_PASSWORD + " TEXT, " + COLUMN_USER_ID + " INTEGER)";

        db.execSQL(usersTableQuery);
        db.execSQL(credentialsTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertUser(UserModel usermodel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, usermodel.getName());
        cv.put(COLUMN_USERNAME, usermodel.getUsername());
        cv.put(COLUMN_PASSWORD, usermodel.getPassword());
        cv.put(COLUMN_IS_ACTIVE, usermodel.isActive());

        long insert = db.insert(USERS_TABLE, null, cv);
        db.close();
        return insert != -1;
    }

    public boolean insertCredential(CredentialModel credentialModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CREDENTIAL_LABEL, credentialModel.getCredLabel());
        cv.put(COLUMN_CREDENTIAL_NAME, credentialModel.getCredName());
        cv.put(COLUMN_CREDENTIAL_USERNAME, credentialModel.getCredUsername());
        cv.put(COLUMN_CREDENTIAL_PASSWORD, credentialModel.getCredPassword());
        cv.put(COLUMN_USER_ID, credentialModel.getUserID());

        long insert = db.insert(CREDENTIALS_TABLE, null ,cv);
        db.close();
        return insert != -1;
    }

    public boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + COLUMN_USERNAME + " = '" + username + "' AND "
                + COLUMN_PASSWORD + " = '" + password + "' AND " + COLUMN_IS_ACTIVE + " = 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        boolean isExisting = cursor.moveToFirst();

        cursor.close();
        db.close();
        return isExisting;
    }

    public boolean checkDisabled(String username, String password) {
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + COLUMN_USERNAME + " = '" + username + "' AND "
                + COLUMN_PASSWORD + " = '" + password + "' AND " + COLUMN_IS_ACTIVE + " = 0";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        boolean isDisabled = cursor.moveToFirst();

        cursor.close();
        db.close();
        return isDisabled;
    }

    public Cursor getUserDetails(String saved_username, String saved_password) {
        String query = "SELECT * FROM " + USERS_TABLE
                + " WHERE " + COLUMN_USERNAME + " = '" + saved_username + "' AND " + COLUMN_PASSWORD + " = '" + saved_password + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getCredentials(int userID) {
        String query = "SELECT * FROM " + CREDENTIALS_TABLE + " WHERE " + COLUMN_USER_ID + " =?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(userID)});

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor viewCredentials(int userID, int credID) {
        String query = "SELECT * FROM " + CREDENTIALS_TABLE + " WHERE " + COLUMN_USER_ID + " =? AND "
                + COLUMN_CREDENTIAL_ID + " = " + credID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(userID)});

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public boolean removeCredential(int userID, int credID) {
        String query = "SELECT * FROM " + CREDENTIALS_TABLE + " WHERE " + COLUMN_USER_ID + " =? AND "
                + COLUMN_CREDENTIAL_ID + " = " + credID;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(userID)});

        if(cursor.getCount() > 0) {
            long delete = db.delete(CREDENTIALS_TABLE,COLUMN_USER_ID + " =? AND " + COLUMN_CREDENTIAL_ID + " = " + credID, new String[]{String.valueOf(userID)});
            cursor.close();
            db.close();
            return delete != -1;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean removeAllCredentials(int userID) {
        String query = "SELECT * FROM " + CREDENTIALS_TABLE + " WHERE " + COLUMN_USER_ID + " =?";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(userID)});

        if(cursor.getCount() > 0) {
            long delete = db.delete(CREDENTIALS_TABLE,COLUMN_USER_ID + " =?", new String[]{String.valueOf(userID)});
            cursor.close();
            db.close();
            return delete != -1;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean updateCredential(int userID, int credID, String label, String name, String user, String pass) {
        String query = "SELECT * FROM " + CREDENTIALS_TABLE + " WHERE " + COLUMN_USER_ID + " =? AND "
                + COLUMN_CREDENTIAL_ID + " = " + credID;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CREDENTIAL_LABEL, label);
        cv.put(COLUMN_CREDENTIAL_NAME, name);
        cv.put(COLUMN_CREDENTIAL_USERNAME, user);
        cv.put(COLUMN_CREDENTIAL_PASSWORD, pass);

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(userID)});

        if(cursor.getCount() > 0) {
            long update = db.update(CREDENTIALS_TABLE, cv, COLUMN_USER_ID + " =? AND " + COLUMN_CREDENTIAL_ID + " = " + credID, new String[]{String.valueOf(userID)});
            cursor.close();
            db.close();
            return update != -1;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean updateUser(int userID, String name, String username, String password) {
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + COLUMN_USER_ID + " =?";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_USERNAME, username);
        cv.put(COLUMN_PASSWORD, password);

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(userID)});

        if(cursor.getCount() > 0) {
            long update = db.update(USERS_TABLE, cv, COLUMN_USER_ID + " =?", new String[]{String.valueOf(userID)});
            cursor.close();
            db.close();
            return update != -1;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean disableUser(int userID) {
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + COLUMN_USER_ID + " =?";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_IS_ACTIVE, 0);

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(userID)});

        if(cursor.getCount() > 0) {
            long update = db.update(USERS_TABLE, cv, COLUMN_USER_ID + " =?", new String[]{String.valueOf(userID)});
            cursor.close();
            db.close();
            return update != -1;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean enableUser(int userID) {
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + COLUMN_USER_ID + " =?";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_IS_ACTIVE, 1);

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(userID)});

        if(cursor.getCount() > 0) {
            long update = db.update(USERS_TABLE, cv, COLUMN_USER_ID + " =?", new String[]{String.valueOf(userID)});
            cursor.close();
            db.close();
            return update != -1;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }
}
