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
    private AvatarTwo avatar2;
    private Image fondo;

    private ArrayList<Enemy> enemies;

    private ArrayList<Avatar> avatars;
    private ArrayList<AvatarTwo> avatarTwos;
    private ArrayList<Bullet> bullets;
    private ArrayList<Wall> walls;


    //Estados de las teclas
    boolean Wpressed = false;
    boolean Apressed = false;
    boolean Spressed = false;
    boolean Dpressed = false;

    boolean UPpressed = false;
    boolean LEFTpressed = false;
    boolean DOWNpressed = false;
    boolean RIGHTpressed = false;

    public void generateMap() {
        walls = new ArrayList<>();

        for (int i = 350; i >= 100; i += -50) {
            Wall wall1 = new Wall(350, i, canvas);
            walls.add(wall1);
        }

        //horizontal
        for (int i = 350; i < 600; i += 50) {
            Wall wall1 = new Wall(i, 100, canvas);
            walls.add(wall1);
        }

        //Pared vertical fondo arriba
        for (int i = 0; i < 250; i += 50) {
            Wall wall1 = new Wall(800, i, canvas);
            walls.add(wall1);
        }

        //Pared vertical fondo medio
        for (int i = 350; i < 400; i += 50) {
            Wall wall1 = new Wall(800, i, canvas);
            walls.add(wall1);
        }

        //Pared vertical fondo abajo
        for (int i = 1000; i <= canvas.getHeight(); i += 50) {
            Wall wall1 = new Wall(550, i, canvas);
            walls.add(wall1);
        }

        //Pared horizontal fondo arriba
        for (int i = 950; i <= 1000; i += 50) {
            Wall wall1 = new Wall(i, 150, canvas);
            walls.add(wall1);
        }

        //Pared horizontal fondo abajo
        for (int i = 1000; i <= canvas.getWidth(); i += 50) {
            Wall wall1 = new Wall(i, 500, canvas);
            walls.add(wall1);
        }

        //Pared horizontal medio abajo
        for (int i = 400; i <= 450; i += 50) {
            Wall wall1 = new Wall(i, 500, canvas);
            walls.add(wall1);
        }

        //Pared vertical medio abajo
        for (int i = 800; i <= 850; i += 50) {
            Wall wall1 = new Wall(450, i, canvas);
            walls.add(wall1);
        }

        for (int i = 200; i > 50; i += -50) {
            Wall wall1 = new Wall(i, 400, canvas);
            walls.add(wall1);
        }

        //modifica primera pared vertical
        for (int i = 0; i < 100; i += 50) {
            Wall wall1 = new Wall(150, i, canvas);
            walls.add(wall1);
        }

        //Modifica primera pared vertical parte2
        for (int i = 200; i <= 250; i += 50) {
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

        for (int i = 700; i < 1000; i += 50) {
            Wall wall1 = new Wall(i, 350, canvas);
            walls.add(wall1);
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);
        generateMap();
        String uri = "file:" + HelloApplication.class.getResource("topDownArenaView.png").getPath();
        fondo = new Image(uri);
        enemies = new ArrayList<>();
        avatars = new ArrayList<>();

        avatars.add(new Avatar(canvas));

        avatarTwos = new ArrayList<>();
        bullets = new ArrayList<>();

        //enemies.add(new Enemy(canvas, 300, 100));
        //enemies.add(new Enemy(canvas, 300, 300));

        canvas.setOnKeyPressed(this::onKeyPressed);
        canvas.setOnKeyReleased(this::onKeyReleased);


        avatar = new Avatar(canvas);
        avatar2 = new AvatarTwo(canvas);

        draw();
    }

    public void draw() {
        new Thread(
                () -> {
                    while (isRunning) {
                        Platform.runLater(() -> {

                            gc.drawImage(fondo, 0, 0, canvas.getWidth(), canvas.getHeight());
                            if(avatars.size() != 0){
                                avatars.get(0).draw();
                            }

                            avatar2.draw();


                            for (int i = 0; i < walls.size(); i++) {
                                if (walls.get(i).damage == 0) {
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
                                    if (bullets.get(i).circle.intersects(walls.get(j).rectangle.getBoundsInParent())) {
                                        bullets.remove(i);
                                        walls.get(j).damage--;
                                    }
                                }
                            }

                            //Colisiones
                            //detectCollission();
                            detectDamage();
                            doKeyboardActions();
                            doKeyboardActions2();
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

    private void detectDamage() {
        for (int i = 0; i < avatars.size(); i++) {
            for (int j = 0; j < bullets.size(); j++) {
                Bullet b = bullets.get(j);
                Avatar a = avatars.get(i);

                double c1 = b.pos.x - a.pos.x;
                double c2 = b.pos.y - a.pos.y;
                double distance = Math.sqrt(Math.pow(c1, 2) + Math.pow(c2, 2));
                if (distance < 12.5) {
                    bullets.remove(j);
                    return;
                }

            }
        }
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

                if (walls.get(i).rectangle.intersects(avatars.get(0).pos.x + avatars.get(0).direction.x - 25, avatars.get(0).pos.y + avatars.get(0).direction.y - 25, 50, 50)) {

                    stopFlag = true;

                }

            }
            if (!stopFlag) {
                avatars.get(0).moveForward();
            }

        }
        if (Apressed) {
            avatars.get(0).changeAngle(-6);
        }
        if (Spressed) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).rectangle.intersects(avatars.get(0).pos.x + avatars.get(0).direction.x - 25, avatars.get(0).pos.y + avatars.get(0).direction.y - 25, 50, 50)) {
                    stopFlag = true;
                }

            }
            if (!stopFlag) {
                avatars.get(0).moveBackward();
            }

        }
        if (Dpressed) {
            avatars.get(0).changeAngle(6);
        }
    }

    private void doKeyboardActions2() {
        boolean stopFlag2 = false;
        if (UPpressed) {
            for (int i = 0; i < walls.size(); i++) {

                if (walls.get(i).rectangle.intersects(avatar2.pos.x + avatar2.direction.x - 25, avatar2.pos.y + avatar2.direction.y - 25, 50, 50)) {
                    stopFlag2 = true;
                }

            }
            if (!stopFlag2) {
                avatar2.moveForward();
            }

        }
        if (LEFTpressed) {
            avatar2.changeAngle(-6);
        }
        if (DOWNpressed) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).rectangle.intersects(avatar2.pos.x + avatar2.direction.x - 25, avatar2.pos.y + avatar2.direction.y - 25, 50, 50)) {
                    stopFlag2 = true;
                }

            }
            if (!stopFlag2) {
                avatar2.moveBackward();
            }

        }
        if (RIGHTpressed) {
            avatar2.changeAngle(6);
        }
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        //Tank 1
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

        //Tank 2
        if (keyEvent.getCode() == KeyCode.UP) {
            UPpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.LEFT) {
            LEFTpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.DOWN) {
            DOWNpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            RIGHTpressed = false;
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        //Tank 1
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
                    new Vector(avatars.get(0).pos.x, avatars.get(0).pos.y),
                    new Vector(2 * avatars.get(0).direction.x, 2 * avatars.get(0).direction.y));
            bullets.add(bullet);
        }

        //Tank 2
        System.out.println(keyEvent.getCode());
        if (keyEvent.getCode() == KeyCode.UP) {
            UPpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.LEFT) {
            LEFTpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.DOWN) {
            DOWNpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            RIGHTpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.CONTROL) {
            Bullet bullet = new Bullet(canvas,
                    new Vector(avatar2.pos.x, avatar2.pos.y),
                    new Vector(2 * avatar2.direction.x, 2 * avatar2.direction.y));
            bullets.add(bullet);
        }
    }
}








