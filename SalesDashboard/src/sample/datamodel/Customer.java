package sample.datamodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


/**
 * Created by Johnny on 27/02/2019
 */
public class Customer {

    private SimpleStringProperty firstName = new SimpleStringProperty("");
    private SimpleStringProperty lastName = new SimpleStringProperty("");
    private SimpleStringProperty phoneNum = new SimpleStringProperty("");
    private SimpleStringProperty email = new SimpleStringProperty("");
    private SimpleStringProperty date = new SimpleStringProperty("");


    public Customer() {
    }

    public Customer(String firstName, String lastName, String phoneNum, String email, String date) {

        if(firstName != null && !firstName.trim().equals(""))
            this.firstName.set(firstName);
        else
            this.firstName.set("Unknown");

        if(lastName != null && !lastName.trim().equals(""))
            this.lastName.set(lastName);
        else
            this.lastName.set("Unknown");

        if(phoneNum != null && !phoneNum.trim().equals(""))
            this.phoneNum.set(phoneNum);
        else
            this.phoneNum.set("Unknown");

        if(email != null && !email.trim().equals(""))
            this.email.set(email);
        else
            this.email.set("Unknown");

        if(date != null && !date.trim().equals(""))
            this.date.set(date);
        else
            this.date.set("2019/01/01");

//        this.firstName.set(firstName);
//        this.lastName.set(lastName);
//        this.phoneNum.set(phoneNum);
//        this.email.set(email);
//        this.date.set(date);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if(firstName != null && !firstName.trim().equals(""))
            this.firstName.set(firstName);
        else
            this.firstName.set("Unknown");
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getPhoneNum() {
        return phoneNum.get();
    }

    public SimpleStringProperty phoneNumProperty() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum.set(phoneNum);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }



}
