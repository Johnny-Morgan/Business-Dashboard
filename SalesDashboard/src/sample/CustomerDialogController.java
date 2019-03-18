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
 *
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
        String date = dateField.getValue().toString();

//        if(!isInt(firstNameField, "Invalid First Name")){
//            Customer newCustomer = new Customer(firstName, lastName, phoneNum, email);
//            return newCustomer;
//        }else {
//            Customer newCustomer = new Customer("INVALID", lastName, phoneNum, email);
//            return newCustomer;
//        }

        if (isInt(phoneNumField, "Invalid Phone Number")) {
            Customer newCustomer = new Customer(firstName, lastName, phoneNum, email, date);
            return newCustomer;
        } else {
            Customer newCustomer = new Customer(firstName, lastName, "Invalid No.", email, date);
            return newCustomer;
        }

        /*

        verify input, check fields aren't empty
        only enable dialog ok button when required fields are filled in by user

         */
    }

    private boolean isInt(TextField input, String message) {
        try {
            Integer.parseInt(input.getText());
            return true;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, message);
            alert.setTitle("Invalid Data");
            alert.showAndWait();
            return false;
        }
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        return LocalDate.parse(stringDate, formatter);
    }

}
