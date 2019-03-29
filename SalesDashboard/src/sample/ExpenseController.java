package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.datamodel.Expense;

/**
 * Created by Johnny on 25/03/2019
 * COMMENTS ABOUT THE PROGRAM GO HERE
 */
public class ExpenseController {

    @FXML
    private TextField descriptionField;

    @FXML
    private ComboBox categoryField;

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker dateField;

    public Expense getNewExpense(){
        String description = descriptionField.getText();
        String category = categoryField.getValue().toString();
        String amount = amountField.getText();
        String date = dateField.getValue().toString();

        Expense newExpense = new Expense(description, category, amount, date);
        return newExpense;
    }





}
