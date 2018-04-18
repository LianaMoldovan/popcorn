package ro.neusoft.lianamoldovan.moviedatabase.views.movie;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.neusoft.lianamoldovan.moviedatabase.model.GetMoviesResponse;
import ro.neusoft.lianamoldovan.moviedatabase.model.Movie;
import ro.neusoft.lianamoldovan.moviedatabase.rest.RestApi;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by liana.moldovan on 17.04.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class MovieListPresenterTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Captor
    private ArgumentCaptor<Callback<GetMoviesResponse>> getMoviesResponseCallBack;
    @Mock
    private Call<GetMoviesResponse> mockGetMoviesCall;
    @Mock
    private RestApi mockAPI;

    private MovieListContract.View view;
    private MovieListPresenter presenter;

    /**
     * Creates a mock from MovieListContract.View
     * Adds the mock as a View to the Presenter
     */
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        view = mock(MovieListContract.View.class);
        presenter = new MovieListPresenter();
        presenter.attachView(view);
        presenter.setRestApi(mockAPI);
    }

    /**
     * Simulates successful {@link RestApi#getMovies(String, String)}
     * <p>
     * Steps:
     * Assume there is internet connection
     * <p>
     * 1. {@link MovieListPresenter#loadMovies()} is called
     * 2. request is simulated
     * 3. on successful response {@link MovieListContract.View#updateMoviesAdapter(List, boolean)}
     */
    @Test
    public void testSuccessfulGetMovies() throws Exception {
        //assume  internet connection
        when(view.isNetworkConnected()).thenReturn(true);

        // mock the api call
        when(mockAPI.getMovies(anyString(), anyString(), anyInt())).thenReturn(mockGetMoviesCall);
        when(mockAPI.getMovies(anyString(), anyString())).thenReturn(mockGetMoviesCall);

        presenter.loadMovies();

        verify(mockGetMoviesCall).enqueue(getMoviesResponseCallBack.capture());

        //prepare fake response
        GetMoviesResponse responseBody = fakeSearchResults();
        Response<GetMoviesResponse> response = Response.success(responseBody);

        getMoviesResponseCallBack.getValue().onResponse(mockGetMoviesCall, response);
        //the movies list is updated
        verify(view).updateMoviesAdapter(anyList(), anyBoolean());
    }

    /**
     * Simulates successful {@link RestApi#getMovies(String, String)} with no results
     * <p>
     * Steps:
     * Assume there is internet connection
     * <p>
     * 1. {@link MovieListPresenter#loadMovies()} is called
     * 2. request is simulated
     * 3. on successful response {@link MovieListContract.View#displayNoResults()}
     */
    @Test
    public void testSuccessfulGetMoviesNoResults() throws Exception {
        //assume no internet connection
        when(view.isNetworkConnected()).thenReturn(true);

        // mock the call
        when(mockAPI.getMovies(anyString(), anyString(), anyInt())).thenReturn(mockGetMoviesCall);
        when(mockAPI.getMovies(anyString(), anyString())).thenReturn(mockGetMoviesCall);

        presenter.loadMovies();

        //prepare fake response ->
        GetMoviesResponse responseBody = fakeSearchNoResults();
        Response<GetMoviesResponse> response = Response.success(responseBody);

        verify(mockGetMoviesCall).enqueue(getMoviesResponseCallBack.capture());
        getMoviesResponseCallBack.getValue().onResponse(mockGetMoviesCall, response);
        //the list of movies should not be updated
        verify(view, never()).updateMoviesAdapter(anyList(), anyBoolean());
        //"No results" message should be displayed
        verify(view).displayNoResults();
    }


    /**
     * Simulates unsuccessful {@link RestApi#getMovies(String, String)} due to "Invalid API key"
     * <p>
     * Steps:
     * Assume there is internet connection
     * <p>
     * 1. {@link MovieListPresenter#loadMovies()} is called
     * 2. request is simulated
     * 3. on response {@link MovieListContract.View#onGetMoviesUnsuccessful()}
     */
    @Test
    public void testUnsuccessfulGetMoviesBecauseUnauthorized() throws Exception {
        //assume internet connection
        when(view.isNetworkConnected()).thenReturn(true);

        //mock the call
        when(mockAPI.getMovies(anyString(), anyString(), anyInt())).thenReturn(mockGetMoviesCall);
        when(mockAPI.getMovies(anyString(), anyString())).thenReturn(mockGetMoviesCall);

        presenter.loadMovies();

        //prepare fake response
        Response<GetMoviesResponse> response = Response.error(401,
                ResponseBody.create(MediaType.parse("application/json;charset=UTF-8"), "Unauthorized"));

        verify(mockGetMoviesCall).enqueue(getMoviesResponseCallBack.capture());
        getMoviesResponseCallBack.getValue().onResponse(mockGetMoviesCall, response);

        //verify the list of movies is not updated
        verify(view, never()).updateMoviesAdapter(anyList(), anyBoolean());
        verify(view).onGetMoviesUnsuccessful();
    }

    /**
     * Simulates failed {@link RestApi#getMovies(String, String)} request due to losing connectivity while in progress
     * <p>
     * Steps:
     * Assume there is internet connection
     * <p>
     * 1. {@link MovieListPresenter#loadMovies()} is called
     * 2. request is simulated
     * 3. onFailure  {@link MovieListContract.View#onGetMoviesFailed(Throwable)}
     */
    @Test
    public void testErrorDisplayedWhenDisconnectingFromInternet() throws Exception {
        //assume there is internet connection
        when(view.isNetworkConnected()).thenReturn(true);

        // mock api call
        when(mockAPI.getMovies(anyString(), anyString(), anyInt())).thenReturn(mockGetMoviesCall);
        when(mockAPI.getMovies(anyString(), anyString())).thenReturn(mockGetMoviesCall);

        presenter.loadMovies();
        //the call is posted
        verify(mockGetMoviesCall).enqueue(getMoviesResponseCallBack.capture());
        getMoviesResponseCallBack.getValue().onFailure(mockGetMoviesCall, any(UnknownHostException.class));

        //verify that view is notified of failure
        verify(view).onGetMoviesFailed(any(UnknownHostException.class));
    }

    /**
     * {@link RestApi#getMovies(String, String)} should never be called if there is no Internet connection
     * <p>
     * Steps:
     * Assume there is NO internet connection
     * <p>
     * 1. {@link MovieListPresenter#loadMovies()} is called
     * 2. request is never posted, but {@link MovieListContract.View#onGetMoviesFailed(Throwable)} is called
     */
    @Test
    public void testRequestNotPerformedWhenNoInternet() throws Exception {
        //assume no internet connection
        when(view.isNetworkConnected()).thenReturn(false);

        // mock api call
        when(mockAPI.getMovies(anyString(), anyString(), anyInt())).thenReturn(mockGetMoviesCall);
        when(mockAPI.getMovies(anyString(), anyString())).thenReturn(mockGetMoviesCall);

        presenter.loadMovies();
        //verify that view is notified of failure
        verify(view).onGetMoviesFailed(any(UnknownHostException.class));
        //verify the request is never posted
        verify(mockGetMoviesCall, never()).enqueue(any(Callback.class));
    }


    /**
     * Returns a fake response
     */
    private GetMoviesResponse fakeSearchResults() {
        GetMoviesResponse searchResults = new GetMoviesResponse();
        searchResults.setPage(1);
        searchResults.setTotalPages(1);
        searchResults.setResults(new ArrayList<>(Arrays.asList(fakeMovie(), fakeMovie())));
        return searchResults;
    }

    /**
     * Returns a fake response
     */
    private GetMoviesResponse fakeSearchNoResults() {
        GetMoviesResponse searchResults = new GetMoviesResponse();
        searchResults.setPage(1);
        searchResults.setTotalPages(1);
        searchResults.setResults(new ArrayList<Movie>());
        return searchResults;
    }

    /**
     * Returns a fake response
     */
    private Movie fakeMovie() {
        Movie movie = new Movie();
        movie.setTitle("Batman");
        movie.setId(16535);
        movie.setVoteCount(33);
        return movie;
    }
}