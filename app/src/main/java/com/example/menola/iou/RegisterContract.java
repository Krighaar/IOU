package com.example.menola.iou;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Peter on 28-04-2015.
 */
public class RegisterContract extends SQLiteOpenHelper {


    //Table one
    public static final String TABLE_REGISTER = "register";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_ID = "userID";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LON = "lon";


    //Table users
    public static final String TABLE_USERS = "users";
    public static final String USER_ID = "_ID";
    public static final String USER_NAME = "name";

    private static final String DATABASE_NAME = "IOU1.db";
    private static final String DATABASE_DROP = "drop table " + TABLE_REGISTER;

    private static final int DATABASE_VERSION = 2;

    static public int getVersion() {
        return DATABASE_VERSION;
    }

    // Database creation sql statement
    private static final String CREATE_TABLE_USERS =

            "create table " + TABLE_USERS
                    + "(" + COLUMN_ID + " integer primary key autoincrement, "
                    + USER_NAME + " text not null);";

    private static final String CREATE_TABLE_REGISTER =

            "create table "
                    + TABLE_REGISTER + "(" + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_USER_ID + " integer not null, "
                    + COLUMN_DESCRIPTION + " text , "
                    + COLUMN_VALUE + " Float , "
                    + COLUMN_LAT + " double , "
                    + COLUMN_LON + " double, "
                    + " FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + " (" + USER_ID + "));";


    public RegisterContract(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(CREATE_TABLE_USERS);
        database.execSQL(CREATE_TABLE_REGISTER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(RegisterContract.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER);
        onCreate(db);
    }
}

