package ro.ubb.lab.domain;

/**
 * Created by horatiu on 13.03.2017.
 */
public class BaseEntity<ID> {
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}
