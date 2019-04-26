/*
 * Copyright (C) 2016-2019 Miloš Stojanović
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package ball.game;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import static ball.game.BallGame.removeTile;

public class Ball extends Circle
{
    private Vector position, velocity, acceleration;
    private double rotation_speed, rotation_angle;
    private List<Tile> obstacleList = new ArrayList<>();
    private Platform platform;

    public Ball(Vector start_position, Vector start_velocity, float pp, Color c1, Color c2)
    {
        super(pp);

        Stop[] stops = { new Stop(0, c1), new Stop(1, c2) };
        //setFill(new LinearGradient(-pp, 0, pp, 0, false, CycleMethod.NO_CYCLE, stops));
        setFill(new RadialGradient(0, 0, pp/3, pp/3, pp/2*3, false, CycleMethod.NO_CYCLE, stops));

        rotation_speed = (Math.random() + 0.5) * 180.0/Math.PI;
        position = start_position;
        velocity = start_velocity;
        acceleration = new Vector(0, 0);
        setTranslateX(position.x());
        setTranslateY(position.y());
    }

    public double getPozX()
    {
        return position.x();
    }

    public double getPozY()
    {
        return position.y();
    }

    public void setObstacleList(List<Tile> list)
    {
        obstacleList = list;
    }

    public void setPlatform(Platform p)
    {
        platform = p;
    }

    public void accelerate(double v)
    {
        velocity.lengthen(v);
    }

    public void reflectRight(double zidX)
    {
        position.x(2*(zidX + getRadius()) - position.x());
        velocity.x(-velocity.x());
    }

    public void reflectLeft(double zidX)
    {
        position.x(2*(zidX - getRadius()) - position.x());
        velocity.x(-velocity.x());
    }

    public void reflectDown(double zidY)
    {
        position.y(2*(zidY + getRadius()) - position.y());
        velocity.y(-velocity.y());
    }

    public void reflectUp(double zidY)
    {
        position.y(2*(zidY - getRadius()) - position.y());
        velocity.y(-velocity.y());
    }

    public void move(float elapsed_time, double g, Rectangle boundary)
    {
        rotation_angle += rotation_speed * elapsed_time;
        acceleration.y(g);
        velocity.add(acceleration.product(elapsed_time));
        position.add(velocity.product(elapsed_time));

        if (position.x() - getRadius() < boundary.getX()) {
            reflectRight(boundary.getX());
            velocity.lengthen(1);
        }

        if (position.x() + getRadius() > boundary.getX()+boundary.getWidth()) {
            reflectLeft(boundary.getX() + boundary.getWidth());
            velocity.lengthen(1);
        }

        if (position.y() + getRadius() > boundary.getY() + boundary.getHeight()) {
            reflectUp(boundary.getY() + boundary.getHeight());
            velocity.lengthen(1);
        }

        if (position.y() - getRadius() < boundary.getY()) {
            reflectDown(boundary.getY());
            velocity.lengthen(1);
        }

        if (obstacleList != null) {
            Tile forRemoval;
            for (Tile p: obstacleList) {
                if (p.reflect(this)) {
                    forRemoval = p;
                    obstacleList.remove(forRemoval);
                    //obstacleList.remove(p);
                    removeTile(forRemoval);
                    break;
                    //obstacleList.remove(p);
                }
            }
            //obstacleList.remove(forRemoval);
        }

        if (platform != null)
            platform.reflect(this);

        //if (position.x() == this.getCenterX()) this.setFill(Color.GREEN);

        setTranslateX(position.x());
        setTranslateY(position.y());
        setRotate(rotation_angle);
    }
}
