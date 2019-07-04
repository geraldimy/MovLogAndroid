package com.example.rog.movlog;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class NowShowing extends Fragment{


    LinearLayoutManager mLayoutManager;
    SharedPreferences mSharedPref;
    private View MovieView;
    private RecyclerView myMovieList;
    private Query MovieRef;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    DatabaseReference movies;
    DatabaseReference ratingTbl;

    public NowShowing() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        movies = mFirebaseDatabase.getReference("Movie");
        ratingTbl = mFirebaseDatabase.getReference("Rating");


        MovieView = inflater.inflate(R.layout.fragment_now_showing, container, false);

        mSharedPref = this.getActivity().getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting  = mSharedPref.getString("Sort","newest");

        if (mSorting.equals("newest"))
        {
            mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        }
        else if (mSorting.equals("oldest"))
        {
            mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);
        }


        myMovieList = (RecyclerView) MovieView.findViewById(R.id.movies_list);
        myMovieList.setLayoutManager(mLayoutManager);



        MovieRef = FirebaseDatabase.getInstance().getReference("Movie")
                .orderByChild("status")
                .equalTo("Now Showing");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Movie");

        return MovieView;
    }

    public void firebaseSearch(String searchText){
        Query firebaseSearchQuery = mRef.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions option =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(firebaseSearchQuery, Model.class)
                        .build();

        FirebaseRecyclerAdapter<Model, MovieViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, MovieViewHolder>(option) {
                    @Override
                    protected void onBindViewHolder(@NonNull MovieViewHolder holder, final int position, @NonNull Model model) {
                        String title = model.getTitle();
                        String genre = model.getGenre();
                        String image = model.getImage();
                        String rating = model.getRating();
                        String year = model.getYear();


                        holder.titlemovie.setText(title);
                        holder.genmovie.setText(genre);
                        Picasso.get().load(image).into(holder.imagemovie);
                        holder.ratingmovie.setText(rating);
                        holder.yearmovie.setText(year);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String  movie_id = getRef(position).getKey();
                                Intent detail = new Intent(getContext(), MovieDetail.class);
                                detail.putExtra("MovieId",  movie_id);
                                startActivity(detail);

                            }
                        });
                    }


                    @NonNull
                    @Override
                    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movielist, viewGroup, false);
                        MovieViewHolder viewHolder = new MovieViewHolder(view);
                        return viewHolder;                    }
                };
        myMovieList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(MovieRef, Model.class)
                        .build();

        final FirebaseRecyclerAdapter<Model, MovieViewHolder> adapter
                = new FirebaseRecyclerAdapter<Model, MovieViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final MovieViewHolder holder, final int position, @NonNull Model model)
            {
                String title = model.getTitle();
                String genre = model.getGenre();
                String image = model.getImage();
                String rating = model.getRating();
                String year = model.getYear();
                String description = model.getDescription();
                String baner1 = model.getBaner1();
                String baner2 = model.getBaner2();
                String baner3 = model.getBaner3();

                holder.titlemovie.setText(title);
                holder.genmovie.setText(genre);
                Picasso.get().load(image).into(holder.imagemovie);
                holder.ratingmovie.setText(rating);
                holder.yearmovie.setText(year);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String  movie_id = getRef(position).getKey();
                        Intent detail = new Intent(getContext(), MovieDetail.class);
                        detail.putExtra("MovieId",  movie_id);
                        startActivity(detail);


                    }
                });

            }


            @NonNull
            @Override
            public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movielist, viewGroup, false);
                MovieViewHolder viewHolder = new MovieViewHolder(view);
                return viewHolder;
            }
        };

        myMovieList.setAdapter(adapter);
        adapter.startListening();
        }

    public static class MovieViewHolder extends RecyclerView.ViewHolder
    {
        TextView titlemovie, genmovie, yearmovie, ratingmovie, descriptionmovie, baner1movie;
        ImageView imagemovie, baner1, baner2, baner3;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            titlemovie = itemView.findViewById(R.id.titlemov);
            genmovie = itemView.findViewById(R.id.genmov);
            imagemovie = itemView.findViewById(R.id.imagemov);
            yearmovie = itemView.findViewById(R.id.yearmov);
            ratingmovie = itemView.findViewById(R.id.ratemov);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Search Title Here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                onStart();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort) {
            showSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        String[] sortOptions = {"Newest","Oldest"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort By")
                .setIcon(R.drawable.ic_action_sort)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0)
                        {
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort","newest");
                            editor.apply();
                            getFragmentManager()
                            .beginTransaction()
                            .detach(NowShowing.this)
                                    .attach(NowShowing.this)
                                    .commit();
                        }
                        else if (which==1)
                        {
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort","oldest");
                            editor.apply();
                            getFragmentManager()
                                    .beginTransaction()
                                    .detach(NowShowing.this)
                                    .attach(NowShowing.this)
                                    .commit();
                        }
                    }
                });
        builder.show();
    }
}