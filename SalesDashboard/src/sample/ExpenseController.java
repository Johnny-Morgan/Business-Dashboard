package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.datamodel.Customer;
import sample.datamodel.Expense;
import sample.datamodel.ExpenseData;

import java.io.IOException;
import java.util.Optional;

public class ExpenseController {

    @FXML
    private AnchorPane mainPanel;

    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Expense> expensesTable;
    private ExpenseData data;

    private final ObservableList<PieChart.Data> details = FXCollections.observableArrayList();
    private javafx.scene.chart.PieChart pieChart;
    private BorderPane root;

    public void initialize(){
        data = new ExpenseData();
        data.loadExpenses();
        expensesTable.setItems(data.getExpenses());
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void enableButtons() {
        Expense selectedExpense = expensesTable.getSelectionModel().getSelectedItem();
        if (selectedExpense != null) {
            editButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }

    @FXML
    public void showAddExpenseDialog(){
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Add New Expense");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("expenseDialog.fxml"));
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
            ExpenseDialogController expenseDialogController = fxmlLoader.getController();
            Expense newExpense = expenseDialogController.getNewExpense();
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
            alert.setContentText("Please select the expense you want to edit.");
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

        ExpenseDialogController expenseDialogController = fxmlLoader.getController();
        expenseDialogController.editExpense(selectedExpense);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            expenseDialogController.updateExpense(selectedExpense);
            data.saveExpenses();
        }
    }

    public void deleteExpense(){
        Expense selectedExpense = expensesTable.getSelectionModel().getSelectedItem();
        if(selectedExpense == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Expense Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the expense you want to delete.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Expense");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete expense: " +
                selectedExpense.getDescription());

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            data.deleteExpense(selectedExpense);
            data.saveExpenses();
        }
    }

    // method to delete expense from table using Delete button
    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        Expense selectedExpense = expensesTable.getSelectionModel().getSelectedItem();
        if (selectedExpense != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Expense");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete expense: " +
                        selectedExpense.getDescription() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    data.deleteExpense(selectedExpense);
                    data.saveExpenses();
                }
            }
        }
    }

    @FXML
    public void showPieChart(){
        Stage pieStage = new Stage();

        // hardcoded expense data
        details.addAll(new javafx.scene.chart.PieChart.Data("Motor", 1440),
                new javafx.scene.chart.PieChart.Data("Advertising", 1200),
                new javafx.scene.chart.PieChart.Data("Stationary & Postage", 461),
                new javafx.scene.chart.PieChart.Data("Telephone & Internet", 720),
                new javafx.scene.chart.PieChart.Data("Heat & Light", 343),
                new javafx.scene.chart.PieChart.Data("Meetings & Events", 755),
                new javafx.scene.chart.PieChart.Data("Bank Charges", 300),
                new javafx.scene.chart.PieChart.Data("Accountancy Fees", 489));

        pieChart = new javafx.scene.chart.PieChart();
        pieChart.setData(details);
        pieChart.setTitle("Business Expenses");
        pieChart.setLegendSide(Side.BOTTOM);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(90);

        root = new BorderPane();
        root.setCenter(pieChart);
        pieStage.setScene(new Scene(root, 600 , 500));
        pieStage.show();
    }
}