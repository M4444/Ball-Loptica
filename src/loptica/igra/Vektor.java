/*
 * Copyright (C) 2016-2019 Miloš Stojanović
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package loptica.igra;

public class Vektor
{
    private double x, y; // projekcije vektora na ose

    public Vektor(double _x, double _y)
    {
        x = _x; y = _y;
    }

    public void x(double nx)
    {
        x = nx;
    }

    public void y(double ny)
    {
        y = ny;
    }

    public double x()
    {
        return x;
    }

    public double y()
    {
        return y;
    }

    public Vektor mnozi(double t)
    {
        x *= t;
        y *= t;
        return this;
    }

    public Vektor proizvod(double t)
    {
        return new Vektor(x*t, y*t);
    }

    public Vektor pomnozi(double t)
    {
        x *= t;
        y *= t;
        return this;
    }

    public Vektor dodaj(Vektor v)
    {
        x += v.x;
        y += v.y;
        return this;
    }

    public Vektor produzi(double r)
    {
        double r0 = Math.sqrt(x*x + y*y);
        double ratio = (r + r0)/r0;

        return pomnozi(ratio);
    }

    public Vektor zbir(Vektor v)
    {
        return new Vektor(x+v.x, y+v.y);
    }
}
