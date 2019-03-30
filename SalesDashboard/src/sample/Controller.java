package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import sample.datamodel.Expense;
import sample.datamodel.ExpenseData;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    @FXML
    private BorderPane mainPanel;

    @FXML
    private TableView<Expense> expensesTable;
    private ExpenseData data;

    public void initialize(){
        data = new ExpenseData();
        data.loadExpenses();
        expensesTable.setItems(data.getExpenses());
    }

    @FXML
    public void showAddExpenseDialog(){
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Add New Expense");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("expensedialog.fxml"));
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
            ExpenseController expenseController = fxmlLoader.getController();
            Expense newExpense = expenseController.getNewExpense();
            data.addExpense(newExpense);
            data.saveExpenses();
        }
    }

    @FXML
    public void showEditExpenseDialog(){
        Expense selectedExpense = expensesTable.getSelectionModel().getSelectedItem();
        if(selectedExpense == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Expense Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the expense you want to edit");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Edit Expense");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("expensedialog.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch(IOException e){
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ExpenseController expenseController = fxmlLoader.getController();
        expenseController.editExpense(selectedExpense);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            expenseController.updateExpense(selectedExpense);
            data.saveExpenses();
        }
    }
}
