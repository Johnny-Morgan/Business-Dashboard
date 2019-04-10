package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.datamodel.Customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


/**
 * Created by Johnny on 28/02/2019
 */
public class CustomerDialogController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker dateField;


    public Customer getNewCustomer() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNum = phoneNumField.getText();
        String email = emailField.getText();
        String date;
        if(dateField.getValue() != null)
            date = dateField.getValue().toString();
        else {
            date = "Invalid Date";
        }
        return new Customer(firstName, lastName, phoneNum, email, date);
    }

    public void editCustomer(Customer customer) {
        firstNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        phoneNumField.setText(customer.getPhoneNum());
        emailField.setText(customer.getEmail());
        dateField.setValue(convertDate(customer.getDate()));
    }

    public void updateCustomer(Customer customer) {
        customer.setFirstName(firstNameField.getText());
        customer.setLastName(lastNameField.getText());
        customer.setPhoneNum(phoneNumField.getText());
        customer.setEmail(emailField.getText());
        customer.setDate(dateField.getValue().toString());
    }

    // Method to convert String date to LocalDate
    public LocalDate convertDate(String stringDate) {
        if(stringDate.equals("Invalid Date"))
            return LocalDate.now();
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            return LocalDate.parse(stringDate, formatter);
        }
    }
}
