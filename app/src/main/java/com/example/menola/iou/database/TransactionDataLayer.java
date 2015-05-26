package com.example.menola.iou.database;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.menola.iou.model.Register;
import com.example.menola.iou.model.User;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Peter on 28-04-2015.
 */
public class TransactionDataLayer {
    // Database fields
    private SQLiteDatabase database;
    private DbHelper dbHelper;

    private static TransactionDataLayer datasource;

    private String[] allColumnsUsers = {DbHelper.USER_ID, DbHelper.USER_NAME};

    private String[] allColumns = {DbHelper.COLUMN_ID,
            DbHelper.COLUMN_USER_ID, DbHelper.COLUMN_DESCRIPTION, DbHelper.COLUMN_VALUE};
    private int totalFromAll;


    private TransactionDataLayer(Context context) {
        dbHelper = new DbHelper(context);
    }

    public static TransactionDataLayer getInstance(Context context) {
        if (datasource == null) {
            datasource = new TransactionDataLayer(context);
        }
        return datasource;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    //add Latlng latlng as las paramater
    public Register createComment(Register transaction) {


        ContentValues values = new ContentValues();
        this.open();
        values.put(DbHelper.COLUMN_USER_ID, transaction.getuser_id());
        values.put(DbHelper.COLUMN_DESCRIPTION, transaction.getDescription());
        values.put(DbHelper.COLUMN_VALUE, transaction.getValue());
        values.put(DbHelper.COLUMN_LAT, transaction.getLatLng().latitude);
        values.put(DbHelper.COLUMN_LON, transaction.getLatLng().longitude);

        long insertId = database.insert(DbHelper.TABLE_REGISTER, null,
                values);
        Cursor cursor = database.query(DbHelper.TABLE_REGISTER,
                allColumns, DbHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Register newRegister = cursorToComment(cursor);
        cursor.close();
        return newRegister;
    }

    public void deleteComment(Register comment) {
        open();
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);


        database.delete(DbHelper.TABLE_REGISTER, DbHelper.COLUMN_ID
                + " = " + id, null);
        close();
    }

    public List<User> getAllUsers() {
        this.open();
        List<User> users = new ArrayList<User>();

        Cursor cursor = database.query(DbHelper.TABLE_USERS,
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
        this.close();
        return users;
    }

    public List<Register> getAllComments() {
        List<Register> comments = new ArrayList<Register>();

        Cursor cursor = database.query(DbHelper.TABLE_REGISTER,
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
        open();
        User user = new User();
        String[] selectionArgs = {userName};

        Cursor resultSet = database.rawQuery("Select * from " + DbHelper.TABLE_USERS + " where " + DbHelper.USER_NAME + " = ?", selectionArgs, null);
        if (resultSet.moveToFirst()) {
            user.setId(resultSet.getInt(0));
            user.setName(resultSet.getString(1));

        } else {
            user = null;
        }

        close();
        return user;
    }

    public User createUser(String userName) {
        open();
        ContentValues values = new ContentValues();
        this.open();
        values.put(DbHelper.USER_NAME, userName);

        long insertId = database.insert(DbHelper.TABLE_USERS, null,
                values);
        Cursor cursor = database.query(DbHelper.TABLE_USERS,
                allColumnsUsers, DbHelper.USER_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        // Register newRegister = cursorToComment(cursor); //Can be deleted??
        close();
        return findUser(userName);
    }

    public float getTotalFromUser(int id) {
        open();
        float total = 0;

        String[] columns = {DbHelper.COLUMN_VALUE};
        String[] idStr = {"" + id};
        String whereClause = DbHelper.COLUMN_USER_ID + " = ?";
        String queryString = " Select SUM(" + DbHelper.COLUMN_VALUE + ") from " + DbHelper.TABLE_REGISTER
                + " WHERE " + DbHelper.COLUMN_USER_ID + "=?";

        // another test
        Cursor resultSet = database.rawQuery(queryString, idStr);

        resultSet.moveToFirst();

        if (resultSet.moveToFirst()) {
            total = resultSet.getFloat(0);
        }
        resultSet.close();
        close();
        return total;
    }

    public List<Register> getAllTransFromUser(int userID) {
        open();
        List<Register> comments = new ArrayList<Register>();

        String[] selection = {"" + userID};

        Cursor cursor = database.query(DbHelper.TABLE_REGISTER,
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
        close();
        return comments;
    }

    public User getUser(int userID) {
        open();
        User user = new User();

        String[] selection = {"" + userID};

        Cursor cursor = database.query(DbHelper.TABLE_USERS,
                allColumnsUsers, DbHelper.COLUMN_ID + " =?", selection, null, null, null);

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
        close();
        return user;
    }

    public float getTotalFromAll() {
        open();
        float total = 0;

        String[] columns = {DbHelper.COLUMN_VALUE};
        String whereClause = DbHelper.COLUMN_USER_ID + " = ?";

        String queryString = " Select SUM(" + DbHelper.COLUMN_VALUE + ") from " + DbHelper.TABLE_REGISTER;


        Cursor resultSet = database.rawQuery(queryString, null);


        if (resultSet.moveToFirst()) {
            total = resultSet.getFloat(0);
        }

        close();
        return total;
    }

    public Register getTransaction(Long regID) {
        open();
        Register register = new Register();


        String[] selectionArgs = {"" + regID};

        Cursor resultSet = database.rawQuery("Select * from " + DbHelper.TABLE_REGISTER + " where " + DbHelper.COLUMN_ID + " = ?", selectionArgs, null);
        if (resultSet.moveToFirst()) {
            register.setId(resultSet.getLong(0));
            register.setUser_id(resultSet.getInt(1));
            register.setDescription(resultSet.getString(2));
            register.setValue(resultSet.getFloat(3));
            register.setLatLng(new LatLng(resultSet.getDouble(4), resultSet.getDouble(5)));

        } else {
            register = null;
        }

        close();
        return register;
    }
}


