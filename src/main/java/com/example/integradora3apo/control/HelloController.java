package com.example.integradora3apo.control;

import com.example.integradora3apo.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;


public class HelloController implements Initializable {

    @FXML
    private Button startGame;

    @FXML
    private TextField textUsuario1;

    @FXML
    private TextField textUsuario2;

    @FXML
    void startGame(ActionEvent event) {
            HelloApplication.showWindow("canvasView.fxml");
            Stage currentStage = (Stage) startGame.getScene().getWindow();
            currentStage.hide();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File musicPath = new File("C:\\Users\\pante\\OneDrive\\Escritorio\\Integradora3-APO2\\src\\main\\resources\\com\\example\\integradora3apo\\pou.wav");

        if(musicPath.exists()){

            try {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(3);

            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        }else {
            System.out.println("No existe");
        }



    }
}


