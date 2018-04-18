package ro.neusoft.lianamoldovan.moviedatabase.views.movie;

import java.util.List;

import ro.neusoft.lianamoldovan.moviedatabase.model.Movie;
import ro.neusoft.lianamoldovan.moviedatabase.views.BaseContract;

/**
 * Created by liana.moldovan on 17.04.2018.
 */

public interface MovieListContract extends BaseContract {
    interface View extends BaseContract.View {

        void displayMainProgressBar(boolean display);

        void displayLoadingFooter(boolean display);

        /** Called when there are no movies found during first page request. */
        void displayNoResults();

        void updateMoviesAdapter(List<Movie> results, boolean isLastPage);

        /** Called when he request reached the server, but was not successful (e.g. 401 - Unauthorized ) */
        void onGetMoviesUnsuccessful();

        /**
         * Called when network exception occurred while talking to the server
         * or when an unexpected exception occurred creating the request or processing the response
         */
        void onGetMoviesFailed(Throwable throwable);

        String getSearchText();

        boolean isNetworkConnected();
    }

    interface Presenter extends BaseContract.Presenter<MovieListContract.View> {

        void loadMovies();

        void cancelServerRequests();

        /** Specifies if a movies loading request is in progress or the total pages has been reached. */
        boolean canLoadMoreMovies();
    }
}
