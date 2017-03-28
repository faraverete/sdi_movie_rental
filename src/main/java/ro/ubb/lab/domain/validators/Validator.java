package ro.ubb.lab.domain.validators;

/**
 * Created by horatiu on 13.03.2017.
 */
public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
