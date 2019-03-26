package sample.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Johnny on 25/03/2019
 * COMMENTS ABOUT THE PROGRAM GO HERE
 */
public class ExpenseData {
    private static final String EXPENSES_FILE = "expenses.xml";

    private static final String EXPENSE = "expense";
    private static final String DESCRIPTION = "description";
    private static final String CATEGORY = "category";
    private static final String AMOUNT = "amount";
    private static final String DATE = "date";
    private ObservableList<Expense> expenses;

    public ExpenseData() {
        expenses = FXCollections.observableArrayList();
    }

    public ObservableList<Expense> getExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense){
        expenses.add(expense);
    }

    public void deleteExpense(Expense expense){
        expenses.remove(expense);
    }

    public void loadExpenses() {
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = new FileInputStream(EXPENSES_FILE);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            Expense Expense = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have a Expense item, we create a new Expense
                    if (startElement.getName().getLocalPart().equals(EXPENSE)) {
                        Expense = new Expense();
                        continue;
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(DESCRIPTION)) {
                            event = eventReader.nextEvent();
                            Expense.setDescription(event.asCharacters().getData());
                            continue;
                        }
                    }
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(CATEGORY)) {
                        event = eventReader.nextEvent();
                        Expense.setCategory(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(AMOUNT)) {
                        event = eventReader.nextEvent();
                        Expense.setAmount(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(DATE)) {
                        event = eventReader.nextEvent();
                        Expense.setDate(event.asCharacters().getData());
                        continue;
                    }

                }

                // If we reach the end of a Expense element, we add it to the list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(EXPENSE)) {
                        expenses.add(Expense);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public void saveExpenses() {

        try {
            // create an XMLOutputFactory
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            // create XMLEventWriter
            XMLEventWriter eventWriter = outputFactory
                    .createXMLEventWriter(new FileOutputStream(EXPENSES_FILE));
            // create an EventFactory
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            // create and write Start Tag
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            StartElement ExpensesStartElement = eventFactory.createStartElement("",
                    "", "Expenses");
            eventWriter.add(ExpensesStartElement);
            eventWriter.add(end);

            for (Expense Expense : expenses) {
                saveExpense(eventWriter, eventFactory, Expense);
            }

            eventWriter.add(eventFactory.createEndElement("", "", "Expenses"));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Problem with Expenses file: " + e.getMessage());
            e.printStackTrace();
        } catch (XMLStreamException e) {
            System.out.println("Problem writing Expense: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveExpense(XMLEventWriter eventWriter, XMLEventFactory eventFactory, Expense Expense)
            throws FileNotFoundException, XMLStreamException {

        XMLEvent end = eventFactory.createDTD("\n");

        // create Expense open tag
        StartElement configStartElement = eventFactory.createStartElement("",
                "", EXPENSE);
        eventWriter.add(configStartElement);
        eventWriter.add(end);
        // Write the different nodes
        createNode(eventWriter, DESCRIPTION, Expense.getDescription());
        createNode(eventWriter, CATEGORY, Expense.getCategory());
        createNode(eventWriter, AMOUNT, Expense.getAmount());
        createNode(eventWriter, DATE, Expense.getDate());

        eventWriter.add(eventFactory.createEndElement("", "", EXPENSE));
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
