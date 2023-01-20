package com.schule.gui;

import com.schule.data.DateLabelFormatter;
import com.schule.data.Kunde;
import com.schule.data.Ablesung;
import com.schule.services.PlausibilitaetsPruefung;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.*;

import jakarta.ws.rs.client.Entity;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class ZaehlerEingabeFormular extends JFrame {
    private final JTextField zaehlernummerText = new JTextField();
    private final JCheckBox eingebautCheck = new JCheckBox();
    private final JTextField zaehlerstandText = new JTextField();
    private final JTextField kommentarText = new JTextField();
    private final JDatePickerImpl datePicker;
    private final JDatePickerImpl datePickerVon;
    private final JDatePickerImpl datePickerBis;
    private final JDatePanelImpl datePanel;
    private final String url = "http://localhost:8080";
    private final Client client = ClientBuilder.newClient();
    private final WebTarget target = client.target(url);
    private List<Kunde> kundenListe = getKundenListe();
    private final JDatePanelImpl datePanelVon;
    private final JDatePanelImpl datePanelBis;
    private JComboBox<Kunde> kundeDropdown;
    private JComboBox<Kunde> kundefilterDropdown;

    public ZaehlerEingabeFormular() {
        super("Zählerdaten erfassen");
        GridLayout gridLayout = new GridLayout(6, 2);

    UtilDateModel model = new UtilDateModel();
    Properties p = new Properties();
    p.put("text.today", "Today");
    p.put("text.month", "Month");
    p.put("text.year", "Year");
    datePanel = new JDatePanelImpl(model, p);
    datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
    datePicker.getModel().setSelected(true);

        UtilDateModel modelVon = new UtilDateModel();
        datePanelVon = new JDatePanelImpl(modelVon, p);
        datePickerVon = new JDatePickerImpl(datePanelVon, new DateLabelFormatter());
        datePickerVon.getModel().setSelected(true);

        UtilDateModel modelBis = new UtilDateModel();
        datePanelBis = new JDatePanelImpl(modelBis, p);
        datePickerBis = new JDatePickerImpl(datePanelBis, new DateLabelFormatter());
        datePickerBis.getModel().setSelected(true);

    addWindowListener(
      new WindowAdapter() {

                    @Override
                    public void windowClosing(final WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
        final Container con = getContentPane();
        con.setLayout(new BorderLayout());
        JPanel grid = new JPanel();
        grid.setLayout(gridLayout);
        JPanel gridUnten = new JPanel(new GridLayout(4,3));

        //Generieren der Labels, Buttons und Textfields
        JLabel kundeLabel = new JLabel("Kunde");
        JLabel zaehlernummer = new JLabel("Zählernummer (8-stellig)");
        zaehlernummerText.setToolTipText("Hier eine 8-stellige Kombination aus Zahlen und Buchstaben einfügen.");
        JLabel datum = new JLabel("Datum");
        JLabel eingebaut = new JLabel("neu eingebaut");
        JLabel zaehlerstand = new JLabel("Zählerstand");
        JLabel kommentar = new JLabel("Kommentar");
        JLabel datumFilterLabel = new JLabel("Filter Datum von...bis: ");

        JButton speichernBtn = new JButton("Speichern");
        JButton anzeigenBtn = new JButton("Ablesungen anzeigen");
        JButton kundenAnzeigenBtn = new JButton("Kunden anzeigen");
        JButton ablesungenZweiJahreBtn = new JButton("Ablesungen letzten 2 Jahre");

        JButton gefiltertBtn = new JButton("gefilterte Daten anzeigen");
        JButton kundenBtn = new JButton("Kunde anlegen");


        kundeDropdown = new JComboBox<Kunde>();
        kundeDropdown.setModel(new DefaultComboBoxModel<>(kundenListe.toArray(new Kunde[0])));
        kundeDropdown.setSelectedIndex(-1);

        kundefilterDropdown = new JComboBox<Kunde>();
        kundefilterDropdown.setModel(new DefaultComboBoxModel<>(kundenListe.toArray(new Kunde[0])));
        kundefilterDropdown.setSelectedIndex(-1);

        //Hinzufügen der Components zum Grid
        con.add(grid, BorderLayout.CENTER);
        grid.add(kundeLabel);
        grid.add(kundeDropdown);
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


        GridLayout gridLayoutEast = new GridLayout(3, 1);
        JPanel gridEast = new JPanel();
        gridEast.setLayout(gridLayoutEast);
        gridEast.add(anzeigenBtn);
        gridEast.add(ablesungenZweiJahreBtn);
        gridEast.add(kundenAnzeigenBtn);

        con.add(gridEast, BorderLayout.EAST);
        con.add(gridUnten, BorderLayout.SOUTH);
        gridUnten.add(kundenBtn);
        gridUnten.add(speichernBtn);
        gridUnten.add(new JLabel(""));
        gridUnten.add(new JLabel(""));
        gridUnten.add(new JLabel(""));
        gridUnten.add(new JLabel(""));
        gridUnten.add(datumFilterLabel);
        gridUnten.add(datePickerVon);
        gridUnten.add(datePickerBis);
        gridUnten.add(kundefilterDropdown);
        gridUnten.add(gefiltertBtn);

        ablesungenZweiJahreBtn.addActionListener(e -> kundenDatenFensterZweiJahreAnzeigen());
        kundenAnzeigenBtn.addActionListener(e-> kundenDatenFensteranzeigen());
        anzeigenBtn.addActionListener(e -> datenFensteranzeigen());
        speichernBtn.addActionListener(e -> saveZaehler());
        kundenBtn.addActionListener((e -> kundenFensteranzeigen()));
        gefiltertBtn.addActionListener(e -> datenFenstergefiltertAnzeigen());
        setSize(700, 300);
        setVisible(true);

        kommentarText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (kommentarText.getText().equals("Gandalf")) {
                    showGandalf();
                }
            }
        });

    }

    private List<Kunde> getKundenListe(){
        Response kundenResponse = target.path("kunden").request().accept(MediaType.APPLICATION_JSON).get();
        kundenListe = kundenResponse.readEntity(new GenericType<List<Kunde>>() {});
        return kundenListe;
    }

    private void showGandalf() {
        ImageIcon imageIcon = new ImageIcon("Resources/Gandalf.jpg");
        JOptionPane.showMessageDialog(null,
                "",
                "YOU SHALL NOT PASS!", JOptionPane.INFORMATION_MESSAGE,
                imageIcon);
    }

    private void kundenDatenFensteranzeigen() {
        Builder builder = target.path("kunden")
            .request().accept(MediaType.APPLICATION_JSON);
        new KundenDatenFenster(builder);
    }

    private void kundenDatenFensterZweiJahreAnzeigen() {
        Builder builder = target.path("ablesungen/vorZweiJahrenHeute")
            .request().accept(MediaType.APPLICATION_JSON);
        new DatenFenster(builder);
    }

    private void kundenFensteranzeigen() {
        new KundenFenster(this);
    }
    private void datenFensteranzeigen() {
        Builder builder = target.path("ablesungen/vorZweiJahrenHeute")
            .request().accept(MediaType.APPLICATION_JSON);
        new DatenFenster(builder);
    }

    private void datenFenstergefiltertAnzeigen() {
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Date dateVon = (Date) datePickerVon.getModel().getValue();

        Date dateBis = (Date) datePickerBis.getModel().getValue();

        Kunde filterKunde = (Kunde) kundefilterDropdown.getSelectedItem();

        WebTarget target1 = target.path("ablesungen");
        if(filterKunde != null) {
            String gefiltertId = filterKunde.getId().toString();
            target1 = target1.queryParam("kunde", gefiltertId);
        }
        if(dateVon != null){
            LocalDate datumVon = dateVon.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            String beginnString = datumVon.format(dateFormatter);
            target1 = target1.queryParam("beginn", beginnString);
        }
        if(dateBis != null){
            LocalDate datumBis = dateBis.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            String endeString = datumBis.format(dateFormatter);
            target1 = target1.queryParam("ende", endeString);
        }
        Builder builder = target1.request().accept(MediaType.APPLICATION_JSON);
        new DatenFenster(builder);

    }

    private void saveZaehler() {
        int zaehlerstand = 0;
        String zaehlernummer = zaehlernummerText.getText();
        Date date = (Date) datePicker.getModel().getValue();
        LocalDate datum = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        boolean eingebaut = eingebautCheck.isSelected();
        String kommentar = kommentarText.getText();

        try {
            zaehlerstand = Integer.parseInt(zaehlerstandText.getText());
        } catch (Exception e) {
        }

        Ablesung newAblesung = new Ablesung(
                zaehlernummer,
                datum,
                (Kunde) kundeDropdown.getSelectedItem(),
                kommentar,
                eingebaut,
                zaehlerstand
        );
        String s = PlausibilitaetsPruefung.machePlausabilitaetspruefung(zaehlernummer,
                zaehlerstandText.getText(), eingebaut, datum);
        boolean exists = false;
        // TODO: Dupletten Prüfung funktioniert nicht
        Response re = target.path("ablesungen")
                .request().accept(MediaType.APPLICATION_JSON).get();
        List<Ablesung> zaehlerdaten = re.readEntity(new GenericType<List<Ablesung>>() {
        });
        for (Ablesung curr : zaehlerdaten) {
            if (newAblesung.equals(curr)) {
                exists = true;
            }
        }
        if (exists) {
            showErrorWindow("Dieser Eintrag exsistiert bereits!");
        } else if (!s.equals("")) {
            showErrorWindow(s);
        }
        Response response = target.path("ablesungen").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(newAblesung, MediaType.APPLICATION_JSON));
        if(response.getStatus() != 201){
            showErrorWindow("Bitte geben Sie einen Kunden an.");
        }
    }

    private void showErrorWindow(String message){
        String appendedMessage =
                "Eine Speicherung des Datensatzes ist nicht erfolgt. \n" + message;
        JOptionPane.showMessageDialog(this, appendedMessage);
    }
    public void update(){
        kundenListe = getKundenListe();
        kundeDropdown.setModel(new DefaultComboBoxModel<>(kundenListe.toArray(new Kunde[0])));
        kundeDropdown.setSelectedIndex(kundenListe.size()-1);

    }

}

