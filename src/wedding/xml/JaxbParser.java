package wedding.xml;

import wedding.models.People;
import wedding.models.Person;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Antonina on 6/28/2017.
 */
public class JaxbParser {

    public void create(String fileName, Person person) throws JAXBException{
        People people = unmarshalFromFile(fileName);
        people.addPerson(person);
        marshalToFile(fileName, people);
    }

    public void delete(String fileName, int id) throws JAXBException{
        People people = unmarshalFromFile(fileName);
        people.getItems().removeIf((Person person) -> id == person.getId());
        marshalToFile(fileName, people);
    }

    public List read(String fileName) throws JAXBException{
        return unmarshalFromFile(fileName).getItems();
    }

    private void marshalToFile(String fileName, People people) throws JAXBException{
        JAXBContext context = JAXBContext.newInstance(People.class, Person.class);
        Marshaller marshaller = context.createMarshaller();

        QName qName = new QName("people");
        JAXBElement<People> jaxbElement = new JAXBElement<>(qName, People.class, people);
        marshaller.marshal(jaxbElement, new File(fileName));
    }

    private People unmarshalFromFile(String fileName) throws JAXBException{
        JAXBContext context = JAXBContext.newInstance(People.class, Person.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller.unmarshal(new StreamSource(fileName), People.class).getValue();
    }
}
