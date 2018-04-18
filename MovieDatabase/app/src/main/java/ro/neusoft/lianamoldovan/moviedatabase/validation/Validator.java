package ro.neusoft.lianamoldovan.moviedatabase.validation;

/**
 * Created by liana.moldovan on 17.04.2018.
 */

public interface Validator {
    enum ERROR_TYPE {
        INVALID,
        VALID
    }

    ERROR_TYPE validate(Object objToVerify);
}
