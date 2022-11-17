package com.schule.gui;

import com.schule.data.DateLabelFormatter;
import com.schule.data.Zaehlerdatum;
import com.schule.model.ZaehlerDatenModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        JLabel kundenummer = new JLabel("Kundennummer");
        JLabel zaehlerart = new JLabel("Zählerart");
        JLabel zaehlernummer = new JLabel("Zählernummer");
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
        speichernBtn.addActionListener(e -> {
            try {
                saveZaehler();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        });
        setSize(600, 300);
        setVisible(true);
    }

    private void datenFensteranzeigen(List<Zaehlerdatum> zaehlerdaten) {
        new DatenFenster(zaehlerdaten);
    }

    private void saveZaehler() throws ParseException {
        int kundennummer = 0;
        String zaehlerart;
        String zaehlernummer = "";
        Date datum = Now();
        boolean eingebaut;
        int zaehlerstand = 0;
        String kommentar = "";
        String s = "";
        // Kommentar
        try {
            kundennummer = Integer.parseInt(kundenummerText.getText());
        } catch (Exception e) {
            s += "Im Feld 'Kundennummer' dürfen nur ganze Zahlen stehen\n";
        }

        // zaehlerart
        zaehlerart = String.valueOf(zaehlerartDrop.getSelectedItem());

        // Zaehlernummer
        try {
            zaehlernummer = zaehlernummerText.getText();
        } catch (Exception e) {
            s += "Im Feld 'Zählernummer stehen keine Werte.\n";
        }

        // datum
        try {
            datum = (Date) datePicker.getModel().getValue();
        } catch (Exception e) {
            s+="Im Feld 'Datum' darf nur ein Datum im Format TT.MM.JJJJ hh:mm stehen";
        }

        // eingebaut
        eingebaut = eingebautCheck.isSelected();

        // zaehlerstand
        try {
            zaehlerstand = Integer.parseInt(zaehlerstandText.getText());
        } catch (Exception e) {
            s+="Im Feld 'Zaehlerstand' dürfen nur ganze Zahlen eingegeben werden.\n";
       }

        // kommentar
        try {
            kommentar = kommentarText.getText();
        } catch (Exception e) {
            s+= "Im Feld Kommentar dürfen nur Zahlen stehen";
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
         s += machePlausabilitaetspruefung();

        boolean exists = false;
        for (Zaehlerdatum curr : zaehlerdaten) {
            if (newZaehlerdatum.equals(curr)) {
                exists = true;
            }
        }
        if (exists) {
            showErrorWindow("Dieser Eintrag exsistiert bereits!");
        } else if(!s.equals("")){
            showErrorWindow(s);
        } else{
            zaehlerdaten.add(newZaehlerdatum);
        }
    }

    public static Date Now() {
        return Calendar.getInstance().getTime();
    }


    private void showErrorWindow(String message) {
        String appendedMessage =
                "Eine Speicherung des Datensatzes ist nicht erfolgt. \n" + message;
        JOptionPane.showMessageDialog(this, appendedMessage);
    }

    private String machePlausabilitaetspruefung() throws ParseException {
        String s ="";
        try{
            Integer.parseInt(kundenummerText.getText());
            if (kundenummerText.getText().length() != 8) {
                s +="Kundennummer zu lang oder zu kurz \n";
            }
        }catch(NumberFormatException e){
            s+="Kundennummer muss eine ganze Zahl sein \n";
        }
        if (zaehlernummerText.getText().length() != 8) {
            s += "Zählernummer zu lang oder zu kurz\n";
        }
        if (!checkIfStringIsASCII(zaehlernummerText.getText())) {
            s += "Die Zählernummer enthält nicht ASCII Zeichen.\n";
        }
        try{
        if (Integer.parseInt(zaehlerstandText.getText()) > 1000000) {
            s +="Der Wert für den Zählerstand ist sehr hoch. Ist das gewollt?\n";
            if (eingebautCheck.isSelected() && Integer.parseInt(zaehlerstandText.getText()) > 1000) {
                s +="Der Wert für den Zählerstand ist für einen neu eingebauten Zähler sehr hoch. Ist das gewollt?\n";
            }

        }
        }catch(NumberFormatException e){
            s+="Zählerstand muss eine ganze Zahl sein \n";
        }
        if (checkIfDateIsInvalid()) {
            s +="Das Datum ist entweder in der Zukunft oder älter als 2 Woche\n";
        }
        return s;
    }

    private boolean checkIfStringIsASCII(String stringToCheck) {
        String upperString = stringToCheck.toUpperCase();
        for (int pos = 0; pos < upperString.length(); pos++) {
            int ascii = upperString.charAt(pos);
            if ((ascii < 48 || ascii > 57) && (ascii < 65 || ascii > 90)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfDateIsInvalid() throws ParseException {
        boolean isInvalidDate = false;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        String enteredDay = datePicker.getModel().getDay() + "";
        String enteredMonth = datePicker.getModel().getMonth() + 1 + "/";
        String enteredYear = datePicker.getModel().getYear() + "/";

        String enteredDateStringAsString = enteredYear + enteredMonth + enteredDay;
        Date enteredDate = sdf.parse(enteredDateStringAsString);

        String tooEarlyString = subtractWeeksFromDate(2);
        Date tooEarlyDate = sdf.parse(tooEarlyString);

        if (enteredDate.after(Now()) || enteredDate.before(tooEarlyDate)) {
            isInvalidDate = true;
        }
        return isInvalidDate;
    }

    private String subtractWeeksFromDate(int weeks) {
        String wantedDate;
        Instant instant = Instant.now();
        ZoneId zone = ZoneId.of("Europe/Paris");
        ZonedDateTime zdtNow =instant.atZone(zone);

        ZonedDateTime zdtWeeksAgo = zdtNow.minusWeeks(weeks);

        String year = String.valueOf(zdtWeeksAgo.getYear());
        String month = String.valueOf(zdtWeeksAgo.getMonth().ordinal()+1);
        String day = String.valueOf(zdtWeeksAgo.getDayOfMonth());

        wantedDate = year + "/" + month + "/" + day;

        return wantedDate;
    }
}
