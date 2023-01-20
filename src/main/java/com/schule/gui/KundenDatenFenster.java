package com.schule.gui;

import com.schule.data.Kunde;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
import java.awt.Container;
import java.util.List;

public class KundenDatenFenster extends JFrame{

    private JTable datenanzeigeFeld;
    private JScrollPane sp;
    private List<Kunde> dataList;
    private Builder request;

    public KundenDatenFenster(Builder builder){
        request = builder;
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
        Kunde a = dataList.get(index);
        new KundeAendernFenster(this,a);
    }

    public void update(){
        Response re = request.get();
        this.dataList = re.readEntity(new GenericType<List<Kunde>>() {
		});
        
        Object[][] allData = new Object[dataList.size()][7];
    
        for (int i = 0; i<dataList.size();i++) {
            Kunde curr = dataList.get(i);
            Object[] o = new Object[3];
            o[0]= curr.getId();
            o[1]= curr.getName();
            o[2]= curr.getVorname();
            allData[i]=o;
        }
    
        String[] headers = {
           "Kundennummer",
            "Name",
            "Vorname"
        };
        datenanzeigeFeld.setModel(new DefaultTableModel(allData,headers));
    }

    private void removeEntry(int index){
        Kunde curr = dataList.get(index);
        String s = "";
            
        s += "Kundennummer: " +  curr.getId() + "\n";
        s += "Name: " +  curr.getName() + "\n";
        s += "Vorname: " +  curr.getVorname() + "\n";


        int dialog = JOptionPane.showConfirmDialog(this, "Soll der Datensatz mit diesen Werten wirklich gelöscht werden?\n" + s,"Werte überprüfen",JOptionPane.YES_NO_OPTION);
        if (dialog == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(this, "Die Zählerdaten wurden nicht gelöscht.");
        } else{
            String url = "http://localhost:8080";
	        Client client = ClientBuilder.newClient();
	        WebTarget target = client.target(url);
            target.path("kunden".concat("/").concat(curr.getId().toString())).request()
            .accept(MediaType.APPLICATION_JSON).delete();
            //Ablesung result = re.readEntity(Ablesung.class);
        JOptionPane.showMessageDialog(this, "Die Zählerdaten wurden gelöscht.");
        update();
    }
    }

}
