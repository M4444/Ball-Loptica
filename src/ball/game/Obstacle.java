/*
 * Copyright (C) 2016-2019 Miloš Stojanović
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package ball.game;

public interface Obstacle {
    //public boolean collision(Ball b);

    public boolean reflect(Ball b);
}
