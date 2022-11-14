package com.schule.gui;

import com.schule.data.Zaehlerdatum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.util.List;

public class DatenFenster extends JFrame{

    private JTextArea datenanzeigeFeld = new JTextArea();
    private JScrollPane sp;
    public DatenFenster(List<Zaehlerdatum> list){

    addWindowListener(new WindowAdapter() {
    });

        sp = new JScrollPane(datenanzeigeFeld);

    final Container con = getContentPane();
    con.setLayout(new GridLayout());
    con.add(sp);

    for (Zaehlerdatum daten:list) {
        datenanzeigeFeld.append(String.valueOf(daten) + "\n");
    }

    setSize(600, 300);
    setVisible(true);

    }

}
