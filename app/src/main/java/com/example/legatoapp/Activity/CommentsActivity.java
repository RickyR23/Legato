package com.example.legatoapp.Activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legatoapp.Adapter.CommentAdapter;
import com.example.legatoapp.models.Comment;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import com.example.legatoapp.R;

public class CommentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private EditText commentInput;
    private ImageButton sendButton;
    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comments);

        recyclerView = findViewById(R.id.recyclerView_comments);
        commentInput = findViewById(R.id.editText_comment);
        sendButton = findViewById(R.id.button_send_comment);

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);

        sendButton.setOnClickListener(v -> {
            String text = commentInput.getText().toString().trim();
            if (!text.isEmpty()) {
                String timestamp = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
                Comment newComment = new Comment("Username", text, R.drawable.angelespfp, timestamp);
                commentAdapter.addComment(newComment);
                commentInput.setText("");
                recyclerView.scrollToPosition(0);
            }

        });

        ImageButton backButton = findViewById(R.id.backFromCommentsButton);
        backButton.setOnClickListener(v -> finish());
    }
}
