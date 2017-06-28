package wedding.models;

import javax.xml.bind.annotation.XmlAnyElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonina on 6/28/2017.
 */
public class People {
    private List<Person> items;

    public People() {
        items = new ArrayList<Person>();
    }

    public People(List<Person> items) {
        this.items = items;
    }

    @XmlAnyElement(lax=true)
    public List<Person> getItems() {
        return items;
    }

    public void addPerson(Person p) {
        this.items.add(p);
    }
}
