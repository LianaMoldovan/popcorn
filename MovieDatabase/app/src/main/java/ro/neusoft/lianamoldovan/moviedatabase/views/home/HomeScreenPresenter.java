package ro.neusoft.lianamoldovan.moviedatabase.views.home;

import ro.neusoft.lianamoldovan.moviedatabase.validation.SearchTextValidator;
import ro.neusoft.lianamoldovan.moviedatabase.validation.Validator;
import ro.neusoft.lianamoldovan.moviedatabase.views.BasePresenter;

/**
 * Created by liana.moldovan on 17.04.2018.
 */

public class HomeScreenPresenter extends BasePresenter<HomeScreenContract.View> implements HomeScreenContract.Presenter {
    @Override
    public void onSearchClick(String searchText) {
        if (!isViewAttached())
            return;

        if (validateSearchText(searchText))//&  getView().isNetworkConnected()
            getView().startSearch(searchText);
    }

    @Override
    public boolean validateSearchText(String searchText) {
        if (new SearchTextValidator().validate(searchText) != Validator.ERROR_TYPE.VALID) {
            getView().displayInvalidInput();
            return false;
        }
        return true;
    }
}
