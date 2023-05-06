package com.example.unetify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.unetify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText mtextInputUsername;
    Button mButtonRegister;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        mtextInputUsername = findViewById(R.id.textInputUsername);
        mButtonRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register(){
        String username = mtextInputUsername.getText().toString();

        if (!username.isEmpty()) {
            updateUser(username);
        }else{
            Toast.makeText(this, "Para continuar inserta todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser(String username){
        String id = mAuth.getCurrentUser().getUid();
        Map<String, Object> map = new HashMap<>();
        map.put("username",username);
        mFirestore.collection("Users").document(id).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(CompleteProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                    clearInputText();
                }else{
                    Log.e("BBDD","No se pudo almacenar correctamente el usuario en la base de datos");
                }
            }
        });
    }

    /*Limpiar los campos*/
    private void clearInputText(){
        mtextInputUsername.setText("");
        mtextInputUsername.clearFocus();
    }
}