package com.example.integradora3apo.control;

import com.example.integradora3apo.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private Button startGame;

    @FXML
    private TextField textUsuario1;

    @FXML
    private TextField textUsuario2;

    @FXML
    void startGame(ActionEvent event) {

        if (textUsuario1.getText().isEmpty() || textUsuario2.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Debes llenar ambos espacios!");

            alert.showAndWait();
        } else {
            HelloApplication.showWindow("canvasView.fxml");
            Stage currentStage = (Stage) textUsuario1.getScene().getWindow();
            currentStage.hide();
        }

    }



}


