package com.schule.gui;

import com.schule.data.DateLabelFormatter;
import com.schule.data.Zaehlerdatum;
import com.schule.persistence.JSONPersistance;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.*;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class ZaehlerEingabeFormular extends JFrame {
    private final String[] zaehlerListe = {"Strom", "Gas", "Heizung", "Wasser"};
    private final List<Zaehlerdatum> zaehlerdaten;
    private final JTextField kundenummerText = new JTextField();
    private final JComboBox zaehlerartDrop = new JComboBox(zaehlerListe);
    private final JTextField zaehlernummerText = new JTextField();
    private final JCheckBox eingebautCheck = new JCheckBox();
    private final JTextField zaehlerstandText = new JTextField();
    private final JTextField kommentarText = new JTextField();
    private final JDatePickerImpl datePicker;
    private final JDatePanelImpl datePanel;

    private final JSONPersistance<Zaehlerdatum> persistance = new JSONPersistance<>();

    public ZaehlerEingabeFormular() {
        super("Zählerdaten erfassen");
        GridLayout gridLayout = new GridLayout(7, 2);

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
                        persistance.save(zaehlerdaten);
                        System.exit(0);
                    }
                }
        );

        zaehlerdaten = persistance.load(Zaehlerdatum.class);

        final Container con = getContentPane();

        con.setLayout(new BorderLayout());

        JPanel grid = new JPanel();
        grid.setLayout(gridLayout);

        //Generieren der Labels, Buttons und Textfields
        JLabel kundenummer = new JLabel("Kundennummer (8-stellig)");
        JLabel zaehlerart = new JLabel("Zählerart");
        JLabel zaehlernummer = new JLabel("Zählernummer (8-stellig)");
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
        grid.add(datePicker);
        grid.add(eingebaut);
        grid.add(eingebautCheck);
        grid.add(zaehlerstand);
        grid.add(zaehlerstandText);
        grid.add(kommentar);
        grid.add(kommentarText);
        con.add(speichernBtn, BorderLayout.SOUTH);

        speichernBtn.addActionListener(e -> saveZaehler());
        speichernBtn.addActionListener(e -> machePlausabilitaetspruefung());
        setSize(600, 300);
        setVisible(true);
    }

    private void machePlausabilitaetspruefung() {
        if (countDigits(Integer.parseInt(kundenummerText.getText())) != 8) {
            JOptionPane.showMessageDialog(this, "Kundennummer zu lang oder zu kurz");
        }
        if (zaehlernummerText.getText().length() != 8) {
            JOptionPane.showMessageDialog(this, "Zählernummer zu lang oder zu kurz");
        }
        if (Integer.parseInt(zaehlerstandText.getText()) > 1000000) {
            int dialog = JOptionPane.showConfirmDialog(this, "Der Wert für den Zählerstand ist sehr hoch. Ist das gewollt?");
            if (dialog == JOptionPane.NO_OPTION) {
                zaehlerstandText.setText("Neu eingeben");
            }
        }
        if (eingebautCheck.isSelected() && Integer.parseInt(zaehlerstandText.getText()) > 1000) {
            int dialog = JOptionPane.showConfirmDialog(this, "Der Wert für den Zählerstand ist für einen neu eingebauten Zähler sehr hoch. Ist das gewollt?");
            if (dialog == JOptionPane.NO_OPTION) {
                zaehlerstandText.setText("Neu eingeben");
            }
        }
    }

    private void saveZaehler() {
        int kundennummer = 0;
        String zaehlerart;
        String zaehlernummer = "";
        Date datum = Now();
        boolean eingebaut;
        int zaehlerstand = 0;
        String kommentar = "";

        // Kommentar
        try {
            kundennummer = Integer.parseInt(kundenummerText.getText());
        } catch (Exception e) {
            showErrorWindow(
                    "Die Kundennummer ist nicht korrekt erfasst. \n" +
                            "Im Feld 'Kundennummer' dürfen nur ganze Zahlen stehen"
            );
        }

        // zaehlerart
        zaehlerart = String.valueOf(zaehlerartDrop.getSelectedItem());

        // Zaehlernummer
        try {
            zaehlernummer = zaehlernummerText.getText();
            if (zaehlernummer.equals("")) {
                showErrorWindow(
                        "Die Zählernummer ist nicht korrekt erfasst. \n" +
                                "Im Feld 'Zählernummer stehen keine Werte."
                );
            }
        } catch (Exception e) {
            showErrorWindow("Fehler bei der Zählernummer.");
        }

        // datum
        try {
            datum = (Date) datePicker.getModel().getValue();
        } catch (Exception e) {
            showErrorWindow(
                    "Das Datum ist nicht korrekt erfasst. \n" +
                            "Im Feld 'Datum' darf nur ein Datum im Format TT.MM.JJJJ hh:mm stehen"
            );
        }

        // eingebaut
        eingebaut = eingebautCheck.isSelected();

        // zaehlerstand
        try {
            zaehlerstand = Integer.parseInt(zaehlerstandText.getText());
        } catch (Exception e) {
            showErrorWindow(
                    "Der Zaehlerstand ist nicht korrekt erfasst. \n" +
                            "Im Feld 'Zaehlerstand' dürfen nur ganze Zahlen eingegeben werden."
            );
        }

        // kommentar
        try {
            kommentar = kommentarText.getText();
        } catch (Exception e) {
            showErrorWindow("Im Feld Kommentar dürfen nur Zahlen stehen");
        }

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

    public static Date Now() {
        return Calendar.getInstance().getTime();
    }

  /* Not needed anymore
  public static String Now(String format) {
    return DateToString(Now(), format);
  }

  public static String DateToString(Date date, String format) {
    return new SimpleDateFormat(format).format(date);
  }*/

    private void showErrorWindow(String message) {
        String appendedMessage =
                "Eine Speicherung des Datensatzes ist nicht erfolgt. \n" + message;
        JOptionPane.showMessageDialog(this, appendedMessage);
    }

    private int countDigits(int integer) {
        int counter = 0;
        while (integer != 0) {
            integer = integer / 10;
            counter++;
        }
        return counter;
    }
}
