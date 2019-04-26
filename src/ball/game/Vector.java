/*
 * Copyright (C) 2016-2019 Miloš Stojanović
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package ball.game;

public class Vector
{
    private double x, y; // vector projection onto the axes

    public Vector(double _x, double _y)
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

    public Vector add(Vector v)
    {
        x += v.x;
        y += v.y;
        return this;
    }

    public Vector sum(Vector v)
    {
        return new Vector(x+v.x, y+v.y);
    }

    public Vector multiply(double t)
    {
        x *= t;
        y *= t;
        return this;
    }

    public Vector product(double t)
    {
        return new Vector(x*t, y*t);
    }

    public Vector lengthen(double r)
    {
        double r0 = Math.sqrt(x*x + y*y);
        double ratio = (r + r0)/r0;

        return multiply(ratio);
    }
}
