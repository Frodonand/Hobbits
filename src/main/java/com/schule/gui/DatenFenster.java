package com.schule.gui;

import com.schule.data.Zaehlerdatum;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Point;
import java.text.DateFormat;
import java.awt.Container;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatenFenster extends JFrame{

    private JTable datenanzeigeFeld;
    private JScrollPane sp;
    private List<Zaehlerdatum> dataList;

    public DatenFenster(List<Zaehlerdatum> dataList){


        this.dataList = dataList;
    datenanzeigeFeld = new JTable() {
        private static final long serialVersionUID = 1L;

        public boolean isCellEditable(int row, int column) {                
                return false;               
        };
    };
    update();

    sp = new JScrollPane(datenanzeigeFeld);

    final Container con = getContentPane();
    con.setLayout(new BorderLayout());
    con.add(sp,BorderLayout.CENTER);

    datenanzeigeFeld.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent mouseEvent) {
            JTable table =(JTable) mouseEvent.getSource();
            Point point = mouseEvent.getPoint();
            int index = table.rowAtPoint(point);
            if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                openEditor(index);
            }
        }
    });
    
    setSize(600, 300);
    setVisible(true);
    }

    protected void openEditor(int index) {
        new ZaehlerAenderungsFormular(dataList.get(index),this);
    }

    public void update(){
        Object[][] allData = new Object[dataList.size()][7];
        Locale locale = new Locale("de", "DE");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
    
        for (int i = 0; i<dataList.size();i++) {
            Zaehlerdatum curr = dataList.get(i);
            Object[] o = new Object[7];
            o[0]= curr.getKundennummer();
            o[1]= curr.getZaehlerart();
            o[2]= curr.getZaehlernummer();
            o[4]= curr.isEingebaut();
            o[5]= curr.getZaehlerstand();
            o[6]= curr.getKommentar();
            Date date = curr.getDatum();
            if(date == null){
                o[3]= null;
            }else{
                o[3]= dateFormat.format(date);
            }
            allData[i]=o;
        }
    
        String[] headers = {
           "Kundennummer",
            "Zählerart",
            "Zählernummer",
            "Datum",
            "Neu eingebaut",
            "Zählerstand" ,
            "Kommentar"
        };
        datenanzeigeFeld.setModel(new DefaultTableModel(allData,headers));
    }

}
