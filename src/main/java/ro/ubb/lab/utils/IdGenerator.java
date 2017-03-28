package ro.ubb.lab.utils;

/**
 * Created by horatiu on 27.03.2017.
 */
public class IdGenerator
{
    private static Long id = new Long(0);

    public static Long generateID()
    {
        return new Long(++id);
    }
}