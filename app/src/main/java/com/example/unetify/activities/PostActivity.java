package com.example.unetify.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unetify.R;
import com.example.unetify.models.Post;
import com.example.unetify.providers.AuthProvider;
import com.example.unetify.providers.ImageProvider;
import com.example.unetify.providers.PostProvider;
import com.example.unetify.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost;
    File mImageFile;
    Button mButtonPost;
    TextInputEditText mTextInputTitle,mTextInputDescription;
    ImageProvider mImageProvider;
    PostProvider mPostProvider;
    AuthProvider mAuthProvider;
    String mTitle = "";
    String mDescription = "";
    AlertDialog mDialog;
    TextView mTextViewUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageProvider = new ImageProvider();
        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProvider();
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();

        mImageViewPost = findViewById(R.id.imageViewPost);
        mButtonPost = findViewById(R.id.btnPost);
        mTextInputTitle = findViewById(R.id.textInputTitle);
        mTextInputDescription = findViewById(R.id.textInputDescription);
        mTextViewUp = findViewById(R.id.textViewUp);

        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPost();
            }
        });
        mImageViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

                try {
                    Thread.sleep(1000);
                    mTextViewUp.setText("");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void clickPost() {

        mTitle = mTextInputTitle.getText().toString();
        mDescription = mTextInputDescription.getText().toString();

        if (!mTitle.isEmpty() && !mDescription.isEmpty()) {
            if (mImageFile != null){
                saveImage();
            }else {
                Toast.makeText(this, "Debes seleccionar una imagen", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Completa los campos para publicar", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage() {
        mDialog.show();
        mImageProvider.save(PostActivity.this,mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Post post = new Post();
                            post.setImage(url);
                            post.setTitle(mTitle);
                            post.setDescription(mDescription);
                            post.setIdUser(mAuthProvider.getUid());
                            mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> taskSave) {
                                    mDialog.dismiss();
                                    if (taskSave.isSuccessful()){
                                        clearForm();
                                        Toast.makeText(PostActivity.this, "La información se almaceno correctamente", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Log.e("ERROR","Se produjo un error al almacenar la imagen en la base de datos ");
                                        Toast.makeText(PostActivity.this, "No se pudo almacenar la información", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }else {
                    mDialog.dismiss();
                    Log.e("ERROR","Se produjo un error al almacenar la imagen en el storage");
                    Toast.makeText(PostActivity.this, "Hubo un error al almacenar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearForm() {
        mTextInputTitle.setText("");
        mTextInputTitle.clearFocus();
        mTextInputDescription.setText("");
        mTextInputDescription.clearFocus();
        mImageViewPost.setImageResource(R.drawable.ic_upload);
        mTitle = "";
        mDescription = "";
        mImageFile = null;
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
