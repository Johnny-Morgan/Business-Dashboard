package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.datamodel.Customer;


/**
 * Created by Johnny on 28/02/2019
 *
 */
public class CustomerController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumField;

    @FXML
    private TextField emailField;

    public Customer getNewCustomer(){
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNum = phoneNumField.getText();
        String email = emailField.getText();

        Customer newCustomer = new Customer(firstName, lastName, phoneNum, email);
        return newCustomer;

        /*

        verify input, check fields aren't empty
        only enable dialog ok button when required fields are filled in by user

         */
    }
}
