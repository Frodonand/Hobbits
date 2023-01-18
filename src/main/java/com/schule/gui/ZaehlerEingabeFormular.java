package com.schule.gui;

import com.schule.data.DateLabelFormatter;
import com.schule.data.Kunde;
import com.schule.data.Ablesung;
import com.schule.model.ZaehlerDatenModel;
import com.schule.services.PlausibilitaetsPruefung;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class ZaehlerEingabeFormular extends JFrame {
    private final String[] zaehlerListe = {"Strom", "Gas", "Heizung", "Wasser"};
    private final List<Ablesung> zaehlerdaten;
    private final JTextField kundenummerText = new JTextField();
    private final JComboBox<String> zaehlerartDrop = new JComboBox<String>(zaehlerListe);
    private final JTextField zaehlernummerText = new JTextField();
    private final JCheckBox eingebautCheck = new JCheckBox();
    private final JTextField zaehlerstandText = new JTextField();
    private final JTextField kommentarText = new JTextField();
    private final JDatePickerImpl datePicker;
    private final JDatePanelImpl datePanel;


  private final ZaehlerDatenModel datenModel;

    public ZaehlerEingabeFormular() {
        super("Zählerdaten erfassen");
        GridLayout gridLayout = new GridLayout(7, 2);

    datenModel = ZaehlerDatenModel.getInstance();

    UtilDateModel model = new UtilDateModel();
    Properties p = new Properties();
    p.put("text.today", "Today");
    p.put("text.month", "Month");
    p.put("text.year", "Year");
    datePanel = new JDatePanelImpl(model, p);
    datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
    datePicker.getModel().setSelected(true);
    addWindowListener(
      new WindowAdapter() {

        @Override
        public void windowClosing(final WindowEvent e) {
          datenModel.save();
          System.exit(0);
        }
      }
    );


                zaehlerdaten = datenModel.getData();

        final Container con = getContentPane();

        con.setLayout(new BorderLayout());

        JPanel grid = new JPanel();
        grid.setLayout(gridLayout);


        //Generieren der Labels, Buttons und Textfields
        JLabel kundenummer = new JLabel("Kundennummer (8-stellig)");
        kundenummerText.setToolTipText("Hier eine 8-stellige Nummer einfügen.");
        JLabel zaehlerart = new JLabel("Zählerart");
        JLabel zaehlernummer = new JLabel("Zählernummer (8-stellig)");
        zaehlernummerText.setToolTipText("Hier eine 8-stellige Kombination aus Zahlen und Buchstaben einfügen.");
        JLabel datum = new JLabel("Datum");
        JLabel eingebaut = new JLabel("neu eingebaut");
        JLabel zaehlerstand = new JLabel("Zählerstand");
        JLabel kommentar = new JLabel("Kommentar");

        JButton speichernBtn = new JButton("Speichern");
        JButton anzeigenBtn = new JButton("Daten anzeigen");


        //Hinzufügen der Components zum Grid
        con.add(grid, BorderLayout.CENTER);
        grid.add(kundenummer);
        grid.add(kundenummerText);
        grid.add(zaehlerart);
        grid.add(zaehlerartDrop);
        grid.add(zaehlernummer);
        grid.add(zaehlernummerText);
        grid.add(datum);
        grid.add(datePicker);
        grid.add(eingebaut);
        grid.add(eingebautCheck);
        grid.add(zaehlerstand);
        grid.add(zaehlerstandText);
        grid.add(kommentar);
        grid.add(kommentarText);
        con.add(speichernBtn, BorderLayout.SOUTH);
        con.add(anzeigenBtn, BorderLayout.EAST);

        anzeigenBtn.addActionListener(e -> datenFensteranzeigen(zaehlerdaten));
        speichernBtn.addActionListener(e -> saveZaehler());
        setSize(600, 300);
        setVisible(true);

        kommentarText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(kommentarText.getText().equals("Gandalf")){
                    showGandalf();
                }
            }
        });

    }

    private void showGandalf() {
        ImageIcon imageIcon = new ImageIcon("Resources/Gandalf.jpg");
        JOptionPane.showMessageDialog(null,
                "",
                "YOU SHALL NOT PASS!", JOptionPane.INFORMATION_MESSAGE,
                imageIcon);
    }

    private void datenFensteranzeigen(List<Ablesung> zaehlerdaten) {
        new DatenFenster(zaehlerdaten);
    }

    private void saveZaehler() {
        int kundennummer = 0;
        int zaehlerstand = 0;
        
        String zaehlerart = String.valueOf(zaehlerartDrop.getSelectedItem());
        String zaehlernummer = zaehlernummerText.getText();
        Date date = (Date) datePicker.getModel().getValue();
        LocalDate datum = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
        boolean eingebaut = eingebautCheck.isSelected();
        String kommentar = kommentarText.getText();


        try {
                    kundennummer = Integer.parseInt(kundenummerText.getText());
                } catch (Exception e) {
                }
                try {
                    zaehlerstand = Integer.parseInt(zaehlerstandText.getText());
                } catch (Exception e) {
                }

                Ablesung newAblesung  = new Ablesung(
                    zaehlernummer,
                    datum,
                    new Kunde(),
                    kommentar,
                    eingebaut,
                    Integer.valueOf(zaehlerstand)
                  );
                String s = PlausibilitaetsPruefung.machePlausabilitaetspruefung(kundenummerText.getText(), zaehlernummer,
                        zaehlerstandText.getText(), eingebaut, datum);
                boolean exists = false;
                for (Ablesung curr : zaehlerdaten) {
                    if (newAblesung.equals(curr)) {
                        exists = true;
                    }
                }
                if (exists) {
                    showErrorWindow("Dieser Eintrag exsistiert bereits!");
                } else if (!s.equals("")) {
                    showErrorWindow(s);
                } else {
                    zaehlerdaten.add(newAblesung);
                }
            }

            private void showErrorWindow(String message) {
                String appendedMessage =
                        "Eine Speicherung des Datensatzes ist nicht erfolgt. \n" + message;
                JOptionPane.showMessageDialog(this, appendedMessage);
            }



}
