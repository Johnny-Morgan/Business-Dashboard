package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sample.datamodel.TodoData;

import java.io.IOException;

public class Main extends Application {
    private Stage window;
    private Scene login, main;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        primaryStage.setTitle("User Log-In");

        Label lblUserName = new Label("User Name");
        Label lblPassword = new Label("Password");

        TextField txtUName = new TextField();
        PasswordField passwordField = new PasswordField();

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> System.exit(0));

        Button btnOK = new Button("OK");
        btnOK.setOnAction(e -> {
            if(validateLogIn(txtUName.getText(),passwordField.getText()))
                window.setScene(main);
        });

        HBox hbox = new HBox(8);
        hbox.getChildren().addAll(btnCancel, btnOK);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(lblUserName, 0, 0, 1, 1);
        gridPane.add(txtUName, 1, 0, 1, 1);
        gridPane.add(lblPassword, 0, 1, 1, 1);
        gridPane.add(passwordField, 1, 1, 1, 1);
        gridPane.add(hbox, 1, 3, 1, 1);

        login = new Scene(gridPane, 300, 130);
        primaryStage.setScene(login);
        primaryStage.show();

        primaryStage.setResizable(false);

        main = new Scene(FXMLLoader.load(getClass().getResource("main.fxml")), 1000, 550);
        primaryStage.setTitle("Business Dashboard");

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        try {
            TodoData.getInstance().storeTodoItems();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void init() throws Exception {
        try {
            TodoData.getInstance().loadTodoItems();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static boolean validateLogIn(String uNameEntered, String passwordEntered) {

        String[][] validLogins = new String[][]{{"John Morgan", "testTest"}, {"t", "t"}};

        int x = 0;
        int y = 1;
        boolean validLogIn = false;

        outer:
        for (String[] oneDArray : validLogins) {
            if ((uNameEntered.equals(oneDArray[x])) && (passwordEntered.equals(oneDArray[y]))) {
                validLogIn = true;
                break outer;
            }
        }

        if (validLogIn) {
            Alert a = new Alert(AlertType.INFORMATION);
            a.setContentText("Log-In Successful");
            a.setHeaderText(null);
            a.show();
            return true;
        } else {
            Alert a = new Alert(AlertType.WARNING);
            a.setContentText("Log-In Failed. \nPlease Try Again.");
            a.setHeaderText(null);
            a.show();
            return false;
        }
    }
}
