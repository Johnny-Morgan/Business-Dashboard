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

    public void initialize(){
        data = new CustomerData();
        data.loadCustomers();
        customersTable.setItems(data.getCustomers());
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void enableButtons(){
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if(selectedCustomer != null){
            editButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }
    @FXML
    public void showAddCustomerDialog(){
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Add New Customer");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("customerdialog.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e){
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            CustomerDialogController customerDialogController = fxmlLoader.getController();
            Customer newCustomer = customerDialogController.getNewCustomer();
            data.addCustomer(newCustomer);
            data.saveCustomers();
        }
    }

    @FXML
    public void showEditCustomerDialog(){
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if(selectedCustomer == null){
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
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch(IOException e){
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        CustomerDialogController customerDialogController = fxmlLoader.getController();
        customerDialogController.editCustomer(selectedCustomer);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            customerDialogController.updateCustomer(selectedCustomer);
            data.saveCustomers();
        }
    }

    public void deleteCustomer(){
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if(selectedCustomer == null){
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
        if(result.isPresent() && result.get() == ButtonType.OK){
            data.deleteCustomer(selectedCustomer);
            data.saveCustomers();
        }
    }

    // method to delete customer from table using Delete button
    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if(selectedCustomer != null){
            if(keyEvent.getCode().equals(KeyCode.DELETE)){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Customer");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete customer: " +
                        selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK) {
                    data.deleteCustomer(selectedCustomer);
                    data.saveCustomers();
                }
            }
        }
    }

}
