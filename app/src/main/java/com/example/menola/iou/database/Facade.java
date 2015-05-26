package com.example.menola.iou.database;

import android.content.Context;

import com.example.menola.iou.model.Register;
import com.example.menola.iou.model.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by peter on 20-05-15.
 */
public class Facade {

    private TransactionDataLayer dataSource;
    private UserMapper um;
    private TransactionMapper tm;

    //== SINGLETON
    private static Facade facade;

    private Facade(Context context) {
        um = new UserMapper(context);
        dataSource = TransactionDataLayer.getInstance(context);
        tm = new TransactionMapper(context);
    }

    public static Facade getInstance(Context context) {
        if (facade == null) {
            facade = new Facade(context);
        }
        return facade;
    }

    //==



    public User getUser(String userName) {
        return um.findUser(userName);
    }

    public List<User> getAllUsers() {
        return um.getAllUsers();
    }

    public User findUserByID(int userID) {
        return um.findUserByID(userID);
    }

    public String getTotalFromAll() {
        return tm.getTotal();
    }
    public float getTotalFromUser(int id) {
        float total = 0;
        total = tm.getTotalFromUser(id);
        return total;
    }

    public Register getTransaction(Long id) {
        return tm.getTransaction(id);
    }


    public void deleteTransaction(Register register) {
        tm.deleteTransaction(register);
    }

    public List<Register> getAllRegFromUser(int userID) {
        dataSource.open();
        List<Register> result = dataSource.getAllTransFromUser(userID);
        dataSource.close();


        return tm.getAllTransactionFromUser(userID);
    }

    public void createTransaction(int id, String description, float value, LatLng latLng) {
        Register transaction = new Register();
        transaction.setUser_id(id);
        transaction.setDescription(description);
        transaction.setValue(value);
        transaction.setLatLng(latLng);
        dataSource.createComment(transaction);
    }

    public void createUser(String userName) {
        dataSource.createUser(userName);
    }
}
