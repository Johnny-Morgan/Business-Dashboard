package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;
import sample.datamodel.Expense;
import sample.datamodel.ExpenseData;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpenseController {

    @FXML
    private AnchorPane mainPanel;

    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Expense> expensesTable;
    @FXML
    private ContextMenu listContextMenu;
    private ExpenseData data;

    private final ObservableList<PieChart.Data> details = FXCollections.observableArrayList();
    private PieChart pieChart;
    private BorderPane root;

    public void initialize(){
        data = new ExpenseData();
        data.loadExpenses();
        expensesTable.setItems(data.getExpenses());
        editButton.setDisable(true);
        deleteButton.setDisable(true);

        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");

        deleteMenuItem.setOnAction((ActionEvent event) -> deleteExpense());
        editMenuItem.setOnAction((ActionEvent event) -> showEditExpenseDialog());

        listContextMenu.getItems().addAll(deleteMenuItem, editMenuItem);
        expensesTable.setContextMenu(listContextMenu);
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

            boolean check = false;
            while (!check) {
                check = validateFields(newExpense) && validateDate(newExpense) && validateAmount(newExpense);
                if (check) {
                    data.addExpense(newExpense);
                    data.saveExpenses();
                    break;
                } else {
                    expenseDialogController = fxmlLoader.getController();
                    expenseDialogController.editExpense(newExpense);

                    result = dialog.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        expenseDialogController.updateExpense(newExpense);
                        data.saveExpenses();
                    } else return;
                }
            }
        }
    }

    @FXML
    public void showEditExpenseDialog() {
        Expense selectedExpense = expensesTable.getSelectionModel().getSelectedItem();
        if (selectedExpense == null) {
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
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ExpenseDialogController expenseDialogController = fxmlLoader.getController();
        expenseDialogController.editExpense(selectedExpense);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            expenseDialogController.updateExpense(selectedExpense);
            boolean check = false;
            while (!check) {
                check = validateFields(selectedExpense) && validateDate(selectedExpense) && validateAmount(selectedExpense);
                if (check) {
                    data.saveExpenses();
                    break;
                } else {
                    expenseDialogController = fxmlLoader.getController();
                    expenseDialogController.editExpense(selectedExpense);

                    result = dialog.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        expenseDialogController.updateExpense(selectedExpense);
                        data.saveExpenses();
                    } else return;
                }
            }
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
    public void showPieChart() {
        Stage pieStage = new Stage();

        double advertisingExpenses = ExpenseData.getPieDataAdvertising();
        double motorExpenses = ExpenseData.getPieDataMotor();
        double phoneExpenses = ExpenseData.getPieDataPhone();
        double heatExpenses = ExpenseData.getPieDataHeat();
        double travelExpenses = ExpenseData.getPieDataTravel();
        double postageExpenses = ExpenseData.getPieDataPostage();
        double bankExpenses = ExpenseData.getPieDataBank();
        double miscExpenses = ExpenseData.getPieDataMisc();

        details.addAll(new javafx.scene.chart.PieChart.Data("Advertising", advertisingExpenses),
                new javafx.scene.chart.PieChart.Data("Motor", motorExpenses),
                new javafx.scene.chart.PieChart.Data("Stationary & Postage", postageExpenses),
                new javafx.scene.chart.PieChart.Data("Phone & Broadband", phoneExpenses),
                new javafx.scene.chart.PieChart.Data("Heat & Light", heatExpenses),
                new javafx.scene.chart.PieChart.Data("Travel & Subsidence", travelExpenses),
                new javafx.scene.chart.PieChart.Data("Banking", bankExpenses),
                new javafx.scene.chart.PieChart.Data("Miscellaneous", miscExpenses));

        pieChart = new javafx.scene.chart.PieChart();
        pieChart.setData(details);
        pieChart.setTitle("Business Expenses");
        pieChart.setLegendSide(Side.BOTTOM);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(90);

        root = new BorderPane();
        root.setCenter(pieChart);
        pieStage.setScene(new Scene(root, 600, 500));
        pieStage.setTitle("Business Dashboard");
        pieStage.show();

        pieStage.setOnCloseRequest((WindowEvent we) -> details.clear());
    }

    // check to see if fields are empty
    public boolean validateFields(Expense expense) {

        if (expense.getDescription().trim().isEmpty() || expense.getCategory().trim().isEmpty() ||
                expense.getAmount().trim().isEmpty() || expense.getDate().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid info");
            alert.setHeaderText(null);
            alert.setContentText("Invalid info.\nAll fields must be filled.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public boolean validateDate(Expense expense) {
        if ((!expense.getDate().equals(null))) {
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

    public boolean validateAmount(Expense expense) {
        Pattern p = Pattern.compile("[0-9.]+");
        Matcher m = p.matcher(expense.getAmount());
        if (m.find() && m.group().equals(expense.getAmount())) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Info");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Expense Amount\nA number should be added e.g. 99.99");
            alert.showAndWait();
            return false;
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