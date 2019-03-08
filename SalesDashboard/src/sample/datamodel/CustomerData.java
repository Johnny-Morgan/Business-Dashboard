package sample.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;


/**
 * Created by Johnny on 27/02/2019
 */

public class CustomerData {

    private static final String CUSTOMERS_FILE = "customers.xml";

    private static final String CUSTOMER = "customer";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String EMAIL = "email";
    private static final String DATE = "date";
    private ObservableList<Customer> customers;

    public CustomerData() {
        customers = FXCollections.observableArrayList();
    }

    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    public void addCustomer(Customer customer){
        customers.add(customer);
    }

    public void deleteCustomer(Customer customer){
        customers.remove(customer);
    }

    public void loadCustomers() {
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = new FileInputStream(CUSTOMERS_FILE);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            Customer customer = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have a customer item, we create a new customer
                    if (startElement.getName().getLocalPart().equals(CUSTOMER)) {
                        customer = new Customer();
                        continue;
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(FIRST_NAME)) {
                            event = eventReader.nextEvent();
                            customer.setFirstName(event.asCharacters().getData());
                            continue;
                        }
                    }
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(LAST_NAME)) {
                        event = eventReader.nextEvent();
                        customer.setLastName(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(PHONE_NUMBER)) {
                        event = eventReader.nextEvent();
                        customer.setPhoneNum(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(EMAIL)) {
                        event = eventReader.nextEvent();
                        customer.setEmail(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(DATE)) {
                        event = eventReader.nextEvent();
                        customer.setDate(event.asCharacters().getData());
                        continue;
                    }

                }

                // If we reach the end of a customer element, we add it to the list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(CUSTOMER)) {
                        customers.add(customer);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public void saveCustomers() {

        try {
            // create an XMLOutputFactory
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            // create XMLEventWriter
            XMLEventWriter eventWriter = outputFactory
                    .createXMLEventWriter(new FileOutputStream(CUSTOMERS_FILE));
            // create an EventFactory
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            // create and write Start Tag
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            StartElement customersStartElement = eventFactory.createStartElement("",
                    "", "customers");
            eventWriter.add(customersStartElement);
            eventWriter.add(end);

            for (Customer customer : customers) {
                saveCustomer(eventWriter, eventFactory, customer);
            }

            eventWriter.add(eventFactory.createEndElement("", "", "customers"));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Problem with Customers file: " + e.getMessage());
            e.printStackTrace();
        } catch (XMLStreamException e) {
            System.out.println("Problem writing customer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveCustomer(XMLEventWriter eventWriter, XMLEventFactory eventFactory, Customer customer)
            throws FileNotFoundException, XMLStreamException {

        XMLEvent end = eventFactory.createDTD("\n");

        // create customer open tag
        StartElement configStartElement = eventFactory.createStartElement("",
                "", CUSTOMER);
        eventWriter.add(configStartElement);
        eventWriter.add(end);
        // Write the different nodes
        createNode(eventWriter, FIRST_NAME, customer.getFirstName());
        createNode(eventWriter, LAST_NAME, customer.getLastName());
        createNode(eventWriter, PHONE_NUMBER, customer.getPhoneNum());
        createNode(eventWriter, EMAIL, customer.getEmail());
        createNode(eventWriter, DATE, customer.getDate());

        eventWriter.add(eventFactory.createEndElement("", "", CUSTOMER));
        eventWriter.add(end);
    }

    private void createNode(XMLEventWriter eventWriter, String name,
                            String value) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

}