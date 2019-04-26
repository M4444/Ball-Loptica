/*
 * Copyright (C) 2016-2019 Miloš Stojanović
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package loptica.igra;

import javafx.scene.paint.Color;

public class Plocica extends PravougaonaPrepreka {
    private static final double VIS_PLOC = 20;
    private static final double SIR_PLOC = 35;

    public Plocica(int MpozX, int MpozY)
    {
        //super(pozX, pozY, SIR_PLOC, VIS_PLOC);
        super(MpozX*SIR_PLOC, MpozY*VIS_PLOC, SIR_PLOC, VIS_PLOC);
        this.setFill(new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1));
    }
}
