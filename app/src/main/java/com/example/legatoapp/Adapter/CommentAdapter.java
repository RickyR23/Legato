package com.example.legatoapp.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.legatoapp.R;
import com.example.legatoapp.models.Comment; // assuming you will create this
import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> commentList;

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView username, commentText;
        TextView timestamp;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.comment_user_image);
            username = itemView.findViewById(R.id.comment_username);
            commentText = itemView.findViewById(R.id.comment_text);
            timestamp = itemView.findViewById(R.id.comment_timestamp);
        }
    }

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.userImage.setImageResource(comment.getImageResId());
        holder.username.setText(comment.getUsername());
        holder.commentText.setText(comment.getText());
        holder.timestamp.setText(comment.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void addComment(Comment comment) {
        commentList.add(0, comment);
        notifyItemInserted(0);
    }
}


