package ro.neusoft.lianamoldovan.moviedatabase.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.ImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import ro.neusoft.lianamoldovan.moviedatabase.R;
import ro.neusoft.lianamoldovan.moviedatabase.model.Movie;
import ro.neusoft.lianamoldovan.moviedatabase.model.MovieGenre;
import ro.neusoft.lianamoldovan.moviedatabase.rest.UrlConstants;

/**
 * Created by liana.moldovan on 16.04.2018.
 */

public class Util {
    private static final String TAG = Util.class.getName();

    /* Image sizes */
    public static final String IMAGE_THUMBNAIL = "/w92";
    public static final String IMAGE_POSTER = "/w185";


    /** Initializes Toolbar, enables home navigation and sets its icon. */
    public static void initToolbar(AppCompatActivity activity) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        if (toolbar == null) return;

        activity.setSupportActionBar(toolbar);
        final ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_nav_back);
        }
    }

    /** Subtract the year from date. The release date is of form: YYYY-MM-DD. */
    public static String extractYear(String releaseDate) {
        if (!TextUtils.isEmpty(releaseDate))
            try {
                return releaseDate.substring(0, 4);
            } catch (IndexOutOfBoundsException exception) {
                Log.e(TAG, "IndexOutOfBoundsException: " + exception.getMessage());
            }

        return releaseDate;
    }

    /**
     * Use Glide to load image and display it in an ImageView.
     * If the request fails or the provided {@param posterPath} is null/invalid, display the app logo as placeholder.
     *
     * @param dimen the expected size {@value IMAGE_THUMBNAIL} or{@value  IMAGE_POSTER}
     */
    public static void loadImage(Context context, String posterPath, ImageView posterView, String dimen) {
        if (TextUtils.isEmpty(posterPath)) {
            // When using RecyclerView, we must clear the view in order to avoid displaying a recycled imaged
            Glide.with(context).clear(posterView);
            // display a placeholder instead
            posterView.setImageResource(R.drawable.ic_app_logo_small);

            return;
        }

        Glide.with(context)
                .load(UrlConstants.IMAGE_BASE_URL + dimen + posterPath)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .dontAnimate()
                        .dontTransform()
                        .placeholder(R.drawable.ic_app_logo_small)
                        .error(R.drawable.ic_app_logo_small)
                ).into(posterView);
    }

    /**
     * Applies custom styling to search box hint:
     * First part of the hint is {@value ro.neusoft.lianamoldovan.moviedatabase.R.color#black}
     * Second part is {@value ro.neusoft.lianamoldovan.moviedatabase.R.color#light_gray}
     */
    public static CharSequence styleSearchHint(Context context) {
        String textPart1 = context.getString(R.string.search_field_hint_part1);
        String textPart2 = context.getString(R.string.search_field_hint_part2);

        SpannableString spannableString = new SpannableString(TextUtils.concat(textPart1, " ", textPart2));
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.black)), 0, textPart1.length(), 0);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.light_gray)), textPart1.length() + 1, spannableString.length(), 0);
        return spannableString;
    }

    /*-
     * The description is composed of the following movie details:
     * - original title (if exists)
     * - original language (if exists)
     * - list of genres (if exists)
     * - release date (if exists)
     *
     * Any of these values can be missing
     */
    public static String computeDescription(Context context, Movie movie) {
        StringBuilder desc = new StringBuilder();

        //original title
        if (!TextUtils.isEmpty(movie.getOriginalTitle()))
            desc.append(movie.getOriginalTitle());

        //original language
        if (!TextUtils.isEmpty(movie.getOriginalLanguage())) {
            desc.append(" (");
            desc.append(movie.getOriginalLanguage().toUpperCase());
            desc.append(")");
        }

        //list of genres
        String genres = appendGenres(context, movie.getGenreIds());
        if (!TextUtils.isEmpty(genres)) {
            if (!TextUtils.isEmpty(desc))
                desc.append(" | ");
            desc.append(genres);
        }

        //release date
        String releaseDate = formatDate(movie.getReleaseDate());
        if (!TextUtils.isEmpty(releaseDate)) {
            if (!TextUtils.isEmpty(desc))
                desc.append(" | ");
            desc.append(releaseDate);
        }

        return desc.toString();
    }

    /**
     * Format date as "dd MMM yyyy".
     * Keep in mind the format of release date is "yyyy-mm-dd".
     */
    private static String formatDate(String date) {
        if (!TextUtils.isEmpty(date)) {
            try {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
                // necessary to parse it to a Date before changing format
                Date parsedDate = dateFormatter.parse(date);
                dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                return dateFormatter.format(parsedDate);
            } catch (ParseException exception) {
                Log.e(TAG, "Date format exception: " + exception.getMessage());
            }
        }

        return date;
    }

    /**
     * @return String containing the list of movie genres. Uses {@link MovieGenre} for mapping the genre name.
     */
    private static String appendGenres(Context context, List<Integer> genreIds) {
        if (genreIds.isEmpty())
            return null;

        StringBuilder genres = new StringBuilder();
        String genre;
        for (int genreId : genreIds) {
            genre = context.getString(MovieGenre.get(genreId).getGenreResId());
            if (!TextUtils.isEmpty(genre)) {
                if (!TextUtils.isEmpty(genres))
                    genres.append(", ");
                genres.append(genre);
            }
        }

        return genres.toString();
    }

    /**
     * @return true if there is an active network connection,  false - otherwise
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public  static Dialog displayErrorDialog(Context context, int messageResId) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(context.getString(messageResId));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

        return alertDialog;
    }
}
