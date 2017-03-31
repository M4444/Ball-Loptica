package loptica.igra;

import javafx.scene.paint.Color;

public class Plocica extends PravougaonaPrepreka {
    private static final double visPloc = 20;
    private static final double sirPloc = 35;
    
    public Plocica(int MpozX, int MpozY)
    {
        //super(pozX, pozY, sirPloc, visPloc);
        super(MpozX*sirPloc, MpozY*visPloc, sirPloc, visPloc);
        this.setFill(new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1));
    }
    
}
