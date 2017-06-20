package wedding.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import wedding.models.Person;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;
import java.util.*;

/**
 * Created by Antonina on 6/18/2017.
 */
public class StaxXmlAssistant {
    private XMLEventWriter xmlEventWriter;
    private XMLEventReader xmlEventReader;
    private XMLEventFactory eventFactory;
    private XMLEvent end;

    private ArrayList<Integer> ids = new ArrayList<>();

    public StaxXmlAssistant() {
        eventFactory = XMLEventFactory.newInstance();
        end = eventFactory.createDTD("\n");
    }

    public void write(String fileName, ArrayList<Person> people) {
        try {
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
            xmlEventWriter = xmlOutputFactory
                    .createXMLEventWriter(new FileOutputStream(new File(fileName)), "UTF-8");

            String rootElement = "people";
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            XMLEvent end = eventFactory.createDTD("\n");

            xmlEventWriter.add(eventFactory.createStartDocument());
            xmlEventWriter.add(end);

            addStartElement(rootElement);

            for(Person person: people) {
                addElement(person);
            }

            addEndElement(rootElement);

            xmlEventWriter.add(eventFactory.createEndDocument());
            xmlEventWriter.close();
        } catch (XMLStreamException |FileNotFoundException e) {
            System.out.print(e.getClass() + ": " + e.getCause());
        }
    }

    public ArrayList<Person> read(String fileName) {
        ArrayList<Person> people = new ArrayList<>();
        Person person = null;
        ArrayList<String> propositions = null, demands = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
            while(xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if(xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();

                    if(startElement.getName().getLocalPart().equals("person")) {
                        person = new Person();
                        Attribute idAttr = startElement.getAttributeByName(new QName("id"));
                        if(idAttr != null){
                            Integer id = Integer.parseInt(idAttr.getValue());
                            person.setId(id);
                            ids.add(id);
                        }
                    } else if(startElement.getName().getLocalPart().equals("birthYear")) {
                        person.setBirthYear(getEventData());
                    } else if(startElement.getName().getLocalPart().equals("name")) {
                        person.setName(getEventData());
                    } else if(startElement.getName().getLocalPart().equals("surname")) {
                        person.setSurname(getEventData());
                    } else if(startElement.getName().getLocalPart().equals("propositions")) {
                        propositions = new ArrayList<>();
                        addDataToArray(propositions);
                    } else if(startElement.getName().getLocalPart().equals("demands")) {
                        demands = new ArrayList<>();
                        addDataToArray(demands);
                    } else if (startElement.getName().getLocalPart().equals("demand")) {
                        if (demands != null) {
                            demands.add(getEventData());
                        }
                    } else if (startElement.getName().getLocalPart().equals("proposition")) {
                        if (propositions != null) {
                            propositions.add(getEventData());
                        }
                    }
                } else if(xmlEvent.isEndElement()) {
                    EndElement endElement = xmlEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals("person")) {
                       person.setPropositions(propositions);
                       person.setDemands(demands);
                       people.add(person);

                       propositions = null;
                       demands = null;
                       person = null;
                    }
                }
            }
        } catch (XMLStreamException |FileNotFoundException e) {
            System.out.print(e.getClass() + ": " + e.getCause());
        }

        return people;
    }

    private String getEventData() throws XMLStreamException{
        XMLEvent xmlEvent = xmlEventReader.nextEvent();
        return xmlEvent.asCharacters().getData();
    }

    private void addDataToArray(ArrayList arrayList) throws XMLStreamException{
        String data = getEventData();
        if (isRealData(data)) {
            arrayList.add(data);
        }
    }

    public void create(String fileName, Person person) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        try {
            xmlEventReader = xmlInputFactory.createXMLEventReader(new FileReader(fileName));
            xmlEventWriter = xmlOutputFactory.createXMLEventWriter(new FileWriter(fileName));
            while(xmlEventReader.hasNext()) {
                XMLEvent event = xmlEventReader.nextEvent();
                xmlEventWriter.add(event);
                if (event.getEventType() == XMLEvent.START_ELEMENT) {
                    if (event.asStartElement().getName().toString().equals("people")) {
                        addElement(person);
                    }
                }
            }
            xmlEventWriter.close();
        } catch (XMLStreamException | IOException e) {
            System.out.print(e.getClass() + ": " + e.getCause());
        }
    }

    public void delete(String fileName, String id) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();

        boolean deleteSection = false;
        try {
            xmlEventReader = xmlInputFactory.createXMLEventReader(new FileReader(fileName));
            xmlEventWriter = xmlOutputFactory.createXMLEventWriter(new FileWriter(fileName));
            while(xmlEventReader.hasNext()) {
                XMLEvent event = xmlEventReader.nextEvent();

                if (event.getEventType() == XMLEvent.START_ELEMENT &&
                        event.asStartElement().getName().toString().equals("person")) {
                    StartElement startElement  = event.asStartElement();
                    Attribute elementId = startElement.getAttributeByName(new QName("id"));
                    if (elementId.getValue().equals(id)) {
                        deleteSection = true;
                    } else {
                        xmlEventWriter.add(event);
                    }
                } else if (event.getEventType() == XMLEvent.END_ELEMENT &&
                        event.asEndElement().getName().toString().equals("person")) {
                    if (!deleteSection) {
                        xmlEventWriter.add(event);
                    } else {
                        deleteSection = false;
                    }
                } else if (!deleteSection) {
                    xmlEventWriter.add(event);
                }
            }
            xmlEventWriter.close();
        } catch (XMLStreamException | IOException e) {
            System.out.print(e.getClass() + ": " + e.getCause());
        }
    }

    private boolean isRealData(String data) {
        return data.matches(".*[a-zA-Z]+.*");
    }

    private Integer getNextId() {
        int maxCurrentId = Collections.max(ids);
        ids.add(++maxCurrentId);
        return maxCurrentId;
    }

    private void addElement(Person person) throws XMLStreamException{
        String elementName = "person";

        xmlEventWriter.add(end);
        xmlEventWriter.add(eventFactory.createStartElement("", "", elementName));
        xmlEventWriter.add(eventFactory.createAttribute("id", getNextId() + ""));
        xmlEventWriter.add(end);

        Map<String, Object> objectMap = convertObjectToMap(person);
        Set<String> elementNodes = objectMap.keySet();
        for(String key : elementNodes){
            ArrayList<String> values = getValues(key, objectMap);
            XMLEvent tab = eventFactory.createDTD("\t");
            if (!key.equals("id")) {
                addNode(key, values, tab);
            }
        }

        addEndElement(elementName);
    }

    private void addNode(String key, ArrayList<String> values, XMLEvent tab) throws XMLStreamException{
        if (values.size() > 1) {
            XMLEvent doubleTab = eventFactory.createDTD("\t \t");
            xmlEventWriter.add(tab);
            addStartElement(key);
            for(String value: values) {
                ArrayList<String> bufArray = new ArrayList<>();
                bufArray.add(value);

                String singularFormKey = key.substring(0, key.length() - 1);
                addNode(singularFormKey, new ArrayList<>(bufArray), doubleTab);
            }
            xmlEventWriter.add(tab);
            addEndElement(key);
        }  else {
            xmlEventWriter.add(tab);
            xmlEventWriter.add(eventFactory.createStartElement("", "", key));
            Characters characters = eventFactory.createCharacters(values.get(0));
            xmlEventWriter.add(characters);

            addEndElement(key);
        }
    }

    private void addStartElement(String elementName) throws  XMLStreamException{
        xmlEventWriter.add(eventFactory.createStartElement("", "", elementName));
        xmlEventWriter.add(end);
    }

    private void addEndElement(String elementName) throws XMLStreamException {
        xmlEventWriter.add(eventFactory.createEndElement("", "", elementName));
        xmlEventWriter.add(end);
    }

    private Map<String, Object> convertObjectToMap(Person person) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(person, Map.class);
    }

    private ArrayList<String> getValues(String key, Map<String, Object> map) {
        ArrayList<String> values = new ArrayList<>();
        Object value = map.get(key);
        if (value instanceof String) {
            values.add((String)value);
        } else if (value instanceof Integer) {
            values.add(value + "");
        } else if (value instanceof ArrayList) {
            values = (ArrayList<String>)map.get(key);
        }
        return values;
    }
}
