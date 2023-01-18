package com.schule.gui;

import com.schule.data.Ablesung;
import com.schule.data.DateLabelFormatter;
import com.schule.data.Kunde;
import com.schule.model.ZaehlerDatenModel;
import com.schule.services.PlausibilitaetsPruefung;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;
import javax.swing.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class ZaehlerAenderungsFormular extends JFrame {
  private final String[] zaehlerListe = { "Strom", "Gas", "Heizung", "Wasser" };
  private final JTextField kundenummerText ;
  private final JComboBox<String> zaehlerartDrop;
  private final JTextField zaehlernummerText;
  private final JCheckBox eingebautCheck;
  private final JTextField zaehlerstandText;
  private final JTextField kommentarText;
  private final JDatePickerImpl datePicker;
  private final JDatePanelImpl datePanel;

  private final ZaehlerDatenModel datenModel;
  private final Ablesung data;
  private final DatenFenster parent;

  public ZaehlerAenderungsFormular(Ablesung data,DatenFenster parent) {
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


     kundenummerText = new JTextField(String.valueOf(data.getKunde().getId()));
     zaehlerartDrop = new JComboBox<String>(zaehlerListe);
     zaehlernummerText = new JTextField(data.getZaehlernummer());
     eingebautCheck = new JCheckBox();
     eingebautCheck.setSelected(data.isNeuEingebaut());
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
    } catch (Exception e) {}
    try {
      zaehlerstand = Integer.parseInt(zaehlerstandText.getText());
  } catch (Exception e) {}

    Ablesung newZaehlerdatum = new Ablesung(
      zaehlernummer,
      datum,
      new Kunde(),
      kommentar,
      eingebaut,
      Integer.valueOf(zaehlerstand)
    );
    int index = datenModel.getData().indexOf(data);
    String s = PlausibilitaetsPruefung.machePlausabilitaetspruefung(kundenummerText.getText(),zaehlernummer,
    zaehlerstandText.getText(),eingebaut,datum);
    if(s.equals("")){
      datenModel.updateEntry(index ,newZaehlerdatum);
      parent.update();
      setVisible(false);
    }else{
      showErrorWindow(s);
    }
  }

  private void showErrorWindow(String message) {
    String appendedMessage =
      "Eine Speicherung des Datensatzes ist nicht erfolgt. \n" + message;
    JOptionPane.showMessageDialog(this, appendedMessage);
  }
}
