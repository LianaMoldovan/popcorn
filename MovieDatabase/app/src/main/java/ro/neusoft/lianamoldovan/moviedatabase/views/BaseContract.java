package ro.neusoft.lianamoldovan.moviedatabase.views;


public interface BaseContract {
    interface View {

    }

    interface Presenter<V extends BaseContract.View> {
        void attachView(V view);

        void detachView();

        V getView();

        boolean isViewAttached();

        void onPresenterCreated();

        void onPresenterDestroy();
    }
}
