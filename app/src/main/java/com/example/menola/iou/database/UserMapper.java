package com.example.menola.iou.database;

import android.content.Context;

import com.example.menola.iou.model.User;

import java.util.List;

/**
 * Created by peter on 26-05-15.
 */
public class UserMapper {
    private TransactionDataLayer dataSource;


    public UserMapper(Context context) {

        dataSource = TransactionDataLayer.getInstance(context);
    }

    public List<User> getAllUsers(){
        return dataSource.getAllUsers();
    }

    public User findUserByID(int userID) {
        return dataSource.getUser(userID);
    }

    public User findUser(String userName) {
        return dataSource.findUser(userName);
    }

    public void deleteUser(int itemId) {
        dataSource.deleteUser(itemId);
    }
}

