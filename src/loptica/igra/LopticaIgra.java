/*
 * Copyright (C) 2016-2019 Miloš Stojanović
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package loptica.igra;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LopticaIgra extends Application {
    private Platforma platforma;
    private Loptica loptica;
    private Loptica[] loptice = new Loptica[9];
    private static Group root = new Group();
    private static List<Plocica> plocice = new ArrayList<>();
    private final Color[] boje = { Color.YELLOW, Color.WHITE, Color.GRAY, Color.RED,
                                   Color.ORANGE, Color.GREEN, Color.BLUE };
    private final Rectangle granica = new Rectangle(0, 0, 800, 600);
    private double g = 9.81;
    private final MyTimer timer = new MyTimer();
    private boolean pauza;
    private Text pauzaTekst = new Text(310, 300, "PAUSE");
    private int mouseX, mouseY;
    private static Text skorTekst = new Text(10, 25, "Score: 0");
    private static int skor = 0;
    private static Text vremeTekst = new Text(550, 25, "Time wasted: 0");
    private static long pocetnoVreme;
    private static boolean pocetnoVremeSet = false;
    private static Text konacnoVremeTekst = new Text(680, 50, "12h 48m 14s");
    private static boolean kraj = false;

    private class MyTimer extends AnimationTimer
    {
        private long prethodni = 0;
        @Override
        public void handle(long sada) // izrazeno u ns
        {
            if (prethodni == 0)
                prethodni = sada;
            float proteklo = (sada - prethodni)/1e9f; // pretvoreno u s
            if (!pauza) {
                pomeriSve(proteklo);
                String trenutnoVreme = vremeFormat((sada - pocetnoVreme)/1000000000);
                vremeTekst.setText("Time wasted: " + trenutnoVreme );
                if (!kraj)
                    konacnoVremeTekst.setText(trenutnoVreme);
            }
            prethodni = sada;
            if (!pocetnoVremeSet) {
                pocetnoVreme = sada;
                pocetnoVremeSet = true;
            }
        }

        private void pomeriSve(float proteklo)
        {
            loptica.pomeri(proteklo, 0, granica);
            for (Loptica l : loptice)
                l.pomeri(proteklo, g, granica);
            platforma.pomeri(proteklo, granica);
        }

        private String vremeFormat(long vreme)
        {
            long sekunde = vreme % 60;
            long minuti = (vreme % 3600)/60;
            long casovi = vreme / 3600;
            String formatVreme = "";

            if (casovi != 0)
                formatVreme += casovi + "h ";
            if (minuti != 0)
                formatVreme += minuti + "m ";
            formatVreme += sekunde + "s";

            //return "12h 48m 14s";
            //return casovi + "h " + minuti + "m " + sekunde + "s";
            return formatVreme;
        }
    }

    @Override
    public void start(Stage primaryStage)
    {
        //Group root = new Group();
        skorTekst.setFont(new Font("Comic Sans MS", 20));
        vremeTekst.setFont(new Font("Comic Sans MS", 20));
        pauzaTekst.setFont(new Font("Comic Sans MS", 50));
        pauzaTekst.setFill(new Color(0, 0.75, 1, 1));
        pauzaTekst.setVisible(pauza);
        konacnoVremeTekst.setFont(new Font("Comic Sans MS", 20));
        konacnoVremeTekst.setFill(new Color(1, 0, 0, 1));
        konacnoVremeTekst.setVisible(kraj);

        granica.setStroke(Color.RED);
        granica.setFill(new Color(0.95, 0.95, 0.78, 1));
        root.getChildren().add(granica);

        platforma = new Platforma(granica.getWidth()/2, granica.getHeight() - 50);
        for (int i = 0; i < loptice.length; i++) {
            Vektor pozicija = new Vektor(2*granica.getWidth()/3 - granica.getWidth()/3*Math.random(),
                                         2*granica.getHeight()/3 - granica.getHeight()/3*Math.random());
            Vektor brzina = new Vektor(Math.random()*120 - 60, Math.random()*80 - 40);

            int colorIndex1 = (int)(Math.random() * boje.length);
            int colorIndex2 = (int)(Math.random() * boje.length);

            loptice[i] = new Loptica(pozicija, brzina, (float)(Math.random()*40 + 30),
                    new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 0.25),
                    new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 0.25));
        }
        root.getChildren().addAll(loptice);

        Vektor pozicija = new Vektor(granica.getWidth()/2, granica.getHeight()/2);
        Vektor brzina = new Vektor(120, 120);
        loptica = new Loptica(pozicija, brzina, 10, Color.RED, Color.RED);
        loptica.setPlatform(platforma);

        /*plocice.add(new Plocica(5, 6));
        plocice.add(new Plocica(7, 6));
        plocice.add(new Plocica(9, 6));
        plocice.add(new Plocica(11, 6));
        plocice.add(new Plocica(9, 10));
        plocice.add(new Plocica(11, 10));
        plocice.add(new Plocica(13, 10));
        plocice.add(new Plocica(15, 10));*/
        for (int i = 4; i<12; i++)
            for (int j = 3; j<20; j++)
                plocice.add(new Plocica(j, i));
        root.getChildren().addAll(plocice);
        loptica.setObstacleList(plocice);

        root.getChildren().add(platforma);
        root.getChildren().add(platforma.getGroup());
        root.getChildren().add(loptica);
        root.getChildren().add(skorTekst);
        root.getChildren().add(vremeTekst);
        root.getChildren().add(pauzaTekst);
        root.getChildren().add(konacnoVremeTekst);

        Scene scene = new Scene(root, granica.getWidth(), granica.getHeight());

        //scene.setOnMousePressed( e -> onMousePressed(e) );
        //scene.setOnMouseReleased(this::onMouseReleased);
        scene.setOnKeyPressed(e -> onKeyPressed(e));
        scene.setOnKeyReleased(e -> onKeyReleased(e));

        primaryStage.setTitle("Balls");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();

        timer.start();
        pauza = false;
    }

    private void onMousePressed(MouseEvent e)
    {
        mouseX = (int)e.getSceneX();
        mouseY = (int)e.getSceneY();
    }

    private void onMouseReleased(MouseEvent e)
    {
        int x1 = (int)Math.min(mouseX, e.getSceneX());
        int y1 = (int)Math.min(mouseY, e.getSceneY());
        int x2 = (int)Math.max(mouseX, e.getSceneX());
        int y2 = (int)Math.max(mouseY, e.getSceneY());

        granica.setX(x1);
	granica.setY(y1);
        granica.setWidth(x2 - x1);
	granica.setHeight(y2 - y1);
    }

    private void onKeyPressed(KeyEvent e)
    {
        switch(e.getCode()) {
            case UP:
                g += 1;
                break;
            case DOWN:
                if (g>1)
                    g -= 1;
                break;
        }

        switch(e.getCode()) {
            case LEFT:
                platforma.pomerajLevo();
                break;
            case RIGHT:
                platforma.pomerajDesno();
                break;
            case P:
                pauza = !pauza;
                pauzaTekst.setVisible(pauza);
                break;
        }
    }

    private void onKeyReleased(KeyEvent e)
    {
        switch(e.getCode()){
            case LEFT:
                platforma.zaustaviLevo();
                break;
            case RIGHT:
                platforma.zaustaviDesno();
                break;
        }
    }

    public static void izbaciPlocicu(Plocica p)
    {
        plocice.remove(p);
        root.getChildren().remove(p);
        skor += 10;
        skorTekst.setText("Score: " + skor);
        if (plocice.isEmpty()) {
            Text pobeda = new Text(325, 40, "YOU WIN!");
            pobeda.setFont(new Font("Comic Sans MS", 30));
            pobeda.setFill(new Color(0, 1, 0, 1));
            kraj = true;
            konacnoVremeTekst.setVisible(true);
            root.getChildren().add(pobeda);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
