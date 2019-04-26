/*
 * Copyright (C) 2016-2019 Miloš Stojanović
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package ball.game;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Platform extends RectangularObstacle
{
    private static final double THICKNESS = 15;
    private static final double START_SIZE = 100;
    private static final double MOVEMENT_SPEED = 200;

    private double position; // centar pravouganoika na x osi
    private double size; // mora uvek da bude u granicama ekrana
    private double speed;

    private boolean movLeft;
    private boolean movRight;

    private Group wholePlatform = new Group();

    public Platform(double start_positionX, double start_positionY)
    {
        super(START_SIZE, THICKNESS);
        accelerateDown = 5;
        accelerateLeft = -3;
        accelerateRight = -3;

        position = start_positionX;
        size = START_SIZE;
        speed = 0;
        movLeft = false;
        movRight = false;
        setFill(Color.BLUE);

        Line leftLine = new Line(getX(), getY(), getX(), getY()+getHeight());
        Line rightLine = new Line(getX()+getWidth(), getY(),
                                  getX()+getWidth(), getY()+getHeight());
        Line bottomLine = new Line(getX(), getY()+getHeight(),
                                   getX()+getWidth(), getY()+getHeight());
        leftLine.setStrokeWidth(2);
        leftLine.setStroke(Color.ORANGE);
        rightLine.setStrokeWidth(2);
        rightLine.setStroke(Color.ORANGE);
        bottomLine.setStrokeWidth(2);
        bottomLine.setStroke(Color.LIGHTBLUE);

        wholePlatform.getChildren().addAll(bottomLine, leftLine, rightLine);

        wholePlatform.setTranslateX(position - START_SIZE/2);
        wholePlatform.setTranslateY(start_positionY - THICKNESS/2);
        setTranslateX(position - START_SIZE/2);
        setTranslateY(start_positionY - THICKNESS/2);
    }

    public Group getGroup()
    {
        return wholePlatform;
    }

    /*@Override
    public boolean reflect(Ball b) // speeds up if it hits from underneath
    {
        boolean ret = super.reflect(b);

        double thisX = this.getX() + this.getTranslateX();
        double thisY = this.getY() + this.getTranslateY();
        if ((b.getPozX() > thisX) &&
            (b.getPozX() < thisX+this.getWidth()) &&
            (b.getPozY() >= thisY+this.getHeight()/2)) {
                b.accelerate(100);
        }

        return ret;
    }*/

    public void moveLeft()
    {
        if (speed == 0) {
            speed = -MOVEMENT_SPEED;
            movLeft = true;
        } else {
            speed = -MOVEMENT_SPEED;
        }
    }

    public void moveRigth()
    {
        if (speed == 0) {
            speed = MOVEMENT_SPEED;
            movRight = true;
        } else {
            speed = MOVEMENT_SPEED;
        }
    }

    public void stopLeft()
    {
        if (movLeft) {
            if (speed < 0) { // moving to the left
                speed = 0;
                movLeft = false;
            } else {
                movLeft = false;
                movRight = true;
            }
        } else {
            speed = MOVEMENT_SPEED;
        }
    }

    public void stopRight()
    {
        if (movRight) {
            if (speed > 0) { // moving to the right
                speed = 0;
                movRight = false;
            } else {
                movRight = false;
                movLeft = true;
            }
        } else {
            speed = -MOVEMENT_SPEED;
        }
    }

    public void move(float elapsed_time, Rectangle boundary)
    {
        position += speed*elapsed_time;

        if (position - size/2 < boundary.getX()) {
            position = boundary.getX() + size/2;
        }

        if (position + size/2 > boundary.getX() + boundary.getWidth()) {
            position = boundary.getX() + boundary.getWidth() - size/2;
        }

        wholePlatform.setTranslateX(position - START_SIZE/2);
        setTranslateX(position - START_SIZE/2);
    }
}
