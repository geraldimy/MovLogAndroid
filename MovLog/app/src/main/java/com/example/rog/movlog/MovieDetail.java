package com.example.rog.movlog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.HashMap;

public class MovieDetail extends AppCompatActivity implements RatingDialogListener {
    TextView movie_genre, movie_name, movie_rating,movLog_rating ;
    HtmlTextView movie_description,movie_review;
    ImageView baner1, baner2, baner3, movie_image,MovStar;
    FloatingActionButton btnRating;
    RatingBar ratingBar;
    Button  btnComment;
    String movieId="";
    private DatabaseReference mref;
    private String receiveMovieId = "";
    FirebaseDatabase database;
    DatabaseReference movies;
    DatabaseReference ratingTbl;

    FirebaseUser firebaseUser;


    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    GoogleSignInClient mGoogleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        movies = FirebaseDatabase.getInstance().getReference("Movie");
        Intent intent = this.getIntent();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        mref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());


        //firebase
        database = FirebaseDatabase.getInstance();
        movies = database.getReference("Movie");
        ratingTbl = database.getReference("Rating");

        //initview
        movie_genre = (TextView) findViewById(R.id.movie_genre);
        movie_description = (HtmlTextView) findViewById(R.id.movie_description);
        movie_name = (TextView) findViewById(R.id.movie_name);
        movie_review = (HtmlTextView) findViewById(R.id.movie_review);
        movLog_rating = (TextView) findViewById(R.id.movLog_rating);
        movie_image = (ImageView) findViewById(R.id.movie_image);
        MovStar = (ImageView) findViewById(R.id.Movstar);
        baner1 = (ImageView) findViewById(R.id.baner1);

        btnRating = (FloatingActionButton) findViewById(R.id.btnRating);
        movie_rating = (TextView) findViewById(R.id.movie_rating);
        btnComment = (Button) findViewById(R.id.btn_comment);


        final Vibrator vibrator = (Vibrator) MovieDetail.this.getSystemService(Context.VIBRATOR_SERVICE);


        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(200);
                Intent comment = new Intent(MovieDetail.this, ShowComment.class);
                comment.putExtra(Model.INTENT_MOVIE_ID, movieId);
                startActivity(comment);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            btnRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vibrator.vibrate(200);
                    showRatingDialog();

                }
            });
        } else {
            btnRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vibrator.vibrate(200);
                    AlertDialog.Builder msgBox = new AlertDialog.Builder(MovieDetail.this);
                    msgBox.setMessage("Login First");
                    msgBox.setCancelable(false);
                    msgBox.setPositiveButton("Login", (dialog, which) -> {
                        Intent intent1 = new Intent(getApplicationContext(), LogGoogle.class);
                        startActivity(intent1);
                        finish();
                    });

                    msgBox.show();
                }


            });
        }


        //Get Movie Id from Intent
        if (getIntent() != null) {
            movieId = getIntent().getStringExtra("MovieId");
            if (!movieId.isEmpty()) {
                getDetail(movieId);
                getRatingShow();
            }
        }
    }





    private void getRatingShow() {
        Query movieRating = ratingTbl.orderByChild("movieId").equalTo(movieId);
        movieRating.addValueEventListener(new ValueEventListener() {

            int count= 0, sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum+= Integer.parseInt(item.getRatevalue());
                    count++;
                }
                if(count !=0) {
                    float average = sum / count;
                    String txtRating = Float.toString(average);
                    movie_rating.setText(txtRating);
                     }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDetail(String movieId) {
        movies.child(movieId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  Model mov = dataSnapshot.getValue(Model.class);

                  movLog_rating.setText(mov.getRating());
                  movie_name.setText(mov.getTitle());
                  movie_genre.setText(mov.getGenre());
                  movie_description.setHtml(mov.getDescription());
                  movie_review.setHtml(mov.getReview());
                  Picasso.get().load(mov.getBaner1()).into(baner1);

                  Picasso.get().load(mov.getImage()).into(movie_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    private void showRatingDialog() {

        new AppRatingDialog.Builder()
                .setPositiveButtonText("submit")
                .setNegativeButtonText("cancel")
                .setDefaultRating(0)
                .setNumberOfStars(5)
                .setTitle("Rate This Movie ")
                .setDescription("Please Select Some Starts and Give You Review")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please Write review here")

                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(MovieDetail.this)
                .show();
    }




    @Override
    public void onNegativeButtonClicked() {

    }


    @Override
    public void onPositiveButtonClicked(int value,  String comments) {
       final Rating rating = new Rating(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                        movieId,
                                        String.valueOf(value),
                                        comments);
////
        ratingTbl.push()
                .setValue(rating)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MovieDetail.this, "Thanks for submit", Toast.LENGTH_SHORT).show();
                    }
                });

//
//         ratingTbl.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(movieId).addListenerForSingleValueEvent(new ValueEventListener() {
//             @Override
//             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                 if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(movieId).exists()) {
//                     ratingTbl.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(movieId).removeValue();
//                     ratingTbl.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(movieId).setValue(rating);
//                     Toast.makeText(MovieDetail.this, "Thanks for submit", Toast.LENGTH_SHORT).show();
//
//
//                 } else {
//                     ratingTbl.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(movieId).setValue(rating);
//                     Toast.makeText(MovieDetail.this, "Thanks for submit", Toast.LENGTH_SHORT).show();
//
//                 }
//             }
//
//                 @Override
//                 public void onCancelled (@NonNull DatabaseError databaseError){
//
//                 }
//
//        });
//        ratingTbl.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(movieId).addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(movieId).exists())
//                {
//                    ratingTbl.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
//                    ratingTbl.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(rating);
//                }
//                else {
//                    ratingTbl.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(movieId).setValue(rating);
//                }
//                Toast.makeText(MovieDetail.this, "Thanks for submit", Toast.LENGTH_SHORT).show();
//            }
//
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



}
    }
