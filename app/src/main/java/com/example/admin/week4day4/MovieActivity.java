package com.example.admin.week4day4;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MovieActivity extends AppCompatActivity {
    private static final String TAG = "Movie";
    private DatabaseReference myMovieRef;
    private FirebaseDatabase database;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private EditText etMovieName;
    private EditText etMovieGenre;
    private EditText etMovieYeasr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        etMovieName = findViewById(R.id.etMovieName);
        etMovieGenre = findViewById(R.id.etMovieType);
        etMovieYeasr = findViewById(R.id.etMovieYear);
        TextView tvUserName = findViewById(R.id.tvUserName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            String username = currentUser.getDisplayName();
            tvUserName.setText(username);
            String useremail = currentUser.getEmail();
            tvEmail.setText(useremail);
        }

        database = FirebaseDatabase.getInstance();
        myMovieRef = database.getReference("movie");

    }
    public void addNewMovie(View view) {
        getMovie();
        Movie movie = getMovie();

        myMovieRef
                .child(currentUser.getUid())
                .child("Favoritemovies")
                .push().setValue(movie).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MovieActivity.this, "movie added"+task, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MovieActivity.this, "movie added"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private Movie getMovie() {
        String movieName = etMovieName.getText().toString();
       String  movieGenre = etMovieGenre.getText().toString();
        String movieYear = etMovieYeasr.getText().toString();
        return new Movie(movieName, movieGenre, movieYear);
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser==null){
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
        }
        // firebase database


    }

    public void signout(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "you are signed out", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, Authentication.class);
        startActivity(intent);
    }

    public void onAddMovie(View view) {
        // Read from the database
        myMovieRef.child(currentUser.getUid()).child("Favoritemovies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Movie movie = dataSnapshot1.getValue(Movie.class);
                    Log.d(TAG, "onDataChange: "+movie.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
