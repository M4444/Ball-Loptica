/*
 * Copyright (C) 2016-2019 Miloš Stojanović
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package loptica.igra;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PravougaonaPrepreka extends Rectangle implements Prepreka{
    // faktori ubrzanja
    protected double ubrzajDole = 0;
    protected double ubrzajGore = 0;
    protected double ubrzajLevo = 0;
    protected double ubrzajDesno = 0;

    public PravougaonaPrepreka(double width, double hight)
    {
        super(width, hight);
    }

    public PravougaonaPrepreka(double x, double y, double width, double hight)
    {
        super(x, y, width, hight);
    }

    @Override
    public boolean odbij(Loptica l) {
        double thisX = this.getX() + this.getTranslateX();
        double thisY = this.getY() + this.getTranslateY();

        if ((l.getPozX() > thisX) &&
            (l.getPozX() < thisX + this.getWidth())) { // X sredina
            if (l.getPozY() + l.getRadius() < thisY ||
                l.getPozY() - l.getRadius() > thisY+this.getHeight()) {
                return false; // van pravougaonika
            } else {
                if (l.getPozY() < thisY+this.getHeight()/2) {
                    l.odbijKaGore(thisY);
                    l.ubrzaj(ubrzajGore);
                    return true;
                } else {
                    l.odbijKaDole(thisY+this.getHeight());
                    l.ubrzaj(ubrzajDole);
                    return true;
                }
            }
        }

        if (l.getPozY() > thisY &&
            l.getPozY() < thisY + this.getHeight()) { // Y sredina
            if (l.getPozX() + l.getRadius() < thisX ||
                l.getPozX() - l.getRadius() > thisX+this.getWidth()) {
                return false; // van pravougaonika
            } else {
                if (l.getPozX() < thisX+this.getWidth()/2) {
                    l.odbijKaLevo(thisX);
                    l.ubrzaj(ubrzajLevo);
                    return true;
                } else {
                    l.odbijKaDesno(thisX+this.getWidth());
                    l.ubrzaj(ubrzajDesno);
                    return true;
                }
            }
        }

        return false;
    }
}
