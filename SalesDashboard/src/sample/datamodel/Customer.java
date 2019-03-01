package sample.datamodel;

import javafx.beans.property.SimpleStringProperty;


/**
 * Created by Johnny on 27/02/2019
 */
public class Customer {

    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty email;
    private SimpleStringProperty phoneNum;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String email, String phoneNum) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.phoneNum = new SimpleStringProperty(phoneNum);

    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPhoneNum() {
        return phoneNum.get();
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum.set(phoneNum);
    }


}
