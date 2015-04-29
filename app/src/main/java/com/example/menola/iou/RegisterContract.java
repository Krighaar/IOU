package com.example.menola.iou;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Peter on 28-04-2015.
 */
public class RegisterContract  extends SQLiteOpenHelper {

    public static final String TABLE_REGISTER = "register";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "comment";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LON = "lon";


    private static final String DATABASE_NAME = "register.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_REGISTER + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME + " text not null,"
            + COLUMN_DESCRIPTION +"text ,"
            + COLUMN_VALUE +"Integer ,"
            + COLUMN_LAT + "double ,"
            + COLUMN_LON + "double);";

    public RegisterContract(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(RegisterContract.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER);
        onCreate(db);
    }
}

