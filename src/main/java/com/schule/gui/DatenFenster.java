package com.schule.gui;

import com.schule.data.Ablesung;
import com.schule.data.Ablesung;
import com.schule.model.ZaehlerDatenModel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.text.DateFormat;
import java.time.LocalDate;
import java.awt.Container;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatenFenster extends JFrame{

    private JTable datenanzeigeFeld;
    private JScrollPane sp;
    private List<Ablesung> dataList;
    private ZaehlerDatenModel persistance;

    public DatenFenster(List<Ablesung> dataList){

        persistance = ZaehlerDatenModel.getInstance();
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
    JButton delete = new JButton("Löschen");
    con.add(delete,BorderLayout.SOUTH);


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
    datenanzeigeFeld.addKeyListener(new KeyAdapter(){
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode() == KeyEvent.VK_DELETE){
                removeEntry(datenanzeigeFeld.getSelectedRow());
            }
        }
    });
    delete.addActionListener(e->removeEntry(datenanzeigeFeld.getSelectedRow()));
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
            Ablesung curr = dataList.get(i);
            Object[] o = new Object[6];
            o[0]= curr.getKunde().getId();
            o[1]= curr.getZaehlernummer();
            o[3]= curr.isNeuEingebaut();
            o[4]= curr.getZaehlerstand();
            o[5]= curr.getKommentar();
            LocalDate date = curr.getDatum();
            if(date == null){
                o[2]= null;
            }else{
                o[2]= dateFormat.format(date);
            }
            allData[i]=o;
        }
    
        String[] headers = {
            "Zählernummer",
            "Datum",
            "Neu eingebaut",
            "Zählerstand" ,
            "Kommentar"
        };
        datenanzeigeFeld.setModel(new DefaultTableModel(allData,headers));
    }

    private void removeEntry(int index){
        Ablesung curr = persistance.getEntry(index);
        String s = "Zählernummer: " +  curr.getZaehlernummer() + "\n";
        s += "Datum: " +  curr.getDatum() + "\n";
        s += "Neu eingebaut: " +  curr.isNeuEingebaut() + "\n";
        s += "Zählerstand: " +  curr.getZaehlerstand() + "\n";
        s += "Kommentar: " +  curr.getKommentar() + "\n";


        int dialog = JOptionPane.showConfirmDialog(this, "Soll der Datensatz mit diesen Werten wirklich gelöscht werden?\n" + s,"Werte überprüfen",JOptionPane.YES_NO_OPTION);
        if (dialog == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(this, "Die Zählerdaten wurden nicht gelöscht.");
        } else{
        persistance.removeEntry(index);
        JOptionPane.showMessageDialog(this, "Die Zählerdaten wurden gelöscht.");
        update();
    }
    }

}
