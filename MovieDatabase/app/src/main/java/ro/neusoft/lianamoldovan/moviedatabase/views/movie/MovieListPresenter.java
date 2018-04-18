package ro.neusoft.lianamoldovan.moviedatabase.views.movie;

import android.support.annotation.VisibleForTesting;

import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.neusoft.lianamoldovan.moviedatabase.model.GetMoviesResponse;
import ro.neusoft.lianamoldovan.moviedatabase.rest.RestApi;
import ro.neusoft.lianamoldovan.moviedatabase.rest.RestService;
import ro.neusoft.lianamoldovan.moviedatabase.rest.UrlConstants;
import ro.neusoft.lianamoldovan.moviedatabase.views.BasePresenter;

/**
 * Created by liana.moldovan on 17.04.2018.
 */

public class MovieListPresenter extends BasePresenter<MovieListContract.View> implements MovieListContract.Presenter {
        /* Used for paginated server requests */
    /** Flag used to keep track whether all movies have been loaded. */
    private boolean isLastPage = false;
    /** Flag used to keep track whether a loading request is currently in progress. */
    private boolean isLoading = false;
    /** Flag used to keep track of the last page requested. */
    private int pageNumber = 0;

    /* Server requests*/
    private Call<GetMoviesResponse> getMoviesCall;
    private RestApi restApi;

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();
        // init rest service
        restApi = RestService.getClient().create(RestApi.class);
    }

    @Override
    public void loadMovies() {
        //post the request only if network connection
        if (!getView().isNetworkConnected()) {
            getView().onGetMoviesFailed(new UnknownHostException());
            return;
        }

        ++pageNumber;
        isLoading = true;

        //while loading the first page, display the main progressbar;
        // for the other pages, there will be a progressbar at the bottom of the list
        if (pageNumber == 1) {
            getView().displayMainProgressBar(true);
            getMoviesCall = restApi.getMovies(UrlConstants.API_KEY, getView().getSearchText());
        } else {
            getMoviesCall = restApi.getMovies(UrlConstants.API_KEY, getView().getSearchText(), pageNumber);
        }

        getMoviesCall.enqueue(new Callback<GetMoviesResponse>() {
            @Override
            public void onResponse(Call<GetMoviesResponse> call, Response<GetMoviesResponse> response) {
                clearLoading();

                // response.isSuccessful() is true if the response code is 2xx
                if (response.isSuccessful()) {
                    handleGetMoviesSuccessful(response.body());
                    return;
                }

                // response.isSuccessful() is false when he request reached the server, but was not successful
                // e.g. 401 - Unauthorized (Invalid API key: You must be granted a valid key)
                getView().onGetMoviesUnsuccessful();
            }

            @Override
            public void onFailure(Call<GetMoviesResponse> call, Throwable throwable) {
                // when network exception occurred talking to the server or when an unexpected
                // exception occurred creating the request or processing the response.
                clearLoading();

                //the request has been canceled in the meantime -> no need to notify the user
                if (call.isCanceled())
                    return;

                getView().onGetMoviesFailed(throwable);
            }
        });
    }

    private void handleGetMoviesSuccessful(GetMoviesResponse serverResponse) {
        getMoviesCall = null;
        if (serverResponse.getResults() != null && !serverResponse.getResults().isEmpty()) {
            //we reached the number of total pages -> no more to request
            isLastPage = serverResponse.getTotalPages() == pageNumber;
            getView().updateMoviesAdapter(serverResponse.getResults(), isLastPage);
            return;
        }

        if (pageNumber == 1) {
            // when there are no movies found at first page request
            getView().displayNoResults();
        }
    }

    /**
     * Dismiss loading bar and keep the loading state.
     * The main progressbar is displayed only when the first page is being loaded.
     * For the other pages, a loading footer is displayed.
     */
    private void clearLoading() {
        isLoading = false;
        if (pageNumber == 1) {
            getView().displayMainProgressBar(false);
        } else {
            // if the current page is greater than 1, it means that the loading bar is displayed.
            // since we want to add items before it and we want to display it later at the end, we will remove it and add it after
            // adding the new movies items
            getView().displayLoadingFooter(false);
        }
    }

    @Override
    public boolean canLoadMoreMovies() {
        return !isLoading && !isLastPage;
    }

    @Override
    public void cancelServerRequests() {
        if (getMoviesCall != null) getMoviesCall.cancel();
    }

    @VisibleForTesting
    public void setRestApi(RestApi restApi) {
        this.restApi = restApi;
    }
}
