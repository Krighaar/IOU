package com.example.menola.iou;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Peter on 28-04-2015.
 */
public class RegisterDataSource {
    // Database fields
    private SQLiteDatabase database;
    private RegisterContract dbHelper;

    private String[] allColumnsUsers = {RegisterContract.USER_ID, RegisterContract.USER_NAME};

    private String[] allColumns = {RegisterContract.COLUMN_ID,
            RegisterContract.COLUMN_USER_ID, RegisterContract.COLUMN_DESCRIPTION, RegisterContract.COLUMN_VALUE};


    public RegisterDataSource(Context context) {
        dbHelper = new RegisterContract(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    //add Latlng latlng as las paramater
    public Register createComment(int user_id, String description, float value, LatLng latLng) {
        ContentValues values = new ContentValues();
        this.open();
        values.put(RegisterContract.COLUMN_USER_ID, user_id);
        values.put(RegisterContract.COLUMN_DESCRIPTION, description);
        values.put(RegisterContract.COLUMN_VALUE, value);
        values.put(RegisterContract.COLUMN_LAT, latLng.latitude);
        values.put(RegisterContract.COLUMN_LON, latLng.longitude);

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

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        Cursor cursor = database.query(RegisterContract.TABLE_USERS,
                allColumnsUsers, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // Register comment = cursorToComment(cursor);
            do {

                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));


                users.add(user);
            } while (cursor.moveToNext());
        }
        // make sure to close the cursor
        cursor.close();
        return users;
    }

    public List<Register> getAllComments() {
        List<Register> comments = new ArrayList<Register>();

        Cursor cursor = database.query(RegisterContract.TABLE_REGISTER,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // Register comment = cursorToComment(cursor);
            do {

                Register comment = new Register();
                comment.setId(Integer.parseInt(cursor.getString(0)));
                comment.setUser_id(cursor.getInt(1));
                comment.setDescription(cursor.getString(2));
                comment.setValue(Float.parseFloat(cursor.getString(3)));


                comments.add(comment);
            } while (cursor.moveToNext());
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Register cursorToComment(Cursor cursor) {
        Register comment = new Register();
        comment.setId(cursor.getLong(0));
        comment.setUser_id(cursor.getInt(1));
        return comment;
    }


    @TargetApi(19)
    public User findUser(String userName) {

        User user = new User();
        String[] selectionArgs = {userName};

        Cursor resultSet = database.rawQuery("Select * from " + RegisterContract.TABLE_USERS + " where " + RegisterContract.USER_NAME + " = ?", selectionArgs, null);
        if (resultSet.moveToFirst()) {
            user.setId(resultSet.getInt(0));
            user.setName(resultSet.getString(1));

        }
        else {
            user = null;
        }


        return user;
    }

    public User createUser(String userName) {

        ContentValues values = new ContentValues();
        this.open();
        values.put(RegisterContract.USER_NAME, userName);

        long insertId = database.insert(RegisterContract.TABLE_USERS, null,
                values);
        Cursor cursor = database.query(RegisterContract.TABLE_USERS,
                allColumnsUsers, RegisterContract.USER_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Register newRegister = cursorToComment(cursor);

        return findUser(userName);
    }

    public float getTotalFromUser(int id) {

        float total = 0;

      /*  Cursor resultSet = database.rawQuery("Select "+RegisterContract.COLUMN_VALUE+" from " + RegisterContract.TABLE_REGISTER + " where " + RegisterContract.COLUMN_USER_ID + "" +
                " = " + id, null);
*/


        String[] columns = {RegisterContract.COLUMN_VALUE};
        String[] idStr = {"" + id};
        String whereClause = RegisterContract.COLUMN_USER_ID + " = ?";

/*

        Cursor resultSet = database.query(RegisterContract.TABLE_REGISTER, columns, whereClause, idStr,
                null, null, null);
*/
        String queryString = " Select " + RegisterContract.COLUMN_VALUE + " from " + RegisterContract.TABLE_REGISTER
                + " where " + RegisterContract.COLUMN_USER_ID + "=?";


// another test
        Cursor resultSet = database.rawQuery(queryString, idStr);

        resultSet.moveToFirst();
        while (!resultSet.isAfterLast()) {
            do {
                total += resultSet.getFloat(0);
            } while (resultSet.moveToLast());

        }


        return total;
    }

    public List<Register> getAllRegFromUser(int userID) {
        List<Register> comments = new ArrayList<Register>();

        String[] selection = {"" + userID};

        Cursor cursor = database.query(RegisterContract.TABLE_REGISTER,
                allColumns, "userID =?", selection, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // Register comment = cursorToComment(cursor);
            do {

                Register comment = new Register();
                comment.setId(Integer.parseInt(cursor.getString(0)));
                comment.setUser_id(cursor.getInt(1));
                comment.setDescription(cursor.getString(2));
                comment.setValue(Float.parseFloat(cursor.getString(3)));


                comments.add(comment);
            } while (cursor.moveToNext());
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    public User getUser(int userID) {

        User user = new User();

        String[] selection = {"" + userID};

        Cursor cursor = database.query(RegisterContract.TABLE_USERS,
                allColumnsUsers, RegisterContract.COLUMN_ID + " =?", selection, null, null, null);

        if (cursor.moveToFirst()) {
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setName(cursor.getString(1));
        } else {
            user.setId(100);
            user.setName("NO USER FOUND!");
        }

        database.close();
        Log.d("FootballApp", user.getName() + "=" + String.valueOf(userID));


        // make sure to close the cursor
        cursor.close();
        return user;
    }
}


