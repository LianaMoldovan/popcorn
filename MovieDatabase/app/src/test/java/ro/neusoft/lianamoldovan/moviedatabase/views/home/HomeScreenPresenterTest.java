package ro.neusoft.lianamoldovan.moviedatabase.views.home;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by liana.moldovan on 17.04.2018.
 */
public class HomeScreenPresenterTest {
    private HomeScreenContract.View homeView;
    private HomeScreenPresenter homePresenter;


    /**
     * Creates a mock fof the View and the Presenter
     * Adds the mock as a View to the Presenter
     */
    @Before
    public void init() {
        homeView = mock(HomeScreenContract.View.class);
        homePresenter = new HomeScreenPresenter();
        homePresenter.attachView(homeView);
    }

    /**
     * Should succeed if search input is not empty/null
     *
     * @throws Exception
     */
    @Test
    public void testSearchWhenValidInput() throws Exception {
        String searchText = "batman";
        homePresenter.onSearchClick(searchText);
        verify(homeView).startSearch(searchText);
    }

    @Test
    public void testSearchWhenNoInput() throws Exception {
        // when no valid input, search should never be performed and should display an error
        homePresenter.onSearchClick("");
        verify(homeView, never()).startSearch(anyString());
        verify(homeView).displayInvalidInput();
    }
}
