package com.schule.gui;

import com.schule.data.DateLabelFormatter;
import com.schule.data.Zaehlerdatum;
import com.schule.persistence.JSONPersistance;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class ZaehlerEingabeFormular extends JFrame {
  private final String zaehlerListe[] = { "Strom", "Gas", "Heizung", "Wasser" };
  private List<Zaehlerdatum> zaehlerdaten;

  private JTextField kundenummerText = new JTextField();
  private JComboBox zaehlerartDrop = new JComboBox(zaehlerListe);
  private JTextField zaehlernummerText = new JTextField();
  private JCheckBox eingebautCheck = new JCheckBox();
  private JTextField zaehlerstandText = new JTextField();
  private JTextField kommentarText = new JTextField();
  private JDatePickerImpl datePicker;
  private JDatePanelImpl datePanel;

  private JSONPersistance<Zaehlerdatum> persistance = new JSONPersistance<Zaehlerdatum>();

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
    grid.add(datePicker);
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

  public static String Now(String format) {
    return DateToString(Now(), format);
  }

  public static String DateToString(Date date, String format) {
    return new SimpleDateFormat(format).format(date);
  }

  private void showErrorWindow(String message) {
    String appendedMessage =
      "Eine Speicherung des Datensatzes ist nicht erfolgt. \n" + message;
    JOptionPane.showMessageDialog(this, appendedMessage);
  }
}
