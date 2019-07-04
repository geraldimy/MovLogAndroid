package com.example.rog.movlog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowComment extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference ratingTbl;

    SwipeRefreshLayout mSwipeRefreshLayout;
    FirebaseRecyclerAdapter<Rating,ShowCommentViewHolder> adapter ;
    TextView txtNoComment;
    TextView txtTitle;
    String movieId="";
    public String mMovie ;








    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter !=null)
            adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
        .setDefaultFontPath("fonts/cf.otf")
        .setFontAttrId(R.attr.fontPath)
        .build());
        setContentView(R.layout.activity_show_comment);

        database = FirebaseDatabase.getInstance();
        ratingTbl = database.getReference("Rating");

        recyclerView = (RecyclerView)findViewById(R.id.recylerComment);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        Toolbar toolbar = findViewById(R.id.toolbarComment);
////        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeComment);







        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getIntent() !=null) {
                    movieId = getIntent().getStringExtra(Model.INTENT_MOVIE_ID);
                    if (!movieId.isEmpty() && movieId != null) {
                        Query query = ratingTbl.orderByChild("movieId").equalTo(movieId);

                        FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                                .setQuery(query, Rating.class)
                                .build();

                        adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull ShowCommentViewHolder holder, int position, @NonNull Rating model) {
                                holder.ratingBar.setRating(Float.parseFloat(model.getRatevalue()));
                                holder.txtEmail.setText(model.getUserId());
                                holder.txtComment.setText(model.getComment());

                            }


                            @NonNull
                            @Override
                            public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                                View view = LayoutInflater.from(viewGroup.getContext())
                                        .inflate(R.layout.show_comment_layout, viewGroup, false);
                                return new ShowCommentViewHolder(view);
                            }
                        };
                        loadComment(movieId);
                    } else {
                        Toast.makeText(ShowComment.this, "No Comment In This Movie", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    txtNoComment.setText("No Comment In This Movie");
                    Toast.makeText(ShowComment.this, "No Comment In This Movie", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (getIntent() !=null)
                    movieId = getIntent().getStringExtra(Model.INTENT_MOVIE_ID);
                if( !movieId.isEmpty() && movieId !=null)
                {
                    Query query = ratingTbl.orderByChild("movieId").equalTo(movieId);

                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query, Rating.class)
                            .build();


                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ShowCommentViewHolder holder, int position, @NonNull Rating model) {
                            holder.ratingBar.setRating(Float.parseFloat(model.getRatevalue()));
                            holder.txtEmail.setText(model.getUserId());
                            holder.txtComment.setText(model.getComment());

                        }


                        @NonNull
                        @Override
                        public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewtype) {

                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.show_comment_layout,parent,false);
                            return new ShowCommentViewHolder(view);
                        }
                    };
                    loadComment(movieId);

                }
                }


        });

    }

    private void loadComment(String movieId) {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing( false );


    }

}
