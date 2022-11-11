package com.schule.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

public class Gui extends JFrame {

    private final String zaehlerListe[] = {"Strom","Gas","Heizung","Wasser"};
    public Gui(){
        super("Zählerdaten erfassen");

        GridLayout gridLayout = new GridLayout(7,2);

        addWindowListener(new WindowAdapter() {
        });

        final Container con = getContentPane();

        con.setLayout(new BorderLayout());

        JPanel grid = new JPanel();
        grid.setLayout(gridLayout);


        //Generieren der Labels, Buttons und Textfields
        JLabel kundenummer = new JLabel("Kundennummer");
        JLabel zaehlerart = new JLabel("Zählerart");
        JLabel zaehlernummer = new JLabel ("Zählernummer");
        JLabel datum = new JLabel ("Datum");
        JLabel eingebaut = new JLabel("neu eingebaut");
        JLabel zaehlerstand = new JLabel("Zählerstand");
        JLabel kommentar = new JLabel("Kommentar");

        JTextField kundenummerText = new JTextField();
        JComboBox zaehlerartDrop = new JComboBox(zaehlerListe);
        JTextField zaehlernummerText = new JTextField ();
        JTextField datumText = new JTextField ();
        JCheckBox eingebautCheck = new JCheckBox();
        JTextField zaehlerstandText = new JTextField();
        JTextField kommentarText = new JTextField();

        JButton speichernBtn = new JButton("Speichern");

        //Hinzufügen der Components zum Grid
        con.add(grid, BorderLayout.CENTER);
        grid.add(kundenummer);
        grid.add(kundenummerText);
        grid.add(zaehlerart);
        grid.add(zaehlerartDrop);
        grid.add(zaehlernummer);
        grid.add(zaehlernummerText);
        grid.add(datum);
        grid.add(datumText);
        grid.add(eingebaut);
        grid.add(eingebautCheck);
        grid.add(zaehlerstand);
        grid.add(zaehlerstandText);
        grid.add(kommentar);
        grid.add(kommentarText);
        con.add(speichernBtn,BorderLayout.SOUTH);

        speichernBtn.addActionListener(e -> {

        });
        setSize(600, 300);
        setVisible(true);

    }
    public static void main(final String[] args) {
        new Gui();
    }


}
