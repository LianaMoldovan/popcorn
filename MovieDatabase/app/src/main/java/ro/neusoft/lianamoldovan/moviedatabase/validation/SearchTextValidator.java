package ro.neusoft.lianamoldovan.moviedatabase.validation;

import static ro.neusoft.lianamoldovan.moviedatabase.validation.Validator.ERROR_TYPE.INVALID;
import static ro.neusoft.lianamoldovan.moviedatabase.validation.Validator.ERROR_TYPE.VALID;

/**
 * Created by liana.moldovan on 17.04.2018.
 */

public class SearchTextValidator implements Validator {

    public SearchTextValidator() {

    }

    /**
     * Validates the search input.
     * The search text is considered valid if has value.
     *
     * @param objToVerify
     * @return ERROR_TYPE
     */
    @Override
    public ERROR_TYPE validate(Object objToVerify) {
        if (!(objToVerify instanceof String))
            return INVALID;

        if (objToVerify == null || objToVerify.toString().isEmpty())
            return INVALID;

        return VALID;
    }
}