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

//        if(phoneNum.equals("2"))
//            phoneNumField.setStyle("-fx-color:red");

//        if(isInt(phoneNumField)){
//            Customer newCustomer = new Customer(firstName, lastName, phoneNum, email);
//            return newCustomer;
//        }else{
//            System.out.println("invalid entry");
//            return null;
//        }
        Customer newCustomer = new Customer(firstName, lastName, phoneNum, email);
        return newCustomer;
        /*

        verify input, check fields aren't empty
        only enable dialog ok button when required fields are filled in by user

         */
    }

    private boolean isInt(TextField input){
        try{
            Integer.parseInt(input.getText());
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    public void editCustomer(Customer customer){
        firstNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        phoneNumField.setText(customer.getPhoneNum());
        emailField.setText(customer.getEmail());
    }

    public void updateCustomer(Customer customer){
        customer.setFirstName(firstNameField.getText());
        customer.setLastName(lastNameField.getText());
        customer.setPhoneNum(phoneNumField.getText());
        customer.setEmail(emailField.getText());
    }
}
