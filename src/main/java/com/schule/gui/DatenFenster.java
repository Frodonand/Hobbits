package com.schule.gui;

import com.schule.data.Zaehlerdatum;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DatenFenster extends JFrame{

    private final JList<Zaehlerdatum> datenanzeigeFeld;
    private final JScrollPane sp;


    public DatenFenster(List<Zaehlerdatum> list){

    DefaultListModel<Zaehlerdatum> viewList = new DefaultListModel<Zaehlerdatum>();
    viewList.addAll(list);
    datenanzeigeFeld = new JList<Zaehlerdatum>(viewList);
    datenanzeigeFeld.setCellRenderer(getRenderer());
    sp = new JScrollPane(datenanzeigeFeld);

    final Container con = getContentPane();
    con.setLayout(new GridLayout());
    con.add(sp);

    setSize(600, 300);
    setVisible(true);

    }


    private ListCellRenderer<? super Zaehlerdatum> getRenderer() {
        return new DefaultListCellRenderer() {
          @Override
          public Component getListCellRendererComponent(JList<?> list,
              Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel listCellRendererComponent = (JLabel) super
                .getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);
            listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0,
                0, 1, 0, Color.BLACK));
            return listCellRendererComponent;
          }
        };
      }

}
