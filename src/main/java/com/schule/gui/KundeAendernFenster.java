package com.schule.gui;

import com.schule.data.Kunde;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

import javax.swing.*;
import java.awt.*;

public class KundeAendernFenster extends JFrame {
    private final JTextField vornameText;
    private final JTextField nameText;

    private KundenDatenFenster parent;

    private Kunde kunde;

    public KundeAendernFenster(KundenDatenFenster parent, Kunde kunde) {
        super("Kunden aendern");
        this.parent = parent;
        this.kunde=kunde;
        GridLayout gridLayout = new GridLayout(4, 2);

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());
        JPanel grid = new JPanel();
        grid.setLayout(gridLayout);

        con.add(grid, BorderLayout.CENTER);


        JLabel vorname = new JLabel("Vorname");
        JLabel name = new JLabel("Name");


        vornameText = new JTextField();
        nameText = new JTextField();

        vornameText.setText(kunde.getVorname());
        nameText.setText(kunde.getName());

        grid.add(vorname);
        grid.add(vornameText);
        grid.add(name);
        grid.add(nameText);

        JButton save = new JButton("Update");
        con.add(save, BorderLayout.SOUTH);
        save.addActionListener(e -> updateKunde());
        setVisible(true);
        setSize(600, 300);

    }

    private void updateKunde() {

        String vorname = vornameText.getText();
        String name = nameText.getText();
        if (vorname.trim().isEmpty() && name.trim().isEmpty()) {
            String appendedMessage = "Der Kundenname darf nicht leer sein!";
            JOptionPane.showMessageDialog(this, appendedMessage);
            return;
        }

        kunde.setName(name);
        kunde.setVorname(vorname);

        String url = "http://localhost:8080";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        target.path("kunden").request(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN)
                .put(Entity.entity(kunde, MediaType.APPLICATION_JSON));
        parent.update();
        setVisible(false);

    }
}







