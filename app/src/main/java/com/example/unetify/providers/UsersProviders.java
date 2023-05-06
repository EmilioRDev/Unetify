package com.example.unetify.providers;

import com.example.unetify.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UsersProviders {

    private CollectionReference mCollection;

    public UsersProviders(){
        mCollection = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String id){

        return  mCollection.document(id).get();
    }

    public Task<Void> create(User user){
        return  mCollection.document(user.getId()).set(user);
    }
}
