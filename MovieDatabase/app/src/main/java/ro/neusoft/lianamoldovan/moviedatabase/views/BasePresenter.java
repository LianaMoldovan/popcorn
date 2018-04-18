package ro.neusoft.lianamoldovan.moviedatabase.views;

import android.support.annotation.CallSuper;

/**
 * Created by liana.moldovan on 13.04.2018.
 */
public abstract class BasePresenter<V extends BaseContract.View> implements BaseContract.Presenter<V> {

    private V view;

    @Override
    final public V getView() {
        return view;
    }

    @Override
    final public void attachView(V view) {
        this.view = view;
    }

    @Override
    final public void detachView() {
        view = null;
    }

    @Override
    final public boolean isViewAttached() {
        return view != null;
    }


    @CallSuper
    @Override
    public void onPresenterDestroy() {
    }

    @Override
    public void onPresenterCreated() {
        //NO-OP
    }
}