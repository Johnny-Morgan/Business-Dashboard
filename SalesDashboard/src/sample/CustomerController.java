package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import sample.datamodel.Customer;
import sample.datamodel.CustomerData;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerController {

    @FXML
    private AnchorPane mainPanel;
    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private CustomerData data;

    public void initialize() {
        data = new CustomerData();
        data.loadCustomers();
        customersTable.setItems(data.getCustomers());
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void enableButtons() {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            editButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }

    @FXML
    public void showAddCustomerDialog() {
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Add New Customer");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("customerdialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            CustomerDialogController customerDialogController = fxmlLoader.getController();
            Customer newCustomer = customerDialogController.getNewCustomer();

            boolean check = validateFields(newCustomer) && validateFirstName(newCustomer) &&
                    validateLastName(newCustomer) && validatePhoneNum(newCustomer) && validateEmail(newCustomer);
            if (check) {
                data.addCustomer(newCustomer);
                data.saveCustomers();
            } else {
                showAddCustomerDialog();
            }
        }
    }


    @FXML
    public void showEditCustomerDialog() {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Customer Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the customer you want to edit");
            alert.showAndWait();
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Edit Contact");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("customerdialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        CustomerDialogController customerDialogController = fxmlLoader.getController();
        customerDialogController.editCustomer(selectedCustomer);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            customerDialogController.updateCustomer(selectedCustomer);
            data.saveCustomers();
        }
    }

    public void deleteCustomer() {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Customer Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the customer you want to delete");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Customer");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete customer: " +
                selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            data.deleteCustomer(selectedCustomer);
            data.saveCustomers();
        }
    }

    // method to delete customer from table using Delete button
    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Customer");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete customer: " +
                        selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    data.deleteCustomer(selectedCustomer);
                    data.saveCustomers();
                }
            }
        }
    }

    // check to see if fields are empty
    public boolean validateFields(Customer customer) {

        if (customer.getFirstName().trim().isEmpty() ||
                customer.getLastName().trim().isEmpty() || customer.getPhoneNum().trim().isEmpty() ||
                customer.getEmail().trim().isEmpty() || customer.getDate().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid info");
            alert.setHeaderText(null);
            alert.setContentText("Invalid info. Please try again");
            alert.showAndWait();
            return false;
        }
        return true;

    }

    public boolean validateFirstName(Customer customer) {
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(customer.getFirstName());
        if (m.find() && m.group().equals(customer.getFirstName())) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Info");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid First Name");
            alert.showAndWait();
            return false;
        }
    }

    public boolean validateLastName(Customer customer) {
        Pattern p = Pattern.compile("[a-zA-Z']+");
        Matcher m = p.matcher(customer.getLastName());
        if (m.find() && m.group().equals(customer.getLastName())) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Info");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Last Name");
            alert.showAndWait();
            return false;
        }
    }

    public boolean validateEmail(Customer customer) {
        Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher m = p.matcher(customer.getEmail());
        if (m.find() && m.group().equals(customer.getEmail())) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Info");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Email Address");
            alert.showAndWait();
            return false;
        }
    }

    public boolean validatePhoneNum(Customer customer) {
        Pattern p = Pattern.compile("^\\+?[\\d ]+");
        Matcher m = p.matcher(customer.getPhoneNum());
        if (m.find() && m.group().equals(customer.getPhoneNum())) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Info");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Phone Number");
            alert.showAndWait();
            return false;
        }
    }

}
