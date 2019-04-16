package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.datamodel.TodoData;
import sample.datamodel.TodoItem;

import java.time.LocalDate;

/**
 * Created by Johnny on 12/03/2019
 * COMMENTS ABOUT THE PROGRAM GO HERE
 */
public class TodoDialogController {
    @FXML
    private TextField shortDescriptionField;
    @FXML
    private TextArea detailsArea;
    @FXML
    private DatePicker deadlinePicker;

    public TodoItem processResults(){
        String shortDescription = shortDescriptionField.getText().trim();
        String details = detailsArea.getText().trim();
        LocalDate deadlineValue = deadlinePicker.getValue();

        TodoItem newItem = new TodoItem(shortDescription, details, deadlineValue);

        if(validateFields(newItem)) {
            TodoData.getInstance().addTodoItem(newItem);
            return newItem;
        }else
            return null;
    }

    public void editItem(TodoItem item) {
        shortDescriptionField.setText(item.getShortDescription());
        detailsArea.setText(item.getDetails());
        deadlinePicker.setValue(item.getDeadline());
    }

    public void updateItem(TodoItem item) {
        item.setShortDescription(shortDescriptionField.getText());
        item.setDetails(detailsArea.getText());
        item.setDeadline(deadlinePicker.getValue());
    }

    // check to see if fields are empty
    public boolean validateFields(TodoItem item) {

        if (item.getShortDescription().trim().isEmpty() || item.getDetails().trim().isEmpty() ||
                item.getDeadline() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid info");
            alert.setHeaderText(null);
            alert.setContentText("Invalid info.\nAll fields must be filled.");
            alert.showAndWait();
            return false;
        }
        return true;
    }


}

