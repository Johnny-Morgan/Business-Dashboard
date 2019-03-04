package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import sample.datamodel.Customer;
import sample.datamodel.CustomerData;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    @FXML
    private BorderPane mainPanel;
    @FXML
    private TableView<Customer> customersTable;

    private CustomerData data;

    public void initialize(){
        data = new CustomerData();
        data.loadCustomers();
        customersTable.setItems(data.getCustomers());
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
            CustomerController customerController = fxmlLoader.getController();
            Customer newCustomer = customerController.getNewCustomer();
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

        

    }
}
