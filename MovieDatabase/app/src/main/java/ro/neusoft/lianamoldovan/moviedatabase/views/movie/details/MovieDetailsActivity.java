package ro.neusoft.lianamoldovan.moviedatabase.views.movie.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import ro.neusoft.lianamoldovan.moviedatabase.R;
import ro.neusoft.lianamoldovan.moviedatabase.model.Movie;
import ro.neusoft.lianamoldovan.moviedatabase.utils.Util;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Util.initToolbar(this);
        retrieveSelectedMovie();
    }

    private void retrieveSelectedMovie() {
        Bundle intentBundle = getIntent().getExtras();
        if (intentBundle != null && intentBundle.containsKey("selected_movie")) {
            Movie movie = intentBundle.getParcelable("selected_movie");
            initViews(movie);
        }
    }

    private void initViews(Movie movie) {
        TextView movieTitle = findViewById(R.id.movie_title);
        movieTitle.setText(movie.getTitle());

        TextView movieYear = findViewById(R.id.movie_year);
        movieYear.setText(Util.extractYear(movie.getReleaseDate()));

        TextView movieAverageVote = findViewById(R.id.item_movie_vote_avg);
        movieAverageVote.setText(String.valueOf(movie.getVoteAverage()));
        TextView movieVoteCount = findViewById(R.id.item_movie_vote_count);
        movieVoteCount.setText(String.valueOf(movie.getVoteCount()));

        TextView movieReleaseDetails = findViewById(R.id.movie_release_details);
        movieReleaseDetails.setText(Util.computeDescription(this, movie));

        TextView movieDesc = findViewById(R.id.movie_desc);
        movieDesc.setText(movie.getOverview());
        ImageView moviePoster = findViewById(R.id.movie_poster);
        Util.loadImage(this, (movie.getBackdropPath() != null) ? movie.getBackdropPath() : movie.getPosterPath(), moviePoster, Util.IMAGE_POSTER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
