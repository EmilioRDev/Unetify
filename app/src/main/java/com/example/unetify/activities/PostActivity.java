package com.example.unetify.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.unetify.R;
import com.example.unetify.providers.ImageProviders;
import com.example.unetify.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost;
    //CardView mCardView;
    File mImageFile;
    Button mButtonPost;
    ImageProviders mImageProviders;
    private final int GALLERY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageProviders = new ImageProviders();

        mImageViewPost = findViewById(R.id.imageViewPost);
        //mCardView = findViewById(R.id.cardViewUp);
        mButtonPost = findViewById(R.id.btnPost);

        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });
        mImageViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void saveImage() {
        mImageProviders.save(PostActivity.this,mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PostActivity.this, "La imagen se almaceno correctamente", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(PostActivity.this, "Hubo un error al almacenar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            mImageFile = FileUtil.from(PostActivity.this,result.getData().getData());
                            mImageViewPost.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
                            /*mImageViewPost.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            mImageViewPost.setLayoutParams(new CardView.LayoutParams(mCardView.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT));*/

                        }catch (Exception e){
                            Log.e("ERROR","Se produjo un error "+ e.getMessage());
                            Toast.makeText(PostActivity.this, "Se produjo un error ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        galleryLauncher.launch(galleryIntent);
    }
}
