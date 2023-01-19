package com.schule.gui;

import com.schule.data.DateLabelFormatter;
import com.schule.data.Kunde;
import com.schule.data.Ablesung;
import com.schule.model.ZaehlerDatenModel;
import com.schule.services.PlausibilitaetsPruefung;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private final JDatePanelImpl datePanel;
    private final String url = "http://localhost:8080";
    private final Client client = ClientBuilder.newClient();
    private final WebTarget target = client.target(url);
    private List<Kunde> kundenListe = getKundenListe();

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
                        System.exit(0);
                    }
                }
        );
        final Container con = getContentPane();
        con.setLayout(new BorderLayout());
        JPanel grid = new JPanel();
        grid.setLayout(gridLayout);

        //Generieren der Labels, Buttons und Textfields
        JLabel kundeLabel = new JLabel("Kunde");
        JLabel zaehlernummer = new JLabel("Zählernummer (8-stellig)");
        zaehlernummerText.setToolTipText("Hier eine 8-stellige Kombination aus Zahlen und Buchstaben einfügen.");
        JLabel datum = new JLabel("Datum");
        JLabel eingebaut = new JLabel("neu eingebaut");
        JLabel zaehlerstand = new JLabel("Zählerstand");
        JLabel kommentar = new JLabel("Kommentar");

        JButton speichernBtn = new JButton("Speichern");
        JButton anzeigenBtn = new JButton("Daten anzeigen");

        JComboBox<Kunde> kundeDropdown = new JComboBox<>();
        kundeDropdown.setModel(new DefaultComboBoxModel<>(kundenListe.toArray(new Kunde[0])));
        kundeDropdown.setSelectedIndex(-1);

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
        con.add(anzeigenBtn, BorderLayout.EAST);

        anzeigenBtn.addActionListener(e -> datenFensteranzeigen());
        speichernBtn.addActionListener(e -> saveZaehler());
        setSize(600, 300);
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
        kundenListe = kundenResponse.readEntity(new GenericType<>() {});
        return kundenListe;
    }

    private void showGandalf() {
        ImageIcon imageIcon = new ImageIcon("Resources/Gandalf.jpg");
        JOptionPane.showMessageDialog(null,
                "",
                "YOU SHALL NOT PASS!", JOptionPane.INFORMATION_MESSAGE,
                imageIcon);
    }

    private void datenFensteranzeigen() {
        Response re = target.path("ablesungen/vorZweiJahrenHeute")
                .request().accept(MediaType.APPLICATION_JSON).get();
        List<Ablesung> ablesungen = re.readEntity(new GenericType<List<Ablesung>>() {
        });
        new DatenFenster(ablesungen);
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
                kundenListe.get(0),
                kommentar,
                eingebaut,
                zaehlerstand
        );
        String s = PlausibilitaetsPruefung.machePlausabilitaetspruefung(zaehlernummer,
                zaehlerstandText.getText(), eingebaut, datum);
        boolean exists = false;
        Response re = target.path("ablesungen/vorZweiJahrenHeute")
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
        target.path("ablesungen").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(newAblesung, MediaType.APPLICATION_JSON));
        System.out.println(re.getStatus());

    }

    private void showErrorWindow(String message) {
        String appendedMessage =
                "Eine Speicherung des Datensatzes ist nicht erfolgt. \n" + message;
        JOptionPane.showMessageDialog(this, appendedMessage);
    }


}
