package com.schule.gui;

import com.schule.data.Ablesung;

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
import java.time.LocalDate;
import java.awt.Container;
import java.util.List;

public class DatenFenster extends JFrame{

    private JTable datenanzeigeFeld;
    private JScrollPane sp;
    private List<Ablesung> dataList;
    private Builder request;

    public DatenFenster(Builder builder){
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
        Ablesung a = dataList.get(index);
        if(a.getKunde()!=null){
            new ZaehlerAenderungsFormular(a,this);
        }else{
            JOptionPane.showMessageDialog(this, "Die Zählerdaten können nicht verändertwerden, da kein Kunde vorhanden ist.");
        }
    }

    public void update(){
        Response re = request.get();
        Object[][] allData = new Object[0][6];
        if(re.getStatus()==200){
        this.dataList = re.readEntity(new GenericType<List<Ablesung>>() {
		});
        
        allData = new Object[dataList.size()][6];
    
        for (int i = 0; i<dataList.size();i++) {
            Ablesung curr = dataList.get(i);
            Object[] o = new Object[6];
            if(curr.getKunde()!=null){
                o[0]= curr.getKunde().getName() + ", " + curr.getKunde().getVorname();
            }else{
                o[0]= null;
            }
            o[1]= curr.getZaehlernummer();
            o[3]= curr.isNeuEingebaut();
            o[4]= curr.getZaehlerstand();
            o[5]= curr.getKommentar();
            LocalDate date = curr.getDatum();
            if(date == null){
                o[2]= null;
            }else{
                o[2]= date.toString();
            }
            allData[i]=o;
            }
        }
        String[] headers = {
            "Name, Vorname",
             "Zählernummer",
             "Datum",
             "Neu eingebaut",
             "Zählerstand" ,
             "Kommentar"
         };
         datenanzeigeFeld.setModel(new DefaultTableModel(allData,headers));
    }

    private void removeEntry(int index){
        Ablesung curr = dataList.get(index);
        String s = "";
        if(curr.getKunde() != null){
            s += "Kundennummer: " +  curr.getKunde().getId() + "\n";
        }else{
            s += "Kundennummer: \n";
        }
        s += "Zählernummer: " +  curr.getZaehlernummer() + "\n";
        s += "Datum: " +  curr.getDatum() + "\n";
        s += "Neu eingebaut: " +  curr.isNeuEingebaut() + "\n";
        s += "Zählerstand: " +  curr.getZaehlerstand() + "\n";
        s += "Kommentar: " +  curr.getKommentar() + "\n";


        int dialog = JOptionPane.showConfirmDialog(this, "Soll der Datensatz mit diesen Werten wirklich gelöscht werden?\n" + s,"Werte überprüfen",JOptionPane.YES_NO_OPTION);
        if (dialog == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(this, "Die Zählerdaten wurden nicht gelöscht.");
        } else{
            String url = "http://localhost:8080";
	        Client client = ClientBuilder.newClient();
	        WebTarget target = client.target(url);
            target.path("ablesungen".concat("/").concat(curr.getId().toString())).request()
            .accept(MediaType.APPLICATION_JSON).delete();
            //Ablesung result = re.readEntity(Ablesung.class);
        JOptionPane.showMessageDialog(this, "Die Zählerdaten wurden gelöscht.");
        dataList.remove(index);
        update();
    }
    }
    }