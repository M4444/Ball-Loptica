/*
 * Copyright (C) 2016-2019 Miloš Stojanović
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package ball.game;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BallGame extends Application {
    private Platform platform;
    private Ball ball;
    private Ball[] balls = new Ball[9];
    private static Group root = new Group();
    private static List<Tile> tiles = new ArrayList<>();
    private final Color[] colors = { Color.YELLOW, Color.WHITE, Color.GRAY, Color.RED,
                                     Color.ORANGE, Color.GREEN, Color.BLUE };
    private final Rectangle boundary = new Rectangle(0, 0, 800, 600);
    private double g = 9.81;
    private final MyTimer timer = new MyTimer();
    private boolean pause;
    private final Text pauseText = new Text(310, 300, "PAUSE");
    private int mouseX, mouseY;
    private static Text scoreText = new Text(10, 25, "Score: 0");
    private static int score = 0;
    private static Text timeText = new Text(550, 25, "Time wasted: 0");
    private static long elapsedTime;
    private static boolean startTimeSet = false;
    private static Text finalTimeText = new Text(680, 50, "12h 48m 14s");
    private static boolean end = false;

    private class MyTimer extends AnimationTimer
    {
        private long previous = 0;
        @Override
        public void handle(long now) // in ns
        {
            if (previous == 0)
                previous = now;
            float elapsed = (now - previous)/1e9f; // converted to s
            if (!pause) {
                moveEverything(elapsed);
                String currentTime = formatTime((now - elapsedTime)/1000000000);
                timeText.setText("Time wasted: " + currentTime);
                if (!end)
                    finalTimeText.setText(currentTime);
            }
            previous = now;
            if (!startTimeSet) {
                elapsedTime = now;
                startTimeSet = true;
            }
        }

        private void moveEverything(float elapsed)
        {
            ball.move(elapsed, 0, boundary);
            for (Ball b : balls)
                b.move(elapsed, g, boundary);
            platform.move(elapsed, boundary);
        }

        private String formatTime(long time)
        {
            long seconds = time % 60;
            long minutes = (time % 3600)/60;
            long hours = time / 3600;
            String timeFormat = "";

            if (hours != 0)
                timeFormat += hours + "h ";
            if (minutes != 0)
                timeFormat += minutes + "m ";
            timeFormat += seconds + "s";

            //return "12h 48m 14s";
            //return hours + "h " + minutes + "m " + seconds + "s";
            return timeFormat;
        }
    }

    @Override
    public void start(Stage primaryStage)
    {
        //Group root = new Group();
        scoreText.setFont(new Font("Comic Sans MS", 20));
        timeText.setFont(new Font("Comic Sans MS", 20));
        pauseText.setFont(new Font("Comic Sans MS", 50));
        pauseText.setFill(new Color(0, 0.75, 1, 1));
        pauseText.setVisible(pause);
        finalTimeText.setFont(new Font("Comic Sans MS", 20));
        finalTimeText.setFill(new Color(1, 0, 0, 1));
        finalTimeText.setVisible(end);

        boundary.setStroke(Color.RED);
        boundary.setFill(new Color(0.95, 0.95, 0.78, 1));
        root.getChildren().add(boundary);

        platform = new Platform(boundary.getWidth()/2, boundary.getHeight() - 50);
        for (int i = 0; i < balls.length; i++) {
            Vector position = new Vector(2*boundary.getWidth()/3 - boundary.getWidth()/3*Math.random(),
                                         2*boundary.getHeight()/3 - boundary.getHeight()/3*Math.random());
            Vector velocity = new Vector(Math.random()*120 - 60, Math.random()*80 - 40);

            int colorIndex1 = (int)(Math.random() * colors.length);
            int colorIndex2 = (int)(Math.random() * colors.length);

            balls[i] = new Ball(position, velocity, (float)(Math.random()*40 + 30),
                    new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 0.25),
                    new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 0.25));
        }
        root.getChildren().addAll(balls);

        Vector position = new Vector(boundary.getWidth()/2, boundary.getHeight()/2);
        Vector velocity = new Vector(120, 120);
        ball = new Ball(position, velocity, 10, Color.RED, Color.RED);
        ball.setPlatform(platform);

        /*tiles.add(new Tile(5, 6));
        tiles.add(new Tile(7, 6));
        tiles.add(new Tile(9, 6));
        tiles.add(new Tile(11, 6));
        tiles.add(new Tile(9, 10));
        tiles.add(new Tile(11, 10));
        tiles.add(new Tile(13, 10));
        tiles.add(new Tile(15, 10));*/
        for (int i = 4; i<12; i++)
            for (int j = 3; j<20; j++)
                tiles.add(new Tile(j, i));
        root.getChildren().addAll(tiles);
        ball.setObstacleList(tiles);

        root.getChildren().add(platform);
        root.getChildren().add(platform.getGroup());
        root.getChildren().add(ball);
        root.getChildren().add(scoreText);
        root.getChildren().add(timeText);
        root.getChildren().add(pauseText);
        root.getChildren().add(finalTimeText);

        Scene scene = new Scene(root, boundary.getWidth(), boundary.getHeight());

        //scene.setOnMousePressed( e -> onMousePressed(e) );
        //scene.setOnMouseReleased(this::onMouseReleased);
        scene.setOnKeyPressed(e -> onKeyPressed(e));
        scene.setOnKeyReleased(e -> onKeyReleased(e));

        primaryStage.setTitle("Ball");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();

        timer.start();
        pause = false;
    }

    private void onMousePressed(MouseEvent e)
    {
        mouseX = (int)e.getSceneX();
        mouseY = (int)e.getSceneY();
    }

    private void onMouseReleased(MouseEvent e)
    {
        int x1 = (int)Math.min(mouseX, e.getSceneX());
        int y1 = (int)Math.min(mouseY, e.getSceneY());
        int x2 = (int)Math.max(mouseX, e.getSceneX());
        int y2 = (int)Math.max(mouseY, e.getSceneY());

        boundary.setX(x1);
        boundary.setY(y1);
        boundary.setWidth(x2 - x1);
        boundary.setHeight(y2 - y1);
    }

    private void onKeyPressed(KeyEvent e)
    {
        switch (e.getCode()) {
            case UP:
                g += 1;
                break;
            case DOWN:
                if (g>1)
                    g -= 1;
                break;
        }

        switch (e.getCode()) {
            case LEFT:
                platform.moveLeft();
                break;
            case RIGHT:
                platform.moveRigth();
                break;
            case P:
                pause = !pause;
                pauseText.setVisible(pause);
                break;
        }
    }

    private void onKeyReleased(KeyEvent e)
    {
        switch (e.getCode()) {
            case LEFT:
                platform.stopLeft();
                break;
            case RIGHT:
                platform.stopRight();
                break;
        }
    }

    public static void removeTile(Tile p)
    {
        tiles.remove(p);
        root.getChildren().remove(p);
        score += 10;
        scoreText.setText("Score: " + score);
        if (tiles.isEmpty()) {
            Text winText = new Text(325, 40, "YOU WIN!");
            winText.setFont(new Font("Comic Sans MS", 30));
            winText.setFill(new Color(0, 1, 0, 1));
            end = true;
            finalTimeText.setVisible(true);
            root.getChildren().add(winText);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
