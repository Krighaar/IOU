package com.example.menola.iou;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Peter on 28-04-2015.
 */
public class RegisterDataSource {
    // Database fields
    private SQLiteDatabase database;
    private RegisterContract dbHelper;
    private String[] allColumns = { RegisterContract.COLUMN_ID,
            RegisterContract.COLUMN_NAME };

    public RegisterDataSource(Context context) {
        dbHelper = new RegisterContract(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Register createComment(String name, String description, int value, LatLng latlng) {
        ContentValues values = new ContentValues();
        values.put(RegisterContract.COLUMN_NAME, name);
        values.put(RegisterContract.COLUMN_DESCRIPTION, description);
        values.put(RegisterContract.COLUMN_VALUE, value);
        values.put(RegisterContract.COLUMN_LAT, latlng.latitude);
        values.put(RegisterContract.COLUMN_LAT, latlng.longitude);

        long insertId = database.insert(RegisterContract.TABLE_REGISTER, null,
                values);
        Cursor cursor = database.query(RegisterContract.TABLE_REGISTER,
                allColumns, RegisterContract.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Register newRegister = cursorToComment(cursor);
        cursor.close();
        return newRegister;
    }

    public void deleteComment(Register comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(RegisterContract.TABLE_REGISTER, RegisterContract.COLUMN_ID
                + " = " + id, null);
    }

    public List<Register> getAllComments() {
        List<Register> comments = new ArrayList<Register>();

        Cursor cursor = database.query(RegisterContract.TABLE_REGISTER,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Register comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Register cursorToComment(Cursor cursor) {
        Register comment = new Register();
        comment.setId(cursor.getLong(0));
        comment.setName(cursor.getString(1));
        return comment;
    }


}


