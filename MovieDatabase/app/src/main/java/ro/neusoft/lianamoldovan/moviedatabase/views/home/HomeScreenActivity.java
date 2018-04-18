package ro.neusoft.lianamoldovan.moviedatabase.views.home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import ro.neusoft.lianamoldovan.moviedatabase.R;
import ro.neusoft.lianamoldovan.moviedatabase.utils.Util;
import ro.neusoft.lianamoldovan.moviedatabase.views.BaseActivity;
import ro.neusoft.lianamoldovan.moviedatabase.views.movie.MovieListActivity;

public class HomeScreenActivity extends BaseActivity<HomeScreenContract.View, HomeScreenContract.Presenter>
        implements HomeScreenContract.View {


    @Override
    protected HomeScreenContract.Presenter initPresenter() {
        return new HomeScreenPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initViews();
    }

    private void initViews() {
        final EditText searchEditText = findViewById(R.id.search_field);
        searchEditText.setHint(Util.styleSearchHint(this));
        ImageButton searchButton = findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkConnected(HomeScreenActivity.this))
                    presenter.onSearchClick(searchEditText.getText().toString());
                else {
                    Util.displayErrorDialog(HomeScreenActivity.this, R.string.error_no_internet);
                }
            }
        });

        initTrendingMovies();
    }

    @Override
    public void startSearch(String searchTitle) {
        Intent intent = new Intent(this, MovieListActivity.class);
        intent.putExtra("search_movie_title", searchTitle);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    public void displayInvalidInput() {
        Util.displayErrorDialog(this, R.string.error_missing_search_input);
    }

    private void initTrendingMovies() {
        //carousel
        RecyclerView trendingRecyclerView = findViewById(R.id.recycler_trending_movies);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trendingRecyclerView.setLayoutManager(linearLayoutManager);
        trendingRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // static list
        int[] trendingPosters = new int[]{R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};
        CarouselAdapter carouselAdapter = new CarouselAdapter(trendingPosters);
        trendingRecyclerView.setAdapter(carouselAdapter);
    }
}
