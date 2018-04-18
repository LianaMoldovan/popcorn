package ro.neusoft.lianamoldovan.moviedatabase.validation;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by liana.moldovan on 17.04.2018.
 */

public class SearchTextValidatorTest {
    private static SearchTextValidator validator;

    @BeforeClass
    public static void init() {
        validator = new SearchTextValidator();
    }

    /**
     * -Should succeed if the searchText is null string
     * -Should fail when searchText is not null
     *
     * @throws Exception
     */
    @Test
    public void testNullSearchText() throws Exception {
        String searchText = null;

        Validator.ERROR_TYPE errorType = validator.validate(searchText);
        Assert.assertEquals(Validator.ERROR_TYPE.INVALID, errorType);
    }

    /**
     * -Should succeed if the searchText is empty string
     * -Should fail when searchText is not empty
     *
     * @throws Exception
     */
    @Test
    public void testEmptySearchText() throws Exception {
        String searchText = "";

        Validator.ERROR_TYPE errorType = validator.validate(searchText);
        Assert.assertEquals(Validator.ERROR_TYPE.INVALID, errorType);
    }

    /**
     * -Should succeed when searchText provided
     * -Should fail when searchText is null string OR empty string
     *
     * @throws Exception
     */
    @Test
    public void testCorrectSearchText() throws Exception {
        String searchText = "Batman";

        Validator.ERROR_TYPE errorType = validator.validate(searchText);
        Assert.assertEquals(Validator.ERROR_TYPE.VALID, errorType);
    }
}