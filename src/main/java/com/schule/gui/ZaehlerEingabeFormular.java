package com.schule.gui;

import com.schule.data.Zaehlerdatum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;

public class ZaehlerEingabeFormular extends JFrame {

    private final String[] zaehlerListe = {"Strom", "Gas", "Heizung", "Wasser"};
    private ArrayList<Zaehlerdatum> zaehlerdaten = new ArrayList<>();

    private final JTextField kundenummerText = new JTextField();
    private final JComboBox zaehlerartDrop = new JComboBox(zaehlerListe);
    private final JTextField zaehlernummerText = new JTextField();
    private final JTextField datumText = new JTextField();
    private final JCheckBox eingebautCheck = new JCheckBox();
    private final JTextField zaehlerstandText = new JTextField();
    private final JTextField kommentarText = new JTextField();

    public ZaehlerEingabeFormular() {
        super("Zählerdaten erfassen");

        GridLayout gridLayout = new GridLayout(7, 2);

        addWindowListener(new WindowAdapter() {
        });

        final Container con = getContentPane();

        con.setLayout(new BorderLayout());

        JPanel grid = new JPanel();
        grid.setLayout(gridLayout);


        //Generieren der Labels, Buttons und Textfields
        JLabel kundenummer = new JLabel("Kundennummer");
        JLabel zaehlerart = new JLabel("Zählerart");
        JLabel zaehlernummer = new JLabel("Zählernummer");
        JLabel datum = new JLabel("Datum");
        JLabel eingebaut = new JLabel("neu eingebaut");
        JLabel zaehlerstand = new JLabel("Zählerstand");
        JLabel kommentar = new JLabel("Kommentar");

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
        con.add(speichernBtn, BorderLayout.SOUTH);


        speichernBtn.addActionListener(e -> saveZaehler());
        setSize(600, 300);
        setVisible(true);

    }

    private void saveZaehler() {
        int kundennummer = 0;
        String zaehlerart;
        String zaehlernummer = "";
        String datum = "";
        boolean eingebaut;
        int zaehlerstand = 0;
        String kommentar = "";

        // Kommentar
        try {
            kundennummer = Integer.parseInt(kundenummerText.getText());
        } catch (Exception e) {
            showErrorWindow("Die Kundennummer ist nicht korrekt erfasst. \n" +
                    "Im Feld 'Kundennummer' dürfen nur ganze Zahlen stehen");
        }

        // zaehlerart
        zaehlerart = String.valueOf(zaehlerartDrop.getSelectedItem());

        // Zaehlernummer
        try {
            zaehlernummer = zaehlernummerText.getText();
            if (zaehlernummer.equals("")) {
                showErrorWindow("Die Zählernummer ist nicht korrekt erfasst. \n" +
                        "Im Feld 'Zählernummer stehen keine Werte.");
            }
        } catch (Exception e) {
            showErrorWindow("Fehler bei der Zählernummer.");
        }

        // datum
        try {
            datum = datumText.getText();
        } catch (
                Exception e) {
            showErrorWindow("Das Datum ist nicht korrekt erfasst. \n" +
                    "Im Feld 'Datum' darf nur ein Datum im Format TT.MM.JJJJ hh:mm stehen");
        }

        // eingebaut
        eingebaut = eingebautCheck.isSelected();


        // zaehlerstand
        try {
            zaehlerstand = Integer.parseInt(zaehlerstandText.getText());
        } catch (
                Exception e) {
            showErrorWindow("Der Zaehlerstand ist nicht korrekt erfasst. \n" +
                    "Im Feld 'Zaehlerstand' dürfen nur ganze Zahlen eingegeben werden.");
        }

        // kommentar
        try {
            kommentar = kommentarText.getText();
        } catch (
                Exception e) {
            showErrorWindow("Im Feld Kommentar dürfen nur Zahlen stehen");
        }


        zaehlerdaten.add(new
                Zaehlerdatum(kundennummer, zaehlerart, zaehlernummer, datum, eingebaut, zaehlerstand, kommentar));
    }

    private void showErrorWindow(String message) {
        String appendedMessage = "Eine Speicherung des Datensatzes ist nicht erfolgt. \n" + message;
        JOptionPane.showMessageDialog(this, appendedMessage);
    }

}
