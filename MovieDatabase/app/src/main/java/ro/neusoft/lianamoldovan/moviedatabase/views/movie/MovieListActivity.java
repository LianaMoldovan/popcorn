package ro.neusoft.lianamoldovan.moviedatabase.views.movie;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;

import ro.neusoft.lianamoldovan.moviedatabase.R;
import ro.neusoft.lianamoldovan.moviedatabase.model.Movie;
import ro.neusoft.lianamoldovan.moviedatabase.utils.Util;
import ro.neusoft.lianamoldovan.moviedatabase.views.BaseActivity;

public class MovieListActivity extends BaseActivity<MovieListContract.View, MovieListContract.Presenter>
        implements MovieListContract.View {
    private static final String TAG = MovieListActivity.class.getName();

    private ProgressBar progressBar;

    private MoviesAdapter moviesAdapter;

    private String searchTitle;

    private Dialog noInternetAlertDialog;

    @Override
    protected MovieListContract.Presenter initPresenter() {
        return new MovieListPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        Util.initToolbar(this);
        searchTitle = retrieveSearchTitle();
        initViews();

        // load first page of movies
        presenter.loadMovies();
    }

    private void initViews() {
        TextView searchTitleTextView = findViewById(R.id.search_movie_title);
        searchTitleTextView.setText(searchTitle);

        progressBar = findViewById(R.id.progress_bar);

        initMovies();
    }

    private String retrieveSearchTitle() {
        Bundle intentBundle = getIntent().getExtras();
        if (intentBundle != null && intentBundle.containsKey("search_movie_title")) {
            return intentBundle.getString("search_movie_title");
        }
        return "";
    }

    private void initMovies() {
        RecyclerView moviesRecyclerView = findViewById(R.id.recycler_movies);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        moviesRecyclerView.setLayoutManager(linearLayoutManager);
        moviesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        moviesRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        moviesAdapter = new MoviesAdapter(this);
        moviesRecyclerView.setAdapter(moviesAdapter);
    }


    @Override
    public void displayMainProgressBar(boolean display) {
        progressBar.setVisibility(display ? View.VISIBLE : View.GONE);
    }

    @Override
    public void displayLoadingFooter(boolean display) {
        if (display)
            moviesAdapter.addLoading();
        else
            moviesAdapter.removeLoading();
    }

    @Override
    public void updateMoviesAdapter(List<Movie> results, boolean isLastPage) {
        moviesAdapter.addMovies(results);
        if (!isLastPage) moviesAdapter.addLoading();
    }

    @Override
    public void onGetMoviesUnsuccessful() {
        Log.e(TAG, "Server request - was not successful");
        Util.displayErrorDialog(this, R.string.error_general);
    }

    @Override
    public void onGetMoviesFailed(Throwable throwable) {
        Log.e(TAG, "Server request failed: " + throwable.getMessage());

        if (throwable instanceof UnknownHostException || throwable instanceof ConnectException) {
            Log.e(TAG, "Fail: No Internet connection");
            noInternetAlertDialog = Util.displayErrorDialog(this, R.string.error_no_internet);
            return;
        }

        Util.displayErrorDialog(this, R.string.error_general);
    }

    @Override
    public String getSearchText() {
        return searchTitle;
    }

    @Override
    public boolean isNetworkConnected() {
        return Util.isNetworkConnected(this);
    }

    @Override
    public void displayNoResults() {
        Util.displayErrorDialog(this, R.string.no_results);
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

    @Override
    public void onBackPressed() {
        presenter.cancelServerRequests();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        presenter.cancelServerRequests();
        super.onPause();
    }

    private final RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = linearLayoutManager.getChildCount();
            int totalItemCount = linearLayoutManager.getItemCount();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

            // while this is not the last page and there is no other request in progress, keep requesting alerts
            if (presenter.canLoadMoreMovies() && !isInternetAlertDisplayed()) {
                //            if (presenter.canLoadMoreMovies()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    presenter.loadMovies();
                }
            }
        }
    };

    boolean isInternetAlertDisplayed() {
        return noInternetAlertDialog != null && noInternetAlertDialog.isShowing();
    }
}
