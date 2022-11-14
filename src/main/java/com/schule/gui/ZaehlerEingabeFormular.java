package com.schule.gui;

import com.schule.data.Zaehlerdatum;
import com.schule.persistence.JSONPersistance;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;

public class ZaehlerEingabeFormular extends JFrame {
  private final String zaehlerListe[] = { "Strom", "Gas", "Heizung", "Wasser" };
  private ArrayList<Zaehlerdatum> zaehlerdaten = new ArrayList<>();

  private JTextField kundenummerText = new JTextField();
  private JComboBox zaehlerartDrop = new JComboBox(zaehlerListe);
  private JTextField zaehlernummerText = new JTextField();
  private JTextField datumText = new JTextField();
  private JCheckBox eingebautCheck = new JCheckBox();
  private JTextField zaehlerstandText = new JTextField();
  private JTextField kommentarText = new JTextField();

  public ZaehlerEingabeFormular() {
    super("Zählerdaten erfassen");
    GridLayout gridLayout = new GridLayout(7, 2);

    addWindowListener(
      new WindowAdapter() {

        @Override
        public void windowClosing(final WindowEvent e) {
          new JSONPersistance<Zaehlerdatum>().save(zaehlerdaten);
        }
      }
    );

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
    int kundennummer = Integer.parseInt(kundenummerText.getText());
    String zaehlerart = String.valueOf(zaehlerartDrop.getSelectedItem());
    String zaehlernummer = zaehlernummerText.getText();
    String datum = datumText.getText();
    boolean eingebaut = eingebautCheck.isSelected();
    int zaehlerstand = Integer.parseInt(zaehlerstandText.getText());
    String kommentar = kommentarText.getText();

    zaehlerdaten.add(
      new Zaehlerdatum(
        kundennummer,
        zaehlerart,
        zaehlernummer,
        datum,
        eingebaut,
        zaehlerstand,
        kommentar
      )
    );
  }
}
