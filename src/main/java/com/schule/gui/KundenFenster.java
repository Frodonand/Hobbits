package com.schule.gui;

import com.schule.data.Kunde;
import com.schule.model.ZaehlerDatenModel;
import jakarta.ws.rs.core.Response;
import org.glassfish.hk2.api.PostConstruct;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.http.HttpClient;
import java.util.List;


public class KundenFenster extends JFrame {
    private final JTextField vornameText;
    private final JTextField nameText;

    private JTable datenanzeigeFeld;
    private JScrollPane sp;
    private List<Kunde> dataList;
    private ZaehlerDatenModel persistance;



    public KundenFenster() {
        super("Kunden anlegen");
        GridLayout gridLayout = new GridLayout(4, 2);

        persistance = ZaehlerDatenModel.getInstance();

        sp = new JScrollPane(datenanzeigeFeld);
        final Container con = getContentPane();
        con.setLayout(new BorderLayout());
        JPanel grid = new JPanel();
        grid.setLayout(gridLayout);

        con.add(grid, BorderLayout.CENTER);


        JLabel vorname = new JLabel("Vorname");
        JLabel name = new JLabel("Name");


        vornameText = new JTextField();
        nameText = new JTextField();

        grid.add(vorname);
        grid.add(vornameText);
        grid.add(name);
        grid.add(nameText);

        JButton save = new JButton("Speichern");
        con.add(save, BorderLayout.SOUTH);
        save.addActionListener(e -> saveKunde());
        setVisible(true);
        setSize(600, 300);

    }

    private void saveKunde() {
        String vorname= vornameText.getText();
        String name = nameText.getText();




    }
}






