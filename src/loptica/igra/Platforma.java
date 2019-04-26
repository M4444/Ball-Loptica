/*
 * Copyright (C) 2016-2019 Miloš Stojanović
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package loptica.igra;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Platforma extends PravougaonaPrepreka
{
    private static final double DEBLJINA = 15;
    private static final double POC_VELICINA = 100;
    private static final double BRZINA_KRETANJA = 200;

    private double pozicija; // centar pravouganoika na x osi
    private double velicina; // mora uvek da bude u granicama ekrana
    private double brzina;

    private boolean pomLevo;
    private boolean pomDesno;

    private Group celaPlatforma = new Group();

    public Platforma(double poc_pozicijaX, double poc_pozicijaY)
    {
        super(POC_VELICINA, DEBLJINA);
        ubrzajDole = 5;
        ubrzajLevo = -3;
        ubrzajDesno = -3;

        pozicija = poc_pozicijaX;
        velicina = POC_VELICINA;
        brzina = 0;
        pomLevo = false;
        pomDesno = false;
        setFill(Color.BLUE);

        Line levaLinija = new Line(getX(), getY(), getX(), getY()+getHeight());
        Line desnaLinija = new Line(getX()+getWidth(), getY(),
                                    getX()+getWidth(), getY()+getHeight());
        Line donjaLinija = new Line(getX(), getY()+getHeight(),
                                    getX()+getWidth(), getY()+getHeight());
        levaLinija.setStrokeWidth(2);
        levaLinija.setStroke(Color.ORANGE);
        desnaLinija.setStrokeWidth(2);
        desnaLinija.setStroke(Color.ORANGE);
        donjaLinija.setStrokeWidth(2);
        donjaLinija.setStroke(Color.LIGHTBLUE);

        celaPlatforma.getChildren().addAll(donjaLinija, levaLinija, desnaLinija);

        celaPlatforma.setTranslateX(pozicija - POC_VELICINA/2);
        celaPlatforma.setTranslateY(poc_pozicijaY - DEBLJINA/2);
        setTranslateX(pozicija - POC_VELICINA/2);
        setTranslateY(poc_pozicijaY - DEBLJINA/2);
    }

    public Group getGroup()
    {
        return celaPlatforma;
    }

    /*@Override
    public boolean odbij(Loptica l) // ubrzava ako udari odozdo
    {
        boolean ret = super.odbij(l);

        double thisX = this.getX() + this.getTranslateX();
        double thisY = this.getY() + this.getTranslateY();
        if ((l.getPozX() > thisX) &&
            (l.getPozX() < thisX+this.getWidth()) &&
            (l.getPozY() >= thisY+this.getHeight()/2)) &&
            !(l.getPozY() + l.getRadius() < thisY ||
              l.getPozY() - l.getRadius() > thisY + this.getHeight()) {
                l.ubrzaj(100);
        }

        return ret;
    }*/

    public void pomerajLevo()
    {
        if (brzina == 0) {
            brzina = -BRZINA_KRETANJA;
            pomLevo = true;
        } else {
            brzina = -BRZINA_KRETANJA;
        }
    }

    public void pomerajDesno()
    {
        if (brzina == 0) {
            brzina = BRZINA_KRETANJA;
            pomDesno = true;
        } else {
            brzina = BRZINA_KRETANJA;
        }
    }

    public void zaustaviLevo()
    {
        if (pomLevo) {
            if (brzina < 0) { // krece se levo
                brzina = 0;
                pomLevo = false;
            } else {
                pomLevo = false;
                pomDesno = true;
            }
        } else {
            brzina = BRZINA_KRETANJA;
        }
    }

    public void zaustaviDesno()
    {
        if (pomDesno) {
            if (brzina > 0) { // krece se desno
                brzina = 0;
                pomDesno = false;
            } else {
                pomDesno = false;
                pomLevo = true;
            }
        } else {
            brzina = -BRZINA_KRETANJA;
        }
    }

    public void pomeri(float proteklo_vreme, Rectangle granica)
    {
        pozicija += brzina*proteklo_vreme;

        if (pozicija - velicina/2 < granica.getX()) {
            pozicija = granica.getX() + velicina/2;
        }

        if (pozicija + velicina/2 > granica.getX() + granica.getWidth()) {
            pozicija = granica.getX() + granica.getWidth() - velicina/2;
        }

        celaPlatforma.setTranslateX(pozicija - POC_VELICINA/2);
        setTranslateX(pozicija - POC_VELICINA/2);
    }
}
