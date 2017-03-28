package ro.ubb.lab.domain.validators;

/**
 * Created by horatiu on 13.03.2017.
 */
public class CustomException extends RuntimeException
{
    public CustomException(String message)
    {
        super(message);
    }

    public CustomException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CustomException(Throwable cause)
    {
        super(cause);
    }
}