package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.datamodel.Expense;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    public void editExpense(Expense expense){
        descriptionField.setText(expense.getDescription());
        categoryField.setValue(expense.getCategory());
        amountField.setText(expense.getAmount());
        dateField.setValue(convertDate(expense.getDate()));
    }

    public void updateExpense(Expense expense){
        expense.setDescription(descriptionField.getText());
        expense.setCategory(categoryField.getValue().toString());
        expense.setAmount(amountField.getText());
        expense.setDate(dateField.getValue().toString());
    }

    // Method to convert String date to LocalDate
    public LocalDate convertDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        return LocalDate.parse(stringDate, formatter);
    }





}
