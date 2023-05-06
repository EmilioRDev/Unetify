package com.example.unetify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView mCircleImageViewBack;
    TextInputEditText mtextInputUsername, mtextInputEmail, mtextInputPassword, mtextInputConfirmPassword;
    Button mButtonRegister;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mCircleImageViewBack = findViewById(R.id.circleImageBack);
        mtextInputUsername = findViewById(R.id.textInputUsername);
        mtextInputEmail = findViewById(R.id.textInputEmail);
        mtextInputPassword = findViewById(R.id.textInputPassword);
        mtextInputConfirmPassword = findViewById(R.id.textInputConfirmPassword);
        mButtonRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void register(){
        String username = mtextInputUsername.getText().toString();
        String email = mtextInputEmail.getText().toString();
        String password = mtextInputPassword.getText().toString();
        String confirmPassword = mtextInputConfirmPassword.getText().toString();

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            if (isEmailValid(email)) {
                if (password.equals(confirmPassword)) {
                    if (password.length() >= 8){
                        createUser(username,email,password);
                    }else {
                        Toast.makeText(this, "La contraseña debe tener al menos 8 carácteres", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Insertastes todos los campos y el email no es válido", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Para continuar inserta todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void createUser(String username,String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = mAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("email",email);
                    map.put("username",username);
                    mFirestore.collection("Users").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("BBDD","El usuario se almaceno correctamente en la base de datos");
                                Toast.makeText(RegisterActivity.this, "El usuario se registro correctamente", Toast.LENGTH_SHORT).show();
                                clearInputText();
                                finish();
                            }else{
                                Log.e("BBDD","No se pudo almacenar correctamente el usuario en la base de datos");
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*VERIFICAR QUE SEA UN EMAIL VALIDO*/
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /*Limpiar los campos*/
    private void clearInputText(){
        mtextInputUsername.setText("");
        mtextInputEmail.setText("");
        mtextInputPassword.setText("");
        mtextInputConfirmPassword.setText("");
    }
}