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

import com.example.menola.iou.model.Register;
import com.example.menola.iou.model.User;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Peter on 28-04-2015.
 */
public class RegisterDataSource {
    // Database fields
    private SQLiteDatabase database;
    private TransactionContract dbHelper;

    private static RegisterDataSource datasource;

    private String[] allColumnsUsers = {TransactionContract.USER_ID, TransactionContract.USER_NAME};

    private String[] allColumns = {TransactionContract.COLUMN_ID,
            TransactionContract.COLUMN_USER_ID, TransactionContract.COLUMN_DESCRIPTION, TransactionContract.COLUMN_VALUE};
    private int totalFromAll;


    private RegisterDataSource(Context context) {
        dbHelper = new TransactionContract(context);
    }

    public static RegisterDataSource getInstance(Context context) {
        if (datasource == null) {
            datasource = new RegisterDataSource(context);
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
    public Register createComment(int user_id, String description, float value, LatLng latLng) {
        ContentValues values = new ContentValues();
        this.open();
        values.put(TransactionContract.COLUMN_USER_ID, user_id);
        values.put(TransactionContract.COLUMN_DESCRIPTION, description);
        values.put(TransactionContract.COLUMN_VALUE, value);
        values.put(TransactionContract.COLUMN_LAT, latLng.latitude);
        values.put(TransactionContract.COLUMN_LON, latLng.longitude);

        long insertId = database.insert(TransactionContract.TABLE_REGISTER, null,
                values);
        Cursor cursor = database.query(TransactionContract.TABLE_REGISTER,
                allColumns, TransactionContract.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Register newRegister = cursorToComment(cursor);
        cursor.close();
        return newRegister;
    }

    public void deleteComment(Register comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(TransactionContract.TABLE_REGISTER, TransactionContract.COLUMN_ID
                + " = " + id, null);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        Cursor cursor = database.query(TransactionContract.TABLE_USERS,
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

        Cursor cursor = database.query(TransactionContract.TABLE_REGISTER,
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

        Cursor resultSet = database.rawQuery("Select * from " + TransactionContract.TABLE_USERS + " where " + TransactionContract.USER_NAME + " = ?", selectionArgs, null);
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
        values.put(TransactionContract.USER_NAME, userName);

        long insertId = database.insert(TransactionContract.TABLE_USERS, null,
                values);
        Cursor cursor = database.query(TransactionContract.TABLE_USERS,
                allColumnsUsers, TransactionContract.USER_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
       // Register newRegister = cursorToComment(cursor); //Can be deleted??

        return findUser(userName);
    }

    public float getTotalFromUser(int id) {

        float total = 0;

        String[] columns = {TransactionContract.COLUMN_VALUE};
        String[] idStr = {"" + id};
        String whereClause = TransactionContract.COLUMN_USER_ID + " = ?";
        String queryString = " Select SUM(" + TransactionContract.COLUMN_VALUE + ") from " + TransactionContract.TABLE_REGISTER
                + " WHERE " + TransactionContract.COLUMN_USER_ID + "=?";

        // another test
        Cursor resultSet = database.rawQuery(queryString, idStr);

        resultSet.moveToFirst();

        if(resultSet.moveToFirst()){
            total = resultSet.getFloat(0);
        }


        return total;
    }

    public List<Register> getAllRegFromUser(int userID) {
        List<Register> comments = new ArrayList<Register>();

        String[] selection = {"" + userID};

        Cursor cursor = database.query(TransactionContract.TABLE_REGISTER,
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

        Cursor cursor = database.query(TransactionContract.TABLE_USERS,
                allColumnsUsers, TransactionContract.COLUMN_ID + " =?", selection, null, null, null);

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

    public String getTotalFromAll() {

        float total = 0;

        String[] columns = {TransactionContract.COLUMN_VALUE};
        String whereClause = TransactionContract.COLUMN_USER_ID + " = ?";

        String queryString = " Select SUM(" + TransactionContract.COLUMN_VALUE + ") from " + TransactionContract.TABLE_REGISTER;


        Cursor resultSet = database.rawQuery(queryString, null);


        if(resultSet.moveToFirst()){
            total = resultSet.getFloat(0);
        }


        return String.valueOf(total);
    }

    public Register getTransaction(Long regID) {

        Register register = new Register();


        String[] selectionArgs = {""+regID};

        Cursor resultSet = database.rawQuery("Select * from " + TransactionContract.TABLE_REGISTER + " where " + TransactionContract.COLUMN_ID + " = ?", selectionArgs, null);
        if (resultSet.moveToFirst()) {
            register.setId(resultSet.getLong(0));
            register.setUser_id(resultSet.getInt(1));
            register.setDescription(resultSet.getString(2));
            register.setValue(resultSet.getFloat(3));
            register.setLatLng(new LatLng(resultSet.getDouble(4),resultSet.getDouble(5)));

        }
        else {
            register = null;
        }


        return register;
    }
}


