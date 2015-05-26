package com.example.menola.iou.database;

import android.content.Context;

import com.example.menola.iou.model.Register;

import java.util.List;

/**
 * Created by peter on 26-05-15.
 */
public class TransactionMapper {

    private TransactionDataLayer datasource;

    public TransactionMapper(Context context){
        datasource = TransactionDataLayer.getInstance(context);
    }

    public float getTotalFromUser(int id) {

        return datasource.getTotalFromUser(id);
    }

    public Register getTransaction(Long id) {
        return datasource.getTransaction(id);
    }

    public void deleteTransaction(Register register) {
        datasource.deleteComment(register);;
    }

    public List<Register> getAllTransactionFromUser(int userID) {
        return datasource.getAllTransFromUser(userID);
    }


    public String getTotal() {
        return String.valueOf(datasource.getTotalFromAll());
    }
}
