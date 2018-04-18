package ro.neusoft.lianamoldovan.moviedatabase.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ro.neusoft.lianamoldovan.moviedatabase.model.GetMoviesResponse;

/**
 * Created by liana.moldovan on 16.04.2018.
 */

public interface RestApi {
    @GET("search/movie")
    Call<GetMoviesResponse> getMovies(
            @Query("api_key") String apiKey,
            @Query("query") String movieTitle,
            @Query("page") int pageIndex
    );

    @GET("search/movie")
    Call<GetMoviesResponse> getMovies(
            @Query("api_key") String apiKey,
            @Query("query") String movieTitle
    );
}
