package ro.ubb.lab.domain.validators;

/**
 * Created by horatiu on 13.03.2017.
 */
public class ValidatorException extends CustomException
{
    public ValidatorException(String message)
    {
        super(message);
    }

    public ValidatorException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ValidatorException(Throwable cause)
    {
        super(cause);
    }
}