/*
 * Copyright (C) 2016-2019 Miloš Stojanović
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package ball.game;

import javafx.scene.paint.Color;

public class Tile extends RectangularObstacle {
    private static final double TILE_HEIGHT = 20;
    private static final double TILE_WIDTH = 35;

    public Tile(int MposX, int MposY)
    {
        //super(posX, posY, TILE_HEIGHT, TILE_WIDTH);
        super(MposX*TILE_WIDTH, MposY*TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
        this.setFill(new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1));
    }
}
