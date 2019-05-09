package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import sample.datamodel.Customer;
import sample.datamodel.CustomerData;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    @FXML
    private ContextMenu listContextMenu;

    private CustomerData data;

    public void initialize() {
        data = new CustomerData();
        data.loadCustomers();
        customersTable.setItems(data.getCustomers());
        editButton.setDisable(true);
        deleteButton.setDisable(true);

        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");

        deleteMenuItem.setOnAction((ActionEvent event) -> deleteCustomer());
        editMenuItem.setOnAction((ActionEvent event) -> showEditCustomerDialog());

        listContextMenu.getItems().addAll(deleteMenuItem, editMenuItem);
        customersTable.setContextMenu(listContextMenu);
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

            boolean check = false;
            while (!check) {
                check = validateFields(newCustomer) && validateFirstName(newCustomer) &&
                        validateLastName(newCustomer) && validatePhoneNum(newCustomer) && validateEmail(newCustomer)
                        && validateDate(newCustomer);
                if (check) {
                    data.addCustomer(newCustomer);
                    data.saveCustomers();
                    break;
                } else {
                    customerDialogController = fxmlLoader.getController();
                    customerDialogController.editCustomer(newCustomer);

                    result = dialog.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        customerDialogController.updateCustomer(newCustomer);
                        data.saveCustomers();
                    } else return;
                }
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
            boolean check = false;
            while (!check) {
                check = validateFields(selectedCustomer) && validateFirstName(selectedCustomer) &&
                        validateLastName(selectedCustomer) && validatePhoneNum(selectedCustomer) &&
                        validateEmail(selectedCustomer) && validateDate(selectedCustomer);
                if (check) {
                    data.saveCustomers();
                    break;
                } else {
                    customerDialogController = fxmlLoader.getController();
                    customerDialogController.editCustomer(selectedCustomer);

                    result = dialog.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        customerDialogController.updateCustomer(selectedCustomer);
                        data.saveCustomers();
                    } else return;
                }
            }
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
        if (isEmpty(customer.getFirstName(), customer.getLastName(), customer.getPhoneNum(),
                customer.getEmail(), customer.getDate())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid info");
            alert.setHeaderText(null);
            alert.setContentText("Invalid info.\nAll fields must be filled.");
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

    public boolean validateDate(Customer customer) {
        if ((!customer.getDate().equals(null))) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Info");
            alert.setHeaderText(null);
            alert.setContentText("No Date Entered.\nPlease Enter Date.");
            alert.showAndWait();
            return false;
        }
    }

    public boolean isEmpty(String... strArr) {
        for (String str : strArr) {
            if (str.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // method to display customers who have not been contacted in over a month
    public void showCustomers() {
        List<String> customerList = new ArrayList<>();
        for (int i = 0; i < customersTable.getItems().size(); i++) {
            Customer customer = customersTable.getItems().get(i);
            LocalDate date = LocalDate.parse(customer.getDate());
            if (date.plusMonths(1).isBefore(LocalDate.now())) {
                customerList.add(customer.getFirstName() + " " + customer.getLastName());
            }
        }
        String message = "";
        for (String customer : customerList) {
            message += customer + "\n";
        }
        if (message.equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("The following customers need to contacted this week");
            alert.setHeaderText(null);
            alert.setContentText("No customers to be contacted today");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("The following customers need to contacted this week");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    // Close down application
    @FXML
    public void exitApplication() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Business Dashboard");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK))
            Platform.exit();
    }
}
