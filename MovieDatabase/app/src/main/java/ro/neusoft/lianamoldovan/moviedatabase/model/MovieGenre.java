package ro.neusoft.lianamoldovan.moviedatabase.model;

import android.util.SparseArray;

import java.util.EnumSet;

import ro.neusoft.lianamoldovan.moviedatabase.R;

/**
 * Created by liana.moldovan on 16.04.2018.
 */

/** Mocked values for movie genre. */
public enum MovieGenre {
    ACTION(28, R.string.movie_genre_action),
    Adventure(12, R.string.movie_genre_adventure),
    ANIMATION(16, R.string.movie_genre_animation),
    COMEDY(35, R.string.movie_genre_comedy),
    CRIME(80, R.string.movie_genre_crime),
    DOCUMENTARY(99, R.string.movie_genre_documentary),
    DRAMA(18, R.string.movie_genre_drama),
    FAMILY(10751, R.string.movie_genre_family),
    FANTASY(14, R.string.movie_genre_fantasy),
    HISTORY(36, R.string.movie_genre_history),
    HORROR(27, R.string.movie_genre_horror),
    MUSIC(10402, R.string.movie_genre_music),
    MYSTERY(9648, R.string.movie_genre_mystery),
    ROMANCE(10749, R.string.movie_genre_romance),
    SCIENCE_FICTION(878, R.string.movie_genre_sf),
    TV_MOVIE(10770, R.string.movie_genre_tv),
    THRILLER(53, R.string.movie_genre_thriller),
    WAR(10752, R.string.movie_genre_war),
    WESTERN(37, R.string.movie_genre_western);


    // static methods to get an enum depending on the movie genre id
    private static final SparseArray<MovieGenre> lookupCode = new SparseArray<>();

    static {
        for (MovieGenre movieGenre : EnumSet.allOf(MovieGenre.class))
            lookupCode.put(movieGenre.getGenreId(), movieGenre);
    }

    /** Movie gender expected from server (Mock value for now). */
    private final int genreResId;
    private final int genreId;

    /**
     * Constructor with parameters.
     *
     * @param genreId    of the movie
     * @param genreResId res id for genre name
     */
    MovieGenre(int genreId, int genreResId) {
        this.genreId = genreId;
        this.genreResId = genreResId;
    }

    public int getGenreResId() {
        return genreResId;
    }

    public int getGenreId() {
        return genreId;
    }

    public static MovieGenre get(Integer genderId) {
        return lookupCode.get(genderId);
    }
}
