package com.example.menola.iou.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import com.example.menola.iou.R;
import com.example.menola.iou.RegisterDataSource;
import com.example.menola.iou.model.Register;
import com.example.menola.iou.model.User;

import java.util.List;

/**
 * Created by peter on 20-05-15.
 */
public class Controller {

    private RegisterDataSource dataSource;

    //== SINGLETON
    private static Controller controller;

    private Controller(Context context) {
        dataSource =  RegisterDataSource.getInstance(context);
    }

    public static Controller getInstance(Context context) {
        if (controller == null) {
            controller = new Controller(context);
        }
        return controller;
    }

    //==


    // Getting things from DB
    public User getUser(String userName) {


        return dataSource.findUser(userName);

    }

    public String getTotalFromAll() {
        dataSource.open();
        String totalFromAll = "";

        totalFromAll = dataSource.getTotalFromAll();
        dataSource.close();
        return totalFromAll;
    }

    public List<User> getAllUsers() {
        dataSource.open();
        List<User> allUsers = dataSource.getAllUsers();
        dataSource.close();
        return allUsers;
    }

    public List<Register> getAllComments() {
        dataSource.open();
        List<Register> allComments = dataSource.getAllComments();
        dataSource.close();
        return allComments;
    }

    public float getTotalFromUser(int id) {
        float total = 0;
        dataSource.open();
        total = dataSource.getTotalFromUser(id);
        dataSource.close();
        return  total;
    }


}
