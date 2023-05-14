package com.example.unetify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.unetify.R;

public class PostDetailActivity extends AppCompatActivity {

    ImageView mImageViewPostDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mImageViewPostDetail = findViewById(R.id.imageViewPostDetail);

    }
}