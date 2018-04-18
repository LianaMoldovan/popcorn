package ro.neusoft.lianamoldovan.moviedatabase.views.home;

import ro.neusoft.lianamoldovan.moviedatabase.views.BaseContract;

/**
 * Created by liana.moldovan on 17.04.2018.
 */

public interface HomeScreenContract {

    interface View extends BaseContract.View {

        /** Starts the activity responsible to post the search request to server. */
        void startSearch(String searchText);

//        boolean isNetworkConnected();

        // Errors

        void displayInvalidInput();
    }

    interface Presenter extends BaseContract.Presenter<HomeScreenContract.View> {

        /**
         * Proceeds with the search request only if search text is provided and there is network connection.
         *
         * @param searchText search box input value
         */
        void onSearchClick(String searchText);

        boolean validateSearchText(String searchText);
    }
}
