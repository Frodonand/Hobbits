package com.schule.gui;

import com.schule.data.Zaehlerdatum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.util.List;

public class DatenFenster extends JFrame{

    private JTextArea datenanzeigeFeld = new JTextArea();
    private JScrollBar sp = new JScrollBar(Adjustable.VERTICAL);
    public DatenFenster(List<Zaehlerdatum> list){

    addWindowListener(new WindowAdapter() {
    });

    final Container con = getContentPane();
    con.setLayout(new GridLayout());

    datenanzeigeFeld.add(sp);

    for (Zaehlerdatum daten:list) {
        datenanzeigeFeld.setText(String.valueOf(daten));
    }

    setSize(600, 300);
    setVisible(true);

    }

}
