package com.example.unetify.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unetify.R;
import com.example.unetify.activities.PostDetailActivity;
import com.example.unetify.models.Like;
import com.example.unetify.models.Post;
import com.example.unetify.providers.AuthProvider;
import com.example.unetify.providers.LikeProvider;
import com.example.unetify.providers.PostProvider;
import com.example.unetify.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class PostAdapter extends FirestoreRecyclerAdapter<Post, PostAdapter.ViewHolder> {

    Context context;
    UserProvider mUserProvider;
    LikeProvider mLikeProvider;
    AuthProvider mAuthProvider;

    public PostAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
        mUserProvider = new UserProvider();
        mLikeProvider = new LikeProvider();
        mAuthProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String postId = document.getId();

        holder.mTextViewTitle.setText(post.getTitle().toUpperCase());
        holder.mTextViewDescription.setText(post.getDescription());
        if (post.getImage() != null){
            if (!post.getImage().isEmpty()){
                Picasso.with(context).load(post.getImage()).into(holder.mImageViewPost);
            }
        }
        holder.mViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("id", postId);
                context.startActivity(intent);
            }
        });

        holder.mImageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Like like = new Like();
                like.setIdUser(mAuthProvider.getUid());
                like.setIdPost(postId);
                like.setTimestamp(new Date().getTime());
                like(like, holder);
            }
        });

        getUserInfo(post.getIdUser(),holder);
        getNumberLikesByPost(postId,holder);
        checkIsExistLike(postId, mAuthProvider.getUid(), holder);
    }

    private void getNumberLikesByPost(String idPost, ViewHolder holder){
        mLikeProvider.getLikesByPost(idPost).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int numberLikes = value.size();
                holder.mTextViewLike.setText(String.valueOf(numberLikes) + " Me gustas");
            }
        });
    }

    private void like(Like like, ViewHolder holder) {
        mLikeProvider.getLikeByPostAndUser(like.getIdPost(), mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();
                if (numberDocuments > 0){
                    String idLike = queryDocumentSnapshots.getDocuments().get(0).getId();
                    holder.mImageViewLike.setImageResource(R.drawable.ic_like_gray);
                    mLikeProvider.delete(idLike);
                }else {
                    holder.mImageViewLike.setImageResource(R.drawable.ic_like_red);
                    mLikeProvider.create(like);
                }
            }
        });
    }

    private void checkIsExistLike(String idPost, String idUser, ViewHolder holder) {
        mLikeProvider.getLikeByPostAndUser(idPost,idUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();
                if (numberDocuments > 0){
                    holder.mImageViewLike.setImageResource(R.drawable.ic_like_red);
                }else {
                    holder.mImageViewLike.setImageResource(R.drawable.ic_like_gray);
                }
            }
        });
    }

    private void getUserInfo(String idUser, ViewHolder holder) {
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username");
                        holder.mTextViewUsername.setText(username);
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextViewTitle, mTextViewDescription, mTextViewUsername,mTextViewLike;
        ImageView mImageViewPost,mImageViewLike;

        View mViewHolder;

        public ViewHolder (View view){
            super(view);
            mTextViewTitle = view.findViewById(R.id.textViewTitlePostCard);
            mTextViewDescription = view.findViewById(R.id.textViewDescriptionPostCard);
            mTextViewUsername = view.findViewById(R.id.textViewUsernamePostCard);
            mTextViewLike = view.findViewById(R.id.textViewLike);
            mImageViewPost = view.findViewById(R.id.imageViewPostCard);
            mImageViewLike = view.findViewById(R.id.imageViewLike);
            mViewHolder = view;

        }
    }
}
