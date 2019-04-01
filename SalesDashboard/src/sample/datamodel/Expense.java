package sample.datamodel;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Johnny on 25/03/2019
 */

public class Expense {
    private SimpleStringProperty description = new SimpleStringProperty("");
    private SimpleStringProperty category = new SimpleStringProperty("");
    private SimpleStringProperty amount = new SimpleStringProperty("");
    private SimpleStringProperty date = new SimpleStringProperty("");

    public Expense(){

    }
    public Expense(String description, String category, String amount, String date) {
        this.description.set(description);
        this.category.set(category);
        this.amount.set(amount);
        this.date.set(date);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getAmount() {
        return amount.get();
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
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
