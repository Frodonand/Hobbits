package com.schule.gui;

import com.schule.data.DateLabelFormatter;
import com.schule.data.Zaehlerdatum;
import com.schule.model.ZaehlerDatenModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.sql.DatabaseMetaData;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class ZaehlerAenderungsFormular extends JFrame {
  private final String[] zaehlerListe = { "Strom", "Gas", "Heizung", "Wasser" };
  private final JTextField kundenummerText ;
  private final JComboBox zaehlerartDrop;
  private final JTextField zaehlernummerText;
  private final JCheckBox eingebautCheck;
  private final JTextField zaehlerstandText;
  private final JTextField kommentarText;
  private final JDatePickerImpl datePicker;
  private final JDatePanelImpl datePanel;

  private final ZaehlerDatenModel datenModel;
  private final Zaehlerdatum data;
  private final DatenFenster parent;

  public ZaehlerAenderungsFormular(Zaehlerdatum data,DatenFenster parent) {
    super("Zählerdaten erfassen");
    GridLayout gridLayout = new GridLayout(7, 2);

    this.parent = parent;
    datenModel = ZaehlerDatenModel.getInstance();
    this.data = data;

    UtilDateModel model = new UtilDateModel();
    Properties p = new Properties();
    p.put("text.today", "Today");
    p.put("text.month", "Month");
    p.put("text.year", "Year");
    datePanel = new JDatePanelImpl(model, p);
    datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
    datePicker.getModel().setSelected(true);
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


     kundenummerText = new JTextField(String.valueOf(data.getKundennummer()));
     zaehlerartDrop = new JComboBox(zaehlerListe);
     zaehlerartDrop.setSelectedItem(data.getZaehlerart());
     zaehlernummerText = new JTextField(data.getZaehlernummer());
     eingebautCheck = new JCheckBox();
     eingebautCheck.setSelected(data.isEingebaut());
     zaehlerstandText = new JTextField(String.valueOf(data.getZaehlerstand()));
     kommentarText = new JTextField(data.getKommentar());

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
        con.add(speichernBtn,BorderLayout.SOUTH);

    speichernBtn.addActionListener(e -> saveZaehler());// ToDo: ändern
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
    Zaehlerdatum newZaehlerdatum = new Zaehlerdatum(
      kundennummer,
      zaehlerart,
      zaehlernummer,
      datum,
      eingebaut,
      zaehlerstand,
      kommentar
    );
    int index = datenModel.getData().indexOf(data);

    datenModel.updateEntry(index ,newZaehlerdatum);
    parent.update();
    setVisible(false);

  }


  public static Date Now() {
    return Calendar.getInstance().getTime();
  }

  private void showErrorWindow(String message) {
    String appendedMessage =
      "Eine Speicherung des Datensatzes ist nicht erfolgt. \n" + message;
    JOptionPane.showMessageDialog(this, appendedMessage);
  }
}
