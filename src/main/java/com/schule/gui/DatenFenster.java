package com.schule.gui;

import com.schule.data.Zaehlerdatum;
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
import java.awt.Container;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatenFenster extends JFrame{

    private JTable datenanzeigeFeld;
    private JScrollPane sp;
    private List<Zaehlerdatum> dataList;
    private ZaehlerDatenModel persistance = ZaehlerDatenModel.getInstance();
    public final static int NO_FILTER = -1;
    private int filter =-1;

    public DatenFenster(){
        init();
        this.dataList = persistance.getData();
    }

    public DatenFenster(int filter){
        this.filter = filter;
        filter();
        init();
    }


    private void filter(){
        this.dataList = persistance.getData();
        if(filter!=-1){
        List<Zaehlerdatum> dataListFilter = new ArrayList<>();
        for (Zaehlerdatum kdaten:dataList) {
            if(kdaten.getKundennummer() == filter){
                dataListFilter.add(kdaten);
            }
          }
        dataList = dataListFilter;
        }

    }

    private void init(){

        persistance = ZaehlerDatenModel.getInstance();
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
        filter();
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

    private void removeEntry(int index){
        if(index != -1){
        Zaehlerdatum curr = dataList.get(index);
        int indexPersistance = persistance.getIndex(curr);
        String s = "Kundennummer: " +  curr.getKundennummer() + "\n";
        s += "Zählerart: "+ curr.getZaehlerart() + "\n";
        s += "Zählernummer: " +  curr.getZaehlernummer() + "\n";
        s += "Datum: " +  curr.getDatum() + "\n";
        s += "Neu eingebaut: " +  curr.isEingebaut() + "\n";
        s += "Zählerstand: " +  curr.getZaehlerstand() + "\n";
        s += "Kommentar: " +  curr.getKommentar() + "\n";


        int dialog = JOptionPane.showConfirmDialog(this, "Soll der Datensatz mit diesen Werten wirklich gelöscht werden?\n" + s,"Werte überprüfen",JOptionPane.YES_NO_OPTION);
        if (dialog == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(this, "Die Zählerdaten wurden nicht gelöscht.");
        } else{
            persistance.removeEntry(indexPersistance);
            JOptionPane.showMessageDialog(this, "Die Zählerdaten wurden gelöscht.");
            update();
        }
    }
    }

}
