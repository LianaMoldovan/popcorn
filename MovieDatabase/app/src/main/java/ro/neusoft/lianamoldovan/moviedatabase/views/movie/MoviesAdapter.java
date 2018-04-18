package ro.neusoft.lianamoldovan.moviedatabase.views.movie;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.neusoft.lianamoldovan.moviedatabase.R;
import ro.neusoft.lianamoldovan.moviedatabase.model.Movie;
import ro.neusoft.lianamoldovan.moviedatabase.utils.Util;
import ro.neusoft.lianamoldovan.moviedatabase.views.movie.details.MovieDetailsActivity;

/**
 * Created by liana.moldovan on 13.04.2018.
 */

class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = MoviesAdapter.class.getName();

    /* Item types */
    private static final int ITEM_MOVIE = 0;
    private static final int ITEM_LOADING = 1;

    private List<Movie> movies = new ArrayList<>();
    private final AppCompatActivity context;

    /** Flag used to determine whether the loading bar is added to the list to be displayed. */
    private boolean isLoadingFooterAdded = false;


    MoviesAdapter(AppCompatActivity context) {
        this.context = context;
    }

    private void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    void addMovies(List<Movie> newMovies) {
        if (movies.isEmpty()) {
            setMovies(newMovies);
            return;
        }

        int index = movies.size();
        movies.addAll(index, newMovies);
        notifyItemRangeInserted(index, newMovies.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_MOVIE) {
            return new MovieViewHolder(inflater.inflate(R.layout.list_item_movie, parent, false));
        } else {
            return new LoadingViewHolder(inflater.inflate(R.layout.list_item_loading_bar, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_MOVIE:
                MovieViewHolder movieHolder = (MovieViewHolder) holder;
                Movie movie = movies.get(position);
                Util.loadImage(context, movie.getPosterPath(), movieHolder.moviePoster, Util.IMAGE_THUMBNAIL);
                movieHolder.movieTitle.setText(movie.getTitle());
                movieHolder.movieYear.setText(Util.extractYear(movie.getReleaseDate()));
                movieHolder.movieAverageVote.setText(String.valueOf(movie.getVoteAverage()));
                movieHolder.movieVoteCount.setText(String.valueOf(movie.getVoteCount()));
                movieHolder.movieDesc.setText(Util.computeDescription(context, movie));
                break;
            case ITEM_LOADING:
                LoadingViewHolder loadHolder = (LoadingViewHolder) holder;
                loadHolder.loadingMore.setIndeterminate(true);
                break;
            default:
                throw new IllegalArgumentException("No such item type!");
        }
    }

    @Override
    public int getItemCount() {
        if (isLoadingFooterAdded)
            return movies.size() + 1;
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movies.size() && isLoadingFooterAdded) ? ITEM_LOADING : ITEM_MOVIE;
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    void addLoading() {
        isLoadingFooterAdded = true;
        // notify loading item has been added to the end of the movies list
        notifyItemInserted(movies.size());
    }

    void removeLoading() {
        int position = movies.size();
        int itemCount = getItemCount();

        isLoadingFooterAdded = false;
        // if the number of items displayed is greater then the number of movies, this means the loading item is
        // displayed. In this case remove the loading item and notify that it has been removed from the end of the
        // movies list
        if (itemCount > position)
            notifyItemRemoved(position);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView moviePoster;
        final TextView movieTitle;
        final TextView movieYear;
        final TextView movieDesc;
        final TextView movieAverageVote;
        final TextView movieVoteCount;

        MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            movieTitle = itemView.findViewById(R.id.item_movie_title);
            moviePoster = itemView.findViewById(R.id.item_movie_poster);
            movieYear = itemView.findViewById(R.id.item_movie_year);
            movieDesc = itemView.findViewById(R.id.item_movie_desc);
            movieAverageVote = itemView.findViewById(R.id.item_movie_vote_avg);
            movieVoteCount = itemView.findViewById(R.id.item_movie_vote_count);
        }

        @Override
        public void onClick(View view) {
            final int selectedPosition = getAdapterPosition();
            Log.i(TAG, "Selected position " + selectedPosition);

            if (selectedPosition < 0) {
                Log.w(TAG, "Invalid position " + selectedPosition);
                return;
            }

            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("selected_movie", movies.get(selectedPosition));
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
        }
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        final ProgressBar loadingMore;

        LoadingViewHolder(View itemView) {
            super(itemView);
            loadingMore = itemView.findViewById(R.id.loading_bar);
        }
    }
}