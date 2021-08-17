package com.booklover.dao;

import com.booklover.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAOUser {

    private DatabaseReference databaseReference;

    public DAOUser(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
    }

    public Task<Void> add(User user){
        return databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
    }

    public Task<Void> update(HashMap<String ,Object> hashMap){
        return databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);
    }

}
