package com.example.rog.movlog;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class ShowCommentViewHolder extends RecyclerView.ViewHolder {
    public TextView txtEmail, txtComment, txtNoComment;
    public RatingBar ratingBar;


    public ShowCommentViewHolder(@NonNull View itemView) {
        super(itemView);

        txtComment = (TextView)itemView.findViewById(R.id.txtComment);
        txtEmail = (TextView)itemView.findViewById(R.id.txtEmail);
        ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
        txtNoComment = (TextView)itemView.findViewById(R.id.noComment);
    }
}
