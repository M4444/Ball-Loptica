/*
 * Copyright (C) 2016-2019 Miloš Stojanović
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package ball.game;

import javafx.scene.shape.Rectangle;

public class RectangularObstacle extends Rectangle implements Obstacle {
    // acceleration factors
    protected double accelerateDown = 0;
    protected double accelerateUp = 0;
    protected double accelerateLeft = 0;
    protected double accelerateRight = 0;

    public RectangularObstacle(double width, double height)
    {
        super(width, height);
    }

    public RectangularObstacle(double x, double y, double width, double height)
    {
        super(x, y, width, height);
    }

    @Override
    public boolean reflect(Ball b) {
        double thisX = this.getX() + this.getTranslateX();
        double thisY = this.getY() + this.getTranslateY();

        if ((b.getPozX() > thisX) &&
            (b.getPozX() < thisX + this.getWidth())) { // X midpoint
            if (b.getPozY() + b.getRadius() < thisY ||
                b.getPozY() - b.getRadius() > thisY+this.getHeight()) {
                return false; // outside the rectangle
            } else {
                if (b.getPozY() < thisY+this.getHeight()/2) {
                    b.reflectUp(thisY);
                    b.accelerate(accelerateUp);
                    return true;
                } else {
                    b.reflectDown(thisY+this.getHeight());
                    b.accelerate(accelerateDown);
                    return true;
                }
            }
        }

        if (b.getPozY() > thisY &&
            b.getPozY() < thisY + this.getHeight()) { // Y midpoint
            if (b.getPozX() + b.getRadius() < thisX ||
                b.getPozX() - b.getRadius() > thisX+this.getWidth()) {
                return false; // outside the rectangle
            } else {
                if (b.getPozX() < thisX+this.getWidth()/2) {
                    b.reflectLeft(thisX);
                    b.accelerate(accelerateLeft);
                    return true;
                } else {
                    b.reflectRight(thisX+this.getWidth());
                    b.accelerate(accelerateRight);
                    return true;
                }
            }
        }

        return false;
    }
}
