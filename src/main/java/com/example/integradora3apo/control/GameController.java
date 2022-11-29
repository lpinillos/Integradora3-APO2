package com.example.integradora3apo.control;

import com.example.integradora3apo.HelloApplication;
import com.example.integradora3apo.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameController implements Initializable {


    //Variables globales de la ventana
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean isRunning = true;


    //Elementos gráficos
    private Avatar avatar;
    private Image fondo;

    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private ArrayList<Wall> walls;


    //Estados de las teclas
    boolean Wpressed = false;
    boolean Apressed = false;
    boolean Spressed = false;
    boolean Dpressed = false;

    public void generateMap() {
        walls = new ArrayList<>();
        for (int i = 350; i >= 50; i += -50) {
            Wall wall1 = new Wall(350, i, canvas);
            walls.add(wall1);
        }

        for (int i = 350; i < 600; i += 50) {
            Wall wall1 = new Wall(i, 100, canvas);
            walls.add(wall1);
        }

        for (int i = 100; i < 400; i += 50) {
            Wall wall1 = new Wall(800, i, canvas);
            walls.add(wall1);
        }

        for (int i = 200; i > 50; i += -50) {
            Wall wall1 = new Wall(i, 400, canvas);
            walls.add(wall1);
        }
        for (int i = 250; i >= 50; i += -50) {
            Wall wall1 = new Wall(150, i, canvas);
            walls.add(wall1);
        }

        for (int i = 200; i <= 400; i += 50) {
            Wall wall1 = new Wall(650, i, canvas);
            walls.add(wall1);
        }

        for (int i = 650; i >= 550; i += -50) {
            Wall wall1 = new Wall(i, 350, canvas);
            walls.add(wall1);
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);
        generateMap();
        String uri = "file:"+ HelloApplication.class.getResource("tilin.gif").getPath();
        fondo = new Image(uri);
        enemies = new ArrayList<>();
        enemies.add(new Enemy(canvas, 300, 100));
        enemies.add(new Enemy(canvas, 300, 300));

        bullets = new ArrayList<>();

        canvas.setOnKeyPressed(this::onKeyPressed);
        canvas.setOnKeyReleased(this::onKeyReleased);

        avatar = new Avatar(canvas);

        draw();
    }

    public void draw() {
        new Thread(
                () -> {
                    while (isRunning) {
                        Platform.runLater(() -> {

                            gc.drawImage(fondo,0,0,canvas.getWidth(), canvas.getHeight());
                            avatar.draw();

                            for (int i = 0; i < walls.size(); i++) {
                                if(walls.get(i).damage == 0){
                                    walls.remove(i);
                                }
                                walls.get(i).draw();
                            }

                            //Pintar enemigos
                            for (int i = 0; i < enemies.size(); i++) {
                                enemies.get(i).draw();
                            }


                            //Balas disparos
                            for (int i = 0; i < bullets.size(); i++) {
                                bullets.get(i).draw();
                                if (bullets.get(i).pos.x > canvas.getWidth() + 20 ||
                                        bullets.get(i).pos.y > canvas.getHeight() + 20 ||
                                        bullets.get(i).pos.y < -20 ||
                                        bullets.get(i).pos.x < -20) {
                                    bullets.remove(i);
                                }

                            }
                            for (int i = 0; i < bullets.size(); i++) {
                                for (int j = 0; j < walls.size(); j++) {
                                    if(bullets.get(i).circle.intersects(walls.get(j).rectangle.getBoundsInParent())){
                                        bullets.remove(i);
                                        walls.get(j).damage--;
                                    }
                                }
                            }

                            //Colisiones
                            detectCollission();

                            doKeyboardActions();

                        });
                        //Sleep
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        ).start();
    }

    private void detectCollission() {
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < bullets.size(); j++) {
                Bullet b = bullets.get(j);
                Enemy e = enemies.get(i);

                double c1 = b.pos.x - e.x;
                double c2 = b.pos.y - e.y;
                double distance = Math.sqrt(Math.pow(c1, 2) + Math.pow(c2, 2));
                if (distance < 12.5) {
                    bullets.remove(j);
                    enemies.remove(i);
                    return;
                }

            }
        }
    }

    private void doKeyboardActions() {
        boolean stopFlag = false;
        if (Wpressed) {
            for (int i = 0; i < walls.size(); i++) {

                if (walls.get(i).rectangle.intersects(avatar.pos.x + avatar.direction.x - 25, avatar.pos.y + avatar.direction.y - 25, 50, 50)) {
                    stopFlag = true;
                }

            }
            if(!stopFlag){
                avatar.moveForward();
            }

        }
        if (Apressed) {
            avatar.changeAngle(-6);
        }
        if (Spressed) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).rectangle.intersects(avatar.pos.x + avatar.direction.x - 25, avatar.pos.y + avatar.direction.y - 25, 50, 50)) {
                   stopFlag = true;
                }

            }
            if(!stopFlag){
                avatar.moveBackward();
            }

        }
        if (Dpressed) {
            avatar.changeAngle(6);
        }
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.W) {
            Wpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.A) {
            Apressed = false;
        }
        if (keyEvent.getCode() == KeyCode.S) {
            Spressed = false;
        }
        if (keyEvent.getCode() == KeyCode.D) {
            Dpressed = false;
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());
        if (keyEvent.getCode() == KeyCode.W) {
            Wpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.A) {
            Apressed = true;
        }
        if (keyEvent.getCode() == KeyCode.S) {
            Spressed = true;
        }
        if (keyEvent.getCode() == KeyCode.D) {
            Dpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.SPACE) {
            Bullet bullet = new Bullet(canvas,
                    new Vector(avatar.pos.x, avatar.pos.y),
                    new Vector(2 * avatar.direction.x, 2 * avatar.direction.y));
            bullets.add(bullet);
        }
    }

}








