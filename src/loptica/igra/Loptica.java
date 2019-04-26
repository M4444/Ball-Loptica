/*
 * Copyright (C) 2016-2019 Miloš Stojanović
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package loptica.igra;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import static loptica.igra.LopticaIgra.izbaciPlocicu;

public class Loptica extends Circle
{
    private Vektor pozicija, brzina, ubrzanje;
    private double brzina_rotacije, ugao_rotacije;
    private List<Plocica> listaPrepreka = new ArrayList<>();
    private Platforma platforma;

    public Loptica(Vektor poc_pozicija, Vektor poc_brzina, float pp, Color b1, Color b2)
    {
        super(pp);

        Stop[] stops = { new Stop(0, b1), new Stop(1, b2) };
        //setFill(new LinearGradient(-pp, 0, pp, 0, false, CycleMethod.NO_CYCLE, stops));
        setFill(new RadialGradient(0, 0, pp/3, pp/3, pp/2*3, false, CycleMethod.NO_CYCLE, stops));

        brzina_rotacije = (Math.random() + 0.5) * 180.0/Math.PI;
        pozicija = poc_pozicija;
        brzina = poc_brzina;
        ubrzanje = new Vektor(0, 0);
        setTranslateX(pozicija.x());
        setTranslateY(pozicija.y());
    }

    public double getPozX()
    {
        return pozicija.x();
    }

    public double getPozY()
    {
        return pozicija.y();
    }

    public void setObstacleList(List<Plocica> list)
    {
        listaPrepreka = list;
    }

    public void setPlatform(Platforma p)
    {
        platforma = p;
    }

    public void ubrzaj(double v)
    {
        brzina.produzi(v);
    }

    public void odbijKaDesno(double zidX)
    {
        pozicija.x(2*(zidX + getRadius()) - pozicija.x());
        brzina.x(-brzina.x());
    }

    public void odbijKaLevo(double zidX)
    {
        pozicija.x(2*(zidX - getRadius()) - pozicija.x());
        brzina.x(-brzina.x());
    }

    public void odbijKaDole(double zidY)
    {
        pozicija.y(2*(zidY + getRadius()) - pozicija.y());
        brzina.y(-brzina.y());
    }

    public void odbijKaGore(double zidY)
    {
        pozicija.y(2*(zidY - getRadius()) - pozicija.y());
        brzina.y(-brzina.y());
    }

    public void pomeri(float proteklo_vreme, double g, Rectangle granica)
    {
        ugao_rotacije += brzina_rotacije * proteklo_vreme;
        ubrzanje.y(g);
        brzina.dodaj(ubrzanje.proizvod(proteklo_vreme));
        pozicija.dodaj(brzina.proizvod(proteklo_vreme));

        if (pozicija.x() - getRadius() < granica.getX()) {
            odbijKaDesno(granica.getX());
            brzina.produzi(1);
        }

        if (pozicija.x() + getRadius() > granica.getX()+granica.getWidth()) {
            odbijKaLevo(granica.getX() + granica.getWidth());
            brzina.produzi(1);
        }

        if (pozicija.y() + getRadius() > granica.getY() + granica.getHeight()) {
            odbijKaGore(granica.getY() + granica.getHeight());
            brzina.produzi(1);
        }

        if (pozicija.y() - getRadius() < granica.getY()) {
            odbijKaDole(granica.getY());
            brzina.produzi(1);
        }

        if (listaPrepreka != null) {
            Plocica zaIzbacivaje;
            for (Plocica p: listaPrepreka) {
                if (p.odbij(this)) {
                    zaIzbacivaje = p;
                    listaPrepreka.remove(zaIzbacivaje);
                    //listaPrepreka.remove(p);
                    izbaciPlocicu(zaIzbacivaje);
                    break;
                    //listaPrepreka.remove(p);
                }
            }
            //listaPrepreka.remove(zaIzbacivaje);
        }

        if (platforma != null)
            platforma.odbij(this);

        //if (pozicija.x() == this.getCenterX()) this.setFill(Color.GREEN);

        setTranslateX(pozicija.x());
        setTranslateY(pozicija.y());
        setRotate(ugao_rotacije);
    }
}
