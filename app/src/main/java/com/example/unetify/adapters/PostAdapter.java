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
import androidx.recyclerview.widget.RecyclerView;

import com.example.unetify.R;
import com.example.unetify.activities.PostDetailActivity;
import com.example.unetify.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class PostAdapter extends FirestoreRecyclerAdapter<Post, PostAdapter.ViewHolder> {

    Context context;

    public PostAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String postId = document.getId();

        holder.mTextViewTitle.setText(post.getTitle());
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
                Log.e("ID", String.valueOf(intent));
                context.startActivity(intent);
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
        TextView mTextViewTitle, mTextViewDescription;
        ImageView mImageViewPost;

        View mViewHolder;

        public ViewHolder (View view){
            super(view);
            mTextViewTitle = view.findViewById(R.id.textViewTitlePostCard);
            mTextViewDescription = view.findViewById(R.id.textViewDescriptionPostCard);
            mImageViewPost = view.findViewById(R.id.imageViewPostCard);
            mViewHolder = view;

        }
    }
}
