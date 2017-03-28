package ro.ubb.lab.domain.validators;

import ro.ubb.lab.domain.Client;

/**
 * Created by horatiu on 27.03.2017.
 */
public class ClientValidator implements Validator<Client> {
    public ClientValidator(){

    }

    public void validate(Client entity) throws ValidatorException
    {
        String err="";
        Long ID=(Long)entity.getId();
        String desc=(String)entity.getName();

        if(ID==null)
            err+="ID must not be null.\n";
        if(ID.longValue() < 0L)
            err = err + "ID must be positive. ";

        if(desc==null)
            err+="Name must not be null.\n";

        if(err!="")
        {
            throw new ValidatorException(err);
        }
    }
}
