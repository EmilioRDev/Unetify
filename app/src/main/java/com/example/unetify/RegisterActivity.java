package com.example.unetify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView mCircleImageViewBack;
    TextInputEditText mtextInputUsername, mtextInputEmail, mtextInputPassword, mtextInputConfirmPassword;
    Button mButtonRegister;

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
                Toast.makeText(this, "Insertastes todos los campos y el email es válido", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Insertastes todos los campos y el email no es válido", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Para continuar inserta todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}